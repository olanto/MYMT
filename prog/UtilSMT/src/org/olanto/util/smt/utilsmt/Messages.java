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
package org.olanto.util.smt.utilsmt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * pour stocker les messages
 */
public class Messages {

    // Messages
    public String MSG00;
    public String MSG01;
    public String MSG02;
    public String MSG03;
    public String MSG04;
    public String MSG05;
    public String MSG06;
    public String MSG07;
    public String MSG08;
    public String MSG09;
    public String MSG10;
    public String MSG11;
    public String MSG12;
    public String MSG13;
    public String MSG14;
    public String MSG15;
    public String MSG16;
    public String MSG17;
    public String MSG18;
    public String MSG19;
    public String MSG20;
    public String MSG21;
    public String MSG22;
    public String MSG23;
    public String MSG24;
    public String MSG25;
    public String MSG26;
    public String MSG27;
    public String MSG28;
    public String MSG29;

    public Messages(String fname) {

        try {
            ConstStringManager msg = new ConstStringManager("C:/MYMT/prog/WebTranslator/src/java/org/olanto/smt/myTranslator/messages/initclient_fr.properties");
            InitProperties(msg);
        } catch (IOException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void InitProperties(ConstStringManager msg) {

        MSG00 = msg.get("MSG00");
        MSG01 = msg.get("MSG01");
        MSG02 = msg.get("MSG02");
        MSG03 = msg.get("MSG03");
        MSG04 = msg.get("MSG04");
        MSG05 = msg.get("MSG05");
        MSG06 = msg.get("MSG06");
        MSG07 = msg.get("MSG07");
        MSG08 = msg.get("MSG08");
        MSG09 = msg.get("MSG09");
        MSG10 = msg.get("MSG10");
        MSG11 = msg.get("MSG11");
        MSG12 = msg.get("MSG12");
        MSG13 = msg.get("MSG13");
        MSG14 = msg.get("MSG14");
        MSG15 = msg.get("MSG15");
        MSG16 = msg.get("MSG16");
        MSG17 = msg.get("MSG17");
        MSG18 = msg.get("MSG18");
        MSG19 = msg.get("MSG19");
        MSG20 = msg.get("MSG20");
        MSG21 = msg.get("MSG21");
        MSG22 = msg.get("MSG22");
        MSG23 = msg.get("MSG23");
        MSG24 = msg.get("MSG24");
        MSG25 = msg.get("MSG25");
        MSG26 = msg.get("MSG26");
        MSG27 = msg.get("MSG27");
        MSG28 = msg.get("MSG28");
        MSG29 = msg.get("MSG29");
    }
}
