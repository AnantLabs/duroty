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
