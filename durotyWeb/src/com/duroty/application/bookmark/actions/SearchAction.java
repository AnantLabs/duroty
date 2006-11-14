/**
 *
 */
package com.duroty.application.bookmark.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.duroty.application.bookmark.interfaces.Bookmark;
import com.duroty.application.bookmark.utils.BookmarkDefaultAction;
import com.duroty.application.bookmark.utils.SearchObj;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


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

            SearchObj searchObj = bookmarkInstance.search(token, page, messagesByPage.intValue(), order.intValue(), extra, false);

            if ((searchObj == null) && (didYouMean != null)) {
                searchObj = bookmarkInstance.search(didYouMean, page, messagesByPage.intValue(), order.intValue(), extra, false);
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
                request.setAttribute(PAGINATION, getPagination(request, "search", searchObj.getHits(), messagesByPage.intValue()));
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
