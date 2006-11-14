/**
 *
 */
package com.duroty.jmx.mbean;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.jboss.system.ServiceMBeanSupport;
import org.jboss.util.naming.NonSerializableFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author durot
 *
 */
public class MailConfig extends ServiceMBeanSupport implements MailConfigMBean {
    /**
     * DOCUMENT ME!
     */
    private Element config;

    /* (non-Javadoc)
     * @see com.duroty.jmx.mbean.MailConfigMBean#getConfiguration()
     */
    public Element getConfiguration() {
        return config;
    }

    /* (non-Javadoc)
     * @see com.duroty.jmx.mbean.MailConfigMBean#setConfiguration(org.w3c.dom.Element)
     */
    public void setConfiguration(Element element) {
        this.config = element;
    }

    /* (non-Javadoc)
     * @see org.jboss.system.ServiceMBeanSupport#startService()
     */
    protected void startService() throws Exception {
        super.startService();

        HashMap properties = getProperties();

        bind((String) ApplicationConstants.options.get(Constants.MAIL_CONFIG), properties);
    }

    /* (non-Javadoc)
     * @see org.jboss.system.ServiceMBeanSupport#stopService()
     */
    protected void stopService() throws Exception {
        super.stopService();

        unbind((String) ApplicationConstants.options.get(Constants.MAIL_CONFIG));
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     */
    private void unbind(String name) throws NamingException {
        if (name != null) {
            InitialContext ctx = new InitialContext();

            try {
                ctx.unbind(name);
            } finally {
                ctx.close();
            }

            NonSerializableFactory.unbind(name);
            log.info("MailConfig service '" + name + "' removed from JNDI");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     *
     * @throws NamingException DOCUMENT ME!
     */
    private void bind(String name, Object object) throws NamingException {
        // Ah ! Session isn't serializable, so we use a helper class
        NonSerializableFactory.bind(name, object);

        Context ctx = new InitialContext();

        try {
            Name n = ctx.getNameParser("").parse(name);

            while (n.size() > 1) {
                String ctxName = n.get(0);

                try {
                    ctx = (Context) ctx.lookup(ctxName);
                } catch (NameNotFoundException e) {
                    ctx = ctx.createSubcontext(ctxName);
                }

                n = n.getSuffix(1);
            }
            StringRefAddr addr = new StringRefAddr("nns", name);
            Reference ref = new Reference(object.getClass().getName(), addr, NonSerializableFactory.class.getName(), null);
            ctx.bind(n.get(0), ref);
        } finally {
            ctx.close();
        }

        log.info("MailConfig Service bound to " + name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected HashMap getProperties() throws Exception {
        boolean debug = log.isDebugEnabled();

        HashMap props = new HashMap();

        if (config == null) {
            log.warn("No configuration specified; using empty properties map");

            return props;
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

                setUnlessNull(props, name, value);

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
            log.debug("Using properties: " + props);
        }

        return props;
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
