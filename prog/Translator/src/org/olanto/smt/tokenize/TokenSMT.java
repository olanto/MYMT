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

package org.olanto.smt.tokenize;

/**
 * Une classe pour définir les token (une définition alpha numérique).
 *
 */
public class TokenSMT {

    static final int EOF = -1;
    static int poschar = 0;
    static String s;
    static int c;

    public final static String detokenise(String w) {
        if (w.length() < 2) {
            return w; // moins de deux caractères
        }
        w = w.substring(0, 1).toUpperCase() + w.substring(1); // première lettre en majuscule
        boolean first = true;
        while (w.contains(" \" ")) { // traitement des ""
            if (first) {
                w = w.replaceFirst(" \" ", " \"");
                first = false;
            } else {
                w = w.replaceFirst(" \" ", "\" ");
                first = true;
            }
        }
        w = w.replace(" , ", ", ");
        w = w.replace(" : ", ": ");
        w = w.replace(" ; ", "; ");
        w = w.replace(" ? ", "? ");
        w = w.replace(" ! ", "! ");
        w = w.replace(" ' ", "'");
        w = w.replace(" ( ", " (");
        w = w.replace(" ) ", ") ");

        w = w.replace(" . ", ". ");
        return w;
    }

    public final static String tokenise(String w) {
        if (w == null || w.length() == 0) {
            return "";
        }
        s = w;
        poschar = 0;
        String res = "";
        String currentTok = nexttok();
        while (currentTok != null) {
            //System.out.println("tok:"+currentTok);
            if (res.equals("")) {
                res = currentTok;
            } else {
                res += " " + currentTok;
            }
            currentTok = nexttok();
        }
        res = detectAbreviation(res);
        res = res.replaceAll("  ", " ");
        res = res.replaceAll("\\. \\.", "..");
        res = res.replaceAll("\\. \\.", "..");

        return res;
    }

    public final static String detectAbreviation(String w) {
        w = w.replaceAll("e\\.g \\.", "e.g.");
//        w = w.replaceAll("No \\.", "No.");
        w = w.replaceAll("a\\.m \\.", "a.m .");
        w = w.replaceAll("p\\.m \\.", "p.m .");
        w = w.replaceAll("i\\.e \\.", "i.e.");
        w = w.replaceAll("rev \\.", "rev.");
        w = w.replaceAll("Mrs \\.", "Mrs.");
        w = w.replaceAll("Mr \\.", "Mr.");
        w = w.replaceAll("Ph \\.", "Ph.");
//        w = w.replaceAll("I \\.", "I.");
//        w = w.replaceAll("II \\.", "II.");
//        w = w.replaceAll("III \\.", "III.");
//        w = w.replaceAll("IV \\.", "IV.");
//        w = w.replaceAll("V \\.", "V.");
        return w;
    }

    public final static int getnextchar() {
        if (poschar >= s.length()) {
            return EOF;  // atteind la fin
        }
        int res = s.charAt(poschar);
        poschar++;
        return res;

    }

    public final static int prevchar() {
        if (poschar <= 1) {
            return EOF;  // on est au début
        }
        int res = s.charAt(poschar - 2);
        return res;
    }

    public final static int nextchar() {
        if (poschar >= s.length()) {
            return EOF;  // on est à la fin
        }
        int res = s.charAt(poschar);
        return res;

    }

    public final static String nexttok() {
        char r;
        c = getnextchar();
        while (c != EOF && (c == ' ' || c == 160)) {  // 160=xA0
            c = getnextchar();
        }
        //System.out.println(c);
        if (c == EOF) {
            return null;  // atteind la fin
        }        // ici on a un caractère qui n'est pas un blanc
        if (!(Character.isLetter((char) c) || Character.isDigit((char) c)) // pas AZ19
                && !(c == '-' && Character.isDigit((char) nextchar())) // pas le signe moins devant un chiffre
                ) {
            return "" + (char) c;
        }
        String token = "";
        while ((Character.isLetter((char) c)
                || Character.isDigit((char) c)
                || (char) c == '.'
                || (char) c == ','
                || (char) c == '-') && (c != EOF)) {  // get word
            if ((char) c == ','
                    && (!Character.isDigit((char) prevchar()) || !Character.isDigit((char) nextchar())) // pas 99,99
                    ) {
                break;
            }
            if ((char) c == '.'
                    && (!(Character.isLetter((char) nextchar()) || Character.isDigit((char) nextchar()))) // est suivi d'un non AZ19
                    && ((Character.isLowerCase((char) prevchar()))) // est précédé d'un non AZ LOWER
                    ) {
                break;
            }
            if ((char) c == '.' && (nextchar() == EOF) // est le point final
                    ) {
                break;
            }
            token += (char) c;
            c = getnextchar();
        }
        if (token.endsWith(".") && Character.isDigit(token.charAt(token.length() - 2))) { // cas 2004.
            token = token.substring(0, token.length() - 1); //élimine le point
            poschar--;  // on a lu un caractère de trop (le point)
        }
        if (c != EOF) {
            poschar--;  // on a lu un caractère de trop
        }
        return token;

    }
}
