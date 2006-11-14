/*
 * ParserFactory.java
 *
 * Created on 27 de octubre de 2004, 10:38
 */
package com.duroty.lucene.parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;

import com.duroty.lucene.parser.exception.ParserException;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class ParserFactory {
	/**
     * DOCUMENT ME!
     */
    //private static final Log log = LogFactory.getLog(ParserFactory.class);
    
    /**
     * DOCUMENT ME!
     */
    private Hashtable mimeTypeSupported = new Hashtable();

    /**
     * Creates a new instance of ParserFactory
     */
    public ParserFactory() {    
    	BasicConfigurator.configure();
    	
        ResourceBundle bundle = ResourceBundle.getBundle("com.duroty.lucene.parser.properties.MimeType");

        Enumeration keys = bundle.getKeys();

        while (keys.hasMoreElements()) {
            String mimetype = (String) keys.nextElement();
            mimeTypeSupported.put(mimetype, bundle.getString(mimetype));
        }
    }

    /**
     * Creates a new ParserFactory object.
     *
     * @param propsFileName DOCUMENT ME!
     */
    public ParserFactory(String propsFileName) {    	
    	//BasicConfigurator.configure();
    	
        Properties props = new Properties();

        try {    	        	
            props.load(new FileInputStream(propsFileName));

            Enumeration keys = props.keys();

            while (keys.hasMoreElements()) {
                String mimetype = (String) keys.nextElement();
                String value = props.getProperty(mimetype);
                mimeTypeSupported.put(mimetype, value);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
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
        
        String className = (String) mimeTypeSupported.get(mimetype);
        
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
        
        String className = (String) mimeTypeSupported.get(mimetype);
        
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
        
        String className = (String) mimeTypeSupported.get(mimetype);
        
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
