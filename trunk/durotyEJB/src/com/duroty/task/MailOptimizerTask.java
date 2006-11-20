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


package com.duroty.task;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.mail.LuceneMessageConstants;
import com.duroty.lucene.mail.indexer.MailIndexerConstants;
import com.duroty.lucene.utils.FileUtilities;

import com.duroty.service.MailOptimizerThread;
import com.duroty.service.Servible;

import com.duroty.utils.log.DLog;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class MailOptimizerTask implements Schedulable, LuceneMessageConstants,
    Servible, MailIndexerConstants {
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
    private Analyzer analyzer;

    /**
     * DOCUMENT ME!
     */
    private String defaultLucenePath;

    /**
     * DOCUMENT ME!
     */
    private String tempDir;

    /**
     * Creates a new MailOptimizerTask object.
     * @throws ClassNotFoundException
     * @throws NamingException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public MailOptimizerTask(int poolSize)
        throws ClassNotFoundException, NamingException, InstantiationException, 
            IllegalAccessException, IOException {
        super();

        this.poolSize = poolSize;

        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap mail = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));

            this.defaultLucenePath = (String) mail.get(Constants.MAIL_LUCENE_PATH);

            this.tempDir = System.getProperty("java.io.tmpdir");

            if (!this.tempDir.endsWith(File.separator)) {
                this.tempDir = this.tempDir + File.separator;
            }

            FileUtilities.deleteMotLocks(new File(this.tempDir));
            FileUtilities.deleteLuceneLocks(new File(this.tempDir));

            //cal borrar tot el que tenim al java.io.tmpdir que presenta lucene o altres temes
            String clazzAnalyzerName = (String) mail.get(Constants.MAIL_LUCENE_ANALYZER);

            if ((clazzAnalyzerName != null) &&
                    !clazzAnalyzerName.trim().equals("")) {
                Class clazz = null;
                clazz = Class.forName(clazzAnalyzerName.trim());
                this.analyzer = (Analyzer) clazz.newInstance();
            } else {
                this.analyzer = new StandardAnalyzer();
            }
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     */
    public void perform(Date arg0, long arg1) {
        if (isInit()) {
            DLog.log(DLog.DEBUG, this.getClass(),
                "MailOptimizerTask is running and wait.");

            return;
        }

        File dirLucene = new File(defaultLucenePath);

        File[] dirUsers = dirLucene.listFiles();

        if (dirUsers != null) {
            try {
                setInit(true);

                flush(dirUsers);
            } catch (Exception e) {
                DLog.log(DLog.ERROR, this.getClass(), e);
                pool.clear();
            } finally {
                setInit(false);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dirUsers DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void flush(File[] dirUsers) throws Exception {
        for (int i = 0; i < dirUsers.length; i++) {
            if (pool.size() >= this.poolSize) {
                break;
            }

            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }

            DLog.log(DLog.DEBUG, this.getClass(),
                "MailOptimizerTask:" + pool.size() + "/" + this.poolSize);

            File dirUser = dirUsers[i];

            String userPath = dirUser.getPath();
            String simplePath = userPath + File.separator + MESSAGES +
                File.separator + SIMPLE_PATH_NAME;
            String key = userPath + File.separator + MESSAGES + File.separator +
                OPTIMIZED_PATH_NAME;

            File lock = new File(this.tempDir +
                    userPath.substring(userPath.lastIndexOf(File.separator) +
                        1, userPath.length()) + "mot.lock");

            if (!poolContains(key) && !lock.exists()) {
                lock.createNewFile();

                addPool(key);

                if (!userPath.endsWith(File.separator)) {
                    userPath = userPath + File.separator;
                }

                MailOptimizerThread mot = new MailOptimizerThread(this,
                        lock.getPath(), userPath, simplePath, key, analyzer);
                Thread thread = new Thread(mot);
                thread.start();
            }
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
        synchronized (MailOptimizerTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (MailOptimizerTask.class) {
            this.init = init;
        }
    }
}
