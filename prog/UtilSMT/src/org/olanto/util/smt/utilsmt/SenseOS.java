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
package org.olanto.util.smt.utilsmt;

import java.util.Map;

/** 
 * Gestion de l'OS et des dépendances d'installation
 */
public class SenseOS {

    private static final String WINDOWS_FAMILIES = "WINDOWS_FAMILIES";
    private static final String UNIX_FAMILIES = "UNIX_FAMILIES";
    private static final String DEF_WINDOWS_HOME = "C:/";
    private static final String DEF_UNIX_HOME = "/home/olanto/";
    private static String OS_TYPE = null;
    private static String MYMT_HOME = null;
    private static  Map<String,String> env;
 
    
    /**
     * permet d'effectuer le test de cette classe
     * @param args pas utilisés
     */
    public static void main(String args[]) {
        
       System.out.println("OS_TYPE:"+getOS_TYPE());
       System.out.println("MYMT_HOME:"+getMYMT_HOME());
       System.out.println("NUMBER_OF_PROCESSORS:"+getENV("NUMBER_OF_PROCESSORS"));
    }

    /**
     * @return the OS_TYPE
     */
    public static String getOS_TYPE() {
        if (OS_TYPE == null) {//
            String runningOS = System.getProperty("os.name");
            System.out.println("running OS:" + runningOS);
            if (runningOS.startsWith("Window")) {
                OS_TYPE = WINDOWS_FAMILIES;
            } else {  // pour le moment tous les autres sont des Unix !!!
                OS_TYPE = UNIX_FAMILIES;
            }
            env=System.getenv();
        }
        return OS_TYPE;
    }

    /** permet de forcer le type de OS
     * @param aOS_TYPE the OS_TYPE to set
     */
    public static void setOS_TYPE(String aOS_TYPE) {
        OS_TYPE = aOS_TYPE;
    }

    /** retourne le home de myCAT
     * @return AT_HOMEthe MYC
     */
    public static String getMYMT_HOME() {
          if (MYMT_HOME == null) {// par encore initialisée
              if (env==null) { // init env
                  getOS_TYPE();
              }
             String res= env.get("MYMT_HOME");
             if (res==null){ // pas défini
                 if(getOS_TYPE().equals(WINDOWS_FAMILIES))
                     MYMT_HOME=DEF_WINDOWS_HOME+"MYMT";
                 else
                     MYMT_HOME=DEF_UNIX_HOME+"MYMT";
             }
             else MYMT_HOME=res;
          }    
        return MYMT_HOME;
    }
 
    
    /** retourne la valeur d'une variable de l'environnement
     * @return the env value
     */
    public static String getENV(String envName) {
        return env.get(envName);
    }

   /** permet de forcer le home de myCAT
     * @param HOME to set
     */
    public static void setMYMT_HOME(String aMYMT) {
        MYMT_HOME = aMYMT;
    }
    
    
}
