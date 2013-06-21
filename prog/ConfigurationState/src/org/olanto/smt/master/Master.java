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
package org.olanto.smt.master;

import org.olanto.smt.configStateCommons.Constants;
import org.olanto.smt.configStateCommons.exceptions.ConnectionException;
import org.olanto.smt.configStateCommons.exceptions.ServiceOperationException;
import org.olanto.smt.configStateCommons.interfaces.IConfigState;
import org.olanto.smt.configStateCommons.interfaces.IService;
import static org.olanto.smt.configStateCommons.Constants.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.util.smt.utilsmt.SenseOS;

/**
 * Cette classe s'occupe de lancer tous les daemons. Elle contient toutes les
 * méthodes de test, ainsi que les opération sur les services.
 */
public class Master {

    private IConfigState configState;

    public Master(String configFilePath) throws NotBoundException, MalformedURLException, RemoteException, IOException {

        Config.loadConfig(new File(configFilePath));

        Remote remote = Naming.lookup("rmi://" + Config.RMI_HOST + "/configState");
        if (remote instanceof IConfigState) {
            configState = (IConfigState) remote;
        } else {
            new RemoteException("The object is not an instance of IConfigState.");
        }

        System.out.println("Launching the daemons...");
        // each node is tested by two separated threads
        String[] IPs = configState.getNodeList();
        for (String ip : IPs) {
            new NodeTester(configState, ip).start();
        }

        // wait 10s to setup the configuration
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }

        // start the services
        for (String ip : IPs) {
            new NodeStarter(configState, ip).start();
        }
    }

    /**
     * Test the local master specified by it's ip address. This method sends
     * a test request to the local master and checks if the response is correct.
     * @param ip the ip address of the local master
     * @return true if the local master is responding, false else.
     */
    protected static boolean testLocalMaster(String ip) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            Socket socket = new Socket(ip, LOCAL_MASTER_PORT);
            socket.setSoTimeout(Config.LOCAL_MASTER_TIMEOUT);

            ois = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            // Request type
            oos.writeByte(REQUEST_TYPE_TEST);
            oos.flush();

            return ois.readUnsignedByte() == RESPONSE_TYPE_OK;

        } catch (IOException ex) {
            return false;
        } finally {
            if (ois != null) try {ois.close();} catch (IOException e) {e.printStackTrace();}
            if (oos != null) try {oos.close();} catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Try to start the specified service.
     * @param service the service to start.
     * @throws ConnectionException if the Local Master is not reachable, or if
     *  there is an IO error.
     * @throws ServiceOperationException if the launching failed.
     * @throws RemoteException if an RMI error occurs.
     */
    protected static void startService(IService service) throws ConnectionException, ServiceOperationException, RemoteException {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            Socket socket = new Socket(service.getIp(), LOCAL_MASTER_PORT);
            socket.setSoTimeout(Constants.REQUEST_TIMEOUT);

            ois = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            // Request type
            oos.writeByte(REQUEST_TYPE_START);

            // write all service informations
            oos.writeObject(service.getDeamon());
            oos.writeObject(service.getIp());
            oos.writeObject(new Integer(service.getPort()));
            oos.flush();

            // read the response type
            int type = ois.readUnsignedByte();
            switch (type) {
                case RESPONSE_TYPE_OK:
                    System.out.println(service.toMinString() + " starting... ");
                    Thread.sleep(Config.START_TIME);
                    break;

                case RESPONSE_TYPE_ERROR_OP:
                    throw new ServiceOperationException((String) ois.readObject());

                case RESPONSE_TYPE_ERROR_COMM:
                    throw new ConnectionException((String) ois.readObject());

                default:
                    throw new ConnectionException("Response type unknown : type=" + type);
            }
        } catch (InterruptedException ex) {
            throw new ServiceOperationException("The service may do not have enought time to start.");
        } catch (ClassNotFoundException ex) {
            throw new ConnectionException("The response is malformed for the service " + service.toMinString());
        } catch (SocketTimeoutException ex) {
            throw new ConnectionException("Connexion timeout with the Local Master : " + service.toMinString());
        } catch (IOException ex) {
            throw new ConnectionException(service.toMinString() + " : " + ex.getMessage());
        } finally {
            if (ois != null) try {ois.close();} catch (IOException e) {e.printStackTrace();}
            if (oos != null) try {oos.close();} catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Try to stop the specified service.
     * @param service the service to stop.
     * @throws ConnectionException if the Local Master is not reachable, or if
     *  there is an IO error.
     * @throws ServiceOperationException if the stopping failed.
     * @throws RemoteException if an RMI error occurs.
     */
    protected static void stopService(IService service) throws ConnectionException, ServiceOperationException, RemoteException {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            Socket socket = new Socket(service.getIp(), LOCAL_MASTER_PORT);
            socket.setSoTimeout(Constants.REQUEST_TIMEOUT);

            ois = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            // Request type
            oos.writeByte(REQUEST_TYPE_STOP);

            // write the port number
            oos.writeObject(new Integer(service.getPort()));
            oos.flush();

            // read the response type
            int type = ois.readUnsignedByte();
            switch (type) {
                case RESPONSE_TYPE_OK:
                    System.out.println(service.toMinString() + " stopping... ");
                    Thread.sleep(Config.STOP_TIME);
                    break;

                case RESPONSE_TYPE_ERROR_OP:
                    throw new ServiceOperationException((String) ois.readObject());

                case RESPONSE_TYPE_ERROR_COMM:
                    throw new ConnectionException((String) ois.readObject());

                default:
                    throw new ConnectionException("Response type unknown : type=" + type);
            }
        } catch (InterruptedException ex) {
            throw new ServiceOperationException("The service may do not have enought time to stop.");
        } catch (ClassNotFoundException ex) {
            throw new ConnectionException("The response is malformed for the service " + service.toMinString());
        } catch (SocketTimeoutException ex) {
            throw new ConnectionException("Connexion timeout with the Local Master : " + service.toMinString());
        } catch (IOException ex) {
            throw new ConnectionException(service.toMinString() + " : " + ex.getMessage());
        } finally {
            if (ois != null) try {ois.close();} catch (IOException e) {e.printStackTrace();}
            if (oos != null) try {oos.close();} catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Test the specified service. 
     * @param service the service to test.
     * @return true if the service works well, false else.
     * @throws RemoteException if an RMI error occurs.
     */
    protected static boolean testService(IService service) throws RemoteException {
        try {
            return Config.TEST_OUTPUT_STRING.equals(service.translate(Config.TEST_INPUT_STRING));
        } catch (ConnectionException ex) {
            return false;
        }
    }

    public static void main(String[] args) {
        start();
    }

       public static void start() {
         //  String conf="C:/MYMT/config/Master.properties";
         String conf=SenseOS.getMYMT_HOME()+"/config/Master.properties";
        try {
            new Master(conf);
        } catch (NotBoundException ex) {
            System.err.println("The configState is not linked : " + ex.getMessage());
        } catch (MalformedURLException ex) {
            System.err.println("URL malformed : " + ex.getMessage());
        } catch (RemoteException ex) {
            System.err.println("An RMI error occurs during the Master "
                    + "initialization. Message : " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("An errors occurs when loading the configuration " +
                    "file. Message : " + ex.getMessage());
        }
    }
}
