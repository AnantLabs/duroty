/*
** $Id: vCardParam.java,v 1.1 2006/03/08 09:07:01 durot Exp $
**
** Copyright (c) 2001-2003 Jeff Gay
** on behalf of ICEMail.org <http://www.icemail.org>
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
package com.duroty.utils.mail.addressbook;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class vCardParam {
    /**
     * DOCUMENT ME!
     */
    protected String name_;

    /**
     * DOCUMENT ME!
     */
    protected String value_;

    /**
     * Creates a new vCardParam object.
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public vCardParam(String name, String value) {
        name_ = name;
        value_ = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name_ + ':' + value_;
    }
}
