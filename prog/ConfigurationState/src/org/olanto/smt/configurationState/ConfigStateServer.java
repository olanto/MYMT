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
package org.olanto.smt.configurationState;

import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import org.olanto.convsrv.server.ConvertService_BASIC;
import org.olanto.util.smt.utilsmt.SenseOS;
import org.xml.sax.SAXException;

/**
 * Cette classe gère le server RMI pour la classe ConfigState.
 * Elle instancie la classe ConfigState avec le fichier de configuration.
 */
public class ConfigStateServer {

    public static void main(String[] args) {
        start();
    }

        public static void start(){

        try {  // start rmi services
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("RMI registry is probably running ...");
            //e.printStackTrace();
        }
        
        try {
            File configFile = new File(SenseOS.getMYMT_HOME()+"/config/configState.xml");
            ConfigState configState = new ConfigState(configFile);


            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }

            System.out.println("wait for rmi...");
            Naming.rebind("rmi://localhost/configState", configState);
            System.out.println("ConfigState server running !");

        } catch (RemoteException ex) {
            System.err.println("An error occurs during the RMI server "
                    + "initialization. Message : " + ex.getMessage());
        } catch (SAXException ex) {
            System.err.println("Parsing failed. Message : " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("An IO error occurs during the RMI server "
                    + "initialization. Message : " + ex.getMessage());
        }
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("RMI registry is probably running ...");
            //e.printStackTrace();
        }

               try {
            System.out.println("initialisation du convertisseur ...");

            ConvertService_BASIC idxobj = new ConvertService_BASIC();

            System.out.println("Enregistrement du serveur");

            String name = "rmi://localhost/CONVSRV";
            System.out.println("name:" + name);
            Naming.rebind(name, idxobj);
            System.out.println("Server is ready");

        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
