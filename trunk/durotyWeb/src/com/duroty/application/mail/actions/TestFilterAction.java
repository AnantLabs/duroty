/**
 *
 */
package com.duroty.application.mail.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.Search;
import com.duroty.application.mail.utils.FilterObj;
import com.duroty.application.mail.utils.MailDefaultAction;
import com.duroty.application.mail.utils.PreferencesObj;
import com.duroty.application.mail.utils.SearchObj;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


/**
 * @author durot
 *
 */
public class TestFilterAction extends MailDefaultAction {
    /**
     * DOCUMENT ME!
     */
    private static final String MESSAGES = "messages";

    /**
     * DOCUMENT ME!
     */
    private static final String HITS = "hits";

    /**
     * DOCUMENT ME!
     */
    private static final String PAGINATION = "pagination";

    /**
     * DOCUMENT ME!
     */
    private static final String TEST_FILTER = "testFilter";

    /**
     * Creates a new MessagesAction object.
     */
    public TestFilterAction() {
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
            Search searchInstance = getSearchInstance(request);
            Preferences preferencesInstance = getPreferencesInstance(request);

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

            Integer messagesByPage = new Integer(30);

            PreferencesObj preferences = preferencesInstance.getPreferences();

            if ((preferences != null) && (preferences.getMessagesByPage() > 0)) {
                messagesByPage = new Integer(preferences.getMessagesByPage());
            }

            int page = _page.intValue();

            if (page > 0) {
                page = page - 1;
            }            
            
            FilterObj filterObj = new FilterObj();
            
            filterObj.setDoesntHaveWords(request.getParameter("doesntHaveWords"));
            filterObj.setFrom(request.getParameter("from"));
            
            Boolean hasAttachment = Boolean.parseBoolean(request.getParameter("hasAttachment"));
            filterObj.setHasAttachment(hasAttachment.booleanValue());
            
            filterObj.setHasWords(request.getParameter("hasWords"));
            
            
            filterObj.setSubject(request.getParameter("subject"));
            filterObj.setTo(request.getParameter("to"));
            
            Boolean operator = Boolean.parseBoolean(request.getParameter("operator"));
            filterObj.setOperator(operator);
            
            SearchObj searchObj = searchInstance.testFilter(filterObj, page,
                    messagesByPage.intValue(), order.intValue(), extra);

            if (searchObj == null) {
            	request.setAttribute("action", "testFilter");
                errors.add("general",
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "general", "Data not found"));
                request.setAttribute("exception",
                    ExceptionCode.ERROR_MESSAGES_PREFIX +
                    "general.datanotfound");
            } else {
            	request.setAttribute("action", "testFilter");
                request.setAttribute(MESSAGES, searchObj.getMessages());
                request.setAttribute(HITS, new Integer(searchObj.getHits()));
                request.setAttribute(PAGINATION,
                    getPagination(request, TEST_FILTER, searchObj.getHits(),
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
