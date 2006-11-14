/**
 *
 */
package com.duroty.application.mail.actions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.utils.MailDefaultAction;
import com.duroty.application.mail.utils.PreferencesObj;
import com.duroty.config.Configuration;
import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.cookie.CookieManager;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;


/**
 * @author durot
 *
 */
public class UpdateSettingsAction extends MailDefaultAction {
    /**
     *
     */
    public UpdateSettingsAction() {
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
        	Preferences preferencesInstance = getPreferencesInstance(request);
        	
        	PreferencesObj preferencesObj = new PreferencesObj();
        	
            DynaActionForm _form = (DynaActionForm) form;
            
            preferencesObj.setName((String) _form.get("name"));
            preferencesObj.setContactEmail((String) _form.get("contactEmail"));
            
            String language = (String) _form.get("language");
            preferencesObj.setLanguage(language);            
            Cookie cookie = new Cookie(Configuration.properties.getProperty(Configuration.COOKIE_LANGUAGE), language);
            cookie.setMaxAge(15552000);
            CookieManager.setCookie("/", cookie, response);
            
            preferencesObj.setSignature((String) _form.get("signature"));
            Boolean vactionActive = new Boolean(false);
            if (_form.get("vacationResponderActive") != null) {
            	vactionActive = (Boolean) _form.get("vacationResponderActive");
            }
            preferencesObj.setVacationActive(vactionActive.booleanValue());
            preferencesObj.setVacationSubject((String) _form.get("vacationResponderSubject"));
            preferencesObj.setVacationBody((String) _form.get("vacationResponderBody"));
            
            Boolean htmlMessage = new Boolean(false);
            if (_form.get("htmlMessage") != null) {
            	htmlMessage = (Boolean) _form.get("htmlMessage");
            }
            preferencesObj.setHtmlMessage(htmlMessage.booleanValue());
            
            Integer byPage = new Integer(20);
            if (_form.get("byPage") != null) {
            	byPage = (Integer) _form.get("byPage");
            }
            preferencesObj.setMessagesByPage(byPage.intValue());
            
            Boolean spamTolerance = new Boolean(false);
            if (_form.get("spamTolerance") != null) {
            	spamTolerance = (Boolean) _form.get("spamTolerance");
            }
            if (spamTolerance.booleanValue()) {
            	preferencesObj.setSpamTolerance(100);
            } else {
            	preferencesObj.setSpamTolerance(-1);
            }
            
            preferencesInstance.setPreferences(preferencesObj);
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
