/*
** $Id: ViewAsDataSource.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;


/**
 * Class ViewAsDataSource implements the DataSource interface for the the JavaBeans
 * Activation Framework. This class allows any data source to be typed to a different
 * MIME type, and therefore allowing the data source to be viewed as a different type.
 *
 * @see QuickViewer
 */
public class ViewAsDataSource implements DataSource {
    /**
     * DOCUMENT ME!
     */
    private MimeType mimeType_;

    /**
     * DOCUMENT ME!
     */
    private DataSource dataSource_;

    /**
     * DOCUMENT ME!
     */
    private String name_;

    /**
     * Construct a data source of the given type from the given data source and name.
     *
     * @param mimeType the new MIME type of the data source
     * @param dataSource the data source of the message part
     * @param name the new name of the data source
     */
    public ViewAsDataSource(String mimeType, DataSource dataSource, String name)
        throws MimeTypeParseException {
        mimeType_ = new MimeType(mimeType);
        dataSource_ = dataSource;
        name_ = name;
    }

    /**
     * Construct a data source of the given type from the given data handler.
     *
     * @param mimeType the new MIME type of the data source
     * @param dataHandler the data handler of the message part
     */
    public ViewAsDataSource(String mimeType, DataHandler handler)
        throws MimeTypeParseException {
        mimeType_ = new MimeType(mimeType);
        dataSource_ = handler.getDataSource();
        name_ = null;

        String dhName = handler.getName();

        if ((dhName != null) && (dhName.length() > 0)) {
            name_ = dhName;
        } else {
            MimeType srcMime = new MimeType(handler.getContentType());

            String mimeName = srcMime.getParameter("name");

            if ((mimeName != null) && (mimeName.length() > 0)) {
                name_ = mimeName;
            }
        }
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
        return mimeType_.getBaseType();
    }

    /**
     * Return the name of this object, i.e. file name of the part.
     *
     * @return name of the object
     */
    public String getName() {
        if (name_ != null) {
            return name_;
        } else {
            return dataSource_.getName();
        }
    }

    /**
     * This method returns an InputStream representing the data, i.e.
     * input stream of the data source.
     *
     * @return an InputStream representing the data
     */
    public InputStream getInputStream() throws IOException {
        return dataSource_.getInputStream();
    }

    /**
     * This method returns an OutputStream where data can be written, i.e.
     * output stream of the data source.
     *
     * @return an OutputStream where data can be written
     */
    public OutputStream getOutputStream() throws IOException {
        return dataSource_.getOutputStream();
    }
}
