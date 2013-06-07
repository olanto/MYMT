/**********
    Copyright © 2010-2013 Olanto Foundation Geneva

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

package localMaster;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Cette classe contient toutes les constantes utiles au Local Master.
 * Les constantes sont récupérées depuis le fichier de configutation 
 * "localmasterConf.properties".
 * @author Lomig Mégard
 */
public class Config {

    public static String PERL_PATH;
    public static String DAEMON_DIR;

    public static void loadConfig(InputStream configStream) throws IOException {
        Properties prop = new Properties();
        prop.load(configStream);

        //TODO remove
        prop.list(System.out);

        String sValue;

        sValue = prop.getProperty("PERL_PATH");
        if (sValue != null) {
            PERL_PATH = sValue;
        } else {
            throw new IOException("The path to the perl bin (PERL_PATH) is not specified " +
                    "in the local master configuration file.");
        }

        sValue = prop.getProperty("DAEMON_DIR");
        if (sValue != null) {
            DAEMON_DIR = sValue;
        } else {
            throw new IOException("The path to the daemon directory (DAEMON_DIR)" +
                    "is not specified in the local master configuration file.");
        }
    }

}
