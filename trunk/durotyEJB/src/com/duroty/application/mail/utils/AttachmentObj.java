/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;


/**
 * @author durot
 *
 */
public class AttachmentObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 3705773324313270983L;

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
     *
     */
    public AttachmentObj() {
        super();

        // TODO Auto-generated constructor stub
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
}
