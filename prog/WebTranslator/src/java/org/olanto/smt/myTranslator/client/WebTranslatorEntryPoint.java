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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import org.olanto.smt.myTranslator.client.rpc.WebTranslatorService;
import org.olanto.smt.myTranslator.client.rpc.WebTranslatorServiceAsync;

/**
 * Main entry point.
 *
 */
public class WebTranslatorEntryPoint implements EntryPoint {

    private WebTranslatorServiceAsync service = GWT.create(WebTranslatorService.class);

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
        final AsyncCallback<GwtMessage> callbackmsgs = new AsyncCallback<GwtMessage>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException(GuiMessage.MSG00);
            }

            @Override
            public void onSuccess(GwtMessage msgs) {
                InitMessages(msgs);
                initCookies();
                RootPanel.get().add(new MainPanel());
            }
        };
        final AsyncCallback<GwtConstant> callbackprops = new AsyncCallback<GwtConstant>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException(GuiMessage.MSG00);
            }

            @Override
            public void onSuccess(GwtConstant props) {
                InitProperties(props);
                service.InitMessagesFromFile(callbackmsgs);

            }
        };

        service.InitPropertiesFromFile(callbackprops);


    }

    private void InitProperties(GwtConstant CONST) {

        GuiConstant.EXP_DAYS = CONST.EXP_DAYS;
        GuiConstant.EXTENSION_SET = CONST.EXTENSION_SET;
        GuiConstant.CORPUS_SET = CONST.CORPUS_SET;
        GuiConstant.LANG_SET = CONST.LANG_SET;
        GuiConstant.INTERFACE_MESSAGE_PATH = CONST.INTERFACE_MESSAGE_PATH;
        GuiConstant.INTERFACE_MESSAGE_LANG = CONST.INTERFACE_MESSAGE_LANG;
    }

    private void InitMessages(GwtMessage MSG) {

        GuiMessage.MSG00 = MSG.MSG00;
        GuiMessage.MSG01 = MSG.MSG01;
        GuiMessage.MSG02 = MSG.MSG02;
        GuiMessage.MSG03 = MSG.MSG03;
        GuiMessage.MSG04 = MSG.MSG04;
        GuiMessage.MSG05 = MSG.MSG05;
        GuiMessage.MSG06 = MSG.MSG06;
        GuiMessage.MSG07 = MSG.MSG07;
        GuiMessage.MSG08 = MSG.MSG08;
        GuiMessage.MSG09 = MSG.MSG09;
        GuiMessage.MSG10 = MSG.MSG10;
        GuiMessage.MSG11 = MSG.MSG11;
        GuiMessage.MSG12 = MSG.MSG12;
        GuiMessage.MSG13 = MSG.MSG13;
        GuiMessage.MSG14 = MSG.MSG14;
        GuiMessage.MSG15 = MSG.MSG15;
        GuiMessage.MSG16 = MSG.MSG16;
        GuiMessage.MSG17 = MSG.MSG17;
        GuiMessage.MSG18 = MSG.MSG18;
        GuiMessage.MSG19 = MSG.MSG19;
        GuiMessage.MSG20 = MSG.MSG20;
        GuiMessage.MSG21 = MSG.MSG21;
        GuiMessage.MSG22 = MSG.MSG22;
        GuiMessage.MSG23 = MSG.MSG23;
        GuiMessage.MSG24 = MSG.MSG24;
        GuiMessage.MSG25 = MSG.MSG25;
        GuiMessage.MSG26 = MSG.MSG26;
        GuiMessage.MSG27 = MSG.MSG27;
        GuiMessage.MSG28 = MSG.MSG28;
        GuiMessage.MSG29 = MSG.MSG29;
    }

    // initialise all cookies in the client navigator if not existing
    private void initCookies() {
        MyMTCookies.initCookie("MyMTEmail", GuiMessage.MSG01);
    }
}
