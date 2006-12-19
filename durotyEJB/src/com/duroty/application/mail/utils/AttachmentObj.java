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
public class AttachmentObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 3705773324313270983L;

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
    private String size;

    /**
     * DOCUMENT ME!
     */
    private String contentType;

    /**
     * DOCUMENT ME!
     */
    private String extension;

    /**
     * Creates a new AttachmentObj object.
     */
    public AttachmentObj() {
        super();

        // TODO Auto-generated constructor stub
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
    	if (!StringUtils.isBlank(name)) {
    		name = name.replaceAll("\\?", "");
    		name = name.replaceAll("&", "");
    		name = name.replaceAll("=", "");
    		int idx1 = name.lastIndexOf('/');
    		if (idx1 > -1) {
    			name = name.substring(idx1 + 1, name.length());
    		}
    		
    		idx1 = name.lastIndexOf('\\');
    		if (idx1 > -1) {
    			name = name.substring(idx1  + 1, name.length());
    		}
    	}
    	
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExtension() {
        return extension;
    }

    /**
     * DOCUMENT ME!
     *
     * @param extension DOCUMENT ME!
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
}
