/**
 *
 */
package com.duroty.lucene.mail.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import com.duroty.hibernate.Filter;
import com.duroty.lucene.analysis.KeywordAnalyzer;
import com.duroty.lucene.mail.LuceneMessageConstants;


/**
 * @author durot
 *
 */
public class FilterQueryParser implements LuceneMessageConstants {
    /**
    * DOCUMENT ME!
    */
    public static final int NORMAL_FIELD = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int REQUIRED_FIELD = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int PROHIBITED_FIELD = 2;

    /**
     * The actual operator that parser uses to combine query terms
     */
    private static Operator operator = Operator.AND;

    /**
     * Creates a new FilterQueryParser object.
     *
     * @param f DOCUMENT ME!
     * @param a DOCUMENT ME!
     */
    public FilterQueryParser(String f, Analyzer a) {
        super();
    }

    /**
     * Creates a new FilterQueryParser object.
     *
     * @param stream DOCUMENT ME!
     */
    public FilterQueryParser(CharStream stream) {
        super();
    }

    /**
     * Creates a new FilterQueryParser object.
     *
     * @param tm DOCUMENT ME!
     */
    public FilterQueryParser(QueryParserTokenManager tm) {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public static Query parse(Filter[] filters, Analyzer analyzer)
        throws ParseException {
        if ((filters != null) && (filters.length > 1)) {
            BooleanQuery bQuery = new BooleanQuery();

            for (int i = 0; i < filters.length; i++) {
                Query q = parse(filters[i], analyzer);

                if (q != null) {
                    //bQuery.add(q, false, false);
                    bQuery.add(q, BooleanClause.Occur.SHOULD);
                }
            }

            return bQuery;
        } else if ((filters != null) && (filters.length == 1)) {
            return parse(filters[0], analyzer);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public static Query parse(Filter filter, Analyzer analyzer)
        throws ParseException {
        if (filter == null) {
            return null;
        }

        String petador = "AND";

        if (filter.isFilOrOperator()) {
            petador = "OR";
        }

        StringBuffer buffer = new StringBuffer();

        if ((filter.getFilFrom() != null) &&
                !filter.getFilFrom().trim().equals("")) {
            QueryParser parser = new QueryParser(Field_from, analyzer);
            parser.setDefaultOperator(FilterQueryParser.operator);

            Query aux = parser.parse(filter.getFilFrom());

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ")");
        }

        if ((filter.getFilTo() != null) &&
                !filter.getFilTo().trim().equals("")) {
            QueryParser parserTo = new QueryParser(Field_to, analyzer);
            parserTo.setDefaultOperator(FilterQueryParser.operator);

            Query auxTo = parserTo.parse(filter.getFilTo());

            QueryParser parserCc = new QueryParser(Field_cc, analyzer);
            parserCc.setDefaultOperator(FilterQueryParser.operator);

            Query auxCc = parserCc.parse(filter.getFilTo());

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + auxTo.toString() + " OR " + auxCc.toString() +
                ") ");
        }

        if ((filter.getFilSubject() != null) &&
                !filter.getFilSubject().trim().equals("")) {
            QueryParser parser = new QueryParser(Field_subject, analyzer);
            parser.setDefaultOperator(FilterQueryParser.operator);

            Query aux = parser.parse(filter.getFilSubject());

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        }

        if ((filter.getFilHasWords() != null) &&
                !filter.getFilHasWords().trim().equals("")) {
            QueryParser parserBody = new QueryParser(Field_body, analyzer);
            parserBody.setDefaultOperator(FilterQueryParser.operator);

            Query auxBody = parserBody.parse(filter.getFilHasWords());

            QueryParser parserAttach = new QueryParser(Field_attachments,
                    analyzer);
            parserAttach.setDefaultOperator(FilterQueryParser.operator);

            Query auxAttach = parserAttach.parse(filter.getFilHasWords());

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + auxBody.toString() + " OR " +
                auxAttach.toString() + ") ");
        }

        if ((filter.getFilDoesntHaveWords() != null) &&
                !filter.getFilDoesntHaveWords().trim().equals("")) {
            QueryParser parserBody = new QueryParser(Field_body, analyzer);
            parserBody.setDefaultOperator(FilterQueryParser.operator);

            Query auxBody = parserBody.parse(filter.getFilDoesntHaveWords());

            QueryParser parserAttach = new QueryParser(Field_attachments,
                    analyzer);
            parserAttach.setDefaultOperator(FilterQueryParser.operator);

            Query auxAttach = parserAttach.parse(filter.getFilDoesntHaveWords());

            if (buffer.length() > 0) {
                buffer.append(" NOT ");
            }

            buffer.append("(" + auxBody.toString() + " OR " +
                auxAttach.toString() + ") ");
        }

        if (filter.isFilHasAttacment()) {
            QueryParser parser = new QueryParser(Field_has_attachments,
                    new KeywordAnalyzer());
            parser.setDefaultOperator(FilterQueryParser.operator);

            Query aux = parser.parse(String.valueOf(filter.isFilHasAttacment()));

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        }

        if (buffer.length() > 0) {
            QueryParser parser = new QueryParser("", new WhitespaceAnalyzer());
            Query query = parser.parse(buffer.toString());

            return query;
        } else {
            return null;
        }
    }

    /**
     * @return Returns the operator.
     */
    public Operator getDefaultOperator() {
        return operator;
    }

    /**
     * @param operator The operator to set.
     */
    public void setOperator(Operator operator) {
        FilterQueryParser.operator = operator;
    }
}
