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
package org.olanto.smt.configStateCommons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Interface pour le ConfigurationState. Utilisée avec le RMI.
 */
public interface IConfigState extends Remote {

    /**
     * Return the list of all nodes (by their ip).
     * @return the ip of each node.
     * @throws RemoteException if an RMI error occurs.
     */
    public String[] getNodeList() throws RemoteException;

    /**
     * Return the reachable mark of a node. If the method returns <code>true</code>,
     * the node is marked as reachable but no test is done and it could be not
     * reachable.
     * @param ip the node ip.
     * @return true if the node is marked as reachable, false else.
     * @throws RemoteException if an RMI error occurs.
     */
    public boolean isNodeReachable(String ip) throws RemoteException;
    public void setNodeReachable(String ip, boolean reachable) throws RemoteException;

    /**
     * Return the collection of all the services in the node specified by it's ip.
     * @param ip the node ip.
     * @return all the services in the node.
     * @throws RemoteException if an RMI error occurs.
     */
    public Collection<IService> getServiceList(String ip) throws RemoteException;

    /**
     * Return the services that match the specified corpus, source language and
     * target language.
     * @param corpus the corpus.
     * @param src the source language.
     * @param target the target language
     * @return the services that match the corpus and the source and target language.
     * @throws RemoteException if an RMI error occurs.
     */
    public Collection<IService> getServices(String corpus, String src, String target) throws RemoteException;

}
