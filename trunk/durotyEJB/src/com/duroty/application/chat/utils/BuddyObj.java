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
package com.duroty.application.chat.utils;

import java.io.Serializable;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class BuddyObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -1941716798495108944L;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
    * DOCUMENT ME!
    */
    private String username;

    /**
     * DOCUMENT ME!
     */
    private int is_online;

    /**
     * Creates a new BuddyObj object.
     *
     * @param name DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param is_online DOCUMENT ME!
     */
    public BuddyObj(String name, String username, int is_online) {
        super();

        // TODO Auto-generated constructor stub
        this.name = name;
        this.username = username;
        this.is_online = is_online;
    }

    /**
    * Creates a new ListObj object.
    */
    public BuddyObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUsername() {
        return username;
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIs_online() {
        return is_online;
    }

    /**
     * DOCUMENT ME!
     *
     * @param is_online DOCUMENT ME!
     */
    public void setIs_online(int is_online) {
        this.is_online = is_online;
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
