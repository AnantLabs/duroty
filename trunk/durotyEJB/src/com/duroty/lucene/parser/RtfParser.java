/*
 * RtfParser.java
 *
 * Created on 15 de noviembre de 2004, 12:43
 */
package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class RtfParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /**
     * Creates a new instance of RtfParser
     */
    public RtfParser() {
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
            return this.getContents();
        } catch (Exception ex) {
            throw new ParserException(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getContents() {
        String contents = "";

        try {
            //creating a default blank styled document
            DefaultStyledDocument styledDoc = new DefaultStyledDocument();

            //Creating a RTF Editor kit
            RTFEditorKit rtfKit = new RTFEditorKit();

            //Populating the contents in the blank styled document
            rtfKit.read(input, styledDoc, 0);

            //Printing out the contents of the RTF document as plain text
            contents = styledDoc.getText(0, styledDoc.getLength());

            /*if (sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (Exception ex) {
                    }
                }*/
        } catch (Exception ex) {
        } finally {
        }

        return contents;
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
