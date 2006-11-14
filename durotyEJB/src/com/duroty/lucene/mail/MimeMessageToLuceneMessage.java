/**
 *
 */
package com.duroty.lucene.mail;

import com.duroty.lucene.parser.ParserFactory;

import com.duroty.utils.mail.MailPart;
import com.duroty.utils.mail.MessageUtilities;

import java.nio.charset.Charset;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;


/**
 * @author durot
 *
 */
public class MimeMessageToLuceneMessage {
    /**
     * DOCUMENT ME!
     */
    private static MimeMessageToLuceneMessage unique = null;

    /**
     * DOCUMENT ME!
     */
    private ParserFactory factory = null;

    /**
     * Creates a new MimeMessageToLuceneMessage object.
     */
    private MimeMessageToLuceneMessage() {
        factory = new ParserFactory();
    }

    /**
     * Creates a new MimeMessageToLuceneMessage object.
     */
    private MimeMessageToLuceneMessage(String propsFileName) {
        factory = new ParserFactory(propsFileName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param propsFileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static MimeMessageToLuceneMessage getInstance(String propsFileName) {
        if (unique == null) {
            if (propsFileName != null) {
                unique = new MimeMessageToLuceneMessage(propsFileName);
            } else {
                unique = new MimeMessageToLuceneMessage();
            }
        }

        return unique;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static MimeMessageToLuceneMessage getInstance() {
        if (unique == null) {
            unique = new MimeMessageToLuceneMessage();
        }

        return unique;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LuceneMessage parse(String idint, MimeMessage mime,
        StringBuffer bodyBuffer, Vector attachments, int indexLimit) {
        if ((idint == null) || (mime == null)) {
            return null;
        }

        LuceneMessage lmsg = new LuceneMessage(idint);

        //lmsg.setBox(box);
        try {
            lmsg.setFrom(MessageUtilities.decodeAddresses(mime.getFrom()));
        } catch (Exception ex) {
        }

        try {
            lmsg.setTo(MessageUtilities.decodeAddresses(mime,
                    Message.RecipientType.TO));
        } catch (Exception ex) {
        }

        try {
            lmsg.setCc(MessageUtilities.decodeAddresses(mime,
                    Message.RecipientType.CC));
        } catch (Exception ex) {
        }

        try {
            lmsg.setSubject(mime.getSubject());
        } catch (Exception ex) {
        }

        try {
            Date date = mime.getReceivedDate();

            if (date == null) {
                date = mime.getSentDate();
            }

            lmsg.setLastDate(date);
        } catch (MessagingException ex) {
        }

        try {
            int size = mime.getSize();
            Enumeration e = mime.getAllHeaders();

            while (e.hasMoreElements()) {
                size += ((Header) e.nextElement()).toString().length();
            }

            lmsg.setSize(new Long(size).longValue());
        } catch (Exception ex) {
        }

        try {
            MessageUtilities.decodeContent(mime, bodyBuffer, attachments, true, "<br/>");
        } catch (Exception ex) {
            bodyBuffer.append("");
        }

        String body = null;

        try {
            body = factory.parse(bodyBuffer.toString(), "text/html",
                    Charset.defaultCharset().displayName());
        } catch (Exception ex) {
            if (ex != null) {
                body = "ERROR reading body" + ex.getMessage();
            }
        }

        if ((body == null) || body.trim().equals("")) {
            body = "";
        }

        lmsg.setBody(body);

        try {
            if ((attachments.size() > 0) && (indexLimit > -1)) {
                StringBuffer attachment = new StringBuffer();
                StringBuffer filetype = new StringBuffer();

                for (int i = 0; i < attachments.size(); i++) {
                    MailPart part = (MailPart) attachments.get(i);

                    int size = part.getSize();

                    boolean control = false;

                    if (indexLimit == 0) {
                        control = true;
                    } else {
                        if (indexLimit > size) {
                            control = true;
                        }
                    }

                    ContentType xctype = MessageUtilities.getContentType(part.getContentType());

                    String charset = xctype.getParameter("charset");

                    if (charset == null) {
                        charset = Charset.defaultCharset().displayName();
                    }

                    String mimetype = null;

                    if (xctype != null) {
                        mimetype = xctype.toString();
                    }

                    if ((part.getName() != null) &&
                            !part.getName().trim().equals("")) {
                        attachment.append(part.getName() + " ");
                    }

                    if (control) {
                        String aux = null;

                        try {
                            aux = factory.parse(part.getPart().getInputStream(),
                                    mimetype, charset);
                        } catch (Exception ex) {
                            if (ex != null) {
                                body = "ERROR reading body" + ex.getMessage();
                            }
                        }

                        if ((aux != null) && !aux.trim().equals("")) {
                            attachment.append(aux + " ");
                        }
                    }

                    if (mimetype != null) {
                        filetype.append(mimetype + " ");
                    }
                }

                lmsg.setAttachments(attachment.toString());
                lmsg.setHasAttachment(true);
                lmsg.setFiletype(filetype.toString());
            } else {
                lmsg.setHasAttachment(false);
                lmsg.setAttachments("");
                lmsg.setFiletype("");
            }
        } catch (Exception ex) {
            lmsg.setHasAttachment(false);
            lmsg.setAttachments("");
            lmsg.setFiletype("");
        }

        return lmsg;
    }
}
