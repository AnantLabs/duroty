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


package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.Send;
import com.duroty.application.mail.utils.IdentityObj;
import com.duroty.application.mail.utils.MailDefaultAction;
import com.duroty.application.mail.utils.PreferencesObj;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;

import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;

import org.apache.commons.io.IOUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class CreateIdentityAction extends MailDefaultAction {
    /**
     * Creates a new CreateIdentityAction object.
     */
    public CreateIdentityAction() {
        super();
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

        try {
            Preferences preferencesInstance = getPreferencesInstance(request);

            PreferencesObj preferencesObj = preferencesInstance.getPreferences();

            DynaActionForm _form = (DynaActionForm) form;

            IdentityObj identityObj = new IdentityObj();

            identityObj.setEmail((String) _form.get("email"));

            Boolean important = (Boolean) _form.get("default");

            if (important == null) {
                important = new Boolean(false);
            }

            identityObj.setImportant(important.booleanValue());
            identityObj.setName((String) _form.get("name"));
            identityObj.setReplyTo((String) _form.get("replyTo"));

            String code = preferencesInstance.createIdentity(identityObj);

            String body = getEmailBody(request, preferencesObj.getLanguage());

            MessageResources message = getResources(request);
            String subject = message.getMessage("general.title");

            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" +
                request.getServerName() + ":" + request.getServerPort() + path +
                "/mail/preferences/validateIdentity.drt?user=" +
                request.getUserPrincipal() + "&code=" + code;

            body = body.replaceAll("\\$\\{email\\}", identityObj.getEmail());
            body = body.replaceAll("\\$\\{url\\}", basePath);

            IdentityObj identityDefault = preferencesInstance.getIdentityDefault();

            Send sendInstance = getSendInstance(request);
            sendInstance.sendIdentity(identityDefault.getEmail(),
                identityObj.getEmail(), subject, body);
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

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws URISyntaxException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    protected String getEmailBody(HttpServletRequest request, String language)
        throws IOException {
        FileInputStream input = null;

        try {
            File file = new File(getServlet().getServletContext().getRealPath("/email/email_" +
                        language + ".html"));
            input = new FileInputStream(file);

            return IOUtils.toString(input);
        } finally {
            IOUtils.closeQuietly(input);
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
