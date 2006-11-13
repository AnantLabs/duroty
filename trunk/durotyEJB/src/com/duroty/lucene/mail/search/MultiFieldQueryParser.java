/**
 *
 */
package com.duroty.lucene.mail.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.queryParser.TokenMgrError;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;


/**
 * @author durot
 *
 */
public class MultiFieldQueryParser extends QueryParser {
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

    /** The actual operator that parser uses to combine query terms */
    private static Operator operator = Operator.AND;

    /**
     * @param f
     * @param a
     */
    public MultiFieldQueryParser(String f, Analyzer a) {
        super(f, a);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param stream
     */
    public MultiFieldQueryParser(CharStream stream) {
        super(stream);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param tm
     */
    public MultiFieldQueryParser(QueryParserTokenManager tm) {
        super(tm);

        // TODO Auto-generated constructor stub
    }

    /**
     * <p>
     * Parses a query which searches on the fields specified.
     * <p>
     * If x fields are specified, this effectively constructs:
     * <pre>
     * <code>
     * (field1:query) (field2:query) (field3:query)...(fieldx:query)
     * </code>
     * </pre>
     *
     * @param query Query string to parse
     * @param fields Fields to search on
     * @param analyzer Analyzer to use
     * @throws ParseException if query parsing fails
     * @throws TokenMgrError if query parsing fails
     */
    public static Query parse(String query, String[] fields, Analyzer analyzer)
        throws ParseException {
        BooleanQuery bQuery = new BooleanQuery();

        for (int i = 0; i < fields.length; i++) {
            QueryParser qp = new QueryParser(fields[i], analyzer);
            qp.setDefaultOperator(operator);

            Query q = qp.parse(query);

            //bQuery.add(q, false, false);
            bQuery.add(q, BooleanClause.Occur.SHOULD);
        }

        return bQuery;
    }

    /**
     * <p>
     * Parses a query, searching on the fields specified.
     * Use this if you need to specify certain fields as required,
     * and others as prohibited.
     * <p><pre>
     * Usage:
     * <code>
     * String[] fields = {"filename", "contents", "description"};
     * int[] flags = {MultiFieldQueryParser.NORMAL FIELD,
     *                MultiFieldQueryParser.REQUIRED FIELD,
     *                MultiFieldQueryParser.PROHIBITED FIELD,};
     * parse(query, fields, flags, analyzer);
     * </code>
     * </pre>
     *<p>
     * The code above would construct a query:
     * <pre>
     * <code>
     * (filename:query) +(contents:query) -(description:query)
     * </code>
     * </pre>
     *
     * @param query Query string to parse
     * @param fields Fields to search on
     * @param flags Flags describing the fields
     * @param analyzer Analyzer to use
     * @throws ParseException if query parsing fails
     * @throws TokenMgrError if query parsing fails
     */
    public static Query parse(String query, String[] fields, int[] flags,
        Analyzer analyzer) throws ParseException {
        BooleanQuery bQuery = new BooleanQuery();

        for (int i = 0; i < fields.length; i++) {
            QueryParser qp = new QueryParser(fields[i], analyzer);
            qp.setDefaultOperator(operator);

            Query q = qp.parse(query);
            int flag = flags[i];

            switch (flag) {
            case REQUIRED_FIELD:

                //bQuery.add(q, true, false);
                bQuery.add(q, BooleanClause.Occur.MUST);

                break;

            case PROHIBITED_FIELD:

                //bQuery.add(q, false, true);
                bQuery.add(q, BooleanClause.Occur.MUST_NOT);

                break;

            default:

                //bQuery.add(q, false, false);
                bQuery.add(q, BooleanClause.Occur.SHOULD);

                break;
            }
        }

        return bQuery;
    }

    /**
     * DOCUMENT ME!
     *
     * @param operator DOCUMENT ME!
     */
    public void setOperator(Operator operator) {
        MultiFieldQueryParser.operator = operator;
    }
}
