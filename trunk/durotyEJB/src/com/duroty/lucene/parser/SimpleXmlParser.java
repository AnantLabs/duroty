/*
 * DefaultXmlParser.java
 *
 * Created on 15 de noviembre de 2004, 13:01
 */
package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import org.apache.commons.io.IOUtils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class SimpleXmlParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /** DOCUMENT ME! */
    private StringBuffer buffer;

    /**
     * Creates a new instance of DefaultXmlParser
     */
    public SimpleXmlParser() {
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
     * @param fileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    public String parse(String fileName) throws ParserException {
        try {
            input = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new ParserException(e);
        }

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
            return this.getContents();
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
     *
     * @throws ParserException DOCUMENT ME!
     */
    private String getContents() throws ParserException {
        /** DOCUMENT ME! */
        String contents = "";
        DocumentBuilderFactory factory;

        /** DOCUMENT ME! */
        DocumentBuilder builder;
        factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();

            org.w3c.dom.Document document = builder.parse(input);

            buffer = new StringBuffer();
            traverseTree(document);
            contents = buffer.toString();
        } catch (Exception ex) {
            throw new ParserException(ex);
        }

        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    private void traverseTree(Node node) {
        /*if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (Exception ex) {
            }
            }*/
        if (node == null) {
            return;
        }

        int type = node.getNodeType();

        if (type == Node.DOCUMENT_NODE) {
            traverseTree(((org.w3c.dom.Document) node).getDocumentElement());
        } else if (type == Node.TEXT_NODE) {
            try {
                String value = node.getNodeValue();

                if ((value != null) && !value.equals("") &&
                        !value.startsWith("\n")) {
                    buffer.append(value + "\n");
                }
            } catch (Exception ex) {
                //buffer.append("\n");
            }

            NodeList childNodes = node.getChildNodes();

            if (childNodes != null) {
                for (int i = 0; i < childNodes.getLength(); i++) {
                    traverseTree(childNodes.item(i));
                }
            }
        } else {
            NodeList childNodes = node.getChildNodes();

            if (childNodes != null) {
                for (int i = 0; i < childNodes.getLength(); i++) {
                    traverseTree(childNodes.item(i));
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.duroty.lucene.parser.Parser#setSleep(long)
     */
    public void setSleep(long sleep) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param charset DOCUMENT ME!
     */
    public void setCharset(String charset) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }
}
