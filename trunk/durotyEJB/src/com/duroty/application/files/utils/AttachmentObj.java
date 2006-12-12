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
package com.duroty.application.files.utils;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class AttachmentObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -936331117855691077L;

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
    private Date date;

    /**
     * DOCUMENT ME!
     */
    private String dateStr;

    /**
     * DOCUMENT ME!
     */
    private int part;

    /**
     * DOCUMENT ME!
     */
    private String extension;

    /**
     * DOCUMENT ME!
     */
    private boolean flagged;

    /**
     * DOCUMENT ME!
     */
    private String box;

    /**
     * DOCUMENT ME!
     */
    private String label;

    /**
     * DOCUMENT ME!
     */
    private String mid;

    /**
     * DOCUMENT ME!
     */
    private int score;

    /**
    * Creates a new AttachmentObj object.
    */
    public AttachmentObj() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getScore() {
        return score;
    }

    /**
     * DOCUMENT ME!
     *
     * @param score DOCUMENT ME!
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMid() {
        return mid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     */
    public void setMid(String mid) {
        this.mid = mid;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBox() {
        return box;
    }

    /**
     * DOCUMENT ME!
     *
     * @param box DOCUMENT ME!
     */
    public void setBox(String box) {
        this.box = box;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flagged DOCUMENT ME!
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPart() {
        return part;
    }

    /**
     * DOCUMENT ME!
     *
     * @param part DOCUMENT ME!
     */
    public void setPart(int part) {
        this.part = part;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     */
    public void setDate(Date date) {
        this.date = date;
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
    public String getDateStr() {
        return dateStr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dateStr DOCUMENT ME!
     */
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabel() {
        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
