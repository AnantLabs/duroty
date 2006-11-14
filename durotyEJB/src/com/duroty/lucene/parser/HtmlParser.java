/*
 * HtmlParser.java
 *
 * Created on 15 de noviembre de 2004, 11:35
 */
package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import com.duroty.utils.io.NullWriter;

import org.apache.commons.io.IOUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.w3c.tidy.Tidy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.nio.charset.Charset;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class HtmlParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /** DOCUMENT ME! */
    private Element element;

    /**
     * DOCUMENT ME!
     */
    private String title = null;

    /**
     * DOCUMENT ME!
     */
    private String body = null;

    /**
     * DOCUMENT ME!
     */
    private String charset = Charset.defaultCharset().displayName();

    /**
     * Creates a new instance of HtmlParser
     */
    public HtmlParser() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    public String parse(File file) throws ParserException {
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ParserException(e);
        }

        return parse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    public String parse(InputStream in) throws ParserException {
        this.input = in;

        return parse();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    private String parse() throws ParserException {
        try {
            Tidy tidy = new Tidy();
            tidy.setUpperCaseTags(true);
            tidy.setInputEncoding(charset);
            tidy.setOutputEncoding(Charset.defaultCharset().displayName());
            tidy.setMakeBare(true);
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

            org.w3c.dom.Document root = tidy.parseDOM(input, null);
            element = root.getDocumentElement();

            StringBuffer buffer = new StringBuffer();

            this.title = this.getTitle();

            if ((this.title != null) && !this.title.trim().equals("")) {
                buffer.append(this.title);
                buffer.append('\n');
            }

            this.body = this.getBody();

            if ((this.body != null) && !this.body.trim().equals("")) {
                buffer.append(this.body);
            }

            return buffer.toString();
        } catch (Exception ex) {
            throw new ParserException(ex);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        if (this.title != null) {
            return this.title;
        }

        if (element == null) {
            return null;
        }

        String title = null;

        NodeList nl = element.getElementsByTagName("title");

        if (nl.getLength() > 0) {
            Element titleElement = ((Element) nl.item(0));
            Text text = (Text) titleElement.getFirstChild();

            if (text != null) {
                title = text.getData();
            }
        }

        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBody() {
        if (this.body != null) {
            return this.body;
        }

        if (element == null) {
            return null;
        }

        String body = "";
        NodeList nl = element.getElementsByTagName("body");

        if (nl.getLength() > 0) {
            body = getBodyText(nl.item(0));
        }

        return body;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getBodyText(Node node) {
        NodeList nl = node.getChildNodes();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < nl.getLength(); i++) {
            Node child = nl.item(i);

            switch (child.getNodeType()) {
            case Node.ELEMENT_NODE:

                if (!child.getNodeName().toLowerCase().equals("script")) {
                    buffer.append(getBodyText(child));
                    buffer.append(" \n");
                }

                break;

            case Node.TEXT_NODE:
                buffer.append(((Text) child).getData());

                break;
            }
        }

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param charset DOCUMENT ME!
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sleep DOCUMENT ME!
     */
    public void setSleep(long sleep) {
        // TODO Auto-generated method stub
    }
}
