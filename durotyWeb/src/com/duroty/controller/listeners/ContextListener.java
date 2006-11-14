package com.duroty.controller.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.duroty.utils.log.DLog;


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
    	DLog.log(DLog.DEBUG, this.getClass(), "Create Context Id: " + contextEvent.getServletContext().getServerInfo());
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void contextDestroyed(ServletContextEvent contextEvent) {        
        DLog.log(DLog.DEBUG, this.getClass(), "Destroy Context Id: " + contextEvent.getServletContext().getServerInfo());
    }
}
