/*
** $Id: PartDataSource.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import javax.mail.MessagingException;
import javax.mail.Part;


/**
 * Class PartDataSource implements the DataSource interface for the the JavaBeans
 * Activation Framework. This class allows a Part to be used as a supplier of type
 * and data; allowing for messages to be contructed without knowing the type of the
 * part being attached.
 */
public class PartDataSource implements DataSource {
    /**
     * DOCUMENT ME!
     */
    private Part part_;

    /**
     * Creates a new PartDataSource object.
     *
     * @param part DOCUMENT ME!
     */
    public PartDataSource(Part part) {
        part_ = part;
    }

    /**
     * This method returns the MIME type of the data in the form of a string.
     * <p>
     * It always return a valid type; either the MIME type of the part or
     * "application/octet-stream" if the data type can not be determined.
     *
     * @return the MIME type of the data in the form of a string
     */
    public String getContentType() {
        String xtype = MessageUtilities.getContentType(part_).toString();

        if (xtype == null) {
            xtype = "application/octet-stream";
        }

        return xtype;
    }

    /**
     * Return the name of this object, i.e. file name of the part.
     *
     * @return name of the object
     */
    public String getName() {
        String xname = MessageUtilities.getFileName(part_);

        if (xname == null) {
            xname = "Untitled";
        }

        return xname;
    }

    /**
     * This method returns an InputStream representing the data, i.e. input stream of the part.
     *
     * @return an InputStream representing the data
     */
    public InputStream getInputStream() throws IOException {
        InputStream result = null;

        try {
            result = part_.getInputStream();
        } catch (MessagingException ex) {
            throw new IOException("MessagingException: " + ex.getMessage());
        }

        return result;
    }

    /**
     * This method returns an OutputStream where data can be written.
     *
     * @return an OutputStream where data can be written
     */
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("PartDataSource is read-only");
    }
}
