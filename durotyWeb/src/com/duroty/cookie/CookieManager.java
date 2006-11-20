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


package com.duroty.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class CookieManager {
    /**
     * Creates a new instance of CookieManager
     */
    private CookieManager() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie cookie = null;

        if ((request != null) && (name != null)) {
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    cookie = cookies[i];

                    if ((cookie.getValue() != null) &&
                            !cookie.getValue().equals("") &&
                            cookie.getName().equals(name)) {
                        break;
                    } else {
                        cookie = null;
                    }
                }
            }
        }

        return cookie;
    }

    /**
     * DOCUMENT ME!
     */
    public static boolean setCookie(String path, Cookie cookie,
        HttpServletResponse response) {
        if ((response != null) && (cookie != null)) {
            cookie.setPath(path);
            response.addCookie(cookie);

            return true;
        } else {
            return false;
        }
    }
}
