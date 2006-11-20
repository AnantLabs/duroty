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
package com.duroty.task;

import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.utils.FileUtilities;

import com.duroty.service.Messageable;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.jboss.varia.scheduler.Schedulable;

import java.io.File;
import java.io.IOException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * @author durot
 *
 */
public class PurgeTrashAndSpam implements Schedulable {
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
    private Messageable messageable;

    /**
     * DOCUMENT ME!
     */
    private String hibernateSessionFactory;

    /**
     * DOCUMENT ME!
     */
    private String folderSpam;

    /**
     * DOCUMENT ME!
     */
    private String folderTrash;

    /**
     * @throws NamingException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     *
     */
    public PurgeTrashAndSpam()
        throws NamingException, ClassNotFoundException, InstantiationException, 
            IllegalAccessException, IOException {
        super();

        Map options = ApplicationConstants.options;

        ctx = new InitialContext();

        HashMap mail = (HashMap) ctx.lookup((String) options.get(
                    Constants.MAIL_CONFIG));

        String messageFactory = (String) mail.get(Constants.MESSAGES_FACTORY);

        if ((messageFactory != null) && !messageFactory.trim().equals("")) {
            Class clazz = null;
            clazz = Class.forName(messageFactory.trim());
            this.messageable = (Messageable) clazz.newInstance();
            this.messageable.setProperties(mail);
        }

        this.hibernateSessionFactory = (String) mail.get(Constants.HIBERNATE_SESSION_FACTORY);
        this.folderSpam = (String) mail.get(Constants.MAIL_FOLDER_SPAM);
        this.folderTrash = (String) mail.get(Constants.MAIL_FOLDER_TRASH);

        String tempDir = System.getProperty("java.io.tmpdir");

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        FileUtilities.deleteMotLocks(new File(tempDir));
        FileUtilities.deleteLuceneLocks(new File(tempDir));
    }

    /* (non-Javadoc)
     * @see org.jboss.varia.scheduler.Schedulable#perform(java.util.Date, long)
     */
    public void perform(Date arg0, long arg1) {
        if (isInit()) {
            DLog.log(DLog.DEBUG, this.getClass(),
                "PurgeTrashAndSpam is running and wait.");

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

            Calendar cal = new GregorianCalendar();
            int year = cal.get(Calendar.YEAR); // 2002
            int month = cal.get(Calendar.MONTH); // 0=Jan, 1=Feb, ...
            int day = cal.get(Calendar.DAY_OF_MONTH); // 1...

            Calendar cal1 = new GregorianCalendar(year, month - 1, day, 0, 0, 0);
            Date date = new Date(cal1.getTimeInMillis());

            Criteria criteria = hsession.createCriteria(Message.class);
            criteria.add(Restrictions.in("mesBox",
                    new String[] { this.folderSpam, this.folderTrash }));
            criteria.add(Restrictions.le("mesDate", date));
            criteria.addOrder(Order.asc("mesDate"));

            ScrollableResults scroll = criteria.scroll();

            while (scroll.next()) {
                Message message = (Message) scroll.get(0);

                Users user = message.getUsers();

                try {
                    messageable.deleteMimeMessage(message.getMesName(), user);
                } catch (Exception e) {
                    DLog.log(DLog.INFO, this.getClass(),
                        e.getMessage() + " for user " + user.getUseUsername());
                }

                hsession.delete(message);

                hsession.flush();

                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.gc();
            DLog.log(DLog.WARN, this.getClass(), e.getMessage());
        } catch (OutOfMemoryError e) {
            System.gc();
            DLog.log(DLog.WARN, this.getClass(), e.getMessage());
        } catch (Throwable e) {
            System.gc();
            DLog.log(DLog.WARN, this.getClass(), e.getMessage());
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            setInit(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isInit() {
        synchronized (PurgeTrashAndSpam.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (PurgeTrashAndSpam.class) {
            this.init = init;
        }
    }
}
