package com.duroty.config;

import java.net.URL;

import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class Configuration {
	public static final String COOKIE_LANGUAGE = "COOKIE_LANGUAGE";
	public static final String COOKIE_MAX_AGE = "COOKIE_MAX_AGE";
	public static final String COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD = "COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD";
	public static final String AVAILABLE_LANGUAGE = "AVAILABLE_LANGUAGE";
	public static final String AUTO_LOCALE = "AUTO_LOCALE";
	public static final String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
	public static final String JNDI_INITIAL_CONTEXT_FACTORY = "JNDI_INITIAL_CONTEXT_FACTORY";
	public static final String JNDI_URL_PKG_PREFIXES = "JNDI_URL_PKG_PREFIXES";
	public static final String JNDI_PROVIDER_URL = "JNDI_PROVIDER_URL";
	public static final String SECURITY_PROTOCOL = "SECURITY_PROTOCOL";
	public static final String INDEX_URI = "INDEX_URI";
	public static final String LOCAL_WEB_SERVER = "LOCAL_WEB_SERVER";
    /**
     * DOCUMENT ME!
     */
    public static Properties properties = new Properties();

    /**
     * Creates a new Configuration object.
     */
    private Configuration() {
    }

    /**
     * DOCUMENT ME!
     */
    public static void init() {
        try {
            URL url = Configuration.class.getResource("/com/duroty/config/constants.properties");

            properties.load(url.openConnection().getInputStream());
        } catch (Exception ex) {
        }
    }
}
