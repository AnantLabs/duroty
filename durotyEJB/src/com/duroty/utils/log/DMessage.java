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
