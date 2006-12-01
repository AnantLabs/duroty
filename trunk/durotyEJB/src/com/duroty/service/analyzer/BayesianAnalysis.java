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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.utils.mail.RFC2822Headers;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class BayesianAnalysis implements MailetAnalyzer {
    /**
     * DOCUMENT ME!
     */
    public static final String messageIsSpamProbability = "X-MessageIsSpamProbability";

    /**
     * DOCUMENT ME!
     */
    public static final String messageIsSpam = "X-MessageIsSpam";

    /**
     * The JDBCBayesianAnalyzer class that does all the work.
     */
    private HibernateBayesianAnalyzer analyzer = new HibernateBayesianAnalyzer();

    /**
     * Holds value of property maxSize.
     */
    private int maxSize = 100000;

    /**
     * Mailet initialization routine.
     * @throws MessagingException if a problem arises
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
     * Scans the mail and determines the spam probability.
     *
     * @param mail The Mail message to be scanned.
     * @throws MessagingException if a problem arises
     */
    public void service(String username, String messageName, MimeMessage message)
        throws Exception {
        try {
            loadData(username);

            String[] headerArray = message.getHeader(messageIsSpamProbability);

            // ignore the message if already analyzed
            if ((headerArray != null) && (headerArray.length > 0)) {
            	message.removeHeader(messageIsSpamProbability);
            	saveChanges(message);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            double probability;

            if (message.getSize() < this.maxSize) {
                message.writeTo(baos);

                String contents = baos.toString();
                probability = analyzer.computeSpamProbability(new BufferedReader(
                            new StringReader(contents)));
            } else {
                probability = 0.0;
            }

            message.setHeader(messageIsSpamProbability, Double.toString(probability));

            /*DecimalFormat probabilityForm = (DecimalFormat) DecimalFormat.getInstance();
            probabilityForm.applyPattern("##0.##%");*/

            //String probabilityString = probabilityForm.format(probability);

            /*if (probability > 0.75) {
                appendToSubject(message,
                    " [" + probabilityString +
                    ((probability > 0.9) ? " SPAM" : " spam") + "]");
            }*/
            if (probability > 0.9) {
            	message.removeHeader(messageIsSpam);
            	saveChanges(message);
            	
                message.setHeader(messageIsSpam, "true");
            }

            saveChanges(message);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param conn DOCUMENT ME!
     * @throws Exception
     */
    private void loadData(String username) throws Exception {
        try {
            synchronized (HibernateBayesianAnalyzer.DATABASE_LOCK) {
                analyzer.tokenCountsClear();
                analyzer.loadHamNSpam(username);
                analyzer.buildCorpus();
                analyzer.tokenCountsClear();
            }
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param toAppend DOCUMENT ME!
     */
    private void appendToSubject(MimeMessage message, String toAppend) {
        try {
            String subject = message.getSubject();

            if (subject == null) {
                message.setSubject(toAppend, "iso-8859-1");
            } else {
                message.setSubject(toAppend + " " + subject, "iso-8859-1");
            }
        } catch (MessagingException ex) {
        }
    }

    /**
     * Saves changes resetting the original message id.
     */
    private void saveChanges(MimeMessage message) throws MessagingException {
        String messageId = message.getMessageID();
        message.saveChanges();

        if (messageId != null) {
            message.setHeader(RFC2822Headers.MESSAGE_ID, messageId);
        }
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
