package com.duroty.application.chat.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ChatException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = 3139961456332133145L;

    /**
    * Creates a new DMailException object.
    */
    public ChatException() {
        super();
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     */
    public ChatException(String message) {
        super(message);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param cause DOCUMENT ME!
     */
    public ChatException(Throwable cause) {
        super(cause);
    }
}
