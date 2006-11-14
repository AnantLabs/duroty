package com.duroty.controller.actions;

import java.rmi.RemoteException;

import com.duroty.application.chat.exceptions.ChatException;
import com.duroty.application.chat.interfaces.Chat;
import com.duroty.constants.Constants;
import com.duroty.cookie.CookieManager;

import com.duroty.session.SessionManager;

import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class LogoutAction extends DefaultAction {
    /**
     * Creates a new instance of LogoutAction
     */
    public LogoutAction() {
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
    	
    	try {
			Chat chat = getChatInstance(request);
			chat.logout();
			
			Cookie cookieUsername = CookieManager.getCookie("c_username", request);
			if (cookieUsername != null) {
				cookieUsername.setMaxAge(-1);
				cookieUsername.setValue("");
				CookieManager.setCookie("/", cookieUsername, response);
			}
			
			Cookie cookiePassword = CookieManager.getCookie("c_password", request);
			if (cookiePassword != null) {
				cookiePassword.setMaxAge(-1);
				cookiePassword.setValue("");
				CookieManager.setCookie("/", cookiePassword, response);
			}					
		} catch (RemoteException e) {
		} catch (NamingException e) {
		} catch (CreateException e) {
		} catch (ChatException e) {
		} finally {
			SessionManager.invalidate(request, response);
		}

        doTrace(request, DLog.INFO, getClass(), "OK");

        return mapping.findForward(Constants.ACTION_SUCCESS_FORWARD);
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

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    }
}
