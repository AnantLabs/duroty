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
package com.duroty.application.bookmark.utils;

import java.io.Serializable;

import java.util.Date;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class BookmarkObj implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -3033412397251757805L;

    /**
    * DOCUMENT ME!
    */
    private String idint;

    /**
     * DOCUMENT ME!
     */
    private long parent;

    /**
     * DOCUMENT ME!
     */
    private String url;

    /**
     * DOCUMENT ME!
     */
    private int depth;

    /**
     * DOCUMENT ME!
     */
    private Date insertDate;

    /**
     * DOCUMENT ME!
     */
    private Date cacheDate;

    /**
     * DOCUMENT ME!
     */
    private String title;

    /**
     * DOCUMENT ME!
     */
    private String titleHighlight;

    /**
     * DOCUMENT ME!
     */
    private String keywords;

    /**
     * DOCUMENT ME!
     */
    private String contents;

    /**
     * DOCUMENT ME!
     */
    private String contentsHighlight;

    /**
     * DOCUMENT ME!
     */
    private boolean flagged;

    /**
     * DOCUMENT ME!
     */
    private boolean notebook;

    /**
     * DOCUMENT ME!
     */
    private String comments;

    /**
     *
     */
    public BookmarkObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getCacheDate() {
        return cacheDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cacheDate DOCUMENT ME!
     */
    public void setCacheDate(Date cacheDate) {
        this.cacheDate = cacheDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comments DOCUMENT ME!
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContents() {
        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contents DOCUMENT ME!
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentsHighlight() {
        return contentsHighlight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contentsHighlight DOCUMENT ME!
     */
    public void setContentsHighlight(String contentsHighlight) {
        this.contentsHighlight = contentsHighlight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDepth() {
        return depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param depth DOCUMENT ME!
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flagged DOCUMENT ME!
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdint() {
        return idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setIdint(String idint) {
        this.idint = idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param insertDate DOCUMENT ME!
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param keywords DOCUMENT ME!
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNotebook() {
        return notebook;
    }

    /**
     * DOCUMENT ME!
     *
     * @param notebook DOCUMENT ME!
     */
    public void setNotebook(boolean notebook) {
        this.notebook = notebook;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getParent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void setParent(long parent) {
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitleHighlight() {
        return titleHighlight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param titleHighlight DOCUMENT ME!
     */
    public void setTitleHighlight(String titleHighlight) {
        this.titleHighlight = titleHighlight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
