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


package com.duroty.service.analyzer;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.utils.mail.MessageUtilities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Header;
import javax.mail.internet.MimeMessage;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class BayesianAnalysisFeeder implements MailetAnalyzer {
    /**
     * The JDBCBayesianAnalyzer class that does all the work.
     */
    private HibernateBayesianAnalyzer analyzer = new HibernateBayesianAnalyzer();

    /**
     * Holds value of property maxSize.
     */
    private int maxSize = 100000;

    /**
     * Scans the mail and updates the token frequencies in the database.
     *
     * The method is synchronized in order to avoid too much database locking,
     * as thousands of rows may be updated just for one message fed.
     *
     * @param mail The Mail message to be scanned.
     * @throws Exception
     */
    public void service(String username, String feedType, MimeMessage message)
        throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            if (message.getSize() > this.maxSize) {
                return;
            }

            StringBuffer buffer = new StringBuffer();

            String from = MessageUtilities.decodeAddresses(message.getFrom());

            if (from.length() > 0) {
                buffer.append(" " + from + " ");
                buffer.append(" " + from + " ");
                buffer.append(" " + from + " ");
                buffer.append(" " + from + " ");
            }

            buffer.append(" " + message.getSubject() + " ");

            clearAllHeaders(message);

            message.writeTo(baos);

            buffer.append(" " + baos.toString());

            BufferedReader br = new BufferedReader(new StringReader(
                        buffer.toString()));

            // this is synchronized to avoid concurrent update of the corpus
            synchronized (HibernateBayesianAnalyzer.DATABASE_LOCK) {
                //Clear out any existing word/counts etc..
                analyzer.clear();

                if ("ham".equalsIgnoreCase(feedType)) {
                    //Process the stream as ham (not spam).
                    analyzer.addHam(br);

                    //Update storage statistics.
                    analyzer.updateHamTokens(username);
                } else {
                    //Process the stream as spam.
                    analyzer.addSpam(br);

                    //Update storage statistics.
                    analyzer.updateSpamTokens(username);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @throws javax.mail.MessagingException DOCUMENT ME!
     */
    private void clearAllHeaders(MimeMessage message)
        throws javax.mail.MessagingException {
        Enumeration headers = message.getAllHeaders();

        while (headers.hasMoreElements()) {
            Header header = (Header) headers.nextElement();

            try {
                message.removeHeader(header.getName());
            } catch (javax.mail.MessagingException me) {
            }
        }

        message.saveChanges();
    }

    /**
     * DOCUMENT ME!
     *
     * @param properties DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws Throwable DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public void init(HashMap properties)
        throws Exception, Throwable, OutOfMemoryError {
        Context ctx = new InitialContext();

        if (properties == null) {
            Map options = ApplicationConstants.options;
            properties = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));
        } else {
        }

        if (properties.get(Constants.MAIL_SPAM_MAX_SIZE) != null) {
            this.maxSize = Integer.parseInt((String) properties.get(
                        Constants.MAIL_SPAM_MAX_SIZE));
        }

        analyzer.init(properties);
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param messageName DOCUMENT ME!
     * @param mime DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws Throwable DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public void service(String username, String messageName, MimeMessage[] mime)
        throws Exception, Throwable, OutOfMemoryError {
        for (int i = 0; i < mime.length; i++) {
            service(username, messageName, mime[i]);
        }
    }
}
