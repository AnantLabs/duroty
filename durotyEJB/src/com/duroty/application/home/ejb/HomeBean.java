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
package com.duroty.application.home.ejb;

import com.duroty.application.home.exceptions.HomeException;
import com.duroty.application.home.manager.HomeManager;
import com.duroty.application.home.utils.FeedObj;

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
 * @ejb.bean name="Home"
 *           display-name="Name for Home"
 *           description="Description for Home"
 *           jndi-name="duroty/ejb/Home"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="member"
 *   role-link="member"
 */
public class HomeBean implements SessionBean {
    /**
         *
         */
    private static final long serialVersionUID = -2866942062147458872L;

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
     * DOCUMENT ME!
     */
    private transient HomeManager manager = null;

    /**
     * Creates a new HomeBean object.
     */
    public HomeBean() {
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
    *         role-name = "member"
     *
     */
    public void ejbCreate() throws CreateException {
        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap home = (HashMap) ctx.lookup((String) options.get(
                        Constants.BOOKMARK_CONFIG));

            this.hibernateSessionFactory = (String) home.get(Constants.HIBERNATE_SESSION_FACTORY);

            manager = new HomeManager(home);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Save feed
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "member"
     *
     * @throws HomeException Thrown if method fails due to system-level error.
     */
    public void saveFeed(int channel, String name, String value)
        throws HomeException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.saveFeed(hfactory.openSession(), repositoryName, channel,
                name, value);
        } catch (NamingException e) {
            throw new HomeException(e);
        } catch (Exception e) {
            if (e instanceof HomeException) {
                throw (HomeException) e;
            }

            throw new HomeException(e);
        } finally {
        }
    }

    /**
     * Get feed
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "member"
     *
     * @throws HomeException Thrown if method fails due to system-level error.
     */
    public FeedObj getFeed(int channel, String name) throws HomeException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getFeed(hfactory.openSession(), repositoryName,
                channel, name);
        } catch (NamingException e) {
            throw new HomeException(e);
        } catch (Exception e) {
            if (e instanceof HomeException) {
                throw (HomeException) e;
            }

            throw new HomeException(e);
        } finally {
        }
    }

    /**
     * Get cookies database
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "member"
     *
     * @throws HomeException Thrown if method fails due to system-level error.
     */
    public Vector getAllFeed(int channel) throws HomeException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getAllFeed(hfactory.openSession(), repositoryName,
                channel);
        } catch (NamingException e) {
            throw new HomeException(e);
        } catch (Exception e) {
            if (e instanceof HomeException) {
                throw (HomeException) e;
            }

            throw new HomeException(e);
        } finally {
        }
    }
}
