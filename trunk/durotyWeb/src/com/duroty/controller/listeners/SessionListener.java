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


package com.duroty.controller.listeners;

import com.duroty.utils.log.DLog;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * Listener called when the Session is created or destroyed.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SessionListener implements HttpSessionListener {
    /**
     * Creates a new SessionListener object.
     */
    public SessionListener() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        DLog.log(DLog.DEBUG, this.getClass(),
            "Create Session Id: " + httpSessionEvent.getSession().getId());
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        DLog.log(DLog.DEBUG, this.getClass(),
            "Destroy Session Id: " + httpSessionEvent.getSession().getId());
    }
}
