/**
 *
 */
package com.duroty.service;

import com.duroty.hibernate.Users;

import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.mail.LuceneMessageConstants;
import com.duroty.lucene.mail.indexer.MailIndexer;
import com.duroty.lucene.mail.indexer.MailIndexerConstants;

import com.duroty.utils.mail.MimeMessageInputStreamSource;
import com.duroty.utils.mail.MimeMessageSource;
import com.duroty.utils.mail.MimeMessageWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.HashMap;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import javax.naming.NamingException;


/**
 * @author durot
 *
 */
public class POP3MessageFactory implements Messageable, LuceneMessageConstants,
    MailIndexerConstants {
    /**
     * DOCUMENT ME!
     */
    private String defaultMessagesPath = null;

    /** default lucene path **/
    private String defaultLucenePath = null;

    /**
     * @throws NamingException
     *
     */
    public POP3MessageFactory() {
        super();
    }

    /* (non-Javadoc)
     * @see com.duroty.service.Messageable#setProperties(java.util.HashMap)
     */
    public void setProperties(HashMap mail) {
        this.defaultMessagesPath = (String) mail.get(Constants.MAIL_MESSAGES_PATH);
        this.defaultLucenePath = (String) mail.get(Constants.MAIL_LUCENE_PATH);

        //this.durotyMailFactory = (String) mail.get(Constants.DUROTY_MAIL_FACTOTY);
        //this.pop3Inbox = (String) mail.get(Constants.MAIL_SERVER_INBOX);
    }

    /* (non-Javadoc)
     * @see com.duroty.service.Messageable#getMimeMessage(java.lang.String, com.duroty.hibernate.Users)
     */
    public MimeMessage getMimeMessage(String mid, Users user)
        throws Exception, Throwable, OutOfMemoryError {
        String userMessagesPath = null;

        if (!defaultMessagesPath.endsWith(File.separator)) {
            userMessagesPath = defaultMessagesPath + File.separator +
                user.getUseUsername() + File.separator + MESSAGES +
                File.separator;
        } else {
            userMessagesPath = defaultMessagesPath + user.getUseUsername() +
                File.separator + MESSAGES + File.separator;
        }

        FileInputStream input = new FileInputStream(new File(userMessagesPath +
                    mid + ".m64"));

        return new MimeMessage((Session) null, input);
    }

    /* (non-Javadoc)
     * @see com.duroty.service.Messageable#deleteMimeMessage(java.lang.String, com.duroty.hibernate.Users)
     */
    public void deleteMimeMessage(String mid, Users user) {
        try {
            String lucenePathMessages = "";

            if (!defaultLucenePath.endsWith(File.separator)) {
                lucenePathMessages = defaultLucenePath + File.separator +
                    user.getUseUsername() + File.separator +
                    Constants.MAIL_LUCENE_MESSAGES;
            } else {
                lucenePathMessages = defaultLucenePath + user.getUseUsername() +
                    File.separator + Constants.MAIL_LUCENE_MESSAGES;
            }

            MailIndexer indexer = new MailIndexer();
            indexer.deleteDocument(lucenePathMessages, Field_idint, mid);

            String userMessagesPath = null;

            if (!defaultMessagesPath.endsWith(File.separator)) {
                userMessagesPath = defaultMessagesPath + File.separator +
                    user.getUseUsername() + File.separator + MESSAGES +
                    File.separator;
            } else {
                userMessagesPath = defaultMessagesPath + user.getUseUsername() +
                    File.separator + MESSAGES + File.separator;
            }

            File file = new File(userMessagesPath + mid + ".m64");
            file.delete();
        } catch (Exception e) {
        }
    }

    /* (non-Javadoc)
     * @see com.duroty.service.Messageable#saveSentMessage(java.lang.String, javax.mail.internet.MimeMessage, com.duroty.hibernate.Users)
     */
    public void saveSentMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError {
        String userMessagesPath = null;

        if (!defaultMessagesPath.endsWith(File.separator)) {
            userMessagesPath = defaultMessagesPath + File.separator +
                user.getUseUsername() + File.separator + MESSAGES +
                File.separator;
        } else {
            userMessagesPath = defaultMessagesPath + user.getUseUsername() +
                File.separator + MESSAGES + File.separator;
        }

        MimeMessage aux = new MimeMessage(mime);
        MimeMessageSource source = new MimeMessageInputStreamSource(userMessagesPath, mid, aux, false);
        MimeMessage newMime = new MimeMessageWrapper(source);
        newMime.addHeader("X-DBox", "SENT");
        newMime.saveChanges();

        Mailet mailet = new Mailet(null, mid, user.getUseUsername(), newMime);
        Thread thread = new Thread(mailet, mid);
        thread.start();
    }

    /* (non-Javadoc)
     * @see com.duroty.service.Messageable#storeMessage(java.lang.String, javax.mail.internet.MimeMessage, com.duroty.hibernate.Users)
     */
    public void storeMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError {
        String userMessagesPath = null;

        if (!defaultMessagesPath.endsWith(File.separator)) {
            userMessagesPath = defaultMessagesPath + File.separator +
                user.getUseUsername() + File.separator + MESSAGES +
                File.separator;
        } else {
            userMessagesPath = defaultMessagesPath + user.getUseUsername() +
                File.separator + MESSAGES + File.separator;
        }

        MimeMessageSource source = new MimeMessageInputStreamSource(userMessagesPath,
                mid, mime, false);
        MimeMessage newMime = new MimeMessageWrapper(source);
        newMime.saveChanges();

        Mailet mailet = new Mailet(null, mid, user.getUseUsername(), newMime);
        Thread thread = new Thread(mailet, mid);
        thread.start();
    }

    /* (non-Javadoc)
     * @see com.duroty.service.Messageable#storeMessage(java.lang.String, java.io.InputStream, com.duroty.hibernate.Users)
     */
    public void storeMessage(String mid, InputStream inputStream, Users user)
        throws Exception, Throwable, OutOfMemoryError {
        String userMessagesPath = null;

        if (!defaultMessagesPath.endsWith(File.separator)) {
            userMessagesPath = defaultMessagesPath + File.separator +
                user.getUseUsername() + File.separator + MESSAGES +
                File.separator;
        } else {
            userMessagesPath = defaultMessagesPath + user.getUseUsername() +
                File.separator + MESSAGES + File.separator;
        }

        MimeMessageSource source = new MimeMessageInputStreamSource(userMessagesPath,
                mid, inputStream, false);
        MimeMessage newMime = new MimeMessageWrapper(source);
        newMime.saveChanges();

        Mailet mailet = new Mailet(null, mid, user.getUseUsername(), newMime);
        Thread thread = new Thread(mailet, mid);
        thread.start();
    }

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
        throws Exception, Throwable, OutOfMemoryError {
        String userMessagesPath = null;

        if (!defaultMessagesPath.endsWith(File.separator)) {
            userMessagesPath = defaultMessagesPath + File.separator +
                user.getUseUsername() + File.separator + MESSAGES +
                File.separator;
        } else {
            userMessagesPath = defaultMessagesPath + user.getUseUsername() +
                File.separator + MESSAGES + File.separator;
        }

        MimeMessageSource source = new MimeMessageInputStreamSource(userMessagesPath,
                mid, mime, false);
        MimeMessage newMime = new MimeMessageWrapper(source);
        newMime.addHeader("X-DBox", "DRAFT");
        newMime.saveChanges();

        Mailet mailet = new Mailet(null, mid, user.getUseUsername(), newMime);
        Thread thread = new Thread(mailet, mid);
        thread.start();
    }
}
