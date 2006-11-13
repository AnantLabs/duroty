package com.duroty.utils.mail;

import java.io.Serializable;

import javax.mail.Part;


/**
 * DOCUMENT ME!
 *
 * @author Durot
 * @version 1.0
 */
public class MailPart implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = 1417113239350157398L;

    /** DOCUMENT ME! */
    private int id;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String contentType;

    /** DOCUMENT ME! */
    private int size;

    /** DOCUMENT ME! */
    private Part part;

    /**
     * DOCUMENT ME!
     */
    private String sizeStr;

    /**
    * Creates a new DMailPart object.
    */
    public MailPart() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Part getPart() {
        return part;
    }

    /**
     * DOCUMENT ME!
     *
     * @param part DOCUMENT ME!
     */
    public void setPart(Part part) {
        this.part = part;
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
    public int getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uid DOCUMENT ME!
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSizeStr() {
        return sizeStr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sizeStr DOCUMENT ME!
     */
    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }
}
