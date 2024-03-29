/*
 * Generated by XDoclet - Do not edit!
 */
package com.duroty.application.bookmark.interfaces;


/**
 * Remote interface for Bookmark.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface Bookmark extends javax.ejb.EJBObject {
    /**
     * Add a bookmark object
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public void addBookmark(
        com.duroty.application.bookmark.utils.BookmarkObj bookmarkObj)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * Update a bookmark object
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public void updateBookmark(
        com.duroty.application.bookmark.utils.BookmarkObj bookmarkObj)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * Get bookmark object by idint
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public com.duroty.application.bookmark.utils.BookmarkObj getBookmark(
        java.lang.String idint)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * Add bookmarks from from links
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public void addBookmarks(java.util.Vector links)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * delete bookmark by idint
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public void deleteBookmark(java.lang.String idint)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * flag bookmark by idint
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public void flagBookmark(java.lang.String idint)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * unflag bookmark by idint
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public void unflagBookmark(java.lang.String idint)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * Search bookmarks by token
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public com.duroty.application.bookmark.utils.SearchObj search(
        java.lang.String token, int page, int messagesByPage, int order,
        java.lang.String orderType, boolean isNotebook)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * DidYouMean by token
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public java.lang.String didYouMean(java.lang.String token)
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * Label keywords
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public java.util.Vector getKeywords()
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;

    /**
     * Label keywords fopr notebook
     * @throws BookmarkException Thrown if method fails due to system-level error.    */
    public java.util.Vector getKeywordsNotebook()
        throws com.duroty.application.bookmark.exceptions.BookmarkException, 
            java.rmi.RemoteException;
}
