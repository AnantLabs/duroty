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
package com.duroty.application.files.utils;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.duroty.application.files.interfaces.Files;
import com.duroty.application.files.interfaces.FilesHome;
import com.duroty.application.files.interfaces.FilesSearch;
import com.duroty.application.files.interfaces.FilesSearchHome;
import com.duroty.application.files.interfaces.FilesSearchUtil;
import com.duroty.application.files.interfaces.FilesUtil;
import com.duroty.application.files.interfaces.Store;
import com.duroty.application.files.interfaces.StoreHome;
import com.duroty.application.files.interfaces.StoreUtil;
import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.PreferencesHome;
import com.duroty.application.mail.interfaces.PreferencesUtil;
import com.duroty.config.Configuration;
import com.duroty.controller.actions.DefaultAction;
import com.duroty.cookie.CookieManager;
import com.duroty.exceptions.LanguageControlException;
import com.duroty.utils.Pagination;


/**
 * @author durot
 *
 */
public abstract class FilesDefaultAction extends DefaultAction {
    /** Controlador de disc per realitzar els attachments dels correus electrònics */
    protected static DiskFileUpload diskFileUpload = null;

    /**
     * Creates a new MailDefaultAction object.
     */
    public FilesDefaultAction() {
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        if (diskFileUpload == null) {
            diskFileUpload = new DiskFileUpload();

            long sizeMax = Long.parseLong(Configuration.properties.getProperty(
                        Configuration.MAX_FILE_UPLOAD));
            diskFileUpload.setSizeMax(sizeMax);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     * @throws CreateException DOCUMENT ME!
     */
    protected Files getFilesInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        FilesHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = FilesUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = FilesUtil.getHome(environment);
        }

        return home.create();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     * @throws CreateException DOCUMENT ME!
     */
    protected Store getStoreInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        StoreHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = StoreUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = StoreUtil.getHome(environment);
        }

        return home.create();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     * @throws CreateException DOCUMENT ME!
     */
    protected Preferences getPreferencesInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        PreferencesHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = PreferencesUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = PreferencesUtil.getHome(environment);
        }

        return home.create();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     * @throws CreateException DOCUMENT ME!
     */
    protected FilesSearch getFilesSearchInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
    	FilesSearchHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = FilesSearchUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = FilesSearchUtil.getHome(environment);
        }

        return home.create();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getPagination(HttpServletRequest request, String action,
        int hits, int messagesByPage) {
        int pag = 1;

        if (action == null) {
            action = "Files.getFiles";
        }

        if (request.getParameter("page") != null) {
            try {
                pag = Integer.parseInt(request.getParameter("page"));

                if (pag <= 0) {
                    pag = 1;
                }
            } catch (NumberFormatException nfe) {
                pag = 1;
            }
        }

        Pagination pagination = new Pagination(action, pag, hits, messagesByPage, 10);

        return pagination.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws LanguageControlException DOCUMENT ME!
     */
    protected Locale languageControl(HttpServletRequest request,
        HttpServletResponse response) throws LanguageControlException {
        Preferences preferences = null;
        String language = null;
        String name = Configuration.properties.getProperty(Configuration.COOKIE_LANGUAGE);
        int maxAge = Integer.parseInt(Configuration.properties.getProperty(
                    Configuration.COOKIE_MAX_AGE));

        Cookie cookie = CookieManager.getCookie(name, request);

        if (cookie != null) {
            language = cookie.getValue();
            cookie.setMaxAge(maxAge);
            CookieManager.setCookie("/", cookie, response);
        } else {
        }

        try {
            preferences = getPreferencesInstance(request);
            language = preferences.getPreferences().getLanguage();
        } catch (RemoteException e) {
        } catch (NamingException e) {
        } catch (CreateException e) {
        } catch (MailException e) {
        }

        Boolean b = new Boolean(Configuration.properties.getProperty(
                    Configuration.AUTO_LOCALE));
        boolean autoLocale = b.booleanValue();

        if (language == null) {
            if (!autoLocale) {
                throw new LanguageControlException("Choose Language. The language is empty",
                    null);
            } else {
                language = Configuration.properties.getProperty(Configuration.DEFAULT_LANGUAGE);
            }
        }

        cookie = new Cookie(name, language);
        cookie.setMaxAge(maxAge);
        CookieManager.setCookie("/", cookie, response);

        return new Locale(language);
    }
}
