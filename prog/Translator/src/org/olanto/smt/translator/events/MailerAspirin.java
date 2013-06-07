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

package org.olanto.smt.translator.events;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.masukomi.aspirin.core.MailQue;
import org.masukomi.aspirin.core.SimpleMimeMessageGenerator;
import org.olanto.smt.translator.entities.Text;

/**
 * Cette implémentation du Mailer utilise la librairie Aspirin. Elle marche
 * uniquement sur un serveur référencé par les DNS.
 */
public class MailerAspirin implements TranslatorListener {

    private static final String FROM_MAIL = "simple.test.888@gmail.com";

    private final String[] recipients;
    private final MailQue mailQue;

    public MailerAspirin(String[] recipients) {
        if (recipients == null || recipients.length == 0) {
            throw new IllegalArgumentException("There must be at least one recipient.");
        }
        this.recipients = recipients;
        this.mailQue = new MailQue();
    }

    @Override
    public void endTranslation(Text text) {
        try {
            MimeMessage msg = createMessage();
            // Setting the Subject and Content Type
            msg.setSubject("Translation result");
            msg.setContent("The translation was completed successfuly.\n" +
                    "The result is : \n" + text, "text/plain");
            mailQue.queMail(msg);
        } catch (MessagingException ex) {
            //TODO
            ex.printStackTrace();
        }
    }

    @Override
    public void errorTranslation(Text text, String errorMsg) {
        try {
            MimeMessage msg = createMessage();
            // Setting the Subject and Content Type
            msg.setSubject("Translation result");
            msg.setContent("An errors occurs during the translation.\n" +
                    "The message is : " + errorMsg, "text/plain");
            mailQue.queMail(msg);
        } catch (MessagingException ex) {
            //TODO
            ex.printStackTrace();
        }
    }

    /**
     * Create a message with all parameters set except Subject and Content.
     * @return the created message.
     * @throws MessagingException if an error occurs during the message or
     * session creation.
     */
    private MimeMessage createMessage() throws MessagingException {

        MimeMessage msg = SimpleMimeMessageGenerator.getNewMimeMessage();

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(FROM_MAIL);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        return msg;
    }
}
