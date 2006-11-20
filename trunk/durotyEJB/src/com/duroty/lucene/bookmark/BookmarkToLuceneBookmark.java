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
/**
 *
 */
package com.duroty.lucene.bookmark;

import com.duroty.application.bookmark.utils.BookmarkObj;

import com.duroty.lucene.parser.ParserFactory;

import com.duroty.utils.http.Extractor;
import com.duroty.utils.http.HttpContent;

import org.apache.commons.lang.StringUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

import java.net.URISyntaxException;
import java.net.URL;

import java.nio.charset.Charset;

import java.util.Date;
import java.util.Vector;

import javax.activation.MimeType;


/**
 * @author durot
 *
 */
public class BookmarkToLuceneBookmark {
    /**
     * DOCUMENT ME!
     */
    private static BookmarkToLuceneBookmark unique = null;

    /**
     * DOCUMENT ME!
     */
    private ParserFactory factory = null;

    /**
     * Creates a new MimeMessageToLuceneMessage object.
     */
    private BookmarkToLuceneBookmark() {
        factory = new ParserFactory();
    }

    /**
     * Creates a new MimeMessageToLuceneMessage object.
     */
    private BookmarkToLuceneBookmark(String propsFileName) {
        factory = new ParserFactory(propsFileName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param propsFileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BookmarkToLuceneBookmark getInstance(String propsFileName) {
        if (unique == null) {
            if (propsFileName != null) {
                unique = new BookmarkToLuceneBookmark(propsFileName);
            } else {
                unique = new BookmarkToLuceneBookmark();
            }
        }

        return unique;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BookmarkToLuceneBookmark getInstance() {
        if (unique == null) {
            unique = new BookmarkToLuceneBookmark();
        }

        return unique;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws URISyntaxException
     * @throws IOException
     */
    public LuceneBookmark parse(String idint, BookmarkObj bookmarkObj)
        throws URISyntaxException, IOException {
        if ((idint == null) || (bookmarkObj == null)) {
            return null;
        }

        LuceneBookmark luceneBookmark = new LuceneBookmark(idint);

        luceneBookmark.setCacheDate(new Date());

        String comments = null;

        try {
            comments = factory.parse(bookmarkObj.getComments(), "text/html",
                    Charset.defaultCharset().displayName());
        } catch (Exception ex) {
            if (ex != null) {
                comments = ex.getMessage();
            }
        }

        luceneBookmark.setComments(comments);

        if (!StringUtils.isBlank(comments)) {
            luceneBookmark.setNotebook(true);
        }

        String url = bookmarkObj.getUrl();
        HttpContent httpContent = new HttpContent(new URL(url));
        MimeType mimeType = httpContent.getContentType();
        String contentType = "text/html";

        if (mimeType != null) {
            contentType = mimeType.getBaseType();
        }

        InputStream inputStream = httpContent.newInputStream();

        if (!StringUtils.isBlank(bookmarkObj.getTitle())) {
            luceneBookmark.setTitle(bookmarkObj.getTitle());
        } else {
            Vector elements = Extractor.getElements(httpContent.newInputStream(),
                    null, "title");

            Text text = null;

            if ((elements != null) && (elements.size() == 1)) {
                Element element = (Element) elements.get(0);
                text = (Text) element.getFirstChild();
            }

            if (text != null) {
                luceneBookmark.setTitle(text.getData());
            } else {
                luceneBookmark.setTitle(url);
            }
        }

        String charset = httpContent.getCharset();

        if (charset == null) {
            charset = Charset.defaultCharset().displayName();
        }

        String contents = null;

        try {
            contents = factory.parse(inputStream, contentType, charset);
        } catch (Exception ex) {
            if (ex != null) {
                contents = ex.getMessage();
            }
        }

        luceneBookmark.setContents(contents);

        luceneBookmark.setDepth(bookmarkObj.getDepth());
        luceneBookmark.setFlagged(bookmarkObj.isFlagged());
        luceneBookmark.setInsertDate(new Date());
        luceneBookmark.setKeywords(bookmarkObj.getKeywords());

        //luceneBookmark.setNotebook(bookmarkObj.isNotebook());
        luceneBookmark.setParent(String.valueOf(bookmarkObj.getParent()));
        luceneBookmark.setUrl(url);
        luceneBookmark.setUrlStr(url);

        return luceneBookmark;
    }
}
