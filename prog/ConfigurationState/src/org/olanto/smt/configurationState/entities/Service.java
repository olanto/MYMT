/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myMT.

   myMT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myMT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myMT.  If not, see <http://www.gnu.org/licenses/>.

**********/
package org.olanto.smt.configurationState.entities;

import org.olanto.smt.configStateCommons.exceptions.ConnectionException;
import org.olanto.smt.configStateCommons.interfaces.IService;
import static org.olanto.smt.configStateCommons.Constants.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cette classe contient la description statique et dynamique d'un service. La
 * partie statique vient du fichier de configuration, la partie dynamique est
 * accessible en RMI.
 */
public class Service extends UnicastRemoteObject implements IService {

    // final part :
    protected final String ip;
    protected final int port;
    protected final String deamon;
    protected final String corpus;
    protected final String source;
    protected final String target;

    protected final long startTime;

    // dynamic part :
    protected boolean responding;

    // statistic part :
    protected long nbSentenceTranslated;
    protected long nbCharTranslated;
    protected long nbTestOk;
    protected long nbTestFail;
    protected long lastTimeOk;

    protected long translatingTime;
    protected double charPerSecond;
    protected double usedRatio;

    private transient BufferedReader br = null;
    private transient PrintWriter pw = null;
    private transient Socket socket = null;
    protected transient boolean restartSocket;

    public Service(String ip, int port, String corpus, String source, String target, String daemon) throws RemoteException {
        this.ip = ip;
        this.port = port;
        this.corpus = corpus;
        this.source = source;
        this.target = target;
        //TODO simple exemple...
        this.deamon = daemon ; // "daemon.pl"; //this.deamon = "daemon_" + source + "_" + target + ".pl ";
        this.startTime = System.currentTimeMillis();

        this.responding = false;

        //init values
        this.nbSentenceTranslated = 0;
        this.nbCharTranslated = 0;
        this.nbTestOk = 0;
        this.nbTestFail = 0;
        this.lastTimeOk = 0;
        this.translatingTime = 0;
        this.charPerSecond = 0;
        this.usedRatio = 0;

        this.restartSocket = false;
    }

    // ----- GET & SET ------
    public String getDeamon() throws RemoteException {
        return deamon;
    }

    public String getIp() throws RemoteException {
        return ip;
    }

    public int getPort() throws RemoteException {
        return port;
    }

    public String getCorpus() throws RemoteException {
        return corpus;
    }

    public String getSource() throws RemoteException {
        return source;
    }

    public String getTarget() throws RemoteException {
        return target;
    }

    public long getNbTestOk() throws RemoteException {
        return nbTestOk;
    }

    public long getNbTestFail() throws RemoteException {
        return nbTestFail;
    }

    public long getLastTimeOk() throws RemoteException {
        return lastTimeOk;
    }

    public long getNbCharTranslated() throws RemoteException {
        return nbCharTranslated;
    }

    public long getNbSentenceTranslated() throws RemoteException {
        return nbSentenceTranslated;
    }

    public double getCharPerSecond() throws RemoteException {
        return charPerSecond;
    }

    public double getUsedRatio() throws RemoteException {
        return usedRatio;
    }

    public boolean isResponding() throws RemoteException {
        return responding;
    }

    public synchronized void setResponding(boolean responding) throws RemoteException {
        this.responding = responding;
        if (responding) {
            nbTestOk++;
            lastTimeOk = System.currentTimeMillis();
        } else {
            nbTestFail++;
        }
    }

    public synchronized void restartSocket() throws RemoteException {
        this.restartSocket = true;
    }

    /**
     * Translate the given sentence. 
     * @param input the sentence to translate.
     * @return the translated sentence.
     * @throws RemoteException if an RMI error occurs.
     * @throws ConnectionException if the service is not reachable.
     */
    public synchronized String translate(String input) throws RemoteException, ConnectionException {
        try {
            if (socket == null || restartSocket) {
                this.close();
                socket = new Socket(ip, port);
                socket.setSoTimeout(REQUEST_TIMEOUT);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                restartSocket = false;
            }

            String response = null;

            //TODO comment mieux gérer ce problème ? ... d'ou il vient ?
            // empty the input stream
            while(br.ready() && (response = br.readLine()) != null){}

            // send the sentence
            final long start = System.currentTimeMillis();
            pw.println(input); // with auto-flush

            // get the response
            response = br.readLine();
            if (response == null) {
                throw new ConnectionException("Connection down.");
            }

            // update stats :
            final long time = System.currentTimeMillis() - start;
            this.nbCharTranslated += input.toCharArray().length;
            this.nbSentenceTranslated++;
            this.translatingTime += time;
            this.charPerSecond = 1000d * (double)nbCharTranslated / (double)translatingTime;
            this.usedRatio = (double)translatingTime / (double)(System.currentTimeMillis()-startTime);

            return response;
        } catch (UnknownHostException ex) {
            throw new ConnectionException(ex.getMessage());
        } catch (SocketTimeoutException ex) {
            throw new ConnectionException("Connexion timeout.");
        } catch (IOException ex) {
            throw new ConnectionException(ex.getMessage());
        }
    }

    public synchronized String toMinString() throws RemoteException {
        return "Service[" + ip + ":" + port + " Corpus=" + corpus + "|" + source + "-->" + target + "]";
    }

    public synchronized String toMaxString() throws RemoteException {
        return this.toString();
    }

    public synchronized double getTestRatio() {
        return (double)(nbTestOk)/(double)(nbTestOk + nbTestFail);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Service other = (Service) obj;
        if ((this.ip == null) ? (other.ip != null) : !this.ip.equals(other.ip)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        if ((this.deamon == null) ? (other.deamon != null) : !this.deamon.equals(other.deamon)) {
            return false;
        }
        if ((this.corpus == null) ? (other.corpus != null) : !this.corpus.equals(other.corpus)) {
            return false;
        }
        if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
            return false;
        }
        if ((this.target == null) ? (other.target != null) : !this.target.equals(other.target)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.ip != null ? this.ip.hashCode() : 0);
        hash = 17 * hash + this.port;
        hash = 17 * hash + (this.deamon != null ? this.deamon.hashCode() : 0);
        hash = 17 * hash + (this.corpus != null ? this.corpus.hashCode() : 0);
        hash = 17 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 17 * hash + (this.target != null ? this.target.hashCode() : 0);
        hash = 17 * hash + (this.responding ? 1 : 0);
        hash = 17 * hash + (int) (this.nbTestOk ^ (this.nbTestOk >>> 32));
        hash = 17 * hash + (int) (this.nbTestFail ^ (this.nbTestFail >>> 32));
        hash = 17 * hash + (int) (this.lastTimeOk ^ (this.lastTimeOk >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return (responding ? "(1)" : "(0)") +
                "Service[" + ip + ":" + port + " Corpus=" + corpus + "|" + source + "-->" + target + "]" +
                "lastOK=" + new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date(lastTimeOk)) +
                ", up at " + new DecimalFormat("0.00%").format(getTestRatio());
    }

    private void close() throws IOException {
        if (br != null) {br.close(); br = null;}
        if (pw != null) {pw.close(); pw = null;}
        if (socket != null) {socket.close(); socket = null;}
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }

}
