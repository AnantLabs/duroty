/*
 ** $Id: DebugUtilities.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import java.io.PrintWriter;


/**
 * DOCUMENT ME!
 *
 * @author Durot
 * @version 1.0
 */
public class DebugUtilities {
    /**
     * DOCUMENT ME!
     *
     * @param aClass DOCUMENT ME!
     * @param writer DOCUMENT ME!
     * @param prefix DOCUMENT ME!
     */
    public static void printClassHierarchy(Class aClass, PrintWriter writer,
        String prefix) {
        String subPrefix = "-->";

        for (int i = 0;; ++i) {
            writer.println(prefix + " " + subPrefix + " " + aClass.getName());

            aClass = aClass.getSuperclass();

            if (aClass == Object.class) {
                break;
            }

            subPrefix = "--" + subPrefix;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cont DOCUMENT ME!
     * @param writer DOCUMENT ME!
     * @param prefix DOCUMENT ME!
     * @param recurse DOCUMENT ME!
     */
    public static void printContainerComponents(java.awt.Container cont,
        PrintWriter writer, String prefix, boolean recurse) {
        java.awt.Component[] comps = cont.getComponents();

        if (comps.length < 1) {
            writer.println(prefix + "Contains no components.");
        }

        for (int i = 0; i < comps.length; ++i) {
            DebugUtilities.printClassHierarchy(comps[i].getClass(), writer,
                prefix + "[" + i + "]");

            if (recurse) {
                Class c = java.awt.Container.class;

                if (c.isAssignableFrom(comps[i].getClass())) {
                    DebugUtilities.printContainerComponents((java.awt.Container) comps[i],
                        writer, (prefix + "[" + i + "] "), recurse);
                }
            }
        }
    }
}
