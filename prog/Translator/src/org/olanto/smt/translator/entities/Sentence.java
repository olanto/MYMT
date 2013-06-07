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

package org.olanto.smt.translator.entities;

/**
 *
  */
public class Sentence {

    public static final int STATUS_TO_TRANSLATE = 0;
    public static final int STATUS_RESERVED = 1;
    public static final int STATUS_TRANSLATED = 2;

    private final long id;
    private int status;

    private String source;
    private String target;

    public Sentence(long id, String source) {
        this.id = id;
        this.source = source;

        // init values
        this.status = STATUS_TO_TRANSLATE;
        this.target = null;
    }

    public long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status != STATUS_TO_TRANSLATE &&
                status != STATUS_RESERVED &&
                status != STATUS_TRANSLATED) {
            throw new IllegalArgumentException("The status is unknown. Status="
                    + status);
        }
        this.status = status;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        if (status != STATUS_RESERVED) {
            throw new UnsupportedOperationException("This sentence " +
                    "is not reserved, or is already translated.");
        }
        this.target = target;
    }

    @Override
    public String toString() {
      //  return source + " --> " + target;  // seulement pour le debugging
          return target;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    

}
