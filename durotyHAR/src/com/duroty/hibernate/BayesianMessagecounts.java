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


package com.duroty.hibernate;


// Generated 17-oct-2006 10:28:11 by Hibernate Tools 3.1.0.beta5

/**
 * BayesianMessagecounts generated by hbm2java
 */
public class BayesianMessagecounts implements java.io.Serializable {
    // Fields    

    /**
     *
     */
    private static final long serialVersionUID = -2971949198018992436L;

    /**
     * DOCUMENT ME!
     */
    private int idint;

    /**
     * DOCUMENT ME!
     */
    private Users users;

    /**
     * DOCUMENT ME!
     */
    private int hamCount;

    /**
     * DOCUMENT ME!
     */
    private int spamCount;

    // Constructors

    /** default constructor */
    public BayesianMessagecounts() {
    }

    /** full constructor */
    public BayesianMessagecounts(int idint, Users users, int hamCount,
        int spamCount) {
        this.idint = idint;
        this.users = users;
        this.hamCount = hamCount;
        this.spamCount = spamCount;
    }

    // Property accessors
    public int getIdint() {
        return this.idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setIdint(int idint) {
        this.idint = idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Users getUsers() {
        return this.users;
    }

    /**
     * DOCUMENT ME!
     *
     * @param users DOCUMENT ME!
     */
    public void setUsers(Users users) {
        this.users = users;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHamCount() {
        return this.hamCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hamCount DOCUMENT ME!
     */
    public void setHamCount(int hamCount) {
        this.hamCount = hamCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSpamCount() {
        return this.spamCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param spamCount DOCUMENT ME!
     */
    public void setSpamCount(int spamCount) {
        this.spamCount = spamCount;
    }
}