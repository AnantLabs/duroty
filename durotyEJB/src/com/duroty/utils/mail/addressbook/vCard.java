/*
** $Id: vCard.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import java.util.Vector;


/**
 * vCard defines the profile of the MIME Content-Type [MIME-DIR] for directory information for
 * a white-pages person object, based on an electronic business card.
 * The profile definition is independent of any particular directory service or protocol.
 * The profile is defined for representing and exchanging
 * a variety of information about an individual.
 * This format is described in the Internet draft - vCard MIME Directory Profile (RFC 2426).
 *
 * This class implements the vCard object which contains a list of properties.
 */
public class vCard {
    // the list of properties in the same order as when added

    /**
     * DOCUMENT ME!
     */
    protected Vector properties_ = new Vector();

    /**
     * Construct an empty vCard.
     */
    public vCard() {
    }

    /**
     * Add the given property to the vCard.
     *
     * @param property the given property to add
     * @return true if the collection changed as a result of the call
     */
    public boolean addProperty(vCardProperty property) {
        return properties_.add(property);
    }

    /**
     * Get a list of properties with the given name.
     *
     * @param name the name of the property to locate
     * @return array of properties located or null
     */
    public vCardProperty[] getProperty(String name) {
        Vector xproperties = new Vector(properties_.size());

        for (int xindex = 0; xindex < properties_.size(); xindex++) {
            vCardProperty xproperty = (vCardProperty) properties_.elementAt(xindex);

            if (xproperty.name_.equalsIgnoreCase(name)) {
                xproperties.add(xproperty);
            }
        }

        if (xproperties.size() == 0) {
            return null;
        }

        vCardProperty[] xreturn = new vCardProperty[xproperties.size()];
        xproperties.copyInto(xreturn);
        xproperties.clear();

        return xreturn;
    }

    /**
     * Return the vCard as a readable string.
     *
     * @see vCardWriter
     */
    public String toString() {
        StringBuffer xbuffer = new StringBuffer(128);

        for (int xindex = 0; xindex < properties_.size(); xindex++) {
            vCardProperty xcp = (vCardProperty) properties_.get(xindex);
            xbuffer.append(xcp.toItem());
        }

        return xbuffer.toString();
    }
}
