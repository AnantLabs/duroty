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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.utils.AttachmentObj;
import com.duroty.application.mail.utils.ContactListObj;
import com.duroty.application.mail.utils.Counters;
import com.duroty.application.mail.utils.FolderObj;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.MailPartObj;
import com.duroty.application.mail.utils.MessageObj;
import com.duroty.hibernate.Attachment;
import com.duroty.hibernate.ContactList;
import com.duroty.hibernate.LabMes;
import com.duroty.hibernate.LabMesId;
import com.duroty.hibernate.Label;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.service.Messageable;
import com.duroty.service.analyzer.BayesianAnalysisFeeder;
import com.duroty.utils.GeneralOperations;
import com.duroty.utils.io.NullWriter;
import com.duroty.utils.log.DLog;
import com.duroty.utils.mail.MailPart;
import com.duroty.utils.mail.MessageUtilities;
import com.duroty.utils.misc.JavaScriptCleaner;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class MailManager implements MailManagerConstants {
    /**
     * DOCUMENT ME!
     */
    private static final String FOLDER_DELETE = "DELETE";

    /** DOCUMENT ME */
    private String folderAll;

    /** DOCUMENT ME */
    private String folderInbox;

    /** DOCUMENT ME */
    private String folderSent;

    /** DOCUMENT ME */
    private String folderTrash;

    /** DOCUMENT ME */
    private String folderDraft;

    /** DOCUMENT ME */
    private String folderSpam;

    /** DOCUMENT ME */
    private String folderBlog;

    /** DOCUMENT ME */
    private String folderChat;

    /** DOCUMENT ME */
    private String folderImportant;

    /** DOCUMENT ME */
    private String folderHidden;

    /**
     * DOCUMENT ME!
     */
    private Messageable messageable;

    /**
     * DOCUMENT ME!
     */
    private Tidy tidy = new Tidy();

    /**
     * DOCUMENT ME!
     */

    //private Analyzer analyzer;

    /**
     * DOCUMENT ME!
     */
    private int quoteSizeAlert;
    
    /**
     * DOCUMENT ME!
     */
    private HashMap extensions;

    /**
     * Creates a new MailManager object.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NamingException 
     */
    public MailManager(HashMap mail)
        throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException, NamingException {
        super();

        String messageFactory = (String) mail.get(Constants.MESSAGES_FACTORY);

        if ((messageFactory != null) && !messageFactory.trim().equals("")) {
            Class clazz = null;
            clazz = Class.forName(messageFactory.trim());
            this.messageable = (Messageable) clazz.newInstance();
            this.messageable.setProperties(mail);
        }

        this.folderAll = (String) mail.get(Constants.MAIL_FOLDER_ALL);
        this.folderInbox = (String) mail.get(Constants.MAIL_FOLDER_INBOX);
        this.folderSent = (String) mail.get(Constants.MAIL_FOLDER_SENT);
        this.folderTrash = (String) mail.get(Constants.MAIL_FOLDER_TRASH);
        this.folderBlog = (String) mail.get(Constants.MAIL_FOLDER_BLOG);
        this.folderDraft = (String) mail.get(Constants.MAIL_FOLDER_DRAFT);
        this.folderSpam = (String) mail.get(Constants.MAIL_FOLDER_SPAM);
        this.folderImportant = (String) mail.get(Constants.MAIL_FOLDER_IMPORTANT);
        this.folderHidden = (String) mail.get(Constants.MAIL_FOLDER_HIDDEN);
        this.folderChat = (String) mail.get(Constants.MAIL_FOLDER_CHAT);
        this.quoteSizeAlert = Integer.valueOf((String) mail.get(
                    Constants.MAIL_QUOTE_SIZE_ALERT)).intValue();

        tidy.setUpperCaseTags(true);
        tidy.setInputEncoding(Charset.defaultCharset().displayName());
        tidy.setOutputEncoding(Charset.defaultCharset().displayName());
        tidy.setMakeBare(true);
        tidy.setMakeClean(true);
        tidy.setShowWarnings(false);
        tidy.setErrout(new PrintWriter(new NullWriter()));
        tidy.setWord2000(true);
        tidy.setDropProprietaryAttributes(true);
        tidy.setFixBackslash(true);
        tidy.setXHTML(true);

        //tidy.setXmlOut(true);
        tidy.setWrapSection(true);
        tidy.setWrapScriptlets(true);
        tidy.setWrapPhp(true);
        tidy.setQuiet(true);
        tidy.setBreakBeforeBR(true);
        tidy.setEscapeCdata(true);
        tidy.setForceOutput(true);
        tidy.setHideComments(false);
        tidy.setPrintBodyOnly(true);
        tidy.setTidyMark(false);
        
        Map options = ApplicationConstants.options;

        Context ctx = new InitialContext();

        this.extensions = (HashMap) ctx.lookup((String) options.get(
                    Constants.EXTENSION_CONFIG));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public FolderObj[] getFolders() throws MailException {
        FolderObj[] folders = new FolderObj[8];

        folders[0] = new FolderObj(this.folderInbox);
        folders[1] = new FolderObj(this.folderImportant);
        folders[2] = new FolderObj(this.folderSent);
        folders[3] = new FolderObj(this.folderDraft);
        folders[4] = new FolderObj(this.folderAll);
        folders[5] = new FolderObj(this.folderChat);
        folders[6] = new FolderObj(this.folderTrash);

        //folders[6] = new FolderObj(this.folderBlog);        
        folders[7] = new FolderObj(this.folderSpam);

        return folders;
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
    public Vector getContactList(Session hsession, String repositoryName)
        throws MailException {
        Vector lists = new Vector();

        try {
            Criteria criteria = hsession.createCriteria(ContactList.class);
            criteria.add(Restrictions.eq("users",
                    getUser(hsession, repositoryName)));
            criteria.addOrder(Order.asc("coliName"));

            ScrollableResults scroll = criteria.scroll();

            while (scroll.next()) {
                ContactList contactList = (ContactList) scroll.get(0);

                ContactListObj obj = new ContactListObj(contactList.getColiIdint(),
                        contactList.getColiName());

                lists.addElement(obj);
            }

            return lists;
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
    * @param mids DOCUMENT ME!
    *
    * @throws MailException DOCUMENT ME!
    */
    public void archiveMessages(Session hsession, String repositoryName,
        String[] mids) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            Query query = hsession.getNamedQuery("archive-messages-by-mid");
            query.setString("hidden", this.folderHidden);
            query.setParameterList("mids", mids);
            query.setString("username", repositoryName);

            query.executeUpdate();

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
     * @param mids DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void deleteLabelsFromMessages(Session hsession,
        String repositoryName, String[] mids) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            Query query = hsession.getNamedQuery("delete-labels-by-mid");
            query.setParameterList("mids", mids);
            query.setString("username", repositoryName);

            query.executeUpdate();

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
     * @param mids DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void applyLabel(Session hsession, String repositoryName,
        Integer label, String[] mids) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            if ((label == null) || (label.intValue() <= 0)) {
                return;
            }

            Users user = getUser(hsession, repositoryName);

            Criteria crit = hsession.createCriteria(Label.class);
            crit.add(Restrictions.eq("labIdint", label));
            crit.add(Restrictions.eq("users", user));

            Label hlabel = (Label) crit.uniqueResult();

            crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.in("mesName", mids));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                LabMes labMes = new LabMes(new LabMesId(message, hlabel));
                hsession.saveOrUpdate(labMes);
                hsession.flush();
            }

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
     * @param folderName DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void deleteMessagesInFolder(Session hsession, String repositoryName,
        String folderName) throws MailException {
        try {
            folderName = parseFolder(folderName);
        } catch (Exception ex) {
            return;
        }

        String newFolder = this.folderTrash;

        if ((folderName != null) &&
                (folderName.equals(this.folderTrash) ||
                folderName.equals(this.folderSpam))) {
            newFolder = FOLDER_DELETE;
        }

        try {
            String[] boxes = null;

            if (folderName.equals(this.folderHidden)) {
                boxes = new String[] {
                        this.folderBlog, this.folderChat, this.folderHidden,
                        this.folderInbox, this.folderSent, this.folderDraft
                    };
            } else {
                boxes = new String[] { folderName };
            }

            Query query = hsession.getNamedQuery("delete-messages-by-folder");
            query.setString("trash", newFolder);
            query.setParameterList("box", boxes);
            query.setString("username", repositoryName);

            query.executeUpdate();

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
     * @param label DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void deleteMessagesInLabel(Session hsession, String repositoryName,
        Integer label) throws MailException {
        try {
            Query query = hsession.getNamedQuery("delete-messages-by-label");
            query.setString("trash", this.folderTrash);
            query.setInteger("label", label.intValue());
            query.setString("username", repositoryName);

            query.executeUpdate();

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
     * @param mids DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void deleteMessages(Session hsession, String repositoryName,
        String[] mids, String folderName) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            folderName = parseFolder(folderName);
        } catch (Exception ex) {
        }

        String newFolder = this.folderTrash;

        if ((folderName != null) &&
                (folderName.equals(this.folderTrash) ||
                folderName.equals(this.folderSpam))) {
            newFolder = FOLDER_DELETE;
        }

        try {
            Query query = hsession.getNamedQuery("delete-messages-by-mid");
            query.setString("trash", newFolder);
            query.setParameterList("mids", mids);
            query.setString("username", repositoryName);

            query.executeUpdate();

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
     * @param mid DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public String displayProperties(Session hsession, String repositoryName,
        String mid) throws MailException {
        try {
            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.eq("mesName", mid));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            Message message = (Message) crit.uniqueResult();

            if (message != null) {
                return message.getMesHeaders();
            } else {
                return null;
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
     * @param mids DOCUMENT ME!
     * @param flag DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void flagMessages(Session hsession, String repositoryName,
        String[] mids, String flag) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            Query query = null;

            if (flag.equals("RECENT")) {
                query = hsession.getNamedQuery("recent-messages-by-mid");
            } else if (flag.equals("FLAGGED")) {
                query = hsession.getNamedQuery("flagged-messages-by-mid");
            }

            query.setParameterList("mids", mids);
            query.setString("username", repositoryName);

            query.executeUpdate();

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
     * @param folderName DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public Vector getMessages(Session hsession, String repositoryName,
        String folderName, int page, int messagesByPage, int order,
        String orderType) throws MailException {
        Vector messages = new Vector();

        try {
            Users user = getUser(hsession, repositoryName);
            Locale locale = new Locale(user.getUseLanguage());
            TimeZone timeZone = TimeZone.getDefault();

            Date now = new Date();
            Calendar calendar = Calendar.getInstance(timeZone, locale);
            calendar.setTime(now);

            SimpleDateFormat formatter1 = new SimpleDateFormat("MMM dd", locale);
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss",
                    locale);
            SimpleDateFormat formatter3 = new SimpleDateFormat("MM/yy", locale);

            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.eq("users", user));

            if (folderName.equals(this.folderAll) ||
                    folderName.equals(this.folderHidden)) {
                crit.add(Restrictions.not(Restrictions.in("mesBox",
                            new String[] {
                                this.folderTrash, this.folderSpam,
                                this.folderChat, FOLDER_DELETE
                            })));
            } else if (folderName.equals(this.folderImportant)) {
                crit.add(Restrictions.eq("mesFlagged", new Boolean(true)));
            } else {
                crit.add(Restrictions.eq("mesBox", folderName));
            }

            switch (order) {
            case ORDER_BY_IMPORTANT:

                if (orderType.equals("ASC")) {
                    crit.addOrder(Order.asc("mesFlagged"));
                } else {
                    crit.addOrder(Order.desc("mesFlagged"));
                }

                break;

            case ORDER_BY_ADDRESS:

                if (orderType.equals("ASC")) {
                    crit.addOrder(Order.asc("mesFrom"));
                } else {
                    crit.addOrder(Order.desc("mesFrom"));
                }

                break;

            case ORDER_BY_SIZE:

                if (orderType.equals("ASC")) {
                    crit.addOrder(Order.asc("mesSize"));
                } else {
                    crit.addOrder(Order.desc("mesSize"));
                }

                break;

            case ORDER_BY_SUBJECT:

                if (orderType.equals("ASC")) {
                    crit.addOrder(Order.asc("mesSubject"));
                } else {
                    crit.addOrder(Order.desc("mesSubject"));
                }

                break;

            case ORDER_BY_DATE:

                if (orderType.equals("ASC")) {
                    crit.addOrder(Order.asc("mesDate"));
                } else {
                    crit.addOrder(Order.desc("mesDate"));
                }

                break;

            case ORDER_BY_UNREAD:

                if (orderType.equals("ASC")) {
                    crit.addOrder(Order.asc("mesRecent"));
                } else {
                    crit.addOrder(Order.desc("mesRecent"));
                }

                break;

            default:
                crit.addOrder(Order.desc("mesDate"));

                break;
            }

            crit.setFirstResult(page * messagesByPage);
            crit.setMaxResults(messagesByPage);

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                MessageObj obj = new MessageObj(message.getMesName());

                obj.setBox(message.getMesBox());

                obj.setFrom(message.getMesFrom());

                if ((message.getAttachments() != null) &&
                        (message.getAttachments().size() > 0)) {
                    obj.setHasAttachment(true);
                } else {
                    obj.setHasAttachment(false);
                }

                int size = message.getMesSize();
                size /= 1024;

                if (size > 1024) {
                    size /= 1024;
                    obj.setSize(size + " MB");
                } else {
                    obj.setSize(((size > 0) ? (size + "") : "<1") + " kB");
                }

                if (message.getMesBox().equals(folderSent)) {
                    try {
                        obj.setEmail(message.getMesTo());
                    } catch (Exception e) {
                        obj.setEmail("unknown to");
                    }
                } else {
                    obj.setEmail(message.getMesFrom());
                }

                Date date = message.getMesDate();

                if (date != null) {
                    Calendar calendar2 = Calendar.getInstance(timeZone, locale);
                    calendar2.setTime(date);

                    if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                Calendar.YEAR)) &&
                            (calendar.get(Calendar.MONTH) == calendar2.get(
                                Calendar.MONTH)) &&
                            (calendar.get(Calendar.DATE) == calendar2.get(
                                Calendar.DATE))) {
                        obj.setDateStr(formatter2.format(calendar2.getTime()));
                    } else if (calendar.get(Calendar.YEAR) == calendar2.get(
                                Calendar.YEAR)) {
                        obj.setDateStr(formatter1.format(calendar2.getTime()));
                    } else {
                        obj.setDateStr(formatter3.format(calendar2.getTime()));
                    }
                }

                obj.setDate(date);

                if (message.getLabMeses() != null) {
                    Iterator it = message.getLabMeses().iterator();
                    StringBuffer label = new StringBuffer();

                    while (it.hasNext()) {
                        if (label.length() > 0) {
                            label.append(", ");
                        }

                        LabMes labMes = (LabMes) it.next();
                        label.append(labMes.getId().getLabel().getLabName());
                    }

                    obj.setLabel(label.toString());
                }

                try {
                    if (StringUtils.isBlank(message.getMesSubject())) {
                        obj.setSubject("(no subject)");
                    } else {
                        obj.setSubject(message.getMesSubject());
                    }
                } catch (Exception ex) {
                    obj.setSubject("(no subject)");
                }

                if (message.isMesFlagged()) {
                    obj.setFlagged(true);
                } else {
                    obj.setFlagged(false);
                }

                if (message.isMesRecent()) {
                    obj.setRecent(true);
                } else {
                    obj.setRecent(false);
                }

                String priority = "normal";

                if (MessageUtilities.isHighPriority(message.getMesHeaders())) {
                    priority = "high";
                } else if (MessageUtilities.isLowPriority(
                            message.getMesHeaders())) {
                    priority = "low";
                }

                obj.setPriority(priority);

                messages.addElement(obj);
            }

            return messages;
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
     * @param folderName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public int getCountMessages(Session hsession, String repositoryName,
        String folderName) throws MailException {
        try {
            folderName = parseFolder(folderName);

            Query query = null;

            if (folderName.equals(this.folderAll) ||
                    folderName.equals(this.folderHidden)) {
                query = hsession.getNamedQuery("count-all-total-messages");
                query.setString("folderSpam", this.folderSpam);
                query.setString("folderTrash", this.folderTrash);
                query.setString("folderDelete", FOLDER_DELETE);
                query.setString("folderChat", this.folderChat);
            } else {
                query = hsession.getNamedQuery("count-messages-by-folder");
                query.setString("folder", folderName);
            }

            query.setInteger("user",
                getUser(hsession, repositoryName).getUseIdint());

            return ((Integer) query.uniqueResult()).intValue();
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
     * @param label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public int getCountMessages(Session hsession, String repositoryName,
        Label label) throws MailException {
        try {
            Query query = hsession.getNamedQuery("count-messages-by-label");
            query.setInteger("label", label.getLabIdint());
            query.setInteger("user",
                getUser(hsession, repositoryName).getUseIdint());
            query.setString("folderSpam", this.folderSpam);
            query.setString("folderTrash", this.folderTrash);
            query.setString("folderDelete", FOLDER_DELETE);

            return ((Integer) query.uniqueResult()).intValue();
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
     * @param label DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public Vector getMessages(Session hsession, String repositoryName,
        Label label, int page, int messagesByPage, int order, String orderType)
        throws MailException {
        Vector messages = new Vector();

        try {
            Users user = getUser(hsession, repositoryName);
            Locale locale = new Locale(user.getUseLanguage());
            TimeZone timeZone = TimeZone.getDefault();

            Date now = new Date();
            Calendar calendar = Calendar.getInstance(timeZone, locale);
            calendar.setTime(now);

            SimpleDateFormat formatter1 = new SimpleDateFormat("MMM dd", locale);
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss",
                    locale);
            SimpleDateFormat formatter3 = new SimpleDateFormat("MM/yy", locale);

            Query hquery = hsession.getNamedQuery("messages-by-label");
            String aux = hquery.getQueryString();

            switch (order) {
            case ORDER_BY_IMPORTANT:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_flagged asc";
                } else {
                    aux += " order by mes_flagged desc";
                }

                break;

            case ORDER_BY_ADDRESS:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_from asc";
                } else {
                    aux += " order by mes_from desc";
                }

                break;

            case ORDER_BY_SIZE:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_size asc";
                } else {
                    aux += " order by mes_size desc";
                }

                break;

            case ORDER_BY_SUBJECT:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_subject asc";
                } else {
                    aux += " order by mes_subject desc";
                }

                break;

            case ORDER_BY_DATE:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_date asc";
                } else {
                    aux += " order by mes_date desc";
                }

                break;

            case ORDER_BY_UNREAD:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_recent asc";
                } else {
                    aux += " order by mes_recent desc";
                }

                break;

            default:
                aux += " order by mes_date desc";

                break;
            }

            SQLQuery h2query = hsession.createSQLQuery(aux);
            h2query.addEntity("i", Message.class);
            h2query.setInteger("label", label.getLabIdint());
            h2query.setInteger("user", user.getUseIdint());
            h2query.setString("folderTrash", this.folderTrash);
            h2query.setString("folderSpam", this.folderSpam);
            h2query.setString("folderDelete", FOLDER_DELETE);
            h2query.setFirstResult(page * messagesByPage);
            h2query.setMaxResults(messagesByPage);

            ScrollableResults scroll = h2query.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                MessageObj obj = new MessageObj(message.getMesName());

                obj.setBox(message.getMesBox());

                obj.setFrom(message.getMesFrom());

                if ((message.getAttachments() != null) &&
                        (message.getAttachments().size() > 0)) {
                    obj.setHasAttachment(true);
                } else {
                    obj.setHasAttachment(false);
                }

                int size = message.getMesSize();
                size /= 1024;

                if (size > 1024) {
                    size /= 1024;
                    obj.setSize(size + " MB");
                } else {
                    obj.setSize(((size > 0) ? (size + "") : "<1") + " kB");
                }

                if (message.getMesBox().equals(folderSent)) {
                    try {
                        obj.setEmail(message.getMesTo());
                    } catch (Exception e) {
                        obj.setEmail("unknown to");
                    }
                } else {
                    obj.setEmail(message.getMesFrom());
                }

                Date date = message.getMesDate();

                if (date != null) {
                    Calendar calendar2 = Calendar.getInstance(timeZone, locale);
                    calendar2.setTime(date);

                    if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                Calendar.YEAR)) &&
                            (calendar.get(Calendar.MONTH) == calendar2.get(
                                Calendar.MONTH)) &&
                            (calendar.get(Calendar.DATE) == calendar2.get(
                                Calendar.DATE))) {
                        obj.setDateStr(formatter2.format(calendar2.getTime()));
                    } else if (calendar.get(Calendar.YEAR) == calendar2.get(
                                Calendar.YEAR)) {
                        obj.setDateStr(formatter1.format(calendar2.getTime()));
                    } else {
                        obj.setDateStr(formatter3.format(calendar2.getTime()));
                    }
                }

                obj.setDate(date);

                if (message.getLabMeses() != null) {
                    Iterator it = message.getLabMeses().iterator();
                    StringBuffer buff = new StringBuffer();

                    while (it.hasNext()) {
                        if (buff.length() > 0) {
                            buff.append(", ");
                        }

                        LabMes labMes = (LabMes) it.next();
                        buff.append(labMes.getId().getLabel().getLabName());
                    }

                    obj.setLabel(buff.toString());
                }

                try {
                    if (StringUtils.isBlank(message.getMesSubject())) {
                        obj.setSubject("(no subject)");
                    } else {
                        obj.setSubject(message.getMesSubject());
                    }
                } catch (Exception ex) {
                    obj.setSubject("(no subject)");
                }

                if (message.isMesFlagged()) {
                    obj.setFlagged(true);
                } else {
                    obj.setFlagged(false);
                }

                if (message.isMesRecent()) {
                    obj.setRecent(true);
                } else {
                    obj.setRecent(false);
                }

                String priority = "normal";

                if (MessageUtilities.isHighPriority(message.getMesHeaders())) {
                    priority = "high";
                } else if (MessageUtilities.isLowPriority(
                            message.getMesHeaders())) {
                    priority = "low";
                }

                obj.setPriority(priority);

                messages.addElement(obj);
            }

            return messages;
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
     * @param folderName DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public MessageObj getMessage(Session hsession, String repositoryName,
        String mid, boolean readReferences, boolean isHtml,
        boolean displayImages) throws MailException {
        ByteArrayOutputStream baos = null;

        try {
            Users user = getUser(hsession, repositoryName);
            Locale locale = new Locale(user.getUseLanguage());
            TimeZone timeZone = TimeZone.getDefault();

            Date now = new Date();
            Calendar calendar = Calendar.getInstance(timeZone, locale);
            calendar.setTime(now);

            SimpleDateFormat formatter1 = new SimpleDateFormat("MMM dd", locale);
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss",
                    locale);
            SimpleDateFormat formatter3 = new SimpleDateFormat("MM/yy", locale);

            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.eq("mesName", mid));
            crit.add(Restrictions.eq("users", user));

            Message message = (Message) crit.uniqueResult();

            message.setMesRecent(false);
            hsession.update(message);
            hsession.flush();

            MessageObj obj = new MessageObj(message.getMesName());

            obj.setBox(message.getMesBox());

            obj.setFrom(message.getMesFrom());
            obj.setTo(message.getMesTo());
            obj.setReplyTo(message.getMesReplyTo());
            obj.setCc(message.getMesCc());

            if ((message.getAttachments() != null) &&
                    (message.getAttachments().size() > 0)) {
                obj.setHasAttachment(true);

                Set set = message.getAttachments();
                Iterator it = set.iterator();
                Vector attachments = new Vector();

                while (it.hasNext()) {
                    Attachment attachment = (Attachment) it.next();
                    AttachmentObj attachmentObj = new AttachmentObj();
                    attachmentObj.setIdint(attachment.getAttPart());
                    attachmentObj.setName(attachment.getAttName());

                    int size = attachment.getAttSize();
                    size /= 1024;

                    if (size > 1024) {
                        size /= 1024;
                        attachmentObj.setSize(size + " MB");
                    } else {
                        attachmentObj.setSize(((size > 0) ? (size + "") : "<1") +
                            " kB");
                    }
                    
                    String extension = (String) this.extensions.get(attachment.getAttContentType());
                    
                    if (StringUtils.isBlank(extension)) {
                    	extension = "generic";
                    }

                    attachmentObj.setExtension(extension);

                    attachmentObj.setContentType(attachment.getAttContentType());
                    attachments.addElement(attachmentObj);
                    obj.setAttachments(attachments);
                }
            } else {
                obj.setHasAttachment(false);
            }

            int size = message.getMesSize();
            size /= 1024;

            if (size > 1024) {
                size /= 1024;
                obj.setSize(size + " MB");
            } else {
                obj.setSize(((size > 0) ? (size + "") : "<1") + " kB");
            }

            if (message.getMesBox().equals(folderSent)) {
                try {
                    obj.setEmail(message.getMesTo());
                } catch (Exception e) {
                    obj.setEmail("unknown to");
                }
            } else {
                obj.setEmail(message.getMesFrom());
            }

            Date date = message.getMesDate();

            if (date != null) {
                Calendar calendar2 = Calendar.getInstance(timeZone, locale);
                calendar2.setTime(date);

                if ((calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) &&
                        (calendar.get(Calendar.MONTH) == calendar2.get(
                            Calendar.MONTH)) &&
                        (calendar.get(Calendar.DATE) == calendar2.get(
                            Calendar.DATE))) {
                    obj.setDateStr(formatter2.format(calendar2.getTime()));
                } else if (calendar.get(Calendar.YEAR) == calendar2.get(
                            Calendar.YEAR)) {
                    obj.setDateStr(formatter1.format(calendar2.getTime()));
                } else {
                    obj.setDateStr(formatter3.format(calendar2.getTime()));
                }
            }

            obj.setDate(date);

            if (message.getLabMeses() != null) {
                Iterator it = message.getLabMeses().iterator();
                StringBuffer label = new StringBuffer();

                while (it.hasNext()) {
                    if (label.length() > 0) {
                        label.append(", ");
                    }

                    LabMes labMes = (LabMes) it.next();
                    label.append(labMes.getId().getLabel().getLabName());
                }

                obj.setLabel(label.toString());
            }

            try {
                if (StringUtils.isBlank(message.getMesSubject())) {
                    obj.setSubject("(no subject)");
                } else {
                    obj.setSubject(message.getMesSubject());
                }
            } catch (Exception ex) {
                obj.setSubject("(no subject)");
            }

            if (message.isMesFlagged()) {
                obj.setFlagged(true);
            } else {
                obj.setFlagged(false);
            }

            if (message.isMesRecent()) {
                obj.setRecent(true);
            } else {
                obj.setRecent(false);
            }

            String priority = "normal";

            if (MessageUtilities.isHighPriority(message.getMesHeaders())) {
                priority = "high";
            } else if (MessageUtilities.isLowPriority(message.getMesHeaders())) {
                priority = "low";
            }

            obj.setPriority(priority);

            String body = message.getMesBody();

            if (!StringUtils.isBlank(body)) {
                obj.setBody(bodyCleaner(body, displayImages));
            } else {
                obj.setBody("");
            }

            String midReferences = message.getMesReferences();

            if (readReferences && (midReferences != null) &&
                    !midReferences.trim().equals("")) {
                obj.setReferencesBefore(readReference(hsession, user, mid,
                        midReferences, date, true, isHtml, displayImages));
                obj.setReferencesAfter(readReference(hsession, user, mid,
                        midReferences, date, false, isHtml, displayImages));
            }

            return obj;
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
            IOUtils.closeQuietly(baos);
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param mids DOCUMENT ME!
     * @param hash DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public MailPartObj getAttachment(Session hsession, String repositoryName,
        String mid, String hash) throws MailException {
    	MailPartObj part = null;

        try {
            MimeMessage mime = messageable.getMimeMessage(mid,
                    getUser(hsession, repositoryName));

            if (mime != null) {
                Vector dmailParts = new Vector();
                MessageUtilities.decodeContent(mime, new StringBuffer(),
                    dmailParts, true, "<br/>");

                while (dmailParts.size() > 0) {
                    MailPart aux = (MailPart) dmailParts.remove(0);

                    if (aux.getId() == Integer.parseInt(hash)) {
                    	part = new MailPartObj();
                    	part.setAttachent(IOUtils.toByteArray(aux.getPart().getInputStream()));
                    	part.setContentType(aux.getContentType());
                    	part.setId(aux.getId());
                    	part.setName(aux.getName());
                    	part.setSize(aux.getSize());

                        break;
                    }
                }
            } else {
                throw new MailException("The Attachment is null");
            }

            if (part == null) {
                throw new MailException("The Attachment is null");
            }

            return part;
        } catch (Exception ex) {
            throw new MailException(ex);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new MailException(ex);
        } catch (Throwable ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            System.gc();
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
    public Counters getInfoCounters(Session hsession, String repositoryName)
        throws MailException {
        Counters counters = new Counters();

        try {
            Users user = getUser(hsession, repositoryName);

            Query query = hsession.getNamedQuery("count-new-messages-by-folder");
            query.setString("folder", this.folderInbox);
            query.setInteger("user", user.getUseIdint());
            counters.setInbox(((Integer) query.uniqueResult()).intValue());

            query = hsession.getNamedQuery("count-new-messages-by-folder");
            query.setString("folder", this.folderSpam);
            query.setInteger("user", user.getUseIdint());
            counters.setSpam(((Integer) query.uniqueResult()).intValue());

            query = hsession.getNamedQuery("group-count-new-messages-by-label");
            query.setInteger("user", user.getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Integer idint = (Integer) scroll.get(0);
                Integer count = (Integer) scroll.get(1);

                counters.addLabel(idint.intValue(), count.intValue());
            }

            counters.setQuota(getQuotaLayer(hsession, user));
        } catch (Exception ex) {
            return null;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }

        return counters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    private String getQuotaLayer(Session hsession, Users user)
        throws MailException {
        String quotaLayer = "";

        int maxQuotaSize = getMaxQuotaSize(hsession, user);
        int usedQuotaSize = getUsedQuotaSize(hsession, user);
        int alertQuotaSize = this.quoteSizeAlert;
        int alertSize = (alertQuotaSize * maxQuotaSize) / 100;

        int count = maxQuotaSize - usedQuotaSize;

        if (count < 0) {
            String mqs = null;
            maxQuotaSize /= 1024;

            if (maxQuotaSize > 1024) {
                maxQuotaSize /= 1024;
                mqs = maxQuotaSize + " MB";
            } else {
                mqs = ((maxQuotaSize > 0) ? (maxQuotaSize + "") : "<1") +
                    " kB";
            }

            String uqs = null;
            usedQuotaSize /= 1024;

            if (usedQuotaSize > 1024) {
                usedQuotaSize /= 1024;
                uqs = usedQuotaSize + " MB";
            } else {
                uqs = ((usedQuotaSize > 0) ? (usedQuotaSize + "") : "<1") +
                    " kB";
            }

            String cs = null;
            count /= 1024;

            if (count > 1024) {
                count /= 1024;
                cs = count + " MB";
            } else {
                cs = ((count > 0) ? (count + "") : "<1") + " kB";
            }

            quotaLayer = "<span class='quotaExceded'>(" + cs +
                ") Quota Alert:</span> " + uqs + " / " + mqs;
        } else {
            if (count <= alertSize) {
                String mqs = null;
                maxQuotaSize /= 1024;

                if (maxQuotaSize > 1024) {
                    maxQuotaSize /= 1024;
                    mqs = maxQuotaSize + " MB";
                } else {
                    mqs = ((maxQuotaSize > 0) ? (maxQuotaSize + "") : "<1") +
                        " kB";
                }

                String uqs = null;
                usedQuotaSize /= 1024;

                if (usedQuotaSize > 1024) {
                    usedQuotaSize /= 1024;
                    uqs = usedQuotaSize + " MB";
                } else {
                    uqs = ((usedQuotaSize > 0) ? (usedQuotaSize + "") : "<1") +
                        " kB";
                }

                String cs = null;
                count /= 1024;

                if (count > 1024) {
                    count /= 1024;
                    cs = count + " MB";
                } else {
                    cs = ((count > 0) ? (count + "") : "<1") + " kB";
                }

                quotaLayer = "<span class='quotaExceded'>(" + cs +
                    ") Quota Alert:</span> " + uqs + " / " + mqs;
            } else {
                String mqs = null;
                maxQuotaSize /= 1024;

                if (maxQuotaSize > 1024) {
                    maxQuotaSize /= 1024;
                    mqs = maxQuotaSize + " MB";
                } else {
                    mqs = ((maxQuotaSize > 0) ? (maxQuotaSize + "") : "<1") +
                        " kB";
                }

                String uqs = null;
                usedQuotaSize /= 1024;

                if (usedQuotaSize > 1024) {
                    usedQuotaSize /= 1024;
                    uqs = usedQuotaSize + " MB";
                } else {
                    uqs = ((usedQuotaSize > 0) ? (usedQuotaSize + "") : "<1") +
                        " kB";
                }

                quotaLayer = "<b>" + uqs + "</b>" + " / <b>" + mqs + "</b>";
            }
        }

        return quotaLayer;
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
    protected Users getUser(Session hsession, String repositoryName)
        throws Exception {
        try {
            Criteria criteria = hsession.createCriteria(Users.class);
            criteria.add(Restrictions.eq("useUsername", repositoryName));
            criteria.add(Restrictions.eq("useActive", new Boolean(true)));

            return (Users) criteria.uniqueResult();
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param mid DOCUMENT ME!
     * @param midReferences DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param beforeSent DOCUMENT ME!
     * @param isHtml DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Vector readReference(Session hsession, Users user, String mid,
        String midReferences, Date date1, boolean beforeSent, boolean isHtml,
        boolean displayImages) {
        Vector references = new Vector();
        ByteArrayOutputStream baos = null;

        try {
            Locale locale = new Locale(user.getUseLanguage());
            TimeZone timeZone = TimeZone.getDefault();

            Date now = new Date();
            Calendar calendar = Calendar.getInstance(timeZone, locale);
            calendar.setTime(now);

            SimpleDateFormat formatter1 = new SimpleDateFormat("MMM dd", locale);
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss",
                    locale);
            SimpleDateFormat formatter3 = new SimpleDateFormat("MM/yy", locale);

            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.not(Restrictions.eq("mesName", mid)));
            crit.add(Restrictions.eq("mesReferences", midReferences));
            crit.add(Restrictions.eq("users", user));
            crit.add(Restrictions.not(Restrictions.eq("mesBox", this.folderSpam)));
            crit.add(Restrictions.not(Restrictions.eq("mesBox", this.folderTrash)));
            crit.add(Restrictions.not(Restrictions.eq("mesBox", FOLDER_DELETE)));

            if (beforeSent) {
                crit.add(Restrictions.le("mesDate", date1));
            } else {
                crit.add(Restrictions.gt("mesDate", date1));
            }

            crit.addOrder(Order.asc("mesDate"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                message.setMesRecent(false);
                hsession.update(message);
                hsession.flush();

                MessageObj obj = new MessageObj(message.getMesName());

                obj.setBox(message.getMesBox());

                obj.setFrom(message.getMesFrom());
                obj.setTo(message.getMesTo());
                obj.setReplyTo(message.getMesReplyTo());
                obj.setCc(message.getMesCc());

                if ((message.getAttachments() != null) &&
                        (message.getAttachments().size() > 0)) {
                    obj.setHasAttachment(true);

                    Set set = message.getAttachments();
                    Iterator it = set.iterator();
                    Vector attachments = new Vector();

                    while (it.hasNext()) {
                        Attachment attachment = (Attachment) it.next();
                        AttachmentObj attachmentObj = new AttachmentObj();
                        attachmentObj.setIdint(attachment.getAttPart());
                        attachmentObj.setName(attachment.getAttName());

                        int size = attachment.getAttSize();
                        size /= 1024;

                        if (size > 1024) {
                            size /= 1024;
                            attachmentObj.setSize(size + " MB");
                        } else {
                            attachmentObj.setSize(((size > 0) ? (size + "") : "<1") +
                                " kB");
                        }
                        
                        String extension = (String) this.extensions.get(attachment.getAttContentType());
                        
                        if (StringUtils.isBlank(extension)) {
                        	extension = "generic";
                        }

                        attachmentObj.setExtension(extension);

                        attachmentObj.setContentType(attachment.getAttContentType());
                        attachments.addElement(attachmentObj);
                        obj.setAttachments(attachments);
                    }
                } else {
                    obj.setHasAttachment(false);
                }

                int size = message.getMesSize();
                size /= 1024;

                if (size > 1024) {
                    size /= 1024;
                    obj.setSize(size + " MB");
                } else {
                    obj.setSize(((size > 0) ? (size + "") : "<1") + " kB");
                }

                if (message.getMesBox().equals(folderSent)) {
                    try {
                        obj.setEmail(message.getMesTo());
                    } catch (Exception e) {
                        obj.setEmail("unknown to");
                    }
                } else {
                    obj.setEmail(message.getMesFrom());
                }

                Date date = message.getMesDate();

                if (date != null) {
                    Calendar calendar2 = Calendar.getInstance(timeZone, locale);
                    calendar2.setTime(date);

                    if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                Calendar.YEAR)) &&
                            (calendar.get(Calendar.MONTH) == calendar2.get(
                                Calendar.MONTH)) &&
                            (calendar.get(Calendar.DATE) == calendar2.get(
                                Calendar.DATE))) {
                        obj.setDateStr(formatter2.format(calendar2.getTime()));
                    } else if (calendar.get(Calendar.YEAR) == calendar2.get(
                                Calendar.YEAR)) {
                        obj.setDateStr(formatter1.format(calendar2.getTime()));
                    } else {
                        obj.setDateStr(formatter3.format(calendar2.getTime()));
                    }
                }

                obj.setDate(date);

                if (message.getLabMeses() != null) {
                    Iterator it = message.getLabMeses().iterator();
                    StringBuffer label = new StringBuffer();

                    while (it.hasNext()) {
                        if (label.length() > 0) {
                            label.append(", ");
                        }

                        LabMes labMes = (LabMes) it.next();
                        label.append(labMes.getId().getLabel().getLabName());
                    }

                    obj.setLabel(label.toString());
                }

                try {
                    if (StringUtils.isBlank(message.getMesSubject())) {
                        obj.setSubject("(no subject)");
                    } else {
                        obj.setSubject(message.getMesSubject());
                    }
                } catch (Exception ex) {
                    obj.setSubject("(no subject)");
                }

                if (message.isMesFlagged()) {
                    obj.setFlagged(true);
                } else {
                    obj.setFlagged(false);
                }

                if (message.isMesRecent()) {
                    obj.setRecent(true);
                } else {
                    obj.setRecent(false);
                }

                String priority = "normal";

                if (MessageUtilities.isHighPriority(message.getMesHeaders())) {
                    priority = "high";
                } else if (MessageUtilities.isLowPriority(
                            message.getMesHeaders())) {
                    priority = "low";
                }

                obj.setPriority(priority);

                String body = message.getMesBody();

                if (!StringUtils.isBlank(body)) {
                    obj.setBody(bodyCleaner(body, displayImages));
                } else {
                    obj.setBody("");
                }

                references.addElement(obj);
            }

            scroll.close();
        } catch (Exception ex) {
            return null;
        } finally {
            IOUtils.closeQuietly(baos);
        }

        if (references.size() <= 0) {
            return null;
        }

        return references;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param mid DOCUMENT ME!
     * @param hash DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void spamMessage(DataSource dataSource, Session hsession,
        String repositoryName, String[] mids) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            MailetAnalyzerThread mat = new MailetAnalyzerThread(messageable,
                    dataSource, getUser(hsession, repositoryName), "SPAM", mids);
            mat.start();

            moveMessages(hsession, repositoryName, mids, this.folderSpam);
        } catch (Exception ex) {
            throw new MailException(ex);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new MailException(ex);
        } catch (Throwable ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param mid DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void notSpamMessage(DataSource dataSource, Session hsession,
        String repositoryName, String[] mids) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            MailetAnalyzerThread mat = new MailetAnalyzerThread(messageable,
                    dataSource, getUser(hsession, repositoryName), "NOTSPAM",
                    mids);
            mat.start();

            moveMessages(hsession, repositoryName, mids, this.folderInbox);
        } catch (Exception ex) {
            throw new MailException(ex);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new MailException(ex);
        } catch (Throwable ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param mids DOCUMENT ME!
     * @param folder DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public void moveMessages(Session hsession, String repositoryName,
        String[] mids, String folder) throws MailException {
        if ((mids == null) || (mids.length == 0)) {
            throw new MailException("ErrorMessages.messages.selection.null");
        }

        try {
            folder = parseFolder(folder);

            if (folder.equals(this.folderAll)) {
                folder = this.folderHidden;
            }

            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.in("mesName", mids));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                message.setMesBox(folder);

                hsession.update(message);
                hsession.flush();
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
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    private String parseFolder(String name) throws MailException {
        if (name == null) {
            throw new MailException("This Folder is null");
        }

        if (name.equals(this.folderAll)) {
            name = this.folderHidden;

            return name;
        }

        if (name.equals(this.folderHidden) ||
                name.equals(this.folderImportant) ||
                name.equals(this.folderAll) || name.equals(this.folderBlog) ||
                name.equals(this.folderDraft) || name.equals(this.folderInbox) ||
                name.equals(this.folderSent) || name.equals(this.folderSpam) ||
                name.equals(this.folderTrash) || name.equals(this.folderChat)) {
            return name;
        } else {
            throw new MailException("This Folder not exist: " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DMailException DOCUMENT ME!
     */
    protected int getMaxQuotaSize(org.hibernate.Session hsession, Users user)
        throws MailException {
        try {
            Criteria crit = hsession.createCriteria(MailPreferences.class);
            crit.add(Restrictions.eq("users", user));

            MailPreferences preferences = (MailPreferences) crit.uniqueResult();

            return preferences.getMaprQuotaSize();
        } catch (Exception ex) {
            return 0;
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DMailException DOCUMENT ME!
     */
    protected int getUsedQuotaSize(org.hibernate.Session hsession, Users user)
        throws MailException {
        try {
            Query query = hsession.getNamedQuery("used-quota-size");
            query.setInteger("user", new Integer(user.getUseIdint()));

            Integer value = (Integer) query.uniqueResult();

            int uqs = value.intValue();
            int pc = ((uqs * 25) / 100);

            return uqs + pc;
        } catch (Exception ex) {
            return 0;
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param body DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String bodyCleaner(String body, boolean displayImages) {
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();

            Document dom = tidy.parseDOM(IOUtils.toInputStream(body), null);

            new JavaScriptCleaner(dom, displayImages);

            //Prepare the DOM document for writing
            Source source = new DOMSource(dom);

            // Prepare the output file
            Result result = new StreamResult(baos);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance()
                                                    .newTransformer();
            xformer.transform(source, result);

            body = baos.toString(Charset.defaultCharset().displayName());

            IOUtils.closeQuietly(baos);
            baos = null;
            baos = new ByteArrayOutputStream();

            tidy.parse(IOUtils.toInputStream(body), baos);

            body = baos.toString();

            /*if (!StringUtils.isBlank(body)) {
                    Pattern pattern = Pattern.compile("(^<a)([\\s\\n]*)(\\w+:\\/\\/[^\\s\\n]+)", Pattern.CASE_INSENSITIVE);
                    //Pattern pattern = Pattern.compile("((\\s|>)+(http://|https://|www.|ftp://|file:/|mailto:)[^ ]*(.*[^<]))", Pattern.CASE_INSENSITIVE);


                    Matcher matcher = pattern.matcher(body);

                    StringBuffer sb = new StringBuffer();

                    while (matcher.find()) {
                            int count = matcher.groupCount();
                                String m1 = matcher.group(0).trim();
                                String m2 = matcher.group(1);
                                matcher.appendReplacement(sb, m2 + "<a href=\"" + m1 + "\" target=\"_new\">" + m1 + "</a>");
                    }

                    if (sb.length() > 0) {
                            return sb.toString();
                    } else {
                            return body;
                    }
            } else {
                return "";
            }*/
            return body;
        } catch (Exception ex) {
            return "";
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision$
     */
    private class MailetAnalyzerThread extends Thread {
        /**
         * DOCUMENT ME!
         */
        private Messageable messageable;

        /**
         * DOCUMENT ME!
         */
        private Users user;

        /**
         * DOCUMENT ME!
         */
        private String category;

        /**
         * DOCUMENT ME!
         */
        private String[] mids;

        /**
         * Creates a new MailetAnalyzerThread object.
         *
         * @param mail DOCUMENT ME!
         * @param category DOCUMENT ME!
         * @param mime DOCUMENT ME!
         */
        public MailetAnalyzerThread(Messageable messageable,
            DataSource dataSource, Users user, String category, String[] mids) {
            this.messageable = messageable;
            this.user = user;
            this.category = category;
            this.mids = mids;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            try {
                String username = user.getUseUsername();

                BayesianAnalysisFeeder mailetAnalyzer = new BayesianAnalysisFeeder();
                mailetAnalyzer.init(null);

                for (int i = 0; i < mids.length; i++) {
                    String feedType = "spam";

                    if (category.equals("NOTSPAM")) {
                        feedType = "ham";
                    }

                    MimeMessage mime = messageable.getMimeMessage(mids[i], user);
                    mailetAnalyzer.service(username, feedType, mime);
                }
            } catch (Exception e) {
                DLog.log(DLog.WARN, this.getClass(), e);
            } catch (java.lang.OutOfMemoryError ex) {
                System.gc();
            } catch (Throwable e) {
                System.gc();
            }
        }
    }
}
