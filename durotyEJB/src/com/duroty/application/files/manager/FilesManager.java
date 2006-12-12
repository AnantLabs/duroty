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
package com.duroty.application.files.manager;

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
import java.util.TimeZone;
import java.util.Vector;

import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

import com.duroty.application.files.exceptions.FilesException;
import com.duroty.application.files.utils.AttachmentObj;
import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.utils.Counters;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.MailPartObj;
import com.duroty.hibernate.AttachmentWithDate;
import com.duroty.hibernate.LabMes;
import com.duroty.hibernate.LabMesId;
import com.duroty.hibernate.Label;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.service.Messageable;
import com.duroty.utils.GeneralOperations;
import com.duroty.utils.io.NullWriter;
import com.duroty.utils.mail.MailPart;
import com.duroty.utils.mail.MessageUtilities;
import com.duroty.utils.misc.JavaScriptCleaner;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class FilesManager implements FilesManagerConstants {
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

    /** DOCUMENT ME */
    private String folderFiles;

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
    public FilesManager(HashMap mail)
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
        this.folderFiles = (String) mail.get(Constants.MAIL_FOLDER_FILES);
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
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public Vector getLabels(Session hsession, String repositoryName)
        throws FilesException {
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
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public void deleteLabelsFromFiles(Session hsession, String repositoryName,
        String[] mids) throws FilesException {
        if ((mids == null) || (mids.length == 0)) {
            throw new FilesException("ErrorMessages.messages.selection.null");
        }

        try {
            Query query = hsession.getNamedQuery("delete-labels-by-mid");
            query.setParameterList("mids", mids);
            query.setString("username", repositoryName);

            query.executeUpdate();

            hsession.flush();
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public void applyLabel(Session hsession, String repositoryName,
        Integer label, Integer[] idints) throws FilesException {
        if ((idints == null) || (idints.length == 0)) {
            throw new FilesException("ErrorMessages.messages.selection.null");
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

            Query query = hsession.getNamedQuery("messages-by-attachments");
            query.setParameterList("idints", idints);
            query.setInteger("user", user.getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                LabMes labMes = new LabMes(new LabMesId(message, hlabel));
                hsession.saveOrUpdate(labMes);
                hsession.flush();
            }

            hsession.flush();
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public void flagFiles(Session hsession, String repositoryName,
        Integer[] idints) throws FilesException {
        if ((idints == null) || (idints.length == 0)) {
            throw new FilesException("ErrorMessages.messages.selection.null");
        }

        try {
            Users user = getUser(hsession, repositoryName);

            Query query = hsession.getNamedQuery("messages-by-attachments");
            query.setParameterList("idints", idints);
            query.setInteger("user", user.getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                if (message.isMesFlagged()) {
                    message.setMesFlagged(false);
                } else {
                    message.setMesFlagged(true);
                }

                hsession.update(message);
                hsession.flush();
            }

            hsession.flush();
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public void deleteFiles(Session hsession, String repositoryName,
        Integer[] idints) throws FilesException {
        if ((idints == null) || (idints.length == 0)) {
            throw new FilesException("ErrorMessages.messages.selection.null");
        }

        try {
            Users user = getUser(hsession, repositoryName);

            Query query = hsession.getNamedQuery("messages-by-attachments");
            query.setParameterList("idints", idints);
            query.setInteger("user", user.getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                message.setMesBox(this.folderTrash);

                hsession.update(message);
                hsession.flush();
            }

            hsession.flush();
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public void restoreFiles(Session hsession, String repositoryName,
        Integer[] idints) throws FilesException {
        if ((idints == null) || (idints.length == 0)) {
            throw new FilesException("ErrorMessages.messages.selection.null");
        }

        try {
            Users user = getUser(hsession, repositoryName);

            Query query = hsession.getNamedQuery("messages-by-attachments");
            query.setParameterList("idints", idints);
            query.setInteger("user", user.getUseIdint());

            ScrollableResults scroll = query.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                message.setMesBox(this.folderFiles);

                hsession.update(message);
                hsession.flush();
            }

            hsession.flush();
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public Vector getFiles(Session hsession, String repositoryName,
        String folderName, int label, int page, int messagesByPage, int order,
        String orderType) throws FilesException {
        Vector files = new Vector();

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

            Query hquery = null;

            String[] folderNameList = new String[0];

            try {
                folderName = parseFolder(folderName);

                folderNameList = new String[] { folderName };

                if (folderName.equals(this.folderAll) ||
                        folderName.equals(this.folderHidden)) {
                    folderNameList = new String[] {
                            this.folderAll, this.folderDraft, this.folderHidden,
                            this.folderImportant, this.folderInbox,
                            this.folderSent
                        };
                }
            } catch (Exception ex) {
            }

            if ((folderNameList.length == 0) && (label <= 0)) {
                hquery = hsession.getNamedQuery("attachments");
            } else if ((folderNameList.length > 0) && (label <= 0)) {
                hquery = hsession.getNamedQuery("attachments-by-folder");
            } else if ((folderNameList.length == 0) && (label > 0)) {
                hquery = hsession.getNamedQuery("attachments-by-label");
            } else if ((folderNameList.length > 0) && (label > 0)) {
                hquery = hsession.getNamedQuery("attachments-by-folder-label");
            }

            String aux = hquery.getQueryString();

            switch (order) {
            case ORDER_BY_SIZE:

                if (orderType.equals("ASC")) {
                    aux += " order by att_size asc";
                } else {
                    aux += " order by att_size desc";
                }

                break;

            case ORDER_BY_DATE:

                if (orderType.equals("ASC")) {
                    aux += " order by mes_date asc";
                } else {
                    aux += " order by mes_date desc";
                }

                break;

            case ORDER_BY_TYPE:

                if (orderType.equals("ASC")) {
                    aux += " order by att_content_type asc";
                } else {
                    aux += " order by att_content_type desc";
                }

                break;

            default:

                if (!orderType.equals("ASC")) {
                    aux += " order by att_name desc";
                } else {
                    aux += " order by att_name asc";
                }

                break;
            }

            SQLQuery h2query = hsession.createSQLQuery(aux);

            if ((folderNameList.length == 0) && (label <= 0)) {
                h2query.setParameterList("no_boxes",
                    new String[] {
                        this.folderTrash, this.folderChat, this.folderSpam,
                        FOLDER_DELETE
                    });
                h2query.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            } else if ((folderNameList.length > 0) && (label <= 0)) {
                h2query.setParameterList("boxes", folderNameList);
                h2query.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            } else if ((folderNameList.length == 0) && (label > 0)) {
                h2query.setInteger("label", label);
                h2query.setParameterList("no_boxes",
                    new String[] {
                        this.folderTrash, this.folderChat, this.folderSpam,
                        FOLDER_DELETE
                    });
                h2query.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            } else if ((folderNameList.length > 0) && (label > 0)) {
                h2query.setInteger("label", label);
                h2query.setParameterList("boxes", folderNameList);
                h2query.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            }

            h2query.setFirstResult(page * messagesByPage);
            h2query.setMaxResults(messagesByPage);

            h2query.addEntity("testo", AttachmentWithDate.class);

            ScrollableResults scroll = h2query.scroll();

            while (scroll.next()) {
                AttachmentWithDate attachment = (AttachmentWithDate) scroll.get(0);

                AttachmentObj obj = new AttachmentObj();
                obj.setContentType(attachment.getAttContentType());

                Date date = attachment.getAttDate();

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

                obj.setDate(date);
                obj.setIdint(attachment.getAttIdint());
                obj.setName(attachment.getAttName());
                obj.setPart(attachment.getAttPart());

                int size = attachment.getAttSize();
                size /= 1024;

                if (size > 1024) {
                    size /= 1024;
                    obj.setSize(size + " MB");
                } else {
                    obj.setSize(((size > 0) ? (size + "") : "<1") + " kB");
                }
                
                String extension = (String) this.extensions.get(attachment.getAttContentType());
                
                if (StringUtils.isBlank(extension)) {
                	extension = "generic";
                }

                obj.setExtension(extension);

                Message message = attachment.getMessage();

                if (message.isMesFlagged()) {
                    obj.setFlagged(true);
                } else {
                    obj.setFlagged(false);
                }

                if (message.getLabMeses() != null) {
                    Iterator it = message.getLabMeses().iterator();
                    StringBuffer lab = new StringBuffer();

                    while (it.hasNext()) {
                        if (lab.length() > 0) {
                            lab.append(", ");
                        }

                        LabMes labMes = (LabMes) it.next();
                        lab.append(labMes.getId().getLabel().getLabName());
                    }

                    if (lab.length() > 0) {
                        obj.setLabel(lab.toString());
                    } else {
                    }
                }

                obj.setBox(message.getMesBox());

                obj.setMid(message.getMesName());

                files.addElement(obj);
            }

            return files;
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    public int getCountFiles(Session hsession, String repositoryName,
        String folderName, int label) throws FilesException {
        try {
            Query hquery = null;

            String[] folderNameList = new String[0];

            try {
                folderName = parseFolder(folderName);

                folderNameList = new String[] { folderName };

                if (folderName.equals(this.folderAll) ||
                        folderName.equals(this.folderHidden)) {
                    folderNameList = new String[] {
                            this.folderAll, this.folderDraft, this.folderHidden,
                            this.folderImportant, this.folderInbox,
                            this.folderSent
                        };
                }
            } catch (Exception ex) {
            }

            if ((folderNameList.length == 0) && (label <= 0)) {
                hquery = hsession.getNamedQuery("count-attachments");
                hquery.setParameterList("no_boxes",
                    new String[] {
                        this.folderTrash, this.folderChat, this.folderSpam,
                        FOLDER_DELETE
                    });
                hquery.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            } else if ((folderNameList.length > 0) && (label <= 0)) {
                hquery = hsession.getNamedQuery("count-attachments-by-folder");
                hquery.setParameterList("boxes", folderNameList);
                hquery.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            } else if ((folderNameList.length == 0) && (label > 0)) {
                hquery = hsession.getNamedQuery("count-attachments-by-label");
                hquery.setInteger("label", label);
                hquery.setParameterList("no_boxes",
                    new String[] {
                        this.folderTrash, this.folderChat, this.folderSpam,
                        FOLDER_DELETE
                    });
                hquery.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            } else if ((folderNameList.length > 0) && (label > 0)) {
                hquery = hsession.getNamedQuery(
                        "count-attachments-by-folder-label");
                hquery.setInteger("label", label);
                hquery.setParameterList("boxes", folderNameList);
                hquery.setInteger("user",
                    getUser(hsession, repositoryName).getUseIdint());
            }

            return ((Integer) hquery.uniqueResult()).intValue();
        } catch (Exception e) {
            throw new FilesException(e);
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
     * @param hash DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FilesException DOCUMENT ME!
     */
    public MailPartObj getAttachment(Session hsession, String repositoryName,
        String mid, String hash) throws FilesException {
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
                        part.setAttachent(IOUtils.toByteArray(
                                aux.getPart().getInputStream()));
                        part.setContentType(aux.getContentType());
                        part.setId(aux.getId());
                        part.setName(aux.getName());
                        part.setSize(aux.getSize());

                        break;
                    }
                }
            } else {
                throw new FilesException("The Attachment is null");
            }

            if (part == null) {
                throw new FilesException("The Attachment is null");
            }

            return part;
        } catch (Exception ex) {
            throw new FilesException(ex);
        } catch (java.lang.OutOfMemoryError ex) {
            System.gc();
            throw new FilesException(ex);
        } catch (Throwable ex) {
            throw new FilesException(ex);
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
     * @throws FilesException DOCUMENT ME!
     */
    public Counters getInfoCounters(Session hsession, String repositoryName)
        throws FilesException {
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
     * @throws FilesException DOCUMENT ME!
     */
    private String getQuotaLayer(Session hsession, Users user)
        throws FilesException {
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
     * @param repositoryName DOCUMENT ME!
     * @param mids DOCUMENT ME!
     * @param folder DOCUMENT ME!
     *
     * @throws FilesException DOCUMENT ME!
     */
    public void moveFiles(Session hsession, String repositoryName,
        String[] mids, String folder) throws FilesException {
        if ((mids == null) || (mids.length == 0)) {
            throw new FilesException("ErrorMessages.messages.selection.null");
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
            throw new FilesException(ex);
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
     * @throws FilesException DOCUMENT ME!
     */
    private String parseFolder(String name) throws FilesException {
        if (name == null) {
            throw new FilesException("This Folder is null");
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
                name.equals(this.folderTrash) || name.equals(this.folderChat) ||
                name.equals(this.folderFiles)) {
            return name;
        } else {
            throw new FilesException("This Folder not exist: " + name);
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
     * @throws DFilesException DOCUMENT ME!
     */
    protected int getMaxQuotaSize(org.hibernate.Session hsession, Users user)
        throws FilesException {
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
     * @throws DFilesException DOCUMENT ME!
     */
    protected int getUsedQuotaSize(org.hibernate.Session hsession, Users user)
        throws FilesException {
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
}
