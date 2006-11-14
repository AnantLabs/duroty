package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.FilterObj;
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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class FormUpdateFilterAction extends MailDefaultAction {
    /**
     * Creates a new FormFilterAction object.
     */
    public FormUpdateFilterAction() {
        super();

        // TODO Auto-generated constructor stub
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
            Preferences preferencesInstance = getPreferencesInstance(request);
            
            request.setAttribute("labels", preferencesInstance.getLabels());

            DynaActionForm _form = (DynaActionForm) form;

            Integer idint = Integer.parseInt(request.getParameter("idint"));
            FilterObj filter = preferencesInstance.getFilter(idint);

            _form.set("idint", filter.getIdint());
            _form.set("label", filter.getLabel().getIdint());
            _form.set("name", filter.getLabel().getName());
            _form.set("from", filter.getFrom());
            _form.set("to", filter.getTo());
            _form.set("subject", filter.getSubject());
            _form.set("hasWords", filter.getHasWords());
            _form.set("doesntHaveWords", filter.getDoesntHaveWords());
            _form.set("hasAttachment", filter.isHasAttachment());
            _form.set("archive", filter.isArchive());
            _form.set("important", filter.isImportant());
            _form.set("forward", filter.getForward());
            _form.set("trash", filter.isTrash());
            _form.set("operator", filter.isOperator());
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
