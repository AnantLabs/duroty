/*
 * Created on 03-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.duroty.lucene.analysis.filter;

import org.apache.lucene.analysis.CharTokenizer;

import java.io.Reader;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class RdLetterTokenizer extends CharTokenizer {
    /**
     * Construct a new LetterTokenizer.
     *
     * @param in DOCUMENT ME!
     */
    public RdLetterTokenizer(Reader in) {
        super(in);
    }

    /**
     * Collects only characters which satisfy {@link Character#isLetter(char)}.
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isTokenChar(char c) {
        return Character.isLetter(c) || Character.isDigit(c); 
    }
}
