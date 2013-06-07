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

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Collection;
import org.olanto.smt.monitor.client.model.Service;

/**
 *
  */
public interface MonitorServiceAsync {
    public void getNodeList(AsyncCallback<String[]> callback);

    public void isNodeReachable(String ip, AsyncCallback<Boolean> callback);

    public void getServices(String ip, AsyncCallback<Collection<Service>> callback);

    public void getAllServices(AsyncCallback<Collection<Service>> callback);
}
