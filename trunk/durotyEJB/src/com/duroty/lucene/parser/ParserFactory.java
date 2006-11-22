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

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.lucene.parser.exception.ParserException;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeUtility;

import javax.naming.Context;
import javax.naming.InitialContext;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class ParserFactory {
    /**
     * DOCUMENT ME!
     */
    private HashMap parseFactoryConfig = new HashMap();

    /**
     * Creates a new instance of ParserFactory
     */
    public ParserFactory() {
        Map options = ApplicationConstants.options;

        try {
            Context ctx = new InitialContext();

            this.parseFactoryConfig = (HashMap) ctx.lookup((String) options.get(
                        Constants.PARSE_FACTORY_CONFIG));
        } catch (Exception e) {
            this.parseFactoryConfig = new HashMap();
        }
    }

    /**
     * Creates a new ParserFactory object.
     *
     * @param properties DOCUMENT ME!
     */
    public ParserFactory(String properties) {
        this();
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param mimetype DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String parse(String str, String mimetype, String charset) {
        if ((str == null) || str.trim().equals("")) {
            return null;
        }

        InputStream input = null;

        String className = (String) parseFactoryConfig.get(mimetype);

        //log.info("PARSE ATTACHMENT: " + className + " >> " + mimetype);
        if (className == null) {
            return null;
        }

        try {
            Class clazz = Class.forName(className);

            Parser parser = (Parser) clazz.newInstance();

            parser.setCharset(MimeUtility.javaCharset(charset));

            input = IOUtils.toInputStream(str);

            return parser.parse(input);
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ParserException e) {
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(input);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     * @param mimetype DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String parse(InputStream input, String mimetype, String charset) {
        if (input == null) {
            return null;
        }

        String className = (String) parseFactoryConfig.get(mimetype);

        //log.info("PARSE ATTACHMENT: " + className + " >> " + mimetype);
        if (className == null) {
            return null;
        }

        try {
            Class clazz = Class.forName(className);

            Parser parser = (Parser) clazz.newInstance();

            parser.setCharset(MimeUtility.javaCharset(charset));

            return parser.parse(input);
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ParserException e) {
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(input);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bytes DOCUMENT ME!
     * @param mimetype DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String parse(byte[] bytes, String mimetype, String charset) {
        if ((bytes == null) || (bytes.length == 0)) {
            return null;
        }

        InputStream input = null;

        String className = (String) parseFactoryConfig.get(mimetype);

        //log.info("PARSE ATTACHMENT: " + className + " >> " + mimetype);
        if (className == null) {
            return null;
        }

        try {
            Class clazz = Class.forName(className);

            Parser parser = (Parser) clazz.newInstance();

            parser.setCharset(MimeUtility.javaCharset(charset));

            input = new ByteArrayInputStream(bytes);

            return parser.parse(input);
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ParserException e) {
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(input);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new ParserFactory();
    }
}
