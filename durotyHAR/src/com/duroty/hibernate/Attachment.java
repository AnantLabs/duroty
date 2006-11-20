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


// Generated 03-ago-2006 12:02:31 by Hibernate Tools 3.1.0.beta5

/**
 * Attachment generated by hbm2java
 */
public class Attachment implements java.io.Serializable {
    // Fields    

    /**
     *
     */
    private static final long serialVersionUID = -1664910123788201143L;

    /**
     * DOCUMENT ME!
     */
    private int attIdint;

    /**
     * DOCUMENT ME!
     */
    private Message message;

    /**
     * DOCUMENT ME!
     */
    private String attName;

    /**
     * DOCUMENT ME!
     */
    private int attSize;

    /**
     * DOCUMENT ME!
     */
    private int attPart;

    /**
     * DOCUMENT ME!
     */
    private String attContentType;

    // Constructors

    /** default constructor */
    public Attachment() {
    }

    /** full constructor */
    public Attachment(int attIdint, Message message, String attName,
        int attSize, int attPart, String attContentType) {
        this.attIdint = attIdint;
        this.message = message;
        this.attName = attName;
        this.attSize = attSize;
        this.attPart = attPart;
        this.attContentType = attContentType;
    }

    // Property accessors
    public int getAttIdint() {
        return this.attIdint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attIdint DOCUMENT ME!
     */
    public void setAttIdint(int attIdint) {
        this.attIdint = attIdint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAttName() {
        return this.attName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attName DOCUMENT ME!
     */
    public void setAttName(String attName) {
        this.attName = attName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAttSize() {
        return this.attSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attSize DOCUMENT ME!
     */
    public void setAttSize(int attSize) {
        this.attSize = attSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAttPart() {
        return this.attPart;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attPart DOCUMENT ME!
     */
    public void setAttPart(int attPart) {
        this.attPart = attPart;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAttContentType() {
        return this.attContentType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attContentType DOCUMENT ME!
     */
    public void setAttContentType(String attContentType) {
        this.attContentType = attContentType;
    }
}
