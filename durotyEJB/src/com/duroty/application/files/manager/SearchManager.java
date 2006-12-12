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

import java.io.File;
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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
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

import com.duroty.application.files.exceptions.FilesException;
import com.duroty.application.files.exceptions.SearchException;
import com.duroty.application.files.utils.AttachmentObj;
import com.duroty.application.files.utils.SearchObj;
import com.duroty.application.mail.utils.AdvancedObj;
import com.duroty.hibernate.Attachment;
import com.duroty.hibernate.LabMes;
import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.lucene.analysis.DictionaryAnalyzer;
import com.duroty.lucene.didyoumean.CompositeDidYouMeanParser;
import com.duroty.lucene.files.search.AdvancedQueryParser;
import com.duroty.lucene.files.search.SimpleQueryParser;
import com.duroty.lucene.filter.ChainedFilter;
import com.duroty.lucene.mail.LuceneMessage;
import com.duroty.lucene.mail.LuceneMessageConstants;
import com.duroty.lucene.mail.indexer.MailIndexer;
import com.duroty.utils.GeneralOperations;


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
    public static final int ORDER_BY_TYPE = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int ORDER_BY_SIZE = 3;

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

    /** DOCUMENT ME! */
    private String folderFiles;

    /** DOCUMENT ME */

    //private String folderChat;

    /** DOCUMENT ME */

    //private String folderImportant;

    /** DOCUMENT ME */
    private String folderHidden;
    
    /**
     * DOCUMENT ME!
     */
    private HashMap extensions;

    /**
     * @throws NamingException 
     *
     */
    public SearchManager(HashMap mail)
        throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException, NamingException {
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
        this.folderFiles = (String) mail.get(Constants.MAIL_FOLDER_FILES);

        //this.folderChat = (String) mail.get(Constants.MAIL_FOLDER_CHAT);
        
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
     * @param token DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FilesException DOCUMENT ME!
     */
    public SearchObj simple(Session hsession, String repositoryName,
        String token, String folderName, int label, int page, int messagesByPage, int order,
        String orderType) throws FilesException {
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
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", locale);
            SimpleDateFormat formatter3 = new SimpleDateFormat("MM/yy", locale);

            searcher = MailIndexer.getSearcher(lucenePathMessages);

            Query query = null;
            
            if (!StringUtils.isBlank(folderName) && folderName.equals(this.folderFiles)) {
            	BooleanQuery booleanQuery = new BooleanQuery();
            	
            	QueryParser parser = new QueryParser(Field_subject, analyzer);
                parser.setDefaultOperator(Operator.AND);
                
                Query aux1 = parser.parse("Files-System");
                booleanQuery.add(aux1, BooleanClause.Occur.MUST);
                
                Query aux2 = SimpleQueryParser.parse(token, analyzer);                
                booleanQuery.add(aux2, BooleanClause.Occur.MUST);
                
                /*QueryParser parser2 = new QueryParser(Field_attachments, analyzer);
                parser2.setDefaultOperator(Operator.AND); 
                Query aux3 = parser2.parse("Message Text");
                booleanQuery.add(aux3, BooleanClause.Occur.MUST_NOT);*/
                
                query = booleanQuery;
            } else {
            	/*BooleanQuery booleanQuery = new BooleanQuery();
            	
            	QueryParser parser = new QueryParser(Field_attachments, analyzer);
                parser.setDefaultOperator(Operator.AND);                
            	
            	Query aux1 = parser.parse("Message Text");
                booleanQuery.add(aux1, BooleanClause.Occur.MUST_NOT);
            	
            	Query aux2 = SimpleQueryParser.parse(token, analyzer);
            	booleanQuery.add(aux2, BooleanClause.Occur.MUST);
            	
            	query = booleanQuery;*/
            	
            	query = SimpleQueryParser.parse(token, analyzer);
            }

            Hits hits = null;

            Date searchStart = new Date();

            boolean reverse = true;

            if (orderType.equals("ASC")) {
                reverse = false;
            } else {
                reverse = true;
            }

            switch (order) {
            case ORDER_BY_SIZE:
                sort = new Sort(new SortField[] {
                            new SortField(Field_size, SortField.STRING, reverse),
                            SortField.FIELD_SCORE
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
                            new SortField(null, SortField.SCORE, !reverse)
                        });

                break;
            }
            
            ChainedFilter chained = null;

            if (label > 0) {
                chained = getChainedFilter(hsession, repositoryName, label);
            }

            if (chained != null) {
                hits = searcher.search(query, chained, sort);
            } else {
                hits = searcher.search(query, sort);
            }

            Date searchEnd = new Date();

            //time in seconds
            double time = ((double) (searchEnd.getTime() - searchStart.getTime())) / (double) 1000;

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

            Vector files = new Vector();

            for (int j = start; j < end; j++) {
                Document doc = hits.doc(j);

                if (doc != null) {
                    LuceneMessage lmsg = new LuceneMessage(doc);

                    Message message = getMessage(hsession, repositoryName, lmsg.getIdint());

                    if (message != null) {
                    	Set set = message.getAttachments();
                    	if (set != null) {
                    		Iterator it = set.iterator();
                    		
                    		while (it.hasNext()) {
                    			Attachment attachment = (Attachment) it.next();
                    			
                    			String name = attachment.getAttName();
                    			if (!StringUtils.isBlank(name) && !name.equals("Message Text")) {                    			
	                    			AttachmentObj obj = new AttachmentObj();
	                                obj.setContentType(attachment.getAttContentType());
	                                
	                                obj.setScore((int) (hits.score(j) * 100));
	
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
	
	                                if (message.isMesFlagged()) {
	                                    obj.setFlagged(true);
	                                } else {
	                                    obj.setFlagged(false);
	                                }
	
	                                if (message.getLabMeses() != null) {
	                                    Iterator it2 = message.getLabMeses().iterator();
	                                    StringBuffer lab = new StringBuffer();
	
	                                    while (it2.hasNext()) {
	                                        if (lab.length() > 0) {
	                                            lab.append(", ");
	                                        }
	
	                                        LabMes labMes = (LabMes) it2.next();
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
                    		}
                    	}                        
                    }
                }
            }

            searchObj.setHits(hitsLength);
            searchObj.setTime(time);
            searchObj.setFiles(files);
        } catch (Exception ex) {
            throw new FilesException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }

        return searchObj;
    }
    
    public SearchObj advanced(Session hsession, String repositoryName,
    		AdvancedObj advancedObj, int page, int messagesByPage, int order,
            String orderType) throws FilesException {
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
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", locale);
                SimpleDateFormat formatter3 = new SimpleDateFormat("MM/yy", locale);

                searcher = MailIndexer.getSearcher(lucenePathMessages);
                
                if (!StringUtils.isBlank(advancedObj.getSubject())) {
                	advancedObj.setSubject("Files-System");
                }

                Query query = AdvancedQueryParser.parseMessages(advancedObj, analyzer);;
                
                /*BooleanQuery booleanQuery = new BooleanQuery();
            	
            	QueryParser parser = new QueryParser(Field_attachments, analyzer);
                parser.setDefaultOperator(Operator.AND);                
            	
            	Query aux1 = parser.parse("\"Message Text\"");
                booleanQuery.add(aux1, BooleanClause.Occur.MUST_NOT);
            	
            	Query aux2 = AdvancedQueryParser.parseMessages(advancedObj, analyzer);
            	booleanQuery.add(aux2, BooleanClause.Occur.MUST);
            	
            	query = booleanQuery;*/

                Hits hits = null;

                Date searchStart = new Date();

                boolean reverse = true;

                if (orderType.equals("ASC")) {
                    reverse = false;
                } else {
                    reverse = true;
                }

                switch (order) {
                case ORDER_BY_SIZE:
                    sort = new Sort(new SortField[] {
                                new SortField(Field_size, SortField.STRING, reverse),
                                SortField.FIELD_SCORE
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
                                new SortField(null, SortField.SCORE, !reverse)
                            });

                    break;
                }
                
                int label = 0;

                if (advancedObj.getLabel() != null) {
                    label = advancedObj.getLabel().getIdint();
                }
                
                ChainedFilter chained = null;

                if (label > 0) {
                    chained = getChainedFilter(hsession, repositoryName, label);
                }

                if (chained != null) {
                    hits = searcher.search(query, chained, sort);
                } else {
                    hits = searcher.search(query, sort);
                }

                Date searchEnd = new Date();

                //time in seconds
                double time = ((double) (searchEnd.getTime() - searchStart.getTime())) / (double) 1000;

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

                Vector files = new Vector();

                for (int j = start; j < end; j++) {
                    Document doc = hits.doc(j);

                    if (doc != null) {
                        LuceneMessage lmsg = new LuceneMessage(doc);

                        Message message = getMessage(hsession, repositoryName, lmsg.getIdint());

                        if (message != null) {
                        	Set set = message.getAttachments();
                        	if (set != null) {
                        		Iterator it = set.iterator();
                        		
                        		while (it.hasNext()) {
                        			Attachment attachment = (Attachment) it.next();
                        			
                        			String name = attachment.getAttName();
                        			if (!StringUtils.isBlank(name) && !name.equals("Message Text")) {
	                        			AttachmentObj obj = new AttachmentObj();
	                                    obj.setContentType(attachment.getAttContentType());
	                                    
	                                    obj.setScore((int) (hits.score(j) * 100));
	
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
	
	                                    if (message.isMesFlagged()) {
	                                        obj.setFlagged(true);
	                                    } else {
	                                        obj.setFlagged(false);
	                                    }
	
	                                    if (message.getLabMeses() != null) {
	                                        Iterator it2 = message.getLabMeses().iterator();
	                                        StringBuffer lab = new StringBuffer();
	
	                                        while (it2.hasNext()) {
	                                            if (lab.length() > 0) {
	                                                lab.append(", ");
	                                            }
	
	                                            LabMes labMes = (LabMes) it2.next();
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
                        		}
                        	}                        
                        }
                    }
                }

                searchObj.setHits(hitsLength);
                searchObj.setTime(time);
                searchObj.setFiles(files);
            } catch (Exception ex) {
                throw new FilesException(ex);
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
        throws FilesException {
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
            throw new FilesException(e);
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
     * @throws FilesException DOCUMENT ME!
     */
    private ChainedFilter getChainedFilter(Session hsession, String username, int label) throws Exception {
        ChainedFilter filter = null;

        try {
            Users user = getUser(hsession, username);

            org.hibernate.Query query1 = null;
            org.hibernate.Query query2 = null;
            
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
