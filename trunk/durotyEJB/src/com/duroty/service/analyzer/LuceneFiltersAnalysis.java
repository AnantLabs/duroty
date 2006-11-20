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
package com.duroty.service.analyzer;

import com.duroty.application.mail.manager.SendMessageThread;

import com.duroty.hibernate.Filter;
import com.duroty.hibernate.LabMes;
import com.duroty.hibernate.LabMesId;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.mail.LuceneMessage;
import com.duroty.lucene.mail.search.FilterQueryParser;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;
import com.duroty.utils.mail.MessageUtilities;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * @author durot
 *
 */
public class LuceneFiltersAnalysis implements MailetAnalyzer {
    /**
     * DOCUMENT ME!
     */
    private Context ctx = null;

    /**
     * DOCUMENT ME!
     */
    private Message message = null;

    /**
     * DOCUMENT ME!
     */
    private LuceneMessage luceneMessage;

    /**
     * DOCUMENT ME!
     */
    private String smtpSessionFactory;

    /**
    * DOCUMENT ME!
    */
    private SessionFactory hfactory = null;

    /**
     * DOCUMENT ME!
     */
    private Analyzer analyzer = null;

    /**
     *
     */
    public LuceneFiltersAnalysis(LuceneMessage luceneMessage, Message message) {
        super();
        this.luceneMessage = luceneMessage;
        this.message = message;
    }

    /* (non-Javadoc)
     * @see com.duroty.service.analyzer.MailetAnalyzer#init(java.util.HashMap)
     */
    public void init(HashMap properties)
        throws Exception, Throwable, OutOfMemoryError {
        ctx = new InitialContext();

        if (properties == null) {
            Map options = ApplicationConstants.options;
            properties = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));
        } else {
        }

        String hibernateSessionFactory = (String) properties.get(Constants.HIBERNATE_SESSION_FACTORY);
        this.ctx = new InitialContext();
        this.hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
        this.smtpSessionFactory = (String) properties.get(Constants.DUROTY_MAIL_FACTOTY);

        String clazzAnalyzerName = (String) properties.get(Constants.MAIL_LUCENE_ANALYZER);

        if ((clazzAnalyzerName != null) &&
                !clazzAnalyzerName.trim().equals("")) {
            Class clazz = null;
            clazz = Class.forName(clazzAnalyzerName.trim());
            this.analyzer = (Analyzer) clazz.newInstance();
        } else {
            this.analyzer = new StandardAnalyzer();
        }
    }

    /* (non-Javadoc)
     * @see com.duroty.service.analyzer.MailetAnalyzer#service(java.lang.String, javax.mail.internet.MimeMessage)
     */
    public void service(String repositoryName, String messageName,
        MimeMessage mime) throws Exception, Throwable, OutOfMemoryError {
        Session hsession = null;
        RAMDirectory auxDir = null;

        try {
            hsession = hfactory.openSession();

            auxDir = new RAMDirectory();

            IndexWriter auxWriter = new IndexWriter(auxDir, analyzer, true);
            auxWriter.addDocument(luceneMessage.getDocPrincipal());
            auxWriter.optimize();
            auxWriter.close();

            Vector filters = getFilters(hsession, repositoryName);

            boolean setbox = true;

            String box = message.getMesBox();

            if (box.equals("SPAM")) {
                setbox = false;
            } else if (box.equals("DRAFT")) {
                setbox = false;
            }

            if (filters != null) {
                while (filters.size() > 0) {
                    Filter filter = (Filter) filters.remove(0);
                    IndexSearcher auxSearcher = new IndexSearcher(auxDir);

                    org.apache.lucene.search.Query query = FilterQueryParser.parse(filter,
                            analyzer);

                    Hits hits = auxSearcher.search(query);

                    if (hits.length() > 0) {
                        //he tingut una coincidencia de filtre per tant cal dur a terme les accions assocides
                        //al filtre
                        if (filter.isFilArchive() && setbox) {
                            //Marco un header per a que s'inserti a la carpeta d'archived
                            message.setMesBox("HIDDEN");
                        } else if (filter.isFilTrash() && setbox) {
                            message.setMesBox("TRASH");
                        } else {
                        }

                        if (filter.isFilImportant()) {
                            message.setMesFlagged(new Boolean(true));
                        }

                        if (filter.getLabel() != null) {
                            LabMes labMes = new LabMes(new LabMesId(message,
                                        filter.getLabel()));
                            message.addLabMeses(labMes);
                        }

                        if ((filter.getFilForwardTo() != null) &&
                                !filter.getFilForwardTo().trim().equals("")) {
                            InternetAddress forwardTo = null;

                            try {
                                forwardTo = new InternetAddress(filter.getFilForwardTo());
                            } catch (Exception e) {
                                forwardTo = null;
                            }

                            if (forwardTo != null) {
                                try {
                                    InternetAddress recipient = (InternetAddress) mime.getFrom()[0];
                                    forwardMailFromLabel(recipient, forwardTo,
                                        "FW: ", mime);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            DLog.log(DLog.DEBUG, this.getClass(), ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);

            if (auxDir != null) {
                auxDir.close();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Vector getFilters(Session hsession, String username) {
        try {
            Vector filters = new Vector();

            Criteria crit = hsession.createCriteria(Users.class);
            crit.add(Restrictions.eq("useUsername", username));
            crit.add(Restrictions.eq("useActive", new Boolean(true)));

            Users user = (Users) crit.uniqueResult();

            Query query = hsession.getNamedQuery("filter-by-user");
            query.setInteger("user", user.getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                filters.addElement(scroll.get(0));
            }

            return filters;
        } catch (Exception ex) {
            DLog.log(DLog.ERROR, this.getClass(), ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sender DOCUMENT ME!
     * @param forwardTo DOCUMENT ME!
     * @param subject DOCUMENT ME!
     * @param message DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void forwardMailFromLabel(InternetAddress sender,
        InternetAddress forwardTo, String subject, MimeMessage message)
        throws Exception {
        //Si el missatge ve d'un forward de label, no es pot aplicar un altre forward de label
        if (isForwardFromLabel(message)) {
            return;
        }

        javax.mail.Session session = (javax.mail.Session) ctx.lookup(smtpSessionFactory);

        MimeMessage forward = MessageUtilities.createNewMessage(sender,
                new Address[] { forwardTo }, "[Fwd: ", " ]",
                "Forward from DUROTY 1.0 LABEL", message, session);

        //Afegeixo un header per a indicar que ja he fet un forward
        forward.addHeader("X-DForwardFromLabel", "true");
        forward.saveChanges();

        Thread thread = new Thread(new SendMessageThread(forward));
        thread.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isForwardFromLabel(MimeMessage mime) {
        boolean control = false;

        try {
            if (!control) {
                String[] aux = mime.getHeader("X-DForwardFromLabel");

                if ((aux != null) && (aux.length > 0)) {
                    for (int i = 0; i < aux.length; i++) {
                        control = Boolean.parseBoolean(aux[i].trim());

                        break;
                    }
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
     * @param repositoryName DOCUMENT ME!
     * @param messageName DOCUMENT ME!
     * @param mime DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws Throwable DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public void service(String repositoryName, String messageName,
        MimeMessage[] mime) throws Exception, Throwable, OutOfMemoryError {
        for (int i = 0; i < mime.length; i++) {
            service(repositoryName, messageName, mime[i]);
        }
    }
}
