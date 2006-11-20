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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class TextParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /**
     * Creates a new instance of TextParser
     */
    public TextParser() {
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
     *
     * @throws ParserException DOCUMENT ME!
     */
    private String getContents() throws ParserException {
        BufferedReader br = null;
        StringWriter sw = null;
        String contents = "";

        try {
            br = new BufferedReader(new InputStreamReader(input));
            sw = new StringWriter();

            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                sw.write(line + "\n");

                /*if (sleep > 0) {
                        try {
                            Thread.sleep(sleep);
                        } catch (Exception ex) {
                        }
                    }*/
            }

            contents = sw.toString();
        } catch (IOException ioe) {
            throw new ParserException(ioe);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                }
            }

            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception ex) {
                }
            }
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
