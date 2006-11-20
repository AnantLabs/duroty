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


package com.duroty.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class InternalErrorException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = 8988442598028135139L;

    /** DOCUMENT ME! */
    private String message;

    /**
     * exception m�s general en el cas que existeixi, proporcionara el
     * message
     */
    private Exception exception;

    /**
     * DOCUMENT ME!
     *
     * @param exception
     */
    public InternalErrorException(Exception exception) {
        this(null, exception);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message
     */
    public InternalErrorException(String message) {
        this(message, null);
    }

    /**
     * Creates a new ModuleException object.
     *
     * @param message DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    public InternalErrorException(String message, Exception exception) {
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
            buff.append(message + "\n\n");
        }

        if (exception != null) {
            String aux = exception.getMessage();

            if ((aux != null) && !aux.trim().equals("")) {
                buff.append("InternalErrorException: " + aux);
            }
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
