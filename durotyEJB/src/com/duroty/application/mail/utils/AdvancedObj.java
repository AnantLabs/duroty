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
public class AdvancedObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = -577160996797886556L;

    /**
     * DOCUMENT ME!
     */
    private LabelObj label;

    /**
     * DOCUMENT ME!
     */
    private String box;

    /**
     * DOCUMENT ME!
     */
    private String from;

    /**
     * DOCUMENT ME!
     */
    private String to;

    /**
     * DOCUMENT ME!
     */
    private String subject;

    /**
     * DOCUMENT ME!
     */
    private String hasWords;

    /**
     * DOCUMENT ME!
     */
    private boolean hasWordsInBody;

    /**
     * DOCUMENT ME!
     */
    private boolean hasWordsInAttachment;

    /**
     * DOCUMENT ME!
     */
    private boolean doesntHaveWordsInBody;

    /**
     * DOCUMENT ME!
     */
    private boolean doesntHaveWordsInAttachment;

    /**
     * DOCUMENT ME!
     */
    private String startDate;

    /**
     * DOCUMENT ME!
     */
    private String endDate;

    /**
     * DOCUMENT ME!
     */
    private String doesntHaveWords;

    /**
     * DOCUMENT ME!
     */
    private boolean hasAttachment;

    /**
     * DOCUMENT ME!
     */
    private boolean archive;

    /**
     * DOCUMENT ME!
     */
    private boolean important;

    /**
     * DOCUMENT ME!
     */
    private String forward;

    /**
     * DOCUMENT ME!
     */
    private boolean trash;

    /**
     * DOCUMENT ME!
     */
    private boolean operator;

    /**
     * DOCUMENT ME!
     */
    private String contentType;

    /**
     * DOCUMENT ME!
     */
    private String fixDate;

    /**
     * Creates a new AdvancedObj object.
     */
    public AdvancedObj() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFixDate() {
        return fixDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fixDate DOCUMENT ME!
     */
    public void setFixDate(String fixDate) {
        this.fixDate = fixDate;
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
    public String getDoesntHaveWords() {
        return doesntHaveWords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doesntHaveWords DOCUMENT ME!
     */
    public void setDoesntHaveWords(String doesntHaveWords) {
        this.doesntHaveWords = doesntHaveWords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFrom() {
        return from;
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isHasAttachment() {
        return hasAttachment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasAttachment DOCUMENT ME!
     */
    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHasWords() {
        return hasWords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasWords DOCUMENT ME!
     */
    public void setHasWords(String hasWords) {
        this.hasWords = hasWords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSubject() {
        return subject;
    }

    /**
     * DOCUMENT ME!
     *
     * @param subject DOCUMENT ME!
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTo() {
        return to;
    }

    /**
     * DOCUMENT ME!
     *
     * @param to DOCUMENT ME!
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isArchive() {
        return archive;
    }

    /**
     * DOCUMENT ME!
     *
     * @param archive DOCUMENT ME!
     */
    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getForward() {
        return forward;
    }

    /**
     * DOCUMENT ME!
     *
     * @param forward DOCUMENT ME!
     */
    public void setForward(String forward) {
        this.forward = forward;
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
    public boolean isTrash() {
        return trash;
    }

    /**
     * DOCUMENT ME!
     *
     * @param trash DOCUMENT ME!
     */
    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LabelObj getLabel() {
        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     */
    public void setLabel(LabelObj label) {
        this.label = label;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOperator() {
        return operator;
    }

    /**
     * DOCUMENT ME!
     *
     * @param operator DOCUMENT ME!
     */
    public void setOperator(boolean operator) {
        this.operator = operator;
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
    public String getEndDate() {
        return endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param endDate DOCUMENT ME!
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDoesntHaveWordsInAttachment() {
        return doesntHaveWordsInAttachment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doesntHaveWordsInAttachment DOCUMENT ME!
     */
    public void setDoesntHaveWordsInAttachment(
        boolean doesntHaveWordsInAttachment) {
        this.doesntHaveWordsInAttachment = doesntHaveWordsInAttachment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDoesntHaveWordsInBody() {
        return doesntHaveWordsInBody;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doesntHaveWordsInBody DOCUMENT ME!
     */
    public void setDoesntHaveWordsInBody(boolean doesntHaveWordsInBody) {
        this.doesntHaveWordsInBody = doesntHaveWordsInBody;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isHasWordsInAttachment() {
        return hasWordsInAttachment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasWordsInAttachment DOCUMENT ME!
     */
    public void setHasWordsInAttachment(boolean hasWordsInAttachment) {
        this.hasWordsInAttachment = hasWordsInAttachment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isHasWordsInBody() {
        return hasWordsInBody;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasWordsInBody DOCUMENT ME!
     */
    public void setHasWordsInBody(boolean hasWordsInBody) {
        this.hasWordsInBody = hasWordsInBody;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param startDate DOCUMENT ME!
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
