package com.duroty.utils.mail;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class AttachmentElement {
    /**
     * DOCUMENT ME!
     */
    private int uid;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private int size;

    /**
     * DOCUMENT ME!
     */
    private String contentType;

    /**
     * Creates a new AttachmentElement object.
     */
    public AttachmentElement(int uid, String name, int size, String contentType) {
        super();
        this.uid = uid;
        this.name = name;
        this.size = size;
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
    public int getUid() {
        return uid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param uid DOCUMENT ME!
     */
    public void setUid(int uid) {
        this.uid = uid;
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
}
