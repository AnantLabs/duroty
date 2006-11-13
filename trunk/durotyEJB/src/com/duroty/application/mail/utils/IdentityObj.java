package com.duroty.application.mail.utils;

import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class IdentityObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 4984200792589837383L;

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
    private String email;

    /**
     * DOCUMENT ME!
     */
    private String replyTo;

    /**
     * DOCUMENT ME!
     */
    private boolean important;

    /**
     * Creates a new IdentityObj object.
     */
    public IdentityObj() {
        super();
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
}
