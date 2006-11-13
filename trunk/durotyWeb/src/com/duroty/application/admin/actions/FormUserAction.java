package com.duroty.application.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.duroty.application.admin.interfaces.Admin;
import com.duroty.application.admin.utils.AdminDefaultAction;
import com.duroty.application.chat.exceptions.ChatException;
import com.duroty.application.chat.exceptions.NotAcceptChatException;
import com.duroty.application.chat.exceptions.NotLoggedInException;
import com.duroty.application.chat.exceptions.NotOnlineException;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class FormUserAction extends AdminDefaultAction {
    /**
     * Creates a new ContactsAction object.
     */
    public FormUserAction() {
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
            Admin adminInstance = getAdminInstance(request); 
            
            request.setAttribute("userRoles", adminInstance.roles());
        } catch (Exception ex) {
        	if (ex instanceof ChatException) {
        		if (ex.getCause() instanceof NotOnlineException) {
        			request.setAttribute("result", "not_online");
        		} else if (ex.getCause() instanceof NotLoggedInException) {
        			request.setAttribute("result", "not_logged_in");
        		} else if (ex.getCause() instanceof NotAcceptChatException) {
        			request.setAttribute("result", "not_accept_chat");
        		} else {
        			request.setAttribute("result", ex.getMessage());
        		}
        	} else {
        		
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
        	}
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
