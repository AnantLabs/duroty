/**
 *
 */
package com.duroty.application.bookmark.actions;

import com.duroty.application.bookmark.interfaces.Bookmark;
import com.duroty.application.bookmark.utils.BookmarkDefaultAction;
import com.duroty.application.bookmark.utils.BookmarkObj;

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
import org.apache.struts.action.DynaActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public class InsertBookmarkAction extends BookmarkDefaultAction {
    /**
     *
     */
    public InsertBookmarkAction() {
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
            DynaActionForm _form = (DynaActionForm) form;

            Bookmark bookmarkInstance = getBookmarkInstance(request);

            BookmarkObj bookmarkObj = new BookmarkObj();
            bookmarkObj.setUrl(_form.getString("url"));
            bookmarkObj.setTitle(_form.getString("title"));
            bookmarkObj.setComments(_form.getString("comments"));
            bookmarkObj.setKeywords(_form.getString("keywords"));

            Boolean flagged = (Boolean) _form.get("flagged");

            if (flagged == null) {
                flagged = new Boolean(false);
            }

            bookmarkObj.setFlagged(flagged.booleanValue());
            
            if (!StringUtils.isBlank(_form.getString("comments"))) {
            	bookmarkObj.setNotebook(true);
            } else {
            	bookmarkObj.setNotebook(false);
            }

            bookmarkInstance.addBookmark(bookmarkObj);
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
