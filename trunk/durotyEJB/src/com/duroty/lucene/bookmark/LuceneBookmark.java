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


package com.duroty.lucene.bookmark;

import com.duroty.utils.NumberUtils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class LuceneBookmark implements LuceneBookmarkContants {
    /**
    * DOCUMENT ME!
    */
    private Document doc;

    /**
    * Creates a new LuceneBookmark object.
    */
    public LuceneBookmark() {
        this(null, null);
    }

    /**
     * Creates a new LuceneBookmark object.
     */
    public LuceneBookmark(String idint) {
        this(idint, null);
    }

    /**
     * Creates a new LuceneBookmark object.
     *
     * @param document DOCUMENT ME!
     */
    public LuceneBookmark(Document doc) {
        this(null, doc);
    }

    /**
     * Creates a new LuceneBookmark object.
     *
     * @param document DOCUMENT ME!
     */
    public LuceneBookmark(String idint, Document doc) {
        super();

        if (doc == null) {
            this.doc = new Document();
        } else {
            this.doc = doc;
        }

        setIdint(idint);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdint() {
        String idint = doc.get(Field_idint);

        if (idint == null) {
            idint = "0";
        }

        return idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setIdint(String idint) {
        if (idint != null) {
            doc.removeField(Field_idint);
            doc.add(new Field(Field_idint, idint, Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getParent() {
        String idint = doc.get(Field_parent);

        if (idint == null) {
            idint = "0";
        }

        return idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setParent(String parent) {
        if (parent != null) {
            doc.removeField(Field_parent);
            doc.add(new Field(Field_parent, parent, Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl() {
        return doc.get(Field_url);
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setUrl(String url) {
        if (url != null) {
            doc.removeField(Field_url);
            doc.add(new Field(Field_url, url, Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrlStr() {
        return doc.get(Field_urlstr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setUrlStr(String urlstr) {
        if (urlstr != null) {
            doc.removeField(Field_urlstr);
            doc.add(new Field(Field_urlstr, urlstr, Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the sentDate.
     */
    public int getDepth() {
        if ((doc.get(Field_depth) != null) &&
                !doc.get(Field_depth).trim().equals("")) {
            return Integer.parseInt(doc.get(Field_depth));
        } else {
            return 0;
        }
    }

    /**
     * @param sentDate The lastDate to set.
     */
    public void setDepth(int depth) {
        doc.removeField(Field_depth);
        doc.add(new Field(Field_depth, String.valueOf(depth), Field.Store.YES,
                Field.Index.UN_TOKENIZED));
    }

    /**
     * @return Returns the sentDate.
     */
    public Date getInsertDate() {
        if ((doc.get(Field_insert_date) != null) &&
                !doc.get(Field_insert_date).trim().equals("")) {
            return new Date(Long.parseLong(doc.get(Field_insert_date)));
        } else {
            return null;
        }
    }

    /**
     * @param sentDate The lastDate to set.
     */
    public void setInsertDate(Date insertDate) {
        doc.removeField(Field_insert_date);

        if (insertDate != null) {
            doc.add(new Field(Field_insert_date,
                    NumberUtils.pad(insertDate.getTime()), Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        } else {
            doc.add(new Field(Field_insert_date, "", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * @return Returns the sentDate.
     */
    public Date getCacheDate() {
        if ((doc.get(Field_cache_date) != null) &&
                !doc.get(Field_cache_date).trim().equals("")) {
            return new Date(Long.parseLong(doc.get(Field_cache_date)));
        } else {
            return null;
        }
    }

    /**
     * @param sentDate The lastDate to set.
     */
    public void setCacheDate(Date cacheDate) {
        doc.removeField(Field_cache_date);

        if (cacheDate != null) {
            doc.add(new Field(Field_cache_date,
                    NumberUtils.pad(cacheDate.getTime()), Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        } else {
            doc.add(new Field(Field_cache_date, "", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * @return Returns the subject.
     */
    public String getTitle() {
        return doc.get(Field_title);
    }

    /**
     * @param subject The subject to set.
     */
    public void setTitle(String title) {
        doc.removeField(Field_title);

        if (title != null) {
            doc.add(new Field(Field_title, title, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            doc.add(new Field(Field_title, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the subject.
     */
    public String getKeywords() {
        return doc.get(Field_keywords);
    }

    /**
     * @param subject The subject to set.
     */
    public void setKeywords(String keywords) {
        doc.removeField(Field_keywords);

        if (keywords != null) {
            doc.add(new Field(Field_keywords, keywords, Field.Store.YES,
                    Field.Index.TOKENIZED));

            //doc.add(Field.Text(Field_subject, subject));
        } else {
            doc.add(new Field(Field_keywords, "", Field.Store.YES,
                    Field.Index.TOKENIZED));

            //doc.add(Field.Text(Field_subject, ""));
        }
    }

    /**
     * @return Returns the body.
     */
    public String getCotents() {
        return doc.get(Field_contents);
    }

    /**
     * @param body The body to set.
     */
    public void setContents(String contents) {
        doc.removeField(Field_contents);

        if (contents != null) {
            doc.add(new Field(Field_contents, contents, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            doc.add(new Field(Field_contents, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFlagged() {
        return new Boolean(doc.get(Field_flagged)).booleanValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setFlagged(boolean flagged) {
        doc.removeField(Field_flagged);

        if (flagged) {
            doc.add(new Field(Field_flagged, "true", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        } else {
            doc.add(new Field(Field_flagged, "false", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNotebook() {
        return new Boolean(doc.get(Field_notebook)).booleanValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setNotebook(boolean notebook) {
        doc.removeField(Field_notebook);

        if (notebook) {
            doc.add(new Field(Field_notebook, "true", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        } else {
            doc.add(new Field(Field_notebook, "false", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * @return Returns the body.
     */
    public String getComments() {
        String aux = doc.get(Field_comments);

        if (aux != null) {
            aux = aux.trim();
        }

        return aux;
    }

    /**
     * @param body The body to set.
     */
    public void setComments(String comments) {
        doc.removeField(Field_comments);

        if (comments != null) {
            doc.add(new Field(Field_comments, comments, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            doc.add(new Field(Field_comments, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }
}
