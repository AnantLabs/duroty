package com.duroty.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class IllegalConcurrentAccessException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = -3327573894201499422L;

    /** DOCUMENT ME! */
    private String message;

    /**
     * exception m�s general en el cas que existeixi, proporcionara el  message
     */
    private Exception exception;

    /**
     * DOCUMENT ME!
     *
     * @param exception
     */
    public IllegalConcurrentAccessException(Exception exception) {
        this(null, exception);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message
     */
    public IllegalConcurrentAccessException(String message) {
        this(message, null);
    }

    /**
     * Creates a new ModuleException object.
     *
     * @param message DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    public IllegalConcurrentAccessException(String message, Exception exception) {
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

        if (message != null) {
            buff.append(message + "<br/><br/>");
        }

        String aux = exception.getMessage();

        if ((aux != null) && !aux.trim().equals("")) {
            buff.append("IllegalConcurrentAccessException: " + aux);
        }

        return buff.toString();
    }

    /**
     * DOCUMENT ME!
     */
    public void printStackTrace() {
        if (message != null) {
            System.err.println(message);
        }

        if (exception != null) {
            exception.printStackTrace();
        }
    }
}