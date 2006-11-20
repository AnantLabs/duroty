/*
** $Id: vCardReader.java,v 1.1 2006/03/08 09:07:01 durot Exp $
**
** Copyright (c) 2001-2003 Jeff Gay
** on behalf of ICEMail.org <http://www.icemail.org>
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
package com.duroty.utils.mail.addressbook;

import netscape.ldap.util.ByteBuf;
import netscape.ldap.util.MimeBase64Decoder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.nio.charset.Charset;


/**
 * vCard defines the profile of the MIME Content-Type [MIME-DIR] for directory information for
 * a white-pages person object, based on an electronic business card.
 * The profile definition is independent of any particular directory service or protocol.
 * The profile is defined for representing and exchanging
 * a variety of information about an individual.
 * This format is described in the Internet draft - vCard MIME Directory Profile (RFC 2426).
 *
 * This class implements an reader for vCards. You can construct an object of this class to parse
 * data in vCard format and manipulate the data as individual vCard objects.
 *
 * This reader parses both V2.1 and V3.0 formats, however the contents of the vCard must
 * be interpreted by the user, i.e. no rules as to the use of the data are implied.
 */
public class vCardReader {
    /**
     * DOCUMENT ME!
     */
    private static final int Debug_ = 0;

    /**
     * BNF:
     *   vcard = [group "."] "BEGIN" ":" "VCARD" 1*CRLF
     *           1*(contentline) ; A vCard object MUST include the VERSION, FN and N types.
     *           [group "."] "END" ":" "VCARD" 1*CRLF
     *
     *   vcard = "BEGIN" [ws] ":" [ws] "VCARD" [ws] 1*CRLF
     *           items *CRLF
     *           "END" [ws] ":" [ws] "VCARD"
     */
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * DOCUMENT ME!
     */
    public static final String DIGIT = "1234567890";

    /**
     * DOCUMENT ME!
     */
    public static final String LWSP = " \t";

    /**
     * DOCUMENT ME!
     */
    public static final String IANA = ALPHA + DIGIT + '-';

    /**
     * DOCUMENT ME!
     */
    public static final String UNSAFE = "\";:,"; // and controls

    /**
     * DOCUMENT ME!
     */
    protected BufferedReader reader_;

    /**
     * DOCUMENT ME!
     */
    protected int currentLine_;

    /**
     * Constructs a reader to parse the vCard data read from a specified file using the default encoding.
     *
     * @param fileName name of the file to open
     * @exception FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     */
    public vCardReader(String fileName) throws FileNotFoundException {
        this(new FileInputStream(fileName));
    }

    /**
     * Constructs a reader to parse the vCard data read from a specified file using the given encoding.
     *
     * @param fileName name of the file to open
     * @param encoding
     * @exception FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     * @exception UnsupportedEncodingException if the named encoding is not supported
     */
    public vCardReader(String fileName, String encoding)
        throws FileNotFoundException, UnsupportedEncodingException {
        this(new FileInputStream(fileName), encoding);
    }

