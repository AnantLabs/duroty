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
public class ContactListObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = -3218813780208953419L;

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
    private String emails;

    /**
     * Creates a new ContactListObj object.
     *
     * @param idint DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public ContactListObj(int idint, String name) {
        super();

        // TODO Auto-generated constructor stub
        this.idint = idint;
        this.name = name;
    }

    /**
     * Creates a new ContactListObj object.
     */
    public ContactListObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEmails() {
        return emails;
    }

    /**
     * DOCUMENT ME!
     *
     * @param emails DOCUMENT ME!
     */
    public void setEmails(String emails) {
        this.emails = emails;
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
}
