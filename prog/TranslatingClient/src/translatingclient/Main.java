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

package translatingclient;

/**
 *
 * @author jacques guyot 21-10-2010
 * exemple de d'utilisation du web-service pour l'extension de requête
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        testWebService("CORP", "en", "fr", "companies are big.");
        testWebService("CORP", "en", "fr", "the simplicity is not complex.");
     //   testWebService("ruen", "ru", "en", "перевод не является сложной.");

    }

    public static void testWebService(String corpus, String sourceLanguage, String targetLanguage, String aSentence) {
        try { // Call Web Service Operation
            simple.translate.service.TranslateServiceService service = new simple.translate.service.TranslateServiceService();
            simple.translate.service.TranslateService port = service.getTranslateServicePort();
            // TODO process result here
            java.lang.String result = port.translateASentence(corpus, sourceLanguage, targetLanguage, aSentence);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
    }
}
