/*
** $Id: MailUtilities.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;


/**
 * General mail store and folder utilities.
 */
public class MailUtilities {
    /**
     * Creates a new MailUtilities object.
     */
    private MailUtilities() {
    }

    // used by SendMessageThread.java
    // used by StoreTreeNode.java
    // used by MailEventThread.java
    // used by FolderTreePanel.java
    // used by FolderTreeNode.java
    static public void setStoreConnected(Store store) throws MessagingException {
        if (store == null) {
            return;
        }

        synchronized (store) {
            if (!store.isConnected()) {
                for (;;) {
                    try {
                        store.connect();

                        break;
                    } catch (AuthenticationFailedException ex) {
                        // UNDONE
                        // I think what I need to do here is to somehow remove and
                        // reinstall this node. This should cause the tree to decide
                        // to call loadChildren() again when we are opened again.
                        //
                        if (store.isConnected()) {
                            store.close();
                        }
                    }
                }
            }
        }
    }

    // used by FolderTableModel.java
    // used by FolderTreePanel.java
    // used by MailEventThread.java
    // MessagePanel.java
    // QuickViewer.java
    static public void setFolderOpenAndReady(Folder f, int perm)
        throws MessagingException {
        if (f == null) {
            return;
        }

        MailUtilities.setStoreConnected(f.getStore());

        synchronized (f) {
            if ((f != null) && !f.isOpen()) {
                //System.err.println("OPEN FOLDER '" + f.getFullName() + "'");
                f.open(perm);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param expunge DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    static public void setFolderClose(Folder f, boolean expunge)
        throws MessagingException {
        if (f == null) {
            return;
        }

        synchronized (f) {
            if ((f != null) && f.isOpen()) {
                f.close(expunge);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param expunge DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    static public void setStoreClose(Store s) throws MessagingException {
        if (s == null) {
            return;
        }

        synchronized (s) {
            if ((s != null) && s.isConnected()) {
                s.close();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param store DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param path DOCUMENT ME!
     *
     * @throws MessagingException DOCUMENT ME!
     */
    static public void saveMessage(Store store, Message message, String path)
        throws MessagingException {
        if (store == null) {
            throw new MessagingException("The store is null");
        }

        if (message == null) {
            throw new MessagingException("The message is null");
        }

        if (path == null) {
            throw new MessagingException("The path is null");
        }

        Store xstore = store;

        if (xstore != null) {
            MailUtilities.setStoreConnected(xstore);

            Folder xfolder = xstore.getFolder(path);

            if ((xfolder != null) && xfolder.exists()) {
                MailUtilities.setFolderOpenAndReady(xfolder, Folder.READ_WRITE);

                message.setFlag(Flags.Flag.SEEN, true);

                Message[] xmessages = { message };
                xfolder.appendMessages(xmessages);

                xfolder.close(false);
            } else {
                throw new MessagingException("Exception.missingFolder");
            }
        } else {
            throw new MessagingException("Exception.missingStore");
        }
    }
}
