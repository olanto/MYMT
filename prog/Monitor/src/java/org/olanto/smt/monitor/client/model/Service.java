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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Cette classe est une réplique statique s'un service.
 */
public class Service implements IsSerializable {

    protected String ip;
    protected int port;
    protected String deamon;
    protected String corpus;
    protected String source;
    protected String target;

    protected long nbSentenceTranslated;
    protected long nbCharTranslated;
    protected long lastTimeOk;

    protected double charPerSecond;
    protected double usedRatio;
    protected double testRatio;
    
    protected boolean responding;

    // for serialization
    public Service() {
    }

    public Service(String ip, int port, String deamon, String corpus, String source, String target,
            long nbSentenceTranslated, long nbCharTranslated, long lastTimeOk,
            double charPerSecond, double usedRatio, double testRatio, boolean responding) {
        this.ip = ip;
        this.port = port;
        this.deamon = deamon;
        this.corpus = corpus;
        this.source = source;
        this.target = target;
        this.nbSentenceTranslated = nbSentenceTranslated;
        this.nbCharTranslated = nbCharTranslated;
        this.lastTimeOk = lastTimeOk;
        this.charPerSecond = charPerSecond;
        this.usedRatio = usedRatio;
        this.testRatio = testRatio;
        this.responding = responding;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Service other = (Service) obj;
        if ((this.ip == null) ? (other.ip != null) : !this.ip.equals(other.ip)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        if ((this.deamon == null) ? (other.deamon != null) : !this.deamon.equals(other.deamon)) {
            return false;
        }
        if ((this.corpus == null) ? (other.corpus != null) : !this.corpus.equals(other.corpus)) {
            return false;
        }
        if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
            return false;
        }
        if ((this.target == null) ? (other.target != null) : !this.target.equals(other.target)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.ip != null ? this.ip.hashCode() : 0);
        hash = 41 * hash + this.port;
        hash = 41 * hash + (this.deamon != null ? this.deamon.hashCode() : 0);
        hash = 41 * hash + (this.corpus != null ? this.corpus.hashCode() : 0);
        hash = 41 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 41 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }

}
