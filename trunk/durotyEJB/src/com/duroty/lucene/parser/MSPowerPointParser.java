/*
 * MsWordParser.java
 *
 * Created on 15 de noviembre de 2004, 13:06
 */
package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class MSPowerPointParser implements Parser, POIFSReaderListener {
    /** DOCUMENT ME! */
    private InputStream in;

    /** DOCUMENT ME! */
    private ByteArrayOutputStream writer;

    /**
     * Creates a new instance of OooWriter
     */
    public MSPowerPointParser() {
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
            in = new FileInputStream(file);
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
        this.in = in;

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
            in = new FileInputStream(fileName);
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
            if (in != null) {
                try {
                    in.close();
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
        String contents = "";

        try {
            POIFSReader reader = new POIFSReader();
            writer = new ByteArrayOutputStream();
            reader.registerListener(this);
            reader.read(in);
            contents = writer.toString();
        } catch (Exception ex) {
            throw new ParserException(ex);
        }

        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void processPOIFSReaderEvent(POIFSReaderEvent event) {
        try {
            if (!event.getName().equalsIgnoreCase("PowerPoint Document")) {
                return;
            }

            DocumentInputStream input = event.getStream();

            byte[] buffer = new byte[input.available()];
            input.read(buffer, 0, input.available());

            byte[] espace = new String("\n\n").getBytes();

            for (int i = 0; i < (buffer.length - 20); i++) {
                long type = LittleEndian.getUShort(buffer, i + 2);
                long size = LittleEndian.getUInt(buffer, i + 4);

                if (type == 4008) {
                    writer.write(buffer, i + 4 + 1, (int) size + 3);
                    writer.write(espace);
                    i = (i + 4 + 1 + (int) size) - 1;
                }

                /*if (sleep > 0) {
                        try {
                            Thread.sleep(sleep);
                        } catch (Exception ex) {
                        }
                    }*/
            }
        } catch (Exception ex) {
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
