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
public class Counters implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -1133170332340442595L;

    /**
    * DOCUMENT ME!
    */
    private int inbox;

    /**
     * DOCUMENT ME!
     */
    private int spam;

    /**
     * DOCUMENT ME!
     */
    private Vector labels = new Vector();

    /**
     * DOCUMENT ME!
     */
    private String quota;

    /**
    * Creates a new Counters object.
    */
    public Counters() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getQuota() {
        return quota;
    }

    /**
     * DOCUMENT ME!
     *
     * @param quota DOCUMENT ME!
     */
    public void setQuota(String quota) {
        this.quota = quota;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInbox() {
        return inbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inbox DOCUMENT ME!
     */
    public void setInbox(int inbox) {
        this.inbox = inbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSpam() {
        return spam;
    }

    /**
     * DOCUMENT ME!
     *
     * @param spam DOCUMENT ME!
     */
    public void setSpam(int spam) {
        this.spam = spam;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getLabels() {
        return labels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     * @param count DOCUMENT ME!
     */
    public void addLabel(int idint, int count) {
        this.labels.addElement(new CounterLabel(idint, count));
    }
}
