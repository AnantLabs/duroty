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

import org.apache.commons.lang.StringUtils;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class MailPartObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 3770511075005658504L;

    /** DOCUMENT ME! */
    private int id;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String contentType;

    /** DOCUMENT ME! */
    private long size;

    /** DOCUMENT ME! */
    private byte[] attachent;

    /**
    * Creates a new MailPartObj object.
    */
    public MailPartObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getAttachent() {
        return attachent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attachent DOCUMENT ME!
     */
    public void setAttachent(byte[] attachent) {
        this.attachent = attachent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contentType DOCUMENT ME!
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(int id) {
        this.id = id;
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
    	if (!StringUtils.isBlank(name)) {
    		int idx1 = name.lastIndexOf('/');
    		if (idx1 > -1) {
    			name = name.substring(idx1 + 1, name.length());
    		}
    		
    		idx1 = name.lastIndexOf('\\');
    		if (idx1 > -1) {
    			name = name.substring(idx1 + 1, name.length());
    		}
    	}
    	
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     */
    public void setSize(long size) {
        this.size = size;
    }
}
