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
package org.olanto.util.smt.utilsmt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConstStringManager {

    private Properties prop;

    /**
     * Charge la liste des propriétés contenu dans le fichier spécifié
     *
     * @param filename le fichier contenant les propriétés
     * @return un objet Properties contenant les propriétés du fichier
     */
    public String get(String property) {
        try {
            return prop.getProperty(property, property + "_UNDEFINED");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property + "_UNDEFINED";
    }

    public ConstStringManager(String filename) throws IOException, FileNotFoundException {
        prop = new Properties();
        FileInputStream input = new FileInputStream(filename);
        try {
            prop.load(input);
        } finally {
            input.close();
        }
    }
}
