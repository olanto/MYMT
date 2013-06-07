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

package org.olanto.smt.myTranslator.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import org.olanto.smt.myTranslator.client.rpc.RMIException;
import org.olanto.smt.myTranslator.client.rpc.WebTranslatorService;
import org.olanto.smt.myTranslator.client.rpc.WebTranslatorServiceAsync;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 * La panel principal contenant tous les composants graphiques.
 */
public class MainPanel extends FlexTable {

    private WebTranslatorServiceAsync service = GWT.create(WebTranslatorService.class);
    //TODO changer place des constantes
    private static final String[] CORPUS = GuiConstant.CORPUS_SET.split("\\;");
      
    private static final String[] LANGS = GuiConstant.LANG_SET.split("\\;");
    private static final String[] EXTS = GuiConstant.EXTENSION_SET.split("\\;");
     private Label lblServerReply = new Label();
    // private TextBox txtCorpus = new TextBox();
    private ListBox lbCorpus = new ListBox(false);
    private ListBox lbSource = new ListBox(false);
    private ListBox lbTarget = new ListBox(false);
    private TextArea taTextSource = new TextArea();
    private SingleUploader fileUpload;
    private String fileField = null;
    private TextArea taTextDest = new TextArea();
    private TextBox txtMailTO = new TextBox();
    private Button btnSend;

    public MainPanel() {

        this.setCellSpacing(6);
        FlexCellFormatter cellFormatter = this.getFlexCellFormatter();

        this.setWidget(0, 0, buildIntroPanel());
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        this.setWidget(1, 0, buildOptionPanel());

        this.setWidget(2, 0, buildSourcePanel());
        this.setWidget(2, 1, buildDestPanel());

        this.setWidget(3, 0, buildButtonPanel());


    }

    private Widget buildIntroPanel() {
     //   Label label = new HTML(Resources.INSTANCE.getText().getText());
        Label label = new HTML(GuiMessage.MSG20);
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(label);
        return decPan;
    }

    private Widget buildOptionPanel() {
        for (int i = 0; i < LANGS.length; i++) {
            this.lbSource.addItem(LANGS[i]);
            this.lbTarget.addItem(LANGS[i]);
        }
        for (int i = 0; i < CORPUS.length; i++) {
            this.lbCorpus.addItem(CORPUS[i]);
        }

        this.lbSource.setSelectedIndex(0); // en
        this.lbTarget.setSelectedIndex(1); // fr


        FlexTable optionPan = new FlexTable();
        optionPan.setCellSpacing(6);
        FlexCellFormatter cellFormatter = optionPan.getFlexCellFormatter();

        optionPan.setHTML(0, 2, GuiMessage.MSG02);
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

     //   optionPan.setHTML(1, 0, "Corpus:");
      optionPan.setHTML(1, 0, GuiMessage.MSG03);
      optionPan.setWidget(1, 1, lbCorpus);
        optionPan.setHTML(1, 2, GuiMessage.MSG04);
        optionPan.setWidget(1, 3, lbSource);
        optionPan.setHTML(1, 4, GuiMessage.MSG05);
        optionPan.setWidget(1, 5, lbTarget);

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(optionPan);

        return decPan;
    }

    private Widget buildSourcePanel() {
        taTextSource.setSize("400", "150");


        IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {

            @Override
            public void onFinish(IUploader uploader) {
                if (uploader.getStatus() == Status.SUCCESS) {

                    // need gwtupload version 0.6.3 to use this feature
                    IUploader.UploadedInfo info = uploader.getServerInfo();
                    fileField = info.field;

                    // old implementation
                    /*
                    Document doc = XMLParser.parse(uploader.getServerResponse());
                    fileField = Utils.getXmlNodeValue(doc, "file-1-field");
                     */

                    lblServerReply.setText(GuiMessage.MSG06);
                } else {
                    lblServerReply.setText(GuiMessage.MSG07);
                }
            }
        };


        fileUpload = new SingleUploader();
        fileUpload.setValidExtensions("txt", "sdlxliff", "docx", "doc", "pdf");
       // fileUpload.setValidExtensions(EXTS);
        fileUpload.addOnFinishUploadHandler(onFinishUploaderHandler);
        fileUpload.setAutoSubmit(true);

        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        layout.setHTML(0, 0, GuiMessage.MSG08);
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        layout.setWidget(1, 0, taTextSource);
        layout.setHTML(2, 0, GuiMessage.MSG09);
        cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
        layout.setWidget(3, 0, fileUpload);

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(layout);

        return decPan;
    }

