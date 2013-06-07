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
import java.rmi.RemoteException;

/**
 * Classe abstraite qui permet de faire un daemon qui éxécute une action en
 * boucle sur un Node.
 */
public abstract class NodeDaemon extends Thread {

    protected final IConfigState configState;

    protected final String ip;
    protected final long sleepTime;

    public NodeDaemon(IConfigState configState, String ip, long sleepTime) {
        this.configState = configState;
        this.ip = ip;
        this.sleepTime = sleepTime;
    }

    @Override
    final public void run() {
        while (true) {
            try {
                this.process();
                System.out.println(this + "Sleeping...");
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                System.err.println(this + " has been awakened during sleeping.");
            } catch (RemoteException ex) {
                //TODO **** gérer boucle infinie *** !!!
                System.err.println(this + "RMI error. Message : " + ex.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * This method is called by the run every sleepTime ms.
     * @throws RemoteException if an RMI error occurs.
     */
    abstract protected void process() throws RemoteException;
}
