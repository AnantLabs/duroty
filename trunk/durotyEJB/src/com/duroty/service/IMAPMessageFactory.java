/**
 *
 */
package com.duroty.service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.duroty.hibernate.Users;
import com.duroty.jmx.mbean.Constants;
import com.duroty.lucene.mail.LuceneMessageConstants;
import com.duroty.lucene.mail.indexer.MailIndexer;
import com.duroty.lucene.mail.indexer.MailIndexerConstants;
import com.duroty.utils.GeneralOperations;
import com.sun.mail.imap.IMAPFolder;


/**
 * @author durot
 *
 */
public class IMAPMessageFactory implements Messageable, LuceneMessageConstants,
    MailIndexerConstants {
    /**
     * DOCUMENT ME!
     */
    private Context ctx = null;

    /** default lucene path **/
    private String defaultLucenePath = null;

    /**
     * DOCUMENT ME!
     */
    private String durotyMailFactory;

    /**
     * DOCUMENT ME!
     */
    private String imapInbox;

    /**
     * DOCUMENT ME!
     */
    private String imapSent;

    /**
     * DOCUMENT ME!
     */
    private String imapDraft;

    /**
     * Creates a new IMAPMessageFactory object.
     * @throws NamingException
     */
    public IMAPMessageFactory() throws NamingException {
        super();
        this.ctx = new InitialContext();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     * @param user DOCUMENT ME!
     */
    public void deleteMimeMessage(String mid, Users user)
        throws Exception, Throwable, OutOfMemoryError {
        javax.mail.Session msession = null;
        Store store = null;
        Folder folder = null;
        Message message = null;

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

            int pos = mid.indexOf(this.imapInbox);

            if (pos > -1) {
                mid = mid.substring(0, pos);
            } else {
                pos = mid.indexOf(this.imapSent);

                if (pos > -1) {
                    mid = mid.substring(0, pos);
                } else {
                    pos = mid.indexOf(this.imapDraft);

                    if (pos > -1) {
                        mid = mid.substring(0, pos);
                    }
                }
            }

            msession = (javax.mail.Session) ctx.lookup(durotyMailFactory);
            store = msession.getStore("imap");

            String imapHost = msession.getProperty("mail.imap.host");

            String repositoryName = user.getUseUsername();

            store.connect(imapHost, repositoryName, user.getUsePassword());

            folder = store.getFolder(this.imapInbox);

            folder.open(Folder.READ_WRITE);

            if (folder instanceof IMAPFolder) {
                message = ((IMAPFolder) folder).getMessageByUID(Long.parseLong(
                            mid));
            }

            //cal comprovar si està als enviats
            if (message == null) {
                GeneralOperations.closeMailFolder(folder, true);

                folder = store.getFolder(this.imapSent);

                folder.open(Folder.READ_WRITE);

                if (folder instanceof IMAPFolder) {
                    message = ((IMAPFolder) folder).getMessageByUID(Long.parseLong(
                                mid));
                }
            }

            //cal comprovar si està als enviats
            if (message == null) {
                GeneralOperations.closeMailFolder(folder, true);

                folder = store.getFolder(this.imapDraft);

                folder.open(Folder.READ_WRITE);

                if (folder instanceof IMAPFolder) {
                    message = ((IMAPFolder) folder).getMessageByUID(Long.parseLong(
                                mid));
                }
            }

            if (message != null) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
        } finally {
            GeneralOperations.closeMailFolder(folder, true);
            GeneralOperations.closeMailStore(store);

            System.gc();
        }
    }

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
        throws Exception, Throwable, OutOfMemoryError {
        javax.mail.Session msession = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        int box = 0;

        try {
            int pos = mid.indexOf(this.imapInbox);

            if (pos > -1) {
                mid = mid.substring(0, pos);
            } else {
                pos = mid.indexOf(this.imapSent);

                if (pos > -1) {
                    box = 1;
                    mid = mid.substring(0, pos);
                } else {
                    pos = mid.indexOf(this.imapDraft);

                    if (pos > -1) {
                        box = 2;
                        mid = mid.substring(0, pos);
                    }
                }
            }

            msession = (javax.mail.Session) ctx.lookup(durotyMailFactory);
            store = msession.getStore("imap");

            String imapHost = msession.getProperty("mail.imap.host");

            String repositoryName = user.getUseUsername();

            store.connect(imapHost, repositoryName, user.getUsePassword());

            if (box == 0) {
                folder = store.getFolder(this.imapInbox);
            } else if (box == 1) {
                folder = store.getFolder(this.imapSent);
            } else if (box == 2) {
                folder = store.getFolder(this.imapDraft);
            } else {
                return null;
            }

            folder.open(Folder.READ_ONLY);

            if (folder instanceof IMAPFolder) {
                message = ((IMAPFolder) folder).getMessageByUID(Long.parseLong(
                            mid));
            }

            return new MimeMessage((MimeMessage) message);
        } finally {
            GeneralOperations.closeMailFolder(folder, true);
            GeneralOperations.closeMailStore(store);

            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param properties DOCUMENT ME!
     */
    public void setProperties(HashMap properties) {
        this.defaultLucenePath = (String) properties.get(Constants.MAIL_LUCENE_PATH);
        this.durotyMailFactory = (String) properties.get(Constants.DUROTY_MAIL_FACTOTY);
        this.imapInbox = (String) properties.get(Constants.MAIL_SERVER_INBOX);
        this.imapSent = (String) properties.get(Constants.MAIL_SERVER_SENT);
        this.imapDraft = (String) properties.get(Constants.MAIL_SERVER_DRAFT);
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
    public void saveSentMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError {
        javax.mail.Session msession = null;
        Store store = null;
        Folder sent = null;

        try {
            msession = (javax.mail.Session) ctx.lookup(durotyMailFactory);
            store = msession.getStore("imap");

            String imapHost = msession.getProperty("mail.imap.host");

            String repositoryName = user.getUseUsername();

            store.connect(imapHost, repositoryName, user.getUsePassword());

            sent = store.getFolder(this.imapSent);

            sent.open(Folder.READ_WRITE);

            mime.setFlag(Flags.Flag.SEEN, false);

            MimeMessage[] message = new MimeMessage[] { mime };
            sent.appendMessages(message);
        } finally {
            GeneralOperations.closeMailFolder(sent, true);
            GeneralOperations.closeMailStore(store);

            System.gc();
        }
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
    public void storeMessage(String mid, MimeMessage mime, Users user)
        throws Exception, Throwable, OutOfMemoryError {
    }

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     * @param inputStream DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void storeMessage(String mid, InputStream inputStream, Users user)
        throws Exception, Throwable, OutOfMemoryError {
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
        javax.mail.Session msession = null;
        Store store = null;
        Folder sent = null;

        try {
            msession = (javax.mail.Session) ctx.lookup(durotyMailFactory);
            store = msession.getStore("imap");

            String imapHost = msession.getProperty("mail.imap.host");

            String repositoryName = user.getUseUsername();

            store.connect(imapHost, repositoryName, user.getUsePassword());

            sent = store.getFolder(this.imapDraft);

            sent.open(Folder.READ_WRITE);

            mime.setFlag(Flags.Flag.SEEN, false);

            sent.appendMessages(new Message[] { mime });
        } finally {
            GeneralOperations.closeMailFolder(sent, true);
            GeneralOperations.closeMailStore(store);

            System.gc();
        }
    }
}
