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
package org.olanto.smt.monitor.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.olanto.smt.configStateCommons.interfaces.IConfigState;
import org.olanto.smt.configStateCommons.interfaces.IService;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import org.olanto.smt.monitor.client.model.Service;

import org.olanto.smt.monitor.client.rpc.MonitorService;
import org.olanto.smt.monitor.client.rpc.RMIException;
import static org.olanto.smt.translator.Config.*;

/**
 *
 */
public class MonitorServiceImpl extends RemoteServiceServlet implements MonitorService {

    private IConfigState configState;
    
    public MonitorServiceImpl() {
        try {
            
            Remote remote = Naming.lookup(RMI_SERVER_SMT);
            if (remote instanceof IConfigState) {
                configState = (IConfigState) remote;
            } else {
                new RemoteException("The object is not an instance of IConfigState.");
            }
        } catch (NotBoundException ex) {
            System.err.println("The configState is not linked : " + ex.getMessage());
        } catch (MalformedURLException ex) {
            System.err.println("URL malformed : " + ex.getMessage());
        } catch (RemoteException ex) {
            System.err.println("An RMI error occurs during the Monitor Service "
                    + "initialization. Message : " + ex.getMessage());
        }
    }

    public String[] getNodeList() throws RMIException {
        try {
            return configState.getNodeList();
        } catch (RemoteException ex) {
            throw new RMIException();
        }
    }

    public boolean isNodeReachable(String ip) throws RMIException {
        try {
            return configState.isNodeReachable(ip);
        } catch (RemoteException ex) {
            throw new RMIException();
        }
    }

    public Collection<Service> getServices(String ip) throws RMIException {
        try {
            Collection<IService> services = configState.getServiceList(ip);
            Collection<Service> servicesStatic  = new HashSet<Service>();

            for (IService service : services) {
                servicesStatic.add(new Service(service.getIp(), service.getPort(),
                        service.getDeamon(), service.getCorpus(), service.getSource(),
                        service.getTarget(), service.getNbSentenceTranslated(),
                        service.getNbCharTranslated(),
                        service.getLastTimeOk(),
                        service.getCharPerSecond(),
                        service.getUsedRatio(),
                        service.getTestRatio(),
                        service.isResponding()));
            }

            return servicesStatic;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RMIException();
        }
    }

    public Collection<Service> getAllServices() throws RMIException {
        try {
            Collection<Service> servicesGWT  = new HashSet<Service>();

            String[] ips = configState.getNodeList();

            for (String ip : ips) {
                Collection<IService> servicesRMI = configState.getServiceList(ip);

                for (IService service : servicesRMI) {
                    servicesGWT.add(new Service(service.getIp(), service.getPort(),
                            service.getDeamon(), service.getCorpus(), service.getSource(),
                            service.getTarget(), service.getNbSentenceTranslated(),
                            service.getNbCharTranslated(),
                            service.getLastTimeOk(),
                            service.getCharPerSecond(),
                            service.getUsedRatio(),
                            service.getTestRatio(),
                            service.isResponding()));
                }

            }

            return servicesGWT;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RMIException();
        }

    }
    
    { // init config
        ReadParameters();
    }
}
