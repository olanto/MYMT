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

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import org.olanto.smt.monitor.client.model.ServiceDS;

/**
 *
 * @author Lomig Mégard
 */
public class MonitorListGrid extends Canvas {

    private final ListGrid grid;

    public MonitorListGrid() {
        grid = new ListGrid();

        grid.setWidth(500);
        grid.setAlternateRecordStyles(true);
        grid.setShowAllRecords(true);

        grid.setCanResizeFields(true);
	grid.setCanReorderFields(true);
	grid.setCanSort(true);
        grid.setAutoFetchData(true);

        //grid.setGroupByField("ip");
        //grid.setGroupStartOpen(GroupStartOpen.ALL);
        
        grid.setAutoFitData(Autofit.BOTH);


        update();

	//setShowResizeBar(true);
        //setResizeBarTarget("next");

        VLayout vlayout = new VLayout();
        vlayout.setDefaultLayoutAlign(Alignment.CENTER);
        vlayout.addMember(new LayoutSpacer());
        vlayout.addMember(grid);
        vlayout.addMember(new LayoutSpacer());

        HLayout hlayout = new HLayout();
        hlayout.setHeight100();
        hlayout.setWidth100();
        hlayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
        hlayout.addMember(new LayoutSpacer());
        hlayout.addMember(vlayout);
        hlayout.addMember(new LayoutSpacer());

        addChild(hlayout);

    }

    public void update() {
        grid.setDataSource(new ServiceDS());
        grid.fetchData();
    }


}
