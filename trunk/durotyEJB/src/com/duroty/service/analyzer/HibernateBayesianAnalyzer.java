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


package com.duroty.service.analyzer;

import com.duroty.hibernate.BayesianHam;
import com.duroty.hibernate.BayesianMessagecounts;
import com.duroty.hibernate.BayesianSpam;
import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.utils.GeneralOperations;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.Iterator;
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
public class HibernateBayesianAnalyzer extends BayesianAnalyzer {
    /**
    *Public object representing a lock on database activity.
    */
    public final static String DATABASE_LOCK = "database lock";

    /**
     * DOCUMENT ME!
     */
    private String hibernateSessionFactory;

    /**
     * DOCUMENT ME!
     */
    private Context ctx = null;

    /**
     * Default constructor.
     */
    public HibernateBayesianAnalyzer() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param properties DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     */
    public void init(HashMap properties) throws NamingException {
        ctx = new InitialContext();

        if (properties == null) {
            Map options = ApplicationConstants.options;
            properties = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));
        } else {
        }

        hibernateSessionFactory = (String) properties.get(Constants.HIBERNATE_SESSION_FACTORY);
    }

    /**
     * Loads the token frequencies from the database.
     * @param conn The connection for accessing the database
     * @throws Exception
     */
    public void loadHamNSpam(String username) throws Exception {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Users user = getUser(hsession, username);

            Criteria crit = hsession.createCriteria(BayesianHam.class);
            crit.add(Restrictions.eq("users", user));

            ScrollableResults scroll = crit.scroll();

            Map ham = getHamTokenCounts();

            while (scroll.next()) {
                BayesianHam bham = (BayesianHam) scroll.get(0);

                String token = bham.getHamToken();
                int count = bham.getHamOcurrences();

                // to reduce memory, use the token only if the count is > 1
                if (count > 1) {
                    ham.put(token, new Integer(count));
                }
            }

            crit = hsession.createCriteria(BayesianSpam.class);
            crit.add(Restrictions.eq("users", user));

            scroll = crit.scroll();

            Map spam = getSpamTokenCounts();

            while (scroll.next()) {
                BayesianSpam bspam = (BayesianSpam) scroll.get(0);
                String token = bspam.getSpamToken();
                int count = bspam.getSpamOcurrences();

                // to reduce memory, use the token only if the count is > 1
                if (count > 1) {
                    spam.put(token, new Integer(count));
                }
            }

            //Get the ham/spam message counts.
            crit = hsession.createCriteria(BayesianMessagecounts.class);
            crit.add(Restrictions.eq("users", user));

            BayesianMessagecounts bmc = (BayesianMessagecounts) crit.uniqueResult();

            if (bmc != null) {
                setHamMessageCount(bmc.getHamCount());
                setSpamMessageCount(bmc.getSpamCount());
            }
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * Updates the database with new "ham" token frequencies.
     * @param conn The connection for accessing the database
     * @throws Exception
     */
    public void updateHamTokens(String username) throws Exception {
        updateTokens(username, getHamTokenCounts(), null);

        setMessageCount(username, getHamMessageCount(), -1);
    }

    /**
     * Updates the database with new "spam" token frequencies.
     * @param conn The connection for accessing the database
     * @throws Exception
     */
    public void updateSpamTokens(String username) throws Exception {
        updateTokens(username, null, getSpamTokenCounts());

        setMessageCount(username, -1, getSpamMessageCount());
    }

    /**
     * DOCUMENT ME!
     *
     * @param conn DOCUMENT ME!
     * @param sqlStatement DOCUMENT ME!
     * @param count DOCUMENT ME!
     * @throws Exception
     * @throws java.sql.SQLException DOCUMENT ME!
     */
    private void setMessageCount(String username, int hamCount, int spamCount)
        throws Exception {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Users user = getUser(hsession, username);

            Criteria crit = hsession.createCriteria(BayesianMessagecounts.class);
            crit.add(Restrictions.eq("users", user));

            BayesianMessagecounts bmc = (BayesianMessagecounts) crit.uniqueResult();

            if (bmc == null) {
                bmc = new BayesianMessagecounts();
                bmc.setHamCount(0);
                bmc.setSpamCount(0);
                bmc.setUsers(user);

                hsession.save(bmc);
                hsession.flush();
            }

            if (hamCount > -1) {
                bmc.setHamCount(bmc.getHamCount() + hamCount);
            }

            if (spamCount > -1) {
                bmc.setSpamCount(bmc.getSpamCount() + spamCount);
            }

            hsession.update(bmc);
            hsession.flush();
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param conn DOCUMENT ME!
     * @param tokens DOCUMENT ME!
     * @param insertSqlStatement DOCUMENT ME!
     * @param updateSqlStatement DOCUMENT ME!
     * @throws Exception
     */
    private void updateTokens(String username, Map hamTokens, Map spamTokens)
        throws Exception {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Users user = getUser(hsession, username);

            if ((hamTokens != null) && (hamTokens.size() > 0)) {
                Iterator i = hamTokens.keySet().iterator();

                while (i.hasNext()) {
                    String key = (String) i.next();
                    int value = ((Integer) hamTokens.get(key)).intValue();

                    Criteria criteria = hsession.createCriteria(BayesianHam.class);
                    criteria.add(Restrictions.eq("users", user));
                    criteria.add(Restrictions.eq("hamToken", key));

                    BayesianHam bh = (BayesianHam) criteria.uniqueResult();

                    if (bh == null) {
                        bh = new BayesianHam();
                        bh.setHamToken(key);
                        bh.setHamOcurrences(value);
                        bh.setUsers(user);

                        hsession.save(bh);
                        hsession.flush();
                    } else {
                        bh.setHamOcurrences(bh.getHamOcurrences() + value);
                        hsession.update(bh);
                        hsession.flush();
                    }
                }
            }

            if ((spamTokens != null) && (spamTokens.size() > 0)) {
                Iterator i = spamTokens.keySet().iterator();

                while (i.hasNext()) {
                    String key = (String) i.next();
                    int value = ((Integer) spamTokens.get(key)).intValue();

                    Criteria criteria = hsession.createCriteria(BayesianSpam.class);
                    criteria.add(Restrictions.eq("users", user));
                    criteria.add(Restrictions.eq("spamToken", key));

                    BayesianSpam bh = (BayesianSpam) criteria.uniqueResult();

                    if (bh == null) {
                        bh = new BayesianSpam();
                        bh.setSpamToken(key);
                        bh.setSpamOcurrences(value);
                        bh.setUsers(user);

                        hsession.save(bh);
                        hsession.flush();
                    } else {
                        bh.setSpamOcurrences(bh.getSpamOcurrences() + value);
                        hsession.update(bh);
                        hsession.flush();
                    }
                }
            }
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
