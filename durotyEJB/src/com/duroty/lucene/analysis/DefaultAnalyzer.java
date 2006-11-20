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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.Reader;

import java.util.Set;


/**
 * DOCUMENT ME!
 *
 * @author Administrador
 */
public class DefaultAnalyzer extends Analyzer {
    /** DOCUMENT ME! */
    protected static Set stopTable;

    /** DOCUMENT ME! */
    public static final String[] DEFAULT_STOP_WORDS = {
            "a", "about", "after", "all", "also", "an", "any", "and", "are",
            "as", "at", "be", "because", "been", "but", "by", "can", "co",
            "corp", "could", "for", "from", "had", "has", "have", "he", "her",
            "his", "if", "in", "inc", "into", "is", "it", "its", "last", "more",
            "most", "mr", "mrs", "ms", "mz", "no", "not", "only", "of", "on",
            "one", "or", "other", "out", "over", "s", "so", "says", "she",
            "some", "such", "than", "that", "the", "their", "there", "they",
            "this", "to", "was", "we", "were", "when", "which", "who", "will",
            "with", "would", "up", "a", "ac�", "ah�", "ajena", "ajenas", "ajeno",
            "ajenos", "al", "algo", "alguna", "algunas", "alguno", "algunos",
            "alg�n", "all�", "all�", "aquel", "aquella", "aquellas", "aquello",
            "aquellos", "aqu�", "cada", "cierta", "ciertas", "cierto", "ciertos",
            "como", "c�mo", "con", "conmigo", "consigo", "contigo", "cualquier",
            "cualquiera", "cualquieras", "cuan", "cuanta", "cuantas", "cu�nta",
            "cu�ntas", "cuanto", "cuantos", "cu�n", "cu�nto", "cu�ntos", "de",
            "dejar", "del", "demasiada", "demasiadas", "demasiado", "demasiados",
            "dem�s", "el", "ella", "ellas", "ellos", "�l", "esa", "esas", "ese",
            "esos", "esta", "estar", "estas", "este", "estos", "hacer", "hasta",
            "jam�s", "junto", "juntos", "la", "las", "lo", "los", "mas", "m�s",
            "me", "menos", "m�a", "mientras", "m�o", "misma", "mismas", "mismo",
            "mismos", "mucha", "muchas", "much�sima", "much�simas", "much�simo",
            "much�simos", "mucho", "muchos", "muy", "nada", "ni", "ninguna",
            "ningunas", "ninguno", "ningunos", "no", "nos", "nosotras",
            "nosotros", "nuestra", "nuestras", "nuestro", "nuestros", "nunca",
            "os", "otra", "otras", "otro", "otros", "para", "parecer", "poca",
            "pocas", "poco", "pocos", "por", "porque", "que", "querer", "qu�",
            "quien", "quienes", "quienesquiera", "quienquiera", "qui�n", "ser",
            "si", "siempre", "s�", "s�n", "Sr", "Sra", "Sres", "Sta", "suya",
            "suyas", "suyo", "suyos", "tal", "tales", "tan", "tanta", "tantas",
            "tanto", "tantos", "te", "tener", "ti", "toda", "todas", "todo",
            "todos", "tomar", "tuya", "tuyo", "t�", "un", "una", "unas", "unos",
            "usted", "ustedes", "varias", "varios", "vosotras", "vosotros",
            "vuestra", "vuestras", "vuestro", "vuestros", "y", "yo", "a", "ah�",
            "o", "al", "als", "alguna", "algunes", "alguns", "alg�", "all�",
            "all�", "quelcom", "aquella", "aquelles", "aquell", "aquells",
            "aqu�", "cada", "certa", "certes", "cert", "certs", "com", "amb",
            "mi", "tu", "qualsevol", "qualsevols", "quantes", "quants",
            "cuantas", "cu�nta", "cu�ntas", "cuanto", "quant", "quants", "de",
            "deixar", "del", "des", "masses", "massa", "demes", "el", "ella",
            "elles", "ells", "�l", "esa", "ese", "ese", "esos", "aquesta",
            "estar", "aquestes", "aquest", "aquests", "fer", "fins", "mai",
            "junt", "junts", "la", "les", "el", "els", "mes", "m�s", "em",
            "menys", "meva", "mentres", "meu", "mateixa", "mateixes", "mateix",
            "mateixos", "molta", "moltes", "moltisima", "moltisimes", "moltisim",
            "moltisims", "molts", "molt", "cap", "ni", "ninguna", "ningunes",
            "ninguno", "ningu", "no", "ens", "nosaltres", "nosotros", "nostra",
            "nostres", "mai", "os", "altre", "altres", "per", "semblar", "poc",
            "poques", "pocs", "perque", "que", "voler", "qu�", "quins", "quins",
            "qualsevol", "qualsevol", "qui", "esser", "si", "sempre", "s�",
            "sense", "Sr", "Sra", "Sres", "Sta", "seva", "seves", "seu", "seus",
            "tal", "tales", "tan", "tanta", "tantes", "tant", "tants", "te",
            "tenir", "ti", "tota", "totes", "tot", "tots", "tomar", "teva",
            "teu", "t�", "un", "una", "unes", "uns", "voste", "vostes", "varis",
            "varies", "vosaltres", "vosotros", "vostra", "vostres", "i", "jo",
            "en"
        };

    /**
     * Creates a new instance of DefaultAnalyzer
     */
    public DefaultAnalyzer() {
        this(DefaultAnalyzer.DEFAULT_STOP_WORDS);
    }

    /**
     * Creates a new instance of DefaultAnalyzer
     *
     * @param stopWords DOCUMENT ME!
     */
    public DefaultAnalyzer(String[] stopWords) {
        buildStopTable(stopWords);
    }

    /**
     * DOCUMENT ME!
     *
     * @param words DOCUMENT ME!
     */
    protected void buildStopTable(String[] words) {
        stopTable = StopFilter.makeStopSet(words);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);

        result = new StandardFilter(result);
        result = new LowerCaseFilter(result);

        if (stopTable != null) {
            return new StopFilter(result, stopTable);
        } else {
            return result;
        }
    }
}
