package com.duroty.utils.http;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;


/**
 * A simple class to store attribute value pairs
 *
 */
public class AttribValuePair {
    /** DOCUMENT ME! */
    private String attrib;

    /** DOCUMENT ME! */
    private String value;

    /** DOCUMENT ME! */
    private boolean ignoreAttribCase = false;

    /**
     * empty constructor that does nothing
     */
    public AttribValuePair() {
    }

    /**
     * initializes object using an attribute and its values
     *
     * @param attrib DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public AttribValuePair(String attrib, String value) {
        this.attrib = attrib;
        this.value = value;
    }

    /**
     * inializes object using attrib=value string
     *
     * @param attribAndValue DOCUMENT ME!
     */
    public AttribValuePair(String attribAndValue) {
        setAttribAndValue(attribAndValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ignore DOCUMENT ME!
     */
    public void setIgnoreAttribCase(boolean ignore) {
        this.ignoreAttribCase = ignore;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getIgnoreAttribCase() {
        return ignoreAttribCase;
    }

    /**
     * set the attrib and value using an attrib=value string
     *
     * @param attribAndValue DOCUMENT ME!
     */
    protected void setAttribAndValue(String attribAndValue) {
        int pos = 0;
        pos = attribAndValue.indexOf("=");

        if (pos == -1) {
            attrib = attribAndValue;
        } else {
            attrib = attribAndValue.substring(0, pos).trim();
            value = attribAndValue.substring(pos + 1).trim();

            if (value.startsWith("\"") || value.startsWith("'")) {
                value = value.substring(1);
            }

            if (value.endsWith("\"") || value.endsWith("'")) {
                value = value.substring(0, value.length() - 1);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAttrib() {
        if (ignoreAttribCase) {
            return attrib.toLowerCase();
        } else {
            return attrib;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws UnsupportedEncodingException
     */
    public String toEncodedString() throws UnsupportedEncodingException {
        return URLEncoder.encode(attrib, "UTF-8") + "=" +
        URLEncoder.encode(value, "UTF-8");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return attrib + "=\"" + value + "\"";
    }
}
