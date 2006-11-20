/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/


package com.duroty.lucene.utils;

import java.io.File;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class FileUtilities {
    /**
     * DOCUMENT ME!
     */
    public static final String FILE_IS_UNLOCK = "is.unlock";

    /**
     * Creates a new FileUtilities object.
     */
    private FileUtilities() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLockDir(File dir) throws IOException {
        boolean lock = true;

        if (dir == null) {
            throw new IOException("The dir is null");
        }

        if (!dir.exists()) {
            throw new IOException("The dir don't exist");
        }

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();

            if (children == null) {
                throw new IOException("The dir is empty");
            }

            for (int i = 0; i < children.length; i++) {
                if (children[i].getName().equals("is.unlock")) {
                    lock = false;

                    break;
                }
            }

            return lock;
        } else {
            if (dir.getName().equals(FILE_IS_UNLOCK)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void deleteMotLocks(File dir) throws IOException {
        if (dir == null) {
            throw new IOException("The dir is null");
        }

        if (!dir.exists()) {
            throw new IOException("The dir don't exist");
        }

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();

            if (children == null) {
                throw new IOException("The dir is empty");
            }

            for (int i = 0; i < children.length; i++) {
                if (children[i].getName().endsWith("mot.lock")) {
                    children[i].delete();
                } else if (children[i].getName().endsWith("bot.lock")) {
                    children[i].delete();
                }
            }
        } else {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void deleteLuceneLocks(File dir) throws IOException {
        if (dir == null) {
            throw new IOException("The dir is null");
        }

        if (!dir.exists()) {
            throw new IOException("The dir don't exist");
        }

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();

            if (children == null) {
                throw new IOException("The dir is empty");
            }

            for (int i = 0; i < children.length; i++) {
                if (children[i].getName().startsWith("lucene-")) {
                    children[i].delete();
                }
            }
        } else {
        }
    }
}
