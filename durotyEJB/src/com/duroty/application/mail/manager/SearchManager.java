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
import com.duroty.application.mail.exceptions.SearchException;
import com.duroty.application.mail.utils.AdvancedObj;
import com.duroty.application.mail.utils.FilterObj;
import com.duroty.application.mail.utils.MessageObj;
import com.duroty.application.mail.utils.SearchObj;

import com.duroty.hibernate.Filter;
import com.duroty.hibernate.LabMes;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.analysis.DictionaryAnalyzer;
import com.duroty.lucene.didyoumean.CompositeDidYouMeanParser;
import com.duroty.lucene.filter.ChainedFilter;
import com.duroty.lucene.mail.LuceneMessage;
import com.duroty.lucene.mail.LuceneMessageConstants;
import com.duroty.lucene.mail.indexer.MailIndexer;
import com.duroty.lucene.mail.search.AdvancedQueryParser;
import com.duroty.lucene.mail.search.FilterQueryParser;
import com.duroty.lucene.mail.search.SimpleQueryParser;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.mail.MessageUtilities;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import org.hibernate.criterion.Restrictions;

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class SearchManager implements LuceneMessageConstants {
    /**
     * DOCUMENT ME!
     */
    private static final String FOLDER_DELETE = "DELETE";

    /**
        * DOCUMENT ME!
        */
    public static final int ORDER_BY_DEFAULT = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_DATE = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_SUBJECT = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_SIZE = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_ADDRESS = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_IMPORTANT = 5;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_UNREAD = 6;

    /**
    * DOCUMENT ME!
    */
    private Analyzer analyzer;

    /**
     * DOCUMENT ME!
     */
    private String defaultLucenePath;

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

    //private String folderChat;

    /** DOCUMENT ME */

    //private String folderImportant;

    /** DOCUMENT ME */
    private String folderHidden;

    /**
     *
     */
    public SearchManager(HashMap mail)
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

        this.folderAll = (String) mail.get(Constants.MAIL_FOLDER_ALL);
        this.folderInbox = (String) mail.get(Constants.MAIL_FOLDER_INBOX);
        this.folderSent = (String) mail.get(Constants.MAIL_FOLDER_SENT);
        this.folderTrash = (String) mail.get(Constants.MAIL_FOLDER_TRASH);
        this.folderBlog = (String) mail.get(Constants.MAIL_FOLDER_BLOG);
        this.folderDraft = (String) mail.get(Constants.MAIL_FOLDER_DRAFT);
        this.folderSpam = (String) mail.get(Constants.MAIL_FOLDER_SPAM);

        //this.folderImportant = (String) mail.get(Constants.MAIL_FOLDER_IMPORTANT);
        this.folderHidden = (String) mail.get(Constants.MAIL_FOLDER_HIDDEN);

        //this.folderChat = (String) mail.get(Constants.MAIL_FOLDER_CHAT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param token DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public SearchObj simple(Session hsession, String repositoryName,
        String token, int page, int messagesByPage, int order,
        String orderType, boolean excludeTrash) throws MailException {
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
        SearchObj searchObj = new SearchObj();
        Sort sort = null;

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

            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Query query = SimpleQueryParser.parse(token, analyzer);

            Hits hits = null;

            Date searchStart = new Date();

            boolean reverse = true;

            if (orderType.equals("ASC")) {
                reverse = false;
            } else {
                reverse = true;
            }

            switch (order) {
            case ORDER_BY_ADDRESS:
                sort = new Sort(new SortField[] {
                            new SortField(Field_from, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_SIZE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_size, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_SUBJECT:
                sort = new Sort(new SortField[] {
                            new SortField(Field_subject, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_DATE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_lastDate, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_UNREAD:
                sort = new Sort(new SortField[] {
                            new SortField(Field_recent, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_IMPORTANT:
                sort = new Sort(new SortField[] {
                            new SortField(Field_flagged, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            default:
                sort = new Sort(new SortField[] {
                            SortField.FIELD_SCORE,
                            new SortField(Field_lastDate, SortField.STRING, true)
                        });

                break;
            }

            ChainedFilter chained = null;

            if (excludeTrash) {
                chained = getChainedFilter(hsession, repositoryName, 0, null);
            }

            if (chained != null) {
                hits = searcher.search(query, chained, sort);
            } else {
                hits = searcher.search(query, sort);
            }

            Date searchEnd = new Date();

            //time in seconds
            double time = ((double) (searchEnd.getTime() -
                searchStart.getTime())) / (double) 1000;

            int hitsLength = hits.length();

            if (hitsLength <= 0) {
                return null;
            }

            int start = page * messagesByPage;
            int end = start + messagesByPage;

            if (end > 0) {
                end = Math.min(hitsLength, end);
            } else {
                end = hitsLength;
            }

            if (start > end) {
                throw new SearchException("Search index of bound. start > end");
            }

            Vector messages = new Vector();

            for (int j = start; j < end; j++) {
                Document doc = hits.doc(j);

                if (doc != null) {
                    LuceneMessage lmsg = new LuceneMessage(doc);

                    Message message = getMessage(hsession, repositoryName,
                            lmsg.getIdint());

                    if (message != null) {
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
                            obj.setSize(((size > 0) ? (size + "") : "<1") +
                                " kB");
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
                            Calendar calendar2 = Calendar.getInstance(timeZone,
                                    locale);
                            calendar2.setTime(date);

                            if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                        Calendar.YEAR)) &&
                                    (calendar.get(Calendar.MONTH) == calendar2.get(
                                        Calendar.MONTH)) &&
                                    (calendar.get(Calendar.DATE) == calendar2.get(
                                        Calendar.DATE))) {
                                obj.setDateStr(formatter2.format(
                                        calendar2.getTime()));
                            } else if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                        Calendar.YEAR)) &&
                                    (calendar.get(Calendar.MONTH) == calendar2.get(
                                        Calendar.MONTH))) {
                                obj.setDateStr(formatter1.format(
                                        calendar2.getTime()));
                            } else {
                                obj.setDateStr(formatter3.format(
                                        calendar2.getTime()));
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
                                buff.append(labMes.getId().getLabel()
                                                  .getLabName());
                            }

                            obj.setLabel(buff.toString());
                        }

                        try {
                            obj.setSubject(message.getMesSubject());
                        } catch (Exception ex) {
                            obj.setSubject("no subject");
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

                        if (MessageUtilities.isHighPriority(
                                    message.getMesHeaders())) {
                            priority = "high";
                        } else if (MessageUtilities.isLowPriority(
                                    message.getMesHeaders())) {
                            priority = "low";
                        }

                        obj.setPriority(priority);

                        messages.addElement(obj);
                    }
                }
            }

            searchObj.setHits(hitsLength);
            searchObj.setTime(time);
            searchObj.setMessages(messages);
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }

        return searchObj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param advancedObj DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public SearchObj advanced(Session hsession, String repositoryName,
        AdvancedObj advancedObj, int page, int messagesByPage, int order,
        String orderType) throws MailException {
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
        SearchObj searchObj = new SearchObj();
        Sort sort = null;

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

            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Query query = AdvancedQueryParser.parseMessages(advancedObj,
                    analyzer);

            Hits hits = null;

            Date searchStart = new Date();

            boolean reverse = true;

            if (orderType.equals("ASC")) {
                reverse = false;
            } else {
                reverse = true;
            }

            switch (order) {
            case ORDER_BY_ADDRESS:
                sort = new Sort(new SortField[] {
                            new SortField(Field_from, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_SIZE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_size, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_SUBJECT:
                sort = new Sort(new SortField[] {
                            new SortField(Field_subject, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_DATE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_lastDate, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_UNREAD:
                sort = new Sort(new SortField[] {
                            new SortField(Field_recent, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_IMPORTANT:
                sort = new Sort(new SortField[] {
                            new SortField(Field_flagged, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            default:
                sort = new Sort(new SortField[] {
                            SortField.FIELD_SCORE,
                            new SortField(Field_lastDate, SortField.STRING, true)
                        });

                break;
            }

            int label = 0;

            if (advancedObj.getLabel() != null) {
                label = advancedObj.getLabel().getIdint();
            }

            String box = advancedObj.getBox();

            if ((box == null) || box.equals("0")) {
                box = this.folderAll;
            }

            ChainedFilter chained = null;

            if ((label > 0) || (box != null)) {
                chained = getChainedFilter(hsession, repositoryName, label, box);

                if (chained == null) {
                    return null;
                }
            }

            if ((query == null) && (chained == null)) {
                return null;
            }

            if (query == null) {
                query = new MatchAllDocsQuery();
            } else {
            }

            if (chained != null) {
                hits = searcher.search(query, chained, sort);
            } else {
                hits = searcher.search(query, sort);
            }

            Date searchEnd = new Date();

            //time in seconds
            double time = ((double) (searchEnd.getTime() -
                searchStart.getTime())) / (double) 1000;

            int hitsLength = hits.length();

            if (hitsLength <= 0) {
                return null;
            }

            int start = page * messagesByPage;
            int end = start + messagesByPage;

            if (end > 0) {
                end = Math.min(hitsLength, end);
            } else {
                end = hitsLength;
            }

            if (start > end) {
                throw new SearchException("Search index of bound. start > end");
            }

            Vector messages = new Vector();

            for (int j = start; j < end; j++) {
                Document doc = hits.doc(j);

                if (doc != null) {
                    LuceneMessage lmsg = new LuceneMessage(doc);

                    Message message = getMessage(hsession, repositoryName,
                            lmsg.getIdint());

                    if (message != null) {
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
                            obj.setSize(((size > 0) ? (size + "") : "<1") +
                                " kB");
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
                            Calendar calendar2 = Calendar.getInstance(timeZone,
                                    locale);
                            calendar2.setTime(date);

                            if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                        Calendar.YEAR)) &&
                                    (calendar.get(Calendar.MONTH) == calendar2.get(
                                        Calendar.MONTH)) &&
                                    (calendar.get(Calendar.DATE) == calendar2.get(
                                        Calendar.DATE))) {
                                obj.setDateStr(formatter2.format(
                                        calendar2.getTime()));
                            } else if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                        Calendar.YEAR)) &&
                                    (calendar.get(Calendar.MONTH) == calendar2.get(
                                        Calendar.MONTH))) {
                                obj.setDateStr(formatter1.format(
                                        calendar2.getTime()));
                            } else {
                                obj.setDateStr(formatter3.format(
                                        calendar2.getTime()));
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
                                buff.append(labMes.getId().getLabel()
                                                  .getLabName());
                            }

                            obj.setLabel(buff.toString());
                        }

                        try {
                            obj.setSubject(message.getMesSubject());
                        } catch (Exception ex) {
                            obj.setSubject("no subject");
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

                        if (MessageUtilities.isHighPriority(
                                    message.getMesHeaders())) {
                            priority = "high";
                        } else if (MessageUtilities.isLowPriority(
                                    message.getMesHeaders())) {
                            priority = "low";
                        }

                        obj.setPriority(priority);

                        messages.addElement(obj);
                    }
                }
            }

            searchObj.setHits(hitsLength);
            searchObj.setTime(time);
            searchObj.setMessages(messages);
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }

        return searchObj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param filterObj DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public SearchObj testFilter(Session hsession, String repositoryName,
        FilterObj filter, int page, int messagesByPage, int order,
        String orderType) throws MailException {
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
        SearchObj searchObj = new SearchObj();
        Sort sort = null;

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

            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Filter hfilter = new Filter();

            hfilter.setFilArchive(filter.isArchive());
            hfilter.setFilDoesntHaveWords(filter.getDoesntHaveWords());
            hfilter.setFilForwardTo(filter.getForward());
            hfilter.setFilFrom(filter.getFrom());
            hfilter.setFilHasAttacment(filter.isHasAttachment());
            hfilter.setFilHasWords(filter.getHasWords());
            hfilter.setFilImportant(filter.isImportant());
            hfilter.setFilSubject(filter.getSubject());
            hfilter.setFilTo(filter.getTo());
            hfilter.setFilTrash(filter.isTrash());
            hfilter.setFilOrOperator(!filter.isOperator());

            Query query = FilterQueryParser.parse(hfilter, analyzer);

            Hits hits = null;

            Date searchStart = new Date();

            boolean reverse = true;

            if (orderType.equals("ASC")) {
                reverse = false;
            } else {
                reverse = true;
            }

            switch (order) {
            case ORDER_BY_ADDRESS:
                sort = new Sort(new SortField[] {
                            new SortField(Field_from, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_SIZE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_size, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_SUBJECT:
                sort = new Sort(new SortField[] {
                            new SortField(Field_subject, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            case ORDER_BY_DATE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_lastDate, SortField.STRING,
                                reverse), SortField.FIELD_SCORE
                        });

                break;

            default:
                sort = new Sort(new SortField[] {
                            SortField.FIELD_SCORE,
                            new SortField(Field_lastDate, SortField.STRING, true)
                        });

                break;
            }

            hits = searcher.search(query, sort);

            Date searchEnd = new Date();

            //time in seconds
            double time = ((double) (searchEnd.getTime() -
                searchStart.getTime())) / (double) 1000;

            int hitsLength = hits.length();

            if (hitsLength <= 0) {
                return null;
            }

            int start = page * messagesByPage;
            int end = start + messagesByPage;

            if (end > 0) {
                end = Math.min(hitsLength, end);
            } else {
                end = hitsLength;
            }

            if (start > end) {
                throw new SearchException("Search index of bound. start > end");
            }

            Vector messages = new Vector();

            for (int j = start; j < end; j++) {
                Document doc = hits.doc(j);

                if (doc != null) {
                    LuceneMessage lmsg = new LuceneMessage(doc);

                    Message message = getMessage(hsession, repositoryName,
                            lmsg.getIdint());

                    if (message != null) {
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
                            obj.setSize(((size > 0) ? (size + "") : "<1") +
                                " kB");
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
                            Calendar calendar2 = Calendar.getInstance(timeZone,
                                    locale);
                            calendar2.setTime(date);

                            if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                        Calendar.YEAR)) &&
                                    (calendar.get(Calendar.MONTH) == calendar2.get(
                                        Calendar.MONTH)) &&
                                    (calendar.get(Calendar.DATE) == calendar2.get(
                                        Calendar.DATE))) {
                                obj.setDateStr(formatter2.format(
                                        calendar2.getTime()));
                            } else if ((calendar.get(Calendar.YEAR) == calendar2.get(
                                        Calendar.YEAR)) &&
                                    (calendar.get(Calendar.MONTH) == calendar2.get(
                                        Calendar.MONTH))) {
                                obj.setDateStr(formatter1.format(
                                        calendar2.getTime()));
                            } else {
                                obj.setDateStr(formatter3.format(
                                        calendar2.getTime()));
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
                                buff.append(labMes.getId().getLabel()
                                                  .getLabName());
                            }

                            obj.setLabel(buff.toString());
                        }

                        try {
                            obj.setSubject(message.getMesSubject());
                        } catch (Exception ex) {
                            obj.setSubject("no subject");
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

                        if (MessageUtilities.isHighPriority(
                                    message.getMesHeaders())) {
                            priority = "high";
                        } else if (MessageUtilities.isLowPriority(
                                    message.getMesHeaders())) {
                            priority = "low";
                        }

                        obj.setPriority(priority);

                        messages.addElement(obj);
                    }
                }
            }

            searchObj.setHits(hitsLength);
            searchObj.setTime(time);
            searchObj.setMessages(messages);
        } catch (Exception ex) {
            throw new MailException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }

        return searchObj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     * @param queryString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SearchException DOCUMENT ME!
     */
    public String didYouMean(String username, String token)
        throws MailException {
        String luceneSpellPath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            luceneSpellPath = defaultLucenePath + File.separator + username +
                File.separator + Constants.MAIL_LUCENE_SPELL;
        } else {
            luceneSpellPath = defaultLucenePath + username + File.separator +
                Constants.MAIL_LUCENE_SPELL;
        }

        try {
            Directory aux = FSDirectory.getDirectory(luceneSpellPath, false);
            CompositeDidYouMeanParser c = new CompositeDidYouMeanParser("",
                    aux, new DictionaryAnalyzer());
            Query qAux = c.suggest(token, Operator.OR);

            if (qAux == null) {
                return null;
            }

            return qAux.toString();
        } catch (Exception e) {
            throw new MailException(e);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     * @param label DOCUMENT ME!
     * @param box DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DMailException DOCUMENT ME!
     */
    private ChainedFilter getChainedFilter(Session hsession, String username,
        int label, String box) throws Exception {
        ChainedFilter filter = null;

        try {
            Users user = getUser(hsession, username);

            org.hibernate.Query query1 = null;
            org.hibernate.Query query2 = null;

            String[] boxes = null;

            if ((box != null) && box.equals("ALL")) {
                boxes = new String[] {
                        this.folderInbox, this.folderSent, this.folderDraft,
                        this.folderHidden, this.folderBlog
                    };
            } else if (box != null) {
                boxes = new String[] { box };
            } else {
                boxes = new String[] {
                        this.folderInbox, this.folderSent, this.folderDraft,
                        this.folderHidden, this.folderBlog
                    };
            }

            if ((boxes == null) && (label == 0)) {
                return null;
            } else if ((boxes != null) && (label == 0)) {
                query1 = hsession.getNamedQuery("count-messages-by-folder");
                query1.setParameterList("folder", boxes);
                query1.setInteger("user", user.getUseIdint());

                query2 = hsession.getNamedQuery("messages-by-folder");
                query2.setParameterList("folder", boxes);
                query2.setInteger("user", user.getUseIdint());
            } else if ((boxes == null) && (label > 0)) {
                query1 = hsession.getNamedQuery("count-messages-by-label");
                query1.setInteger("label", label);
                query1.setInteger("user", user.getUseIdint());
                query1.setString("folderTrash", this.folderTrash);
                query1.setString("folderSpam", this.folderSpam);
                query1.setString("folderDelete", FOLDER_DELETE);

                query2 = hsession.getNamedQuery("messages-by-label");
                query2.setInteger("label", label);
                query2.setInteger("user", user.getUseIdint());
                query2.setString("folderTrash", this.folderTrash);
                query2.setString("folderSpam", this.folderSpam);
                query2.setString("folderDelete", FOLDER_DELETE);
            } else {
                query1 = hsession.getNamedQuery(
                        "count-messages-by-folder-label");
                query1.setInteger("label", label);
                query1.setParameterList("folder", boxes);
                query1.setInteger("user", user.getUseIdint());
                query1.setString("folderTrash", this.folderTrash);
                query1.setString("folderSpam", this.folderSpam);
                query1.setString("folderDelete", FOLDER_DELETE);

                query2 = hsession.getNamedQuery("messages-by-folder-label");
                query2.setInteger("label", label);
                query2.setParameterList("folder", boxes);
                query2.setInteger("user", user.getUseIdint());
                query2.setString("folderTrash", this.folderTrash);
                query2.setString("folderSpam", this.folderSpam);
                query2.setString("folderDelete", FOLDER_DELETE);
            }

            Integer count = (Integer) query1.uniqueResult();

            if (count.intValue() > 0) {
                QueryFilter[] qFilters = new QueryFilter[count.intValue()];

                ScrollableResults scroll = query2.scroll();

                int i = 0;

                while (scroll.next()) {
                    Message message = (Message) scroll.get(0);
                    qFilters[i] = new QueryFilter(new TermQuery(
                                new Term(LuceneMessageConstants.Field_idint,
                                    message.getMesName())));
                    i++;
                }

                filter = new ChainedFilter(qFilters);
            } else {
                QueryFilter[] qFilters = new QueryFilter[0];
                filter = new ChainedFilter(qFilters);
            }

            return filter;
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param messageObj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private Message getMessage(Session hsession, String repositoryName,
        String messageName) throws Exception {
        try {
            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.eq("mesName", messageName));
            crit.add(Restrictions.eq("users", getUser(hsession, repositoryName)));

            Message message = (Message) crit.uniqueResult();

            if (message == null) {
                String lucenePathMessages = "";

                if (!defaultLucenePath.endsWith(File.separator)) {
                    lucenePathMessages = defaultLucenePath + File.separator +
                        repositoryName + File.separator +
                        Constants.MAIL_LUCENE_MESSAGES;
                } else {
                    lucenePathMessages = defaultLucenePath + repositoryName +
                        File.separator + Constants.MAIL_LUCENE_MESSAGES;
                }

                MailIndexer indexer = new MailIndexer();
                indexer.deleteDocument(lucenePathMessages, Field_idint,
                    messageName);
            }

            return message;
        } finally {
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
}
