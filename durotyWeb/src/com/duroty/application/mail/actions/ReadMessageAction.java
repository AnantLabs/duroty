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

import com.duroty.application.mail.interfaces.Mail;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.MailDefaultAction;
import com.duroty.application.mail.utils.PreferencesObj;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


/**
 * @author durot
 *
 */
public class ReadMessageAction extends MailDefaultAction {
    /**
     * DOCUMENT ME!
     */
    private static final String MESSAGE = "message";
    
    /**
     * DOCUMENT ME!
     */
    private static final String IDENTITIES = "identities";

    /**
     * DOCUMENT ME!
     */
    private static final String PREFERENCES = "preferences";

    /**
     * Creates a new ReadMessageAction object.
     */
    public ReadMessageAction() {
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

            String mid = request.getParameter("mid");
            //String folder = request.getParameter("folder");
            Boolean displayImages = Boolean.parseBoolean(request.getParameter("displayImages"));

            PreferencesObj preferences = preferencesInstance.getPreferences();

            request.setAttribute(MESSAGE, mailInstance.getMessage(mid, true, preferences.isHtmlMessage(), displayImages.booleanValue()));
            request.setAttribute(IDENTITIES, preferencesInstance.getIdentities());
            request.setAttribute(PREFERENCES, preferencesInstance.getPreferences());
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
