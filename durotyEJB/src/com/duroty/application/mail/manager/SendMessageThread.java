package com.duroty.application.mail.manager;

import org.apache.commons.mail.Email;

import com.duroty.application.mail.exceptions.MailException;

import java.util.Vector;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;


/**
 * SendMessageThread presents a dialog which displays the progress of sending a
 * specific message.
 *
 * <p>
 * The message is checked and completed before sending. It may also be saved
 * into the configured 'sent' folder.
 * </p>
 *
 * <p>
 * After the message is sent, this thread also disposes of the parent frame,
 * i.e. composer.
 * </p>
 *
 * @see ComposeFrame
 */
public class SendMessageThread implements Runnable {
    /** DOCUMENT ME! */
    private Email email;

    /**
     * DOCUMENT ME!
     */
    private MimeMessage mime;

    /**
     * DOCUMENT ME!
     */
    private Vector mimes;

    /**
     * Create a thread of execution for sending the given message, and saving
     * the message to the configured 'sent' folder if necessary.
     *
     * @param message message to send
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public SendMessageThread(Email email) throws MessagingException {
        super();
        this.email = email;
    }

    /**
     * Create a thread of execution for sending the given message, and saving
     * the message to the configured 'sent' folder if necessary.
     *
     * @param message message to send
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public SendMessageThread(MimeMessage mime) throws MessagingException {
        super();
        this.mime = mime;
    }

    /**
     * Create a thread of execution for sending the given message, and saving
     * the message to the configured 'sent' folder if necessary.
     *
     * @param message message to send
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public SendMessageThread(Vector mimes) throws MessagingException {
        super();
        this.mimes = mimes;
    }

    /**
     * <p>
     * Implementation of Thread.run()
     * </p>
     */
    public void start() throws Exception {
        try {
            if (email != null) {
            	email.getMimeMessage().removeHeader("X-DBox");
                email.send();

                return;
            }

            if (mime != null) {
            	mime.removeHeader("X-DBox");
                Transport.send(mime);

                return;
            }

            if (mimes != null) {
                for (int i = 0; i < mimes.size(); i++) {
                    MimeMessage mm = (MimeMessage) mimes.get(i);
                    mm.removeHeader("X-DBox");
                    Transport.send(mm);
                }
            }
        } catch (Exception ex) {
            throw new MailException(ex);
        } catch (java.lang.OutOfMemoryError ex) {
        	System.gc();
            throw new MailException(ex);
        } catch (Throwable ex) {
            throw new MailException(ex);
        } finally {
            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        try {
            start();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
