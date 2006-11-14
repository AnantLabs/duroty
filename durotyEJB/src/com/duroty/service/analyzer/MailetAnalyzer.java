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
