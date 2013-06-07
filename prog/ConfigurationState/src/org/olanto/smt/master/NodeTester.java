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

import org.olanto.smt.configStateCommons.interfaces.IConfigState;
import org.olanto.smt.configStateCommons.interfaces.IService;

import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Ce daemon s'occupe de tester un Node et tous ses services.
  */
public class NodeTester extends NodeDaemon {

    public NodeTester(IConfigState configState, String ip) {
        super(configState, ip, Config.DAEMON_SLEEP_TEST);
        this.setName("NodeTester[" + ip + "]");
    }

    @Override
    protected void process() throws RemoteException {
        // 1 -- test the node
        boolean nodeOK = Master.testLocalMaster(ip);
        configState.setNodeReachable(ip, nodeOK);

        // 2 -- test services
        Collection<IService> services = configState.getServiceList(ip);
        if (nodeOK) {
            System.out.println(this + "Testing services...");
            for (IService service : services) {
                boolean serviceOK = Master.testService(service);
                service.setResponding(serviceOK);
                if (serviceOK) {
                    System.out.println(this + "UP : " + service.toMaxString());
                } else {
                    System.out.println(this + "DOWN : " + service.toMaxString());
                }
            }
        } else {
            System.out.println(this + "Node down ! All the services are marked " +
                    "as down.");
            for (IService service : services) {
                service.setResponding(false);
            }
        }
    }
}
