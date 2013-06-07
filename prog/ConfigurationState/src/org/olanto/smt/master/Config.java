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
package org.olanto.smt.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Cette classe contient toutes les constantes utiles au Master.
 * Les constantes ont une valeur par défaut qui peut être modifiée
 * avec le fichier de configuration "masterConf.properties".
 */
public class Config {

    public static String RMI_HOST = "localhost";

    public static int LOCAL_MASTER_TIMEOUT = 1000; // 1s
 
    public static long START_TIME = 10000; // 20s
    public static long STOP_TIME = 1000; // 1s
    public static long DAEMON_SLEEP_TEST = 600000; // 10m
    public static long DAEMON_SLEEP_START = 60000; // 1m

    public static String TEST_INPUT_STRING = "$$$";
    public static String TEST_OUTPUT_STRING = "$$$ |0-0| ";

    public static void loadConfig(File configFile) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(configFile));

        //TODO remove
        prop.list(System.out);

        String sValue;
        int iValue;

        try {
            sValue = prop.getProperty("LOCAL_MASTER_TIMEOUT");
            if (sValue != null) {
                iValue = Integer.parseInt(sValue);
                LOCAL_MASTER_TIMEOUT = iValue;
            }
        } catch (NumberFormatException e) {
            //TODO
            System.out.println("Warning : ....");
        }

 
        try {
            sValue = prop.getProperty("START_TIME");
            if (sValue != null) {
                iValue = Integer.parseInt(sValue);
                START_TIME = iValue;
            }
        } catch (NumberFormatException e) {
            //TODO
            System.out.println("Warning : ....");
        }

        try {
            sValue = prop.getProperty("STOP_TIME");
            if (sValue != null) {
                iValue = Integer.parseInt(sValue);
                STOP_TIME = iValue;
            }
        } catch (NumberFormatException e) {
            //TODO
            System.out.println("Warning : ....");
        }

        try {
            sValue = prop.getProperty("DAEMON_SLEEP_TEST");
            if (sValue != null) {
                iValue = Integer.parseInt(sValue);
                DAEMON_SLEEP_TEST = iValue;
            }
        } catch (NumberFormatException e) {
            //TODO
            System.out.println("Warning : ....");
        }

        try {
            sValue = prop.getProperty("DAEMON_SLEEP_START");
            if (sValue != null) {
                iValue = Integer.parseInt(sValue);
                DAEMON_SLEEP_START = iValue;
            }
        } catch (NumberFormatException e) {
            //TODO
            System.out.println("Warning : ....");
        }

        sValue = prop.getProperty("RMI_HOST");
        if (sValue != null) {
            RMI_HOST = sValue;
        }

        sValue = prop.getProperty("TEST_INPUT_STRING");
        if (sValue != null) {
            TEST_INPUT_STRING = sValue;
        }

        sValue = prop.getProperty("TEST_OUTPUT_STRING");
        if (sValue != null) {
            TEST_OUTPUT_STRING = sValue;
        }
    }
}
