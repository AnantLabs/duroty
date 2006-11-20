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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class MSExcelParser implements Parser {
    /** DOCUMENT ME! */
    private InputStream input;

    /**
     * Creates a new instance of OooWriter
     */
    public MSExcelParser() {
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
        String contents = "";

        try {
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);

                Iterator rows = sheet.rowIterator();

                while (rows.hasNext()) {
                    HSSFRow row = (HSSFRow) rows.next();

                    Iterator cells = row.cellIterator();

                    while (cells.hasNext()) {
                        HSSFCell cell = (HSSFCell) cells.next();

                        switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC:

                            String num = Double.toString(cell.getNumericCellValue())
                                               .trim();

                            if (num.length() > 0) {
                                buffer.append(num + " ");
                            }

                            break;

                        case HSSFCell.CELL_TYPE_STRING:

                            String text = cell.getStringCellValue().trim();

                            if (text.length() > 0) {
                                buffer.append(text + " ");
                            }

                            break;
                        }
                    }

                    buffer.append("\n");

                    /*if (sleep > 0) {
                            try {
                                Thread.sleep(sleep);
                            } catch (Exception ex) {
                            }
                        }*/
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
        // TODO Auto-generated method stub
        return null;
    }
}
