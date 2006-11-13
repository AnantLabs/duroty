package com.duroty.application.admin.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class AdminException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = 2801014131059900117L;

    /**
    * Creates a new DMailException object.
    */
    public AdminException() {
        super();
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     */
    public AdminException(String message) {
        super(message);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public AdminException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param cause DOCUMENT ME!
     */
    public AdminException(Throwable cause) {
        super(cause);
    }
}
