package com.duroty.application.mail.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class MailException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = -6191057171043192200L;

    /**
    * Creates a new DMailException object.
    */
    public MailException() {
        super();
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     */
    public MailException(String message) {
        super(message);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param cause DOCUMENT ME!
     */
    public MailException(Throwable cause) {
        super(cause);
    }
}