    /**
     * Constructs a reader to parse the vCard data read from the given input stream using the default encoding.
     *
     * @param is the given input stream
     */
    public vCardReader(InputStream is) {
        reader_ = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Constructs a reader to parse the vCard data read from the given input stream using the given encoding.
     *
     * @param is the given input stream
     * @param encoding the name of the encoding to use
     * @exception UnsupportedEncodingException if the named encoding is not supported
     */
    public vCardReader(InputStream is, String encoding)
        throws UnsupportedEncodingException {
        reader_ = new BufferedReader(new InputStreamReader(is, encoding));
    }

    /**
     * Close the reader and underlying streams.
     *
     * @exception IOException if an IO error occurs
     */
    public void close() throws IOException {
        reader_.close();
    }

    /**
     * Return the next vCard from the reader.
     *
     * BNF:
     *   vcard_entity = 1*(vcard)
     *
     * @return the next vCard or null if end of stream
     * @exception IOException if an IO error occurs
     */
    public vCard getCard() throws IOException {
        return parseCard();
    }

    /**
     * Decode the data in the given vCard property.
     *
     * @param property the given property to decode
     */
    public static void decodeProperty(vCardProperty property) {
        // decode the value if necessary
        vCardParam xparam = property.getParam("ENCODING");

        // V3.0 supports:
        //   special escapes
        //   "B" (BASE64)
        // V2.1 supports:
        //   "7BIT" / "8BIT" / "QUOTED-PRINTABLE" / "BASE64" / "X-" word
        if (xparam == null) {
            // decode special escapes if found
            String xvalue = (String) property.value_;

            if (xvalue.indexOf('\\') < 0) {
                return; // nothing
            }

            int xlength = xvalue.length();
            StringBuffer xbuffer = new StringBuffer(xlength);
            int xindex = 0;

            while (xindex < xlength) {
                if (xvalue.charAt(xindex) == '\\') {
                    // convert the escaped character if possible
                    try {
                        char xchar = xvalue.charAt(xindex + 1);

                        if ((xchar == 'n') || (xchar == 'N')) {
                            xchar = '\n';
                        }

                        xbuffer.append(xchar);
                    } catch (Exception xex) {
                    }

                    // bump past the encoding
                    xindex++;
                    xindex++;
                } else {
                    xbuffer.append(xvalue.charAt(xindex));
                    xindex++;
                }
            }

            property.value_ = xbuffer.toString();
        } else if (xparam.value_.equalsIgnoreCase("7BIT")) {
            // value is ASCII 7BIT characters which has already been converted to UNICODE by the JVM
        } else if (xparam.value_.equalsIgnoreCase("8BIT")) {
            // value is ASCII 8BIT characters which has already been converted to UNICODE by the JVM
        } else if (xparam.value_.equalsIgnoreCase("QUOTED-PRINTABLE")) {
            String xvalue = (String) property.value_;
            int xlength = xvalue.length();
            StringBuffer xbuffer = new StringBuffer(xlength);
            int xindex = 0;

            while (xindex < xlength) {
                if (xvalue.charAt(xindex) == '=') {
                    // convert the unprintable to the control character if possible
                    try {
                        String xhex = xvalue.substring(xindex + 1, xindex + 3);
                        byte[] xbytes = new byte[1];
                        xbytes[0] = Byte.parseByte(xhex, 16);
                        xbuffer.append(new String(xbytes,
                                Charset.defaultCharset().displayName()));
                    } catch (Exception xex) {
                    }

                    // bump past the encoding
                    xindex++;
                    xindex++;
                    xindex++;
                } else {
                    xbuffer.append(xvalue.charAt(xindex));
                    xindex++;
                }
            }

            property.value_ = xbuffer.toString();
        } else if (xparam.value_.equalsIgnoreCase("B") ||
                xparam.value_.equalsIgnoreCase("BASE64")) {
            MimeBase64Decoder xdecoder = new MimeBase64Decoder();
            ByteBuf xin = new ByteBuf((String) property.value_);
            ByteBuf xout = new ByteBuf();
            xdecoder.translate(xin, xout);
            property.value_ = xout;
        }
    }

    /**
     * Parse a vCard from the reader.
     *
     * @return a vCard or null if end of the stream
     * @exception IOException if an IO error occurs
     */
    protected vCard parseCard() throws IOException {
        String xline = getLine();

        if (xline == null) {
            return null;
        }

        // parse the BEGIN
        int xindex = xline.indexOf(':');

        if (xindex < 0) {
            return null;
        }

        String xgroup = null;
        String xname = xline.substring(0, xindex);
        String xvalue = xline.substring(xindex + 1);
        xindex = xname.indexOf('.');

        if ((xindex > 0) && (xindex < (xname.length() - 1))) {
            xgroup = xname.substring(0, xindex);
            xname = xname.substring(xindex + 1);
        }

        xname = xname.trim();
        xvalue = xvalue.trim();

        if (!xname.equalsIgnoreCase("begin") ||
                !xvalue.equalsIgnoreCase("vcard")) {
            return null;
        }

        // create an initial vCard
        vCard xvcard = new vCard();
        xvcard.addProperty(new vCardProperty(xgroup, xname, xvalue, false));

        // parse the contents, looking for the END
        while (true == true) {
            xline = getLine();

            if (xline == null) {
                return null;
            }

            xindex = xline.indexOf(':');

            if (xindex < 0) {
                return null;
            }

            xgroup = null;
            xname = xline.substring(0, xindex);
            xvalue = xline.substring(xindex + 1);
            xindex = xname.indexOf('.');

            if ((xindex > 0) && (xindex < (xname.length() - 1))) {
                xgroup = xname.substring(0, xindex);
                xname = xname.substring(xindex + 1);
            }

            xname = xname.trim();
            xvalue = xvalue.trim();

            // parse the END if possible
            if (xname.equalsIgnoreCase("end") &&
                    xvalue.equalsIgnoreCase("vcard")) {
                xvcard.addProperty(new vCardProperty(xgroup, xname, xvalue,
                        false));

                break;
            }

            // parse the contentline
            // BNF: V3.0
            //   contentline  = [group "."] name *(";" param ) ":" value
            //
            // BNF: V2.1
            //   item         = [groups "."] name [params] ":" value
            //   params       = ";" [ws] paramlist
            String xparamlist = null;
            xindex = xname.indexOf(';');

            if (xindex >= 0) {
                xparamlist = xname.substring(xindex);
                xname = xname.substring(0, xindex);
                xname = xname.trim();

                vCardProperty xvcardproperty = new vCardProperty(xgroup, xname,
                        xvalue, true);
                parseParams(xparamlist, xvcardproperty);
                decodeProperty(xvcardproperty);
                xvcard.addProperty(xvcardproperty);
            } else {
                vCardProperty xvcardproperty = new vCardProperty(xgroup, xname,
                        xvalue, true);
                decodeProperty(xvcardproperty);
                xvcard.addProperty(xvcardproperty);
            }

            if (Debug_ > 5) {
                System.out.println("G: [" + xgroup + "] N: [" + xname +
                    "] P: [" + xparamlist + "] V: " + xvalue);
            }
        }

        return xvcard;
    }

    // BNF: V3.0

    /**
     * DOCUMENT ME!
     *
     * @param paramlist DOCUMENT ME!
     * @param property DOCUMENT ME!
     */
    protected void parseParams(String paramlist, vCardProperty property) {
        // look for semicolon marking start of param
        if (paramlist.indexOf(';') != 0) {
            return; // error
        }

        // skip white space (V2.1)
        int xlength = paramlist.length() - 1;
        int xindex = 1;

        while ((xindex <= xlength) &&
                (LWSP.indexOf(paramlist.charAt(xindex)) >= 0)) {
            xindex++;
        }

        if (xindex >= xlength) {
            return; // error
        }

        // parse the name
        int xstart = xindex;

        while ((xindex <= xlength) &&
                (IANA.indexOf(paramlist.charAt(xindex)) >= 0)) {
            xindex++;
        }

        if (xstart == xindex) {
            return; // error
        }

        String xname = paramlist.substring(xstart, xindex);

        // skip white space (V2.1)
        while ((xindex <= xlength) &&
                (LWSP.indexOf(paramlist.charAt(xindex)) >= 0)) {
            xindex++;
        }

        // parse the value if present
        if ((xindex <= xlength) && (paramlist.charAt(xindex) == '=')) {
            xindex++;

            if (xindex > xlength) {
                return; // error
            }

            // skip white space (V2.1)
            while ((xindex <= xlength) &&
                    (LWSP.indexOf(paramlist.charAt(xindex)) >= 0)) {
                xindex++;
            }

            if (xindex > xlength) {
                return; // error
            }

            // parse param value (2.1) or list (3.0)
            do {
                if (paramlist.charAt(xindex) == ',') {
                    xindex++;
                }

                String xvalue = null;

                if (paramlist.charAt(xindex) == '"') {
                    xindex++;
                    xstart = xindex;

                    while ((xindex <= xlength) &&
                            (paramlist.charAt(xindex) != '"')) {
                        xindex++;
                    }

                    if (xstart == xindex) {
                        return; // error
                    }

                    if (paramlist.charAt(xindex) != '"') {
                        return; // error
                    }

                    xvalue = paramlist.substring(xstart, xindex);
                    xindex++;
                } else {
                    xstart = xindex;

                    while ((xindex <= xlength) &&
                            (UNSAFE.indexOf(paramlist.charAt(xindex)) < 0)) {
                        xindex++;
                    }

                    if (xstart == xindex) {
                        return; // error
                    }

                    xvalue = paramlist.substring(xstart, xindex);
                }

                property.addParam(new vCardParam(xname, xvalue));
            } while ((xindex <= xlength) && (paramlist.charAt(xindex) == ','));
        } else {
            // there's no value for the parameter, so assume this is an abbreviation for
            //   TYPE=<value> in V2.1
            property.addParam(new vCardParam("TYPE", xname));
        }

        // skip white space (V2.1)
        while ((xindex <= xlength) &&
                (LWSP.indexOf(paramlist.charAt(xindex)) >= 0)) {
            xindex++;
        }

        // check for another param
        if ((xindex <= xlength) && (paramlist.charAt(xindex) == ';')) {
            parseParams(paramlist.substring(xindex), property);
        }
    }

    /**
     * Get the next complete property line from the reader. The line is unfolded if necessary.
     *
     * @return the property line
     * @throws IOException if an IO exception occurs
     */
    protected String getLine() throws IOException {
        String xline = null;

        // skip past empty lines
        do {
            xline = reader_.readLine();
            currentLine_++;
        } while ((xline != null) && (xline.length() == 0));

        // return if there's nothing left
        if (xline == null) {
            return null;
        }

        // unfold lines if necessary
        while (reader_.ready()) {
            reader_.mark(2000);

            String xfold = reader_.readLine();
            reader_.reset();

            if ((xfold == null) || (xfold.length() == 0)) {
                break;
            }

            if (LWSP.indexOf(xfold.charAt(0)) >= 0) {
                reader_.readLine();
                currentLine_++;

                int xindex = 0;

                while (LWSP.indexOf(xfold.charAt(xindex)) >= 0) {
                    xindex++;
                }

                xline = xline.concat(xfold.substring(xindex));

                continue;
            }

            // unfold QUOTED-PRINTABLE encodings too (version 2.1)
            if (xline.endsWith("=") && (xline.indexOf("QUOTED-PRINTABLE") > 0)) {
                reader_.readLine();
                currentLine_++;
                xline = xline.substring(0, xline.length() - 1);
                xline = xline.concat(xfold);

                continue;
            }

            break;
        }

        return xline;
    }
}
