/**
 *
 */
package com.duroty.jmx.mbean;

import java.util.HashMap;
import java.util.Map;

import org.jboss.system.ServiceMBeanSupport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author durot
 *
 */
public class ApplicationConstants extends ServiceMBeanSupport
    implements ApplicationConstantsMBean {
    /**
     * Aquests atributes seran insertats en un Java Map public static per tal de poder accedir
     * a les constants de cada mbean per jndi. Els atributs contenen el nom jndi d'acces als map
     * de configuració de cada mbean
     */
    public static HashMap options = new HashMap();

    /**
    * DOCUMENT ME!
    */
    private Element config;

    /* (non-Javadoc)
     * @see com.duroty.jmx.mbean.ApplicationConstantsMBean#getConfiguration()
     */
    public Element getConfiguration() {
        return config;
    }

    /* (non-Javadoc)
     * @see com.duroty.jmx.mbean.ApplicationConstantsMBean#setConfiguration(org.w3c.dom.Element)
     */
    public void setConfiguration(Element element) {
        this.config = element;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void startService() throws Exception {
        super.startService();

        boolean debug = log.isDebugEnabled();

        if (config == null) {
            log.warn("No configuration specified; using empty properties map");

            return;
        }

        NodeList list = config.getElementsByTagName("property");
        int len = list.getLength();

        for (int i = 0; i < len; i++) {
            Node node = list.item(i);

            switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:

                Element child = (Element) node;
                String name;
                String value;

                // get the name
                if (child.hasAttribute("name")) {
                    name = child.getAttribute("name");
                } else {
                    log.warn(
                        "Ignoring invalid element; missing 'name' attribute: " +
                        child);

                    break;
                }

                // get the value
                if (child.hasAttribute("value")) {
                    value = child.getAttribute("value");
                } else {
                    log.warn(
                        "Ignoring invalid element; missing 'value' attribute: " +
                        child);

                    break;
                }

                if (debug) {
                    log.debug("setting property " + name + "=" + value);
                }

                setUnlessNull(options, name, value);

                break;

            case Node.COMMENT_NODE:

                // ignore
                break;

            default:
                log.debug("ignoring unsupported node type: " + node);

                break;
            }
        }

        if (debug) {
            log.debug("Using properties: " + options);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void stopService() throws Exception {
        super.stopService();
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    private void setUnlessNull(Map map, String key, String value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
