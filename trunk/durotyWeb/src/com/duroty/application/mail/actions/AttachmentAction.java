/**
 *
 */
package com.duroty.application.mail.actions;

import com.duroty.application.mail.interfaces.Mail;
import com.duroty.application.mail.interfaces.MailHome;
import com.duroty.application.mail.interfaces.MailUtil;

import com.duroty.config.Configuration;

import com.duroty.controller.actions.DownloadAction;

import com.duroty.utils.log.DLog;
import com.duroty.utils.log.DMessage;
import com.duroty.utils.mail.MailPart;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.io.IOException;
import java.io.InputStream;

import java.util.Hashtable;

import javax.mail.MessagingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public class AttachmentAction extends DownloadAction {
    /**
     * Creates a new AttachmentAction object.
     */
    public AttachmentAction() {
        super();
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DownloadAction#getStreamInfo(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        MailHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = MailUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = MailUtil.getHome(environment);
        }

        Mail mailInstance = home.create();

        String mid = request.getParameter("mid");
        String part = request.getParameter("part");

        return new SI(mailInstance, mid, part);
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doTrace(javax.servlet.http.HttpServletRequest, int, java.lang.Class, java.lang.String)
     */
    protected void doTrace(HttpServletRequest request, int level, Class classe,
        String message) throws Exception {
        DLog.log(level, classe, DMessage.toString(request, message));
    }

    /**
     * This class represents the pertinent details about the file to be
     * downloaded.
     *
     */
    public static class SI implements StreamInfo {
        /**
         * DOCUMENT ME!
         */
        private MailPart part;

        /**
         * Creates a new SI object.
         *
         * @param loginInstance DOCUMENT ME!
         * @param dmailInstance DOCUMENT ME!
         * @param uid DOCUMENT ME!
         * @param hash DOCUMENT ME!
         */
        public SI(Mail mailInstance, String mid, String hash) {
            try {
                this.part = mailInstance.getAttachment(mid, hash);
            } catch (Exception e) {
                this.part = null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getContentType() {
            if (part != null) {
                String contenttype = null;

                if ((part.getContentType() != null) &&
                        (part.getContentType().equalsIgnoreCase("message/rfc822") ||
                        part.getContentType().equalsIgnoreCase("message/delivery-status"))) {
                    contenttype = "text/html";
                } else {
                    contenttype = part.getContentType();
                }

                return contenttype;
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IOException DOCUMENT ME!
         */
        public InputStream getInputStream() throws IOException {
            if (part != null) {
                try {
                    return part.getPart().getInputStream();
                } catch (MessagingException e) {
                }
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getName() {
            if (part != null) {
                return part.getName();
            }

            return null;
        }
    }
}
