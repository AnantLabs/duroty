package com.duroty.application.bookmark.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class BookmarkException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = 5839447154147982305L;

    /**
    * Creates a new DMailException object.
    */
    public BookmarkException() {
        super();
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     */
    public BookmarkException(String message) {
        super(message);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public BookmarkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param cause DOCUMENT ME!
     */
    public BookmarkException(Throwable cause) {
        super(cause);
    }
}
