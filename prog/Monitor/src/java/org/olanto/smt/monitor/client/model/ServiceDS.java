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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import java.util.Collection;
import org.olanto.smt.monitor.client.rpc.MonitorService;
import org.olanto.smt.monitor.client.rpc.MonitorServiceAsync;

/**
 *
  */
public class ServiceDS extends DataSource {

    private MonitorServiceAsync service = GWT.create(MonitorService.class);
    
    public ServiceDS() {

        DataSourceTextField responding = new DataSourceTextField("responding", "UP");
        DataSourceTextField ip = new DataSourceTextField("ip", "IP");
        DataSourceIntegerField port = new DataSourceIntegerField("port", "Port");
        DataSourceTextField corpus = new DataSourceTextField("corpus", "Corpus");
        DataSourceTextField source = new DataSourceTextField("source", "src");
        DataSourceTextField target = new DataSourceTextField("target", "trg");

        DataSourceIntegerField nbSentenceTranslated = new DataSourceIntegerField("nbSentenceTranslated", "#sentences");
        DataSourceIntegerField nbCharTranslated = new DataSourceIntegerField("nbCharTranslated", "#chars");

        DataSourceTextField lastTimeOk = new DataSourceTextField("lastTimeOk", "last UP");
        DataSourceTextField charPerSecond = new DataSourceTextField("charPerSecond", "chars/s");
        DataSourceTextField usedRatio = new DataSourceTextField("usedRatio", "used at");
        DataSourceTextField testRatio = new DataSourceTextField("testRatio", "UP at");


        this.setFields(responding, ip, port, corpus, source, target, 
                nbSentenceTranslated, nbCharTranslated, lastTimeOk,
                charPerSecond, usedRatio, testRatio);

        this.setClientOnly(true);

        service.getAllServices(new AsyncCallback<Collection<Service>>() {
            public void onFailure(Throwable caught) {
                //TODO gestion erreur
                System.out.println("[ERREUR] Probleme de recuperation des personnes");
		caught.printStackTrace();

            }

            public void onSuccess(Collection<Service> result) {
                for (Service service : result) {
                    ServiceLGR serviceLGR = new ServiceLGR(service);
                    ServiceDS.this.addData(serviceLGR);
                }
            }
        });
        
    }
}
