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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.varia.scheduler.Schedulable;

import com.duroty.hibernate.Conversations;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ChatConversationsTask implements Schedulable {
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
    private String hibernateSessionFactory;

    /**
     * Creates a new ChatConversationsTask object.
     *
     * @param poolSize DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     * @throws NamingException DOCUMENT ME!
     * @throws InstantiationException DOCUMENT ME!
     * @throws IllegalAccessException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public ChatConversationsTask()
        throws ClassNotFoundException, NamingException, InstantiationException, 
            IllegalAccessException, IOException {
        super();

        try {
            Map options = ApplicationConstants.options;

            ctx = new InitialContext();

            HashMap chat = (HashMap) ctx.lookup((String) options.get(
                        Constants.CHAT_CONFIG));

            this.hibernateSessionFactory = (String) chat.get(Constants.HIBERNATE_SESSION_FACTORY);
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
                "ChatConversationsTask is running and wait.");

            return;
        }

        try {
            setInit(true);

            flush();
        } catch (Exception e) {
            DLog.log(DLog.ERROR, this.getClass(), e);
        } finally {
            setInit(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dirUsers DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void flush() throws Exception {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Calendar cal = new GregorianCalendar();

            //int hour = cal.get(Calendar.HOUR_OF_DAY);
            //int minute = cal.get(Calendar.MINUTE);
            //int second = cal.get(Calendar.SECOND);
            Calendar cal1 = new GregorianCalendar(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE) - 1, cal.get(Calendar.SECOND));
            Date date = new Date(cal1.getTimeInMillis());

            Criteria crit = hsession.createCriteria(Conversations.class);
            crit.add(Restrictions.le("convStamp", date));
            crit.addOrder(Order.asc("convStamp"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {    
                hsession.delete(scroll.get(0));
                hsession.flush();
            }            
        } catch (Exception e) {
            System.gc();

            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } catch (OutOfMemoryError e) {
            System.gc();

            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } catch (Throwable e) {
            System.gc();

            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } finally {
            GeneralOperations.closeHibernateSession(hsession);

            setInit(false);

            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isInit() {
        synchronized (ChatConversationsTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (ChatConversationsTask.class) {
            this.init = init;
        }
    }
}
