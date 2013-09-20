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
package org.olanto.translate.service.rest;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.olanto.translate.service.TranslateService;

/**
 * REST Web Service
 *
 */
@Path("query")
public class myclass {

    @Context
    private UriInfo context;

    /**
     * try with
     * http://localhost:8080/language/translate/query?key=CORP&source=fr&target=en&q=bonjour
     * http://srv2.olanto.org/R1/translate/query?key=CORPONU&source=fr&target=en&q=bonjour
     
     */
    public myclass() {
    }

    /**
     * Retrieves representation of an instance of olanto.rest.myclass
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public TranslationJaxbBean get(
            @DefaultValue("CORP") @QueryParam("key") String key,
            @DefaultValue("en") @QueryParam("source") String source,
            @DefaultValue("fr") @QueryParam("target") String target,
            @DefaultValue("hello") @QueryParam("q") String q,
            @DefaultValue("no") @QueryParam("debug") String debug) {
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
  /*      return "<res><h1>Response:</h1>"
         + "<p>key:" + key + "</p>"
         + "<p>source:" + source + "</p>"
         + "<p>target:" + target + "</p>"
         + "<p>q:" + q + "</p>"
         + "</res>";
         */
        String tranlatedText = "error";
        try {
            tranlatedText = TranslateService.translateASentence(key, source, target, q);
            if (tranlatedText.startsWith("Error during translation")) {
                return new TranslationJaxbBean(true, "Error during translation, check parameters and service availabity please", "Error during translation for this : " + q + " from " + source + " to " + target);
            } else {
                if (debug.equals("yes")) {
                    return new TranslationJaxbBean(false, "", tranlatedText + " is a translation of : '" + q + "' from " + source + " to " + target);
                } else {
                    return new TranslationJaxbBean(false, "", tranlatedText);
                }

            }
        } catch (Exception ex) {
            return new TranslationJaxbBean(true, "exception:" + ex.getMessage(), "exception:" + ex.getMessage() + " " + tranlatedText + " is a translation of : '" + q + "' from " + source + " to " + target);
        }


    }

    /**
     * PUT method for updating or creating an instance of myclass
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
