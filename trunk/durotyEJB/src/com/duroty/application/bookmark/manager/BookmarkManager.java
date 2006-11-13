/**
 *
 */
package com.duroty.application.bookmark.manager;

import com.duroty.application.bookmark.exceptions.BookmarkException;
import com.duroty.application.bookmark.exceptions.SearchException;
import com.duroty.application.bookmark.utils.BookmarkObj;
import com.duroty.application.bookmark.utils.PreferencesObj;
import com.duroty.application.bookmark.utils.SearchObj;
import com.duroty.application.mail.exceptions.MailException;

import com.duroty.hibernate.Bookmark;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.analysis.DictionaryAnalyzer;
import com.duroty.lucene.analysis.KeywordAnalyzer;
import com.duroty.lucene.bookmark.LuceneBookmark;
import com.duroty.lucene.bookmark.LuceneBookmarkContants;
import com.duroty.lucene.bookmark.indexer.BookmarkIndexer;
import com.duroty.lucene.bookmark.search.SimpleQueryParser;
import com.duroty.lucene.didyoumean.CompositeDidYouMeanParser;

import com.duroty.service.Bookmarklet;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.NumberUtils;

import org.apache.commons.lang.StringUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.hibernate.Criteria;
import org.hibernate.Session;

import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import java.net.URL;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


/**
 * @author durot
 *
 */
public class BookmarkManager implements LuceneBookmarkContants {
    /**
    * DOCUMENT ME!
    */
    private Analyzer analyzer;

    /**
    * DOCUMENT ME!
    */
    private String defaultLucenePath;

