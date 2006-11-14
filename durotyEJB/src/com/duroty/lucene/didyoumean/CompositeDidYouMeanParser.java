package com.duroty.lucene.didyoumean;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.io.StringReader;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class CompositeDidYouMeanParser implements DidYouMeanParser {
    /**
     * DOCUMENT ME!
     */
    private String defaultField;

    /**
     * DOCUMENT ME!
     */
    private Directory spellIndexDirectory;

    /**
     * DOCUMENT ME!
     */
    private Analyzer analyzer;

    /**
     * Creates a new CompositeDidYouMeanParser object.
     *
     * @param defaultField DOCUMENT ME!
     * @param spellIndexDirectory DOCUMENT ME!
     */
    public CompositeDidYouMeanParser(String defaultField,
        Directory spellIndexDirectory, Analyzer analyzer) {
        this.defaultField = defaultField;
        this.spellIndexDirectory = spellIndexDirectory;
        this.analyzer = analyzer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public Query parse(String queryString) throws ParseException {
        QueryParser queryParser = new QueryParser(defaultField, analyzer);
        queryParser.setDefaultOperator(Operator.AND);

        return queryParser.parse(queryString);
    }

    /**
     * DOCUMENT ME!
     *
     * @param queryString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public Query suggest(String queryString, Operator operator)
        throws ParseException {
        QuerySuggester querySuggester = new QuerySuggester(defaultField,
                analyzer);
        querySuggester.setDefaultOperator(operator);

        Query query = querySuggester.parse(queryString);

        return querySuggester.hasSuggestedQuery() ? query : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision$
     */
    private class QuerySuggester extends QueryParser {
        /**
         * DOCUMENT ME!
         */
        private boolean suggestedQuery = false;

        /**
         * Creates a new QuerySuggester object.
         *
         * @param field DOCUMENT ME!
         * @param analyzer DOCUMENT ME!
         */
        public QuerySuggester(String field, Analyzer analyzer) {
            super(field, analyzer);
        }

        /**
         * DOCUMENT ME!
         *
         * @param field DOCUMENT ME!
         * @param queryText DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws ParseException DOCUMENT ME!
         */
        protected Query getFieldQuery(String field, String queryText)
            throws ParseException {
            // Copied from org.apache.lucene.queryParser.QueryParser
            // replacing construction of TermQuery with call to getTermQuery()
            // which finds close matches.
            TokenStream source = getAnalyzer().tokenStream(field,
                    new StringReader(queryText));
            Vector v = new Vector();
            Token t;

            while (true) {
                try {
                    t = source.next();
                } catch (IOException e) {
                    t = null;
                }

                if (t == null) {
                    break;
                }

                v.addElement(t.termText());
            }

            try {
                source.close();
            } catch (IOException e) {
                // ignore
            }

            if (v.size() == 0) {
                return null;
            } else if (v.size() == 1) {
                return new TermQuery(getTerm(field, (String) v.elementAt(0)));
            } else {
                PhraseQuery q = new PhraseQuery();
                q.setSlop(getPhraseSlop());

                for (int i = 0; i < v.size(); i++) {
                    q.add(getTerm(field, (String) v.elementAt(i)));
                }

                return q;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param field DOCUMENT ME!
         * @param queryText DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws ParseException DOCUMENT ME!
         */
        private Term getTerm(String field, String queryText)
            throws ParseException {
            SpellChecker spellChecker = new SpellChecker(spellIndexDirectory);

            try {
                if (spellChecker.exist(queryText)) {
                    return new Term(field, queryText);
                }

                String[] similarWords = spellChecker.suggestSimilar(queryText, 1);

                if (similarWords.length == 0) {
                    return new Term(field, queryText);
                }

                suggestedQuery = true;

                return new Term(field, similarWords[0]);
            } catch (IOException e) {
                throw new ParseException(e.getMessage());
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean hasSuggestedQuery() {
            return suggestedQuery;
        }
    }
}
