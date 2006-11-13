/*
 * LanguageAction.java
 *
 * Created on 3 de agosto de 2004, 9:49
 */
package com.duroty.controller.actions;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.duroty.config.Configuration;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.cookie.CookieManager;
import com.duroty.exceptions.IllegalConcurrentAccessException;
import com.duroty.exceptions.LanguageControlException;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;



/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class LanguageAction extends Action {
    /**
     * Creates a new instance of LanguageAction
     */
    public LanguageAction() {
    }

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
            actionForward = doExecute(mapping, form, request, response);
        } catch (Exception e) {
            actionForward = doOnError(mapping, form, request, response, e);
        }

        return actionForward;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ActionMessages errors = new ActionMessages();
        
        String name = Configuration.properties.getProperty(Configuration.COOKIE_LANGUAGE);
    	int maxAge = Integer.parseInt(Configuration.properties.getProperty(Configuration.COOKIE_MAX_AGE));

        String language = request.getParameter("language");

        if (language != null) {
            boolean control = false;

            String[] avlang = Configuration.properties.getProperty(Configuration.AVAILABLE_LANGUAGE).split(",");

            for (int i = 0; i < avlang.length; i++) {
                if (language.equals(avlang[i])) {
                    control = true;

                    break;
                }
            }

            if (!control) {
                doTrace(request, DLog.ERROR, getClass(),
                    "The language '" + language + "'is not available");

                errors.add(Constants.LANGUAGE_ERROR_PARAMETER,
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "Language"));
            } else {            	
            	Cookie cookie = new Cookie(name, language);
        		cookie.setMaxAge(maxAge);
        		CookieManager.setCookie("/", cookie, response);
            	
                Locale currentLocale = new Locale(language);
                setLocale(request, currentLocale);
            }
        } else {
        	Cookie cookie = CookieManager.getCookie(name, request);
        	if (cookie != null) {
        		language = cookie.getValue();
        		cookie.setMaxAge(maxAge);
        		CookieManager.setCookie("/", cookie, response);
        	}
        	
        	Boolean b = new Boolean(Configuration.properties.getProperty(Configuration.AUTO_LOCALE));
            boolean autoLocale = b.booleanValue();

            if (language == null) {
                if (!autoLocale) {
                    throw new LanguageControlException("Choose Language. The language is empty", null);
                } else {
                	language = Configuration.properties.getProperty(Configuration.DEFAULT_LANGUAGE);
                }
            }
            
            Locale currentLocale = new Locale(language);
            setLocale(request, currentLocale);
        }

        String langReferer = request.getParameter("referer");

        if ((langReferer != null) && !langReferer.equals("")) {
            langReferer = URLDecoder.decode(langReferer, Charset.defaultCharset().displayName());
        } else {
            langReferer = "/login/login.jsp";
        }

        request.setAttribute("langReferer", langReferer);

        if (errors.isEmpty()) {
            doTrace(request, DLog.INFO, getClass(), "OK");

            return new ActionForward(langReferer, false);
        } else {
            saveErrors(request, errors);

            return mapping.findForward(Constants.ACTION_FAIL_FORWARD);
        }
    }

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
        if (exception instanceof IllegalConcurrentAccessException) {
            IllegalConcurrentAccessException icae = (IllegalConcurrentAccessException) exception;
            DLog.log(DLog.FATAL, getClass(), icae.getMessage());
            request.setAttribute("exception", icae);

            return mapping.findForward(Constants.ILLEGAL_CONCURRENT_ACCESS_GLOBAL_FORWARD);
        } else if (exception instanceof LanguageControlException) {
            DLog.log(DLog.INFO, getClass(), exception.getMessage());

            return mapping.findForward(Constants.AUTO_LOCALE_GLOBAL_FORWARD);
        } else {
            DLog.log(DLog.ERROR, getClass(), exception.getMessage());

            request.setAttribute(Constants.EXCEPTION_PARAMENTER, exception);

            return mapping.findForward(Constants.INTERNAL_ERROR_GLOBAL_FORWARD);
        }
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
    protected void doTrace(HttpServletRequest request, int level, Class classe,
        String message) throws Exception {
        DLog.log(level, classe, DMessage.toString(request, message));
    }
}
