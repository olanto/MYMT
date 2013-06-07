/**********
Copyright Â© 2010-2012 Olanto Foundation Geneva

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

import org.olanto.util.smt.utilsmt.ConstStringManager;
import org.olanto.util.smt.utilsmt.SenseOS;
import org.olanto.smt.myTranslator.client.GwtMessage;
import java.io.FileInputStream;
import java.util.Properties;
import org.olanto.smt.myTranslator.client.GwtConstant;
import org.olanto.convsrv.server.UtilsFiles;
import org.olanto.smt.translator.Action;
import org.olanto.smt.translator.Translator;
import org.olanto.smt.translator.UpperCaser;
import org.olanto.smt.translator.entities.Sentence;
import org.olanto.smt.translator.entities.Text;
import org.olanto.smt.translator.events.TranslatorListener;
import org.olanto.smt.translator.exceptions.TranslatorException;
import org.olanto.smt.xliff.UploadedFile;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gwtupload.server.UploadServlet;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import org.olanto.smt.myTranslator.client.rpc.RMIException;

import org.olanto.smt.myTranslator.client.rpc.WebTranslatorService;
import org.apache.commons.fileupload.FileItem;

import static org.olanto.smt.myTranslator.server.Config.*;

/**
 *
 */
public class WebTranslatorServiceImpl extends RemoteServiceServlet implements WebTranslatorService {

    int id = 0;
    boolean verboseTiming = true;
    Properties prop;
    ConstStringManager msg;
    GwtConstant CONST;

    @Override
    public void translateAndMail(String corpus, String source, String target,
            String text, String[] recipients, boolean fromFile)
            throws RMIException, TranslatorException {

        //TODO 
        //TranslatorListener listener = new MailerAspirin(recipients);
        TranslatorListener mailer = new Mailer(recipients);

        // launch the translation
        processWithListener(corpus, source, target, text, fromFile, mailer);
    }

    @Override
    public String translate(String corpus, String source, String target,
            String text, boolean fromFile)
            throws RMIException, TranslatorException {

        // create the listener
        long startTime = 0;
        if (verboseTiming) {
            id++;
            startTime = System.currentTimeMillis();
            System.out.println("start id: " + id);
        }
        ResponseWaiter waiter = new ResponseWaiter();

        // launch the translation
        processWithListener(corpus, source, target, text, fromFile, waiter);

        // wait for a result
        Text textTranslated = waiter.waitFor();

        if (verboseTiming) {
            long processtime = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("stop id: " + id + " process in: " + processtime + " [s]");
        }
        return textTranslated.toString();
    }

    private void processWithListener(String corpus, String source, String target,
            String text, boolean fromFile, TranslatorListener listener)
            throws RMIException, TranslatorException {
        if (corpus == null || corpus.isEmpty()) {
            throw new TranslatorException("The corpus is empty, or not setted.");
        } else if (source == null || source.isEmpty()) {
            throw new TranslatorException("The source language is empty, or not setted.");
        } else if (target == null || target.isEmpty()) {
            throw new TranslatorException("The target language is empty, or not setted.");
        } else if (text == null || text.isEmpty()) {
            throw new TranslatorException("The text source is empty, or not setted.");
        }
        try {

            Text textFormatted;
            if (fromFile) {// get the text from a file
                textFormatted = new Text(corpus, source, target, getContentFromFile(text));
            } else { // translate from text box
                Collection<Sentence> sentences = Text.segmentSentences(text);
                textFormatted = new Text(corpus, source, target, sentences);
            }
            textFormatted.tokenizeSentences();
            Action translator = new Translator(textFormatted);
            System.out.println("Serveur create uppercase");
            Action upperCaser = new UpperCaser();

            // link in the right order
            translator.addNextAction(upperCaser);
            upperCaser.addTranslatorListener(listener);

            // launch the translation
            translator.process(textFormatted);

        } catch (NotBoundException ex) {
            throw new RMIException("The configState is not linked : " + ex.getMessage());
        } catch (MalformedURLException ex) {
            throw new RMIException("URL malformed : " + ex.getMessage());
        } catch (RemoteException ex) {
            throw new RMIException("An RMI error occurs during the Translator "
                    + "initialization. Message : " + ex.getMessage());
        }
    }

