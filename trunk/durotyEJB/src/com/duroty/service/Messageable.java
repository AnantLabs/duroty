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
    public void deleteMimeMessage(String mid, Users user) throws Exception, Throwable, OutOfMemoryError;

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
