/*
** $Id: ContentDisposition.java,v 1.1 2006/03/08 09:07:01 durot Exp $
**
** Copyright (c) 2000-2003 Jeff Gay
** on behalf of ICEMail.org <http://www.icemail.org>
** Copyright (c) 1999 by Jeff Gay
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
**
*/
package com.duroty.utils.mail;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.mail.internet.HeaderTokenizer;
import javax.mail.internet.ParseException;


/**
 * This class represents a MIME ContentDisposition value.
 * It provides methods to parse a MIME formatted string into the individual
 * disposition components, access the components, and to generate a MIME
 * style ContentType string.
 */
public class ContentDisposition {
    /**
     * DOCUMENT ME!
     */
    String type_;

    /**
     * DOCUMENT ME!
     */
    Hashtable params_;

    /**
     * Construct a ContentDisposition by parsing the MIME formatted string.
     *
     * @param header the MIME formated string
     */
    public ContentDisposition(String header) throws ParseException {
        type_ = null;
        params_ = new Hashtable(5); // max number of MIME parameters

        // parse the header line looking for the MIME specifics
        if (header == null) {
            type_ = new String("default");
        }

        String xname = null;
        String xvalue = null;
        HeaderTokenizer.Token xtoken;

        HeaderTokenizer xtkr = new HeaderTokenizer(header, HeaderTokenizer.MIME);

        do {
            xtoken = xtkr.next();

            switch (xtoken.getType()) {
            case ';':

                // end of a parameter
                if (type_ == null) {
                    if ((xname == null) || (xvalue != null)) {
                        throw new ParseException("Missing disposition type");
                    }

                    type_ = xname;
                } else if ((xname == null) || (xvalue == null)) {
                    throw new ParseException("Invalid disposition parameter");
                } else {
                    params_.put(xname, xvalue);
                }

                // reset for next parameter
                xname = null;
                xvalue = null;

                break;

            case '=':

                if ((xname == null) || (xvalue != null)) {
                    throw new ParseException("Invalid disposition parameter");
                }

                break;

            case HeaderTokenizer.Token.QUOTEDSTRING:
                xtoken = new HeaderTokenizer.Token(HeaderTokenizer.Token.ATOM,
                        '"' + xtoken.getValue() + '"');

            case HeaderTokenizer.Token.ATOM:

                if (xname == null) {
                    xname = xtoken.getValue();
                } else if (xvalue == null) {
                    xvalue = xtoken.getValue();
                } else {
                    xvalue = xvalue + xtoken.getValue();
                }

                break;

            case HeaderTokenizer.Token.COMMENT:
                break;

            case HeaderTokenizer.Token.EOF:

                if (xname != null) {
                    if (xvalue != null) {
                        // parameter name and value pair
                        params_.put(xname, xvalue);
                    } else {
                        // type
                        if (type_ != null) {
                            throw new ParseException(
                                "Duplicate disposition type");
                        }

                        type_ = xname;
                    }
                }

                break;
            }
        } while (xtoken.getType() != HeaderTokenizer.Token.EOF);

        // last check to make sure that at lease type was specific
        if (type_ == null) {
            throw new ParseException("Missing disposition type");
        }
    }

    /**
     * Get the disposition type.
     *
     * @return the disposition type
     */
    public String getType() {
        return type_;
    }

    /**
     * Match with the given disposition type.
     *
     * @param type the disposition type to match
     */
    public boolean match(String type) {
        return type_.equals(type);
    }

    /**
     * Get a parameter by name.
     *
     * @return the parameter value or null
     */
    public String getParameter(String name) {
        return (String) params_.get(name);
    }

    /**
     * Get the parameters formatted as a MIME string.
     *
     * @return the parameters formatted as a MIME string
     */
    public String getValues() {
        String x822 = type_ + ';';

        for (Enumeration xe = params_.keys(); xe.hasMoreElements();) {
            String xname = (String) xe.nextElement();
            String xvalue = (String) params_.get(xname);
            x822 = x822 + xname + '=' + xvalue + ';';
        }

        return x822;
    }

    /**
     * Format this ContentDisposition as a MIME string.
     */
    public String toString() {
        return "Content-Disposition: " + getValues();
    }
}
