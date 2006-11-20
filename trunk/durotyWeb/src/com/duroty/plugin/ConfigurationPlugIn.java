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


package com.duroty.plugin;

import com.duroty.config.Configuration;

import com.duroty.utils.log.DLog;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletException;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class ConfigurationPlugIn implements PlugIn {
    /**
     * Creates a new instance of ConfigurationPlugIn
     */
    public ConfigurationPlugIn() {
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        Configuration.properties = null;

        DLog.log(DLog.INFO, getClass(), "destroy ConfigurationPlugIn");

        System.gc();
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionServlet DOCUMENT ME!
     * @param moduleConfig DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public void init(ActionServlet actionServlet, ModuleConfig moduleConfig)
        throws ServletException {
        try {
            Configuration.init();
        } catch (Exception e) {
            e.printStackTrace();
            DLog.log(DLog.FATAL, getClass(), e.getMessage());
            throw new ServletException(e);
        }
    }
}
