/**
 *
 */
package com.duroty.application.bookmark.utils;

import java.io.Serializable;


/**
 * @author durot
 *
 */
public class PreferencesObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -1868580233239994618L;

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
    private String language;

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
}
