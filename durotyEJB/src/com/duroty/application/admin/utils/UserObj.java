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
package com.duroty.application.admin.utils;

import java.io.Serializable;

import java.util.Date;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class UserObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -1226731540438568845L;

    /**
    * DOCUMENT ME!
    */
    private int idint;

    /**
     * DOCUMENT ME!
     */
    private String username;

    /**
     * DOCUMENT ME!
     */
    private String password;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private boolean active;

    /**
     * DOCUMENT ME!
     */
    private Date registerDate;

    /**
     * DOCUMENT ME!
     */
    private String email;

    /**
     * DOCUMENT ME!
     */
    private String emailIdentity;

    /**
     * DOCUMENT ME!
     */
    private String language;

    /**
     * DOCUMENT ME!
     */
    private Integer[] roles;

    /**
     * DOCUMENT ME!
     */
    private int messagesByPage;

    /**
     * DOCUMENT ME!
     */
    private String signature;

    /**
     * DOCUMENT ME!
     */
    private boolean htmlMessages;

    /**
     * DOCUMENT ME!
     */
    private boolean vactionActive;

    /**
     * DOCUMENT ME!
     */
    private String vacationSubject;

    /**
     * DOCUMENT ME!
     */
    private String vacationBody;

    /**
     * DOCUMENT ME!
     */
    private int quotaSize;

    /**
     * DOCUMENT ME!
     */
    private boolean spamTolerance;

    /**
     * DOCUMENT ME!
     */
    private String usedQuota;

    /**
    * Creates a new UserObj object.
    */
    public UserObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUsedQuota() {
        return usedQuota;
    }

    /**
     * DOCUMENT ME!
     *
     * @param usedQuota DOCUMENT ME!
     */
    public void setUsedQuota(String usedQuota) {
        this.usedQuota = usedQuota;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        return active;
    }

    /**
     * DOCUMENT ME!
     *
     * @param active DOCUMENT ME!
     */
    public void setActive(boolean active) {
        this.active = active;
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
    public boolean isHtmlMessages() {
        return htmlMessages;
    }

    /**
     * DOCUMENT ME!
     *
     * @param htmlMessages DOCUMENT ME!
     */
    public void setHtmlMessages(boolean htmlMessages) {
        this.htmlMessages = htmlMessages;
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
    public String getLanguage() {
        return language;
    }

    /**
     * DOCUMENT ME!
     *
     * @param language DOCUMENT ME!
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMessagesByPage() {
        return messagesByPage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param messagesByPage DOCUMENT ME!
     */
    public void setMessagesByPage(int messagesByPage) {
        this.messagesByPage = messagesByPage;
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
    public String getPassword() {
        return password;
    }

    /**
     * DOCUMENT ME!
     *
     * @param password DOCUMENT ME!
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getQuotaSize() {
        return quotaSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param quotaSize DOCUMENT ME!
     */
    public void setQuotaSize(int quotaSize) {
        this.quotaSize = quotaSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param registerDate DOCUMENT ME!
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer[] getRoles() {
        return roles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param roles DOCUMENT ME!
     */
    public void setRoles(Integer[] roles) {
        this.roles = roles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSignature() {
        return signature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param signature DOCUMENT ME!
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSpamTolerance() {
        return spamTolerance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param spamTolerance DOCUMENT ME!
     */
    public void setSpamTolerance(boolean spamTolerance) {
        this.spamTolerance = spamTolerance;
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
    public String getVacationBody() {
        return vacationBody;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vacationBody DOCUMENT ME!
     */
    public void setVacationBody(String vacationBody) {
        this.vacationBody = vacationBody;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVacationSubject() {
        return vacationSubject;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vacationSubject DOCUMENT ME!
     */
    public void setVacationSubject(String vacationSubject) {
        this.vacationSubject = vacationSubject;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isVactionActive() {
        return vactionActive;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vactionActive DOCUMENT ME!
     */
    public void setVactionActive(boolean vactionActive) {
        this.vactionActive = vactionActive;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEmailIdentity() {
        return emailIdentity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param emailIdentity DOCUMENT ME!
     */
    public void setEmailIdentity(String emailIdentity) {
        this.emailIdentity = emailIdentity;
    }
}
