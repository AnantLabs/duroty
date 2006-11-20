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
import com.duroty.application.mail.utils.ContactListObj;
import com.duroty.application.mail.utils.ContactObj;
import com.duroty.application.mail.utils.FilterObj;
import com.duroty.application.mail.utils.IdentityObj;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.PreferencesObj;
import com.duroty.application.mail.utils.SearchContactsObj;
import com.duroty.application.mail.utils.SearchGroupsObj;

import com.duroty.hibernate.BuddyList;
import com.duroty.hibernate.ConColi;
import com.duroty.hibernate.ConColiId;
import com.duroty.hibernate.Contact;
import com.duroty.hibernate.ContactList;
import com.duroty.hibernate.Filter;
import com.duroty.hibernate.Identity;
import com.duroty.hibernate.LabMes;
import com.duroty.hibernate.LabMesId;
import com.duroty.hibernate.Label;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.mail.LuceneMessageConstants;
import com.duroty.lucene.mail.indexer.MailIndexer;
import com.duroty.lucene.mail.search.FilterQueryParser;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.mail.MessageUtilities;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.File;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.mail.internet.InternetAddress;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class PreferencesManager implements LuceneMessageConstants {
    /**
    * DOCUMENT ME!
    */
    private static final int ORDER_BY_EMAIL = 2;

    /**
     * DOCUMENT ME!
     */
    private static final int ORDER_BY_FREQUENCY_SENT = 0;

    /**
     * DOCUMENT ME!
     */
    private static final int ORDER_BY_FREQUENCY_RECEIVED = 1;

    /**
     * DOCUMENT ME!
     */
    private Analyzer analyzer;

    /**
     * DOCUMENT ME!
     */
    private String defaultLucenePath;

    /**
     * Creates a new PreferencesManager object.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public PreferencesManager(HashMap mail)
        throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException {
        super();

        String luceneAnalyzer = (String) mail.get(Constants.MAIL_LUCENE_ANALYZER);

        if ((luceneAnalyzer != null) && !luceneAnalyzer.trim().equals("")) {
            Class clazz = null;
            clazz = Class.forName(luceneAnalyzer.trim());
            this.analyzer = (Analyzer) clazz.newInstance();
        }

        this.defaultLucenePath = (String) mail.get(Constants.MAIL_LUCENE_PATH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public PreferencesObj getPreferences(Session hsession, String username)
        throws MailException {
        try {
            Users user = getUser(hsession, username);

            Criteria crit = hsession.createCriteria(MailPreferences.class);
            crit.add(Restrictions.eq("users", user));

            MailPreferences preferences = (MailPreferences) crit.uniqueResult();

            PreferencesObj obj = new PreferencesObj();

            obj.setName(user.getUseName());
            obj.setContactEmail(user.getUseEmail());
            obj.setHtmlMessage(preferences.isMaprHtmlMessage());
            obj.setMessagesByPage(preferences.getMaprMessagesByPage());
            obj.setVacationActive(preferences.isMaprVacationActive());
            obj.setVacationBody(preferences.getMaprVacationBody());
            obj.setVacationSubject(preferences.getMaprVacationSubject());

            //obj.setQuotaSize(preferences.getMaprQuotaSize());
            obj.setSignature(preferences.getMaprSignature());
            obj.setLanguage(user.getUseLanguage());

            obj.setSpamTolerance(preferences.getMaprSpamTolerance());

            return obj;
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param preferencesObj DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void setPreferences(Session hsession, String username,
        PreferencesObj preferencesObj) throws MailException {
        try {
            Users user = getUser(hsession, username);

            user.setUseEmail(preferencesObj.getContactEmail());
            user.setUseName(preferencesObj.getName());
            user.setUseLanguage(preferencesObj.getLanguage());

            hsession.update(user);
            hsession.flush();

            Criteria crit = hsession.createCriteria(MailPreferences.class);
            crit.add(Restrictions.eq("users", user));

            MailPreferences preferences = (MailPreferences) crit.uniqueResult();

            preferences.setMaprHtmlMessage(preferencesObj.isHtmlMessage());
            preferences.setMaprMessagesByPage(preferencesObj.getMessagesByPage());

            if (preferencesObj.getSignature() != null) {
                preferences.setMaprSignature(preferencesObj.getSignature()
                                                           .replaceAll("'",
                        "\\\\'"));
            }

            preferences.setMaprSpamTolerance(preferencesObj.getSpamTolerance());
            preferences.setMaprVacationActive(preferencesObj.isVacationActive());
            preferences.setMaprVacationBody(preferencesObj.getVacationBody());
            preferences.setMaprVacationSubject(preferencesObj.getVacationSubject());
            preferences.setUsers(user);

            hsession.update(preferences);
            hsession.flush();
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public Vector getIdentities(Session hsession, String username)
        throws MailException {
        Vector identities = new Vector();

        try {
            Criteria crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.eq("users", getUser(hsession, username)));
            crit.add(Restrictions.eq("ideActive", new Boolean(true)));
            crit.addOrder(Order.asc("ideEmail"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Identity identity = (Identity) scroll.get(0);
                IdentityObj obj = new IdentityObj();
                obj.setIdint(identity.getIdeIdint());
                obj.setEmail(identity.getIdeEmail());
                obj.setImportant(identity.isIdeDefault());
                obj.setName(identity.getIdeName());
                obj.setReplyTo(identity.getIdeReplyTo());

                identities.addElement(obj);
            }

            return identities;
        } catch (Exception ex) {
            return null;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
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
    protected Users getUser(Session hsession, String username)
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
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public SearchContactsObj searchContacts(Session hsession,
        String repositoryName, String token, int page, int byPage, int order,
        String extra) throws Exception {
        SearchContactsObj sobj = new SearchContactsObj();
        Vector contacts = new Vector();

        try {
            Criteria crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.or(Restrictions.ilike("conName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("conEmail", token, MatchMode.ANYWHERE)));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.add(Restrictions.isNotNull("conSentDate"));
            crit.add(Restrictions.isNotNull("conReceivedDate"));

            int hits = crit.list().size();

            sobj.setHits(hits);

            crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.or(Restrictions.ilike("conName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("conEmail", token, MatchMode.ANYWHERE)));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.add(Restrictions.isNotNull("conSentDate"));
            crit.add(Restrictions.isNotNull("conReceivedDate"));

            switch (order) {
            case ORDER_BY_EMAIL:
                crit.addOrder(Order.asc("conEmail"));

                break;

            case ORDER_BY_FREQUENCY_SENT:
                crit.addOrder(Order.asc("conSentDate"));

                break;

            case ORDER_BY_FREQUENCY_RECEIVED:
                crit.addOrder(Order.asc("conReceivedDate"));

                break;

            default:
                crit.addOrder(Order.asc("conSentDate"));

                break;
            }

            if (byPage > 0) {
                crit.setFirstResult(page * byPage);
                crit.setMaxResults(byPage);
            }

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Contact contact = (Contact) scroll.get(0);

                ContactObj obj = new ContactObj();
                obj.setIdint(contact.getConIdint());
                obj.setName(contact.getConName());
                obj.setEmail(contact.getConEmail());

                if (!StringUtils.isBlank(contact.getConDescription())) {
                    obj.setDescription(contact.getConDescription()
                                              .replaceAll("\n", "\\\\n")
                                              .replaceAll("\r", "\\\\r")
                                              .replaceAll("'", "''"));
                }

                contacts.addElement(obj);
            }

            sobj.setContacts(contacts);

            return sobj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public SearchGroupsObj searchGroups(Session hsession,
        String repositoryName, String token, int page, int byPage, int order,
        String extra) throws Exception {
        SearchGroupsObj sobj = new SearchGroupsObj();
        Vector groups = new Vector();

        try {
            Criteria crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.or(Restrictions.ilike("coliName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("coliName", token, MatchMode.ANYWHERE)));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            int hits = crit.list().size();

            sobj.setHits(hits);

            crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.or(Restrictions.ilike("coliName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("coliName", token, MatchMode.ANYWHERE)));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.addOrder(Order.asc("coliName"));

            if (byPage > 0) {
                crit.setFirstResult(page * byPage);
                crit.setMaxResults(byPage);
            }

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                ContactList contactList = (ContactList) scroll.get(0);

                ContactListObj obj = new ContactListObj();
                obj.setIdint(contactList.getColiIdint());
                obj.setName(contactList.getColiName());

                Set set = contactList.getConColis();

                StringBuffer emails = new StringBuffer();

                if (set != null) {
                    Iterator it = set.iterator();

                    while (it.hasNext()) {
                        ConColi conColi = (ConColi) it.next();
                        Contact contact = conColi.getId().getContact();

                        if (emails.length() > 0) {
                            emails.append(", ");
                        }

                        String name = "";

                        if (!StringUtils.isBlank(contact.getConName())) {
                            name = contact.getConName() + " ";
                        }

                        emails.append(name + "<" + contact.getConEmail() + ">");
                    }

                    if (emails.length() > 0) {
                        obj.setEmails(emails.toString() + ", ");
                    }
                }

                groups.addElement(obj);
            }

            sobj.setGroups(groups);

            return sobj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public Vector suggestContacts(Session hsession, String repositoryName,
        String token) throws Exception {
        Vector contacts = new Vector();

        try {
            Criteria crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.ilike("coliName", token, MatchMode.ANYWHERE));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.addOrder(Order.asc("coliName"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                ContactList contactList = (ContactList) scroll.get(0);

                ContactObj obj = new ContactObj();
                obj.setIdint(contactList.getColiIdint());
                obj.setName(contactList.getColiName());
                obj.setEmail("GROUP");

                contacts.addElement(obj);
            }

            crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.or(Restrictions.ilike("conName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("conEmail", token, MatchMode.ANYWHERE)));
            crit.add(Restrictions.isNotNull("conSentDate"));
            crit.add(Restrictions.isNotNull("conReceivedDate"));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.addOrder(Order.desc("conCount"));

            scroll = crit.scroll();

            while (scroll.next()) {
                Contact contact = (Contact) scroll.get(0);

                ContactObj obj = new ContactObj();
                obj.setIdint(contact.getConIdint());
                obj.setName(contact.getConName());
                obj.setEmail(contact.getConEmail());

                contacts.addElement(obj);
            }

            return contacts;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public SearchContactsObj getContacts(Session hsession,
        String repositoryName, int page, int byPage, int order, String extra)
        throws Exception {
        Vector contacts = new Vector();

        SearchContactsObj sobj = new SearchContactsObj();

        try {
            Criteria crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.isNotNull("conSentDate"));
            crit.add(Restrictions.isNotNull("conReceivedDate"));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            int hits = crit.list().size();

            sobj.setHits(hits);

            crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.isNotNull("conSentDate"));
            crit.add(Restrictions.isNotNull("conReceivedDate"));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.setFirstResult(page * byPage);
            crit.setMaxResults(byPage);

            switch (order) {
            case ORDER_BY_EMAIL:
                crit.addOrder(Order.asc("conEmail"));

                break;

            case ORDER_BY_FREQUENCY_SENT:
                crit.addOrder(Order.desc("conCount"));

                break;

            case ORDER_BY_FREQUENCY_RECEIVED:
                crit.addOrder(Order.desc("conCount"));

                break;

            default:
                crit.addOrder(Order.desc("conCount"));

                break;
            }

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Contact contact = (Contact) scroll.get(0);

                ContactObj obj = new ContactObj();
                obj.setIdint(contact.getConIdint());
                obj.setName(contact.getConName());
                obj.setEmail(contact.getConEmail());

                if (!StringUtils.isBlank(contact.getConDescription())) {
                    obj.setDescription(contact.getConDescription()
                                              .replaceAll("\n", "\\\\n")
                                              .replaceAll("\r", "\\\\r")
                                              .replaceAll("'", "''"));
                }

                contacts.addElement(obj);
            }

            sobj.setContacts(contacts);

            return sobj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param byPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param extra DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public SearchGroupsObj getGroups(Session hsession, String repositoryName,
        int page, int byPage, int order, String extra)
        throws Exception {
        Vector contacts = new Vector();

        SearchGroupsObj sobj = new SearchGroupsObj();

        try {
            Criteria crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            int hits = crit.list().size();

            sobj.setHits(hits);

            crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.addOrder(Order.asc("coliName"));

            if (byPage > 0) {
                crit.setFirstResult(page * byPage);
                crit.setMaxResults(byPage);
            }

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                ContactList contactList = (ContactList) scroll.get(0);

                ContactListObj obj = new ContactListObj();
                obj.setIdint(contactList.getColiIdint());
                obj.setName(contactList.getColiName());

                Set set = contactList.getConColis();

                StringBuffer emails = new StringBuffer();

                if (set != null) {
                    Iterator it = set.iterator();

                    while (it.hasNext()) {
                        ConColi conColi = (ConColi) it.next();
                        Contact contact = conColi.getId().getContact();

                        if (emails.length() > 0) {
                            emails.append(", ");
                        }

                        String name = "";

                        if (!StringUtils.isBlank(contact.getConName())) {
                            name = contact.getConName().replaceAll("'", "\\\\'") +
                                " ";
                        }

                        emails.append(name + "<" + contact.getConEmail() + ">");
                    }

                    if (emails.length() > 0) {
                        obj.setEmails(emails.toString() + ", ");
                    }
                }

                contacts.addElement(obj);
            }

            sobj.setGroups(contacts);

            return sobj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idints DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void deleteContacts(Session hsession, String repositoryName,
        Integer[] idints) throws Exception {
        try {
            Users user = getUser(hsession, repositoryName);
            Criteria crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.eq("users", user));
            crit.add(Restrictions.in("conIdint", idints));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Contact contact = (Contact) scroll.get(0);

                Criteria crit1 = hsession.createCriteria(Identity.class);
                crit1.add(Restrictions.eq("ideEmail", contact.getConEmail()));
                crit1.add(Restrictions.eq("ideActive", new Boolean(true)));

                ScrollableResults scroll2 = crit1.scroll();

                while (scroll2.next()) {
                    Identity identity = (Identity) scroll2.get(0);
                    Users buddy = identity.getUsers();

                    if (buddy != null) {
                        Criteria auxCrit = hsession.createCriteria(BuddyList.class);
                        auxCrit.add(Restrictions.eq("usersByBuliOwnerIdint",
                                user));
                        auxCrit.add(Restrictions.eq("usersByBuliBuddyIdint",
                                buddy));

                        BuddyList buddyList = (BuddyList) auxCrit.uniqueResult();

                        if (buddyList != null) {
                            hsession.delete(buddyList);
                            hsession.flush();
                        }

                        auxCrit = hsession.createCriteria(BuddyList.class);
                        auxCrit.add(Restrictions.eq("usersByBuliOwnerIdint",
                                buddy));
                        auxCrit.add(Restrictions.eq("usersByBuliBuddyIdint",
                                user));

                        buddyList = (BuddyList) auxCrit.uniqueResult();

                        if (buddyList != null) {
                            hsession.delete(buddyList);
                            hsession.flush();
                        }
                    }
                }

                hsession.delete(contact);
                hsession.flush();
            }
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idints DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void deleteGroups(Session hsession, String repositoryName,
        Integer[] idints) throws Exception {
        try {
            Criteria crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));
            crit.add(Restrictions.in("coliIdint", idints));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                hsession.delete(scroll.get(0));
                hsession.flush();
            }
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param contactObj DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void updateContact(Session hsession, String repositoryName,
        ContactObj contactObj) throws Exception {
        try {
            Contact contact = (Contact) hsession.load(Contact.class,
                    contactObj.getIdint());

            contact.setConDescription(contactObj.getDescription());
            contact.setConEmail(contactObj.getEmail());
            contact.setConName(contactObj.getName());

            hsession.saveOrUpdate(contact);

            hsession.flush();
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public void createLabel(Session hsession, String repositoryName,
        LabelObj label) throws MailException {
        String lucenePathMessages = null;

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePathMessages = defaultLucenePath + File.separator +
                repositoryName + File.separator +
                Constants.MAIL_LUCENE_MESSAGES;
        } else {
            lucenePathMessages = defaultLucenePath + repositoryName +
                File.separator + Constants.MAIL_LUCENE_MESSAGES;
        }

        Searcher searcher = null;

        try {
            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Users user = getUser(hsession, repositoryName);

            if (user != null) {
                Label hlabel = new Label();
                hlabel.setUsers(user);
                hlabel.setLabName(label.getName());
                hsession.save(hlabel);
                hsession.flush();

                if (label.getFilters() != null) {
                    Iterator it = label.getFilters().iterator();

                    while (it.hasNext()) {
                        FilterObj filter = (FilterObj) it.next();
                        Filter hfilter = new Filter();

                        //hfilter.setUser(user);
                        hfilter.setLabel(hlabel);
                        hfilter.setFilArchive(filter.isArchive());
                        hfilter.setFilDoesntHaveWords(filter.getDoesntHaveWords());
                        hfilter.setFilForwardTo(filter.getForward());
                        hfilter.setFilFrom(filter.getFrom());

                        //CANVIDUROT
                        hfilter.setFilHasAttacment(filter.isHasAttachment());
                        hfilter.setFilHasWords(filter.getHasWords());
                        hfilter.setFilImportant(filter.isImportant());
                        hfilter.setFilSubject(filter.getSubject());
                        hfilter.setFilTo(filter.getTo());
                        hfilter.setFilTrash(filter.isTrash());
                        hfilter.setFilOrOperator(!filter.isOperator());

                        hsession.save(hfilter);
                        hsession.flush();

                        org.apache.lucene.search.Query query = FilterQueryParser.parse(hfilter,
                                analyzer);

                        if ((searcher != null) && (query != null)) {
                            Hits hits = searcher.search(query);

                            for (int j = 0; j < hits.length(); j++) {
                                Document doc = hits.doc(j);
                                String uid = doc.get(Field_idint);

                                Criteria crit = hsession.createCriteria(Message.class);
                                crit.add(Restrictions.eq("mesName", uid));
                                crit.add(Restrictions.eq("users", user));

                                Message message = (Message) crit.uniqueResult();

                                if (message != null) {
                                    try {
                                        LabMesId id = new LabMesId();
                                        id.setLabel(hlabel);
                                        id.setMessage(message);

                                        LabMes lm = new LabMes(id);
                                        hsession.saveOrUpdate(lm);
                                        hsession.flush();
                                    } catch (HibernateException e) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public void updateLabel(Session hsession, String repositoryName,
        LabelObj label) throws MailException {
        try {
            Criteria crit = hsession.createCriteria(Label.class);
            crit.add(Restrictions.eq("labIdint", label.getIdint()));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            Label hlabel = (Label) crit.uniqueResult();

            hlabel.setLabName(label.getName());

            hsession.update(hlabel);
            hsession.flush();
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void deleteLabel(Session hsession, String repositoryName,
        Integer idint) throws Exception {
        try {
            Criteria crit = hsession.createCriteria(Label.class);
            crit.add(Restrictions.eq("labIdint", idint));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            Label label = (Label) crit.uniqueResult();

            hsession.delete(label);

            hsession.flush();
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public void createFilter(Session hsession, String repositoryName,
        FilterObj filter) throws MailException {
        String lucenePathMessages = null;

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePathMessages = defaultLucenePath + File.separator +
                repositoryName + File.separator +
                Constants.MAIL_LUCENE_MESSAGES;
        } else {
            lucenePathMessages = defaultLucenePath + repositoryName +
                File.separator + Constants.MAIL_LUCENE_MESSAGES;
        }

        Searcher searcher = null;

        try {
            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Users user = getUser(hsession, repositoryName);

            if (user != null) {
                Criteria critLabel = hsession.createCriteria(Label.class);
                critLabel.add(Restrictions.eq("users", user));
                critLabel.add(Restrictions.eq("labIdint",
                        new Integer(filter.getLabel().getIdint())));

                Label hlabel = (Label) critLabel.uniqueResult();

                if (hlabel == null) {
                    hlabel = new Label();
                    hlabel.setUsers(user);
                    hlabel.setLabName(filter.getLabel().getName());
                    hsession.save(hlabel);
                    hsession.flush();
                }

                Filter hfilter = new Filter();

                //hfilter.setUser(user);
                hfilter.setLabel(hlabel);
                hfilter.setFilArchive(filter.isArchive());
                hfilter.setFilDoesntHaveWords(filter.getDoesntHaveWords());
                hfilter.setFilForwardTo(filter.getForward());
                hfilter.setFilFrom(filter.getFrom());

                //CANVIDUROT
                hfilter.setFilHasAttacment(filter.isHasAttachment());
                hfilter.setFilHasWords(filter.getHasWords());
                hfilter.setFilImportant(filter.isImportant());
                hfilter.setFilSubject(filter.getSubject());
                hfilter.setFilTo(filter.getTo());
                hfilter.setFilTrash(filter.isTrash());
                hfilter.setFilOrOperator(!filter.isOperator());

                hsession.save(hfilter);
                hsession.flush();

                org.apache.lucene.search.Query query = FilterQueryParser.parse(hfilter,
                        analyzer);

                if ((searcher != null) && (query != null)) {
                    Hits hits = searcher.search(query);

                    for (int j = 0; j < hits.length(); j++) {
                        Document doc = hits.doc(j);
                        String uid = doc.get(Field_idint);

                        Criteria crit = hsession.createCriteria(Message.class);
                        crit.add(Restrictions.eq("mesName", uid));
                        crit.add(Restrictions.eq("users", user));

                        Message message = (Message) crit.uniqueResult();

                        if (message != null) {
                            try {
                                LabMesId id = new LabMesId();
                                id.setLabel(hlabel);
                                id.setMessage(message);

                                LabMes lm = new LabMes(id);
                                hsession.saveOrUpdate(lm);
                                hsession.flush();
                            } catch (HibernateException e) {
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ManagerException DOCUMENT ME!
     */
    public void updateFilter(Session hsession, String repositoryName,
        FilterObj filter) throws MailException {
        String lucenePathMessages = null;

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePathMessages = defaultLucenePath + File.separator +
                repositoryName + File.separator +
                Constants.MAIL_LUCENE_MESSAGES;
        } else {
            lucenePathMessages = defaultLucenePath + repositoryName +
                File.separator + Constants.MAIL_LUCENE_MESSAGES;
        }

        Searcher searcher = null;

        try {
            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Users user = getUser(hsession, repositoryName);

            if (user != null) {
                Criteria crit1 = hsession.createCriteria(Label.class);
                crit1.add(Restrictions.eq("labIdint",
                        filter.getLabel().getIdint()));
                crit1.add(Restrictions.eq("users", user));

                Label hlabel = (Label) crit1.uniqueResult();

                Filter hfilter = (Filter) hsession.load(Filter.class,
                        filter.getIdint());

                hfilter.setFilArchive(filter.isArchive());
                hfilter.setFilDoesntHaveWords(filter.getDoesntHaveWords());
                hfilter.setFilForwardTo(filter.getForward());
                hfilter.setFilFrom(filter.getFrom());

                //CANVIDUROT
                hfilter.setFilHasAttacment(filter.isHasAttachment());
                hfilter.setFilHasWords(filter.getHasWords());
                hfilter.setFilImportant(filter.isImportant());
                hfilter.setFilSubject(filter.getSubject());
                hfilter.setFilTo(filter.getTo());
                hfilter.setFilTrash(filter.isTrash());
                hfilter.setFilOrOperator(!filter.isOperator());

                hfilter.setLabel(hlabel);

                hsession.update(hfilter);
                hsession.flush();

                org.apache.lucene.search.Query query = FilterQueryParser.parse(hfilter,
                        analyzer);

                if ((searcher != null) && (query != null)) {
                    Hits hits = searcher.search(query);

                    for (int j = 0; j < hits.length(); j++) {
                        Document doc = hits.doc(j);
                        String uid = doc.get(Field_idint);

                        Criteria crit2 = hsession.createCriteria(Message.class);
                        crit2.add(Restrictions.eq("mesName", uid));
                        crit2.add(Restrictions.eq("users", user));

                        Message message = (Message) crit2.uniqueResult();

                        if (message != null) {
                            try {
                                LabMesId id = new LabMesId();
                                id.setLabel(hlabel);
                                id.setMessage(message);

                                LabMes lm = new LabMes(id);
                                hsession.saveOrUpdate(lm);
                                hsession.flush();
                            } catch (HibernateException e) {
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void deleteFilter(Session hsession, String repositoryName,
        Integer idint) throws Exception {
        try {
            Query query = hsession.getNamedQuery("delete-filter-by-idint");
            query.setInteger("idint", idint);
            query.setString("username", repositoryName);

            query.executeUpdate();

            hsession.flush();
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public Vector getLabels(Session hsession, String repositoryName)
        throws MailException {
        Vector labels = new Vector();

        try {
            Criteria criteria = hsession.createCriteria(Label.class);
            criteria.add(Restrictions.eq("users",
                    getUser(hsession, repositoryName)));
            criteria.addOrder(Order.asc("labName"));

            ScrollableResults scroll = criteria.scroll();

            while (scroll.next()) {
                Label label = (Label) scroll.get(0);

                LabelObj obj = new LabelObj(label.getLabIdint(),
                        label.getLabName());

                labels.addElement(obj);
            }

            return labels;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public Vector getFilters(Session hsession, String repositoryName)
        throws MailException {
        Vector filters = new Vector();

        try {
            Query query = hsession.getNamedQuery("filters");
            query.setInteger("user",
                getUser(hsession, repositoryName).getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Filter filter = (Filter) scroll.get(0);
                FilterObj obj = new FilterObj();
                Label label = filter.getLabel();

                obj.setArchive(filter.isFilArchive());
                obj.setDoesntHaveWords(filter.getFilDoesntHaveWords());
                obj.setForward(filter.getFilForwardTo());
                obj.setFrom(filter.getFilFrom());
                obj.setHasAttachment(filter.isFilHasAttacment());
                obj.setHasWords(filter.getFilHasWords());
                obj.setIdint(filter.getFilIdint());
                obj.setImportant(filter.isFilImportant());
                obj.setLabel(new LabelObj(label.getLabIdint(),
                        label.getLabName()));
                obj.setOperator(!filter.isFilOrOperator());
                obj.setSubject(filter.getFilSubject());
                obj.setTo(filter.getFilTo());
                obj.setTrash(filter.isFilTrash());

                filters.addElement(obj);
            }

            return filters;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public FilterObj getFilter(Session hsession, String repositoryName,
        Integer idint) throws MailException {
        try {
            Query query = hsession.getNamedQuery("filter");
            query.setInteger("idint", idint);
            query.setInteger("user",
                getUser(hsession, repositoryName).getUseIdint());

            Filter filter = (Filter) query.uniqueResult();
            FilterObj obj = new FilterObj();
            Label label = filter.getLabel();

            obj.setArchive(filter.isFilArchive());
            obj.setDoesntHaveWords(filter.getFilDoesntHaveWords());
            obj.setForward(filter.getFilForwardTo());
            obj.setFrom(filter.getFilFrom());
            obj.setHasAttachment(filter.isFilHasAttacment());
            obj.setHasWords(filter.getFilHasWords());
            obj.setIdint(filter.getFilIdint());
            obj.setImportant(filter.isFilImportant());
            obj.setLabel(new LabelObj(label.getLabIdint(), label.getLabName()));
            obj.setOperator(!filter.isFilOrOperator());
            obj.setSubject(filter.getFilSubject());
            obj.setTo(filter.getFilTo());
            obj.setTrash(filter.isFilTrash());

            return obj;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param contactObj DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void addContact(Session hsession, String repositoryName,
        String action, ContactObj contactObj) throws MailException {
        Contact contact = null;

        try {
            Users user = getUser(hsession, repositoryName);

            if ((action != null) && action.equals("update")) {
                Criteria crit = hsession.createCriteria(Contact.class);
                crit.add(Restrictions.eq("users", user));
                crit.add(Restrictions.eq("conEmail", contactObj.getEmail()));

                contact = (Contact) crit.uniqueResult();
            }

            if (contact == null) {
                contact = new Contact();
            }

            contact.setConDescription(contactObj.getDescription());
            contact.setConEmail(contactObj.getEmail());
            contact.setConName(contactObj.getName());
            contact.setUsers(user);

            hsession.saveOrUpdate(contact);

            hsession.flush();
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param emails DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void addGroup(Session hsession, String repositoryName,
        String action, String name, String emails) throws MailException {
        ContactList contactList = null;

        try {
            Users user = getUser(hsession, repositoryName);

            InternetAddress[] tos = MessageUtilities.encodeAddresses(emails,
                    null);

            if ((action != null) && action.equals("update")) {
                Criteria crit = hsession.createCriteria(ContactList.class);
                crit.add(Restrictions.eq("users", user));
                crit.add(Restrictions.eq("coliName", name));

                contactList = (ContactList) crit.uniqueResult();
            }

            if (contactList == null) {
                contactList = new ContactList();

                contactList.setColiName(name);
                contactList.setUsers(user);

                for (int i = 0; i < tos.length; i++) {
                    Criteria crit = hsession.createCriteria(Contact.class);
                    crit.add(Restrictions.eq("users", user));
                    crit.add(Restrictions.eq("conEmail", tos[i].getAddress()));

                    Contact contact = (Contact) crit.uniqueResult();

                    if (contact == null) {
                        contact = new Contact();
                        contact.setConEmail(tos[i].getAddress());
                        contact.setConName(tos[i].getPersonal());
                        contact.setUsers(user);

                        hsession.saveOrUpdate(contact);
                        hsession.flush();
                    }

                    ConColiId id = new ConColiId(contact, contactList);
                    contactList.addConColi(new ConColi(id));
                }

                hsession.saveOrUpdate(contactList);
                hsession.flush();
            } else {
                contactList.setColiName(name);
                contactList.setUsers(user);

                for (int i = 0; i < tos.length; i++) {
                    Criteria crit = hsession.createCriteria(Contact.class);
                    crit.add(Restrictions.eq("users", user));
                    crit.add(Restrictions.eq("conEmail", tos[i].getAddress()));

                    Contact contact = (Contact) crit.uniqueResult();

                    if (contact == null) {
                        contact = new Contact();
                        contact.setConEmail(tos[i].getAddress());
                        contact.setConName(tos[i].getPersonal());
                        contact.setUsers(user);

                        hsession.saveOrUpdate(contact);
                        hsession.flush();
                    }

                    ConColiId id = new ConColiId(contact, contactList);
                    contactList.addConColi(new ConColi(id));
                }

                hsession.saveOrUpdate(contactList);
                hsession.flush();
            }
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param contactObj DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public ContactObj getContact(Session hsession, String repositoryName,
        int idint) throws MailException {
        ContactObj obj = null;

        try {
            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(Contact.class);
            crit.add(Restrictions.eq("users", user));
            crit.add(Restrictions.eq("conIdint", new Integer(idint)));

            Contact contact = (Contact) crit.uniqueResult();

            obj.setDescription(contact.getConDescription());
            obj.setEmail(contact.getConEmail());
            obj.setIdint(contact.getConIdint());
            obj.setName(contact.getConName());

            return obj;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param contactObj DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public ContactListObj getGroup(Session hsession, String repositoryName,
        int idint) throws MailException {
        ContactListObj obj = null;

        try {
            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(ContactList.class);
            crit.add(Restrictions.eq("users", user));
            crit.add(Restrictions.eq("coliIdint", new Integer(idint)));

            ContactList contactList = (ContactList) crit.uniqueResult();

            Set set = contactList.getConColis();

            StringBuffer emails = new StringBuffer();

            if (set != null) {
                Iterator it = set.iterator();

                while (it.hasNext()) {
                    ConColi conColi = (ConColi) it.next();
                    Contact contact = conColi.getId().getContact();

                    if (emails.length() > 0) {
                        emails.append(", ");
                    }

                    String name = "";

                    if (!StringUtils.isBlank(contact.getConName())) {
                        name = contact.getConName() + " ";
                    }

                    emails.append(name + "<" + contact.getConEmail() + ">");
                }

                if (emails.length() > 0) {
                    obj.setEmails(emails.toString());
                }
            }

            obj.setIdint(contactList.getColiIdint());
            obj.setName(contactList.getColiName());

            return obj;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param identityObj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public String createIdentity(Session hsession, String repositoryName,
        IdentityObj identityObj) throws MailException {
        String randomString = null;

        try {
            Users user = getUser(hsession, repositoryName);

            randomString = RandomStringUtils.randomAlphanumeric(25);

            Identity identity = new Identity();
            identity.setIdeActive(false);
            identity.setIdeCode(randomString);
            identity.setIdeDefault(identityObj.isImportant());
            identity.setIdeEmail(identityObj.getEmail());
            identity.setIdeName(identityObj.getName());

            if (!StringUtils.isBlank(identityObj.getReplyTo())) {
                identity.setIdeReplyTo(identityObj.getReplyTo());
            } else {
                identity.setIdeReplyTo(identityObj.getName());
            }

            identity.setUsers(user);

            hsession.save(identity);

            hsession.flush();

            return randomString;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void setIdentityDefault(Session hsession, String repositoryName,
        int idint) throws MailException {
        try {
            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.not(Restrictions.eq("ideIdint",
                        new Integer(idint))));
            crit.add(Restrictions.eq("users", user));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Identity identity = (Identity) scroll.get(0);
                identity.setIdeDefault(false);
                hsession.update(identity);
            }

            crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.eq("ideIdint", new Integer(idint)));
            crit.add(Restrictions.eq("users", user));

            Identity identity = (Identity) crit.uniqueResult();

            if (identity != null) {
                identity.setIdeDefault(true);
                hsession.update(identity);
                hsession.flush();
            } else {
                throw new MailException("Error in default identity");
            }
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public IdentityObj getIdentityDefault(Session hsession,
        String repositoryName) throws MailException {
        try {
            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.eq("ideDefault", new Boolean(true)));
            crit.add(Restrictions.eq("ideActive", new Boolean(true)));
            crit.add(Restrictions.eq("users", user));

            Identity identity = (Identity) crit.uniqueResult();

            IdentityObj identityObj = new IdentityObj();

            identityObj.setEmail(identity.getIdeEmail());
            identityObj.setImportant(identity.isIdeDefault());
            identityObj.setIdint(identity.getIdeIdint());
            identityObj.setName(identity.getIdeName());
            identityObj.setReplyTo(identity.getIdeReplyTo());

            return identityObj;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void validateIdentity(Session hsession, String repositoryName,
        String code) throws MailException {
        try {
            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.eq("ideCode", code));
            crit.add(Restrictions.eq("ideActive", new Boolean(false)));
            crit.add(Restrictions.eq("users", user));

            Identity identity = (Identity) crit.uniqueResult();

            if (identity != null) {
                identity.setIdeActive(true);
                hsession.update(identity);
                hsession.flush();

                if (identity.isIdeDefault()) {
                    setIdentityDefault(hsession, repositoryName,
                        identity.getIdeIdint());
                }
            }
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void deleteIdentity(Session hsession, String repositoryName,
        int idint) throws MailException {
        try {
            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(Identity.class);
            crit.add(Restrictions.eq("ideIdint", new Integer(idint)));
            crit.add(Restrictions.eq("users", user));

            Identity identity = (Identity) crit.uniqueResult();

            if (identity != null) {
                hsession.delete(identity);
                hsession.flush();
            }
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }
}
