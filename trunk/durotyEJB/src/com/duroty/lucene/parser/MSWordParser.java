/*
 * MsWordParser.java
 *
 * Created on 15 de noviembre de 2004, 13:06
 */
package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class MSWordParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /**
     * Creates a new instance of OooWriter
     */
    public MSWordParser() {
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
        String contents = "";

        try {
            HWPFDocument doc = new HWPFDocument(input);
            Range r = doc.getRange();
            StringBuffer buffer = new StringBuffer();

            for (int x = 0; x < r.numSections(); x++) {
                Section s = r.getSection(x);

                for (int y = 0; y < s.numParagraphs(); y++) {
                    Paragraph p = null;

                    try {
                        p = s.getParagraph(y);
                    } catch (Exception e) {
                        buffer.append("\n");
                    }

                    if (p != null) {
                        for (int z = 0; z < p.numCharacterRuns(); z++) {
                            try {
                                //character run
                                CharacterRun run = p.getCharacterRun(z);

                                //character run text
                                buffer.append(run.text());
                            } catch (Exception e) {
                                buffer.append(" ");
                            }
                        }
                    }

                    /*if (sleep > 0) {
                            try {
                                Thread.sleep(sleep);
                            } catch (Exception ex) {
                            }
                        }*/
                    // use a new line at the paragraph break
                    buffer.append("\n");
                }
            }

            contents = buffer.toString();
        } catch (Exception ex) {
            throw new ParserException(ex);
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
        return null;
    }
}
