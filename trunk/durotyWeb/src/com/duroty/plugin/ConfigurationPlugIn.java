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
