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
package org.olanto.smt.monitor.client.model;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import java.util.Date;

/**
 *
 */
public class ServiceLGR extends ListGridRecord {

    private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("M/d/yy H:mm");
    private static NumberFormat percentFormat = NumberFormat.getPercentFormat();
    private static NumberFormat decimalFormat = NumberFormat.getDecimalFormat();

    public ServiceLGR(Service service) {
        this.setAttribute("ip", service.ip);
        this.setAttribute("port", service.port);
        this.setAttribute("corpus", service.corpus+" - "+ service.deamon);
        this.setAttribute("source", service.source);
        this.setAttribute("target", service.target);
        this.setAttribute("nbSentenceTranslated", service.nbSentenceTranslated);
        this.setAttribute("nbCharTranslated", service.nbCharTranslated);
        this.setAttribute("lastTimeOk", dateFormat.format(new Date(service.lastTimeOk)));
        this.setAttribute("charPerSecond", decimalFormat.format(service.charPerSecond));
        this.setAttribute("usedRatio", percentFormat.format(service.usedRatio));
        this.setAttribute("testRatio", percentFormat.format(service.testRatio));
        this.setAttribute("responding", (service.responding ? "1" : "0"));
    }
}
