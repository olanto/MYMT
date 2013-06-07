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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Main entry point.
 *
 */
public class MainEntryPoint implements EntryPoint {
    /** 
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {

        
	HLayout mainLayout = new HLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();

        final MonitorListGrid grid = new MonitorListGrid();

        /*
        Timer timer = new Timer() {
            @Override
            public void run() {
                grid.update();
            }
        };

        timer.scheduleRepeating(2000);
*/
        mainLayout.addChild(grid);

        mainLayout.draw();

    }
}
