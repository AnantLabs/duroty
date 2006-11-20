/*
 * FileNotReadableException.java
 *
 * Created on 26 de octubre de 2004, 17:04
 */
package com.duroty.utils.io;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class FileNotReadableException extends Exception {
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of FileNotReadableException
     *
     * @param message DOCUMENT ME!
     */
    public FileNotReadableException(String message) {
        super(message);
    }
}
