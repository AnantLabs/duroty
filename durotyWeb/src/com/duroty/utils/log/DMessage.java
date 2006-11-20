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


package com.duroty.utils.log;

import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class DMessage {
    /** DOCUMENT ME! */
    private static final String DELIMITER = "\t";

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(HttpServletRequest request, String message) {
        ///ip;page;referer;message missatge per tra�ar bons logs
        StringBuffer buffRequest = null;

        if (request != null) {
            buffRequest = request.getRequestURL();

            if ((request.getQueryString() != null) && (buffRequest != null)) {
                buffRequest.append("?");
                buffRequest.append(request.getQueryString());

                return request.getRemoteHost() + DELIMITER +
                buffRequest.toString() + DELIMITER +
                request.getHeader("referer") + DELIMITER + message;
            } else {
                return request.getRemoteHost() + DELIMITER +
                request.getHeader("referer") + DELIMITER + message;
            }
        } else {
            return message;
        }
    }
}
