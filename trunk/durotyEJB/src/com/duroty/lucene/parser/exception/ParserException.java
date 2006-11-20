/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/


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

        if (message != null) {
            buff.append(message + " ");
        }

        if (exception != null) {
            buff.append("Exception: " + exception.getMessage());
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
