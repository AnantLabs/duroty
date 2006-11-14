/* CVS ID: $Id: ByteStore.java,v 1.1 2006/03/08 09:07:01 durot Exp $ */
package com.duroty.utils.misc;

import java.io.EOFException;
import java.io.InputStream;
import java.io.Serializable;


/*
 * ByteStore.java
 *
 *
 * Created: Sun Sep 19 17:22:13 1999
 *
 * Copyright (C) 1999-2000 Sebastian Schaffert
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/**
 *
 * @author Sebastian Schaffert
 * @version
 */
public class ByteStore implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -7791069239541050709L;

    /**
     * DOCUMENT ME!
     */
    byte[] bytes;

    /**
     * DOCUMENT ME!
     */
    String content_type = null;

    /**
     * DOCUMENT ME!
     */
    String content_encoding = null;

    /**
     * DOCUMENT ME!
     */
    String name;

    /**
     * DOCUMENT ME!
     */
    String description = "";

    /**
     * Creates a new ByteStore object.
     *
     * @param b DOCUMENT ME!
     */
    public ByteStore(byte[] b) {
        bytes = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setDescription(String s) {
        description = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setContentType(String s) {
        content_type = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        if (content_type != null) {
            return content_type;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setContentEncoding(String s) {
        content_encoding = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        return content_encoding;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setName(String s) {
        name = s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        return bytes.length;
    }

    /**
     * Create a ByteStore out of an InputStream
     */
    public static ByteStore getBinaryFromIS(InputStream in, int nr_bytes_to_read) {
        byte[] s = new byte[nr_bytes_to_read + 100];
        int count = 0;
        int lastread = 0;

        // System.err.print("Reading ... ");
        if (in != null) {
            synchronized (in) {
                while (count < s.length) {
                    try {
                        lastread = in.read(s, count, nr_bytes_to_read - count);
                    } catch (EOFException ex) {
                        System.err.println(ex.getMessage());
                        lastread = 0;
                    } catch (Exception z) {
                        System.err.println(z.getMessage());
                        lastread = 0;
                    }

                    count += lastread;

                    // System.err.print(lastread+" ");
                    if (lastread < 1) {
                        break;
                    }
                }
            }

            // System.err.println();
            byte[] s2 = new byte[count + 1];

            for (int i = 0; i < (count + 1); i++) {
                s2[i] = s[i];
            }

            //System.err.println("new byte-array, size "+s2.length);
            ByteStore d = new ByteStore(s2);

            return d;
        } else {
            return null;
        }
    }
} // ByteStore
