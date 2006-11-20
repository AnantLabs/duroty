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


/**
 *
 */
package com.duroty.service;

import com.duroty.application.mail.manager.SendMessageThread;

import com.duroty.hibernate.Attachment;
import com.duroty.hibernate.BuddyList;
import com.duroty.hibernate.Contact;
import com.duroty.hibernate.Identity;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.mail.LuceneMessage;
import com.duroty.lucene.mail.MimeMessageToLuceneMessage;
import com.duroty.lucene.mail.indexer.MailIndexer;
import com.duroty.lucene.mail.indexer.MailIndexerConstants;

import com.duroty.service.analyzer.BayesianAnalysis;
import com.duroty.service.analyzer.LuceneFiltersAnalysis;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;
import com.duroty.utils.mail.MailPart;
import com.duroty.utils.mail.MessageUtilities;
import com.duroty.utils.mail.RFC2822Headers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.nio.charset.Charset;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * @author durot
 *
 */
public class Mailet implements Runnable, MailIndexerConstants {
    /**
     * DOCUMENT ME!
     */
    private Servible servible;

    /**
    * DOCUMENT ME!
    */
    private Context ctx = null;

    /**
     * DOCUMENT ME!
     */
    private String hibernateSessionFactory;

    /**
     * DOCUMENT ME!
     */
    private String smtpSessionFactory;

    /**
     * DOCUMENT ME!
     */
    private Analyzer analyzer = null;

    /**
     * DOCUMENT ME!
     */
    private MimeMessage mime = null;

    /**
     * DOCUMENT ME!
     */
    private String messageName = null;

    /**
     * DOCUMENT ME!
     */
    private String repositoryName = null;

    /**
     * DOCUMENT ME!
     */
    private int indexLimit = -1;

    /**
     * DOCUMENT ME!
     */
    private String defaultLucenePath = null;

