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
package com.duroty.controller.actions;

import com.duroty.application.open.interfaces.Open;
import com.duroty.application.open.interfaces.OpenHome;
import com.duroty.application.open.interfaces.OpenUtil;

import com.duroty.config.Configuration;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;

import com.duroty.controller.singleton.CaptchaServiceSingleton;

import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;

import com.octo.captcha.service.CaptchaServiceException;

import org.apache.commons.lang.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.rmi.RemoteException;

import java.util.Hashtable;

import javax.ejb.CreateException;

import javax.naming.NamingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public class ForgotPasswordAction extends DefaultAction {
    /**
     *
     */
    public ForgotPasswordAction() {
        super();
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ActionMessages errors = new ActionMessages();

        try {
            Boolean isResponseCorrect = Boolean.FALSE;

            //remenber that we need an id to validate!
            String captchaId = request.getSession().getId();

            //retrieve the response
            String captchResponse = request.getParameter("j_captcha_response");

            // Call the Service method
            try {
                isResponseCorrect = CaptchaServiceSingleton.getInstance()
                                                           .validateResponseForID(captchaId,
                        captchResponse);
            } catch (CaptchaServiceException e) {
                //should not happen, may be thrown if the id is not valid 
            }

            if (!isResponseCorrect.booleanValue()) {
                errors.add("general",
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "general", "general.captcha.error"));
                request.setAttribute("exception", "general.captcha.error");
                doTrace(request, DLog.ERROR, getClass(), "general.captcha.error");
            } else {
                String j_data = request.getParameter("j_data");

                if (StringUtils.isBlank(j_data)) {
                    errors.add("general",
                        new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                            "general", "general.captcha.data.required"));
                    request.setAttribute("exception",
                        "general.captcha.data.required");
                    doTrace(request, DLog.ERROR, getClass(),
                        "general.captcha.data.required");
                } else {
                    Open openInstance = getOpenInstance(request);

                    openInstance.forgotPassword(j_data);
                }
            }
        } catch (Exception ex) {
            String errorMessage = ExceptionUtilities.parseMessage(ex);

            if (errorMessage == null) {
                errorMessage = "NullPointerException";
            }

            errors.add("general",
                new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                    "general", errorMessage));
            request.setAttribute("exception", errorMessage);
            doTrace(request, DLog.ERROR, getClass(), errorMessage);
        }

        if (errors.isEmpty()) {
            doTrace(request, DLog.INFO, getClass(), "OK");

            return mapping.findForward(Constants.ACTION_SUCCESS_FORWARD);
        } else {
            saveErrors(request, errors);

            return mapping.findForward(Constants.ACTION_FAIL_FORWARD);
        }
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doTrace(javax.servlet.http.HttpServletRequest, int, java.lang.Class, java.lang.String)
     */
    protected void doTrace(HttpServletRequest request, int level, Class classe,
        String message) throws Exception {
        DLog.log(level, classe, DMessage.toString(request, message));
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
    protected Open getOpenInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        OpenHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = OpenUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = OpenUtil.getHome(environment);
        }

        return home.create();
    }
}
