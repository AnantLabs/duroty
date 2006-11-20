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


// Generated 07-sep-2006 11:32:29 by Hibernate Tools 3.1.0.beta5

/**
 * Bookmark generated by hbm2java
 */
public class Bookmark implements java.io.Serializable {
    // Fields    

    /**
     *
     */
    private static final long serialVersionUID = -5804638740586577542L;

    /**
     * DOCUMENT ME!
     */
    private int booIdint;

    /**
     * DOCUMENT ME!
     */
    private Users users;

    /**
     * DOCUMENT ME!
     */
    private String booUrl;

    /**
     * DOCUMENT ME!
     */
    private String booTitle;

    /**
     * DOCUMENT ME!
     */
    private String booKeywords;

    /**
     * DOCUMENT ME!
     */
    private String booComments;

    // Constructors

    /** default constructor */
    public Bookmark() {
    }

    /** minimal constructor */
    public Bookmark(int booIdint, Users users, String booUrl) {
        this.booIdint = booIdint;
        this.users = users;
        this.booUrl = booUrl;
    }

    /** full constructor */
    public Bookmark(int booIdint, Users users, String booUrl, String booTitle,
        String booKeywords, String booComments) {
        this.booIdint = booIdint;
        this.users = users;
        this.booUrl = booUrl;
        this.booTitle = booTitle;
        this.booKeywords = booKeywords;
        this.booComments = booComments;
    }

    // Property accessors
    public int getBooIdint() {
        return this.booIdint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param booIdint DOCUMENT ME!
     */
    public void setBooIdint(int booIdint) {
        this.booIdint = booIdint;
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
    public String getBooUrl() {
        return this.booUrl;
    }

    /**
     * DOCUMENT ME!
     *
     * @param booUrl DOCUMENT ME!
     */
    public void setBooUrl(String booUrl) {
        this.booUrl = booUrl;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBooTitle() {
        return this.booTitle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param booTitle DOCUMENT ME!
     */
    public void setBooTitle(String booTitle) {
        this.booTitle = booTitle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBooKeywords() {
        return this.booKeywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param booKeywords DOCUMENT ME!
     */
    public void setBooKeywords(String booKeywords) {
        this.booKeywords = booKeywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBooComments() {
        return this.booComments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param booComments DOCUMENT ME!
     */
    public void setBooComments(String booComments) {
        this.booComments = booComments;
    }
}
