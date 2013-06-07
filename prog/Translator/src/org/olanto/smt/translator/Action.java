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

import java.util.Collection;
import java.util.HashSet;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.events.TranslatorListener;

/**
 * Cette classe permet de créer une action de traduction.
 * Elle offre la possibilité de chainer plusieurs actions sous la forme d'arbres.
 * Chaque action peut recevoir des TranslationListener.
 */
abstract public class Action {

    private final Collection<TranslatorListener> listeners;
    private final Collection<Action> nextActions;

    protected Action() {
        this.listeners = new HashSet<TranslatorListener>();
        this.nextActions = new HashSet<Action>();
    }

    public void addTranslatorListener(TranslatorListener listener) {
        listeners.add(listener);
    }

    public boolean removeTranslatorListener(TranslatorListener listener) {
        return listeners.remove(listener);
    }

    public void addNextAction(Action action) {
        nextActions.add(action);
    }

    public boolean removeNextAction(Action action) {
        return nextActions.remove(action);
    }

    protected synchronized void fireTranslationEnd(Text text) {

        for (Action action : nextActions) {
            action.process(text);
        }

        for (TranslatorListener listener : listeners) {
            listener.endTranslation(text);
        }
    }

    protected synchronized void fireTranslationError(Text text, String msg) {

        for (Action action : nextActions) {
            action.fireTranslationError(text, msg);
        }

        for (TranslatorListener listener : listeners) {
            listener.errorTranslation(text, msg);
        }
    }

    abstract public void process(Text text);

}
