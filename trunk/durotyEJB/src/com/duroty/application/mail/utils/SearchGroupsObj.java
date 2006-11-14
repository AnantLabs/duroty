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
public class SearchGroupsObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 1540861750505428681L;

    /**
    * DOCUMENT ME!
    */
    private Vector groups;

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
    public SearchGroupsObj() {
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
    public Vector getGroups() {
        return groups;
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    public void setGroups(Vector groups) {
        this.groups = groups;
    }
}
