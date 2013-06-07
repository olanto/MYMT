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

import org.olanto.smt.configurationState.entities.Node;
import org.olanto.smt.configStateCommons.interfaces.IConfigState;
import org.olanto.smt.configStateCommons.interfaces.IService;
import org.olanto.smt.configurationState.parser.ConfigParser;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xml.sax.SAXException;

/**
 * Cette classe contient et gère la BDD des services.
  */
public class ConfigState extends UnicastRemoteObject implements IConfigState {

    private Map<String, Node> nodes;

    public ConfigState(File configFile) throws RemoteException, SAXException, IOException {
        ConfigParser parser = new ConfigParser();
        nodes = parser.parse(configFile);
        System.out.println("Configuration parsed :");
        System.out.println(this);
    }

    public synchronized String[] getNodeList() throws RemoteException {
        Set<String> set = nodes.keySet();
        return set.toArray(new String[set.size()]);
    }

    public synchronized boolean isNodeReachable(String ip) throws RemoteException {
        Node node = nodes.get(ip);
        if (node != null) {
            return node.isReachable();
        } else {
            //TODO exception ??
            return false;
        }
    }

    public synchronized void setNodeReachable(String ip, boolean reachable) throws RemoteException {
        Node node = nodes.get(ip);
        if (node != null) {
            node.setReachable(reachable);
        } else {
            //TODO exception ??
        }
    }

    public synchronized Collection<IService> getServiceList(String ip) throws RemoteException {
        Node node = nodes.get(ip);
        if (node != null) {
            return node.getServiceList();
        } else {
            return null;
        }
    }

    public synchronized Collection<IService> getServices(String corpus, String src, String target) throws RemoteException {
        Collection<IService> matchingServices = new HashSet<IService>();
        for(Entry<String, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            matchingServices.addAll(node.getServices(corpus, src, target));
        }
        return matchingServices;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        for(Entry<String, Node> entry : nodes.entrySet()) {
            out.append(entry.getValue()).append("\n");
        }
        return out.toString();
    }

}
