package com.duroty.lucene.parser.exception;

/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class ParserException extends Exception {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3545236946625769784L;

    /** DOCUMENT ME! */
    private String message;

    /** DOCUMENT ME! */
    private Exception exception;

    /**
     * Creates a new ModuleException object.
     *
     * @param message DOCUMENT ME!
     */
    public ParserException(String message) {
        this(message, null);
    }

    /**
     * Creates a new ModuleException object.
     *
     * @param exception DOCUMENT ME!
     */
    public ParserException(Exception exception) {
        this(null, exception);
    }

    /**
     * Creates a new instance of DSecurityManagerException
     *
     * @param message DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    public ParserException(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMessage() {
        StringBuffer buff = new StringBuffer();

        if(message != null) {
            buff.append(message + " ");
        }

        if(exception != null) {
            buff.append("Exception: " + exception.getMessage());
        }

        return buff.toString();
    }

    /**
     * DOCUMENT ME!
     */
    public void printStackTrace() {
        if(message != null) {
            System.err.println(message);
        }

        if(exception != null) {
            exception.printStackTrace();
        }
    }
}
