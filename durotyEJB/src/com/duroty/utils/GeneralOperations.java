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
package com.duroty.utils;

import com.duroty.utils.log.DLog;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;


/**
 * @author durot
 *
 */
public class GeneralOperations {
    /**
     * Creates a new GeneralOperations object.
     */
    private GeneralOperations() {
    }

    /**
    * DOCUMENT ME!
    *
    * @param hsession DOCUMENT ME!
    */
    public static void closeHibernateSession(Session hsession) {
        if (hsession != null) {
            try {
                hsession.close();
            } catch (HibernateException e) {
                DLog.log(DLog.DEBUG, GeneralOperations.class, e.getMessage());
            } finally {
                hsession = null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hfactory DOCUMENT ME!
     */
    public static void closeHibernateSessionFactory(SessionFactory hfactory) {
        if (hfactory != null) {
            try {
                hfactory.close();
            } catch (HibernateException e) {
                DLog.log(DLog.DEBUG, GeneralOperations.class, e.getMessage());
            } finally {
                hfactory = null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param folder DOCUMENT ME!
     * @param expunge DOCUMENT ME!
     */
    public static void closeMailFolder(Folder folder, boolean expunge) {
        if (folder == null) {
            return;
        }

        synchronized (folder) {
            if ((folder != null) && folder.isOpen()) {
                try {
                    folder.close(expunge);
                } catch (MessagingException e) {
                    DLog.log(DLog.DEBUG, GeneralOperations.class, e.getMessage());
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param store DOCUMENT ME!
     */
    public static void closeMailStore(Store store) {
        if (store == null) {
            return;
        }

        synchronized (store) {
            if ((store != null) && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    DLog.log(DLog.DEBUG, GeneralOperations.class, e.getMessage());
                }
            }
        }
    }
}
