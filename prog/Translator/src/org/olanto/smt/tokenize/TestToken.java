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

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestToken {

    static String LANG, COLLECT, EXT1, EXT2;
    static int count = 0;
    static int countok = 0;
  
    public static void main(String[] args) {

        String FN = "C:/tokenise example/corpus.so";
        String FNTok = "C:/tokenise example/corpus.tok.so";


        TokenizeAndCompare(FN, FNTok, "UTF-8");

        System.out.println("count:"+count+", ok:"+countok);

    }

    private static void TokenizeAndCompare(String source, String target, String Encoding) {
        try {
            BufferedReader input = null;
            BufferedReader inputtok = null;
            input = new BufferedReader(new InputStreamReader(new FileInputStream(source), Encoding));
            inputtok = new BufferedReader(new InputStreamReader(new FileInputStream(target), Encoding));
            String w;
            w = input.readLine();
            String wtok;
            wtok = inputtok.readLine();
            int breakafter=0;
            while (w != null) {
                compare(w,wtok);
                w = input.readLine();
                wtok = inputtok.readLine();
                breakafter++;
                if (breakafter>10000000)break;
            }
            input.close();
            inputtok.close();
        } catch (Exception ex) {
            Logger.getLogger(TestToken.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void compare(String w, String wref){

        String wtok=TokenSMT.tokenise(w);
          if(wtok.equals(wref)){
              countok++;}
          else{
          System.out.println("\n"+wtok+"\n"+wref);
          }
        count++;
    }
}





