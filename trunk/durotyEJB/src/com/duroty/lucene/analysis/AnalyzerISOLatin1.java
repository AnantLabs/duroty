/*
 * AnalyzerISOLatin1.java
 *
 * Created on 13 de noviembre de 2004, 23:08
 */
package com.duroty.lucene.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;

import com.duroty.lucene.analysis.filter.ISOLatin1AccentFilter;
import com.duroty.lucene.analysis.filter.RdLowerCaseTokenizer;


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
