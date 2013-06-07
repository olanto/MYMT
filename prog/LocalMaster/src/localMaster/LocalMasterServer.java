/**********
    Copyright © 2010-2013 Olanto Foundation Geneva

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

package localMaster;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import static org.olanto.smt.configStateCommons.Constants.*;
import org.olanto.smt.configStateCommons.exceptions.CommunicationException;
import org.olanto.smt.configStateCommons.exceptions.ServiceOperationException;

/**
 * Cette classe s'occupe de la partie serveur du local master.
 * Elle prend en charge les requêtes du Master et les transmet au local master
 * après parsing.
 * @author Lomig Mégard
 */
public class LocalMasterServer implements Runnable {

    private final LocalMaster lm;
    private final ServerSocket serverSocket;

    public LocalMasterServer(int port) throws IOException {
        lm = new LocalMaster();
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        while (true) {
            try {
                // New connexion with the Master
                Socket socket = serverSocket.accept();
                System.out.println("Get a new connection ! " + socket);

                oos = new ObjectOutputStream(
                        new BufferedOutputStream(socket.getOutputStream()));
                oos.flush();
                ois = new ObjectInputStream(
                        new BufferedInputStream(socket.getInputStream()));

                try {
                    // read the request TYPE :
                    int type = ois.readUnsignedByte();
                    switch (type) {
                        case REQUEST_TYPE_START:
                            try {
                                // read the service informations
                                String daemon = (String) ois.readObject();
                                String ip = (String) ois.readObject();
                                int port = (Integer) ois.readObject();
                                
                                System.out.println("Request a start with port " + port + ".");

                                // run the command
                                lm.startRestart(daemon, ip, port);
                            } catch (ClassNotFoundException ex) {
                                throw new CommunicationException("The request is malformed. "
                                        + ex.getMessage());
                            } catch (IOException ex) {
                                throw new ServiceOperationException("Error during the command execution. "
                                        + ex.getMessage());
                            }
                            break;

                        case REQUEST_TYPE_STOP:
                            try {
                                // read the service object
                                int port = (Integer) ois.readObject();
                                System.out.println("Request a stop with port " + port + ".");

                                lm.stop(port);
                            } catch (ClassNotFoundException ex) {
                                throw new CommunicationException("The request is malformed. "
                                        + ex.getMessage());
                            }
                            break;

                        case REQUEST_TYPE_TEST:
                            System.out.println("Request a test");
                            break;

                        default:
                            throw new CommunicationException("The request type is unknown : "
                                    + "type=" + type);
                    } // end switch

                    // If all went well
                    oos.writeByte(RESPONSE_TYPE_OK);
                    oos.flush();

                } catch (CommunicationException ex) {
                    System.err.println("An error occurs during request handling. "
                            + "Message : " + ex.getMessage());

                    // send in response the error message
                    oos.writeByte(RESPONSE_TYPE_ERROR_COMM);
                    oos.writeObject(ex.getMessage());
                    oos.flush();
                } catch (ServiceOperationException ex) {
                    System.err.println("An error occurs during request handling. "
                            + "Message : " + ex.getMessage());

                    // send in response the error message
                    oos.writeByte(RESPONSE_TYPE_ERROR_OP);
                    oos.writeObject(ex.getMessage());
                    oos.flush();
                }

            } catch (IOException ex) {
                System.err.println("An error occurs during accepting a new request. "
                        + "Message : " + ex.getMessage());
            } finally {
                if (ois != null) try {ois.close();} catch (IOException e) {e.printStackTrace();}
                if (oos != null) try {oos.close();} catch (IOException e) {e.printStackTrace();}
            }
        } // end while
        
    }

    public static void main(String[] args) {
        try {
            Config.loadConfig(LocalMasterServer.class.getClassLoader().getResourceAsStream("localmasterConf.properties"));
            LocalMasterServer lmServer = new LocalMasterServer(LOCAL_MASTER_PORT);
            new Thread(lmServer).start();
        } catch (IOException ex) {
            System.err.println("An error occurs during server initialization. " +
                    "Message : " + ex.getMessage());
        }
    }
}
