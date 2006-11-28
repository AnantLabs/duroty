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
package com.duroty.application.notebook.actions;

import com.duroty.application.bookmark.interfaces.Bookmark;
import com.duroty.application.bookmark.utils.BookmarkDefaultAction;
import com.duroty.application.bookmark.utils.SearchObj;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;

import com.duroty.utils.Pagination;
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
public class SearchAction extends BookmarkDefaultAction {
    /**
    * DOCUMENT ME!
    */
    private static final String BOOKMARKS = "bookmarks";

    /**
     * DOCUMENT ME!
     */
    private static final String HITS = "hits";

    /**
     * DOCUMENT ME!
     */
    private static final String TIME = "time";

    /**
     * DOCUMENT ME!
     */
    private static final String PAGINATION = "pagination";

    /**
     * DOCUMENT ME!
     */
    private static final String DIDYOUMEAN = "didyoumean";

    /**
     *
     */
    public SearchAction() {
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
            Bookmark bookmarkInstance = getBookmarkInstance(request);

            String extra = request.getParameter("extra");

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

            Integer messagesByPage = new Integer(10);

            int page = _page.intValue();

            if (page > 0) {
                page = page - 1;
            }

            String token = request.getParameter("token");

            String didYouMean = null;

            try {
                didYouMean = bookmarkInstance.didYouMean(token);
            } catch (Exception ex) {
            }

            SearchObj searchObj = bookmarkInstance.search(token, page,
                    messagesByPage.intValue(), order.intValue(), extra, true);

            if ((searchObj == null) && (didYouMean != null)) {
                searchObj = bookmarkInstance.search(didYouMean, page,
                        messagesByPage.intValue(), order.intValue(), extra, true);
                request.setAttribute(DIDYOUMEAN, didYouMean);
            }

            if (searchObj == null) {
                errors.add("general",
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "general", "Data not found"));
                request.setAttribute("exception",
                    ExceptionCode.ERROR_MESSAGES_PREFIX +
                    "general.datanotfound");
            } else {
                request.setAttribute(BOOKMARKS, searchObj.getBookmarks());
                request.setAttribute(HITS, new Integer(searchObj.getHits()));
                request.setAttribute(TIME, new Double(searchObj.getTime()));
                request.setAttribute(PAGINATION,
                    getPagination(request, "search", searchObj.getHits(),
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

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param action DOCUMENT ME!
     * @param hits DOCUMENT ME!
     * @param messagesByPage DOCUMENT ME!
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

        Pagination pagination = new Pagination("Notebook", pag, hits,
                messagesByPage, 10, action, page, order, extra);

        return pagination.getText();
    }
}
