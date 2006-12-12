/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/
/**
 *
 */
package com.duroty.application.mail.actions;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.duroty.application.mail.interfaces.Mail;
import com.duroty.application.mail.interfaces.MailHome;
import com.duroty.application.mail.interfaces.MailUtil;
import com.duroty.application.mail.utils.MailPartObj;
import com.duroty.config.Configuration;
import com.duroty.constants.Constants;
import com.duroty.controller.actions.DownloadAction;
import com.duroty.session.SessionManager;


/**
 * @author durot
 *
 */
public class AttachmentAction extends DownloadAction {
    /**
         *
         */
    private static final long serialVersionUID = 1631746723406641292L;

    /**
    * Creates a new AttachmentAction object.
    */
    public AttachmentAction() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    protected void doDownload(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        DataInputStream in = null;
        ByteArrayInputStream bais = null;
        ServletOutputStream op = null;

        try {
            String mid = request.getParameter("mid");
            String part = request.getParameter("part");

            Mail filesInstance = getMailInstance(request);

            MailPartObj obj = filesInstance.getAttachment(mid, part);

            int length = 0;
            op = response.getOutputStream();

            String mimetype = obj.getContentType();

            //
            //  Set the response and go!
            //
            //  Yes, I know that the RFC says 'attachment'.  Unfortunately, IE has a typo
            //  in it somewhere, and Netscape seems to accept this typing as well.
            //
            response.setContentType((mimetype != null) ? mimetype
                                                       : "application/octet-stream");
            response.setContentLength((int) obj.getSize());
            response.setHeader("Content-Disposition",
                "attachement; filename=\"" + obj.getName() + "\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");

            //
            //  Stream to the requester.
            //
            byte[] bbuf = new byte[1024];
            bais = new ByteArrayInputStream(obj.getAttachent());
            in = new DataInputStream(bais);

            while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                op.write(bbuf, 0, length);
            }
        } catch (Exception ex) {
        } finally {
            try {
                op.flush();
            } catch (Exception ex) {
            }

            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(op);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     * @throws CreateException DOCUMENT ME!
     */
    protected Mail getMailInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        MailHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = MailUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = MailUtil.getHome(environment);
        }

        return home.create();
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Hashtable getContextProperties(HttpServletRequest request) {
        Hashtable props = (Hashtable) SessionManager.getObject(Constants.CONTEXT_PROPERTIES,
                request);

        if (props == null) {
            props = new Hashtable();

            props.put(Context.INITIAL_CONTEXT_FACTORY,
                Configuration.properties.getProperty(
                    Configuration.JNDI_INITIAL_CONTEXT_FACTORY));
            props.put(Context.URL_PKG_PREFIXES,
                Configuration.properties.getProperty(
                    Configuration.JNDI_URL_PKG_PREFIXES));
            props.put(Context.PROVIDER_URL,
                Configuration.properties.getProperty(
                    Configuration.JNDI_PROVIDER_URL));

            Principal principal = request.getUserPrincipal();
            props.put(Context.SECURITY_PRINCIPAL, principal.getName());
            props.put(Context.SECURITY_CREDENTIALS,
                SessionManager.getObject(Constants.JAAS_PASSWORD, request));

            props.put(Context.SECURITY_PROTOCOL,
                Configuration.properties.getProperty(
                    Configuration.SECURITY_PROTOCOL));

            SessionManager.setObject(Constants.CONTEXT_PROPERTIES, props,
                request);
        }

        return props;
    }
}
