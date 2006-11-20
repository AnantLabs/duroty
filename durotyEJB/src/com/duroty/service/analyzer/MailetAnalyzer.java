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


/**
 *
 */
package com.duroty.service.analyzer;

import java.util.HashMap;

import javax.mail.internet.MimeMessage;


/**
 * @author durot
 *
 */
public interface MailetAnalyzer {
    /**
     * DOCUMENT ME!
     *
     * @param properties DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void init(HashMap properties)
        throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void service(String repositoryName, String messageName,
        MimeMessage mime) throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param repositoryName DOCUMENT ME!
     * @param messageName DOCUMENT ME!
     * @param mime DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws Throwable DOCUMENT ME!
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public void service(String repositoryName, String messageName,
        MimeMessage[] mime) throws Exception, Throwable, OutOfMemoryError;
}
