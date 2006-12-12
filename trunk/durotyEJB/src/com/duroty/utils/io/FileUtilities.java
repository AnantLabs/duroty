/*
 ** $Id: FileUtilities.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author Durot
 * @version 1.0
 */
public class FileUtilities {
    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void copyFile(File from, File to) throws IOException {
        int bytes;
        long length;
        long fileSize;

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(from));
        } catch (IOException ex) {
            throw new IOException(
                "FileUtilities.copyFile: opening input stream '" +
                from.getPath() + "', " + ex.getMessage());
        }

        try {
            out = new BufferedOutputStream(new FileOutputStream(to));
        } catch (Exception ex) {
            try {
                in.close();
            } catch (IOException ex1) {
            }

            throw new IOException(
                "FileUtilities.copyFile: opening output stream '" +
                to.getPath() + "', " + ex.getMessage());
        }

        byte[] buffer;
        buffer = new byte[8192];
        fileSize = from.length();

        for (length = fileSize; length > 0;) {
            bytes = (int) ((length > 8192) ? 8192 : length);

            try {
                bytes = in.read(buffer, 0, bytes);
            } catch (IOException ex) {
                try {
                    in.close();
                    out.close();
                } catch (IOException ex1) {
                }

                throw new IOException(
                    "FileUtilities.copyFile: reading input stream, " +
                    ex.getMessage());
            }

            if (bytes < 0) {
                break;
            }

            length -= bytes;

            try {
                out.write(buffer, 0, bytes);
            } catch (IOException ex) {
                try {
                    in.close();
                    out.close();
                } catch (IOException ex1) {
                }

                throw new IOException(
                    "FileUtilities.copyFile: writing output stream, " +
                    ex.getMessage());
            }
        }

        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            throw new IOException(
                "FileUtilities.copyFile: closing file streams, " +
                ex.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     * @param extension DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean fileEqualsExtension(String fileName, String extension) {
        boolean result = false;

        int fnLen = fileName.length();
        int exLen = extension.length();

        if (fnLen > exLen) {
            String fileSuffix = fileName.substring(fnLen - exLen);

            if (FileUtilities.supportsCaseSensitivePathNames()) {
                result = fileSuffix.equals(extension);
            } else {
                result = fileSuffix.equalsIgnoreCase(extension);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     * @param lenght DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getExtension(String filename, int lenght) {
        if (filename == null) {
            return null;
        }

        String extension = null;
        int idx = filename.lastIndexOf(".");
        if (idx > -1) {
        	extension = filename.substring(idx + 1, filename.length());
        } else {
        	return null;
        }

        if (extension.length() > lenght) {
            return null;
        }

        return extension;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public boolean supportsCaseSensitivePathNames() {
        boolean result = true;

        String osname = System.getProperty("os.name");

        if (osname != null) {
            if (osname.startsWith("macos")) {
                result = false;
            } else if (osname.startsWith("Windows")) {
                result = false;
            }
        }

        return result;
    }

    /**
     * Determines if a filename matches a 'globbing' pattern. The pattern can
     * contain the following special symbols:
     *
     * <ul>
     * <li>
     * - Matches zero or more of any character
     * </li>
     * <li>
     * ? - Matches exactly one of any character
     * </li>
     * <li>
     * [...] - Matches one of any character in the list or range
     * </li>
     * </ul>
     *
     *
     * @param pattern The name of the file to check.
     *
     * @return If the file name matches the expression, true, else false.
     */
    public static boolean isPatternString(String pattern) {
        if (pattern.indexOf("*") >= 0) {
            return true;
        }

        if (pattern.indexOf("?") >= 0) {
            return true;
        }

        int index = pattern.indexOf("[");

        if ((index >= 0) && (pattern.indexOf("]") > (index + 1))) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     * @param pattern DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean matchPattern(String fileName, String pattern) {
        return FileUtilities.recurseMatchPattern(fileName, pattern, 0, 0);
    }

    /**
     * An internal routine to implement expression matching. This routine is
     * based on a self-recursive algorithm.
     *
     * @param string The string to be compared.
     * @param pattern The expression to compare <em>string</em> to.
     * @param sIdx The index of where we are in <em>string</em>.
     * @param pIdx The index of where we are in <em>pattern</em>.
     *
     * @return True if <em>string</em> matched pattern, else false.
     */
    private static boolean recurseMatchPattern(String string, String pattern,
        int sIdx, int pIdx) {
        int pLen = pattern.length();
        int sLen = string.length();

        for (;;) {
            if (pIdx >= pLen) {
                if (sIdx >= sLen) {
                    return true;
                } else {
                    return false;
                }
            }

            if ((sIdx >= sLen) && (pattern.charAt(pIdx) != '*')) {
                return false;
            }

            // Check for a '*' as the next pattern char.
            // This is handled by a recursive call for
            // each postfix of the name.
            if (pattern.charAt(pIdx) == '*') {
                if (++pIdx >= pLen) {
                    return true;
                }

                for (;;) {
                    if (FileUtilities.recurseMatchPattern(string, pattern,
                                sIdx, pIdx)) {
                        return true;
                    }

                    if (sIdx >= sLen) {
                        return false;
                    }

                    ++sIdx;
                }
            }

            // Check for '?' as the next pattern char.
            // This matches the current character.
            if (pattern.charAt(pIdx) == '?') {
                ++pIdx;
                ++sIdx;

                continue;
            }

            // Check for '[' as the next pattern char.
            // This is a list of acceptable characters,
            // which can include character ranges.
            if (pattern.charAt(pIdx) == '[') {
                for (++pIdx;; ++pIdx) {
                    if ((pIdx >= pLen) || (pattern.charAt(pIdx) == ']')) {
                        return false;
                    }

                    if (pattern.charAt(pIdx) == string.charAt(sIdx)) {
                        break;
                    }

                    if ((pIdx < (pLen - 1)) &&
                            (pattern.charAt(pIdx + 1) == '-')) {
                        if (pIdx >= (pLen - 2)) {
                            return false;
                        }

                        char chStr = string.charAt(sIdx);
                        char chPtn = pattern.charAt(pIdx);
                        char chPtn2 = pattern.charAt(pIdx + 2);

                        if ((chPtn <= chStr) && (chPtn2 >= chStr)) {
                            break;
                        }

                        if ((chPtn >= chStr) && (chPtn2 <= chStr)) {
                            break;
                        }

                        pIdx += 2;
                    }
                }

                for (; pattern.charAt(pIdx) != ']'; ++pIdx) {
                    if (pIdx >= pLen) {
                        --pIdx;

                        break;
                    }
                }

                ++pIdx;
                ++sIdx;

                continue;
            }

            // Check for backslash escapes
            // We just skip over them to match the next char.
            if (pattern.charAt(pIdx) == '\\') {
                if (++pIdx >= pLen) {
                    return false;
                }
            }

            if ((pIdx < pLen) && (sIdx < sLen)) {
                if (pattern.charAt(pIdx) != string.charAt(sIdx)) {
                    return false;
                }
            }

            ++pIdx;
            ++sIdx;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getUserHomeDirectory() {
        String userDirName = System.getProperty("user.home", null);

        if (userDirName == null) {
            userDirName = System.getProperty("user.dir", null);
        }

        return userDirName;
    }
}
