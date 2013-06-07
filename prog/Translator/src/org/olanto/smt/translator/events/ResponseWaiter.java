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

package org.olanto.smt.translator.events;

import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 *
  */
public class ResponseWaiter implements TranslatorListener {

    private Text responseText = null;
    private String responseError = null;

    public synchronized Text waitFor() throws TranslatorException {
        try {
            this.wait();
            if (responseError != null) {
                throw new TranslatorException(responseError);
            }
            return responseText;
        } catch (InterruptedException ex) {
            throw new TranslatorException(responseError);
        }
    }

    @Override
    public synchronized void endTranslation(Text text) {
        responseText = text;
        this.notifyAll();
    }

    @Override
    public synchronized void errorTranslation(Text text, String msg) {
        responseText = text;
        responseError = msg;
        this.notifyAll();
    }

}
