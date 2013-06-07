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

import org.olanto.smt.configStateCommons.exceptions.ConnectionException;
import org.olanto.smt.configStateCommons.interfaces.IConfigState;
import org.olanto.smt.configStateCommons.interfaces.IService;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.olanto.smt.translator.entities.Sentence;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.exceptions.TranslatorException;
import static org.olanto.smt.translator.Config.*;

/**
 *
 */
public class Translator extends Action {

    private final IConfigState configState;
    private Text text;
    private Queue<Sentence> sentencesToTranslate;
    private List<TranslatorService> translators;
    private int runningCounter;
    

    /**
     * Initialize a new Translator.
     * @param text used only for settings, the sentences are not read.
     * @param RMIhost the host of the configState.
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws TranslatorException if there are no services availables.
     */
    public Translator(Text text, String RMIhost) throws NotBoundException, MalformedURLException, RemoteException, TranslatorException {
        Remote remote = Naming.lookup("rmi://" + RMIhost + "/configState");
        configState = (IConfigState) remote;

        this.runningCounter = 0;

        Collection<IService> services = configState.getServices(text.getCorpus(), text.getSource(), text.getTarget());
        if (services.isEmpty()) {
            throw new TranslatorException("There are no services availables.");
        }

        translators = new LinkedList<TranslatorService>();
        for (IService service : services) {
            TranslatorService translator = new TranslatorService(service);
            translators.add(translator);
        }

    }

       public Translator(Text text) throws NotBoundException, MalformedURLException, RemoteException, TranslatorException {
        Remote remote = Naming.lookup(RMI_SERVER_SMT);
        configState = (IConfigState) remote;

        this.runningCounter = 0;

        Collection<IService> services = configState.getServices(text.getCorpus(), text.getSource(), text.getTarget());
        if (services.isEmpty()) {
            throw new TranslatorException("There are no services availables.");
        }

        translators = new LinkedList<TranslatorService>();
        for (IService service : services) {
            TranslatorService translator = new TranslatorService(service);
            translators.add(translator);
        }

    }

    @Override
    public void process(Text text) {
        this.text = text;
        if (text.isEmpty()) {
            fireTranslationError(text, "The text to translate is empty.");
            return;
        }
        this.sentencesToTranslate = new LinkedList<Sentence>(text.getSentences());

        for (TranslatorService translator : translators) {
            translator.start();
        }
    }
    
 
    private synchronized void removeTranslator(TranslatorService translator) {
        translators.remove(translator);
        if (translators.isEmpty()) {
            fireTranslationError(text, "There are no more services availables.");
        }
    }

    private synchronized Sentence next() throws InterruptedException {
        if (sentencesToTranslate.isEmpty() && runningCounter == 0) {

            fireTranslationEnd(text);

            // stop all translators :
            for (TranslatorService translator : translators) {
                translator.interrupt();
            }
            return null;
        }
        
        while(sentencesToTranslate.isEmpty()) {
            wait();
        }
        
        runningCounter++;
        Sentence sentence = sentencesToTranslate.remove();
        sentence.setStatus(Sentence.STATUS_RESERVED);
        return sentence;
    }

    private synchronized void finish(Sentence sentence, String target) {
        sentence.setTarget(target);
        sentence.setStatus(Sentence.STATUS_TRANSLATED);
        runningCounter--;
    }

    private synchronized void restore(Sentence sentence) {
        sentence.setStatus(Sentence.STATUS_TO_TRANSLATE);
        sentencesToTranslate.add(sentence);
        runningCounter--;
        notifyAll();
    }

    private class TranslatorService extends Thread {

        private final IService service;

        public TranslatorService(IService service) throws RemoteException {
            super("Translator[" + service.getIp() + ":" + service.getPort() + "]");
            this.service = service;
        }

        @Override
        public void run() {
            Sentence sentence = null;
            try {
                while ((sentence = next()) != null) {
                    try {
                        String target = service.translate(sentence.getSource());
                        // remove tags from moses
                        target = DeTokenizer.process(target);
                        finish(sentence, target);
                        //System.out.println(this + "has translated " + sentence);

                    } catch (ConnectionException ex) {
                        service.setResponding(false);
                        restore(sentence);
                        removeTranslator(this);
                        System.err.println(this + "DOWN. Message : " + ex.getMessage());
                        return;
                    }
                } // end while
            } catch (InterruptedException ex) {
                // end
            } catch (RemoteException ex) {
                restore(sentence);
                removeTranslator(this);
                System.err.println(this + "DOWN. Message : " + ex.getMessage());
            }
        }

        @Override
        public String toString() {
            return this.getName();
        }
       
    }
}
