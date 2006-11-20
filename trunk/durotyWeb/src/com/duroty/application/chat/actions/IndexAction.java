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
package com.duroty.application.chat.actions;

import com.duroty.application.chat.exceptions.ChatException;
import com.duroty.application.chat.exceptions.NotAcceptChatException;
import com.duroty.application.chat.exceptions.NotLoggedInException;
import com.duroty.application.chat.exceptions.NotOnlineException;
import com.duroty.application.chat.interfaces.Chat;
import com.duroty.application.chat.utils.ChatDefaultAction;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;

import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;

import org.apache.commons.lang.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public class IndexAction extends ChatDefaultAction {
    /**
     *
     */
    public IndexAction() {
        super();

        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ActionMessages errors = new ActionMessages();

        try {
            Chat chatInstance = getChatInstance(request);

            String call = request.getParameter("call");
            String recipient = request.getParameter("recipient");
            String message = request.getParameter("msg");
            String awayMessage = request.getParameter("awayMessage");
            int away = 0;

            try {
                away = Integer.valueOf(request.getParameter("away"));
            } catch (Exception ex) {
            }

            int state = 0;

            try {
                state = Integer.valueOf(request.getParameter("state"));
            } catch (Exception ex) {
            }

            String result = null;

            if (!StringUtils.isBlank(call)) {
                if (call.equals("send")) {
                    chatInstance.send(recipient, message, 0);
                    result = "sent";
                } else if (call.equals("ping")) {
                    result = chatInstance.ping(away);
                } else if (call.equals("login")) {
                    chatInstance.login();
                } else if (call.equals("logout")) {
                    chatInstance.logout();
                    result = "not_logged_in";
                } else if (call.equals("signin")) {
                    chatInstance.signin();
                    result = "sign_in";
                } else if (call.equals("signout")) {
                    chatInstance.signout();
                    result = "not_online";
                } else if (call.equals("state")) {
                    chatInstance.setState(state, awayMessage);
                }
            } else {
            }

            request.setAttribute("result", result);
        } catch (Exception ex) {
            if (ex instanceof ChatException) {
                if (ex.getCause() instanceof NotOnlineException) {
                    request.setAttribute("result", "not_online");
                } else if (ex.getCause() instanceof NotLoggedInException) {
                    request.setAttribute("result", "not_logged_in");
                } else if (ex.getCause() instanceof NotAcceptChatException) {
                    request.setAttribute("result", "not_accept_chat");
                } else {
                    request.setAttribute("result", ex.getMessage());
                }
            } else {
                String errorMessage = ExceptionUtilities.parseMessage(ex);

                if (errorMessage == null) {
                    errorMessage = "NullPointerException";
                }

                request.setAttribute("result", errorMessage);

                errors.add("general",
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "general", errorMessage));
                request.setAttribute("exception", errorMessage);
                doTrace(request, DLog.ERROR, getClass(), errorMessage);
            }
        } finally {
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
}
