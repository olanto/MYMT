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
package org.olanto.smt.monitor.client;

import org.olanto.smt.monitor.client.model.Service;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Collection;
import org.olanto.smt.monitor.client.rpc.MonitorService;
import org.olanto.smt.monitor.client.rpc.MonitorServiceAsync;
import org.olanto.smt.monitor.client.rpc.RMIException;

/**
 * OLD 
 */
public class Monitor extends VerticalPanel {

    /*
    private final MonitorServiceAsync service = GWT.create(MonitorService.class);

    private final TextArea textArea;

    private final MonitorTable table;

    public Monitor() {

        this.table = new MonitorTable();
        this.textArea = new TextArea();
        this.textArea.setTitle("LOG");
        this.textArea.setSize("300px", "200px");

        Timer timer = new Timer() {
            @Override
            public void run() {
                update();
            }
        };

        timer.scheduleRepeating(10000);

        this.add(table);

        this.add(textArea);

        update();
    }


    private void fillTable(final String ip) {
        final AsyncCallback<Collection<Service>> callback = new AsyncCallback<Collection<Service>>() {
            public void onSuccess(Collection<Service> result) {

                for (Service service : result) {
                    table.addRow(new RowData(service));
                }

            }

            public void onFailure(Throwable caught) {
                String message = "Can not get services from Node[" + ip + "]. Cause : ";
                try {
                    throw caught;
                } catch (InvocationException ex) {
                    message += "An RPD error occurs !";
                } catch (RMIException ex) {
                    message += "An RMI error occurs !";
                } catch (Throwable ex) {
                     message += "An very unexpected error occurs !";
                }
                log(message);
            }
        };

        service.getServices(ip, callback);

    }

    private Widget buildTree() {

        final Tree tree = new Tree();

        final AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
            public void onSuccess(String[] result) {

                TreeItem root = new TreeItem("MyTranslator");

                for (String ip : result) {
                    root.addItem(ip);
                }

                tree.addItem(root);
            }

            public void onFailure(Throwable caught) {
                String message = null;
                try {
                    throw caught;
                } catch (InvocationException ex) {
                    message = "An RPD error occurs !";
                } catch (RMIException ex) {
                    message = "An RMI error occurs !";
                } catch (Throwable ex) {
                     message = "An very unexpected error occurs !";
                }
                log(message);
            }
        };

        service.getNodeList(callback);

        return tree;
    }

    private void update() {

        table.empty();

        final AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
            public void onSuccess(String[] result) {
                for (String ip : result) {
                    fillTable(ip);
                }
            }

            public void onFailure(Throwable caught) {
                String message = "Can not get the node's ip. Cause : ";
                try {
                    throw caught;
                } catch (InvocationException ex) {
                    message += "An RPD error occurs !";
                } catch (RMIException ex) {
                    message += "An RMI error occurs !";
                } catch (Throwable ex) {
                     message += "An very unexpected error occurs !";
                }
                log(message);
            }
        };

        service.getNodeList(callback);
    }

    private void log(String msg) {
        textArea.setText(textArea.getText() + "\n" + msg);
    }

     * 
     */
}
