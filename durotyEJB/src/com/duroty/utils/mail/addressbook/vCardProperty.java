/*
** $Id: vCardProperty.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import netscape.ldap.util.ByteBuf;
import netscape.ldap.util.MimeBase64Encoder;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class vCardProperty {
    /**
     * DOCUMENT ME!
     */
    protected String name_;

    /**
     * DOCUMENT ME!
     */
    protected String group_;

    /**
     * DOCUMENT ME!
     */
    protected Object value_;

    /**
     * DOCUMENT ME!
     */
    protected Vector params_ = new Vector();

    /**
     * Instantiate a property from the given parameters.
     *
     * @param group list of groups or null
     * @param name  name of property
     * @param value value of property, either String or ByteBuf
     * @param isRaw unused
     */
    public vCardProperty(String group, String name, Object value, boolean isRaw) {
        name_ = name;
        group_ = group;
        value_ = value;
    }

    /**
     * Add the give parameter to the property.
     *
     * @param param parameter name and value
     */
    public boolean addParam(vCardParam param) {
        return params_.add(param);
    }

    /**
     * Get the FIRST parameter that has the given name
     *
     * @param name name and of parameter to fetch
     * @return FIRST parameter found or null
     */
    public vCardParam getParam(String name) {
        for (int xindex = 0; xindex < params_.size(); xindex++) {
            vCardParam xparam = (vCardParam) params_.elementAt(xindex);

            if (xparam.name_.equalsIgnoreCase(name)) {
                return xparam;
            }
        }

        return null;
    }

    /**
     * Determine if the property has the given paramenter name and value.
     *
     * @param name  name and of parameter to compare
     * @param value value of parameter to compare
     * @return true if the given paramenter and value is found
     */
    public boolean hasParam(String name, String value) {
        for (int xindex = 0; xindex < params_.size(); xindex++) {
            vCardParam xparam = (vCardParam) params_.elementAt(xindex);

            if (xparam.name_.equalsIgnoreCase(name)) {
                if (xparam.value_.equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get a list of parameters with the given name.
     *
     * @param name the name of the parameter to locate
     * @return array of parameter located or null
     */
    public vCardParam[] getParams(String name) {
        Vector xparams = new Vector(params_.size());

        for (int xindex = 0; xindex < params_.size(); xindex++) {
            vCardParam xparam = (vCardParam) params_.elementAt(xindex);

            if (xparam.name_.equalsIgnoreCase(name)) {
                xparams.add(xparam);
            }
        }

        if (xparams.size() == 0) {
            return null;
        }

        vCardParam[] xreturn = new vCardParam[xparams.size()];
        xparams.copyInto(xreturn);
        xparams.clear();

        return xreturn;
    }

    /**
     * Return the group of the property.
     */
    public String getGroup() {
        return group_;
    }

    /**
     * Return the name of the property.
     */
    public String getName() {
        return name_;
    }

    /**
     * Return the value of the property.
     */
    public Object getValue() {
        return value_;
    }

    /**
     * Convert the property to a vCard standard item.
     * <p>
     * [group "."] name *(";" param ) ":" value CRLF
     */
    public StringBuffer toItem() {
        StringBuffer xbuf = new StringBuffer(128);

        if (group_ != null) {
            xbuf.append(group_).append('.');
        }

        xbuf.append(name_);

        for (int xindex = 0; xindex < params_.size(); xindex++) {
            vCardParam xparam = (vCardParam) params_.elementAt(xindex);
            xbuf.append(';').append(xparam.name_).append('=').append(xparam.value_);
        }

        xbuf.append(':');

        if (value_ instanceof String) {
            xbuf.append((String) value_);
        } else if (value_ instanceof ByteBuf) {
            ByteBuf xin = (ByteBuf) value_;
            ByteBuf xout = new ByteBuf();
            MimeBase64Encoder xencoder = new MimeBase64Encoder();

            xencoder.translate(xin, xout);
            xencoder.eof(xout);
            xbuf.append(xout.toString());
        }

        xbuf.append('\n');

        return xbuf;
    }

    /**
     * Convert the property to a readable string.
     */
    public String toString() {
        if (group_ == null) {
            return name_ + '[' + value_ + ']';
        } else {
            return group_ + '.' + name_ + '[' + value_ + ']';
        }
    }
}
