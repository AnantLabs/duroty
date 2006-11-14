package com.duroty.application.home.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class HomeException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = 2345675985793945502L;

    /**
    * Creates a new DMailException object.
    */
    public HomeException() {
        super();
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     */
    public HomeException(String message) {
        super(message);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public HomeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param cause DOCUMENT ME!
     */
    public HomeException(Throwable cause) {
        super(cause);
    }
}
