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


package com.duroty.lucene.mail.search;

import com.duroty.application.mail.utils.AdvancedObj;

import com.duroty.lucene.analysis.KeywordAnalyzer;
import com.duroty.lucene.mail.LuceneMessageConstants;

import com.duroty.utils.NumberUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class AdvancedQueryParser extends QueryParser
    implements LuceneMessageConstants {
    /**
     * Creates a new AdvancedMultiQueryParser object.
     *
     * @param f DOCUMENT ME!
     * @param a DOCUMENT ME!
     */
    public AdvancedQueryParser(String f, Analyzer a) {
        super(f, a);

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new AdvancedMultiQueryParser object.
     *
     * @param stream DOCUMENT ME!
     */
    public AdvancedQueryParser(CharStream stream) {
        super(stream);

        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new AdvancedMultiQueryParser object.
     *
     * @param tm DOCUMENT ME!
     */
    public AdvancedQueryParser(QueryParserTokenManager tm) {
        super(tm);

        // TODO Auto-generated constructor stub
    }

    /**
     * String from = request.getParameter("from");
            String to = request.getParameter("to");
            String subject = request.getParameter("subject");
            String label = request.getParameter("label");
            String box = request.getParameter("box");
            String hasWords = request.getParameter("hasWords");
            String hasWordsInBody = request.getParameter("hasWordsInBody");
            String hasWordsInAttachment = request.getParameter("hasWordsInAttachment");
            String doesntHaveWords = request.getParameter("doesntHaveWords");
            String doesntHaveWordsInBody = request.getParameter("doesntHaveWordsInBody");
            String doesntHaveWordsInAttachment = request.getParameter("doesntHaveWordsInAttachment");
            String hasAttachment = request.getParameter("hasAttachment");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
     *
     * @param queryFields DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public static Query parseMessages(AdvancedObj advancedObj, Analyzer analyzer)
        throws ParseException {
        if (advancedObj == null) {
            return null;
        }

        String petador = "AND";

        if (!advancedObj.isOperator()) {
            petador = "OR";
        }

        StringBuffer buffer = new StringBuffer();

        String from = advancedObj.getFrom();

        if ((from != null) && !from.trim().equals("")) {
            QueryParser parser = new QueryParser(Field_from, analyzer);
            parser.setDefaultOperator(Operator.AND);

            Query aux = parser.parse(from);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ")");
        }

        String to = advancedObj.getTo();

        if ((to != null) && !to.trim().equals("")) {
            QueryParser parserTo = new QueryParser(Field_to, analyzer);
            parserTo.setDefaultOperator(Operator.AND);

            Query auxTo = parserTo.parse(to);

            QueryParser parserCc = new QueryParser(Field_cc, analyzer);
            parserCc.setDefaultOperator(Operator.AND);

            Query auxCc = parserCc.parse(to);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + auxTo.toString() + " OR " + auxCc.toString() +
                ") ");
        }

        String subject = advancedObj.getSubject();

        if ((subject != null) && !subject.trim().equals("")) {
            QueryParser parser = new QueryParser(Field_subject, analyzer);
            parser.setDefaultOperator(Operator.AND);

            Query aux = parser.parse(subject);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        }

        String hasWords = advancedObj.getHasWords();

        if ((hasWords != null) && !hasWords.trim().equals("")) {
            Query auxBody = null;
            Query auxAttach = null;

            if (advancedObj.isHasWordsInBody()) {
                QueryParser parserBody = new QueryParser(Field_body, analyzer);
                parserBody.setDefaultOperator(Operator.AND);
                auxBody = parserBody.parse(hasWords);
            }

            if (advancedObj.isHasWordsInAttachment()) {
                QueryParser parserAttach = new QueryParser(Field_attachments,
                        analyzer);
                parserAttach.setDefaultOperator(Operator.AND);
                auxAttach = parserAttach.parse(hasWords);
            }

            if ((auxBody != null) && (auxAttach != null)) {
                if (buffer.length() > 0) {
                    buffer.append(" " + petador + " ");
                }

                buffer.append("((" + auxBody.toString() + ") OR (" +
                    auxAttach.toString() + ")) ");
            } else if ((auxBody != null) && (auxAttach == null)) {
                if (buffer.length() > 0) {
                    buffer.append(" " + petador + " ");
                }

                buffer.append("(" + auxBody.toString() + ") ");
            } else if ((auxBody == null) && (auxAttach != null)) {
                if (buffer.length() > 0) {
                    buffer.append(" " + petador + " ");
                }

                buffer.append("(" + auxAttach.toString() + ") ");
            }
        }

        String doesntHaveWords = advancedObj.getDoesntHaveWords();

        if ((doesntHaveWords != null) && !doesntHaveWords.trim().equals("")) {
            Query auxBody = null;
            Query auxAttach = null;

            if (advancedObj.isDoesntHaveWordsInBody()) {
                QueryParser parserBody = new QueryParser(Field_body, analyzer);
                parserBody.setDefaultOperator(Operator.AND);
                auxBody = parserBody.parse(doesntHaveWords);
            }

            if (advancedObj.isDoesntHaveWordsInAttachment()) {
                QueryParser parserAttach = new QueryParser(Field_attachments,
                        analyzer);
                parserAttach.setDefaultOperator(Operator.AND);
                auxAttach = parserAttach.parse(doesntHaveWords);
            }

            if ((auxBody != null) && (auxAttach != null)) {
                if (buffer.length() > 0) {
                    buffer.append(" " + petador + " ");
                }

                buffer.append("((" + auxBody.toString() + ") OR (" +
                    auxAttach.toString() + ")) ");
            } else if ((auxBody != null) && (auxAttach == null)) {
                if (buffer.length() > 0) {
                    buffer.append(" " + petador + " ");
                }

                buffer.append("(" + auxBody.toString() + ") ");
            } else if ((auxBody == null) && (auxAttach != null)) {
                if (buffer.length() > 0) {
                    buffer.append(" " + petador + " ");
                }

                buffer.append("(" + auxAttach.toString() + ") ");
            }
        }

        String filetype = advancedObj.getContentType();

        if ((filetype != null) && !filetype.trim().equals("")) {
            filetype = filetype.replaceAll("\\s+", " OR ");

            QueryParser parser = new QueryParser(Field_filetype, analyzer);
            parser.setDefaultOperator(Operator.AND);

            Query aux = parser.parse(filetype);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        }

        Date startDate = null;
        Date endDate = null;

        int fixDateStr = 0;

        try {
            fixDateStr = Integer.parseInt(advancedObj.getFixDate());
        } catch (NumberFormatException e1) {
        }

        if (fixDateStr > 0) {
            Calendar cal = new GregorianCalendar();
            int year = cal.get(Calendar.YEAR); // 2002
            int month = cal.get(Calendar.MONTH); // 0=Jan, 1=Feb, ...
            int day = cal.get(Calendar.DAY_OF_MONTH); // 1...

            //int hour = cal.get(Calendar.HOUR_OF_DAY);
            //int minute = cal.get(Calendar.MINUTE);
            //int second = cal.get(Calendar.SECOND);
            Calendar cal1 = null;

            switch (fixDateStr) {
            case 1:

                //AVUI
                cal1 = new GregorianCalendar(year, month, day, 0, 0, 0);
                startDate = new Date(cal1.getTimeInMillis());
                endDate = new Date();

                break;

            case 2:

                //des de ahir
                cal1 = new GregorianCalendar(year, month, day - 1, 0, 0, 0);
                startDate = new Date(cal1.getTimeInMillis());
                endDate = new Date();

                break;

            case 3:

                //la última setmana
                cal1 = new GregorianCalendar(year, month, day - 7, 0, 0, 0);
                startDate = new Date(cal1.getTimeInMillis());
                endDate = new Date();

                break;

            case 4:

                //l'últim mes
                cal1 = new GregorianCalendar(year, month - 1, day, 0, 0, 0);
                startDate = new Date(cal1.getTimeInMillis());
                endDate = new Date();

                break;

            default:
                break;
            }
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String startDateStr = advancedObj.getStartDate();

            if (startDateStr != null) {
                try {
                    startDate = formatter.parse(startDateStr);
                } catch (Exception e) {
                    startDate = null;
                }
            }

            String endDateStr = advancedObj.getEndDate();

            if (endDateStr != null) {
                try {
                    endDate = formatter.parse(endDateStr);
                } catch (Exception e) {
                    endDate = null;
                }
            }
        }

        if ((startDate != null) && (endDate != null)) {
            Term tBeginDate = new Term(Field_lastDate,
                    NumberUtils.pad(startDate.getTime()));
            Term tEndDate = new Term(Field_lastDate,
                    NumberUtils.pad(endDate.getTime()));

            RangeQuery aux = new RangeQuery(tBeginDate, tEndDate, true);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        } else if ((startDate != null) && (endDate == null)) {
            Term tBeginDate = new Term(Field_lastDate,
                    NumberUtils.pad(startDate.getTime()));
            Date now = new Date();
            Term tEndDate = new Term(Field_lastDate,
                    NumberUtils.pad(now.getTime()));

            RangeQuery aux = new RangeQuery(tBeginDate, tEndDate, true);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        } else if ((startDate == null) && (endDate != null)) {
            Term tBeginDate = new Term(Field_lastDate, "0");
            Term tEndDate = new Term(Field_lastDate,
                    NumberUtils.pad(endDate.getTime()));

            RangeQuery aux = new RangeQuery(tBeginDate, tEndDate, true);

            if (buffer.length() > 0) {
                buffer.append(" " + petador + " ");
            }

            buffer.append("(" + aux.toString() + ") ");
        } else {
        }

        boolean hasAttachment = advancedObj.isHasAttachment();

        if (hasAttachment) {
            QueryParser parser = new QueryParser(Field_has_attachments,
                    new KeywordAnalyzer());
            parser.setDefaultOperator(Operator.AND);

            Query aux = parser.parse(String.valueOf(hasAttachment));

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
     * DOCUMENT ME!
     *
     * @param queryFields DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParseException DOCUMENT ME!
     */
    public static Query parseFlags(Hashtable queryFields, Analyzer analyzer)
        throws ParseException {
        if ((queryFields == null) || (queryFields.size() == 0)) {
            return null;
        }

        BooleanQuery bQuery = new BooleanQuery();
        Query simpleQuery = null;
        int control = 0;

        String label = (String) queryFields.get(Field_label);

        if ((label != null) && !label.trim().equals("")) {
            control++;

            QueryParser parser = new QueryParser(Field_label, analyzer);
            parser.setDefaultOperator(Operator.AND);

            Query aux = parser.parse(escape(label));
            simpleQuery = aux;
            bQuery.add(aux, BooleanClause.Occur.MUST);
        }

        if (control > 0) {
            if (control == 1) {
                return simpleQuery;
            } else {
                return bQuery;
            }
        } else {
            return null;
        }
    }
}
