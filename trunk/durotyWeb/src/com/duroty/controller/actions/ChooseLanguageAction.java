package com.duroty.controller.actions;

import com.duroty.constants.Constants;
import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;



import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author  jordi marques
 */
public class ChooseLanguageAction extends DefaultAction {
    /** Creates a new instance of LoginAction */
    public ChooseLanguageAction() {
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
        String langReferer = request.getHeader("referer");

        if ((langReferer != null) && !langReferer.equals("")) {
            langReferer = URLEncoder.encode(langReferer, Charset.defaultCharset().displayName());
        } else {
            langReferer = "index.drt";
        }

        request.setAttribute("langReferer", langReferer);
        
        doTrace(request, DLog.INFO, getClass(), "OK");

        return mapping.findForward(Constants.ACTION_SUCCESS_FORWARD);
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

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    }
}
