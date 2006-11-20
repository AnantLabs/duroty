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
public class SearchGroupsObj implements Serializable {
    /**
     * DOCUMENT ME!
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
     * Creates a new SearchGroupsObj object.
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
     * @param groups DOCUMENT ME!
     */
    public void setGroups(Vector groups) {
        this.groups = groups;
    }
}
