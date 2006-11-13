/*
 * AnalyzerISOLatin1.java
 *
 * Created on 13 de noviembre de 2004, 23:08
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
