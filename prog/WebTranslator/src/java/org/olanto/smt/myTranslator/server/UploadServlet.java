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

package org.olanto.smt.myTranslator.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;

/**
 * Cette servlet est utilisée uniquement pour l'upload de fichier.
 * Elle répond à un upload avec les informations du fichier.
  */
public class UploadServlet extends UploadAction {

    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response = "";
        int cont = 0;
        for (FileItem item : sessionFiles) {
            if (!item.isFormField()) {
                cont++;
                try {
                    // Compose a xml message with the full file information which can be parsed in client side
                    response += "<file-" + cont + "-field>" + item.getFieldName() + "</file-" + cont + "-field>\n";
                    response += "<file-" + cont + "-name>" + item.getName() + "</file-" + cont + "-name>\n";
                    response += "<file-" + cont + "-size>" + item.getSize() + "</file-" + cont + "-size>\n";
                    response += "<file-" + cont + "-type>" + item.getContentType() + "</file-" + cont + "type>\n";
                } catch (Exception e) {
                    throw new UploadActionException(e);
                }
            }
        }

        /// Send information of the received files to the client.
        return "<response>\n" + response + "</response>\n";
    }
}
