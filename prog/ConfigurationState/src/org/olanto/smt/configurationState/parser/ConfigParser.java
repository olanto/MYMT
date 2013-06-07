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
package org.olanto.smt.configurationState.parser;

import org.olanto.smt.configurationState.entities.Node;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
  */
public class ConfigParser {

    SAXParser parser;

    public ConfigParser() throws SAXException {
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException ex) {
            throw new SAXException("Configuration Exception : " + ex.getMessage());
        }
    }

    public Map<String, Node> parse(File configFile) throws SAXException, IOException {
        Map<String, Node> nodes = new HashMap<String, Node>();
        DefaultHandler handler = new ConfigStateHandler(nodes);
        parser.parse(configFile, handler);
        return nodes;
    }
}
