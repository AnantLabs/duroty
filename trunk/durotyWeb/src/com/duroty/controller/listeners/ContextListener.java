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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ContextListener implements ServletContextListener {
    /**
     * Creates a new ContextListener object.
     */
    public ContextListener() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void contextInitialized(ServletContextEvent contextEvent) {
        DLog.log(DLog.DEBUG, this.getClass(),
            "Create Context Id: " +
            contextEvent.getServletContext().getServerInfo());
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void contextDestroyed(ServletContextEvent contextEvent) {
        DLog.log(DLog.DEBUG, this.getClass(),
            "Destroy Context Id: " +
            contextEvent.getServletContext().getServerInfo());
    }
}
