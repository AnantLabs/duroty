/*
 * ISOLatin1Filter.java
 *
 * Created on 13 de noviembre de 2004, 23:09
 */
package com.duroty.lucene.analysis.filter;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;


/**
 *
 * @author  Administrador
 */
public class ISOLatin1AccentFilter extends TokenFilter {
    /** Creates a new instance of ISOLatin1Filter */
    public ISOLatin1AccentFilter(TokenStream ts) {
        super(ts);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Token next() throws IOException {
        Token t = input.next();

        if (t == null) {
            return null;
        }

        String text = t.termText();
        String type = t.type();

        int idx = text.indexOf("'");

        // Removes if the apostrophe is at beginning.
        if (idx < 2) {
            if (text.length() > (idx + 3)) {
                t = new Token(text.substring(idx + 1), t.startOffset(),
                        t.endOffset(), type);
            }

            // BUG: shoud return null, but with Lucene 1 all following words are deleted!
        }

        if (t == null) {
            return null;
        }

        text = t.termText();
        type = t.type();

        StringBuffer trimmed = new StringBuffer();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c != '.') {
                trimmed.append(c);
            }
        }

        t = new Token(trimmed.toString(), t.startOffset(), t.endOffset(), type);

        if (t == null) {
            return null;
        }

        String tokenText = t.termText();
        StringBuffer chars = new StringBuffer();

        // Loop over the characters, replace those that need to be.
        for (int i = 0; i < tokenText.length(); i++) {
            switch (tokenText.charAt(i)) {
            case 'À':
            case 'Á':
            case 'Â':
            case 'Ã':
            case 'Ä':
            case 'Å':
                chars.append("A");

                break;

            case 'Æ':
                chars.append("AE");

                break;

            case 'Ç':
                chars.append("C");

                break;

            case 'È':
            case 'É':
            case 'Ê':
            case 'Ë':
                chars.append("E");

                break;

            case 'Ì':
            case 'Í':
            case 'Î':
            case 'Ï':
                chars.append("I");

                break;

            case 'Ð':
                chars.append("D");

                break;

            case 'Ñ':
                chars.append("N");

                break;

            case 'Ò':
            case 'Ó':
            case 'Ô':
            case 'Õ':
            case 'Ö':
            case 'Ø':
                chars.append("O");

                break;

            case 'Œ':
                chars.append("OE");

                break;

            case 'Þ':
                chars.append("TH");

                break;

            case 'Ù':
            case 'Ú':
            case 'Û':
            case 'Ü':
                chars.append("U");

                break;

            case 'Ý':
            case 'Ÿ':
                chars.append("Y");

                break;

            case 'à':
            case 'á':
            case 'â':
            case 'ã':
            case 'ä':
            case 'å':
                chars.append("a");

                break;

            case 'æ':
                chars.append("ae");

                break;

            case 'ç':
                chars.append("c");

                break;

            case 'è':
            case 'é':
            case 'ê':
            case 'ë':
                chars.append("e");

                break;

            case 'ì':
            case 'í':
            case 'î':
            case 'ï':
                chars.append("i");

                break;

            case 'ð':
                chars.append("d");

                break;

            case 'ñ':
                chars.append("n");

                break;

            case 'ò':
            case 'ó':
            case 'ô':
            case 'õ':
            case 'ö':
            case 'ø':
                chars.append("o");

                break;

            case 'œ':
                chars.append("oe");

                break;

            case 'ß':
                chars.append("ss");

                break;

            case 'þ':
                chars.append("th");

                break;

            case 'ù':
            case 'ú':
            case 'û':
            case 'ü':
                chars.append("u");

                break;

            case 'ý':
            case 'ÿ':
                chars.append("y");

                break;

            default:
                chars.append(tokenText.charAt(i));

                break;
            }
        }

        // Finally we return a new token with transformed characters.
        return new Token(chars.toString(), t.startOffset(), t.endOffset(),
            t.type());
    }
}
