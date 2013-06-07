/*
 * Copyright 2011 Simple Shift. All rights reserved.
 * SIMPLE SHIFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/

package simple.translate.service;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ejb.Stateless;
import org.olanto.smt.translator.Tokenizer;
import org.olanto.smt.translator.Translator;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 *
 * @author Jacques Guyot
 */
@WebService()
@Stateless()
public class TranslateService {

    /**
     * Web service translation
     */
    @WebMethod(operationName = "translateASentence")
    public String translateASentence(@WebParam(name = "corpus")
    String corpus, @WebParam(name = "sourceLanguage")
    String sourceLanguage, @WebParam(name = "targetLanguage")
    String targetLanguage, @WebParam(name = "aSentence")
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
            return textTranslated.toString();
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
