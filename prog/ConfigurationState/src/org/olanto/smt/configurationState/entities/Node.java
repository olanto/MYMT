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

import org.olanto.smt.configStateCommons.interfaces.IService;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Cette classe représente un noeud (une adresse ip) qui contient plusieurs
 * services.
 */
public class Node {
    private final String ip;
    private boolean reachable;

    //TODO modifier en List ? --> changer le parsing
    private Map<Integer, Service> services;

    public Node(String ip) {
        this.ip = ip;
        services = new HashMap<Integer, Service>();

        reachable = false;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public Collection<IService> getServiceList() {
        //TODO utiliser plutôt une liste ?
        Set<IService> set = new HashSet<IService>();
        for(Entry<Integer, Service> entry : services.entrySet()) {
            set.add(entry.getValue());
        }
        return set;
    }

    public Collection<IService> getServices(String corpus, String src, String target) {
        //TODO utiliser plutôt une liste ?
        Collection<IService> matchingServices = new HashSet<IService>();
        for(Entry<Integer, Service> entry : services.entrySet()) {
            Service service = entry.getValue();
            if (service.responding &&
                    service.corpus.equals(corpus) &&
                    service.source.equals(src)    &&
                    service.target.equals(target) ) {
                matchingServices.add(service);
            }
        }
        return matchingServices;
    }

    public Service getService(int port) {
        return services.get(port);
    }

    public void addService(int port, Service service) {
        services.put(port, service);
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Node[").append(ip).append("]");
        out.append((reachable ? "LM ok" : "LM FAILURE")).append("\n");
        for(Entry<Integer, Service> entry : services.entrySet()) {
            out.append("  ").append(entry.getValue()).append("\n");
        }
        return out.toString();
    }

}
