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
