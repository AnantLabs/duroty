/* CVS ID: $Id: JavaScriptCleaner.java,v 1.1 2006/03/08 09:07:01 durot Exp $ */
package com.duroty.utils.misc;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/*
 * JavaScriptCleaner.java
 *
 * Created: Mon Jan  1 15:20:54 2001
 *
 * Copyright (C) 1999-2001 Sebastian Schaffert
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
 * JavaScriptCleaner.java This class removes hopefully all of the possible
 * malicious code from HTML messages like SCRIPT tags, javascript: hrefs and
 * onMouseOver, ...; Furthermore, we should consider removing all IMG tags as
 * they might be used to call CGIs Created: Mon Jan  1 15:20:54 2001
 *
 * @author Sebastian Schaffert
 * @version
 */
public class JavaScriptCleaner {
    /**
     * DOCUMENT ME!
     */
    private Document d;

    /**
     * DOCUMENT ME!
     */
    private boolean displayImages = false;

    /**
     * Creates a new JavaScriptCleaner object.
     *
     * @param d DOCUMENT ME!
     */
    public JavaScriptCleaner(Document d, boolean displayImages) {
        this.d = d;
        this.displayImages = displayImages;

        walkTree(d.getDocumentElement());
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    protected void walkTree(Node node) {
        /* First we check for element types that shouldn't be sent to the user.
           For that, we add an attribute "malicious" that can be handled by the XSLT
           stylesheets that display the message.
         */
        if (node instanceof Element &&
                ((Element) node).getTagName().toUpperCase().equals("SCRIPT")) {
            Element n = d.createElement("img");
            n.setAttribute("src", "");
            n.setAttribute("width", "50");
            n.setAttribute("height", "50");
            n.setAttribute("border", "5");
            n.setAttribute("alt",
                "Marked malicious because of potential JavaScript abuse");

            node.getParentNode().replaceChild(n, node);
        }

        if (node instanceof Element &&
                ((Element) node).getTagName().toUpperCase().equals("IFRAME")) {
            Element n = d.createElement("img");
            n.setAttribute("src", "");
            n.setAttribute("width", "50");
            n.setAttribute("height", "50");
            n.setAttribute("border", "5");
            n.setAttribute("alt",
                "Marked malicious because of potential IFrame abuse");

            node.getParentNode().replaceChild(n, node);
        }

        if (node instanceof Element &&
                ((Element) node).getTagName().toUpperCase().equals("FRAME")) {
            Element n = d.createElement("img");
            n.setAttribute("src", "");
            n.setAttribute("width", "50");
            n.setAttribute("height", "50");
            n.setAttribute("border", "5");
            n.setAttribute("alt",
                "Marked malicious because of potential Frame abuse");

            node.getParentNode().replaceChild(n, node);
        }

        if (!displayImages) {
            if (node instanceof Element &&
                    ((Element) node).getTagName().toUpperCase().equals("IMG")) {
                Element n = d.createElement("img");
                n.setAttribute("src", "");
                n.setAttribute("width", "50");
                n.setAttribute("height", "50");
                n.setAttribute("border", "5");
                n.setAttribute("alt",
                    "Image marked malicious because of potential Image/CGI abuse");

                node.getParentNode().replaceChild(n, node);
            }
        }

        /* What we also really don't like in HTML messages are FORMs! */
        if (node instanceof Element &&
                ((Element) node).getTagName().toUpperCase().equals("FORM")) {
            Element n = d.createElement("img");
            n.setAttribute("src", "");
            n.setAttribute("width", "50");
            n.setAttribute("height", "50");
            n.setAttribute("border", "5");
            n.setAttribute("alt",
                "FORM marked malicious because of potential Javascript abuse");

            node.getParentNode().replaceChild(n, node);
        }

        /* Now we search the attribute list for attributes that may potentially be used maliciously.
           These will be:
           - href: check for a String containing "javascript"
           - onXXX events: if they exist, the link will be marked "malicious".
         */
        String javascript_href = "javascript";
        NamedNodeMap map = node.getAttributes();

        for (int i = 0; i < map.getLength(); i++) {
            Attr a = (Attr) map.item(i);

            /* First case: look for hrefs containing "javascript" */
            if (a.getName().toUpperCase().equals("HREF")) {
                for (int j = 0;
                        j < (a.getValue().length() - javascript_href.length());
                        j++) {
                    if (a.getValue().regionMatches(true, j, javascript_href, 0,
                                javascript_href.length())) {
                        Element n = d.createElement("img");
                        n.setAttribute("src", "");
                        n.setAttribute("width", "50");
                        n.setAttribute("height", "50");
                        n.setAttribute("border", "5");
                        n.setAttribute("alt",
                            "Marked malicious because of potential JavaScript abuse (HREF attribute contains javascript code)");

                        node.getParentNode().replaceChild(n, node);

                        break;
                    }
                }

                /* All elements containing "onXXX" tags get the malicious attribute immediately */
            } else if (a.getName().toUpperCase().startsWith("ON")) {
                Element n = d.createElement("img");
                n.setAttribute("src", "");
                n.setAttribute("width", "50");
                n.setAttribute("height", "50");
                n.setAttribute("border", "5");
                n.setAttribute("alt",
                    "Marked malicious because of potential JavaScript abuse (element contains script events)");

                node.getParentNode().replaceChild(n, node);
            }
        }

        /* Do that recursively */
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();

            for (int i = 0; i < nl.getLength(); i++) {
                walkTree(nl.item(i));
            }
        }
    }
} // JavaScriptCleaner
