/*
 * OooCalc.java
 *
 * Created on 15 de noviembre de 2004, 13:05
 */
package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class OooCalcParser implements Parser {
    /** DOCUMENT ME! */
    private final String CODE_SXC = "content.xml";

    /**
     * DOCUMENT ME!
     */
    private String charset;

    /** DOCUMENT ME! */
    private ZipFile zp;

    /** DOCUMENT ME! */
    private InputStream input;

    /**
     * Creates a new OooWriterParser object.
     */
    public OooCalcParser() {
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
            zp = new ZipFile(file);
        } catch (ZipException e) {
            throw new ParserException(e);
        } catch (IOException e) {
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
        // TODO Auto-generated method stub
        return null;
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
            zp = new ZipFile(fileName);
        } catch (IOException e) {
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
            Enumeration en = zp.entries();

            while (en.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) en.nextElement();

                if (entry.getName().equals(CODE_SXC)) {
                    input = zp.getInputStream(entry);

                    break;
                }
            }

            SimpleXmlParser sxp = new SimpleXmlParser();
            sxp.setCharset(this.charset);

            return sxp.parse(input);
        } catch (Exception ex) {
            throw new ParserException(ex);
        } finally {
            if (zp != null) {
                try {
                    zp.close();
                } catch (IOException e) {
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
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
        this.charset = charset;
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
