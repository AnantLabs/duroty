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
package com.duroty.application.bookmark.ejb;

import com.duroty.application.bookmark.exceptions.BookmarkException;
import com.duroty.application.bookmark.manager.BookmarkManager;
import com.duroty.application.bookmark.utils.BookmarkObj;
import com.duroty.application.bookmark.utils.SearchObj;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import org.hibernate.SessionFactory;

import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * XDoclet-based session bean.  The class must be declared
 * public according to the EJB specification.
 *
 * To generate the EJB related files to this EJB:
 *                - Add Standard EJB module to XDoclet project properties
 *                - Customize XDoclet configuration for your appserver
 *                - Run XDoclet
 *
 * Below are the xdoclet-related tags needed for this EJB.
 *
 * @ejb.bean name="Bookmark"
 *           display-name="Name for Bookmark"
 *           description="Description for Bookmark"
 *           jndi-name="duroty/ejb/Bookmark"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="bookmark"
 *   role-link="bookmark"
 */
public class BookmarkBean implements SessionBean {
    /**
         *
         */
    private static final long serialVersionUID = 4449764533947619038L;

    /** The session context */
    private SessionContext context;

    /**
    * the context jndi
    */
    private transient Context ctx = null;

    /**
     * the hibernate default factory
     */
    private String hibernateSessionFactory = null;

    /**
     * the manager bean
     */
    private transient BookmarkManager manager = null;

    /**
     * Creates a new BookmarkBean object.
     */
    public BookmarkBean() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Set the associated session context. The container calls this method
     * after the instance creation.
     *
     * The enterprise bean instance should store the reference to the context
     * object in an instance variable.
     *
     * This method is called with no transaction context.
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void setSessionContext(SessionContext newContext)
        throws EJBException {
        context = newContext;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbRemove() throws EJBException, RemoteException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbActivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub
    }

    /**
     * An ejbCreate method as required by the EJB specification.
     *
     * The container calls the instance?s <code>ejbCreate</code> method whose
     * signature matches the signature of the <code>create</code> method invoked
     * by the client. The input parameters sent from the client are passed to
     * the <code>ejbCreate</code> method. Each session bean class must have at
     * least one <code>ejbCreate</code> method. The number and signatures
     * of a session bean?s <code>create</code> methods are specific to each
     * session bean class.
     *
     * @throws CreateException Thrown if method fails due to system-level error.
     *
     * @ejb.create-method
    * @ejb.permission
    *         role-name = "bookmark"
     *
     */
    public void ejbCreate() throws CreateException {
        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap bookmark = (HashMap) ctx.lookup((String) options.get(
                        Constants.BOOKMARK_CONFIG));

            this.hibernateSessionFactory = (String) bookmark.get(Constants.HIBERNATE_SESSION_FACTORY);

            manager = new BookmarkManager(bookmark);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
    * Add a bookmark object
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public void addBookmark(BookmarkObj bookmarkObj) throws BookmarkException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.addBookmark(hfactory.openSession(), repositoryName,
                bookmarkObj);
        } catch (NamingException e) {
            throw new BookmarkException(e);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * Update a bookmark object
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public void updateBookmark(BookmarkObj bookmarkObj)
        throws BookmarkException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.updateBookmark(hfactory.openSession(), repositoryName,
                bookmarkObj);
        } catch (NamingException e) {
            throw new BookmarkException(e);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
     * Get bookmark object by idint
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "bookmark"
     *
     * @throws BookmarkException Thrown if method fails due to system-level error.
     */
    public BookmarkObj getBookmark(String idint) throws BookmarkException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getBookmark(hfactory.openSession(), repositoryName,
                idint);
        } catch (NamingException e) {
            throw new BookmarkException(e);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * Add bookmarks from from links
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public void addBookmarks(Vector links) throws BookmarkException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.addBookmarks(hfactory.openSession(), repositoryName, links);
        } catch (NamingException e) {
            throw new BookmarkException(e);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * delete bookmark by idint
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public void deleteBookmark(String idint) throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            manager.deleteBookmark(repositoryName, idint);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * flag bookmark by idint
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public void flagBookmark(String idint) throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            manager.flagBookmark(repositoryName, idint);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * unflag bookmark by idint
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public void unflagBookmark(String idint) throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            manager.unflagBookmark(repositoryName, idint);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * Search bookmarks by token
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public SearchObj search(String token, int page, int messagesByPage,
        int order, String orderType, boolean isNotebook)
        throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            return manager.search(repositoryName, token, page, messagesByPage,
                order, orderType, isNotebook);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
    * DidYouMean by token
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "bookmark"
    *
    * @throws BookmarkException Thrown if method fails due to system-level error.
    */
    public String didYouMean(String token) throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            return manager.didYouMean(repositoryName, token);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
     * Label keywords
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "bookmark"
     *
     * @throws BookmarkException Thrown if method fails due to system-level error.
     */
    public Vector getKeywords() throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getKeywords(repositoryName);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }

    /**
     * Label keywords fopr notebook
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "bookmark"
     *
     * @throws BookmarkException Thrown if method fails due to system-level error.
     */
    public Vector getKeywordsNotebook() throws BookmarkException {
        //SessionFactory hfactory = null;
        try {
            //hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getKeywordsNotebook(repositoryName);
        } catch (Exception e) {
            if (e instanceof BookmarkException) {
                throw (BookmarkException) e;
            }

            throw new BookmarkException(e);
        } finally {
        }
    }
}
