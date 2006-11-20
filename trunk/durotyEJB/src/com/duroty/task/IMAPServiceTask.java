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
package com.duroty.task;

import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.mail.indexer.MailIndexerConstants;
import com.duroty.lucene.utils.FileUtilities;

import com.duroty.service.Mailet;
import com.duroty.service.Servible;
import com.duroty.service.analyzer.BayesianAnalysis;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;
import com.duroty.utils.mail.MailUtilities;

import com.sun.mail.imap.IMAPFolder;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Restrictions;

import org.jboss.varia.scheduler.Schedulable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * @author durot
 *
 */
public class IMAPServiceTask implements Schedulable, Servible,
    MailIndexerConstants {
    /**
        * DOCUMENT ME!
        */
    private static final List pool = new ArrayList(10);

    /**
     * DOCUMENT ME!
     */
    private Context ctx = null;

    /**
     * DOCUMENT ME!
     */
    private boolean init = false;

    /**
     * DOCUMENT ME!
     */
    private int poolSize = 10;

    /**
     * DOCUMENT ME!
     */
    private String durotyMailFactory;

    /**
     * DOCUMENT ME!
     */
    private String hibernateSessionFactory;

    /**
     * DOCUMENT ME!
     */
    private String imapInbox;

    /**
     * DOCUMENT ME!
     */
    private String imapSent;

    /**
     * DOCUMENT ME!
     */
    private String imapDraft;

    /**
     * @throws NamingException
     * @throws IOException
     *
     */
    public IMAPServiceTask(int poolSize) throws NamingException, IOException {
        super();
        this.poolSize = poolSize;

        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap mail = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));

            this.durotyMailFactory = (String) mail.get(Constants.DUROTY_MAIL_FACTOTY);
            this.hibernateSessionFactory = (String) mail.get(Constants.HIBERNATE_SESSION_FACTORY);

            this.imapInbox = (String) mail.get(Constants.MAIL_SERVER_INBOX);
            this.imapSent = (String) mail.get(Constants.MAIL_SERVER_SENT);
            this.imapDraft = (String) mail.get(Constants.MAIL_SERVER_DRAFT);

            String tempDir = System.getProperty("java.io.tmpdir");

            if (!tempDir.endsWith(File.separator)) {
                tempDir = tempDir + File.separator;
            }

            FileUtilities.deleteMotLocks(new File(tempDir));
            FileUtilities.deleteLuceneLocks(new File(tempDir));
        } finally {
        }
    }

    /* (non-Javadoc)
     * @see org.jboss.varia.scheduler.Schedulable#perform(java.util.Date, long)
     */
    public void perform(Date arg0, long arg1) {
        if (isInit()) {
            DLog.log(DLog.DEBUG, this.getClass(),
                "IMAPServiceTask is running and wait.");

            return;
        }

        flush();
    }

    /**
     * DOCUMENT ME!
     */
    private void flush() {
        setInit(true);

        SessionFactory hfactory = null;
        Session hsession = null;
        javax.mail.Session msession = null;
        Store store = null;
        Folder rootFolder = null;
        Folder folder = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();
            msession = (javax.mail.Session) ctx.lookup(durotyMailFactory);
            store = msession.getStore("imap");

            String imapHost = msession.getProperty("mail.imap.host");

            Query query = hsession.getNamedQuery("users-mail");
            query.setBoolean("active", true);
            query.setString("role", "mail");

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Users user = (Users) scroll.get(0);

                String repositoryName = user.getUseUsername();

                store.connect(imapHost, repositoryName, user.getUsePassword());

                rootFolder = store.getDefaultFolder();

                if ((rootFolder.getType() & Folder.HOLDS_FOLDERS) != 0) {
                    folder = store.getFolder(this.imapInbox);

                    Message[] messages = getMimeMessages(folder,
                            new Flags(Flags.Flag.SEEN), false);
                    MailUtilities.setFolderOpenAndReady(folder,
                        Folder.READ_WRITE);

                    if ((messages != null) && (messages.length > 0)) {
                        for (int i = 0; i < messages.length; i++) {
                            if (pool.size() >= poolSize) {
                                DLog.log(DLog.WARN, this.getClass(),
                                    "PoolSize " + pool.size());

                                break;
                            }

                            MimeMessage mime = null;

                            if (messages[i] instanceof MimeMessage) {
                                mime = new MimeMessage((MimeMessage) messages[i]);

                                String messageName = null;

                                if (folder instanceof IMAPFolder) {
                                    messageName = String.valueOf(((IMAPFolder) folder).getUID(
                                                messages[i]));
                                } else {
                                    messageName = String.valueOf(messages[i].getMessageNumber());
                                }

                                messageName += this.imapInbox;

                                boolean existMessage = existMessageName(hfactory.openSession(),
                                        user, messageName);

                                if (existMessage) {
                                    messages[i].setFlag(Flags.Flag.SEEN, true);
                                } else {
                                    String key = messageName + "--" +
                                        repositoryName;

                                    if (!poolContains(key)) {
                                        addPool(key);

                                        if (!isSpam(user, mime)) {
                                            Mailet mailet = new Mailet(this,
                                                    messageName,
                                                    repositoryName, mime);

                                            Thread thread = new Thread(mailet,
                                                    key);
                                            thread.start();

                                            //cal controlar si el borrem
                                            messages[i].setFlag(Flags.Flag.SEEN,
                                                true);
                                        } else {
                                            messages[i].setFlag(Flags.Flag.SEEN,
                                                true);
                                            messages[i].setFlag(Flags.Flag.DELETED,
                                                true);
                                        }
                                    }
                                }

                                Thread.sleep(100);
                            }
                        }
                    }

                    MailUtilities.setFolderClose(folder, true);
                    folder = null;
                    messages = null;

                    folder = store.getFolder(this.imapSent);
                    messages = getMimeMessages(folder,
                            new Flags(Flags.Flag.SEEN), false);
                    MailUtilities.setFolderOpenAndReady(folder,
                        Folder.READ_WRITE);

                    if (!folder.exists()) {
                        folder.create(Folder.HOLDS_MESSAGES);
                    }

                    if (!folder.isSubscribed()) {
                        folder.setSubscribed(true);
                    }

                    if ((messages != null) && (messages.length > 0)) {
                        for (int i = 0; i < messages.length; i++) {
                            if (pool.size() >= poolSize) {
                                DLog.log(DLog.WARN, this.getClass(),
                                    "PoolSize " + pool.size());

                                break;
                            }

                            MimeMessage mime = null;

                            if (messages[i] instanceof MimeMessage) {
                                mime = new MimeMessage((MimeMessage) messages[i]);

                                mime.addHeader("X-DBox", "SENT");

                                String messageName = null;

                                if (folder instanceof IMAPFolder) {
                                    messageName = String.valueOf(((IMAPFolder) folder).getUID(
                                                messages[i]));
                                } else {
                                    messageName = String.valueOf(messages[i].getMessageNumber());
                                }

                                messageName += this.imapSent;

                                boolean existMessage = existMessageName(hfactory.openSession(),
                                        user, messageName);

                                if (existMessage) {
                                    messages[i].setFlag(Flags.Flag.SEEN, true);
                                } else {
                                    String key = messageName + "--" +
                                        repositoryName;

                                    if (!poolContains(key)) {
                                        addPool(key);

                                        if (!isSpam(user, mime)) {
                                            Mailet mailet = new Mailet(this,
                                                    messageName,
                                                    repositoryName, mime);

                                            Thread thread = new Thread(mailet,
                                                    key);
                                            thread.start();

                                            //cal controlar si el borrem
                                            messages[i].setFlag(Flags.Flag.SEEN,
                                                true);
                                        } else {
                                            messages[i].setFlag(Flags.Flag.SEEN,
                                                true);
                                            messages[i].setFlag(Flags.Flag.DELETED,
                                                true);
                                        }
                                    }
                                }

                                Thread.sleep(100);
                            }
                        }
                    }

                    MailUtilities.setFolderClose(folder, true);
                    folder = null;
                    messages = null;

                    folder = store.getFolder(this.imapDraft);
                    messages = getMimeMessages(folder,
                            new Flags(Flags.Flag.SEEN), false);
                    MailUtilities.setFolderOpenAndReady(folder,
                        Folder.READ_WRITE);

                    if (!folder.exists()) {
                        folder.create(Folder.HOLDS_MESSAGES);
                    }

                    if (!folder.isSubscribed()) {
                        folder.setSubscribed(true);
                    }

                    if ((messages != null) && (messages.length > 0)) {
                        for (int i = 0; i < messages.length; i++) {
                            if (pool.size() >= poolSize) {
                                DLog.log(DLog.WARN, this.getClass(),
                                    "PoolSize " + pool.size());

                                break;
                            }

                            MimeMessage mime = null;

                            if (messages[i] instanceof MimeMessage) {
                                mime = new MimeMessage((MimeMessage) messages[i]);

                                mime.addHeader("X-DBox", "DRAFT");

                                String messageName = null;

                                if (folder instanceof IMAPFolder) {
                                    messageName = String.valueOf(((IMAPFolder) folder).getUID(
                                                messages[i]));
                                } else {
                                    messageName = String.valueOf(messages[i].getMessageNumber());
                                }

                                messageName += this.imapDraft;

                                boolean existMessage = existMessageName(hfactory.openSession(),
                                        user, messageName);

                                if (existMessage) {
                                    messages[i].setFlag(Flags.Flag.SEEN, true);
                                } else {
                                    String key = messageName + "--" +
                                        repositoryName;

                                    if (!poolContains(key)) {
                                        addPool(key);

                                        if (!isSpam(user, mime)) {
                                            Mailet mailet = new Mailet(this,
                                                    messageName,
                                                    repositoryName, mime);

                                            Thread thread = new Thread(mailet,
                                                    key);
                                            thread.start();

                                            //cal controlar si el borrem
                                            messages[i].setFlag(Flags.Flag.SEEN,
                                                true);
                                        } else {
                                            messages[i].setFlag(Flags.Flag.SEEN,
                                                true);
                                            messages[i].setFlag(Flags.Flag.DELETED,
                                                true);
                                        }
                                    }
                                }

                                Thread.sleep(100);
                            }
                        }
                    }

                    MailUtilities.setFolderClose(folder, true);
                    folder = null;
                }

                MailUtilities.setFolderClose(rootFolder, true);
                MailUtilities.setStoreClose(store);
            }
        } catch (Exception e) {
            System.gc();
            pool.clear();

            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } catch (OutOfMemoryError e) {
            System.gc();
            pool.clear();

            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } catch (Throwable e) {
            System.gc();
            pool.clear();

            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } finally {
            System.gc();

            GeneralOperations.closeMailFolder(folder, false);
            GeneralOperations.closeMailFolder(rootFolder, false);
            GeneralOperations.closeMailStore(store);
            GeneralOperations.closeHibernateSession(hsession);

            setInit(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void addPool(String key) {
        pool.add(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void removePool(String key) {
        pool.remove(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean poolContains(String key) {
        return pool.contains(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isInit() {
        synchronized (IMAPServiceTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (IMAPServiceTask.class) {
            this.init = init;
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
    private boolean isSpam(Users user, MimeMessage mime) {
        boolean control = false;
        double probability = 0.0;

        try {
            BayesianAnalysis bayesianAnalysis = new BayesianAnalysis();
            bayesianAnalysis.init(null);
            bayesianAnalysis.service(user.getUseUsername(), null, mime);

            String[] aux = mime.getHeader(BayesianAnalysis.messageIsSpamProbability);

            if ((aux != null) && (aux.length > 0)) {
                for (int i = 0; i < aux.length; i++) {
                    probability = Double.parseDouble(aux[i].trim());

                    break;
                }
            }

            Iterator it = user.getMailPreferenceses().iterator();

            MailPreferences mailPreferences = (MailPreferences) it.next();

            if (mailPreferences.getMaprSpamTolerance() == -1) {
                return false;
            }

            double tolerance = ((double) mailPreferences.getMaprSpamTolerance()) / 100;

            if ((probability > 0.0) && (tolerance < 1.0) &&
                    (probability >= tolerance)) {
                control = true;
            }

            return control;
        } catch (NamingException e) {
            return false;
        } catch (Exception ex) {
            return false;
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();

            return false;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param folder DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Message[] getMimeMessages(Folder folder, Flags flags, boolean aux) {
        try {
            MailUtilities.setFolderOpenAndReady(folder, Folder.READ_ONLY);

            SearchTerm search = new FlagTerm(flags, aux);

            return folder.search(search);
        } catch (Exception ex) {
            return null;
        } finally {
            GeneralOperations.closeMailFolder(folder, false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param messageName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean existMessageName(Session hsession, Users user,
        String messageName) {
        try {
            Criteria crit = hsession.createCriteria(com.duroty.hibernate.Message.class);
            crit.add(Restrictions.eq("users", user));
            crit.add(Restrictions.eq("mesName", messageName));

            com.duroty.hibernate.Message message = (com.duroty.hibernate.Message) crit.uniqueResult();

            if (message != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }
}
