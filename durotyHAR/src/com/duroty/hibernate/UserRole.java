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


// Generated 31-jul-2006 11:12:15 by Hibernate Tools 3.1.0.beta5

/**
 * UserRole generated by hbm2java
 */
public class UserRole implements java.io.Serializable {
    // Fields    

    /**
     *
     */
    private static final long serialVersionUID = -4346115411235345377L;

    /**
     * DOCUMENT ME!
     */
    private UserRoleId id;

    // Constructors

    /** default constructor */
    public UserRole() {
    }

    /** full constructor */
    public UserRole(UserRoleId id) {
        this.id = id;
    }

    // Property accessors

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UserRoleId getId() {
        return this.id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(UserRoleId id) {
        this.id = id;
    }
}
