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


// Generated 02-ago-2006 17:07:31 by Hibernate Tools 3.1.0.beta5

/**
 * LabMes generated by hbm2java
 */
public class LabMes implements java.io.Serializable {
    // Fields    

    /**
     *
     */
    private static final long serialVersionUID = 874728225161192357L;

    /**
     * DOCUMENT ME!
     */
    private LabMesId id;

    // Constructors

    /** default constructor */
    public LabMes() {
    }

    /** full constructor */
    public LabMes(LabMesId id) {
        this.id = id;
    }

    // Property accessors
    public LabMesId getId() {
        return this.id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(LabMesId id) {
        this.id = id;
    }
}
