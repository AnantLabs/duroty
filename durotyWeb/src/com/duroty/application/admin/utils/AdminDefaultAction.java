/**
 *
 */
package com.duroty.application.admin.utils;

import com.duroty.application.admin.interfaces.Admin;
import com.duroty.application.admin.interfaces.AdminHome;
import com.duroty.application.admin.interfaces.AdminUtil;
import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.interfaces.Mail;
import com.duroty.application.mail.interfaces.MailHome;
import com.duroty.application.mail.interfaces.MailUtil;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.PreferencesHome;
import com.duroty.application.mail.interfaces.PreferencesUtil;
import com.duroty.application.mail.interfaces.Search;
import com.duroty.application.mail.interfaces.SearchHome;
import com.duroty.application.mail.interfaces.SearchUtil;
import com.duroty.application.mail.interfaces.Send;
import com.duroty.application.mail.interfaces.SendHome;
import com.duroty.application.mail.interfaces.SendUtil;

import com.duroty.config.Configuration;

import com.duroty.controller.actions.DefaultAction;

import com.duroty.cookie.CookieManager;

import com.duroty.exceptions.LanguageControlException;

import com.duroty.utils.Pagination;

import org.apache.commons.fileupload.DiskFileUpload;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.rmi.RemoteException;

import java.util.Hashtable;
import java.util.Locale;

import javax.ejb.CreateException;

import javax.naming.NamingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public abstract class AdminDefaultAction extends DefaultAction {
    /** Controlador de disc per realitzar els attachments dels correus electrònics */
    protected static DiskFileUpload diskFileUpload = null;

    /**
     * Creates a new MailDefaultAction object.
     */
    public AdminDefaultAction() {
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        if (diskFileUpload == null) {
            diskFileUpload = new DiskFileUpload();
            diskFileUpload.setSizeMax(1073741824);
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
    protected Admin getAdminInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        AdminHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = AdminUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = AdminUtil.getHome(environment);
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
    protected Mail getMailInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        MailHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = MailUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = MailUtil.getHome(environment);
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
    protected Send getSendInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        SendHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = SendUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = SendUtil.getHome(environment);
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
    protected Search getSearchInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        SearchHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = SearchUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = SearchUtil.getHome(environment);
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
        String page = request.getParameter("page");
        String order = request.getParameter("order");
        String extra = request.getParameter("extra");
        int pag = 1;

        if (action == null) {
            action = "INBOX";
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

        if (order == null) {
            order = "0";
        }

        if (extra == null) {
            extra = "0";
        }

        Pagination pagination = new Pagination("Mail", pag, hits,
                messagesByPage, 10, action, page, order, extra);

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
