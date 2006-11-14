package com.duroty.controller.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.duroty.utils.log.DLog;


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
    	DLog.log(DLog.DEBUG, this.getClass(), "Create Session Id: " + httpSessionEvent.getSession().getId());
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    	DLog.log(DLog.DEBUG, this.getClass(), "Destroy Session Id: " + httpSessionEvent.getSession().getId());
    }
}
