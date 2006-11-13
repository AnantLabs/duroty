package com.duroty.jmx.mbean;

import org.jboss.system.ServiceMBean;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface ApplicationConstantsMBean extends ServiceMBean {
    /**
    * Configuration for the application service.
    */
    org.w3c.dom.Element getConfiguration();

    /**
     * Configuration for the application service.
     */
    void setConfiguration(org.w3c.dom.Element element);
}
