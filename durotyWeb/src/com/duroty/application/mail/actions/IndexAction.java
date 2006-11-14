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
import com.duroty.application.mail.utils.SearchGroupsObj;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.session.SessionManager;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


/**
 * @author durot
 *
 */
public class IndexAction extends MailDefaultAction {
    /**
     * DOCUMENT ME!
     */
    private static final String FOLDERS = "folders";

    /**
     * DOCUMENT ME!
     */
    private static final String LABELS = "labels";

    /**
     * DOCUMENT ME!
     */
    private static final String IDENTITIES = "identities";

    /**
     * DOCUMENT ME!
     */
    private static final String PREFERENCES = "preferences";
    
    private static final String CONTACT_LIST = "groups";

    /**
     * Creates a new IndexAction object.
     */
    public IndexAction() {
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

            SessionManager.setObject(FOLDERS, mailInstance.getFolders(), request);
            SessionManager.setObject(LABELS, mailInstance.getLabels(), request);
            
            SessionManager.setObject(IDENTITIES, preferencesInstance.getIdentities(), request);
            
            PreferencesObj obj = preferencesInstance.getPreferences();
            
            if (obj.isHtmlMessage()) {
            	if (obj.getSignature() != null) {
            		obj.setSignature("<br/><p>" + obj.getSignature().replaceAll("\n", "<br/>").replaceAll("\r", "").replaceAll("'", "\\'") + "</p>");
            	}
            } else {
            	if (obj.getSignature() != null) {
            		obj.setSignature(obj.getSignature().replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("'", "\\'"));
            	}            	
            }
            
            request.setAttribute(PREFERENCES, obj);
            
            SearchGroupsObj sobj = preferencesInstance.getGroups(0, 0, 0, null);
            if (sobj != null) {
            	request.setAttribute(CONTACT_LIST, sobj.getGroups());
            }
            
            SessionManager.setObject("user", request.getUserPrincipal().getName(), request);
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
