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


package com.duroty.application.mail.utils;

import java.io.Serializable;

import java.util.Vector;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class Counters implements Serializable {
    /**
     * DOCUMENT ME!
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
