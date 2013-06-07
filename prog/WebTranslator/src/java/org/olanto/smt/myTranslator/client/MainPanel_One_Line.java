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

package org.olanto.smt.myTranslator.client;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import org.olanto.smt.myTranslator.client.rpc.RMIException;
import org.olanto.smt.myTranslator.client.rpc.WebTranslatorService;
import org.olanto.smt.myTranslator.client.rpc.WebTranslatorServiceAsync;
import org.olanto.smt.translator.exceptions.TranslatorException;

/**
 * La panel principal contenant tous les composants graphiques.
 */
public class MainPanel_One_Line extends VerticalPanel {

    private WebTranslatorServiceAsync service = GWT.create(WebTranslatorService.class);

    //TODO changer place des constantes
    private static final String[] CORPUS= {"test", "ruen"};
    private static final String[] LANGS = {"en", "fr", "ru"};

    private Label lblServerReply = new Label();
   // private TextBox txtCorpus = new TextBox();
    private ListBox lbCorpus = new ListBox(false);
    private ListBox lbSource = new ListBox(false);
    private ListBox lbTarget = new ListBox(false);

    private TextArea taTextSource = new TextArea();
    private FileUpload fileUpload = new FileUpload();


    private TextArea taTextDest = new TextArea();
    private TextBox txtMailTO = new TextBox();

    
    private Button btnSend;
    
    public MainPanel_One_Line() {
        this.setSpacing(10);

        this.add(buildIntroPanel());
        this.add(buildOptionPanel());
        this.add(buildSourcePanel());
        this.add(buildDestPanel());
        this.add(buildButtonPanel());

        
    }

    private Widget buildIntroPanel() {
        Label label = new HTML(Resources.INSTANCE.getText().getText());
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

        optionPan.setHTML(0, 0, "<h4>Options</h4>");
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        optionPan.setHTML(1, 0, "Corpus:");
        optionPan.setWidget(1, 1, lbCorpus);
        optionPan.setHTML(2, 0, "Source:");
        optionPan.setWidget(2, 1, lbSource);
        optionPan.setHTML(3, 0, "Target:");
        optionPan.setWidget(3, 1, lbTarget);

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(optionPan);

        return decPan;
    }

    private Widget buildSourcePanel() {
        taTextSource.setSize("400px", "150px");

        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        layout.setHTML(0, 0, "<h4>Source</h4>");
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        layout.setWidget(1, 0, taTextSource);
        layout.setHTML(2, 0, "OR");
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

        FlexTable layout = new FlexTable();
        layout.setCellSpacing(6);
        FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

        layout.setHTML(0, 0, "<h4>Destination</h4>");
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        layout.setWidget(1, 0, taTextDest);
        layout.setHTML(2, 0, "OR");
        cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
        layout.setWidget(3, 0, txtMailTO);

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(layout);

        return decPan;
    }

    private Widget buildButtonPanel() {
        btnSend = new Button("Send to server");

        // Create an asynchronous callback to handle the result.
        final AsyncCallback<Void> callbackMail = new AsyncCallback<Void>() {
            public void onSuccess(Void v) {
                lblServerReply.setText("Request correctly send.");
            }

            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (RMIException ex) {
                    lblServerReply.setText("RMI error : " + ex.getMessage());
                } catch (TranslatorException ex) {
                    lblServerReply.setText("Translation failed : " + ex.getMessage());
                } catch (Throwable ex) {
                    lblServerReply.setText("Request failed");
                }
            }
        };

        final AsyncCallback<String> callbackString = new AsyncCallback<String>() {
            public void onSuccess(String text) {
                lblServerReply.setText("Translation successful");
                taTextDest.setText(text);
            }

            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (RMIException ex) {
                    lblServerReply.setText("RMI error : " + ex.getMessage());
                } catch (TranslatorException ex) {
                    lblServerReply.setText("Translation failed : " + ex.getMessage());
                } catch (Throwable ex) {
                    lblServerReply.setText("Request failed");
                }
            }
        };

        // Listen for the button clicks
        btnSend.addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent event) {
              //  String corpus = txtCorpus.getText();
                String corpus = lbCorpus.getValue(lbCorpus.getSelectedIndex());
                String source = lbSource.getValue(lbSource.getSelectedIndex());
                String target = lbTarget.getValue(lbTarget.getSelectedIndex());
                String text = null;

                if (fileUpload.getFilename().isEmpty()) {
                    text = taTextSource.getText();
                } else {
                    // not implemented
                }

                if (txtMailTO.getText().isEmpty()) {
                    service.translate(corpus, source, target, text, false,
                            callbackString);
                } else {
                    String[] recipients = new String[]{txtMailTO.getText()};
                    service.translateAndMail(corpus, source, target, text,
                            recipients, false, callbackMail);
                }

                lblServerReply.setText("wait...");
            }
        });

        FlexTable buttonPan = new FlexTable();
        buttonPan.setCellSpacing(6);
        FlexCellFormatter cellFormatter = buttonPan.getFlexCellFormatter();

        buttonPan.setWidget(0, 0, btnSend);
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

        buttonPan.setHTML(1, 0, "Status:");
        buttonPan.setWidget(1, 1, lblServerReply);

        // Wrap the option panel in a DecoratorPanel
        DecoratorPanel decPan = new DecoratorPanel();
        decPan.setWidget(buttonPan);

        return decPan;
    }

}
