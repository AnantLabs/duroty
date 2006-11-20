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
import com.duroty.application.mail.utils.FilterObj;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.MailDefaultAction;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;

import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class InsertFilterAction extends MailDefaultAction {
    /**
     * Creates a new FormFilterAction object.
     */
    public InsertFilterAction() {
        super();

        // TODO Auto-generated constructor stub
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

            DynaActionForm _form = (DynaActionForm) form;

            FilterObj filter = new FilterObj();

            Boolean archive = (Boolean) _form.get("archive");

            if (archive == null) {
                archive = new Boolean(false);
            }

            filter.setArchive(archive.booleanValue());

            filter.setDoesntHaveWords((String) _form.get("doesntHaveWords"));
            filter.setForward((String) _form.get("forward"));
            filter.setFrom((String) _form.get("from"));

            Boolean hasAttachment = (Boolean) _form.get("hasAttachment");

            if (hasAttachment == null) {
                hasAttachment = new Boolean(false);
            }

            filter.setHasAttachment(hasAttachment.booleanValue());

            filter.setHasWords((String) _form.get("hasWords"));

            Boolean important = (Boolean) _form.get("important");

            if (important == null) {
                important = new Boolean(false);
            }

            filter.setImportant(important.booleanValue());

            Integer label = (Integer) _form.get("label");
            int labIdint = 0;

            if (label != null) {
                labIdint = label.intValue();
            }

            String name = (String) _form.get("name");

            if ((name != null) && !name.trim().equals("")) {
                filter.setLabel(new LabelObj(0, (String) _form.get("name")));
            } else {
                filter.setLabel(new LabelObj(labIdint,
                        (String) _form.get("name")));
            }

            Boolean operator = (Boolean) _form.get("operator");

            if (operator == null) {
                operator = new Boolean(false);
            }

            filter.setOperator(operator.booleanValue());

            filter.setSubject((String) _form.get("subject"));
            filter.setTo((String) _form.get("to"));

            Boolean trash = (Boolean) _form.get("trash");

            if (trash == null) {
                trash = new Boolean(false);
            }

            filter.setTrash(trash.booleanValue());

            preferencesInstance.createFilter(filter);
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
