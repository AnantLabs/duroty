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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class DLog {
    /** DOCUMENT ME! */
    public static final int DEBUG = Level.DEBUG_INT;

    /** DOCUMENT ME! */
    public static final int INFO = Level.INFO_INT;

    /** DOCUMENT ME! */
    public static final int WARN = Level.WARN_INT;

    /** DOCUMENT ME! */
    public static final int ERROR = Level.ERROR_INT;

    /** DOCUMENT ME! */
    public static final int FATAL = Level.FATAL_INT;

    /** DOCUMENT ME! */
    public static final int ALL = Level.ALL_INT;

    /** DOCUMENT ME! */
    public static final int OFF = Level.OFF_INT;

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     * @param classe DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public static void log(int level, Class classe, String message) {
        if ((message == null) || message.trim().equals("")) {
            return;
        }

        Logger logger = Logger.getLogger(classe);

        //Log logger = LogFactory.getLog(DirectoryDAO.class);
        if (level == DEBUG) {
            logger.debug(message);
        } else if (level == INFO) {
            logger.info(message);
        } else if (level == WARN) {
            logger.warn(message);
        } else if (level == ERROR) {
            logger.error(message);
        } else if (level == FATAL) {
            logger.fatal(message);
        } else if (level == ALL) {
            logger.debug(message);
            logger.info(message);
            logger.warn(message);
            logger.error(message);
            logger.fatal(message);
        } else {
            //Level.OFF_INT
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     * @param classe DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    public static void log(int level, Class classe, Exception exception) {
        String message = "Unknown Exception";

        if (exception != null) {
            message = exception.getClass().getName() + " - ";

            if (exception.getMessage() != null) {
                message = message + exception.getMessage();
            }
        }

        log(level, classe, message);
    }
}
