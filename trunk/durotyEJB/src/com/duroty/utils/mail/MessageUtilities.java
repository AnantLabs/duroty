/*
 ** $Id: MessageUtilities.java,v 1.9 2006/06/30 13:00:37 durot Exp $
 **
 ** Copyright (c) 2000-2003 Jeff Gay
 ** on behalf of ICEMail.org <http://www.icemail.org>
 ** Copyright (c) 1998-2000 by Timothy Gerard Endres
 **
 ** This program is free software.
 **
 ** You may redistribute it and/or modify it under the terms of the GNU
 ** General Public License as published by the Free Software Foundation.
 ** Version 2 of the license should be included with this distribution in
 ** the file LICENSE, as well as License.html. If the license is not
 ** included with this distribution, you may find a copy at the FSF web
 ** site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 ** Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 **
 ** THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
 ** NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
 ** OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
 ** CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
 ** REDISTRIBUTION OF THIS SOFTWARE.
 */
package com.duroty.utils.mail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.lucene.demo.html.Entities;

import com.duroty.utils.mail.addressbook.vCard;
//import com.duroty.utils.misc.JavaScriptFilter;


/**
 * Message parsing and generation utility routines.
 */
public class MessageUtilities {
    /**
     * This wrapper is used to work around a bug in the sun io inside
     * sun.io.ByteToCharConverter.getConverterClass(). The programmer uses a
     * cute coding trick that appends the charset to a prefix to create a
     * Class name that they then attempt to load. The problem is that if the
     * charset is not one that they "recognize", and the name has something
     * like a dash character in it, the resulting Class name has an invalid
     * character in it. This results in an IllegalArgumentException instead of
     * the UnsupportedEncodingException that is documented. Thus, we need to
     * catch the undocumented exception.
     *
     * @param part The part from which to get the content.
     *
     * @return The content.
     *
     * @exception MessagingException if the content charset is unsupported.
     */
    public static Object getPartContent(Part part) throws MessagingException {
        Object result = null;

        try {
            result = part.getContent();
        } catch (IllegalArgumentException ex) {
            throw new MessagingException("content charset is not recognized: " +
                ex.getMessage());
        } catch (IOException ex) {
            throw new MessagingException("getPartContent(): " +
                ex.getMessage());
        }

        return result;
    }

