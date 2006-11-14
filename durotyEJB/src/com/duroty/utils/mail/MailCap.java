/*
** $Id: MailCap.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Vector;

import javax.activation.CommandInfo;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;


/**
 * This class encapsulates the mailcap file and access to associated data.
 */
public class MailCap {
    /**
     * DOCUMENT ME!
     */

    //private String fileName_;

    /**
     * DOCUMENT ME!
     */
    private Vector types_ = new Vector();

    /**
     * Create a default MailCap, setting the default filename.
     */
    public MailCap() {
    }

    //............................................................
    // Accessors
    public Enumeration getTypes() {
        return types_.elements();
    }

    //............................................................
    // Mutators

    /**
     * Directly set the name of the MailCap file.
     * Need to invoke this before calling load()
     */
    public void setFileName(String fileName) {
        //fileName_ = fileName;
    }

    /**
     * Load a new set of command maps from the mailcap file.
     */
    public void load() {
        MailcapCommandMap map = new MailcapCommandMap();

        map.addMailcap(
            "message/*;;x-java-view=com.duroty.utils.mail.QuickViewer");
        map.addMailcap(
            "multipart/*;;x-java-view=com.duroty.utils.mail.MultipartViewer");
        map.addMailcap("image/*;;x-java-view=com.duroty.utils.mail.ImageViewer");
        map.addMailcap("text/rtf;;x-java-view=com.duroty.utils.mail.TextViewer");
        map.addMailcap(
            "text/html;;x-java-view=com.duroty.utils.mail.HtmlViewer");
        map.addMailcap(
            "text/plain;;x-java-view=com.duroty.utils.mail.TextViewer");
        map.addMailcap(
            "text/x-vcard;;x-java-view=com.duroty.utils.mail.vCardViewer");

        CommandMap.setDefaultCommandMap(map);

        //parseMailcap(capfile);
    }

    /**
     * Create a viewer for the given data handler, i.e. part.
     */
    static public Object getViewer(DataHandler dh) {
        Object xobject = null;
        String xtext = null;

        CommandInfo xcinfo = dh.getCommand("view");

        if (xcinfo == null) {
            String contentType = dh.getContentType();

            try {
                ContentType mime = new ContentType(contentType);
                contentType = mime.getBaseType();
            } catch (ParseException ex) {
            }

            Object[] xargs = new Object[1];
            xargs[0] = contentType;
            xtext = "MailCap.noviewcommand";
        } else {
            try {
                xobject = dh.getBean(xcinfo);
            } catch (Throwable ex) {
                xobject = null;
            }

            if (xobject == null) {
                Object[] xargs = new Object[2];
                xargs[0] = xcinfo.getCommandName();
                xargs[1] = xcinfo.getCommandClass();
                xtext = "MailCap.noviewer";
            }
        }

        if (xobject == null) {
            xobject = xtext;
        }

        return xobject;
    }

    //............................................................
    // HACK
    // This is a horrible hack that is needed only because
    // CommandMap does not allow us to ask for the list of
    // known content types.
    //
    private void parseMailcap(File capfile)
        throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(capfile));

        for (;;) {
            String line = reader.readLine();

            if (line == null) {
                break;
            }

            if (line.startsWith("#")) {
                continue;
            }

            if (line.startsWith("!")) {
                continue;
            }

            int index = line.indexOf(";");

            if (index < 0) {
                continue;
            }

            String baseType = line.substring(0, index);

            index = baseType.indexOf("/");

            if (index < 0) {
                continue;
            }

            String subType = baseType.substring(index + 1);

            if (subType.equals("*")) {
                continue;
            }

            types_.addElement(baseType);
        }
    }
}
