package com.duroty.application.bookmark.actions;

import com.duroty.application.bookmark.interfaces.Bookmark;
import com.duroty.application.bookmark.utils.BookmarkDefaultAction;
import com.duroty.application.bookmark.utils.BookmarkObj;

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
public class FormBookmarkAction extends BookmarkDefaultAction {
    /**
     * Creates a new ContactsAction object.
     */
    public FormBookmarkAction() {
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
            DynaActionForm _form = (DynaActionForm) form;

            Bookmark bookmarkInstance = getBookmarkInstance(request);

            BookmarkObj bookmarkObj = bookmarkInstance.getBookmark(request.getParameter(
                        "idint"));

            _form.set("idint", bookmarkObj.getIdint());
            _form.set("url", bookmarkObj.getUrl());
            _form.set("title", bookmarkObj.getTitle());
            _form.set("comments", bookmarkObj.getComments());
            _form.set("keywords", bookmarkObj.getKeywords());
            _form.set("flagged", new Boolean(bookmarkObj.isFlagged()));
            
            request.setAttribute("keywords", bookmarkInstance.getKeywords());
        } catch (Exception ex) {
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
