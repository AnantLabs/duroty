package com.duroty.application.files.actions;

import com.duroty.application.files.interfaces.Files;
import com.duroty.application.files.utils.FilesDefaultAction;
import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.PreferencesObj;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class GetFilesAction extends FilesDefaultAction {
    /**
        * DOCUMENT ME!
        */
    private static final String FILES = "files";

    /**
     * Creates a new GetFiles object.
     */
    public GetFilesAction() {
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
            Files filesInstance = getFilesInstance(request);
            Preferences preferencesInstance = getPreferencesInstance(request);

            String folder = request.getParameter("folder");
            String orderType = request.getParameter("orderType");
            
            if (StringUtils.isBlank(orderType)) {
            	orderType = "ASC";
            }

            Integer label = null;

            try {
                label = new Integer(request.getParameter("label"));
            } catch (Exception ex) {
                label = new Integer(0);
            }

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

            request.setAttribute(FILES,
                filesInstance.getFiles(folder, label.intValue(),
                    page, messagesByPage.intValue(),
                    order.intValue(), orderType));
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
