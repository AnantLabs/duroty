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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public abstract class ZipParser implements Parser {
    /** DOCUMENT ME! */
    protected ZipFile zFile;

    /**
     * Creates a new instance of ZipParser
     */
    public ZipParser() {
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
            this.zFile = new ZipFile(file);
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
        throw new ParserException(
            "The zip parser don't support inputStream, user file or filename");
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
            this.zFile = new ZipFile(new File(fileName));
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
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    protected abstract String parse() throws ParserException;
}
