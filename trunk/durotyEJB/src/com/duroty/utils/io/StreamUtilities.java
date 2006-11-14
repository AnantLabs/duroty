/*
 ** $Id: StreamUtilities.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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
package com.duroty.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * DOCUMENT ME!
 *
 * @author Durot
 * @version 1.0
 */
public class StreamUtilities {
    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static public int copyStream(InputStream in, OutputStream out)
        throws IOException {
        int result = 0;
        byte[] buf = new byte[8 * 1024];

        for (;;) {
            int numRead = in.read(buf);

            if (numRead == -1) {
                break;
            }

            out.write(buf, 0, numRead);
            result += numRead;
        }

        out.flush();

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static public byte[] getStreamBytes(InputStream in)
        throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamUtilities.copyStream(in, out);

        return out.toByteArray();
    }
}
