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


package org.olanto.smt.xliff;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
  */
public class Xliff {

    org.jdom.Document document;
    Element racine;
    String mode = "";  // GET, PUT
    List<String> sourceSeg;
    String[] targetSeg;


    public Xliff(){}

    public static void main(String[] args) { // only for debugging

        Xliff xliff = new Xliff();
        //xliff.initFromFile("C:/SIMPLE/data/WTDS367-18.doc_en-US_fr-FR-XX.sdlxliff");
        String s=Xliff.file2String("C:/SIMPLE/data/test.sdlxliff", "UTF-8");
        System.out.println("sdlxlfiff:"+s.substring(0, 100));
        
        xliff.initFromString(s);

        xliff.getSourceSeg();
         String[] dummy = new String[xliff.sourceSeg.size()];
        for (int i = 0; i < dummy.length; i++) {
            dummy[i] = "$$$$$" + (i + 1) + "$$$$$";
        }
        xliff.setTargetSeg(dummy);
        xliff.saveAsFile("C:/SIMPLE/data/WTDS367-18.doc_en-US_fr-FR-XX.sdlxliff");
        msg(xliff.saveAsString(),true);
    }

    public void initFromFile(String fileName) {
        //On crée une instance de SAXBuilder
        SAXBuilder sxb = new SAXBuilder();
        try {
            document = sxb.build(new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        racine = document.getRootElement();
    }
    
    public void initFromString(String txt) {
        //On crée une instance de SAXBuilder
        SAXBuilder sxb = new SAXBuilder();
        try {
            document = sxb.build(new StringReader(txt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        racine = document.getRootElement();
    }

    public static String[] list2tab(List<String> list) {
        String[] b = new String[list.size()];
        list.toArray(b);
        return b;
    }

    public String[] getSourceSeg() {
        boolean verbose = true;

        mode = "GET";
        sourceSeg = new Vector<String>();
        ExloreXLIFF(racine);
        String[] res = new String[sourceSeg.size()];
        sourceSeg.toArray(res);
        msg("# tagetsegs:" + sourceSeg.size(), verbose);
        if (verbose) {
            for (int i = 0; i < res.length; i++) {
                msg(i + ":" + res[i], verbose);
            }
        }
        return res;
    }

    public void setTargetSeg(String[] targetSeg) {

        boolean verbose = true;
        mode = "PUT";
        this.targetSeg = targetSeg;
        msg("# targetsegs:" + targetSeg.length, verbose);
        if (verbose) {
            for (int i = 0; i < targetSeg.length; i++) {
                msg(i + ":" + targetSeg[i], verbose);
            }
        }
        ExloreXLIFF(racine);
    }

   public void showXML() {
        try {
            XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
            output.output(document, System.out);
        } catch (java.io.IOException e) {
        }
    }

   public void saveAsFile(String fichier) {
        try {
            XMLOutputter output = new XMLOutputter(Format.getRawFormat());
            output.output(document, new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8"));
        } catch (java.io.IOException e) {
        }
    }

     public static final String file2String(String fname, String txt_encoding) {
        StringBuffer txt = new StringBuffer("");
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fname), txt_encoding);
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                txt.append(w);
                txt.append("\n");
                w = in.readLine();
            }
            return txt.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
   public String saveAsString() {
             StringWriter s=new StringWriter();
      try {
            XMLOutputter output = new XMLOutputter(Format.getRawFormat());
             output.output(document, s);
        } catch (java.io.IOException e) {
        }
      return  s.toString();
    }

    static void msg(String s, boolean verbose) {
        if (verbose) {
            System.out.println(s);
        }
    }

    void ExloreXLIFF(Element root) {
        boolean verbose = false;
        msg("root:" + root.getName(), verbose);
        List<Element> xliffElem = root.getChildren();
        Iterator i = xliffElem.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            if (courant.getName().equals("trans-unit")) { // on recherche les trans-unit
                processTransUnit(courant);
            }
            ExloreXLIFF(courant);
        }
    }

    boolean istranslate(Element ta) {  // test si la trans unit est à traduire
        boolean verbose = false;
        if (ta.getName().equals("trans-unit")) {
            Attribute ata = ta.getAttribute("translate");
            if (ata != null) {
                String ataval = ata.getValue();
                msg("attribute translate=" + ataval, verbose);
                if (ataval.equals("no")) {
                    return false;
                }
            }
            msg("no attribute translate=...", verbose);
            return true;
        } else {
            msg("error in istranslate must be a <trans-unit>", verbose);
        }
        return false;
    }

    String getid(Element ta) {  // cherche le l'id de la transunit
        boolean verbose = false;
        if (ta.getName().equals("trans-unit")) {
            Attribute ata = ta.getAttribute("id");
            if (ata != null) {
                String ataval = ata.getValue();
                msg("attribute id=", verbose);
                return ataval;
            }
            System.out.println("no attribute id=...");
            return "";
        } else {
            System.out.println("error in getid, must be a <trans-unit>");
        }
        return "";
    }

    String getmid(Element mrk) { // cherche le l'id de la mrk
        boolean verbose = false;
        if (mrk.getName().equals("mrk")) {
            Attribute ata = mrk.getAttribute("mid");
            if (ata != null) {
                String ataval = ata.getValue();
                msg("attribute mid=", verbose);
                return ataval;
            }
            System.out.println("no attribute mid=...");
            return "";
        } else {
            System.out.println("error in getmid, must be a <mrk>");
        }
        return "";
    }

    String processinternalTag(Element elem, String nice) {
        boolean verbose = false;
        List<Element> xliffElem = elem.getContent();
        Iterator i = xliffElem.iterator();
        String text = "";
        while (i.hasNext()) {
            Object courant = (Object) i.next();
            msg(nice + courant.getClass().getCanonicalName(), verbose);
            if (courant.getClass().getCanonicalName().equals("org.jdom.Text")) {
                if (text.equals("")) {
                    text += " ";
                }
                text += ((Text) courant).getTextNormalize();
            } else if (courant.getClass().getCanonicalName().equals("org.jdom.Element")) {
                Element e = (Element) courant;
                if (verbose) {
                    System.out.println(nice + e.getName());
                }
                if (text.equals("")) {
                    text += " ";
                }
                text += processinternalTag(e, "-" + nice);
            } else {
                System.out.println(nice + "Error for :" + courant.getClass().getCanonicalName());
            }
        }

        return text;
    }

    Element getMrkElement(Element root, String taid, boolean sourcetype) {
        boolean verbose = false;
        Element res = null;
        //On crée une List contenant tous les noeuds de l'Element racine
        msg("from getMrkElement root:" + root.getName(), verbose);
        if (root.getName().equals("mrk")) {
            msg("from getMrkElement return root:" + root.getName(), verbose);
            return root;
        } else if (sourcetype) {
            processMrkInSegSource(root, taid);
        } else {
            processMrkInTarget(root, taid);
        }
        return res;
    }

    void processMrkInSegSource(Element elem, String taid) {
        boolean verbose = false;
        List<Element> xliffElem = elem.getChildren();
        Iterator i = xliffElem.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            msg("from processMrkInSegSource:" + courant.getName(), verbose);
            Element mrkelem = getMrkElement(courant, taid, true);
            if (mrkelem != null) {
                msg("from processMrkInSegSource mrk found:" + mrkelem.getName(), verbose);
                msg("trans-unit id=" + taid, verbose);
                msg("(seg-source) " + mrkelem.getName() + " mid=" + getmid(mrkelem), verbose);
                String sourceTxt = processinternalTag(mrkelem, "- ");
                sourceSeg.add(sourceTxt);
                msg(sourceTxt, verbose);
            }
        }
    }

    void processMrkInTarget(Element elem, String taid) {
        boolean verbose = true;
        List<Element> xliffElem = elem.getChildren();
        Iterator i = xliffElem.iterator();
        while (i.hasNext()) {
            Element courant = (Element) i.next();
            msg(courant.getName(), verbose);
            Element mrkelem = getMrkElement(courant, taid, false);
            if (mrkelem != null) {
                String mid = getmid(mrkelem);
                msg("(target) " + mrkelem.getName() + " mid=" + mid, verbose);
                mrkelem.setText(targetSeg[Integer.parseInt(mid)-1]);
            }
        }
    }

    void processTransUnit(Element ta) {
        boolean verbose = false;

        msg("ta:" + ta.getName(), verbose);
        if (istranslate(ta)) {
            String taid = getid(ta);
            msg("id:" + getid(ta), verbose);
            List<Element> xliffElem = ta.getChildren();

            //On crée un Iterator sur notre liste
            Iterator i = xliffElem.iterator();
            while (i.hasNext()) {
                Element courant = (Element) i.next();
                //On affiche le nom de l'element courant
                msg(courant.getName(), verbose);
                if (courant.getName().equals("source")) {
//                    System.out.println(courant.getName());
//                    System.out.println(courant.getTextTrim());
                    ;
                }
                if (courant.getName().equals("seg-source")) {
                    msg("from ta :" + courant.getName(), verbose);
                    if (mode.equals("GET")) {
                        processMrkInSegSource(courant, taid);
                    }
                    ;
                }
                if (courant.getName().equals("target")) {
                    msg(courant.getName(), verbose);
                    if (mode.equals("PUT")) {
                        processMrkInTarget(courant, taid);
                    }
                    ;
                }
            }
        } else {
            msg("sgkip this ...", verbose);
        }
    }
}


