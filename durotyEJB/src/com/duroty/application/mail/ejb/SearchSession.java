/*
 * Generated by XDoclet - Do not edit!
 */
package com.duroty.application.mail.ejb;


/**
 * Session layer for Search.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public class SearchSession extends com.duroty.application.mail.ejb.SearchBean
    implements javax.ejb.SessionBean {
    /**
          *
          */
    private static final long serialVersionUID = -2298865428538034831L;

    /**
     * DOCUMENT ME!
     *
     * @throws javax.ejb.EJBException DOCUMENT ME!
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    public void ejbActivate()
        throws javax.ejb.EJBException, java.rmi.RemoteException {
        super.ejbActivate();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws javax.ejb.EJBException DOCUMENT ME!
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    public void ejbPassivate()
        throws javax.ejb.EJBException, java.rmi.RemoteException {
        super.ejbPassivate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ctx DOCUMENT ME!
     *
     * @throws javax.ejb.EJBException DOCUMENT ME!
     */
    public void setSessionContext(javax.ejb.SessionContext ctx)
        throws javax.ejb.EJBException {
        super.setSessionContext(ctx);
    }

    /**
     * DOCUMENT ME!
     */
    public void unsetSessionContext() {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws javax.ejb.EJBException DOCUMENT ME!
     * @throws java.rmi.RemoteException DOCUMENT ME!
     */
    public void ejbRemove()
        throws javax.ejb.EJBException, java.rmi.RemoteException {
        super.ejbRemove();
    }
}
