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
package com.duroty.application.chat.utils;

import com.duroty.application.mail.interfaces.Preferences;
import com.duroty.application.mail.interfaces.PreferencesHome;
import com.duroty.application.mail.interfaces.PreferencesUtil;

import com.duroty.config.Configuration;

import com.duroty.controller.actions.DefaultAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.rmi.RemoteException;

import java.util.Hashtable;

import javax.ejb.CreateException;

import javax.naming.NamingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author durot
 *
 */
public abstract class ChatDefaultAction extends DefaultAction {
    /**
     * Creates a new ChatDefaultAction object.
     */
    public ChatDefaultAction() {
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
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
    protected Preferences getPreferencesInstance(HttpServletRequest request)
        throws NamingException, RemoteException, CreateException {
        PreferencesHome home = null;

        Boolean localServer = new Boolean(Configuration.properties.getProperty(
                    Configuration.LOCAL_WEB_SERVER));

        if (localServer.booleanValue()) {
            home = PreferencesUtil.getHome();
        } else {
            Hashtable environment = getContextProperties(request);
            home = PreferencesUtil.getHome(environment);
        }

        return home.create();
    }
}
