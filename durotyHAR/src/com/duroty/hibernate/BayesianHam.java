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
 * BayesianHam generated by hbm2java
 */
public class BayesianHam implements java.io.Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 6739929143550873120L;

    // Fields    

    /**
     * DOCUMENT ME!
     */
    private int hamIdint;

    /**
     * DOCUMENT ME!
     */
    private Users users;

    /**
     * DOCUMENT ME!
     */
    private String hamToken;

    /**
     * DOCUMENT ME!
     */
    private int hamOcurrences;

    // Constructors

    /** default constructor */
    public BayesianHam() {
    }

    /** full constructor */
    public BayesianHam(int hamIdint, Users users, String hamToken,
        int hamOcurrences) {
        this.hamIdint = hamIdint;
        this.users = users;
        this.hamToken = hamToken;
        this.hamOcurrences = hamOcurrences;
    }

    // Property accessors
    public int getHamIdint() {
        return this.hamIdint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hamIdint DOCUMENT ME!
     */
    public void setHamIdint(int hamIdint) {
        this.hamIdint = hamIdint;
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
    public String getHamToken() {
        return this.hamToken;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hamToken DOCUMENT ME!
     */
    public void setHamToken(String hamToken) {
        this.hamToken = hamToken;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHamOcurrences() {
        return this.hamOcurrences;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hamOcurrences DOCUMENT ME!
     */
    public void setHamOcurrences(int hamOcurrences) {
        this.hamOcurrences = hamOcurrences;
    }
}