    /**
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     *
     */
    public BookmarkManager(HashMap bookmark)
        throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException {
        super();

        String luceneAnalyzer = (String) bookmark.get(Constants.BOOKMARK_LUCENE_ANALYZER);

        if ((luceneAnalyzer != null) && !luceneAnalyzer.trim().equals("")) {
            Class clazz = null;
            clazz = Class.forName(luceneAnalyzer.trim());
            this.analyzer = (Analyzer) clazz.newInstance();
        }

        this.defaultLucenePath = (String) bookmark.get(Constants.BOOKMARK_LUCENE_PATH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param bookmarkObj DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     */
    public void addBookmark(Session hsession, String repositoryName,
        BookmarkObj bookmarkObj) throws BookmarkException {
        //Cal insertar un bookmark a la base de dades i l'optimazer ja s'encarrega d'indexar
        try {
            Date now = new Date();
            String idint = NumberUtils.pad(now.getTime());

            Bookmarklet bookmarklet = new Bookmarklet(null, idint,
                    repositoryName, bookmarkObj);
            Thread thread = new Thread(bookmarklet, "bookmarklet-" + idint);
            thread.start();

            /*Bookmark bookmark = new Bookmark();
            bookmark.setBooDescription(bookmarkObj.getComments());
            bookmark.setBooKeywords(bookmarkObj.getKeywords());
            bookmark.setBooTitle(bookmarkObj.getTitle());
            bookmark.setBooUrl(bookmarkObj.getUrl());
            bookmark.setUsers(getUser(hsession, repositoryName));

            hsession.save(bookmark);
            hsession.flush();*/
        } catch (Exception e) {
            throw new BookmarkException(e);
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
     * @throws BookmarkException DOCUMENT ME!
     */
    public BookmarkObj getBookmark(Session hsession, String repositoryName,
        String idint) throws BookmarkException {
        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        try {
            BookmarkIndexer indexer = new BookmarkIndexer();
            Document doc = indexer.selectMessage(lucenePath, Field_idint, idint);

            if (doc != null) {
                LuceneBookmark luceneBookmark = new LuceneBookmark(doc);
                BookmarkObj bookmarkObj = new BookmarkObj();
                bookmarkObj.setIdint(luceneBookmark.getIdint());
                bookmarkObj.setUrl(luceneBookmark.getUrl());
                bookmarkObj.setTitle(luceneBookmark.getTitle());
                bookmarkObj.setComments(luceneBookmark.getComments());
                bookmarkObj.setFlagged(luceneBookmark.isFlagged());
                bookmarkObj.setKeywords(luceneBookmark.getKeywords());

                return bookmarkObj;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new BookmarkException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param bookmarkObj DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     */
    public void updateBookmark(Session hsession, String repositoryName,
        BookmarkObj bookmarkObj) throws BookmarkException {
        String idint = NumberUtils.pad(Long.parseLong(bookmarkObj.getIdint()));

        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        try {
            BookmarkIndexer indexer = new BookmarkIndexer();
            Document doc = indexer.selectMessage(lucenePath, Field_idint, idint);

            LuceneBookmark luceneBookmark = new LuceneBookmark(doc);
            luceneBookmark.setIdint(idint);
            luceneBookmark.setComments(bookmarkObj.getComments());
            if (!StringUtils.isBlank(bookmarkObj.getComments())) {
            	luceneBookmark.setNotebook(true);
            } else {
            	luceneBookmark.setNotebook(false);
            }
            luceneBookmark.setUrl(bookmarkObj.getUrl());
            luceneBookmark.setUrlStr(bookmarkObj.getUrl());
            luceneBookmark.setTitle(bookmarkObj.getTitle());
            luceneBookmark.setKeywords(bookmarkObj.getKeywords());

            if (doc != null) {
                indexer.updateDocument(lucenePath, Field_idint, idint,
                    luceneBookmark.getDoc(), analyzer);
            }
        } catch (Exception e) {
            throw new BookmarkException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param links DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     */
    public void addBookmarks(Session hsession, String repositoryName,
        Vector links) throws BookmarkException {
        //Cal insertar un bookmark a la base de dades i l'optimazer ja s'encarrega d'indexar
        if (links == null) {
            return;
        }

        try {
            Users user = getUser(hsession, repositoryName);

            for (int i = 0; i < links.size(); i++) {
                Bookmark bookmark = new Bookmark();
                bookmark.setBooUrl(((URL) links.get(i)).toString());
                bookmark.setUsers(user);

                hsession.save(bookmark);
                hsession.flush();
            }
        } catch (Exception e) {
            throw new BookmarkException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     */
    public void deleteBookmark(String repositoryName, String idint)
        throws BookmarkException {
        idint = NumberUtils.pad(Long.parseLong(idint));

        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        try {
            BookmarkIndexer indexer = new BookmarkIndexer();
            indexer.deleteDocument(lucenePath, Field_idint, idint);
        } catch (Exception e) {
            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     */
    public void flagBookmark(String repositoryName, String idint)
        throws BookmarkException {
        idint = NumberUtils.pad(Long.parseLong(idint));

        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        try {
            BookmarkIndexer indexer = new BookmarkIndexer();

            Document doc = indexer.selectMessage(lucenePath, Field_idint, idint);

            if (doc != null) {
                LuceneBookmark luceneBookmark = new LuceneBookmark(doc);
                luceneBookmark.setFlagged(true);

                indexer.updateDocument(lucenePath, Field_idint, idint,
                    luceneBookmark.getDoc(), analyzer);
            }
        } catch (Exception e) {
            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param repositoryName DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     */
    public void unflagBookmark(String repositoryName, String idint)
        throws BookmarkException {
        idint = NumberUtils.pad(Long.parseLong(idint));

        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        try {
            BookmarkIndexer indexer = new BookmarkIndexer();

            Document doc = indexer.selectMessage(lucenePath, Field_idint, idint);

            if (doc != null) {
                LuceneBookmark luceneBookmark = new LuceneBookmark(doc);
                luceneBookmark.setFlagged(false);

                indexer.updateDocument(lucenePath, Field_idint, idint,
                    luceneBookmark.getDoc(), analyzer);
            }
        } catch (Exception e) {
            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param repositoryName DOCUMENT ME!
     * @param token DOCUMENT ME!
     * @param page DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
     * @param order DOCUMENT ME!
     * @param orderType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws BookmarkException DOCUMENT ME!
     * @throws SearchException DOCUMENT ME!
     */
    public SearchObj search(String repositoryName, String token, int page,
        int messagesByPage, int order, String orderType, boolean isNotebook)
        throws BookmarkException {
        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        Searcher searcher = null;
        SearchObj searchObj = new SearchObj();
        Highlighter highlighter = null;

        try {
            searcher = BookmarkIndexer.getSearcher(lucenePath);

            Query query = null;
            Hits hits = null;

            if (StringUtils.isBlank(token)) {
                if (isNotebook) {
                    query = SimpleQueryParser.parse("notebook:true",
                            new KeywordAnalyzer());
                } else {
                    query = new MatchAllDocsQuery();
                }

                hits = searcher.search(query,
                        new Sort(new SortField[] {
                                SortField.FIELD_SCORE,
                                new SortField(Field_insert_date,
                                    SortField.STRING, true)
                            }));
            } else {
                query = SimpleQueryParser.parse(token, analyzer);

                StringBuffer buffer = new StringBuffer();

                if (isNotebook) {
                    buffer.append("(" + query.toString() + ") AND ");

                    QueryParser parser = new QueryParser(Field_notebook,
                            new KeywordAnalyzer());
                    parser.setDefaultOperator(Operator.AND);

                    Query aux = parser.parse(String.valueOf(true));

                    buffer.append("(" + aux.toString() + ") ");
                }

                if (buffer.length() > 0) {
                    QueryParser parser = new QueryParser("",
                            new WhitespaceAnalyzer());
                    query = parser.parse(buffer.toString());
                }

                hits = searcher.search(query);
            }

            Date searchStart = new Date();

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

            Vector bookmarks = new Vector();

            for (int j = start; j < end; j++) {
                Document doc = hits.doc(j);

                if (doc != null) {
                    LuceneBookmark luceneBookmark = new LuceneBookmark(doc);

                    SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b>",
                            "</b>");
                    highlighter = new Highlighter(formatter,
                            new QueryScorer(query));
                    highlighter.setTextFragmenter(new SimpleFragmenter(150));

                    BookmarkObj bookmarkObj = new BookmarkObj();
                    bookmarkObj.setCacheDate(luceneBookmark.getCacheDate());
                    bookmarkObj.setComments(luceneBookmark.getComments());
                    bookmarkObj.setContents(luceneBookmark.getCotents());
                    bookmarkObj.setDepth(luceneBookmark.getDepth());
                    bookmarkObj.setFlagged(luceneBookmark.isFlagged());
                    bookmarkObj.setIdint(luceneBookmark.getIdint());
                    bookmarkObj.setInsertDate(luceneBookmark.getInsertDate());
                    bookmarkObj.setKeywords(luceneBookmark.getKeywords());
                    bookmarkObj.setNotebook(luceneBookmark.isNotebook());
                    bookmarkObj.setParent(Long.parseLong(
                            luceneBookmark.getParent()));
                    bookmarkObj.setTitle(luceneBookmark.getTitle());
                    bookmarkObj.setTitleHighlight(luceneBookmark.getTitle());
                    bookmarkObj.setUrl(luceneBookmark.getUrl());

                    String contents = luceneBookmark.getCotents();
                    String hcontents = null;

                    if ((contents != null) && (!contents.trim().equals(""))) {
                        contents = contents.replaceAll("\\s+", " ");

                        TokenStream tokenStream = analyzer.tokenStream(Field_contents,
                                new StringReader(contents));
                        hcontents = highlighter.getBestFragment(tokenStream,
                                contents);

                        if (hcontents != null) {
                            contents = hcontents;
                        } else {
                            contents = null;
                        }
                    }

                    bookmarkObj.setContentsHighlight(contents);

                    String title = luceneBookmark.getTitle();
                    String htitle = null;

                    if ((title != null) && (!title.trim().equals(""))) {
                        title = title.replaceAll("\\s+", " ");

                        TokenStream tokenStream = analyzer.tokenStream(Field_title,
                                new StringReader(title));
                        htitle = highlighter.getBestFragment(tokenStream, title);

                        if (htitle != null) {
                            title = htitle;
                        }
                    }

                    bookmarkObj.setTitleHighlight(title);

                    bookmarks.addElement(bookmarkObj);
                }
            }

            searchObj.setHits(hitsLength);
            searchObj.setTime(time);
            searchObj.setBookmarks(bookmarks);
        } catch (Exception ex) {
            throw new SearchException(ex);
        } finally {
        }

        return searchObj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param repositoryName DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    public String didYouMean(String repositoryName, String token)
        throws MailException {
        String luceneSpellPath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            luceneSpellPath = defaultLucenePath + File.separator +
                repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_SPELL;
        } else {
            luceneSpellPath = defaultLucenePath + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_SPELL;
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
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SearchException DOCUMENT ME!
     * @throws BookmarkException DOCUMENT ME!
     */
    public Vector getKeywords(String repositoryName)
        throws SearchException, BookmarkException {
        Vector keywords = new Vector();

        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        Searcher searcher = null;

        try {
            searcher = BookmarkIndexer.getSearcher(lucenePath);

            Query query = new MatchAllDocsQuery();

            Hits hits = searcher.search(query);

            if (hits != null) {
                for (int i = 0; i < hits.length(); i++) {
                    Document doc = hits.doc(i);
                    String[] auxk = doc.getValues(Field_keywords);

                    if (auxk != null) {
                        for (int j = 0; j < auxk.length; j++) {
                            if ((auxk[j] != null) &&
                                    !auxk[j].trim().equals("") &&
                                    !keywords.contains(auxk[j])) {
                                keywords.addElement(auxk[j].toLowerCase());
                            }
                        }
                    }
                }
            }

            Collections.sort(keywords);
        } catch (IOException e) {
            throw new SearchException(e);
        } catch (Exception e) {
            throw new SearchException(e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                }
            }
        }

        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SearchException DOCUMENT ME!
     * @throws BookmarkException DOCUMENT ME!
     */
    public Vector getKeywordsNotebook(String repositoryName)
        throws SearchException, BookmarkException {
        Vector keywords = new Vector();

        String lucenePath = "";

        if (!defaultLucenePath.endsWith(File.separator)) {
            lucenePath = defaultLucenePath + File.separator + repositoryName +
                File.separator + Constants.BOOKMARK_LUCENE_BOOKMARK;
        } else {
            lucenePath = defaultLucenePath + repositoryName + File.separator +
                Constants.BOOKMARK_LUCENE_BOOKMARK;
        }

        Searcher searcher = null;

        try {
            searcher = BookmarkIndexer.getSearcher(lucenePath);

            Query query = SimpleQueryParser.parse("notebook:true",
                    new KeywordAnalyzer());

            Hits hits = searcher.search(query);

            if (hits != null) {
                for (int i = 0; i < hits.length(); i++) {
                    Document doc = hits.doc(i);
                    String[] auxk = doc.getValues(Field_keywords);

                    if (auxk != null) {
                        for (int j = 0; j < auxk.length; j++) {
                            if ((auxk[j] != null) &&
                                    !auxk[j].trim().equals("") &&
                                    !keywords.contains(auxk[j])) {
                                keywords.addElement(auxk[j].toLowerCase());
                            }
                        }
                    }
                }
            }

            Collections.sort(keywords);
        } catch (IOException e) {
            throw new SearchException(e);
        } catch (Exception e) {
            throw new SearchException(e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                }
            }
        }

        return keywords;
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
        throws BookmarkException {
        try {
            Users user = getUser(hsession, username);

            PreferencesObj obj = new PreferencesObj();
            obj.setLanguage(user.getUseLanguage());

            return obj;
        } catch (Exception ex) {
            throw new BookmarkException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }
}
