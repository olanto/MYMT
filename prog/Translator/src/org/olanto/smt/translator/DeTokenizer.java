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

import org.olanto.smt.tokenize.TokenSMT;

/**
 *
  */
public class DeTokenizer {

    public static final String regex = "\\|\\d+-\\d+\\| ";

    public static String process(String input) {
        
        input=input.replaceAll(regex, "");  // éliminer les références |nn|

        return TokenSMT.detokenise(input);

    }

}
