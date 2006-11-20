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


package com.duroty.jmx.mbean;

import org.jboss.system.ServiceMBeanSupport;

import org.jboss.util.naming.NonSerializableFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class DurotyMailFactory extends ServiceMBeanSupport
    implements DurotyMailFactoryMBean {
    /**
     * DOCUMENT ME!
     */
    private String JNDIName;

    /**
     * DOCUMENT ME!
     */
    private String username;

    /**
     * DOCUMENT ME!
     */
    private String password;

    /**
     * DOCUMENT ME!
     */
    private Element config;

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * DOCUMENT ME!
     *
     * @param password DOCUMENT ME!
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * DOCUMENT ME!
     *
     * @param JNDIName DOCUMENT ME!
     */
    public void setJNDIName(String JNDIName) {
        this.JNDIName = JNDIName;
    }

    /**
     * DOCUMENT ME!
     */
    public String getJNDIName() {
        return this.JNDIName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element getConfiguration() {
        return this.config;
    }

    /**
     * DOCUMENT ME!
     *
     * @param element DOCUMENT ME!
     */
    public void setConfiguration(Element element) {
        this.config = element;
    }

    /* (non-Javadoc)
     * @see org.jboss.system.ServiceMBeanSupport#startService()
     */
    protected void startService() throws Exception {
        super.startService();

        //      Setup password authentication
        final PasswordAuthentication pa = new PasswordAuthentication(getUsername(),
                getPassword());

        Authenticator a = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };

        Properties props = getProperties();

        bind(getJNDIName() + "Properties", props);

        // Finally create a mail session
        Session session = Session.getInstance(props, a);

        bind(getJNDIName(), session);
    }

    /* (non-Javadoc)
     * @see org.jboss.system.ServiceMBeanSupport#stopService()
     */
    protected void stopService() throws Exception {
        super.stopService();

        unbind(getJNDIName());
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
            log.info("DurotyMailFactory service '" + name +
                "' removed from JNDI");
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
            Reference ref = new Reference(object.getClass().getName(), addr,
                    NonSerializableFactory.class.getName(), null);
            ctx.bind(n.get(0), ref);
        } finally {
            ctx.close();
        }

        log.info("DurotyMailFactory Service bound to " + name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected Properties getProperties() throws Exception {
        boolean debug = log.isDebugEnabled();

        Properties props = new Properties();

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

                props.setProperty(name, value);

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
}
