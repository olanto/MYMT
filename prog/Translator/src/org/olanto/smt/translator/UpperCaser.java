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

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.LinkedList;
import org.olanto.smt.translator.entities.Sentence;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.events.TranslatorListener;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 * Cette action prend en entrée de sa méthode process() un texte traduit et essaie
 * de lancer une traduction d'upper case sur le résultat.
  */
public class UpperCaser extends Action implements TranslatorListener {

    private Text lowerText = null;

    
    @Override
    public void process(Text text) {
        try {

            lowerText = text;
    // System.out.println("process uppercase corpus "+lowerText.getCorpus() + "UP"+" "+lowerText.getTarget()+" "+ lowerText.getTarget());
          //change the text settings
            Collection<Sentence> translatedSentences = lowerText.getSentences();
            Collection<Sentence> newSentences = new LinkedList<Sentence>();
            for (Sentence translatedSentence : translatedSentences) {
                newSentences.add(new Sentence(translatedSentence.getId(), translatedSentence.getTarget()));
            }

            final Text newText = new Text(lowerText.getCorpus() + "UP",
                    lowerText.getTarget(), lowerText.getTarget(), newSentences,
                    lowerText.getUpfile(), lowerText.isFromfile(), lowerText.getXliff(), lowerText.getDocx());
            //create a new Translator with specific upper case settings
            Translator trans = new Translator(newText, "localhost");

            trans.addTranslatorListener(this);

            trans.process(newText);

        } catch (TranslatorException ex) {
            // There is no upper caser, just give the lower case text
            fireTranslationEnd(lowerText);
        } catch (NotBoundException ex) {
            fireTranslationError(text, "The configState is not linked : " + ex.getMessage());
        } catch (MalformedURLException ex) {
            fireTranslationError(text, "URL malformed : " + ex.getMessage());
        } catch (RemoteException ex) {
            fireTranslationError(text, "An RMI error occurs during the Translator "
                    + "initialization. Message : " + ex.getMessage());
        }
    }


    @Override
    public void endTranslation(Text text) {
        fireTranslationEnd(text);
    }

    @Override
    public void errorTranslation(Text text, String msg) {
        // TODO change policy ?
        // if an error occurs during the upper case process,
        // just give the lower case text
        fireTranslationEnd(lowerText);
    }
}
