package com.duroty.utils.misc;

import java.util.regex.*;


/*
 * JavaScriptFilter.java
 *
 * Created: Thu Oct 14 12:08:28 1999
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
 * Filter JavaScript content from HTML messages to avoid security problems.
 *
 * @author Sebastian Schaffert
 * @version
 */
public class JavaScriptFilter {
    /**
     * DOCUMENT ME!
     */
    private static Pattern[] filter = new Pattern[3];

    /**
     * DOCUMENT ME!
     */
    private static String[] substitution = new String[3];

    /**
     * DOCUMENT ME!
     */
    private static boolean initialized = false;

    /**
     * Creates a new JavaScriptFilter object.
     */
    public JavaScriptFilter() {
    }

    /**
     * DOCUMENT ME!
     */
    public static void init() {
        try {
            filter[0] = Pattern.compile("<\\s*SCRIPT[^>]*>",
                    Pattern.CASE_INSENSITIVE);
            filter[1] = Pattern.compile("<\\s*\\/SCRIPT[^>]*>",
                    Pattern.CASE_INSENSITIVE);
            filter[2] = Pattern.compile("<\\s*A +HREF *=.*\"(javascript:[^\"]*)\"[^>]*>([^<]+)</A>",
                    Pattern.CASE_INSENSITIVE);

            //filter[3] = Pattern.compile("\\s*<!\\[CDATA\\[\\s*", Pattern.CASE_INSENSITIVE);
            //filter[4] = Pattern.compile("\\s*\\]\\]>\\s*", Pattern.CASE_INSENSITIVE);
            //filter[5] = Pattern.compile("<\\s*STYLE[^>]*>", Pattern.CASE_INSENSITIVE);
            //filter[6] = Pattern.compile("<\\s*\\/STYLE[^>]*>", Pattern.CASE_INSENSITIVE);
            substitution[0] = "<P><FONT color=\"red\">WebMail security: JavaScript filtered</FONT>:<BR>\n<HR>\n<FONT COLOR=\"orange\"><PRE>";
            substitution[1] = "</PRE></FONT><HR><FONT color=\"red\">JavaScript end</FONT><P>";
            substitution[2] = "<FONT COLOR=\"red\">WebMail security: JavaScript link filtered:</FONT> <FONT COLOR=\"orange\">$1</FONT> $2 ";

            //substitution[3] = "";
            //substitution[4] = "";
            //substitution[5] = "";
            //substitution[6] = "";
            // Link highlighting
            //uri=new RE("http\\:\\/\\/(.+)(html|\\/)(\\S|\\-|\\+|\\.|\\\|\\:)");
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String apply(String s) {
        if ((s == null) || s.trim().equals("")) {
            return null;
        }

        if (!initialized) {
            init();
        }

        String retval = s;

        for (int i = 0; i < filter.length; i++) {
            Matcher m = filter[i].matcher(retval);
            retval = m.replaceAll(substitution[i]);
        }

        return retval;
    }
} // JavaScriptFilter
