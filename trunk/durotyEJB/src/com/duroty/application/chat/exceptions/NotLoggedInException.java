package com.duroty.application.chat.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class NotLoggedInException extends ChatException {
    /**
         *
         */
    private static final long serialVersionUID = -8611340206093028317L;

    /**
    * Creates a new NotLoggedException object.
    */
    public NotLoggedInException() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new NotLoggedException object.
     *
     * @param message DOCUMENT ME!
     */
    public NotLoggedInException(String message) {
        super(message);

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new NotLoggedException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new NotLoggedException object.
     *
     * @param cause DOCUMENT ME!
     */
    public NotLoggedInException(Throwable cause) {
        super(cause);

        // TODO Auto-generated constructor stub
    }
}