    private UploadedFile getContentFromFile(String fieldName) throws TranslatorException {
        List<FileItem> items = UploadServlet.getSessionFileItems(this.getThreadLocalRequest());
        FileItem item = UploadServlet.findItemByFieldName(items, fieldName);
        String fileName = item.getName();
        System.out.println("upload file:" + fileName);
        if (item == null) {
            throw new TranslatorException("Unable to find the file.");
        }
        String text = "ERROR: Unable to find the file.";

        if (fileName.toLowerCase().endsWith(".txt")
                || fileName.toLowerCase().endsWith(".sdlxliff")) { // charge sous forme de txt
            try {
                text = UtilsFiles.file2String(item.getInputStream(), "UTF-8");
                text = text.substring(1);  // !!!!!!!!!!!!!!!!!! skip first char it is ? (bug?)
                //System.out.println("read from up:"+text.substring(0, 10));
                UploadServlet.removeSessionFileItems(this.getThreadLocalRequest());

                return new UploadedFile(text, fileName);
            } catch (IOException ex) {
                Logger.getLogger(WebTranslatorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                byte[] bytes = UtilsFiles.file2byte(item.getInputStream(), (int) item.getSize());
                UploadServlet.removeSessionFileItems(this.getThreadLocalRequest());
                return new UploadedFile(bytes, fileName);
            } catch (IOException ex) {
                Logger.getLogger(WebTranslatorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    { // init config
        ReadParameters();
    }

    @Override
    public GwtConstant InitPropertiesFromFile() {
        //   String fileName = SenseOS.getMYCAT_HOME() + "/config/GUI_fix.xml";
        String fileName = SenseOS.getMYMT_HOME() + "/config/GUI_fix.xml";
        System.out.println("found properties file:" + fileName);
        CONST = new GwtConstant();
        FileInputStream f = null;
        try {
            f = new FileInputStream(fileName);
        } catch (Exception e) {
            System.out.println("cannot find properties file:" + fileName);
            Logger.getLogger(WebTranslatorServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            prop = new Properties();
            prop.loadFromXML(f);
//            prop.list(System.out);
            InitProperties(CONST);
        } catch (Exception e) {
            System.out.println("errors in properties file:" + fileName);
            Logger.getLogger(WebTranslatorServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return CONST;
    }

    @Override
    public GwtMessage InitMessagesFromFile() {

        GwtMessage MSG = new GwtMessage();
        try {
            System.out.println("messages" + CONST.INTERFACE_MESSAGE_PATH);
            //     msg = new ConstStringManager("C:/MYMT/prog/WebTranslator/src/java/org/olanto/smt/myTranslator/messages/initclient_fr.properties");
            msg = new ConstStringManager(SenseOS.getMYMT_HOME() + CONST.INTERFACE_MESSAGE_PATH
                    + "_" + CONST.INTERFACE_MESSAGE_LANG + ".properties");
            InitProperties(MSG);
        } catch (IOException ex) {
            Logger.getLogger(WebTranslatorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return MSG;
    }

    private void InitProperties(GwtConstant CONST) {

        CONST.EXP_DAYS = Integer.parseInt(prop.getProperty("EXP_DAYS"));
        CONST.EXTENSION_SET = prop.getProperty("EXTENSION_SET");
        CONST.CORPUS_SET = prop.getProperty("CORPUS_SET");
        CONST.LANG_SET = prop.getProperty("LANG_SET");
        CONST.INTERFACE_MESSAGE_PATH = prop.getProperty("INTERFACE_MESSAGE_PATH");
        CONST.INTERFACE_MESSAGE_LANG = prop.getProperty("INTERFACE_MESSAGE_LANG");

    }

    private void InitProperties(GwtMessage MSG) {

        MSG.MSG00 = msg.get("MSG00");
        MSG.MSG01 = msg.get("MSG01");
        MSG.MSG02 = msg.get("MSG02");
        MSG.MSG03 = msg.get("MSG03");
        MSG.MSG04 = msg.get("MSG04");
        MSG.MSG05 = msg.get("MSG05");
        MSG.MSG06 = msg.get("MSG06");
        MSG.MSG07 = msg.get("MSG07");
        MSG.MSG08 = msg.get("MSG08");
        MSG.MSG09 = msg.get("MSG09");
        MSG.MSG10 = msg.get("MSG10");
        MSG.MSG11 = msg.get("MSG11");
        MSG.MSG12 = msg.get("MSG12");
        MSG.MSG13 = msg.get("MSG13");
        MSG.MSG14 = msg.get("MSG14");
        MSG.MSG15 = msg.get("MSG15");
        MSG.MSG16 = msg.get("MSG16");
        MSG.MSG17 = msg.get("MSG17");
        MSG.MSG18 = msg.get("MSG18");
        MSG.MSG19 = msg.get("MSG19");
        MSG.MSG20 = msg.get("MSG20");
        MSG.MSG21 = msg.get("MSG21");
        MSG.MSG22 = msg.get("MSG22");
        MSG.MSG23 = msg.get("MSG23");
        MSG.MSG24 = msg.get("MSG24");
        MSG.MSG25 = msg.get("MSG25");
        MSG.MSG26 = msg.get("MSG26");
        MSG.MSG27 = msg.get("MSG27");
        MSG.MSG28 = msg.get("MSG28");
        MSG.MSG29 = msg.get("MSG29");
    }
}
