/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;

import java.util.Vector;


/**
 * @author durot
 *
 */
public class SearchContactsObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 24833476991875557L;

    /**
    * DOCUMENT ME!
    */
    private Vector contacts;

    /**
     * DOCUMENT ME!
     */
    private int hits;

    /**
     * DOCUMENT ME!
     */
    private double time;

    /**
     *
     */
    public SearchContactsObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHits() {
        return hits;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hits DOCUMENT ME!
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getContacts() {
        return contacts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contacts DOCUMENT ME!
     */
    public void setContacts(Vector contacts) {
        this.contacts = contacts;
    }
}
