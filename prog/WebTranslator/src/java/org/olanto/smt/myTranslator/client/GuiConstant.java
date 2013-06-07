/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.smt.myTranslator.client;

/**
 * Une classe pour déclarer des constantes du client
 *
 */
public class GuiConstant {

    // Boolean variable for setting automatically
    // the size of areas wrt the screen size, not implemented yet.
    public static int EXP_DAYS;  // durée des cookies
    public static String EXTENSION_SET;// listes des extension à disposition
    public static String CORPUS_SET;  // listes corpus à disposition
    public static String LANG_SET;// listes des langues à disposition
    public static String INTERFACE_MESSAGE_PATH;// chemin pour les messages
    public static String INTERFACE_MESSAGE_LANG;// langue de l'utilisateur

    /**
     * **********************************************************************************
     */
    public static String show() {
        return "GUI"
                + "\nPARAMETERS"
                + "\n    EXP_DAYS: " + EXP_DAYS
                + "\n    EXTENSION_SET: " + EXTENSION_SET
                + "\n    CORPUS_SET: " + CORPUS_SET
                + "\n    LANG_SET: " + LANG_SET
                + "\n    INTERFACE_MESSAGE_PATH: " + INTERFACE_MESSAGE_PATH
                + "\n    INTERFACE_MESSAGE_LANG: " + INTERFACE_MESSAGE_LANG
                ;
    }
}
