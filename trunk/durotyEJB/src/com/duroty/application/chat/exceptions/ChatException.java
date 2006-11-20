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
package com.duroty.application.chat.exceptions;

/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class ChatException extends Exception {
    /**
         *
         */
    private static final long serialVersionUID = 3139961456332133145L;

    /**
    * Creates a new DMailException object.
    */
    public ChatException() {
        super();
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     */
    public ChatException(String message) {
        super(message);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param message DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DMailException object.
     *
     * @param cause DOCUMENT ME!
     */
    public ChatException(Throwable cause) {
        super(cause);
    }
}
