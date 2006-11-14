/**
 *
 */
package com.duroty.application.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.duroty.application.admin.interfaces.Admin;
import com.duroty.application.admin.utils.AdminDefaultAction;
import com.duroty.application.admin.utils.SearchUsersObj;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.Pagination;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


/**
 * @author durot
 *
 */
public class SearchUsersAction extends AdminDefaultAction {
    /**
     * DOCUMENT ME!
     */
    private static final String SEARCH_USERS = "searchUsers";

    /**
     * DOCUMENT ME!
     */
    private static final String USERS = "durotyUsers";

    /**
     * DOCUMENT ME!
     */
    private static final String HITS = "hits";

    /**
     * DOCUMENT ME!
     */
    private static final String PAGINATION = "pagination";

    /**
     *
     */
    public SearchUsersAction() {
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
            Admin adminInstance = getAdminInstance(request);

            

            Integer _page = null;

            try {
                _page = new Integer(request.getParameter("page"));
            } catch (Exception ex) {
                _page = new Integer(0);
            }

            Integer byPage = null;

            try {
            	byPage = new Integer(request.getParameter("byPage"));
            } catch (Exception ex) {
            	byPage = new Integer(0);
            }
            

            int page = _page.intValue();

            if (page > 0) {
                page = page - 1;
            }

            String token = request.getParameter("token");

            SearchUsersObj sobj = null;

            if (StringUtils.isBlank(token)) {
                sobj = adminInstance.allUsers(page, byPage.intValue());
            } else {
                sobj = adminInstance.suggestUsers(token, page, byPage.intValue());
            }

            if (sobj != null) {
                request.setAttribute(USERS, sobj.getUsers());
                request.setAttribute(HITS, new Integer(sobj.getHits()));
                request.setAttribute(PAGINATION,
                    getPagination(request, SEARCH_USERS, sobj.getHits(),
                    		byPage.intValue()));
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

        Pagination pagination = new Pagination("Contacts", pag, hits,
                messagesByPage, 10, action, page, order, extra);

        return pagination.getText();
    }
}
