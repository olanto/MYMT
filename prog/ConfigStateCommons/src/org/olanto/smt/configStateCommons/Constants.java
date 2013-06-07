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
package org.olanto.smt.configStateCommons;

/**
 * Contient toutes les constantes.
  */
public class Constants {

    public static final int LOCAL_MASTER_PORT = 7777;

    public static final int REQUEST_TYPE_START = 0;
    public static final int REQUEST_TYPE_STOP = 1;
    public static final int REQUEST_TYPE_TEST = 2;

    public static final int RESPONSE_TYPE_OK = 0;
    public static final int RESPONSE_TYPE_ERROR_COMM = 1;
    public static final int RESPONSE_TYPE_ERROR_OP = 2;

    public static final int REQUEST_TIMEOUT = 120000; // 30s
}
