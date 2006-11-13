/*
 * DefaultAction.java
 *
 * Created on 13 de mayo de 2004, 10:45
 */
package com.duroty.controller.actions;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.duroty.application.chat.interfaces.Chat;
import com.duroty.application.chat.interfaces.ChatHome;
import com.duroty.application.chat.interfaces.ChatUtil;
import com.duroty.config.Configuration;
import com.duroty.constants.Constants;
import com.duroty.cookie.CookieManager;
import com.duroty.exceptions.IllegalConcurrentAccessException;
import com.duroty.exceptions.InternalErrorException;
import com.duroty.exceptions.LanguageControlException;
import com.duroty.session.SessionManager;
import com.duroty.utils.log.DLog;


/**
 * Template action for stuts action classes that implements some of the
 * functionality needed for all subclasses like exception handling.
 *
 * <p>
 * If during execution of the action, an
 * <code>IllegalConcurrentAccessException</code> is thrown, it tries to find
 * an <code>ActionForward</code> with name "IllegalConcurrentAccess" and
 * returns it.  In case of any other exception, it tries to find an
 * <code>ActionForward</code> with name "InternalError" and returns it.
 * </p>
 */
public abstract class DefaultAction extends Action {
    /**
     * Executes an action calling doExecute. If an exception is  thrown, it
     * executes doOnError.
     *
     * @param mapping the action's ActionMapping
     * @param form the ActionForm associatied with this class
     * @param request the request object
     * @param response the response object
     *
     * @return an ActionForward with the result of the action
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward actionForward = null;

        try {
        	//request.setCharacterEncoding(charset);
        	//response.setCharacterEncoding(charset);
        	
            Locale locale = languageControl(request, response);
            setLocale(request, locale);
            doInit(mapping, form, request, response);
            actionForward = doExecute(mapping, form, request, response);
            
            //Chat chat = getChatInstance(request);
            //chat.login();
        } catch (Exception e) {
            actionForward = doOnError(mapping, form, request, response, e);
        }

        return actionForward;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping the action's ActionMapping
     * @param form the ActionForm associatied with this class
     * @param request the request object
     * @param response the response object
     *
     * @throws Exception Any exception can be thrown by the action. However, it
     *         is recommended to handle all exceptions and provide meaningful
     *         information to the final user.
     */
    protected abstract void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception;

    /**
     * Executes the action
     *
     * @param mapping the action's ActionMapping
     * @param form the ActionForm associatied with this class
     * @param request the request object
     * @param response the response object
     *
     * @return an ActionForward with the result of the action
     *
     * @throws Exception Any exception can be thrown by the action. However, it
     *         is recommended to handle all exceptions and provide meaningful
     *         information to the final user.
     */
    protected abstract ActionForward doExecute(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception;

    /**
     * Performs whichever necessary actions to handle an exception thrown
     * during execution
     *
     * @param mapping the action's ActionMapping
     * @param form the ActionForm associatied with this class
     * @param request the request object
     * @param response the response object
     * @param exception the exception to process
     *
     * @return an ActionForward with exception-handling page
     */
    protected ActionForward doOnError(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response,
        Exception exception) {
        if (exception instanceof AuthenticationException) {
            AuthenticationException ae = (AuthenticationException) exception;
            DLog.log(DLog.FATAL, getClass(), ae.getMessage());
            request.setAttribute("exception", ae);

            return mapping.findForward(Constants.LOGIN_GLOGAL_FORWARD);
        } else if (exception instanceof IllegalConcurrentAccessException) {
            IllegalConcurrentAccessException icae = (IllegalConcurrentAccessException) exception;
            DLog.log(DLog.FATAL, getClass(), icae.getMessage());
            request.setAttribute("exception", icae);

            return mapping.findForward(Constants.ILLEGAL_CONCURRENT_ACCESS_GLOBAL_FORWARD);
        } else if (exception instanceof LanguageControlException) {
            DLog.log(DLog.WARN, getClass(), exception.getMessage());
            request.setAttribute("exception", exception);

            return mapping.findForward(Constants.AUTO_LOCALE_GLOBAL_FORWARD);
        } else if (exception instanceof InternalErrorException) {
            DLog.log(DLog.ERROR, getClass(), exception.getMessage());
            request.setAttribute("exception", exception);

            return mapping.findForward(Constants.INTERNAL_ERROR_GLOBAL_FORWARD);
        } else {
            DLog.log(DLog.FATAL, getClass(), exception.getMessage());

            request.setAttribute("exception", exception);

            return mapping.findForward(Constants.INTERNAL_ERROR_GLOBAL_FORWARD);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Locale languageControl(HttpServletRequest request,
        HttpServletResponse response) throws LanguageControlException {
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

        if (language == null) {
            String locale = request.getParameter("locale");

            if (locale != null) {
                boolean control = false;

                String[] avlang = Configuration.properties.getProperty(Configuration.AVAILABLE_LANGUAGE)
                                                          .split(",");

                for (int i = 0; i < avlang.length; i++) {
                    if (locale.equals(avlang[i])) {
                        control = true;
                        language = locale;

                        break;
                    }
                }

                if (!control) {
                    throw new LanguageControlException(
                        "Choose Language. The language = " + language +
                        " not enabled", null);
                } else {
                    cookie = new Cookie(name, language);
                    cookie.setMaxAge(maxAge);
                    CookieManager.setCookie("/", cookie, response);
                }
            }
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
     * @return DOCUMENT ME!
     */
    protected Hashtable getContextProperties(HttpServletRequest request) {
        Hashtable props = (Hashtable) SessionManager.getObject(Constants.CONTEXT_PROPERTIES,
                request);

        if (props == null) {
            props = new Hashtable();

            props.put(Context.INITIAL_CONTEXT_FACTORY, Configuration.properties.getProperty(Configuration.JNDI_INITIAL_CONTEXT_FACTORY));
            props.put(Context.URL_PKG_PREFIXES, Configuration.properties.getProperty(Configuration.JNDI_URL_PKG_PREFIXES));
            props.put(Context.PROVIDER_URL, Configuration.properties.getProperty(Configuration.JNDI_PROVIDER_URL));

            Principal principal = request.getUserPrincipal();
            props.put(Context.SECURITY_PRINCIPAL, principal.getName());
            props.put(Context.SECURITY_CREDENTIALS, SessionManager.getObject(Constants.JAAS_PASSWORD, request));
            
            props.put(Context.SECURITY_PROTOCOL, Configuration.properties.getProperty(Configuration.SECURITY_PROTOCOL));

            SessionManager.setObject(Constants.CONTEXT_PROPERTIES, props, request);
        }

        return props;
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param classe DOCUMENT ME!
     * @param message DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected abstract void doTrace(HttpServletRequest request, int level,
        Class classe, String message) throws Exception;

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
    protected Chat getChatInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        ChatHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = ChatUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = ChatUtil.getHome(environment);
        }

        return home.create();
    }
}
