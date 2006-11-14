/*
** $Id: LDAPUtilities.java,v 1.1 2006/03/08 09:07:02 durot Exp $
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
package com.duroty.utils.ldap;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPEntry;

import netscape.ldap.util.LDIFAttributeContent;
import netscape.ldap.util.LDIFContent;
import netscape.ldap.util.LDIFRecord;

import java.util.Enumeration;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class LDAPUtilities {
    /**
     * Creates a new LDAPUtilities object.
     */
    private LDAPUtilities() {
    }

    //............................................................
    // entry utilities
    public static String getObjectClass(LDAPEntry entry) {
        return getObjectClass(entry.getAttributeSet());
    }

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getAttributeValue(LDAPEntry entry, String name) {
        return getAttributeValue(entry.getAttributeSet(), name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param record DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static LDAPEntry convert(LDIFRecord record) {
        //System.out.println( "LDAPUtilities.convert(): r-" + record );
        String xdn = record.getDN();
        LDIFContent xcontent = record.getContent();

        switch (xcontent.getType()) {
        case LDIFContent.ATTRIBUTE_CONTENT:

            LDIFAttributeContent xattrContent = (LDIFAttributeContent) xcontent;
            LDAPAttribute[] xattributes = xattrContent.getAttributes();

            return new LDAPEntry(xdn, new LDAPAttributeSet(xattributes));

        default:
            break;
        }

        return new LDAPEntry(xdn);
    }

    //............................................................
    // attribute set utilities
    public static LDAPAttributeSet cloneWithoutNulls(LDAPAttributeSet set) {
        LDAPAttributeSet xclone = new LDAPAttributeSet();
        Enumeration xenum = set.getAttributes();

        while (xenum.hasMoreElements()) {
            LDAPAttribute xattribute = (LDAPAttribute) xenum.nextElement();
            String[] xvalues = xattribute.getStringValueArray();

            if ((xvalues != null) && (xvalues.length > 0)) {
                xclone.add(new LDAPAttribute(xattribute.getName(), xvalues));
            }
        }

        return xclone;
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public static void removeAttribute(LDAPAttributeSet set, String name) {
        for (int xindex = 0; xindex < set.size(); xindex++) {
            if (set.elementAt(xindex).getName().equals(name)) {
                set.removeElementAt(xindex);

                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void replaceAttribute(LDAPAttributeSet set, String name,
        String value) {
        removeAttribute(set, name);
        set.add(new LDAPAttribute(name, value));
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getAttributeValue(LDAPAttributeSet set, String name) {
        LDAPAttribute xattribute = set.getAttribute(name);

        if (xattribute != null) {
            Enumeration xenum = xattribute.getStringValues();

            if (xenum.hasMoreElements()) {
                return (String) xenum.nextElement();
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getObjectClass(LDAPAttributeSet set) {
        for (int xindex = 0; xindex < set.size(); xindex++) {
            LDAPAttribute xattribute = set.elementAt(xindex);

            if (xattribute.getName().equalsIgnoreCase("objectclass")) {
                Enumeration xenum = xattribute.getStringValues();

                while (xenum.hasMoreElements()) {
                    String xvalue = (String) xenum.nextElement();

                    if (xvalue.equalsIgnoreCase("person")) {
                        return "Person";
                    }

                    if (xvalue.equalsIgnoreCase("groupofnames")) {
                        return "GroupOfNames";
                    }
                }
            }
        }

        /** @todo what to do with bad entries? */
        return "Unknown";
    }

    /**
     * Create a distinguished name based on the contents of the attribute set.
     * @return distinguished name or null if not possible
     */
    public static String createDN(LDAPAttributeSet set) {
        String xobjectclass = getObjectClass(set);

        if (xobjectclass.equals("Person")) {
            String xcn = getAttributeValue(set, "cn");
            String xea = getAttributeValue(set, "emailAddress");

            if (xcn == null) {
                if (xea == null) {
                    return null;
                } else {
                    return "emailAddress=" + xea;
                }
            } else {
                if (xea == null) {
                    return "cn=" + xcn;
                } else {
                    return "cn=" + xcn + ",emailAddress=" + xea;
                }
            }
        } else if (xobjectclass.equals("GroupOfNames")) {
            String xcn = getAttributeValue(set, "cn");
            String xnm = getAttributeValue(set, "name");

            if (xcn == null) {
                if (xnm == null) {
                    /** @todo what to do with incomplete groupofnames? */
                } else {
                    return "name=" + xnm;
                }
            } else {
                return "cn=" + xcn;
            }
        } else {
            /** @todo what to do with unknow object classes? */
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     * @param member DOCUMENT ME!
     */
    static public void removeMember(LDAPAttributeSet set, String member) {
        for (int xindex = 0; xindex < set.size(); xindex++) {
            LDAPAttribute xattribute = set.elementAt(xindex);

            if (xattribute.getName().equals("member")) {
                Enumeration xenum = xattribute.getStringValues();

                if (xenum.hasMoreElements()) {
                    String xvalue = (String) xenum.nextElement();

                    if (xvalue.equals(member)) {
                        set.removeElementAt(xindex);

                        return;
                    }
                }
            }
        }
    }
}
