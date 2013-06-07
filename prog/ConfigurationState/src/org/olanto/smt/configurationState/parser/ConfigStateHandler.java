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
import org.olanto.smt.configurationState.entities.Service;
import java.rmi.RemoteException;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
  */
public class ConfigStateHandler extends DefaultHandler {

    Map<String, Node> nodes;

    private String corpus, source, target, ip, daemon;
    private int port;
    private StringBuffer buffer;

    public ConfigStateHandler(Map nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Nodes can't be null.");
        }
        this.nodes = nodes;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("state")) {
        } else if (qName.equals("translator")) {
        } else if (qName.equals("corpus")) {
            buffer = new StringBuffer();
        } else if (qName.equals("source")) {
            buffer = new StringBuffer();
        } else if (qName.equals("target")) {
            buffer = new StringBuffer();
        } else if (qName.equals("daemon")) {
            buffer = new StringBuffer();
        } else if (qName.equals("list")) {
        } else if (qName.equals("service")) {
        } else if (qName.equals("ip")) {
            buffer = new StringBuffer();
        } else if (qName.equals("port")) {
            buffer = new StringBuffer();
        } else {
            throw new SAXException("Unknown tag : " + qName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("state")) {
        } else if (qName.equals("translator")) {
        } else if (qName.equals("corpus")) {
            corpus = buffer.toString();
        } else if (qName.equals("source")) {
            source = buffer.toString();
        } else if (qName.equals("target")) {
            target = buffer.toString();
        } else if (qName.equals("daemon")) {
            daemon = buffer.toString();
        } else if (qName.equals("list")) {
        } else if (qName.equals("service")) {
            Node node = nodes.get(ip);
            if (node == null) {
                node = new Node(ip);
                nodes.put(ip, node);
            }
            Service service = node.getService(port);
            if (service != null) {
                throw new SAXException("There is two services with the same ip " +
                        "and port number. " + service);
            }
            try {
                service = new Service(ip, port, corpus, source, target, daemon);
            } catch (RemoteException ex) {
                throw new SAXException("RMI error : " + ex);
            }
            node.addService(port, service);
        } else if (qName.equals("ip")) {
            ip = buffer.toString();
        } else if (qName.equals("port")) {
            try {
                port = Integer.parseInt(buffer.toString());
            } catch (NumberFormatException e) {
                throw new SAXException("The port number isn't an integer : " + buffer.toString());
            }
        } else {
            throw new SAXException("Unknown tag : " + qName);
        }
        buffer = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (buffer != null) {
            buffer.append(str);
        }
    }
}
