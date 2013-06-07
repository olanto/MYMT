/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myMT.
 *
 * myMT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myMT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myMT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.smt.translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.util.smt.utilsmt.Messages;
import org.olanto.util.smt.utilsmt.SenseOS;

/**
 * Cette classe contient toutes les constantes utiles au Master. Les constantes
 * ont une valeur par défaut qui peut être modifiée avec le fichier de
 * configuration "WebTranslator.properties".
 */
public class Config {

    public static String WARNING_SENTENCE_LENGTH = "Translation result";
    public static int MAX_SENTENCE_LENGTH = 500;  // max length of segment in char
    public static String RMI_SERVER_SMT = "rmi://localhost/configState";  // address of rmi server
    public static String RMI_SERVER_CONV = "rmi://localhost/CONVSRV";  // address of rmi server
    public static String INTERFACE_MESSAGE_LANG = "en";  // language for messages
    private static String initOK;
    private static Properties prop = new Properties();
  //  public static Messages MSG = new Messages(SenseOS.getMYMT_HOME() + "/config/messages/inittrans_" + INTERFACE_MESSAGE_LANG + ".properties");
   public static Messages MSG ;

    public static void loadConfig(File configFile) throws IOException {

        prop.load(new FileInputStream(configFile));

        prop.list(System.out);

        if (getStringParameter("WARNING_SENTENCE_LENGTH") != null) {
            WARNING_SENTENCE_LENGTH = getStringParameter("WARNING_SENTENCE_LENGTH");
        }
        if (getStringParameter("MAX_SENTENCE_LENGTH") != null) {
            MAX_SENTENCE_LENGTH = Integer.parseInt(getStringParameter("MAX_SENTENCE_LENGTH"));
        }
        if (getStringParameter("RMI_SERVER_SMT") != null) {
            RMI_SERVER_SMT = getStringParameter("RMI_SERVER_SMT");
        }
        if (getStringParameter("RMI_SERVER_CONV") != null) {
            RMI_SERVER_CONV = getStringParameter("RMI_SERVER_CONV");
        }
        if (getStringParameter("INTERFACE_MESSAGE_LANG") != null) {
            RMI_SERVER_CONV = getStringParameter("INTERFACE_MESSAGE_LANG");
        }
        MSG = new Messages(SenseOS.getMYMT_HOME() + "/config/messages/inittrans_" + INTERFACE_MESSAGE_LANG + ".properties");

    }

    private static String getStringParameter(String paramName) {
        //System.out.println(paramName+"<-"+prop.getProperty(paramName));
        return prop.getProperty(paramName);
    }

    public static void ReadParameters() {
        if (initOK == null) {
            try {
                // init config
                loadConfig(new File(SenseOS.getMYMT_HOME() + "/config/Translator.properties"));
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
            initOK = "OK";
        }
    }
}
