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

import org.olanto.smt.configStateCommons.exceptions.ConnectionException;
import org.olanto.smt.configStateCommons.exceptions.ServiceOperationException;
import org.olanto.smt.configStateCommons.interfaces.IConfigState;
import org.olanto.smt.configStateCommons.interfaces.IService;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Ce daemon s'occupe de redémarrer les services morts d'un Node.
 */
public class NodeStarter extends NodeDaemon {

    public NodeStarter(IConfigState configState, String ip) {
        super(configState, ip, Config.DAEMON_SLEEP_TEST);
        this.setName("NodeStarter[" + ip + "]");
    }

    @Override
    protected void process() throws RemoteException {
        if (configState.isNodeReachable(ip)) {
            System.out.println(this + "Searching services to (re)start...");
            Collection<IService> services = configState.getServiceList(ip);
            for (IService service : services) {
                // On start uniquement les services marqués comme down
                if (!service.isResponding()) {
                    try {
                        if (Master.testService(service)) {
                            System.out.println(service.toMinString() + " is marked as down but is UP.");
                            service.setResponding(true);
                        } else {
                            System.out.println(service.toMinString() + " is down ! Try to restart it...");
                            Master.startService(service);
                            // if no error, re-test it
                            service.restartSocket();
                            boolean test = Master.testService(service);
                            service.setResponding(test);
                            if (test) {
                                System.out.println("The service was successfully started. " + service.toMaxString());
                            } else {
                                System.out.println("Starting FAILED." + service.toMaxString());
                            }
                        }
                    } catch (ConnectionException ex) {
                        System.err.println("Starting " + service.toMinString() + " FAILED. "
                                + "Message : " + ex.getMessage());
                        service.setResponding(false);
                    } catch (ServiceOperationException ex) {
                        System.err.println("Starting " + service.toMinString() + " FAILED. "
                                + "Message : " + ex.getMessage());
                        service.setResponding(false);
                    }
                }
            } // end for
        } else {
            System.out.println(this + "Node down !");
        }

    }
}
