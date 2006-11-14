/**
 *
 */
package com.duroty.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.duroty.application.bookmark.utils.BookmarkObj;
import com.duroty.hibernate.Users;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.lucene.bookmark.BookmarkToLuceneBookmark;
import com.duroty.lucene.bookmark.LuceneBookmark;
import com.duroty.lucene.bookmark.indexer.BookmarkIndexer;
import com.duroty.lucene.bookmark.indexer.BookmarkIndexerConstants;


/**
 * @author durot
 *
 */
public class Bookmarklet implements Runnable, BookmarkIndexerConstants {
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
    private Analyzer analyzer = null;

    /**
     * DOCUMENT ME!
     */
    private String idint = null;

    /**
     * DOCUMENT ME!
     */
    private String repositoryName = null;

    /**
     * DOCUMENT ME!
     */
    private String lucenePath = null;

    /**
     * DOCUMENT ME!
     */
    private BookmarkObj bookmarkObj = null;

    /**
     * Creates a new Mailet object.
     *
     * @param servible DOCUMENT ME!
     * @param messageName DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param mime DOCUMENT ME!
     */
    public Bookmarklet(Servible servible, String idint, String repositoryName,
        BookmarkObj bookmarkObj) {
        super();
        this.servible = servible;
        this.idint = idint;
        this.repositoryName = repositoryName;
        this.bookmarkObj = bookmarkObj;

        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap bookmark = (HashMap) ctx.lookup((String) options.get(
                        Constants.BOOKMARK_CONFIG));

            this.lucenePath = (String) bookmark.get(Constants.MAIL_LUCENE_PATH);

            String clazzAnalyzerName = (String) bookmark.get(Constants.MAIL_LUCENE_ANALYZER);

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
            System.gc();
        } finally {
            if (servible != null) {
                servible.removePool(idint + "--" + repositoryName);
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
        try {
            BookmarkToLuceneBookmark bookmarkToLuceneBookmark = BookmarkToLuceneBookmark.getInstance();
            LuceneBookmark luceneBookmark = bookmarkToLuceneBookmark.parse(idint,
                    bookmarkObj);

            String userLucenePathMessages = null;

            if (!lucenePath.endsWith(File.separator)) {
                userLucenePathMessages = lucenePath + File.separator +
                    repositoryName + File.separator + BOOKMARKS;
            } else {
                userLucenePathMessages = lucenePath + repositoryName +
                    File.separator + BOOKMARKS;
            }

            BookmarkIndexer indexer = new BookmarkIndexer();
            indexer.insertDocument(userLucenePathMessages, idint,
                luceneBookmark.getDoc(), analyzer);
        } finally {
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
}
