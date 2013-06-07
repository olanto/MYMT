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

import org.olanto.smt.configurationState.ConfigStateServer;
import java.rmi.Naming;
import org.olanto.convsrv.server.ConvertService_BASIC;


/**
 * Cette classe s'occupe de lancer tous les serveur.
 */
public class DeployServers {

/* 
-Djava.rmi.server.codebase="file:///C:/MYMT/prog/ConfigurationState/dist/ConfigurationState.jar" -Djava.security.policy="file:///C:/MYMT/prog/ConfigurationState/rmi.policy"
 * start master & converter server
 */

    public static void main(String[] args) {

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

        ConfigStateServer.start();
        Master.start();
}
}
