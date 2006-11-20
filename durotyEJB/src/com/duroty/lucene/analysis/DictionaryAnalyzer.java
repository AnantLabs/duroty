/*
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.duroty.lucene.analysis;

import com.duroty.lucene.analysis.filter.ISOLatin1AccentFilter;
import com.duroty.lucene.analysis.filter.RdLowerCaseTokenizer;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.Reader;


/**
 * DOCUMENT ME!
 *
 * @author Administrador
 */
public class DictionaryAnalyzer extends DefaultAnalyzer {
    /**
     * Creates a new instance of DictionaryAnalyzer
     */
    public DictionaryAnalyzer() {
        super();
    }

    /**
     * Creates a new DictionaryAnalyzer object.
     *
     * @param stopWords DOCUMENT ME!
     */
    public DictionaryAnalyzer(String[] stopWords) {
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

        return result;
    }
}
