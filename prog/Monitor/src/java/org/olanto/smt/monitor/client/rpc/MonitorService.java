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
package org.olanto.smt.monitor.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Collection;
import org.olanto.smt.monitor.client.model.Service;

/**
 *
 */
@RemoteServiceRelativePath("service/monitorservice")
public interface MonitorService extends RemoteService {
    public String[] getNodeList() throws RMIException;

    public boolean isNodeReachable(String ip) throws RMIException;

    public Collection<Service> getServices(String ip) throws RMIException;

    public Collection<Service> getAllServices() throws RMIException;
}
