/*
 * Generated by XDoclet - Do not edit!
 */
package com.duroty.application.home.interfaces;


/**
 * Remote interface for Home.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface Home extends javax.ejb.EJBObject {
    /**
     * Save feed
     * @throws HomeException Thrown if method fails due to system-level error.    */
    public void saveFeed(int channel, java.lang.String name,
        java.lang.String value)
        throws com.duroty.application.home.exceptions.HomeException, 
            java.rmi.RemoteException;

    /**
     * Get feed
     * @throws HomeException Thrown if method fails due to system-level error.    */
    public com.duroty.application.home.utils.FeedObj getFeed(int channel,
        java.lang.String name)
        throws com.duroty.application.home.exceptions.HomeException, 
            java.rmi.RemoteException;

    /**
     * Get cookies database
     * @throws HomeException Thrown if method fails due to system-level error.    */
    public java.util.Vector getAllFeed(int channel)
        throws com.duroty.application.home.exceptions.HomeException, 
            java.rmi.RemoteException;
}
