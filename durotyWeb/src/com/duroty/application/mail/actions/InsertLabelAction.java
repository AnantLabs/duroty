/**
 *
 */
package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.FilterObj;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.MailDefaultAction;

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
 * @author durot
 *
 */
public class InsertLabelAction extends MailDefaultAction {
    /**
     *
     */
    public InsertLabelAction() {
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
        	Preferences preferencesInstance = getPreferencesInstance(request);
        	
        	DynaActionForm _form = (DynaActionForm) form;

            //label
            LabelObj label = new LabelObj(0, (String) _form.get("name"));

            //filter
            FilterObj filter = new FilterObj();

            String from = (String) _form.get("from");
            String to = (String) _form.get("to");
            String subject = (String) _form.get("subject");
            String hasWords = (String) _form.get("hasWords");
            String doesntHaveWords = (String) _form.get("doesntHaveWords");
            
            if ((from != null && !from.trim().equals("")) || (to != null && !to.trim().equals("")) || (subject != null && !subject.trim().equals("")) || (hasWords != null && !hasWords.trim().equals("")) || (doesntHaveWords != null && !doesntHaveWords.trim().equals(""))) {
            	filter.setFrom(from);
                filter.setTo(to);
                filter.setSubject(subject);
                filter.setHasWords(hasWords);
                filter.setDoesntHaveWords(doesntHaveWords);

                Boolean hasAttachment = (Boolean) _form.get("hasAttachment");
                if (hasAttachment == null) {
                	hasAttachment = new Boolean(false);
                }
                filter.setHasAttachment(hasAttachment.booleanValue());

                Boolean archive = (Boolean) _form.get("archive");
                if (archive == null) {
                	archive = new Boolean(false);
                }
                filter.setArchive(archive.booleanValue());

                Boolean important = (Boolean) _form.get("important");
                if (important == null) {
                	important = new Boolean(false);
                }
                filter.setImportant(important.booleanValue());

                filter.setForward((String) _form.get("forward"));

                Boolean trash = (Boolean) _form.get("trash");
                if (trash == null) {
                	trash = new Boolean(false);
                }
                filter.setTrash(trash.booleanValue());
                
                Boolean orOperator = (Boolean) _form.get("operator");
                if (orOperator == null) {
                	orOperator = new Boolean(false);
                }
                if (orOperator != null) {
                	filter.setOperator(orOperator.booleanValue());
                }
                
                label.addFilter(filter);
            }   
            preferencesInstance.createLabel(label);
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
