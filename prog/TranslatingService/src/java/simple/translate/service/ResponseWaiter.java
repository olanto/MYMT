/*
 * Copyright 2011 Simple Shift. All rights reserved.
 * SIMPLE SHIFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package simple.translate.service;

import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.events.TranslatorListener;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 *
 * @author Jacques Guyot
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