    /**
     * Creates a new Mailet object.
     *
     * @param servible DOCUMENT ME!
     * @param messageName DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param mime DOCUMENT ME!
     */
    public Mailet(Servible servible, String messageName, String repositoryName,
        MimeMessage mime) {
        super();
        this.servible = servible;
        this.messageName = messageName;
        this.repositoryName = repositoryName;
        this.mime = mime;

        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap mail = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));

            this.hibernateSessionFactory = (String) mail.get(Constants.HIBERNATE_SESSION_FACTORY);
            this.smtpSessionFactory = (String) mail.get(Constants.DUROTY_MAIL_FACTOTY);
            this.defaultLucenePath = (String) mail.get(Constants.MAIL_LUCENE_PATH);

            parseIndexLimit((String) mail.get(Constants.MAIL_ATTACHMENT_SIZE));

            String clazzAnalyzerName = (String) mail.get(Constants.MAIL_LUCENE_ANALYZER);

            if ((clazzAnalyzerName != null) &&
                    !clazzAnalyzerName.trim().equals("")) {
                Class clazz = null;
                clazz = Class.forName(clazzAnalyzerName.trim());
                this.analyzer = (Analyzer) clazz.newInstance();
            } else {
                this.analyzer = new StandardAnalyzer();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            flush();
        } catch (Exception e2) {
            System.gc();
        } catch (OutOfMemoryError e2) {
            System.gc();
        } catch (Throwable e2) {
            if (mime != null) {
                sendError(mime, e2);
            }

            System.gc();
        } finally {
            if (servible != null) {
                servible.removePool(messageName + "--" + repositoryName);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    private void flush() throws Exception, Throwable, OutOfMemoryError {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Users user = getUser(hsession, repositoryName);

            this.saveInReplyTo(messageName, mime);

            Message message = new Message();

            message.setMesBox(getBoxName(mime, user));

            mime.removeHeader("X-DBox");
            mime.saveChanges();

            StringBuffer bodyBuffer = new StringBuffer();
            Vector mailParts = new Vector();

            //Apply label/filter analysis to mimemessage
            LuceneMessage luceneMessage = prepareLuceneMessage(messageName,
                    mime, bodyBuffer, mailParts);

            LuceneFiltersAnalysis luceneFiltersAnalysis = new LuceneFiltersAnalysis(luceneMessage,
                    message);
            luceneFiltersAnalysis.init(null);
            luceneFiltersAnalysis.service(repositoryName, messageName, mime);

            message.setMesReferences(getParentId(messageName, mime));
            message.setUsers(user);
            message.setMesName(messageName);

            String from = "unknown from";

            try {
                from = MessageUtilities.decodeAddresses(mime.getFrom());
            } catch (Exception e) {
            }

            message.setMesFrom(from);

            String to = "unknown to";

            try {
                to = MessageUtilities.decodeAddresses(mime,
                        javax.mail.Message.RecipientType.TO);
            } catch (Exception e) {
            }

            message.setMesTo(to);

            String cc = "unknown cc";

            try {
                cc = MessageUtilities.decodeAddresses(mime,
                        javax.mail.Message.RecipientType.CC);
            } catch (Exception e) {
            }

            message.setMesCc(cc);

            String replyTo = "unknown replyTo";

            try {
                replyTo = MessageUtilities.decodeAddresses(mime.getReplyTo());
            } catch (Exception e) {
                replyTo = from;
            }

            message.setMesReplyTo(replyTo);

            try {
                message.setMesSubject(mime.getSubject());
            } catch (Exception e2) {
                message.setMesSubject("unknown subject");
            }

            message.setMesBody(bodyBuffer.toString());

            if (mime.getSentDate() == null) {
                if (mime.getReceivedDate() == null) {
                    message.setMesDate(new Date());
                } else {
                    message.setMesDate(mime.getReceivedDate());
                }
            } else {
                message.setMesDate(mime.getSentDate());
            }

            if ((message.getMesBox() != null) &&
                    message.getMesBox().equals("SENT")) {
                message.setMesRecent(new Boolean(false));
            } else {
                if (isRecent(mime)) {
                    message.setMesRecent(new Boolean(true));
                } else {
                    message.setMesRecent(new Boolean(false));
                }
            }

            int size = mime.getSize();
            Enumeration e = mime.getAllHeaders();

            while (e.hasMoreElements()) {
                size += ((Header) e.nextElement()).toString().length();
            }

            message.setMesSize(new Integer(size));

            try {
                InternetHeaders xheaders = MessageUtilities.getHeadersWithFrom(mime);
                Enumeration xenum = xheaders.getAllHeaderLines();
                StringBuffer buff = new StringBuffer();

                while (xenum.hasMoreElements()) {
                    buff.append(xenum.nextElement());
                    buff.append('\n');
                }

                message.setMesHeaders(buff.toString());
            } catch (Exception e2) {
                message.setMesHeaders("");
            }

            if ((mailParts != null) && (mailParts.size() > 0)) {
                for (int j = 0; j < mailParts.size(); j++) {
                    MailPart part = (MailPart) mailParts.get(j);
                    Attachment attachment = new Attachment();
                    attachment.setAttContentType(part.getContentType());
                    attachment.setAttName(part.getName());
                    attachment.setAttPart(part.getId());
                    attachment.setAttSize(part.getSize());
                    attachment.setMessage(message);

                    message.addAttachment(attachment);
                }
            }

            if (!message.getMesBox().equals("SPAM")) {
                parseContacts(hsession, user, mime, message.getMesBox());
            }

            //Inserto el dmail message i els attachments corresponents
            hsession.save(message);
            hsession.flush();

            indexerLucene(messageName, luceneMessage, mime);

            try {
                Iterator it = user.getMailPreferenceses().iterator();
                MailPreferences mailPreferences = (MailPreferences) it.next();

                if (mailPreferences.isMaprVacationActive()) {
                    sendVacationMessage(repositoryName, from);
                }
            } catch (Exception ex) {
            }
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * Utility method that parses an amount string.
     * You can use 'k' and 'm' as optional postfixes to the amount (both upper and lowercase).
     * In other words, "1m" is the same as writing "1024k", which is the same as
     * "1048576".
     *
     * @param amount the amount string to parse
     */
    private void parseIndexLimit(String limit) {
        try {
            limit = limit.toLowerCase();

            if (limit.endsWith("k")) {
                limit = limit.substring(0, limit.length() - 1);
                this.indexLimit = Integer.parseInt(limit) * 1024;
            } else if (limit.endsWith("m")) {
                limit = limit.substring(0, limit.length() - 1);
                this.indexLimit = Integer.parseInt(limit) * 1024 * 1024;
            } else {
                this.indexLimit = Integer.parseInt(limit);
            }
        } catch (Exception e) {
            this.indexLimit = -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    private void saveInReplyTo(String id, MimeMessage message) {
        try {
            String[] inReplyTos = message.getHeader(RFC2822Headers.IN_REPLY_TO);

            String messageId = message.getMessageID();

            if ((inReplyTos == null) || (inReplyTos.length <= 0)) {
                if (messageId != null) {
                    message.addHeader(RFC2822Headers.IN_REPLY_TO, messageId);
                    message.saveChanges();
                }
            }

            String[] references = message.getHeader(RFC2822Headers.REFERENCES);

            if ((references == null) || (references.length <= 0)) {
                message.addHeader(RFC2822Headers.REFERENCES, messageId);
                message.saveChanges();
            }
        } catch (Exception e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getBoxName(MimeMessage mime, Users user) {
        String _box = null;

        try {
            if (_box == null) {
                String[] aux = mime.getHeader(BayesianAnalysis.messageIsSpam);

                if ((aux != null) && (aux.length > 0)) {
                    if (controlSpam(user, mime)) {
                        _box = "SPAM";
                    }
                } else {
                    aux = mime.getHeader("X-DBox");

                    if ((aux != null) && (aux.length > 0)) {
                        _box = aux[0];
                    }
                }
            }
        } catch (Exception e) {
            _box = "INBOX";
        }

        if (_box == null) {
            _box = "INBOX";
        }

        return _box;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param bodyBuffer DOCUMENT ME!
     * @param attachments DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private LuceneMessage prepareLuceneMessage(String id, MimeMessage message,
        StringBuffer bodyBuffer, Vector attachments) throws Exception {
        MimeMessageToLuceneMessage mmlm = MimeMessageToLuceneMessage.getInstance();

        LuceneMessage lmsg = mmlm.parse(id, message, bodyBuffer, attachments,
                indexLimit);

        return lmsg;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected Users getUser(org.hibernate.Session hsession, String username)
        throws Exception {
        try {
            Criteria criteria = hsession.createCriteria(Users.class);
            criteria.add(Restrictions.eq("useUsername", username));
            criteria.add(Restrictions.eq("useActive", new Boolean(true)));

            return (Users) criteria.uniqueResult();
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getParentId(String id, MimeMessage mime) {
        String references = null;

        try {
            if (references == null) {
                String[] aux = mime.getHeader(RFC2822Headers.REFERENCES);

                //Cal controlar que portem un array
                if ((aux != null) && (aux.length > 0)) {
                    if (aux[0] != null) {
                        String[] ref = aux[0].split("\\s+");

                        if ((ref != null) && (ref.length > 1)) {
                            references = ref[0];
                        }
                    }

                    if (references == null) {
                        references = aux[0];
                    }
                }
            }

            if (references == null) {
                String[] aux = mime.getHeader(RFC2822Headers.IN_REPLY_TO);

                //Cal veure que portem un array
                if ((aux != null) && (aux.length > 0)) {
                    references = aux[0];
                }
            }

            if (references == null) {
                references = mime.getMessageID();

                mime.addHeader(RFC2822Headers.IN_REPLY_TO, references);
                mime.addHeader(RFC2822Headers.REFERENCES, references);
            }
        } catch (MessagingException e) {
            references = null;
        }

        return references;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isRecent(MimeMessage mime) {
        boolean control = true;

        try {
            String[] aux = mime.getHeader("X-DRecent");

            if ((aux != null) && (aux.length > 0)) {
                for (int i = 0; i < aux.length; i++) {
                    control = Boolean.parseBoolean(aux[i].trim());

                    break;
                }
            }
        } catch (Exception e) {
            control = true;
        }

        return control;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hfactory DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param mime DOCUMENT ME!
     */
    private void parseContacts(Session hsession, Users user, MimeMessage mime,
        String box) {
        try {
            Address[] addresses = null;

            Date sentDate = null;
            Date receivedDate = null;

            if (box.equals("SENT")) {
                addresses = mime.getAllRecipients();
                sentDate = mime.getSentDate();

                if (sentDate == null) {
                    sentDate = new Date();
                }
            } else {
                addresses = mime.getFrom();
                receivedDate = mime.getReceivedDate();

                if (receivedDate == null) {
                    receivedDate = new Date();
                }
            }

            if ((addresses != null) && (addresses.length > 0)) {
                String name = null;
                String email = null;

                for (int i = 0; i < addresses.length; i++) {
                    if (addresses[i] instanceof InternetAddress) {
                        InternetAddress xinet = (InternetAddress) addresses[i];

                        name = xinet.getPersonal();

                        if ((name != null) && (name.length() > 49)) {
                            name = name.substring(0, 49);
                        }

                        email = xinet.getAddress();
                    } else {
                        email = addresses[i].toString();
                    }

                    Criteria crit1 = hsession.createCriteria(Contact.class);
                    crit1.add(Restrictions.eq("users", user));
                    crit1.add(Restrictions.eq("conEmail", email));

                    Contact contact = (Contact) crit1.uniqueResult();

                    if (contact != null) {
                        if (receivedDate != null) {
                            contact.setConReceivedDate(receivedDate);
                        }

                        if (sentDate != null) {
                            contact.setConSentDate(sentDate);
                        }

                        int freq = contact.getConCount();

                        contact.setConCount(freq + 1);

                        if (!StringUtils.isBlank(name)) {
                            name = name.replaceAll(",", " ");
                            name = name.replaceAll(";", " ");
                            contact.setConName(name);
                        }

                        hsession.update(contact);
                    } else {
                        contact = new Contact();
                        contact.setConEmail(email);
                        contact.setConName(name);
                        contact.setConReceivedDate(receivedDate);
                        contact.setConSentDate(sentDate);
                        contact.setConCount(new Integer(1));
                        contact.setUsers(user);

                        hsession.save(contact);
                    }

                    hsession.flush();

                    try {
                        if ((contact.getConSentDate() != null) &&
                                (contact.getConReceivedDate() != null)) {
                            Criteria crit2 = hsession.createCriteria(Identity.class);
                            crit2.add(Restrictions.eq("ideEmail", email));
                            crit2.add(Restrictions.eq("ideActive",
                                    new Boolean(true)));

                            List list = crit2.list();

                            if (list != null) {
                                Iterator scroll = list.iterator();

                                while (scroll.hasNext()) {
                                    Identity identity = (Identity) scroll.next();
                                    Users buddy = identity.getUsers();

                                    if ((buddy != null) &&
                                            (user.getUseIdint() != buddy.getUseIdint())) {
                                        Criteria auxCrit = hsession.createCriteria(BuddyList.class);
                                        auxCrit.add(Restrictions.eq(
                                                "usersByBuliOwnerIdint", user));
                                        auxCrit.add(Restrictions.eq(
                                                "usersByBuliBuddyIdint", buddy));

                                        BuddyList buddyList = (BuddyList) auxCrit.uniqueResult();

                                        if (buddyList != null) {
                                            buddyList.setBuliActive(true);
                                            buddyList.setBuliLastDate(new Date());
                                            hsession.update(buddyList);
                                        } else {
                                            buddyList = new BuddyList();
                                            buddyList.setBuliActive(true);
                                            buddyList.setBuliLastDate(new Date());
                                            buddyList.setUsersByBuliBuddyIdint(buddy);
                                            buddyList.setUsersByBuliOwnerIdint(user);
                                            hsession.save(buddyList);
                                        }
                                    }
                                }
                            }

                            hsession.flush();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     * @param luceneMessage DOCUMENT ME!
     * @param message DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void indexerLucene(String id, LuceneMessage luceneMessage,
        MimeMessage message) throws Exception {
        MailIndexer indexer = new MailIndexer();

        String userLucenePathMessages = null;

        if (!defaultLucenePath.endsWith(File.separator)) {
            userLucenePathMessages = defaultLucenePath + File.separator +
                repositoryName + File.separator + MESSAGES;
        } else {
            userLucenePathMessages = defaultLucenePath + repositoryName +
                File.separator + MESSAGES;
        }

        indexer.insertDocument(userLucenePathMessages, id,
            luceneMessage.getDocPrincipal(), analyzer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    private void sendError(MimeMessage message, Throwable exception) {
        if (true) {
            return;
        }

        StringWriter sw = null;
        PrintWriter writer = null;
        javax.mail.Session msession = null;

        try {
            msession = (javax.mail.Session) ctx.lookup(smtpSessionFactory);

            InternetAddress sender = new InternetAddress("postmaster@duroty.com",
                    "Postmaster");
            InternetAddress errorTo = new InternetAddress(message.getFrom()[0].toString());

            sw = new StringWriter();
            writer = new PrintWriter(sw);

            if (exception != null) {
                exception.printStackTrace(writer);
            }

            //Create the message to forward
            MimeMessage forward = MessageUtilities.createNewMessage(sender,
                    new Address[] { errorTo }, "[ERROR: ", " ]", sw.toString(),
                    message, msession);

            //MimeMessage newMessage = MessageUtilities.createForward(sender, forwardTo, message, this.mailSession);
            Thread thread = new Thread(new SendMessageThread(forward));
            thread.start();

            System.gc();
        } catch (Exception e) {
            DLog.log(DLog.ERROR, this.getClass(),
                "Impossible sent error: " + e.getMessage());
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            DLog.log(DLog.ERROR, this.getClass(),
                "Impossible sent error: " + ex.getMessage());
        } catch (Throwable e) {
            DLog.log(DLog.ERROR, this.getClass(),
                "Impossible sent error: " + e.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param to DOCUMENT ME!
     */
    private void sendVacationMessage(String username, String to) {
        SessionFactory hfactory = null;
        Session hsession = null;
        javax.mail.Session msession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();
            msession = (javax.mail.Session) ctx.lookup(smtpSessionFactory);

            Users user = getUser(hsession, username);

            Criteria crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.eq("users", getUser(hsession, username)));
            crit.add(Restrictions.eq("ideActive", new Boolean(true)));
            crit.add(Restrictions.eq("ideDefault", new Boolean(true)));

            Identity identity = (Identity) crit.uniqueResult();

            if (identity != null) {
                InternetAddress _from = new InternetAddress(identity.getIdeEmail(),
                        identity.getIdeName());
                InternetAddress _to = InternetAddress.parse(to)[0];

                if (_from.getAddress().equals(_to.getAddress())) {
                    return;
                }

                HtmlEmail email = new HtmlEmail();
                email.setMailSession(msession);

                email.setFrom(_from.getAddress(), _from.getPersonal());
                email.addTo(_to.getAddress(), _to.getPersonal());

                Iterator it = user.getMailPreferenceses().iterator();
                MailPreferences mailPreferences = (MailPreferences) it.next();

                email.setSubject(mailPreferences.getMaprVacationSubject());
                email.setHtmlMsg("<p>" + mailPreferences.getMaprVacationBody() +
                    "</p><p>" + mailPreferences.getMaprSignature() + "</p>");

                email.setCharset(Charset.defaultCharset().displayName());

                email.send();
            }
        } catch (Exception e) {
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean controlSpam(Users user, MimeMessage mime) {
        try {
            Iterator it = user.getMailPreferenceses().iterator();

            MailPreferences mailPreferences = (MailPreferences) it.next();

            if (mailPreferences.getMaprSpamTolerance() == -1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
