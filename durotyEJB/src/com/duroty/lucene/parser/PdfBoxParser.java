/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/


package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import org.apache.commons.io.IOUtils;

import org.pdfbox.exceptions.CryptographyException;
import org.pdfbox.exceptions.InvalidPasswordException;

import org.pdfbox.pdmodel.PDDocument;

import org.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * DOCUMENT ME!
 *
 * @author durot TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class PdfBoxParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /**
     * Creates a new PdfBoxParser object.
     */
    public PdfBoxParser() {
        super();

        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.duroty.lucene.parser.Parser#parse(java.lang.String)
     */
    public String parse(String fileName) throws ParserException {
        try {
            input = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new ParserException(e);
        }

        return parse();
    }

    /* (non-Javadoc)
     * @see com.duroty.lucene.parser.Parser#parse(java.io.File)
     */
    public String parse(File file) throws ParserException {
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ParserException(e);
        }

        return parse();
    }

    /* (non-Javadoc)
     * @see com.duroty.lucene.parser.Parser#parse(java.io.InputStream)
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
     * @throws IOException DOCUMENT ME!
     */
    private String parse() throws ParserException {
        PDDocument pdfDocument = null;
        StringWriter writer = null;

        try {
            pdfDocument = PDDocument.load(input);

            if (pdfDocument.isEncrypted()) {
                //Just try using the default password and move on
                pdfDocument.decrypt("");
            }

            //create a writer where to append the text content.
            writer = new StringWriter();

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.writeText(pdfDocument, writer);

            // Note: the buffer to string operation is costless;
            // the char array value of the writer buffer and the content string
            // is shared as long as the buffer content is not modified, which will
            // not occur here.
            return writer.getBuffer().toString();
        } catch (CryptographyException e) {
        } catch (InvalidPasswordException e) {
        } catch (IOException e) {
        } finally {
            if (pdfDocument != null) {
                try {
                    pdfDocument.close();
                } catch (IOException e) {
                }
            }

            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(writer);
        }

        return null;
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
