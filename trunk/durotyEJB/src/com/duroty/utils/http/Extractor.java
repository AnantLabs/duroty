/**
 *
 */
package com.duroty.utils.http;

import com.duroty.utils.io.NullWriter;

import org.apache.commons.io.IOUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.w3c.tidy.Tidy;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.net.URL;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author durot TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class Extractor {
    /**
     * initializes HTML document without content
     *
     * @param url DOCUMENT ME!
     */
    private Extractor() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param contents DOCUMENT ME!
     * @param outputStream DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Vector getLinks(String contents, OutputStream outputStream)
        throws Exception {
        InputStream inputStream = null;

        try {
            inputStream = IOUtils.toInputStream(contents);

            return getLinks(inputStream, outputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inputStream DOCUMENT ME!
     * @param outputStream DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Vector getLinks(InputStream inputStream,
        OutputStream outputStream) throws Exception {
        try {
            Document doc = parseToDOM(inputStream, outputStream);

            Vector links = new Vector();
            extractLinks(doc.getDocumentElement(), links);

            return links;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * gets all Element nodes of a given type as a Vector
     *
     * @param type the type of elements to return. e.g. type="a" will return
     *        all tags. type must be lowercase
     *
     * @return a Vector containing all element nodes of the given type
     */
    public static Vector getElements(String contents,
        OutputStream outputStream, String type) {
        InputStream inputStream = null;

        try {
            inputStream = IOUtils.toInputStream(contents);

            return getElements(inputStream, outputStream, type);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * gets all Element nodes of a given type as a Vector
     *
     * @param type the type of elements to return. e.g. type="a" will return
     *        all tags. type must be lowercase
     *
     * @return a Vector containing all element nodes of the given type
     */
    public static Vector getElements(InputStream inputStream,
        OutputStream outputStream, String type) {
        Document doc = parseToDOM(inputStream, outputStream);

        Vector links = new Vector();
        extractElements(doc.getDocumentElement(), type, links);

        return links;
    }

    /**
     * Extract links from the given DOM subtree and put it into the given
     * vector.
     *
     * @param element the top level DOM element of the DOM tree to parse
     * @param links the vector that will store the links
     */
    protected static void extractLinks(Element element, Vector links) {
        // this should not happen !
        if (element == null) {
            return;
        }

        String name = element.getNodeName().toLowerCase();

        if (name.equals("a")) {
            // A HREF= 
            addLink(element.getAttribute("href"), links);
        } else if (name.equals("frame")) {
            // FRAME SRC=
            addLink(element.getAttribute("src"), links);
        } else if (name.equals("img")) {
            // IMG SRC=
            addLink(element.getAttribute("src"), links);
        } else if (name.equals("area")) {
            // AREA HREF=
            addLink(element.getAttribute("href"), links);
        } else if (name.equals("meta")) {
            // META HTTP-EQUIV=REFRESH
            String equiv = element.getAttribute("http-equiv");

            if ((equiv != null) && (equiv.equalsIgnoreCase("refresh"))) {
                String content = element.getAttribute("content");

                if (content == null) {
                    content = "";
                }

                StringTokenizer st = new StringTokenizer(content, ";");

                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    AttribValuePair av = new AttribValuePair(token);

                    if (av.getAttrib().equalsIgnoreCase("url")) {
                        addLink(av.getValue(), links);
                    }
                }
            }
        }

        // recursive travel through all childs
        NodeList childs = element.getChildNodes();

        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i) instanceof Element) {
                extractLinks((Element) childs.item(i), links);
            }
        }
    }

    /**
     * Extract links to includes images from the given DOM subtree and  put
     * them into the given vector.
     *
     * @param element the top level DOM element of the DOM tree to parse
     * @param links the vector that will store the links
     */
    protected static void extractImageLinks(Element element, Vector links) {
        // this should not happen !
        if (element == null) {
            return;
        }

        String name = element.getNodeName();

        if (name.equals("img")) {
            // IMG SRC=
            addLink(element.getAttribute("src"), links);
        }

        // recursive travel through all childs
        NodeList childs = element.getChildNodes();

        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i) instanceof Element) {
                extractImageLinks((Element) childs.item(i), links);
            }
        }
    }

    /**
     * Extract elements from the given DOM subtree and put it into the given
     * vector.
     *
     * @param element the top level DOM element of the DOM tree to parse
     * @param type HTML tag to extract (e.g. "a", "form", "head" ...)
     * @param elementList the vector that will store the elements
     */
    protected static void extractElements(Element element, String type,
        Vector elementList) {
        // this should not happen !
        if (element == null) {
            return;
        }

        String name = element.getNodeName();

        if (name.equals(type)) {
            elementList.add(element);
        }

        // recursive travel through all childs
        NodeList childs = element.getChildNodes();

        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i) instanceof Element) {
                extractElements((Element) childs.item(i), type, elementList);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private static Document parseToDOM(InputStream inputStream,
        OutputStream outputStream) {
        // set tidy parameters        
        Tidy tidy = new Tidy();
        tidy.setUpperCaseTags(true);
        tidy.setMakeClean(true);
        tidy.setShowWarnings(false);
        tidy.setErrout(new PrintWriter(new NullWriter()));
        tidy.setXmlOut(false);
        tidy.setWord2000(true);
        tidy.setDropProprietaryAttributes(true);
        tidy.setFixBackslash(true);
        tidy.setXHTML(true);
        tidy.setWrapSection(true);
        tidy.setWrapScriptlets(true);
        tidy.setWrapPhp(true);
        tidy.setQuiet(true);

        tidy.setErrout(new PrintWriter(new NullWriter()));

        Document domDoc = tidy.parseDOM(inputStream, outputStream);

        return domDoc;
    }

    /**
     * adds a links to the given vector. ignores (but logs) possible errors
     *
     * @param newURL DOCUMENT ME!
     * @param links DOCUMENT ME!
     */
    private static void addLink(String newURL, Vector links) {
        // remove part after # from the URL
        int pos = newURL.indexOf("#");

        if (pos >= 0) {
            newURL = newURL.substring(0, pos);
        }

        try {
            URL u = new URL(newURL);
            links.add(u);
        } catch (Exception e) {
        }
    }
}
