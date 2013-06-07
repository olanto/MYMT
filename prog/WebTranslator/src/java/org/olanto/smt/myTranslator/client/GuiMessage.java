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
public class GuiMessage {

    // Messages
    public static String MSG00="Not supported yet.";  
    public static String MSG01="enter your email here";  
    public static String MSG02="<h4>Select</h4>";  
    public static String MSG03="Corpus:";  
    public static String MSG04="Source:";  
    public static String MSG05="Target:";  
    public static String MSG06="File uploaded successfully. Click on Translate to start the translation.";  
    public static String MSG07="An error occurs while uploading !";  
    public static String MSG08="<h4>Original</h4><p>Enter your text here</p>";  
    public static String MSG09="OR upload a file (txt or xliff formats only)";  
    public static String MSG10="<h4>Translation</h4><p>Get your result here</p>";  
    public static String MSG11="OR enter your email address";  
    public static String MSG12="Translate";  
    public static String MSG13="Translation in progress. The result will be sent to you by email.";  
    public static String MSG14="RMI error : ";  
    public static String MSG15="Translation failed : ";  
    public static String MSG16="Request failed";  
    public static String MSG17="Translation done successfully.";  
    public static String MSG18="Translation in progress. Please wait …";  
    public static String MSG19="Status:";  
    public static String MSG20="not_used";  
    public static String MSG21="not_used";  
    public static String MSG22="not_used";  
    public static String MSG23="not_used";  
    public static String MSG24="not_used";  
    public static String MSG25="not_used";  
    public static String MSG26="not_used";  
    public static String MSG27="not_used";  
    public static String MSG28="not_used";  
    public static String MSG29="not_used";  
  
    /**
     * **********************************************************************************
     */
    public static String show() {
        return "GUI"
                + "\nMESSAGES"
                + "\n    MSG00: " + MSG00
                + "\n    MSG01: " + MSG01
                + "\n    MSG02: " + MSG02;
    }
}
