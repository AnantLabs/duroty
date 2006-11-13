package com.duroty.application.chat.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class NotOnlineException extends ChatException {
    /**
         *
         */
    private static final long serialVersionUID = -7473045707202056822L;

    /**
    * Creates a new NotLoggedException object.
    */
    public NotOnlineException() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new NotLoggedException object.
     *
     * @param message DOCUMENT ME!
     */
    public NotOnlineException(String message) {
        super(message);

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new NotLoggedException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public NotOnlineException(String message, Throwable cause) {
        super(message, cause);

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new NotLoggedException object.
     *
     * @param cause DOCUMENT ME!
     */
    public NotOnlineException(Throwable cause) {
        super(cause);

        // TODO Auto-generated constructor stub
    }
}
