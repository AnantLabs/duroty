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
public class SearchObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -6998286622791044887L;

    /**
    * DOCUMENT ME!
    */
    private Vector messages;

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
    public SearchObj() {
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
    public Vector getMessages() {
        return messages;
    }

    /**
     * DOCUMENT ME!
     *
     * @param messages DOCUMENT ME!
     */
    public void setMessages(Vector messages) {
        this.messages = messages;
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
}
