/*
 * Pagination.java
 *
 * Created on 16 de noviembre de 2004, 10:26
 */
package com.duroty.utils;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class Pagination {
    /** DOCUMENT ME! */
    private String text;

    /**
     * Creates a new Pagination object.
     */
    public Pagination() {
        text = "";
    }

    /**
     * Creates a new Pagination object.
     *
     * @param actual DOCUMENT ME!
     * @param total DOCUMENT ME!
     * @param byPage DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param action folder
     * @param name page
     * @param uid order
     * @param extra desc
     */
    public Pagination(String object, int actual, int total, int byPage, int group,
        String action, String name, String uid, String extra) {
    	text = "";

        int totalPages = (int) Math.ceil(((double) total / (double) byPage));

        if (totalPages <= 1) {
            return;
        }
        
        int before = actual - 1;
        int after = actual + 1;
        int min = max(1, (actual - ((group / 2) + 1)));
        int max = min(totalPages, (actual + ((group / 2) - 1)));

        if (actual > 1) {
        	text = text + "<span onclick=\"" + object + ".displayLocation('" + action + ":" + before + "*" + uid + "!" + extra + "')\" class=\"paginationLink\"><<</span>&nbsp;&nbsp;";
        } else {
        	text = text + "<span class=\"pagination\"><<&nbsp;&nbsp;</span>";
        }

        if (min != 1) {
            text = text + "&nbsp;...&nbsp;";
        }

        for (int i = min; i < actual; i++) {
            text = text + "&nbsp;<span onclick=\"" + object + ".displayLocation('" + action + ":" + i + "*" + uid + "!" + extra + "')\" class=\"paginationLink\">" + i + "</span>&nbsp;";
        }

        text = text + "<span class=\"paginationActual\">&nbsp;" + actual + "&nbsp;</span>";

        for (int i = (actual + 1); i <= max; i++) {
            text = text + "&nbsp;<span onclick=\"" + object + ".displayLocation('" + action + ":" + i + "*" + uid + "!" + extra + "')\" class=\"paginationLink\">" + i + "</span>&nbsp;";
        }

        if (max != totalPages) {
            text = text + "&nbsp;...&nbsp;";
        }

        if (actual < totalPages) {
            text = text + "&nbsp;&nbsp;<span onclick=\"" + object + ".displayLocation('" + action + ":" + after + "*" + uid + "!" + extra + "')\" class=\"paginationLink\">>></span>";
        } else {
            text = text + "<span class=\"pagination\">&nbsp;&nbsp;>></span>";
        }
        
        if (total > byPage) {    	
    		text = text + "&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"paginationTotal\">(" + total + ")</span>";    		
    	} else {
    	}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return this.text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int min(int a, int b) {
        if (a > b) {
            return b;
        } else {
            return a;
        }
    }
}
