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

package org.olanto.translate.service;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.smt.translator.Tokenizer;
import org.olanto.smt.translator.Translator;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 *
 * @author Jacques Guyot
 */
public class TranslateService {

    /**
     * Web service translation
     */
    public static String translateASentence(
    String corpus, 
    String sourceLanguage, 
    String targetLanguage,
    String aSentence) {
        try {
            //TODO write your implementation code here:
            // Get and parse the source text :
            String tokenizedText = Tokenizer.process(aSentence);
            Text textFormatted = new Text(corpus, sourceLanguage, targetLanguage, Text.parseSentences(tokenizedText, "\\."));
            Translator translator = new Translator(textFormatted, "localhost");
            ResponseWaiter waiter = new ResponseWaiter();
            translator.addTranslatorListener(waiter);
            translator.process(textFormatted);
            Text textTranslated = waiter.waitFor();
            String returnTxt=textTranslated.toString().replace("\n", ""); // remove cr
            return returnTxt;
        } catch (TranslatorException ex) {
            Logger.getLogger(TranslateService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(TranslateService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TranslateService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error during translation, look in log";
    }
}
