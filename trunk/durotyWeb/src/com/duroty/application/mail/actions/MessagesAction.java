/**
 *
 */
package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Mail;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.MailDefaultAction;
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
public class MessagesAction extends MailDefaultAction {
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
     * Creates a new MessagesAction object.
     */
    public MessagesAction() {
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
            Mail mailInstance = getMailInstance(request);
            Preferences preferencesInstance = getPreferencesInstance(request);

            String folder = request.getParameter("folder");
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

            if (folder.startsWith("label")) {            	
            	request.setAttribute("action", "label");
            	
            	int label = Integer.parseInt(folder.substring(5, folder.length()));
            	
                request.setAttribute(MESSAGES,
                    mailInstance.getMessages(label, page,
                        messagesByPage.intValue(), order.intValue(), extra));

                int hits = mailInstance.getCountMessages(label);
                request.setAttribute(HITS, new Integer(hits));
                request.setAttribute(PAGINATION, getPagination(request, folder, hits, messagesByPage.intValue()));
            } else {
            	request.setAttribute("action", "folder");
            	
                request.setAttribute(MESSAGES,
                    mailInstance.getMessages(folder, page,
                        messagesByPage.intValue(), order.intValue(), extra));

                int hits = mailInstance.getCountMessages(folder);
                request.setAttribute(HITS, new Integer(hits));
                request.setAttribute(PAGINATION, getPagination(request, folder, hits, messagesByPage.intValue()));
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
