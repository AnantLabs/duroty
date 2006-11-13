/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;


/**
 * @author durot
 *
 */
public class FolderObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 696724678931019590L;

    /**
    * DOCUMENT ME!
    */
    private String name;

    /**
     * Creates a new FolderObj object.
     *
     * @param name DOCUMENT ME!
     */
    public FolderObj(String name) {
        super();
        this.name = name;
    }

    /**
     * Creates a new FolderObj object.
     */
    public FolderObj() {
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
