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

package org.olanto.smt.myTranslator.client.rpc;

import org.olanto.smt.myTranslator.client.GwtConstant;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.olanto.smt.myTranslator.client.GwtMessage;

/**
 *
 */
public interface WebTranslatorServiceAsync {
    public void translateAndMail(String corpus, String source, String target,
            String text, String[] destMails, boolean fromFile, AsyncCallback<Void> callback);

    public void translate(String corpus, String source, String target,
            String text, boolean fromFile, AsyncCallback<String> callback);
    public void InitPropertiesFromFile( AsyncCallback<GwtConstant> callback);
   public void InitMessagesFromFile( AsyncCallback<GwtMessage> callback);
}
