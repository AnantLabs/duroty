/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;


/**
 * @author durot
 *
 */
public class PreferencesObj implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6305132688283385767L;

    /**
    * DOCUMENT ME!
    */
    private int idint;

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
    private boolean htmlMessage;

    /**
     * DOCUMENT ME!
     */
    private boolean vacationActive;

    /**
     * DOCUMENT ME!
     */
    private String vacationSubject;

    /**
     * DOCUMENT ME!
     */
    private int quotaSize;

    /**
     * DOCUMENT ME!
     */
    private String vacationBody;

    /**
     * DOCUMENT ME!
     */
    private String language;

    /**
     * DOCUMENT ME!
     */
    private String contactEmail;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private int spamTolerance;

    /**
    *
    */
    public PreferencesObj() {
        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isHtmlMessage() {
        return htmlMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param htmlMessage DOCUMENT ME!
     */
    public void setHtmlMessage(boolean htmlMessage) {
        this.htmlMessage = htmlMessage;
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
    public boolean isVacationActive() {
        return vacationActive;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vacationActive DOCUMENT ME!
     */
    public void setVacationActive(boolean vacationActive) {
        this.vacationActive = vacationActive;
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
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contactEmail DOCUMENT ME!
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
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
    public int getSpamTolerance() {
        return spamTolerance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param spamTolerance DOCUMENT ME!
     */
    public void setSpamTolerance(int spamTolerance) {
        this.spamTolerance = spamTolerance;
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
