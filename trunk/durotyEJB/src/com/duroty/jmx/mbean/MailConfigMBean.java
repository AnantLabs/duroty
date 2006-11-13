package com.duroty.jmx.mbean;

import org.jboss.system.ServiceMBean;


/**
 * @author durot
 *
 */
public interface MailConfigMBean extends ServiceMBean {
    /**
        * Configuration for the mail service.
        */
    public org.w3c.dom.Element getConfiguration();

    /**
     * Configuration for the mail service.
     */
    public void setConfiguration(org.w3c.dom.Element element);
}
