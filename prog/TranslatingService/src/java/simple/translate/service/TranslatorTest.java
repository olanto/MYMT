/*
 * Copyright 2011 Simple Shift. All rights reserved.
 * SIMPLE SHIFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package simple.translate.service;

import java.util.LinkedList;
import java.util.List;
import org.olanto.smt.translator.Tokenizer;
import org.olanto.smt.translator.Translator;
import org.olanto.smt.translator.entities.Sentence;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.events.TranslatorListener;

/**
 * classe de test... pas avec junit car multithread.
 * @author Jacques Guyot
 */
public class TranslatorTest implements TranslatorListener {

    public TranslatorTest() {

        try {
            List<Sentence> sentences = new LinkedList();

            sentences.add(new Sentence(1, Tokenizer.process("Aid for Trade aims "
                    + "to help developping countries.")));
            sentences.add(new Sentence(2, Tokenizer.process("The succes of the "
                    + "initiative depends on creating closer cooperation in national "
                    + "capitals between trade, finance and development officials of "
                    + "WTO member governments.")));
            sentences.add(new Sentence(3, Tokenizer.process("This page gathers "
                    + "key informations on Switzerland's participation in the WTO.")));
            sentences.add(new Sentence(4, Tokenizer.process("Switzerland has been "
                    + "a WTO member since 1 July 1995.")));

            for (int i = 5; i < 30; i++) {
                sentences.add(new Sentence(i, Tokenizer.process("Switzerland has been "
                        + "a WTO member since 1 July 1995.")));
            }

            Text text = new Text("CORP", "en", "fr", sentences);

            Translator transalor = new Translator(text, "localhost");

            transalor.addTranslatorListener(this);

            transalor.process(text);

        } catch (Exception ex) {
            System.err.println("ERROR : " + ex.getMessage());
        }

    }

    public void endTranslation(Text text) {
        System.out.println("*********** Translation end ************");
        //System.out.println(text);
    }

    public void errorTranslation(Text text, String msg) {
        System.err.println(msg);
    }


    public static void main(String[] args) {
        new TranslatorTest();
    }
}
