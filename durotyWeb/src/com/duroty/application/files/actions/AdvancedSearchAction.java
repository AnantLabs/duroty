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
package com.duroty.application.files.actions;

import com.duroty.application.files.interfaces.FilesSearch;
import com.duroty.application.files.utils.FilesDefaultAction;
import com.duroty.application.files.utils.SearchObj;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.AdvancedObj;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.PreferencesObj;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public class AdvancedSearchAction extends FilesDefaultAction {
    /**
        * DOCUMENT ME!
        */
    private static final String FILES = "files";

    /**
     * DOCUMENT ME!
     */
    private static final String HITS = "hits";

    /**
     * DOCUMENT ME!
     */
    private static final String PAGINATION = "pagination";

    /**
     * Creates a new MessagesAction object.
     */
    public AdvancedSearchAction() {
        super();
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ActionMessages errors = new ActionMessages();

        try {
            FilesSearch filesSearchInstance = getFilesSearchInstance(request);
            Preferences preferencesInstance = getPreferencesInstance(request);

            String orderType = request.getParameter("orderType");

            Integer _page = null;

            try {
                _page = new Integer(request.getParameter("page"));
            } catch (Exception ex) {
                _page = new Integer(0);
            }

            Integer order = null;

            try {
                order = new Integer(request.getParameter("order"));
            } catch (Exception ex) {
                order = new Integer(0);
            }

            Integer messagesByPage = new Integer(30);

            PreferencesObj preferences = preferencesInstance.getPreferences();

            if ((preferences != null) && (preferences.getMessagesByPage() > 0)) {
                messagesByPage = new Integer(preferences.getMessagesByPage());
            }

            int page = _page.intValue();

            if (page > 0) {
                page = page - 1;
            }

            AdvancedObj advancedObj = new AdvancedObj();

            advancedObj.setContentType(request.getParameter("filetype"));
            advancedObj.setEndDate(request.getParameter("endDate"));
            advancedObj.setFixDate(request.getParameter("fixDate"));
            advancedObj.setHasWords(request.getParameter("hasWords"));
            advancedObj.setSubject(request.getParameter("folder"));

            int label = 0;

            try {
                label = Integer.parseInt(request.getParameter("label"));
                advancedObj.setLabel(new LabelObj(label, null));
            } catch (Exception ex) {
            }

            advancedObj.setStartDate(request.getParameter("startDate"));

            advancedObj.setOperator(true);

            SearchObj searchObj = filesSearchInstance.advanced(advancedObj,
                    page, messagesByPage.intValue(), order.intValue(), orderType);

            if (searchObj == null) {
                errors.add("general",
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "general", "Data not found"));
                request.setAttribute("exception",
                    ExceptionCode.ERROR_MESSAGES_PREFIX +
                    "general.datanotfound");
            } else {
                request.setAttribute(FILES, searchObj.getFiles());
                request.setAttribute(HITS, new Integer(searchObj.getHits()));
                request.setAttribute(PAGINATION,
                    getPagination(request, null, searchObj.getHits(),
                        messagesByPage.intValue()));
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
