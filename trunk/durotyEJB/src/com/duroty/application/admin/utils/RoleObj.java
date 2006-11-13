package com.duroty.application.admin.utils;

import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class RoleObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 3470638438602073925L;

    /**
    * DOCUMENT ME!
    */
    private int idint;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * Creates a new RoleObj object.
     */
    public RoleObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new RoleObj object.
     *
     * @param idint DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public RoleObj(int idint, String name) {
        super();

        // TODO Auto-generated constructor stub
        this.idint = idint;
        this.name = name;
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
