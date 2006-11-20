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

import java.util.Date;
import java.util.Vector;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class MessageObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = -1974152234948409670L;

    /**
     * DOCUMENT ME!
     */
    private String mid;

    /**
     * DOCUMENT ME!
     */
    private String from;

    /**
     * DOCUMENT ME!
     */
    private String replyTo;

    /**
     * DOCUMENT ME!
     */
    private String to;

    /**
     * DOCUMENT ME!
     */
    private String cc;

    /**
     * DOCUMENT ME!
     */
    private String bcc;

    /**
     * DOCUMENT ME!
     */
    private String email;

    /**
     * DOCUMENT ME!
     */
    private String subject;

    /**
     * DOCUMENT ME!
     */
    private String body;

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
    private boolean flagged;

    /**
     * DOCUMENT ME!
     */
    private boolean recent;

    /**
     * DOCUMENT ME!
     */
    private boolean hasAttachment;

    /**
     * DOCUMENT ME!
     */
    private Vector attachments;

    /**
     * DOCUMENT ME!
     */
    private String size;

    /**
     * DOCUMENT ME!
     */
    private String priority;

    /**
     * DOCUMENT ME!
     */
    private Vector referencesBefore;

    /**
     * DOCUMENT ME!
     */
    private Vector referencesAfter;

    /**
     * DOCUMENT ME!
     */
    private Date date;

    /**
     * DOCUMENT ME!
     */
    private String dateStr;

    /**
     * Creates a new MessageObj object.
     *
     * @param mid DOCUMENT ME!
     */
    public MessageObj(String mid) {
        this.mid = mid;
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
    public Vector getAttachments() {
        return attachments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attachments DOCUMENT ME!
     */
    public void setAttachments(Vector attachments) {
        this.attachments = attachments;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bcc DOCUMENT ME!
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBody() {
        return body;
    }

    /**
     * DOCUMENT ME!
     *
     * @param body DOCUMENT ME!
     */
    public void setBody(String body) {
        this.body = body;
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
    public String getCc() {
        return cc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cc DOCUMENT ME!
     */
    public void setCc(String cc) {
        this.cc = cc;
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
    public String getPriority() {
        return priority;
    }

    /**
     * DOCUMENT ME!
     *
     * @param priority DOCUMENT ME!
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRecent() {
        return recent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param recent DOCUMENT ME!
     */
    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getReferencesAfter() {
        return referencesAfter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param referencesAfter DOCUMENT ME!
     */
    public void setReferencesAfter(Vector referencesAfter) {
        this.referencesAfter = referencesAfter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getReferencesBefore() {
        return referencesBefore;
    }

    /**
     * DOCUMENT ME!
     *
     * @param referencesBefore DOCUMENT ME!
     */
    public void setReferencesBefore(Vector referencesBefore) {
        this.referencesBefore = referencesBefore;
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
}