    private Widget buildDestPanel() {
        taTextDest.setReadOnly(true);
        taTextDest.setSize("400px", "150px");
        txtMailTO.setWidth("350px");
        txtMailTO.setText(Cookies.getCookie("MyMTEmail"));

        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        layout.setHTML(0, 0, GuiMessage.MSG10);
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        layout.setWidget(1, 0, taTextDest);
        layout.setHTML(2, 0, GuiMessage.MSG11);
        cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
        layout.setWidget(3, 0, txtMailTO);
        txtMailTO.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
               MyMTCookies.updateCookie("MyMTEmail", txtMailTO.getText());
            }
        }); 

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(layout);

        return decPan;
    }

    private Widget buildButtonPanel() {
        btnSend = new Button(GuiMessage.MSG12);

        // Create an asynchronous callback to handle the result.
        final AsyncCallback<Void> callbackMail = new AsyncCallback<Void>() {

            public void onSuccess(Void v) {
                lblServerReply.setText(GuiMessage.MSG13);
            }

            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (RMIException ex) {
                    lblServerReply.setText(GuiMessage.MSG14 + ex.getMessage());
                } catch (TranslatorException ex) {
                    lblServerReply.setText(GuiMessage.MSG15 + ex.getMessage());
                } catch (Throwable ex) {
                    lblServerReply.setText(GuiMessage.MSG16);
                }
            }
        };

        final AsyncCallback<String> callbackString = new AsyncCallback<String>() {

            public void onSuccess(String text) {
                lblServerReply.setText(GuiMessage.MSG17);
                taTextDest.setText(text);
            }

            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (RMIException ex) {
                    lblServerReply.setText(GuiMessage.MSG14  + ex.getMessage());
                } catch (TranslatorException ex) {
                    lblServerReply.setText(GuiMessage.MSG15  + ex.getMessage());
                } catch (Throwable ex) {
                    lblServerReply.setText(GuiMessage.MSG16 );
                }
            }
        };

        // Listen for the button clicks
        btnSend.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                //  String corpus = txtCorpus.getText();
                taTextDest.setText("");
                String corpus = lbCorpus.getValue(lbCorpus.getSelectedIndex());
                String source = lbSource.getValue(lbSource.getSelectedIndex());
                String target = lbTarget.getValue(lbTarget.getSelectedIndex());
                String text = null;
                boolean fromFile;

                if (fileField == null) {
                    fromFile = false;
                    text = taTextSource.getText();
                    if (text.charAt(text.length() - 1) != '.') {
                        text += "."; // ajouter un point
                    }
                } else {
                    text = fileField;
                    fromFile = true;
                    /*
                    if (fileUpload.getStatus() != Status.SUCCESS) {
                    lblServerReply.setText("please wait while uploading...");
                    return;
                    }
                     */
                }

                if (txtMailTO.getText().isEmpty()) {
                    service.translate(corpus, source, target, text, fromFile,
                            callbackString);
                } else {
                    String[] recipients = new String[]{txtMailTO.getText()};
                    service.translateAndMail(corpus, source, target, text,
                            recipients, fromFile, callbackMail);
                }

                fileField = null;
                fileUpload.reuse();
                lblServerReply.setText(GuiMessage.MSG18);
            }
        });

        FlexTable buttonPan = new FlexTable();
        buttonPan.setCellSpacing(6);
        FlexCellFormatter cellFormatter = buttonPan.getFlexCellFormatter();

        buttonPan.setWidget(0, 0, btnSend);
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        buttonPan.setHTML(1, 0, GuiMessage.MSG19);
        buttonPan.setWidget(1, 1, lblServerReply);

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(buttonPan);

        return decPan;
    }
}