    /**
     * This wrapper is used to work around a bug in the sun io inside
     * sun.io.ByteToCharConverter.getConverterClass(). The programmer uses a
     * cute coding trick that appends the charset to a prefix to create a
     * Class name that they then attempt to load. The problem is that if the
     * charset is not one that they "recognize", and the name has something
     * like a dash character in it, the resulting Class name has an invalid
     * character in it. This results in an IllegalArgumentException instead of
     * the UnsupportedEncodingException that is documented. Thus, we need to
     * catch the undocumented exception.
     *
     * @param dh The DataHandler from which to get the content.
     *
     * @return The content.
     *
     * @exception MessagingException if the content charset is unsupported.
     */
    public static Object getDataHandlerContent(DataHandler dh)
        throws MessagingException {
        Object result = null;

        try {
            result = dh.getContent();
        } catch (IllegalArgumentException ex) {
            throw new MessagingException("content charset is not recognized: " +
                ex.getMessage());
        } catch (IOException ex) {
            throw new MessagingException("getDataHandlerContent(): " +
                ex.getMessage());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     * @param msgText DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param session DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static MimeMessage createNewMessage(Address from, Address to,
        String msgText, MimeMessage message, Session session)
        throws Exception {
        return createNewMessage(from, new Address[] { to }, null, null,
            msgText, message, session);
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     * @param subjectPrefix DOCUMENT ME!
     * @param subjectSuffix DOCUMENT ME!
     * @param msgText DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param session DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static MimeMessage createNewMessage(Address from, Address[] to,
        String subjectPrefix, String subjectSuffix, String msgText,
        MimeMessage message, Session session) throws Exception {
        if (from == null) {
            throw new IllegalArgumentException("from addres cannot be null.");
        }

        if ((to == null) || (to.length <= 0)) {
            throw new IllegalArgumentException("to addres cannot be null.");
        }

        if (message == null) {
            throw new IllegalArgumentException("to message cannot be null.");
        }

        try {
            //Create the message
            MimeMessage newMessage = new MimeMessage(session);

            StringBuffer buffer = new StringBuffer();

            if (subjectPrefix != null) {
                buffer.append(subjectPrefix);
            }

            if (message.getSubject() != null) {
                buffer.append(message.getSubject());
            }

            if (subjectSuffix != null) {
                buffer.append(subjectSuffix);
            }

            if (buffer.length() > 0) {
                newMessage.setSubject(buffer.toString());
            }

            newMessage.setFrom(from);
            newMessage.addRecipients(Message.RecipientType.TO, to);

            //Create your new message part
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(msgText);

            //Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);

            //Create and fill part for the forwarded content
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(message.getDataHandler());

            //Add part to multi part
            multipart.addBodyPart(messageBodyPart2);

            //Associate multi-part with message
            newMessage.setContent(multipart);

            newMessage.saveChanges();

            return newMessage;
        } finally {
        }
    }

    /**
     * Create a reply message to the given message. The reply message is
     * addressed to only the from / reply address or all receipients based on
     * the replyToAll flag.
     *
     * @param fromName DOCUMENT ME!
     * @param fromEmail DOCUMENT ME!
     * @param message The message which to reply
     * @param body The attached text to include in the reply
     * @param replyToAll Reply to all receipients of the original message
     *
     * @return Message Reply message
     *
     * @exception MessagingException if the message contents are invalid
     */
    public static Message createReply(Address[] from, Message message,
        boolean replyToAll) throws MessagingException {
        if ((from == null) || (from.length <= 0)) {
            throw new MessagingException("The from is null");
        }

        // create an empty reply message
        Message xreply = message.reply(replyToAll);

        // set the default from address
        xreply.setFrom(from[0]);

        // Message.reply() may set the "replied to" flag, so
        // attempt to save that new state and fail silently...
        try {
            message.saveChanges();
        } catch (MessagingException ex) {
        }

        return xreply;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @throws javax.mail.MessagingException DOCUMENT ME!
     */
    public static void clearAllHeaders(MimeMessage message)
        throws javax.mail.MessagingException {
        Enumeration headers = message.getAllHeaders();

        while (headers.hasMoreElements()) {
            Header header = (Header) headers.nextElement();

            try {
                message.removeHeader(header.getName());
            } catch (javax.mail.MessagingException me) {
            }
        }

        message.saveChanges();
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String encodeStringToXml(String address) {
        if (address == null) {
            return null;
        }

        //return encodeString(address);
        return address.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                      .replaceAll("\"", "&quot;").replaceAll("&", "&amp;");
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String encodeString(String s) {    	
        s = Entities.encode(s);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            switch (c) {
            case '-':
                sb.append("&ndash;");

                break;

            default:
                sb.append(c);

                break;
            }
        }

        return sb.toString();
    }

    //............................................................

    /**
     * Decode from address(es) of the message into UTF strings. This is a
     * convenience method provided to simplify application code.
     *
     * @param message The message to interogate
     *
     * @return String List of decoded addresses
     *
     * @exception MessagingException if the message contents are invalid
     */
    public static String decodeFrom(Message message)
        throws MessagingException, Exception {
        Address[] xaddresses = message.getFrom();

        return decodeAddresses(xaddresses);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static String decodeFromName(Message message)
        throws MessagingException {
        Address[] xaddresses = message.getFrom();

        return decodeAddressesName(xaddresses);
    }

    /**
     * Decode recipent addresses of the message into UTF strings. This is a
     * convenience method provided to simplify application code.
     *
     * @param message The message to interogate
     * @param type The type of message recipients to decode, i.e. from, to, cc,
     *        etc
     *
     * @return String List of decoded addresses
     *
     * @exception MessagingException if the message contents are invalid
     */
    public static String decodeAddresses(Message message,
        Message.RecipientType type) throws MessagingException, Exception {
        Address[] xaddresses = message.getRecipients(type);

        return decodeAddresses(xaddresses);
    }

    /**
     * Decode mail addresses into UTF strings.
     *
     * <p>
     * This routine is necessary because Java Mail Address.toString() routines
     * convert into MIME encoded strings (ASCII C and B encodings), not UTF.
     * Of course, the returned string is in the same format as MIME, but
     * converts to UTF character encodings.
     * </p>
     *
     * @param addresses The list of addresses to decode
     *
     * @return String List of decoded addresses
     */
    public static String decodeAddresses(Address[] addresses)
        throws Exception {
        StringBuffer xlist = new StringBuffer();

        if (addresses != null) {
            for (int xindex = 0; xindex < addresses.length; xindex++) {
                // at this time, only internet addresses can be decoded
                if (xlist.length() > 0) {
                    xlist.append(", ");
                }

                if (addresses[xindex] instanceof InternetAddress) {
                    InternetAddress xinet = (InternetAddress) addresses[xindex];

                    if (xinet.getPersonal() == null) {
                        xlist.append(xinet.getAddress());
                    } else {
                        // If the address has a ',' in it, we must
                        // wrap it in quotes, or it will confuse the
                        // code that parses addresses separated by commas.
                        String personal = xinet.getPersonal();
                        int idx = personal.indexOf(",");
                        String qStr = ((idx == -1) ? "" : "\"");
                        xlist.append(qStr);
                        xlist.append(personal);
                        xlist.append(qStr);
                        xlist.append(" <");
                        xlist.append(xinet.getAddress());
                        xlist.append(">");
                    }
                } else {
                    // generic, and probably not portable,
                    // but what's a man to do...
                    xlist.append(addresses[xindex].toString());
                }
            }
        }

        return xlist.toString();
    }

    /**
     * Decode mail addresses into UTF strings.
     *
     * <p>
     * This routine is necessary because Java Mail Address.toString() routines
     * convert into MIME encoded strings (ASCII C and B encodings), not UTF.
     * Of course, the returned string is in the same format as MIME, but
     * converts to UTF character encodings.
     * </p>decodeAddresses
     *
     * @param addresses The list of addresses to decode
     *
     * @return String List of decoded addresses
     */
    public static String decodeAddressesName(Address[] addresses) {
        StringBuffer xlist = new StringBuffer();

        if (addresses != null) {
            for (int xindex = 0; xindex < addresses.length; xindex++) {
                // at this time, only internet addresses can be decoded
                if (xlist.length() > 0) {
                    xlist.append(", ");
                }

                if (addresses[xindex] instanceof InternetAddress) {
                    InternetAddress xinet = (InternetAddress) addresses[xindex];

                    if (xinet.getPersonal() == null) {
                        xlist.append(xinet.getAddress());
                    } else {
                        // If the address has a ',' in it, we must
                        // wrap it in quotes, or it will confuse the
                        // code that parses addresses separated by commas.
                        String personal = xinet.getPersonal();
                        int idx = personal.indexOf(",");
                        String qStr = ((idx == -1) ? "" : "\"");
                        xlist.append(qStr);
                        xlist.append(personal);
                        xlist.append(qStr);
                    }
                } else {
                    // generic, and probably not portable,
                    // but what's a man to do...
                    xlist.append(addresses[xindex].toString());
                }
            }
        }

        return xlist.toString();
    }

    /**
     * Decode mail addresses into UTF strings.
     *
     * <p>
     * This routine is necessary because Java Mail Address.toString() routines
     * convert into MIME encoded strings (ASCII C and B encodings), not UTF.
     * Of course, the returned string is in the same format as MIME, but
     * converts to UTF character encodings.
     * </p>decodeAddresses
     *
     * @param addresses The list of addresses to decode
     *
     * @return String List of decoded addresses
     */
    public static String decodeAddressesEmail(Address[] addresses) {
        StringBuffer xlist = new StringBuffer();

        if (addresses != null) {
            for (int xindex = 0; xindex < addresses.length; xindex++) {
                // at this time, only internet addresses can be decoded
                if (xlist.length() > 0) {
                    xlist.append(", ");
                }

                if (addresses[xindex] instanceof InternetAddress) {
                    InternetAddress xinet = (InternetAddress) addresses[xindex];

                    String email = xinet.getAddress();
                    int idx = email.indexOf(",");
                    String qStr = ((idx == -1) ? "" : "\"");
                    xlist.append(qStr);
                    xlist.append(email);
                    xlist.append(qStr);
                } else {
                    // generic, and probably not portable,
                    // but what's a man to do...
                    xlist.append(addresses[xindex].toString());
                }
            }
        }

        return xlist.toString();
    }

    /**
     * Encode UTF strings into mail addresses.
     *
     * @param string DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static InternetAddress[] encodeAddresses(String string,
        String charset) throws MessagingException {
    	if (string == null) {
    		return null;
    	}
    	
        // parse the string into the internet addresses
        // NOTE: these will NOT be character encoded
        InternetAddress[] xaddresses = InternetAddress.parse(string);

        // now encode each to the given character set
        if (charset != null) {
	        for (int xindex = 0; xindex < xaddresses.length; xindex++) {
	            String xpersonal = xaddresses[xindex].getPersonal();
	
	            try {
	                if (xpersonal != null) {
	                	if (charset != null) {
	                		xaddresses[xindex].setPersonal(xpersonal, charset);
	                	} else {
	                		xaddresses[xindex].setPersonal(xpersonal, "ISO-8859-1");
	                	}
	                }
	            } catch (UnsupportedEncodingException xex) {
	                throw new MessagingException(xex.toString());
	            }
	        }
        }

        return xaddresses;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fromName DOCUMENT ME!
     * @param fromEmail DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static InternetAddress getDefaultFromAddress(String fromName,
        String fromEmail) throws MessagingException {
        // encode the address
        String xcharset = "ISO-8859-1";
        InternetAddress[] xaddresses = encodeAddresses(getDefaultFromString(
                    fromName, fromEmail), xcharset);

        return xaddresses[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param fromName DOCUMENT ME!
     * @param fromEmail DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getDefaultFromString(String fromName, String fromEmail) {
        // build a string from the user defined From personal and address
        String xstring = fromName;

        String xaddress = fromEmail;

        if (xaddress == null) {
            xaddress = "unknown";
        }

        if ((xstring != null) && (xstring.length() > 0)) {
            xstring = xstring + " <" + xaddress + ">";
        } else {
            xstring = xaddress;
        }

        return xstring;
    }

    /**
     * DOCUMENT ME!
     *
     * @param replyToName DOCUMENT ME!
     * @param replyToEmail DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static InternetAddress getDefaultReplyTo(String replyToName,
        String replyToEmail) throws MessagingException {
        // build a string from the user defined ReplyTo personal and address
        String xstring = replyToName;

        String xaddress = replyToEmail;

        if (xaddress == null) {
            xaddress = "unknown";
        }

        if ((xstring != null) && (xstring.length() > 0)) {
            xstring = xstring + " <" + xaddress + ">";
        } else {
            xstring = xaddress;
        }

        // encode the address
        String xcharset = "ISO-8859-1";
        InternetAddress[] xaddresses = encodeAddresses(xstring, xcharset);

        return xaddresses[0];
    }

    //............................................................
    public static InternetHeaders getHeadersWithFrom(Message message)
        throws MessagingException {
        Header xheader;
        InternetHeaders xheaders = new InternetHeaders();
        Enumeration xe = message.getAllHeaders();

        for (; xe.hasMoreElements();) {
            xheader = (Header) xe.nextElement();
            xheaders.addHeader(xheader.getName(), xheader.getValue());
        }

        return xheaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static InternetHeaders getHeaders(Message message)
        throws MessagingException {
        Header xheader;
        InternetHeaders xheaders = new InternetHeaders();
        Enumeration xe = message.getAllHeaders();

        for (; xe.hasMoreElements();) {
            xheader = (Header) xe.nextElement();

            if (!xheader.getName().startsWith("From ")) {
                xheaders.addHeader(xheader.getName(), xheader.getValue());
            }
        }

        return xheaders;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param headers DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static void addHeaders(Message message, InternetHeaders headers)
        throws MessagingException {
        Header xheader;
        Enumeration xe = headers.getAllHeaders();

        for (; xe.hasMoreElements();) {
            xheader = (Header) xe.nextElement();
            message.addHeader(xheader.getName(), xheader.getValue());
        }
    }

    //............................................................
    public static void attach(MimeMultipart multipart, Vector attachments,
        String charset) throws MessagingException {
        for (int xindex = 0; xindex < attachments.size(); xindex++) {
            Object xobject = attachments.elementAt(xindex);

            // attach based on type of object
            if (xobject instanceof Part) {
                attach(multipart, (Part) xobject, charset);
            } else if (xobject instanceof File) {
                attach(multipart, (File) xobject, charset);
            } else if (xobject instanceof String) {
                attach(multipart, (String) xobject, charset, Part.ATTACHMENT,
                    false);
            } else if (xobject instanceof vCard) {
                attach(multipart, (vCard) xobject, charset);
            } else {
                throw new MessagingException("Cannot attach objects of type " +
                    xobject.getClass().getName());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param multipart DOCUMENT ME!
     * @param part DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static void attach(MimeMultipart multipart, Part part, String charset)
        throws MessagingException {
        MimeBodyPart xbody = new MimeBodyPart();
        PartDataSource xds = new PartDataSource(part);
        DataHandler xdh = new DataHandler(xds);
        xbody.setDataHandler(xdh);

        int xid = multipart.getCount() + 1;
        String xtext;

        // UNDONE
        //xbody.setContentLanguage( String ); // this could be language from Locale
        //xbody.setContentMD5( String md5 ); // don't know about this yet
        xtext = part.getDescription();

        if (xtext == null) {
            xtext = "Part Attachment: " + xid;
        }

        xbody.setDescription(xtext, charset);

        xtext = getContentDisposition(part).getType();
        xbody.setDisposition(xtext);

        xtext = MessageUtilities.getFileName(part);

        if ((xtext == null) || (xtext.length() < 1)) {
            xtext = "PART" + xid;
        }

        MessageUtilities.setFileName(xbody, xtext, charset);

        multipart.addBodyPart(xbody);
    }

    /**
     * DOCUMENT ME!
     *
     * @param multipart DOCUMENT ME!
     * @param file DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static void attach(MimeMultipart multipart, File file, String charset)
        throws MessagingException {
        // UNDONE how to specify the character set of the file???
        MimeBodyPart xbody = new MimeBodyPart();
        FileDataSource xds = new FileDataSource(file);
        DataHandler xdh = new DataHandler(xds);
        xbody.setDataHandler(xdh);

        //System.out.println(xdh.getContentType());
        // UNDONE
        // xbody.setContentLanguage( String ); // this could be language from Locale
        // xbody.setContentMD5( String md5 ); // don't know about this yet
        xbody.setDescription("File Attachment: " + file.getName(), charset);
        xbody.setDisposition(Part.ATTACHMENT);
        MessageUtilities.setFileName(xbody, file.getName(), charset);

        multipart.addBodyPart(xbody);
    }

    /**
     * DOCUMENT ME!
     *
     * @param multipart DOCUMENT ME!
     * @param text DOCUMENT ME!
     * @param charset DOCUMENT ME!
     * @param disposition DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static void attach(MimeMultipart multipart, String text,
        String charset, String disposition, boolean isHtml)
        throws MessagingException {
        int xid = multipart.getCount() + 1;

        MimeBodyPart xbody = new MimeBodyPart();

        String xname = null;

        if (isHtml) {
            xname = "HTML" + xid + ".html";
            xbody.setContent(text, "text/html" + "; charset=" + charset);
            xbody.setDescription("Html Attachment: " + xname, charset);
        } else {
            xname = "TEXT" + xid + ".txt";
            xbody.setText(text, charset);
            xbody.setDescription("Text Attachment: " + xname, charset);
        }

        // UNDONE
        //xbody.setContentLanguage( String ); // this could be language from Locale
        //xbody.setContentMD5( String md5 ); // don't know about this yet        
        xbody.setDisposition(disposition);
        MessageUtilities.setFileName(xbody, xname, charset);

        multipart.addBodyPart(xbody);
    }

    /**
     * DOCUMENT ME!
     *
     * @param multipart DOCUMENT ME!
     * @param vcard DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static void attach(MimeMultipart multipart, vCard vcard,
        String charset) throws MessagingException {
        String xpath = "/tmp";
        String xcontent = vcard.toString();

        try {
            // write the vCard to a temporary file in UTF-8 format
            File xfile = new File(xpath);
            FileOutputStream xfos = new FileOutputStream(xfile);
            OutputStreamWriter xosw = new OutputStreamWriter(xfos,
                    Charset.defaultCharset().displayName());

            xosw.write(xcontent, 0, xcontent.length());
            xosw.flush();
            xosw.close();
            xfos.close();

            // attach the temporary file to the message
            attach(multipart, xfile, Charset.defaultCharset().displayName());
        } catch (Exception xex) {
            System.out.println("vCard attachment failed: " + xex.toString());
        }
    }

    //............................................................

    /**
     * Decode the contents of the Part into text and attachments.
     *
     * @param part DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     * @param dmailParts DOCUMENT ME!
     * @param chooseHtml DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     * @throws IOException
     */
    public static StringBuffer decodeContent(Part part, StringBuffer buffer,
        Vector dmailParts, boolean chooseHtml, String breakLine)
        throws MessagingException, IOException {
        MessageUtilities.subDecodeContent(part, buffer, dmailParts, chooseHtml, breakLine);

        // If we did not get any body text, scan on more time
        // for a text/plain part that is not 'inline', and use
        // that as a proxy...
        if ((buffer.length() == 0) && (dmailParts != null)) {
            MessageUtilities.scanDmailParts(dmailParts, buffer, chooseHtml, breakLine);
        }

        //En el cas que no podem aconseguir un missatge html agafem el text
        if ((buffer.length() == 0) && (dmailParts != null)) {
            MessageUtilities.scanDmailParts(dmailParts, buffer, false, breakLine);
        }

        return buffer;
    }

    /**
     * Decode the contents of the Part into text and without attachments.
     *
     * @param part DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     * @param dmailParts DOCUMENT ME!
     * @param chooseHtml DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     * @throws IOException
     */
    public static StringBuffer decodeContentWithoutAttachments(Part part,
        StringBuffer buffer, Vector dmailParts, boolean chooseHtml,
        String breakLine) throws MessagingException, IOException {
        MessageUtilities.subDecodeContent(part, buffer, dmailParts, chooseHtml, breakLine);

        // If we did not get any body text, scan on more time
        // for a text/plain part that is not 'inline', and use
        // that as a proxy...
        if ((buffer.length() == 0) && (dmailParts != null)) {
            MessageUtilities.scanDmailParts(dmailParts, buffer, chooseHtml, breakLine);
        }

        //En el cas que no podem aconseguir un missatge html agafem el text
        if ((buffer.length() == 0) && (dmailParts != null)) {
            MessageUtilities.scanDmailParts(dmailParts, buffer, false, breakLine);
        }

        return buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dmailParts DOCUMENT ME!
     * @param contentType DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    protected static StringBuffer scanDmailParts(Vector dmailParts,
        StringBuffer buffer, boolean chooseHtml, String breakLine)
        throws MessagingException {
        if ((buffer.length() == 0) && (dmailParts != null)) {
            int size = dmailParts.size();
            int j = 0;

            for (int i = 0; i < size; i++) {
            	//message/rfc822
            	
                MailPart dmailPart = (MailPart) dmailParts.get(j);

                //ContentType xctype = MessageUtilities.getContentType(dmailPart.getContentType());
                ContentType xctype = MessageUtilities.getContentType(dmailPart.getPart());

                if (xctype.match("text/plain") && !chooseHtml) {
                    String xjcharset = xctype.getParameter("charset");

                    if (xjcharset == null) {
                        // not present, assume ASCII character encoding                    	
                        try {
                            Header xheader;
                            Enumeration xe = dmailPart.getPart().getAllHeaders();

                            for (; xe.hasMoreElements();) {
                                xheader = (Header) xe.nextElement();

                                String aux = xheader.getName().toLowerCase()
                                                    .trim();

                                if (aux.indexOf("subject") > -1) {
                                    int pos1 = aux.indexOf("=?");
                                    int pos2 = aux.indexOf("?q?");

                                    if ((pos1 > -1) && (pos2 > -1)) {
                                        xjcharset = aux.substring(pos1, pos2);
                                    }

                                    break;
                                }
                            }
                        } catch (Exception ex) {
                        }

                        if (xjcharset == null) {
                            xjcharset = Charset.defaultCharset().displayName(); // US-ASCII in JAVA terms
                        }
                    }

                    //String str = JavaScriptFilter.apply(buff.toString());
                    xjcharset = MimeUtility.javaCharset(xjcharset);

                    MessageUtilities.decodeTextPlain(buffer, dmailPart.getPart(), breakLine, xjcharset);

                    dmailParts.removeElementAt(j);

                    break;
                } else if (xctype.match("text/html") && chooseHtml) {
                    String xjcharset = xctype.getParameter("charset");

                    if (xjcharset == null) {
                        // not present, assume ASCII character encoding                    	
                        try {
                            Header xheader;
                            Enumeration xe = dmailPart.getPart().getAllHeaders();

                            for (; xe.hasMoreElements();) {
                                xheader = (Header) xe.nextElement();

                                String aux = xheader.getName().toLowerCase()
                                                    .trim();

                                if (aux.indexOf("subject") > -1) {
                                    int pos1 = aux.indexOf("=?");
                                    int pos2 = aux.indexOf("?q?");

                                    if ((pos1 > -1) && (pos2 > -1)) {
                                        xjcharset = aux.substring(pos1, pos2);
                                    }

                                    break;
                                }
                            }
                        } catch (Exception ex) {
                        }

                        if (xjcharset == null) {
                            xjcharset = Charset.defaultCharset().displayName(); // US-ASCII in JAVA terms
                        }
                    }

                    xjcharset = MimeUtility.javaCharset(xjcharset);

                    MessageUtilities.decodeTextHtml(buffer, dmailPart.getPart(), xjcharset);

                    dmailParts.removeElementAt(j);

                    break;
                } else {
                    j++;
                }
            }
        }

        return buffer;
    }

    /**
     * Given a message that we are replying to, or forwarding,
     *
     * @param part The part to decode.
     * @param buffer The new message body text buffer.
     * @param dmailParts Vector for new message's attachments.
     *
     * @return The buffer being filled in with the body.
     *
     * @throws MessagingException DOCUMENT ME!
     * @throws IOException
     */
    protected static StringBuffer subDecodeContent(Part part,
        StringBuffer buffer, Vector dmailParts, boolean chooseHtml,
        String breakLine) throws MessagingException, IOException {
        boolean attachIt = true;

        // decode based on content type and disposition
        ContentType xctype = MessageUtilities.getContentType(part);

        ContentDisposition xcdisposition = MessageUtilities.getContentDisposition(part);

        if (xctype.match("multipart/*")) {
            attachIt = false;

            Multipart xmulti = (Multipart) MessageUtilities.getPartContent(part);

            int xparts = 0;

            try {
                xparts = xmulti.getCount();
            } catch (MessagingException e) {
                attachIt = true;
                xparts = 0;
            }

            for (int xindex = 0; xindex < xparts; xindex++) {
                MessageUtilities.subDecodeContent(xmulti.getBodyPart(xindex), buffer, dmailParts, chooseHtml, breakLine);
            }
        } else if (xctype.match("message/rfc822")) {
        	MimeMessage newMessage = new MimeMessage((Session) null, part.getInputStream());
            decodeContent(newMessage, buffer, dmailParts, chooseHtml, breakLine);
        } else if (xctype.match("text/plain") && !chooseHtml) {
            if (xcdisposition.match("inline")) {
                attachIt = false;

                String xjcharset = xctype.getParameter("charset");

                if (xjcharset == null) {
                    // not present, assume ASCII character encoding                    	
                    try {
                        Header xheader;
                        Enumeration xe = part.getAllHeaders();

                        for (; xe.hasMoreElements();) {
                            xheader = (Header) xe.nextElement();

                            String aux = xheader.getName().toLowerCase().trim();

                            if (aux.indexOf("subject") > -1) {
                                int pos1 = aux.indexOf("=?");
                                int pos2 = aux.indexOf("?q?");

                                if ((pos1 > -1) && (pos2 > -1)) {
                                    xjcharset = aux.substring(pos1, pos2);
                                }

                                break;
                            }
                        }
                    } catch (Exception ex) {
                    	System.out.print(ex.getMessage());
                    }

                    if (xjcharset == null) {
                        xjcharset = Charset.defaultCharset().displayName(); // US-ASCII in JAVA terms
                    }
                }

                MessageUtilities.decodeTextPlain(buffer, part, breakLine, xjcharset);
            }
        } else if (xctype.match("text/html") && chooseHtml) {
            if (xcdisposition.match("inline")) {
                attachIt = false;

                String xjcharset = xctype.getParameter("charset");

                if (xjcharset == null) {
                    // not present, assume ASCII character encoding                    	
                    try {
                        Header xheader;
                        Enumeration xe = part.getAllHeaders();

                        for (; xe.hasMoreElements();) {
                            xheader = (Header) xe.nextElement();

                            String aux = xheader.getName().toLowerCase().trim();

                            if (aux.indexOf("subject") > -1) {
                                int pos1 = aux.indexOf("=?");
                                int pos2 = aux.indexOf("?q?");

                                if ((pos1 > -1) && (pos2 > -1)) {
                                    xjcharset = aux.substring(pos1, pos2);
                                }

                                break;
                            }
                        }
                    } catch (Exception ex) {
                    }

                    if (xjcharset == null) {
                        xjcharset = Charset.defaultCharset().displayName(); // US-ASCII in JAVA terms
                    }
                }

                MessageUtilities.decodeTextHtml(buffer, part, xjcharset);
            }
        }

        if (attachIt) {
            // UNDONE should simple single line entries be
            //        created for other types and attachments?
            //
            // UNDONE should attachements only be created for "attachments" or all
            // unknown content types?
            if (dmailParts != null) {
                MailPart aux = new MailPart();
                aux.setPart(part);
                aux.setId(dmailParts.size());
                aux.setName(MessageUtilities.encodeStringToXml(
                        MessageUtilities.getPartName(part)));
                aux.setContentType(xctype.getBaseType());
                aux.setSize(part.getSize());

                dmailParts.addElement(aux);
            }
        }

        return buffer;
    }

    /**
     * Get the name of a part. The part is interogated for a valid name from
     * the provided file name or description.
     *
     * @param part The part to interogate
     *
     * @return String containing the name of the part
     *
     * @exception MessagingException if contents of the part are invalid
     *
     * @see javax.mail.Part
     */
    public static String getPartName(Part part) throws MessagingException {
        String xdescription = MessageUtilities.getFileName(part);

        if ((xdescription == null) || (xdescription.length() < 1)) {
            xdescription = part.getDescription();
        }

        if (((xdescription == null) || (xdescription.length() < 1)) &&
                part instanceof MimePart) {
            xdescription = ((MimePart) part).getContentID();
        }

        if ((xdescription == null) || (xdescription.length() < 1)) {
            xdescription = "Message Text";
        }

        return xdescription;
    }

    /**
     * Decode contents of TEXT/PLAIN message parts into UTF encoded strings.
     * Why can't JAVA Mail provide this method?? Who knows!?!?!?
     *
     * @param buffer DOCUMENT ME!
     * @param part DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static StringBuffer decodeTextPlain(StringBuffer buffer, Part part,
        String breakLine, String charset) throws MessagingException {
        // pick off the individual lines of text
        // and append to the buffer
        try {
        	StringBuffer buff = new StringBuffer();
        	
            BufferedReader xreader = MessageUtilities.getTextReader(part.getInputStream(), charset);

            for (String xline; (xline = xreader.readLine()) != null;) {
                buff.append(xline);
                buff.append(breakLine);
            }

            xreader.close();
            
            //String aux = JavaScriptFilter.apply(buff.toString());
            
            buffer.append(buff.toString());

            return buffer;
        } catch (IOException xex) {
            throw new MessagingException(xex.toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param bytes DOCUMENT ME!
     * @param breakLine DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static StringBuffer decodeTextPlain(StringBuffer buffer,
        byte[] bytes, String breakLine, String charset)
        throws MessagingException {
        // pick off the individual lines of text
        // and append to the buffer
        String aux = null;

        try {
            aux = new String(bytes, charset).replaceAll("\n", breakLine);
            //aux = JavaScriptFilter.apply(aux);
        } catch (UnsupportedEncodingException e) {
            new MessagingException(e.getMessage());
        }

        buffer.append(aux);

        return buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param part DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static StringBuffer decodeTextHtml(StringBuffer buffer, Part part, String charset) {
        BufferedReader xreader = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;

        try {
        	charset = MimeUtility.javaCharset(charset);
        	
            xreader = MessageUtilities.getTextReader(part, charset);

            String body = IOUtils.toString(xreader);  
            
            //buffer.append(JavaScriptFilter.apply(body));
            buffer.append(body);

            return buffer;
        } catch (Exception xex) {
            buffer = new StringBuffer();

            return buffer;
        } finally {
            IOUtils.closeQuietly(xreader);
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(baos);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param bytes DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static StringBuffer decodeTextHtml(StringBuffer buffer,
        byte[] bytes, String charset) {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;

        try {
            String body = new String(bytes);
            
            charset = MimeUtility.javaCharset(charset);
            
            //buffer.append(JavaScriptFilter.apply(body));
            buffer.append(body);

            return buffer;
        } catch (Exception xex) {
            buffer = new StringBuffer();

            return buffer;
        } finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(baos);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param part DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static BufferedReader getTextReader(Part part, String charset)
        throws MessagingException {
        try {
            InputStream xis = part.getInputStream(); // transfer decoded only

            // now construct a reader from the decoded stream
            return MessageUtilities.getTextReader(xis, charset);
        } catch (IOException xex) {
            throw new MessagingException(xex.toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param stream DOCUMENT ME!
     * @param charset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedEncodingException DOCUMENT ME!
     */
    public static BufferedReader getTextReader(InputStream stream,
        String charset) throws UnsupportedEncodingException {
        // Sun has a HUGE bug in their io code. They use a cute coding trick
        // that appends the charset to a prefix to create a Class name that
        // they then attempt to load. The problem is that if the charset is
        // not one that they "recognize", and the name has something like a
        // dash character in it, the resulting Class name has an invalid
        // character in it. This results in an IllegalArgumentException
        // instead of the UnsupportedEncodingException that we expect. Thus,
        // we need to catch the undocumented exception.
        InputStreamReader inReader;

        try {
            charset = MimeUtility.javaCharset(charset); // just to be sure
            inReader = new InputStreamReader(stream, charset);
        } catch (UnsupportedEncodingException ex) {
            inReader = null;
        } catch (IllegalArgumentException ex) {
            inReader = null;
        }

        if (inReader == null) {
            // This is the "bug" case, and we need to do something
            // that will at least put text in front of the user...
            inReader = new InputStreamReader(stream,
                    Charset.defaultCharset().displayName());
        }

        return new BufferedReader(inReader);
    }

    //............................................................

    /**
     * Get a textual description of a message. This is a helper method for
     * applications.
     *
     * @param msg The message to interogate
     *
     * @return String containing the description of the message
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static String getMessageDescription(Message msg)
        throws MessagingException {
        StringBuffer xbuffer = new StringBuffer(1024);
        MessageUtilities.getPartDescription(msg, xbuffer, "", true);

        return xbuffer.toString();
    }

    /**
     * Get a textual description of a part.
     *
     * @param part The part to interogate
     * @param buf a string buffer for the description
     * @param prefix a prefix for each line of the description
     * @param recurse boolean specifying wether to recurse through sub-parts or
     *        not
     *
     * @return StringBuffer containing the description of the part
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static StringBuffer getPartDescription(Part part, StringBuffer buf,
        String prefix, boolean recurse) throws MessagingException {
        if (buf == null) {
            return buf;
        }

        ContentType xctype = MessageUtilities.getContentType(part);

        String xvalue = xctype.toString();
        buf.append(prefix);
        buf.append("Content-Type: ");
        buf.append(xvalue);
        buf.append('\n');

        xvalue = part.getDisposition();
        buf.append(prefix);
        buf.append("Content-Disposition: ");
        buf.append(xvalue);
        buf.append('\n');

        xvalue = part.getDescription();
        buf.append(prefix);
        buf.append("Content-Description: ");
        buf.append(xvalue);
        buf.append('\n');

        xvalue = MessageUtilities.getFileName(part);
        buf.append(prefix);
        buf.append("Content-Filename: ");
        buf.append(xvalue);
        buf.append('\n');

        if (part instanceof MimePart) {
            MimePart xmpart = (MimePart) part;
            xvalue = xmpart.getContentID();
            buf.append(prefix);
            buf.append("Content-ID: ");
            buf.append(xvalue);
            buf.append('\n');

            String[] langs = xmpart.getContentLanguage();

            if (langs != null) {
                buf.append(prefix);
                buf.append("Content-Language: ");

                for (int pi = 0; pi < langs.length; ++pi) {
                    if (pi > 0) {
                        buf.append(", ");
                    }

                    buf.append(xvalue);
                }

                buf.append('\n');
            }

            xvalue = xmpart.getContentMD5();
            buf.append(prefix);
            buf.append("Content-MD5: ");
            buf.append(xvalue);
            buf.append('\n');

            xvalue = xmpart.getEncoding();
            buf.append(prefix);
            buf.append("Content-Encoding: ");
            buf.append(xvalue);
            buf.append('\n');
        }

        buf.append('\n');

        if (recurse && xctype.match("multipart/*")) {
            Multipart xmulti = (Multipart) MessageUtilities.getPartContent(part);

            int xparts = xmulti.getCount();

            for (int xindex = 0; xindex < xparts; xindex++) {
                MessageUtilities.getPartDescription(xmulti.getBodyPart(xindex),
                    buf, (prefix + "   "), true);
            }
        }

        return buf;
    }

    /**
     * Get the content dispostion of a part. The part is interogated for a
     * valid content disposition. If the content disposition is missing, a
     * default disposition is created based on the type of the part.
     *
     * @param part The part to interogate
     *
     * @return ContentDisposition of the part
     *
     * @throws MessagingException DOCUMENT ME!
     *
     * @see javax.mail.Part
     */
    public static ContentDisposition getContentDisposition(Part part)
        throws MessagingException {
        String[] xheaders = part.getHeader("Content-Disposition");

        try {
            if (xheaders != null) {
                return new ContentDisposition(xheaders[0]);
            }
        } catch (ParseException xex) {
            throw new MessagingException(xex.toString());
        }

        // set default disposition based on part type
        if (part instanceof MimeBodyPart) {
            return new ContentDisposition("attachment");
        }

        return new ContentDisposition("inline");
    }

    /**
     * A 'safe' version of JavaMail getContentType(), i.e. don't throw
     * exceptions. The part is interogated for a valid content type. If the
     * content type is missing or invalid, a default content type of
     * "text/plain" is assumed, which is suggested by the MIME standard.
     *
     * @param part The part to interogate
     *
     * @return ContentType of the part
     *
     * @see javax.mail.Part
     */
    public static ContentType getContentType(Part part) {
        String xtype = null;

        try {
            xtype = part.getContentType();
        } catch (MessagingException xex) {
        }

        if (xtype == null) {
            xtype = "text/plain"; // MIME default content type if missing
        }

        ContentType xctype = null;

        try {
            xctype = new ContentType(xtype.toLowerCase());
        } catch (ParseException xex) {
        }

        if (xctype == null) {
            xctype = new ContentType("text", "plain", null);
        }

        return xctype;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xtype DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ContentType getContentType(String xtype) {
        if (xtype == null) {
            xtype = "text/plain"; // MIME default content type if missing
        }

        ContentType xctype = null;

        try {
            xctype = new ContentType(xtype.toLowerCase());
        } catch (ParseException xex) {
        }

        if (xctype == null) {
            xctype = new ContentType("text", "plain", null);
        }

        return xctype;
    }

    /**
     * Determin if the message is high-priority.
     *
     * @param message the message to examine
     *
     * @return true if the message is high-priority
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static boolean isHighPriority(Message message)
        throws MessagingException {
        if (message instanceof MimeMessage) {
            MimeMessage xmime = (MimeMessage) message;
            String xpriority = xmime.getHeader("Importance", null);

            if (xpriority != null) {
                xpriority = xpriority.toLowerCase();

                if (xpriority.indexOf("high") == 0) {
                    return true;
                }
            }

            // X Standard: X-Priority: 1 | 2 | 3 | 4 | 5 (lowest)
            xpriority = xmime.getHeader("X-Priority", null);

            if (xpriority != null) {
                xpriority = xpriority.toLowerCase();

                if ((xpriority.indexOf("1") == 0) ||
                        (xpriority.indexOf("2") == 0)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * Determin if the message is high-priority.
     *
     * @param message the message to examine
     *
     * @return true if the message is high-priority
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static boolean isHighPriority(String fullHeaders) throws MessagingException {
        if (fullHeaders != null) {
        	fullHeaders = fullHeaders.toLowerCase();
        	
        	int pos1 = fullHeaders.indexOf("importance: high");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("importance:high");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority: 1");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority:1");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority: 2");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority:2");
        	if (pos1 > -1) {
        		return true;
        	}
        }

        return false;
    }

    /**
     * Determin if the message is low-priority.
     *
     * @param message the message to examine
     *
     * @return true if the message is low-priority
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static boolean isLowPriority(Message message)
        throws MessagingException {
        if (message instanceof MimeMessage) {
            MimeMessage xmime = (MimeMessage) message;
            String xpriority = xmime.getHeader("Importance", null);

            if (xpriority != null) {
                xpriority = xpriority.toLowerCase();

                if (xpriority.indexOf("low") == 0) {
                    return true;
                }
            }

            // X Standard: X-Priority: 1 | 2 | 3 | 4 | 5 (lowest)
            xpriority = xmime.getHeader("X-Priority", null);

            if (xpriority != null) {
                xpriority = xpriority.toLowerCase();

                if ((xpriority.indexOf("4") == 0) ||
                        (xpriority.indexOf("5") == 0)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * Determin if the message is high-priority.
     *
     * @param message the message to examine
     *
     * @return true if the message is high-priority
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static boolean isLowPriority(String fullHeaders) throws MessagingException {
        if (fullHeaders != null) {
        	fullHeaders = fullHeaders.toLowerCase();
        	
        	int pos1 = fullHeaders.indexOf("importance: low");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("importance:low");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority: 4");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority:4");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority: 5");
        	if (pos1 > -1) {
        		return true;
        	}
        	
        	pos1 = fullHeaders.indexOf("x-priority:5");
        	if (pos1 > -1) {
        		return true;
        	}
        }

        return false;
    }

    /**
     * A 'safe' version of JavaMail getFileName() which doesn't throw
     * exceptions. Encoded filenames are also decoded if necessary. Why
     * doesn't JAVA Mail do this?
     *
     * @param part The part to interogate
     *
     * @return File name of the part, or null if missing or invalid
     *
     * @see javax.mail.Part
     */
    public static String getFileName(Part part) {
        if (part == null) {
            return null;
        }

        String xname = null;

        try {
            xname = part.getFileName();
        } catch (MessagingException xex) {
        }

        // decode the file name if necessary
        if ((xname != null) && xname.startsWith("=?")) {
            try {
                xname = MimeUtility.decodeWord(xname);
            } catch (Exception xex) {
            }
        }

        return xname;
    }

    /**
     * A better version of setFileName() which will encode the name if
     * necessary. Why doesn't JAVA Mail do this?
     *
     * @param part the part to manipulate
     * @param name the give file name encoded in UTF (JAVA)
     * @param charset the encoding character set
     *
     * @throws MessagingException DOCUMENT ME!
     */
    public static void setFileName(Part part, String name, String charset)
        throws MessagingException {
        try {
            name = MimeUtility.encodeWord(name, charset, null);
        } catch (Exception xex) {
        }

        part.setFileName(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean haveAttachment(Message message) {
        ContentType xct = MessageUtilities.getContentType(message);

        if (xct.match("application/pkcs7-signature") || // new implementations
                xct.match("application/x-pkcs7-signature") || // old implementations
                xct.match("multipart/signed")) { // internet standard

            // signed by some means
            return false;
        } else if (xct.match("application/pkcs7-mime") || // new implementations
                xct.match("application/x-pkcs7-mime") || // old implementations
                xct.match("multipart/encrypted")) { // internet standard

            // encrypted or enveloped by some means
            return false;
        } else if (xct.match("multipart/mixed")) {
            // attachments of some type
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws UnsupportedEncodingException DOCUMENT ME!
     * @throws EmailException DOCUMENT ME!
     * @throws MessagingException DOCUMENT ME!
     */
    public static void main(String[] args)
        throws UnsupportedEncodingException, EmailException, MessagingException {
        InternetAddress[] aux1 = new InternetAddress[5];
        aux1[0] = new InternetAddress("duroty@iigov.net", "Jordi Marquès");
        aux1[1] = new InternetAddress("duroty@iigov.net", "Jordi Marquès");
        aux1[2] = new InternetAddress("duroty@iigov.net", "Jordi Marquès");
        aux1[3] = new InternetAddress("duroty@iigov.net", "Jordi Marquès");
        aux1[4] = new InternetAddress("duroty@iigov.net", "Jordi Marquès");

        System.out.println(MessageUtilities.decodeAddressesEmail(aux1));

        HtmlEmail email = new HtmlEmail();
        email.setHostName("10.0.0.68");
        email.setFrom("duroty@iigov.net");
        email.addReplyTo("duroty@iigov.net");

        email.addTo("cagao@ii.org");

        email.addCc("raul1@iigov.org");
        email.addCc("raul2@iigov.org");
        email.addCc("raul3@iigov.org");
        email.addCc("raul4@iigov.org");
        email.addCc("raul5@iigov.org");

        email.addBcc("caca1@iigov.org");

        email.setHtmlMsg("<html>la merda fa pudor</html>");

        email.buildMimeMessage();

        MimeMessage mime = email.getMimeMessage();

        System.out.println(MessageUtilities.decodeAddressesEmail(
                mime.getAllRecipients()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMessageSize(MimeMessage mime) {
        int size = 0;

        Enumeration e = null;

        try {
            e = mime.getAllHeaders();

            while (e.hasMoreElements()) {
                size += ((Header) e.nextElement()).toString().length();
            }
        } catch (MessagingException me) {
        }

        size += getPartSize(mime);

        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param part DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int getPartSize(Part part) {
        int size = 0;

        try {
            boolean attachIt = true;
            ContentType xctype = MessageUtilities.getContentType(part);
            ContentDisposition xcdisposition = MessageUtilities.getContentDisposition(part);

            if (xctype.match("multipart/*")) {
                attachIt = false;

                Multipart xmulti = (Multipart) MessageUtilities.getPartContent(part);

                int xparts = xmulti.getCount();

                for (int xindex = 0; xindex < xparts; xindex++) {
                    MessageUtilities.getPartSize(xmulti.getBodyPart(xindex));
                }
            } else if (xctype.match("text/plain")) {
                if (xcdisposition.match("inline")) {
                    attachIt = false;
                    size += sizeInline(part);
                }
            } else if (xctype.match("text/html")) {
                if (xcdisposition.match("inline")) {
                    attachIt = false;
                    size += sizeInline(part);
                }
            }

            if (attachIt) {
                size += IOUtils.toByteArray(part.getInputStream()).length;
            }
        } catch (MessagingException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }

        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param part DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int sizeInline(Part part) {
        int size = 0;

        try {
            size += IOUtils.toByteArray(part.getInputStream()).length;
        } catch (IOException e) {
        } catch (MessagingException e) {
        } catch (Exception e) {
        }

        return size;
    }
}
