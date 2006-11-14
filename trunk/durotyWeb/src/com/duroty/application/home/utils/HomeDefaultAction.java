/**
 *
 */
package com.duroty.application.home.utils;

import com.duroty.application.home.interfaces.Home;
import com.duroty.application.home.interfaces.HomeHome;
import com.duroty.application.home.interfaces.HomeUtil;
import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.PreferencesHome;
import com.duroty.application.mail.interfaces.PreferencesUtil;

import com.duroty.config.Configuration;

import com.duroty.controller.actions.DefaultAction;

import com.duroty.cookie.CookieManager;

import com.duroty.exceptions.LanguageControlException;

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
public abstract class HomeDefaultAction extends DefaultAction {
    /**
     * Creates a new HomeDefaultAction object.
     */
    public HomeDefaultAction() {
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
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
    protected Home getHomeInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        HomeHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = HomeUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = HomeUtil.getHome(environment);
        }

        return home.create();
    }
}
