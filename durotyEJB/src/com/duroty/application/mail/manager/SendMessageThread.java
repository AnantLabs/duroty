/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/
package com.duroty.application.mail.manager;

import com.duroty.application.mail.exceptions.MailException;

import org.apache.commons.mail.Email;

import java.util.Vector;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class SendMessageThread implements Runnable {
    /** DOCUMENT ME! */
    private Email email;

    /**
     * DOCUMENT ME!
     */
    private MimeMessage mime;

    /**
     * DOCUMENT ME!
     */
    private Vector mimes;

    /**
     * Create a thread of execution for sending the given message, and saving
     * the message to the configured 'sent' folder if necessary.
     *
     * @param message message to send
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public SendMessageThread(Email email) throws MessagingException {
        super();
        this.email = email;
    }

    /**
     * Create a thread of execution for sending the given message, and saving
     * the message to the configured 'sent' folder if necessary.
     *
     * @param message message to send
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public SendMessageThread(MimeMessage mime) throws MessagingException {
        super();
        this.mime = mime;
    }

    /**
     * Create a thread of execution for sending the given message, and saving
     * the message to the configured 'sent' folder if necessary.
     *
     * @param message message to send
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public SendMessageThread(Vector mimes) throws MessagingException {
        super();
        this.mimes = mimes;
    }

    /**
     * <p>
     * Implementation of Thread.run()
     * </p>
     */
    public void start() throws Exception {
        try {
            if (email != null) {
                email.getMimeMessage().removeHeader("X-DBox");
                email.send();

                return;
            }

            if (mime != null) {
                mime.removeHeader("X-DBox");
                Transport.send(mime);

                return;
            }

            if (mimes != null) {
                for (int i = 0; i < mimes.size(); i++) {
                    MimeMessage mm = (MimeMessage) mimes.get(i);
                    mm.removeHeader("X-DBox");
                    Transport.send(mm);
                }
            }
        } catch (Exception ex) {
            throw new MailException(ex);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new MailException(ex);
        } catch (Throwable ex) {
            throw new MailException(ex);
        } finally {
            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
