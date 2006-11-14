package com.duroty.application.chat.utils;

import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ConversationsObj implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8409570939975944525L;

    /**
     * DOCUMENT ME!
     */
    private String sender;

    /**
     * DOCUMENT ME!
     */
    private String message;

    /**
     * Creates a new ConversationsObj object.
     *
     * @param sender DOCUMENT ME!
     * @param recipient DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public ConversationsObj(String sender, String message) {
        super();

        // TODO Auto-generated constructor stub
        this.sender = sender;
        this.message = message;
    }

    /**
    * Creates a new ConversationsObj object.
    */
    public ConversationsObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMessage() {
        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSender() {
        return sender;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sender DOCUMENT ME!
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
}
