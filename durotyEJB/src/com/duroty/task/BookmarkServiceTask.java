/**
 *
 */
package com.duroty.task;

import com.duroty.application.bookmark.utils.BookmarkObj;
import com.duroty.hibernate.Bookmark;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.lucene.utils.FileUtilities;

import com.duroty.service.Bookmarklet;
import com.duroty.service.Servible;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.NumberUtils;
import com.duroty.utils.log.DLog;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.jboss.varia.scheduler.Schedulable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * @author durot
 *
 */
public class BookmarkServiceTask implements Schedulable, Servible {
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
    private String hibernateSessionFactory;

    /**
     * @throws NamingException
     * @throws IOException 
     *
     */
    public BookmarkServiceTask(int poolSize) throws NamingException, IOException {
        super();

        this.poolSize = poolSize;

        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap bookmark = (HashMap) ctx.lookup((String) options.get(
                        Constants.BOOKMARK_CONFIG));
            this.hibernateSessionFactory = (String) bookmark.get(Constants.HIBERNATE_SESSION_FACTORY);
            
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
                "BookmarkServiceTask is running and wait.");

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

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Criteria crit = hsession.createCriteria(Bookmark.class);

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                if (pool.size() >= poolSize) {
                    break;
                }

                Bookmark bookmark = (Bookmark) scroll.get(0);

                Users user = bookmark.getUsers();

                String idint = NumberUtils.pad(Long.parseLong(String.valueOf(
                                bookmark.getBooIdint())));

                String key = idint + "--" + user.getUseUsername();

                if (!poolContains(key)) {
                    addPool(key);
                    
                    BookmarkObj bookmarkObj = new BookmarkObj();
                    bookmarkObj.setIdint(idint);
                    bookmarkObj.setUrl(bookmark.getBooUrl());

                    Bookmarklet bookmarklet = new Bookmarklet(this, idint,
                            user.getUseUsername(), bookmarkObj);
                    Thread thread = new Thread(bookmarklet, key);
                    thread.start();
                    
                    hsession.delete(bookmark);
                    hsession.flush();
                }

                Thread.sleep(100);
            }
        } catch (Exception e) {
        	pool.clear();
        	DLog.log(DLog.ERROR, this.getClass(), e.getMessage());
            System.gc();
        } catch (OutOfMemoryError e) {
        	pool.clear();
        	DLog.log(DLog.ERROR, this.getClass(), e.getMessage());
            System.gc();
        } catch (Throwable e) {
            pool.clear();
            DLog.log(DLog.ERROR, this.getClass(), e.getMessage());
        } finally {
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
        synchronized (BookmarkServiceTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (BookmarkServiceTask.class) {
            this.init = init;
        }
    }
}
