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

import org.olanto.smt.configStateCommons.exceptions.ConnectionException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour un service de traduction. Utilisée pour le RMI.
 */
public interface IService extends Remote {

    public String getDeamon() throws RemoteException;
    public String getIp() throws RemoteException;
    public int getPort() throws RemoteException;
    public String getCorpus() throws RemoteException;
    public String getSource() throws RemoteException;
    public String getTarget() throws RemoteException;

    public void restartSocket() throws RemoteException;
    
    public boolean isResponding() throws RemoteException;
    public void setResponding(boolean responding) throws RemoteException;


    public String translate(String input) throws RemoteException, ConnectionException;

    public long getNbCharTranslated() throws RemoteException;
    public long getNbSentenceTranslated() throws RemoteException;
    public long getNbTestOk() throws RemoteException;
    public long getNbTestFail() throws RemoteException;
    public long getLastTimeOk() throws RemoteException;
    public double getCharPerSecond() throws RemoteException;
    public double getUsedRatio() throws RemoteException;
    public double getTestRatio() throws RemoteException;

    public String toMinString() throws RemoteException;
    public String toMaxString() throws RemoteException;
}
