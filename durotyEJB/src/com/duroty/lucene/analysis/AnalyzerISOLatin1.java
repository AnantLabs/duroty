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


package com.duroty.lucene.analysis;

import com.duroty.lucene.analysis.filter.ISOLatin1AccentFilter;
import com.duroty.lucene.analysis.filter.RdLowerCaseTokenizer;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;

import java.io.Reader;


/**
 * DOCUMENT ME!
 *
 * @author Administrador
 */
public class AnalyzerISOLatin1 extends DefaultAnalyzer {
    /**
     * Creates a new instance of AnalyzerISOLatin1
     */
    public AnalyzerISOLatin1() {
        super();
    }

    /**
     * Creates a new AnalyzerISOLatin1 object.
     *
     * @param stopWords DOCUMENT ME!
     */
    public AnalyzerISOLatin1(String[] stopWords) {
        super(stopWords);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        // The token stream that will be returned.
        TokenStream result;

        // Builds the chain...
        /*result = new StandardTokenizer(reader);
        result = new StandardFilter(result);
        result = new LowerCaseFilter(result);*/
        result = new RdLowerCaseTokenizer(reader);

        if (stopTable != null) {
            result = new StopFilter(result, stopTable);
        } else {
        }

        result = new ISOLatin1AccentFilter(result);

        result = new SnowballFilter(result, "English");
        result = new SnowballFilter(result, "Spanish");

        //result = new SnowballFilter(result, "French");
        //result = new SnowballFilter(result, "Italian");
        return result;
    }
}
