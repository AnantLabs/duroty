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
package com.duroty.service;

import com.duroty.hibernate.Users;

import java.io.InputStream;

import java.util.HashMap;

import javax.mail.internet.MimeMessage;


/**
 * @author durot
 *
 */
public interface Messageable {
    /**
     * DOCUMENT ME!
     *
     * @param mail DOCUMENT ME!
     */
    public void setProperties(HashMap mail);

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public MimeMessage getMimeMessage(String mid, Users user)
        throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     * @param user DOCUMENT ME!
     */
    public void deleteMimeMessage(String mid, Users user)
        throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void saveSentMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void storeMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void storeMessage(String mid, InputStream inputStream, Users user)
        throws Exception, Throwable, OutOfMemoryError;

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     * @param mime DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void storeDraftMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError;
}
