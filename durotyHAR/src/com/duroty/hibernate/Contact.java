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


// Generated 14-sep-2006 11:28:32 by Hibernate Tools 3.1.0.beta5
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Contact generated by hbm2java
 */
public class Contact implements java.io.Serializable {
    // Fields    

    /**
     *
     */
    private static final long serialVersionUID = 2767238887114619717L;

    /**
     * DOCUMENT ME!
     */
    private int conIdint;

    /**
     * DOCUMENT ME!
     */
    private Users users;

    /**
     * DOCUMENT ME!
     */
    private String conName;

    /**
     * DOCUMENT ME!
     */
    private String conEmail;

    /**
     * DOCUMENT ME!
     */
    private Date conSentDate;

    /**
     * DOCUMENT ME!
     */
    private Date conReceivedDate;

    /**
     * DOCUMENT ME!
     */
    private int conCount;

    /**
     * DOCUMENT ME!
     */
    private String conDescription;

    /**
     * DOCUMENT ME!
     */
    private Set conColis = new HashSet(0);

    // Constructors

    /** default constructor */
    public Contact() {
    }

    /** minimal constructor */
    public Contact(int conIdint, Users users, String conEmail, int conCount) {
        this.conIdint = conIdint;
        this.users = users;
        this.conEmail = conEmail;
        this.conCount = conCount;
    }

    /** full constructor */
    public Contact(int conIdint, Users users, String conName, String conEmail,
        Date conSentDate, Date conReceivedDate, int conCount,
        String conDescription, Set conColis) {
        this.conIdint = conIdint;
        this.users = users;
        this.conName = conName;
        this.conEmail = conEmail;
        this.conSentDate = conSentDate;
        this.conReceivedDate = conReceivedDate;
        this.conCount = conCount;
        this.conDescription = conDescription;
        this.conColis = conColis;
    }

    // Property accessors
    public int getConIdint() {
        return this.conIdint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conIdint DOCUMENT ME!
     */
    public void setConIdint(int conIdint) {
        this.conIdint = conIdint;
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
    public String getConName() {
        return this.conName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conName DOCUMENT ME!
     */
    public void setConName(String conName) {
        this.conName = conName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getConEmail() {
        return this.conEmail;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conEmail DOCUMENT ME!
     */
    public void setConEmail(String conEmail) {
        this.conEmail = conEmail;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getConSentDate() {
        return this.conSentDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conSentDate DOCUMENT ME!
     */
    public void setConSentDate(Date conSentDate) {
        this.conSentDate = conSentDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getConReceivedDate() {
        return this.conReceivedDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conReceivedDate DOCUMENT ME!
     */
    public void setConReceivedDate(Date conReceivedDate) {
        this.conReceivedDate = conReceivedDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getConCount() {
        return this.conCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conCount DOCUMENT ME!
     */
    public void setConCount(int conCount) {
        this.conCount = conCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getConDescription() {
        return this.conDescription;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conDescription DOCUMENT ME!
     */
    public void setConDescription(String conDescription) {
        this.conDescription = conDescription;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getConColis() {
        return this.conColis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conColis DOCUMENT ME!
     */
    public void setConColis(Set conColis) {
        this.conColis = conColis;
    }
}
