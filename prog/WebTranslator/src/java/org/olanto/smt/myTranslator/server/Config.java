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

package org.olanto.smt.myTranslator.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.util.smt.utilsmt.SenseOS;

/**
 * Cette classe contient toutes les constantes utiles au Master.
 * Les constantes ont une valeur par défaut qui peut être modifiée
 * avec le fichier de configuration "WebTranslator.properties".
 */
public class Config {

    public static String MAIL_SUBJECT_TXT = "Translation result";
    public static String MAIL_FILE_PREFIX = "";

    public static String FROM_MAIL = "simple.test.888@gmail.com";
    public static String SMTP_AUTH_USER = "simple.test.888";
    public static String SMTP_AUTH_PWD = "xxxxx";
    public static String SMTP_HOST_NAME = "smtp.gmail.com";
    public static String SMTP_PORT = "465";
    public static String SMTP_SSL = "YES";
    public static String SMTP_AUTH = "YES";


    private static String initOK;

    private static Properties prop = new Properties();

    public static void loadConfig(File configFile) throws IOException {

        prop.load(new FileInputStream(configFile));

        //TODO remove
        prop.list(System.out);

        if (getStringParameter("MAIL_SUBJECT_TXT") != null) {
            MAIL_SUBJECT_TXT = getStringParameter("MAIL_SUBJECT_TXT");
        }
        if (getStringParameter("FROM_MAIL") != null) {
            FROM_MAIL = getStringParameter("FROM_MAIL");
        }
        if (getStringParameter("SMTP_AUTH_USER") != null) {
            SMTP_AUTH_USER = getStringParameter("SMTP_AUTH_USER");
        }
        if (getStringParameter("SMTP_AUTH_PWD") != null) {
            SMTP_AUTH_PWD = getStringParameter("SMTP_AUTH_PWD");
        }
        if (getStringParameter("SMTP_HOST_NAME") != null) {
            SMTP_HOST_NAME = getStringParameter("SMTP_HOST_NAME");
        }
        if (getStringParameter("SMTP_PORT") != null) {
            SMTP_PORT = getStringParameter("SMTP_PORT");
        }
        if (getStringParameter("SMTP_SSL") != null) {
            SMTP_SSL = getStringParameter("SMTP_SSL");
        }
        if (getStringParameter("SMTP_AUTH") != null) {
            SMTP_AUTH = getStringParameter("SMTP_AUTH");
        }
        if (getStringParameter("MAIL_FILE_PREFIX") != null) {
            MAIL_FILE_PREFIX = getStringParameter("MAIL_FILE_PREFIX");
        }
    }

    private static String getStringParameter(String paramName) {
        //System.out.println(paramName+"<-"+prop.getProperty(paramName));
        return prop.getProperty(paramName);
    }

    public static void ReadParameters(){
        if (initOK==null){
        try {
            // init config
            loadConfig(new File(SenseOS.getMYMT_HOME() +"/config/WebTranslator.properties"));
            initOK="OK";
        } catch (IOException ex) {
            Logger.getLogger(WebTranslatorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
}
