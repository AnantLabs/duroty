/*
** $Id: vCardUtilities.java,v 1.1 2006/03/08 09:07:01 durot Exp $
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

import com.duroty.utils.ldap.LDAPUtilities;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPEntry;

import java.util.Locale;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class vCardUtilities {
    /**
     * DOCUMENT ME!
     */
    private static final int Debug_ = 0;

    /**
     * DOCUMENT ME!
     *
     * @param card DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public String getFileName(vCard card) {
        // first try for the normal naming property
        vCardProperty xproperty1 = null;
        vCardProperty[] xproperties = card.getProperty("n");

        if (xproperties != null) {
            // parse the NAME value for "Family;Given;Middle;Prefix;Suffix"
            xproperty1 = selectLanguage(xproperties);

            StringTokenizer xtokenizer = new StringTokenizer((String) xproperty1.getValue(),
                    ";");

            // get surname if possible
            String xname = new String();

            if (xtokenizer.hasMoreTokens()) {
                xname = xtokenizer.nextToken().trim();
            }

            // get given name if possible
            if (xtokenizer.hasMoreTokens()) {
                if (xname.length() > 0) {
                    xname = xname + '_' + xtokenizer.nextToken().trim();
                } else {
                    xname = xtokenizer.nextToken().trim();
                }
            }

            // return a file name if possible
            if (xname.length() > 0) {
                return xname + ".vcf";
            }
        }

        // return something
        return "vcard.vcf";
    }

    /**
     * DOCUMENT ME!
     *
     * @param card DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public LDAPEntry convertCardToLDAP(vCard card) {
        // determine the version of the card
        float xversion = (float) 2.1; // default version
        vCardProperty xproperty0 = null;
        vCardProperty xproperty1 = null;
        vCardProperty xproperty2 = null;
        vCardProperty xproperty3 = null;
        vCardProperty xproperty4 = null;
        vCardProperty xproperty5 = null;
        vCardProperty xproperty6 = null;
        vCardProperty xproperty7 = null;
        vCardProperty xproperty8 = null;
        vCardProperty xproperty9 = null;
        vCardProperty[] xproperties = card.getProperty("version");

        if ((xproperties != null) && (xproperties.length == 1)) {
            xversion = new Float((String) xproperties[0].getValue()).floatValue();
        }

        if (Debug_ > 5) {
            System.out.println("version: " + xversion);
        }

        LDAPEntry xentry = new LDAPEntry();

        // determine the class of the object
        xproperties = card.getProperty("x-objectclass");

        if (xproperties == null) {
            // assume it's a person as per vCard standards
            xentry.getAttributeSet().add(new LDAPAttribute("objectclass",
                    "person"));
        } else {
            xentry.getAttributeSet().add(new LDAPAttribute("objectclass",
                    (String) xproperties[0].getValue()));
        }

        String xpart = null;

        // names
        xproperties = card.getProperty("n");

        if (Debug_ > 5) {
            System.out.println("n properties: " + xproperties.length);
        }

        if (xproperties == null) {
            // look for full name
            xproperties = card.getProperty("fn");

            if (Debug_ > 5) {
                System.out.println("fn properties: " + xproperties.length);
            }

            if (xproperties != null) {
                // parse the FULLNAME for "givenames... Family"
                xproperty1 = selectLanguage(xproperties);
                xpart = (String) xproperty1.getValue();

                int xindex = xpart.lastIndexOf(' ');

                if (xindex < 0) {
                    xentry.getAttributeSet().add(new LDAPAttribute(
                            "givenName", xpart));
                } else {
                    xentry.getAttributeSet().add(new LDAPAttribute(
                            "givenName", xpart.substring(0, xindex - 1)));
                    xentry.getAttributeSet().add(new LDAPAttribute("surname",
                            xpart.substring(xindex + 1)));
                }
            }
        } else {
            // parse the NAME value for "Surname;Given;Middle;Prefix;Suffix"
            xproperty1 = selectLanguage(xproperties);

            String[] xcomponents = decomposeComposite((String) xproperty1.getValue());

            if (xcomponents.length > 0) {
                xentry.getAttributeSet().add(new LDAPAttribute("surname",
                        xcomponents[0]));
            }

            xpart = null;

            if (xcomponents.length > 4) {
                xpart = xcomponents[3] + ' ' + xcomponents[1] + ' ' +
                    xcomponents[2] + ' ' + xcomponents[4];
            } else if (xcomponents.length > 3) {
                xpart = xcomponents[3] + ' ' + xcomponents[1] + ' ' +
                    xcomponents[2];
            } else if (xcomponents.length > 2) {
                xpart = xcomponents[1] + ' ' + xcomponents[2];
            } else if (xcomponents.length > 1) {
                xpart = xcomponents[1];
            }

            if (xpart != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("givenName",
                        xpart));
            }
        }

        // email addresses
        xproperties = card.getProperty("email");

        if (Debug_ > 5) {
            System.out.println("email properties: " + xproperties.length);
        }

        if (xproperties != null) {
            xproperty1 = null; // home email
            xproperty2 = null; // work email

            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                // priority given to property of TYPE=INTERNET and TYPE=PREF
                if (xproperties[xindex].hasParam("type", "internet")) {
                    if (xproperties[xindex].hasParam("type", "home")) {
                        xproperty1 = swap(xproperty1, xproperties[xindex]);
                    }

                    if (xproperties[xindex].hasParam("type", "work")) {
                        xproperty2 = swap(xproperty2, xproperties[xindex]);
                    }

                    if (xproperty2 == null) {
                        // special case, using default value for place; TYPE=WORK
                        if (!xproperties[xindex].hasParam("type", "home") &&
                                !xproperties[xindex].hasParam("type", "work")) {
                            xproperty2 = xproperties[xindex];
                        }
                    }
                }
            }

            // set home email if necessary
            if (xproperty1 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("emailAddress",
                        (String) xproperty1.getValue()));
            }

            // set work email if necessary
            if (xproperty2 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute(
                        "busEmailAddress", (String) xproperty2.getValue()));
            }
        }

        // phone numbers
        xproperties = card.getProperty("tel");

        if (Debug_ > 5) {
            System.out.println("tel properties: " + xproperties.length);
        }

        if (xproperties != null) {
            xproperty0 = null; // home phone
            xproperty1 = null; // home fax
            xproperty2 = null; // home mobile
            xproperty3 = null; // home car
            xproperty4 = null; // home pager
            xproperty5 = null; // work phone
            xproperty6 = null; // work fax
            xproperty7 = null; // work mobile
            xproperty8 = null; // work car
            xproperty9 = null; // work pager

            int xswaps = 0;

            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                if (Debug_ > 5) {
                    System.out.println("tel property " + xindex + ": " +
                        xproperties[xindex].toString());
                }

                if (xproperties[xindex].hasParam("type", "home")) {
                    xswaps = 0;

                    if (xproperties[xindex].hasParam("type", "voice")) {
                        xproperty0 = swap(xproperty0, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "fax")) {
                        xproperty1 = swap(xproperty1, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "cell")) {
                        xproperty2 = swap(xproperty2, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "car")) {
                        xproperty3 = swap(xproperty3, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "pager")) {
                        xproperty4 = swap(xproperty4, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xswaps == 0) {
                        // VOICE is default if no additional types are specified
                        xproperty0 = swap(xproperty0, xproperties[xindex]);
                    }
                }

                if (xproperties[xindex].hasParam("type", "work") ||
                        !xproperties[xindex].hasParam("type", "home")) {
                    xswaps = 0;

                    if (xproperties[xindex].hasParam("type", "voice")) {
                        xproperty5 = swap(xproperty5, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "fax")) {
                        xproperty6 = swap(xproperty6, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "cell")) {
                        xproperty7 = swap(xproperty7, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "car")) {
                        xproperty8 = swap(xproperty8, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xproperties[xindex].hasParam("type", "pager")) {
                        xproperty9 = swap(xproperty9, xproperties[xindex]);
                        xswaps++;
                    }

                    if (xswaps == 0) {
                        // VOICE is default if no additional types are specified
                        xproperty5 = swap(xproperty5, xproperties[xindex]);
                    }
                }
            }

            // set home phone if necessary
            if (xproperty0 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("phone",
                        (String) xproperty0.getValue()));
            }

            // set home fax if necessary
            if (xproperty1 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("fax",
                        (String) xproperty1.getValue()));
            }

            // set home mobile phone if necessary
            if (xproperty2 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("mobile",
                        (String) xproperty2.getValue()));
            }

            // set home car phone if necessary
            if (xproperty3 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("car",
                        (String) xproperty3.getValue()));
            }

            // set home pager if necessary
            if (xproperty4 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("pager",
                        (String) xproperty4.getValue()));
            }

            // set business phone if necessary
            if (xproperty5 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busPhone",
                        (String) xproperty5.getValue()));
            }

            // set business fax if necessary
            if (xproperty6 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busFax",
                        (String) xproperty6.getValue()));
            }

            // set business mobile phone if necessary
            if (xproperty7 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busMobile",
                        (String) xproperty7.getValue()));
            }

            // set business car phone if necessary
            if (xproperty8 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busCar",
                        (String) xproperty8.getValue()));
            }

            // set business pager if necessary
            if (xproperty9 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busPager",
                        (String) xproperty9.getValue()));
            }
        }

        // addresses; POBOX ; EXTENDED ADDRESS ; STREET ; LOCALITY ; REGION ; PO CODE ; COUNTRY
        // default is INTL, POSTAL, PARCEL, WORK
        xproperty1 = null; // selected HOME address
        xproperty2 = null; // selected WORK address
        xproperties = card.getProperty("adr");

        if (Debug_ > 5) {
            System.out.println("adr properties: " + xproperties.length);
        }

        if (xproperties != null) {
            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                if (xproperties[xindex].hasParam("type", "home")) {
                    if (xproperty1 == null) {
                        xproperty1 = xproperties[xindex];
                    } else {
                        // priority to preffered addresses
                        if (xproperties[xindex].hasParam("type", "pref")) {
                            xproperty1 = xproperties[xindex];
                        }
                    }
                }

                if (xproperties[xindex].hasParam("type", "work") ||
                        !xproperties[xindex].hasParam("type", "home")) {
                    if (xproperty2 == null) {
                        xproperty2 = xproperties[xindex];
                    } else {
                        // priority to preffered addresses
                        if (xproperties[xindex].hasParam("type", "pref")) {
                            xproperty2 = xproperties[xindex];
                        }
                    }
                }
            }
        }

        // home address; PO BOX ; EXTENDED ADDRESS ; STREET ; LOCALITY ; REGION ; PO CODE ; COUNTRY
        if (xproperty1 != null) {
            if (Debug_ > 5) {
                System.out.println("home adr value: " + xproperty1.getValue());
            }

            String[] xcomponents = decomposeComposite((String) xproperty1.getValue());

            if (xcomponents.length > 2) {
                xpart = xcomponents[0] + ',' + xcomponents[1] + ',' +
                    xcomponents[2];
            } else if (xcomponents.length > 1) {
                xpart = xcomponents[0] + ',' + xcomponents[1];
            } else {
                xpart = xcomponents[0];
            }

            xentry.getAttributeSet().add(new LDAPAttribute("street", xpart));

            if (xcomponents.length > 3) {
                xentry.getAttributeSet().add(new LDAPAttribute("city",
                        xcomponents[3]));
            }

            if (xcomponents.length > 4) {
                xentry.getAttributeSet().add(new LDAPAttribute("state",
                        xcomponents[4]));
            }

            if (xcomponents.length > 5) {
                xentry.getAttributeSet().add(new LDAPAttribute("postalCode",
                        xcomponents[5]));
            }

            if (xcomponents.length > 6) {
                xentry.getAttributeSet().add(new LDAPAttribute("country",
                        xcomponents[6]));
            }
        }

        // work address; POBOX ; EXTENDED ADDRESS ; STREET ; LOCALITY ; REGION ; PO CODE ; COUNTRY
        if (xproperty2 != null) {
            if (Debug_ > 5) {
                System.out.println("work adr value: " + xproperty2.getValue());
            }

            String[] xcomponents = decomposeComposite((String) xproperty2.getValue());

            if (xcomponents.length > 2) {
                xpart = xcomponents[0] + ',' + xcomponents[1] + ',' +
                    xcomponents[2];
            } else if (xcomponents.length > 1) {
                xpart = xcomponents[0] + ',' + xcomponents[1];
            } else {
                xpart = xcomponents[0];
            }

            xentry.getAttributeSet().add(new LDAPAttribute("busStreet", xpart));

            if (xcomponents.length > 3) {
                xentry.getAttributeSet().add(new LDAPAttribute("busCity",
                        xcomponents[3]));
            }

            if (xcomponents.length > 4) {
                xentry.getAttributeSet().add(new LDAPAttribute("busState",
                        xcomponents[4]));
            }

            if (xcomponents.length > 5) {
                xentry.getAttributeSet().add(new LDAPAttribute(
                        "busPostalCode", xcomponents[5]));
            }

            if (xcomponents.length > 6) {
                xentry.getAttributeSet().add(new LDAPAttribute("busCountry",
                        xcomponents[6]));
            }
        }

        // organization
        xproperties = card.getProperty("org");

        if (Debug_ > 5) {
            System.out.println("org properties: " + xproperties.length);
        }

        if (xproperties != null) {
            // parse the ORG value for "name;*"
            xproperty1 = selectLanguage(xproperties);

            String[] xcomponents = decomposeComposite((String) xproperty1.getValue());

            if (xcomponents.length > 0) {
                xentry.getAttributeSet().add(new LDAPAttribute("busName",
                        xcomponents[0]));
            }

            if (xcomponents.length > 1) {
                xentry.getAttributeSet().add(new LDAPAttribute("busSection",
                        xcomponents[1]));
            }
        }

        // nicknames
        xproperties = card.getProperty("nickname");

        if (Debug_ > 5) {
            System.out.println("nickname properties: " + xproperties.length);
        }

        if (xproperties != null) {
            xproperty1 = null; // home nickname
            xproperty2 = null; // work nickname

            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                // priority given to property of TYPE=WORK
                if (xproperties[xindex].hasParam("type", "home")) {
                    xproperty1 = swap(xproperty1, xproperties[xindex]);
                }

                if (xproperties[xindex].hasParam("type", "work")) {
                    xproperty2 = swap(xproperty2, xproperties[xindex]);
                }

                if (xproperty2 == null) {
                    // special case, using default value for place; TYPE=WORK
                    if (!xproperties[xindex].hasParam("type", "home") &&
                            !xproperties[xindex].hasParam("type", "work")) {
                        xproperty2 = xproperties[xindex];
                    }
                }
            }

            // set home alias if necessary
            if (xproperty1 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("alias",
                        (String) xproperty1.getValue()));
            }

            // set work alias if necessary
            if (xproperty2 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busAlias",
                        (String) xproperty2.getValue()));
            }
        }

        // urls
        xproperties = card.getProperty("url");

        if (Debug_ > 5) {
            System.out.println("url properties: " + xproperties.length);
        }

        if (xproperties != null) {
            xproperty1 = null; // home url
            xproperty2 = null; // work url

            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                // priority given to property of TYPE=WORK
                if (xproperties[xindex].hasParam("type", "home")) {
                    xproperty1 = swap(xproperty1, xproperties[xindex]);
                }

                if (xproperties[xindex].hasParam("type", "work")) {
                    xproperty2 = swap(xproperty2, xproperties[xindex]);
                }

                if (xproperty2 == null) {
                    // special case, using default value for place; TYPE=WORK
                    if (!xproperties[xindex].hasParam("type", "home") &&
                            !xproperties[xindex].hasParam("type", "work")) {
                        xproperty2 = xproperties[xindex];
                    }
                }
            }

            // set home url if necessary
            if (xproperty1 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("homeUrl",
                        (String) xproperty1.getValue()));
            }

            // set work url if necessary
            if (xproperty2 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busUrl",
                        (String) xproperty2.getValue()));
            }
        }

        // categories
        xproperties = card.getProperty("categories");

        if (Debug_ > 5) {
            System.out.println("categories properties: " + xproperties.length);
        }

        if (xproperties != null) {
            xproperty1 = null; // home category
            xproperty2 = null; // work category

            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                // priority given to property of TYPE=WORK
                if (xproperties[xindex].hasParam("type", "home")) {
                    xproperty1 = swap(xproperty1, xproperties[xindex]);
                }

                if (xproperties[xindex].hasParam("type", "work")) {
                    xproperty2 = swap(xproperty2, xproperties[xindex]);
                }

                if (xproperty2 == null) {
                    // special case, using default value for place; TYPE=WORK
                    if (!xproperties[xindex].hasParam("type", "home") &&
                            !xproperties[xindex].hasParam("type", "work")) {
                        xproperty2 = xproperties[xindex];
                    }
                }
            }

            // set home url if necessary
            if (xproperty1 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("category",
                        (String) xproperty1.getValue()));
            }

            // set work url if necessary
            if (xproperty2 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busCategory",
                        (String) xproperty2.getValue()));
            } else {
                // try translate role to business category
                translateText(card, "role", xentry, "busCategory");
            }
        }

        // notes
        xproperties = card.getProperty("note");

        if (Debug_ > 5) {
            System.out.println("note properties: " + xproperties.length);
        }

        if (xproperties != null) {
            xproperty1 = null; // home note
            xproperty2 = null; // work note

            for (int xindex = 0; xindex < xproperties.length; xindex++) {
                // priority given to property of TYPE=WORK
                if (xproperties[xindex].hasParam("type", "home")) {
                    xproperty1 = swap(xproperty1, xproperties[xindex]);
                }

                if (xproperties[xindex].hasParam("type", "work")) {
                    xproperty2 = swap(xproperty2, xproperties[xindex]);
                }

                if (xproperty2 == null) {
                    // special case, using default value for place; TYPE=WORK
                    if (!xproperties[xindex].hasParam("type", "home") &&
                            !xproperties[xindex].hasParam("type", "work")) {
                        xproperty2 = xproperties[xindex];
                    }
                }
            }

            // set home other if necessary
            if (xproperty1 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("other",
                        (String) xproperty1.getValue()));
            }

            // set work other if necessary
            if (xproperty2 != null) {
                xentry.getAttributeSet().add(new LDAPAttribute("busOther",
                        (String) xproperty2.getValue()));
            }
        }

        // TODO; decompose into busAssistant and busAssistantPhone
        translate(card, "agent", xentry, "busAssistant");

        translateText(card, "title", xentry, "busTitle");

        translate(card, "tz", xentry, "timezone");
        translate(card, "bday", xentry, "birthday");
        translate(card, "uid", xentry, "uid");
        translate(card, "rev", xentry, "lastUpdate");

        return xentry;
    }

    //............................................................

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public vCard convertLDAPToCard(LDAPEntry entry) {
        // convert based upon type of entry
        if (LDAPUtilities.getObjectClass(entry).equals("Person")) {
            return convertPersonToCard(entry);
        } else if (LDAPUtilities.getObjectClass(entry).equals("GroupOfNames")) {
            return convertGroupToCard(entry);
        }

        return new vCard();
    }

    //............................................................

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static private vCard convertPersonToCard(LDAPEntry entry) {
        vCard xcard = new vCard();
        vCardProperty xprop = null;
        String xvalue = null;

        // start the vCard
        xprop = new vCardProperty(null, "BEGIN", "VCARD", false);
        xcard.addProperty(xprop);
        xprop = new vCardProperty(null, "VERSION", "3.0", false);
        xcard.addProperty(xprop);
        xprop = new vCardProperty(null, "MAILER", "ICEMail", false);
        xcard.addProperty(xprop);

        // class attributes for interoperability
        translate(entry, "objectclass", xcard, "X-OBJECTCLASS", null, null);

        // personal attributes
        vCardParam xhome = new vCardParam("TYPE", "HOME");
        vCardParam xvoice = new vCardParam("TYPE", "VOICE");
        vCardParam xfax = new vCardParam("TYPE", "FAX");
        vCardParam xcell = new vCardParam("TYPE", "CELL");
        vCardParam xcar = new vCardParam("TYPE", "CAR");
        vCardParam xpager = new vCardParam("TYPE", "PAGER");
        vCardParam xinternet = new vCardParam("TYPE", "INTERNET");

        String xgivenname = LDAPUtilities.getAttributeValue(entry, "givenName");
        String xsurname = LDAPUtilities.getAttributeValue(entry, "surname");

        if ((xgivenname != null) || (xsurname != null)) {
            xvalue = new String();

            if (xgivenname != null) {
                xvalue = xgivenname;
            }

            if (xsurname != null) {
                xvalue = xvalue + " " + xsurname;
            }

            xprop = new vCardProperty(null, "FN", xvalue, false);
            xcard.addProperty(xprop);

            xvalue = new String();

            if (xsurname != null) {
                xvalue = xsurname;
            }

            if (xgivenname != null) {
                xvalue = xvalue + ";" + xgivenname;
            }

            xprop = new vCardProperty(null, "N", xvalue, false);
            xcard.addProperty(xprop);
        }

        String[] xhomeAddressProperties = {
                "postalBox", "extended", "street", "city", "state", "postalCode",
                "country"
            };
        xvalue = composeComposite(entry, xhomeAddressProperties);

        if (xvalue.length() >= xhomeAddressProperties.length) {
            xprop = new vCardProperty(null, "ADR", xvalue, false);
            xprop.addParam(xhome);
            xcard.addProperty(xprop);
        }

        translate(entry, "birthday", xcard, "BDAY", null, null);
        translate(entry, "phone", xcard, "TEL", xhome, xvoice);
        translate(entry, "fax", xcard, "TEL", xhome, xfax);
        translate(entry, "mobile", xcard, "TEL", xhome, xcell);
        translate(entry, "car", xcard, "TEL", xhome, xcar);
        translate(entry, "pager", xcard, "TEL", xhome, xpager);
        translate(entry, "emailAddress", xcard, "EMAIL", xhome, xinternet);
        translate(entry, "alias", xcard, "NICKNAME", xhome, null);
        translate(entry, "homeURL", xcard, "URL", xhome, null);
        translate(entry, "other", xcard, "NOTE", xhome, null);
        translate(entry, "category", xcard, "CATEGORIES", xhome, null);

        // business attributes
        vCardParam xwork = new vCardParam("TYPE", "WORK");

        String[] xorgProperties = { "busName", "busSection" };
        xvalue = composeComposite(entry, xorgProperties);

        if (xvalue.length() >= xorgProperties.length) {
            xprop = new vCardProperty(null, "ORG", xvalue, false);
            xcard.addProperty(xprop);
        }

        String[] xbusAddressProperties = {
                "busPostalBox", "busExtended", "busStreet", "busCity",
                "busState", "busPostalCode", "busCountry"
            };
        xvalue = composeComposite(entry, xbusAddressProperties);

        if (xvalue.length() >= xbusAddressProperties.length) {
            xprop = new vCardProperty(null, "ADR", xvalue, false);
            xprop.addParam(xwork);
            xcard.addProperty(xprop);
        }

        translate(entry, "busTitle", xcard, "TITLE", xwork, null);
        translate(entry, "busPhone", xcard, "TEL", xwork, xvoice);
        translate(entry, "busFax", xcard, "TEL", xwork, xfax);
        translate(entry, "busMobile", xcard, "TEL", xwork, xcell);
        translate(entry, "busCar", xcard, "TEL", xwork, xcar);
        translate(entry, "busPager", xcard, "TEL", xwork, xpager);
        translate(entry, "busEmailAddress", xcard, "EMAIL", xwork, xinternet);
        translate(entry, "busAlias", xcard, "NICKNAME", xwork, null);
        translate(entry, "busURL", xcard, "URL", xwork, null);
        translate(entry, "busOther", xcard, "NOTE", xwork, null);
        translate(entry, "busCategory", xcard, "CATEGORIES", xwork, null);

        String[] xagentProperties = { "busAssistant", "busAssistantPhone" };
        xvalue = composeComposite(entry, xagentProperties);

        if (xvalue.length() >= xagentProperties.length) {
            xprop = new vCardProperty(null, "AGENT", xvalue, false);
            xcard.addProperty(xprop);
        }

        //    translate( entry, "busManager", xcard, "??", null, null );
        // other attributes
        translate(entry, "uid", xcard, "UID", null, null);
        translate(entry, "timezone", xcard, "TZ", null, null);
        translate(entry, "lastUpdate", xcard, "REV", null, null);

        //    translate( entry, "preferredContact", xcard, "??", null, null );
        // end the card
        xprop = new vCardProperty(null, "END", "VCARD", false);
        xcard.addProperty(xprop);

        return xcard;
    }

    //............................................................

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static private vCard convertGroupToCard(LDAPEntry entry) {
        vCard xcard = new vCard();
        vCardProperty xprop = null;

        // start the vCard
        xprop = new vCardProperty(null, "BEGIN", "VCARD", false);
        xcard.addProperty(xprop);
        xprop = new vCardProperty(null, "VERSION", "3.0", false);
        xcard.addProperty(xprop);
        xprop = new vCardProperty(null, "MAILER", "ICEMail", false);
        xcard.addProperty(xprop);

        // class attributes for interoperability
        translate(entry, "objectclass", xcard, "X-OBJECTCLASS", null, null);

        // group entry conversion
        String xname = LDAPUtilities.getAttributeValue(entry, "busName");
        String xsection = LDAPUtilities.getAttributeValue(entry, "busSection");

        if ((xname != null) || (xsection != null)) {
            String xvalue = new String();

            if (xname != null) {
                xvalue = xname;
            }

            if (xsection != null) {
                xvalue = xvalue + ";" + xsection;
            }

            xprop = new vCardProperty(null, "ORG", xvalue, false);
            xcard.addProperty(xprop);
        }

        translate(entry, "name", xcard, "FN", null, null);
        translate(entry, "name", xcard, "N", null, null);
        translate(entry, "alias", xcard, "NICKNAME", null, null);
        translate(entry, "description", xcard, "NOTE", null, null);
        translate(entry, "owner", xcard, "AGENT", null, null);
        translate(entry, "category", xcard, "CATEGORIES", null, null);
        translate(entry, "url", xcard, "URL", null, null);
        translate(entry, "uid", xcard, "UID", null, null);
        translate(entry, "lastUpdate", xcard, "REV", null, null);

        // TODO how to convert the list of members?
        // end the card
        xprop = new vCardProperty(null, "END", "VCARD", false);
        xcard.addProperty(xprop);

        return xcard;
    }

    //............................................................

    /**
     * Translate the given LDAP attribute to vCard property if possible.
     * The given parameters are also added to the property if provided.
     */
    static private void translate(LDAPEntry entry, String attributeName,
        vCard card, String propertyName, vCardParam param1, vCardParam param2) {
        String xvalue = LDAPUtilities.getAttributeValue(entry, attributeName);

        if (xvalue != null) {
            vCardProperty xprop = new vCardProperty(null, propertyName, xvalue,
                    false);

            if (param1 != null) {
                xprop.addParam(param1);
            }

            if (param2 != null) {
                xprop.addParam(param2);
            }

            card.addProperty(xprop);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param card DOCUMENT ME!
     * @param vname DOCUMENT ME!
     * @param entry DOCUMENT ME!
     * @param lname DOCUMENT ME!
     */
    static private void translateText(vCard card, String vname,
        LDAPEntry entry, String lname) {
        vCardProperty[] xproperties = card.getProperty(vname);

        if (xproperties != null) {
            vCardProperty xproperty = selectLanguage(xproperties);
            entry.getAttributeSet().add(new LDAPAttribute(lname,
                    (String) xproperty.getValue()));
        }
    }

    /**
     * Translate the given vCard property to LDAP attribute if possible.
     */
    static private void translate(vCard card, String vname, LDAPEntry entry,
        String lname) {
        vCardProperty[] xproperties = card.getProperty(vname);

        if (xproperties != null) {
            entry.getAttributeSet().add(new LDAPAttribute(lname,
                    (String) xproperties[0].getValue()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param composite DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static private String[] decomposeComposite(String composite) {
        // count the components
        int xcount = 1;
        int xpos = composite.indexOf(';');

        while (xpos >= 0) {
            xcount++;

            // bump past last position and search again
            xpos = composite.indexOf(';', xpos + 1);
        }

        if (Debug_ > 5) {
            System.out.println("decomposeComposite found " + xcount +
                " components");
        }

        // parse out the components
        String[] xcomponents = new String[xcount];
        xpos = 0;

        int xlast = 0;

        for (xcount = 1; xcount < xcomponents.length; xcount++) {
            xpos = composite.indexOf(';', xlast);

            if (xpos > xlast) {
                // found a component
                xcomponents[xcount - 1] = composite.substring(xlast, xpos).trim();
                xlast = xpos + 1;
            } else {
                // nothing found
                xcomponents[xcount - 1] = new String();
                xlast++;
            }
        }

        xcomponents[xcount - 1] = composite.substring(xlast);

        return xcomponents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     * @param properties DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static private String composeComposite(LDAPEntry entry, String[] properties) {
        StringBuffer xbuf = new StringBuffer(128);
        String xvalue = null;

        for (int xindex = 0; xindex < properties.length; xindex++) {
            xvalue = LDAPUtilities.getAttributeValue(entry, properties[xindex]);

            if (xindex > 0) {
                xbuf.append(';');
            }

            if (xvalue != null) {
                xbuf.append(xvalue.trim());
            }
        }

        return xbuf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static private vCardProperty swap(vCardProperty o, vCardProperty n) {
        if (o == null) {
            return n;
        } else {
            if (n.hasParam("type", "pref")) {
                return n;
            }
        }

        return o;
    }

    /**
     * select the best suited property based on language; RFC 1766
     * primary tag of vCard LANGUAGE is ISO 639
     */
    static private vCardProperty selectLanguage(vCardProperty[] properties) {
        if (properties.length == 1) {
            return properties[0];
        }

        // get default locale language; ISO 639
        String xlanguage = Locale.getDefault().getLanguage();

        if (xlanguage == null) {
            return properties[0]; // impossible to select
        }

        // select the first occurance of the language
        String xplanguage = null;

        for (int xindex = 0; xindex < properties.length; xindex++) {
            // determine language of the property
            vCardParam xparam = properties[xindex].getParam("language");

            if (xparam == null) {
                xplanguage = null;
            } else {
                xplanguage = xparam.value_;
            }

            if (xplanguage == null) {
                xplanguage = "en-US"; // default language
            }

            if (xplanguage.indexOf('-') == 2) {
                xplanguage = xplanguage.substring(0, 1);

                if (xlanguage.equalsIgnoreCase(
                            new Locale(xplanguage, "", "").getLanguage())) {
                    return properties[xindex];
                }
            }
        }

        return properties[0]; // couldn't select a property
    }
}
