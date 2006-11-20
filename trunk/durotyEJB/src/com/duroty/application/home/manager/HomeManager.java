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
package com.duroty.application.home.manager;

import com.duroty.application.home.exceptions.HomeException;
import com.duroty.application.home.utils.FeedObj;

import com.duroty.hibernate.FeedChannel;
import com.duroty.hibernate.FeedData;
import com.duroty.hibernate.Users;

import com.duroty.utils.GeneralOperations;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.Vector;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class HomeManager {
    /**
     * Creates a new HomeManager object.
     *
     * @param bookmark DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     * @throws InstantiationException DOCUMENT ME!
     * @throws IllegalAccessException DOCUMENT ME!
     */
    public HomeManager(HashMap bookmark)
        throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @throws HomeException DOCUMENT ME!
     */
    public void saveFeed(Session hsession, String repositoryName,
        int idintChannel, String name, String value) throws HomeException {
        try {
            FeedChannel channel = null;

            Users user = getUser(hsession, repositoryName);

            if (idintChannel == 0) {
                Criteria criteria = hsession.createCriteria(FeedChannel.class);
                criteria.add(Restrictions.eq("users", user));
                criteria.add(Restrictions.eq("fechName", "Main"));

                channel = (FeedChannel) criteria.uniqueResult();

                if (channel == null) {
                    channel = new FeedChannel();
                    channel.setFechName("Main");
                    channel.setUsers(user);

                    hsession.saveOrUpdate(channel);
                    hsession.flush();
                }
            } else {
                Criteria criteria = hsession.createCriteria(FeedChannel.class);
                criteria.add(Restrictions.eq("users", user));
                criteria.add(Restrictions.eq("fechIdint",
                        new Integer(idintChannel)));

                channel = (FeedChannel) criteria.uniqueResult();
            }

            Criteria criteria = hsession.createCriteria(FeedData.class);
            criteria.add(Restrictions.eq("fedaName", name));
            criteria.add(Restrictions.eq("feedChannel", channel));

            FeedData feed = (FeedData) criteria.uniqueResult();

            if (feed == null) {
                feed = new FeedData();
                feed.setFedaName(name);
                feed.setFedaValue(value);
                feed.setFeedChannel(channel);

                hsession.save(feed);
            } else {
                feed.setFedaName(name);
                feed.setFedaValue(value);
                hsession.saveOrUpdate(feed);
            }

            hsession.flush();

            /*if (value.indexOf("=none;expires=") > -1) {
                    hsession.delete(feed);
                    hsession.flush();
            }*/
        } catch (Exception e) {
            throw new HomeException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idintChannel DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @throws HomeException DOCUMENT ME!
     */
    public FeedObj getFeed(Session hsession, String repositoryName,
        int idintChannel, String name) throws HomeException {
        FeedObj obj = null;

        try {
            FeedChannel channel = null;

            Users user = getUser(hsession, repositoryName);

            if (idintChannel == 0) {
                Criteria criteria = hsession.createCriteria(FeedChannel.class);
                criteria.add(Restrictions.eq("users", user));
                criteria.add(Restrictions.eq("fechName", "Main"));

                channel = (FeedChannel) criteria.uniqueResult();

                if (channel == null) {
                    channel = new FeedChannel();
                    channel.setFechName("Main");
                    channel.setUsers(user);

                    hsession.saveOrUpdate(channel);
                    hsession.flush();
                }
            } else {
                Criteria criteria = hsession.createCriteria(FeedChannel.class);
                criteria.add(Restrictions.eq("users", user));
                criteria.add(Restrictions.eq("fechIdint",
                        new Integer(idintChannel)));

                channel = (FeedChannel) criteria.uniqueResult();
            }

            Criteria criteria = hsession.createCriteria(FeedData.class);
            criteria.add(Restrictions.eq("fedaName", name));
            criteria.add(Restrictions.eq("feedChannel", channel));

            FeedData feed = (FeedData) criteria.uniqueResult();

            if (feed != null) {
                obj = new FeedObj();
                obj.setChannel(channel.getFechIdint());
                obj.setIdint(feed.getFedaIdint());
                obj.setName(feed.getFedaName());
                obj.setValue(feed.getFedaValue());
            }

            return obj;
        } catch (Exception e) {
            throw new HomeException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param idintChannel DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws HomeException DOCUMENT ME!
     */
    public Vector getAllFeed(Session hsession, String repositoryName,
        int idintChannel) throws HomeException {
        Vector feeds = new Vector();

        try {
            FeedChannel channel = null;

            Users user = getUser(hsession, repositoryName);

            if (idintChannel == 0) {
                Criteria criteria = hsession.createCriteria(FeedChannel.class);
                criteria.add(Restrictions.eq("users", user));
                criteria.add(Restrictions.eq("fechName", "Main"));

                channel = (FeedChannel) criteria.uniqueResult();

                if (channel == null) {
                    channel = new FeedChannel();
                    channel.setFechName("Main");
                    channel.setUsers(user);

                    hsession.saveOrUpdate(channel);
                    hsession.flush();
                }
            } else {
                Criteria criteria = hsession.createCriteria(FeedChannel.class);
                criteria.add(Restrictions.eq("users", user));
                criteria.add(Restrictions.eq("fechIdint",
                        new Integer(idintChannel)));

                channel = (FeedChannel) criteria.uniqueResult();
            }

            Criteria crit = hsession.createCriteria(FeedData.class);
            crit.add(Restrictions.eq("feedChannel", channel));
            crit.addOrder(Order.asc("fedaName"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                FeedData feed = (FeedData) scroll.get(0);
                feeds.addElement(feed.getFedaValue());
            }

            return feeds;
        } catch (Exception e) {
            throw new HomeException(e);
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
