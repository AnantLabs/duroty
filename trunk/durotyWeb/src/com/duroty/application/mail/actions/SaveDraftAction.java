/**
 *
 */
package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.Send;
import com.duroty.application.mail.utils.MailDefaultAction;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;

import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.io.File;

import java.nio.charset.Charset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public class SaveDraftAction extends MailDefaultAction {
    /**
     * Creates a new SaveDraftAction object.
     */
    public SaveDraftAction() {
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
            boolean isMultipart = FileUpload.isMultipartContent(request);

            if (isMultipart) {
                Map fields = new HashMap();
                Vector attachments = new Vector();

                //Parse the request
                List items = diskFileUpload.parseRequest(request);

                //Process the uploaded items
                Iterator iter = items.iterator();

                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    if (item.isFormField()) {
                        fields.put(item.getFieldName(), item.getString());
                    } else {
                        File dir = new File(System.getProperty("user.home") +
                                File.separator + "tmp");

                        if (!dir.exists()) {
                            dir.mkdir();
                        }

                        File out = new File(dir, item.getName());
                        item.write(out);

                        attachments.addElement(out);
                    }
                }

                String body = "";

                if (fields.get("taBody") != null) {
                    body = (String) fields.get("taBody");
                } else if (fields.get("taReplyBody") != null) {
                    body = (String) fields.get("taReplyBody");
                }

                Preferences preferencesInstance = getPreferencesInstance(request);

                Send sendInstance = getSendInstance(request);

                String mid = (String) fields.get("mid");

                sendInstance.saveDraft(mid,
                    Integer.parseInt((String) fields.get("identity")),
                    (String) fields.get("to"), (String) fields.get("cc"),
                    (String) fields.get("bcc"), (String) fields.get("subject"),
                    body, attachments,
                    preferencesInstance.getPreferences().isHtmlMessage(),
                    Charset.defaultCharset().displayName(),
                    (String) fields.get("priority"));
            } else {
                errors.add("general",
                    new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                        "mail.send", "The form is null"));
                request.setAttribute("exception", "The form is null");
                request.setAttribute("newLocation", null);
                doTrace(request, DLog.ERROR, getClass(), "The form is null");
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
