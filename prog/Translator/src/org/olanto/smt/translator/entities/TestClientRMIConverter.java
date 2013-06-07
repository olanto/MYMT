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

package org.olanto.smt.translator.entities;

import java.rmi.Naming;
import java.rmi.Remote;
import org.olanto.convsrv.server.ConvertService;

/** Test du client de conversion
 *
 * 
 */
public class TestClientRMIConverter {

    public static void main(String[] args) {


        try {

            System.out.println("connect to server");

   //         Remote r = Naming.lookup("rmi://localhost/CONVSRV");
        Remote r = Naming.lookup("rmi://192.168.1.95/CONVSRV");

            System.out.println("access to server");

            if (r instanceof ConvertService) {
                ConvertService is = ((ConvertService) r);
                String s = is.getInformation();
                System.out.println("receive = " + s);

                System.out.println("end ...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
