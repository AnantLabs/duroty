/*
 * DefaultAnalyzer.java
 *
 * Created on 13 de noviembre de 2004, 23:25
 */
package com.duroty.lucene.analysis;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


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
            "with", "would", "up", "a", "acá", "ahí", "ajena", "ajenas", "ajeno",
            "ajenos", "al", "algo", "alguna", "algunas", "alguno", "algunos",
            "algún", "allá", "allí", "aquel", "aquella", "aquellas", "aquello",
            "aquellos", "aquí", "cada", "cierta", "ciertas", "cierto", "ciertos",
            "como", "cómo", "con", "conmigo", "consigo", "contigo", "cualquier",
            "cualquiera", "cualquieras", "cuan", "cuanta", "cuantas", "cuánta",
            "cuántas", "cuanto", "cuantos", "cuán", "cuánto", "cuántos", "de",
            "dejar", "del", "demasiada", "demasiadas", "demasiado", "demasiados",
            "demás", "el", "ella", "ellas", "ellos", "él", "esa", "esas", "ese",
            "esos", "esta", "estar", "estas", "este", "estos", "hacer", "hasta",
            "jamás", "junto", "juntos", "la", "las", "lo", "los", "mas", "más",
            "me", "menos", "mía", "mientras", "mío", "misma", "mismas", "mismo",
            "mismos", "mucha", "muchas", "muchísima", "muchísimas", "muchísimo",
            "muchísimos", "mucho", "muchos", "muy", "nada", "ni", "ninguna",
            "ningunas", "ninguno", "ningunos", "no", "nos", "nosotras",
            "nosotros", "nuestra", "nuestras", "nuestro", "nuestros", "nunca",
            "os", "otra", "otras", "otro", "otros", "para", "parecer", "poca",
            "pocas", "poco", "pocos", "por", "porque", "que", "querer", "qué",
            "quien", "quienes", "quienesquiera", "quienquiera", "quién", "ser",
            "si", "siempre", "sí", "sín", "Sr", "Sra", "Sres", "Sta", "suya",
            "suyas", "suyo", "suyos", "tal", "tales", "tan", "tanta", "tantas",
            "tanto", "tantos", "te", "tener", "ti", "toda", "todas", "todo",
            "todos", "tomar", "tuya", "tuyo", "tú", "un", "una", "unas", "unos",
            "usted", "ustedes", "varias", "varios", "vosotras", "vosotros",
            "vuestra", "vuestras", "vuestro", "vuestros", "y", "yo", "a", "ahí",
            "o", "al", "als", "alguna", "algunes", "alguns", "algú", "allá",
            "allí", "quelcom", "aquella", "aquelles", "aquell", "aquells",
            "aquí", "cada", "certa", "certes", "cert", "certs", "com", "amb",
            "mi", "tu", "qualsevol", "qualsevols", "quantes", "quants",
            "cuantas", "cuánta", "cuántas", "cuanto", "quant", "quants", "de",
            "deixar", "del", "des", "masses", "massa", "demes", "el", "ella",
            "elles", "ells", "él", "esa", "ese", "ese", "esos", "aquesta",
            "estar", "aquestes", "aquest", "aquests", "fer", "fins", "mai",
            "junt", "junts", "la", "les", "el", "els", "mes", "més", "em",
            "menys", "meva", "mentres", "meu", "mateixa", "mateixes", "mateix",
            "mateixos", "molta", "moltes", "moltisima", "moltisimes", "moltisim",
            "moltisims", "molts", "molt", "cap", "ni", "ninguna", "ningunes",
            "ninguno", "ningu", "no", "ens", "nosaltres", "nosotros", "nostra",
            "nostres", "mai", "os", "altre", "altres", "per", "semblar", "poc",
            "poques", "pocs", "perque", "que", "voler", "qué", "quins", "quins",
            "qualsevol", "qualsevol", "qui", "esser", "si", "sempre", "sí",
            "sense", "Sr", "Sra", "Sres", "Sta", "seva", "seves", "seu", "seus",
            "tal", "tales", "tan", "tanta", "tantes", "tant", "tants", "te",
            "tenir", "ti", "tota", "totes", "tot", "tots", "tomar", "teva",
            "teu", "tú", "un", "una", "unes", "uns", "voste", "vostes", "varis",
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
