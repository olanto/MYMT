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

package org.olanto.smt.translator;

import java.util.LinkedList;
import java.util.List;
import org.olanto.smt.translator.entities.Sentence;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.events.TranslatorListener;

/**
 * classe de test... pas avec junit car multithread.
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

            Text text = new Text("test", "en", "fr", sentences);

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
