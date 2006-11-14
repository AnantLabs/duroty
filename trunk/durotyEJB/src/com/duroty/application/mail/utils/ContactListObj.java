/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;


/**
 * @author durot
 *
 */
public class ContactListObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -3218813780208953419L;

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
    private String emails;

    /**
    * Creates a new ContactListObj object.
    *
    * @param idint DOCUMENT ME!
    * @param name DOCUMENT ME!
    */
    public ContactListObj(int idint, String name) {
        super();

        // TODO Auto-generated constructor stub
        this.idint = idint;
        this.name = name;
    }

    /**
    *
    */
    public ContactListObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEmails() {
        return emails;
    }

    /**
     * DOCUMENT ME!
     *
     * @param emails DOCUMENT ME!
     */
    public void setEmails(String emails) {
        this.emails = emails;
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
        this.name = name;
    }
}
