/**
 *
 */
package com.duroty.application.bookmark.utils;

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
    private Vector bookmarks;

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
    public Vector getBookmarks() {
        return bookmarks;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bookmarks DOCUMENT ME!
     */
    public void setBookmarks(Vector bookmarks) {
        this.bookmarks = bookmarks;
    }
}
