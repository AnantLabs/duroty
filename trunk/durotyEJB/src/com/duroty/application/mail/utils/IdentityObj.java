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


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class IdentityObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 4984200792589837383L;

    /**
     * DOCUMENT ME!
     */
    private int idint;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private String email;

    /**
     * DOCUMENT ME!
     */
    private String replyTo;

    /**
     * DOCUMENT ME!
     */
    private boolean important;

    /**
     * Creates a new IdentityObj object.
     */
    public IdentityObj() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEmail() {
        return email;
    }

    /**
     * DOCUMENT ME!
     *
     * @param email DOCUMENT ME!
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIdint() {
        return idint;
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
    public boolean isImportant() {
        return important;
    }

    /**
     * DOCUMENT ME!
     *
     * @param important DOCUMENT ME!
     */
    public void setImportant(boolean important) {
        this.important = important;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param replyTo DOCUMENT ME!
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
