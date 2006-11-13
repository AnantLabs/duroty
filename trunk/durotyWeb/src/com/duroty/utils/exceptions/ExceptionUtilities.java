package com.duroty.utils.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ExceptionUtilities {
    /**
    * Creates a new ExceptionUtilities object.
    */
    public ExceptionUtilities() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String parseMessage(Exception e) {
        if ((e != null) && (e.getMessage() != null)) {
            return e.getMessage().replaceAll("\n", "\\n");
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String parseMessage(String msg) {
        if (msg != null) {
            return msg.replaceAll("\n", "\\n");
        }

        return null;
    }
}
