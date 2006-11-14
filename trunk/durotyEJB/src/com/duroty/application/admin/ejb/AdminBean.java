package com.duroty.application.admin.ejb;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.SessionFactory;

import com.duroty.application.admin.exceptions.AdminException;
import com.duroty.application.admin.manager.AdminManager;
import com.duroty.application.admin.utils.SearchUsersObj;
import com.duroty.application.admin.utils.UserObj;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;


/**
 * XDoclet-based session bean.  The class must be declared
 * public according to the EJB specification.
 *
 * To generate the EJB related files to this EJB:
 *                - Add Standard EJB module to XDoclet project properties
 *                - Customize XDoclet configuration for your appserver
 *                - Run XDoclet
 *
 * Below are the xdoclet-related tags needed for this EJB.
 *
 * @ejb.bean name="Admin"
 *           display-name="Name for Admin"
 *           description="Description for Admin"
 *           jndi-name="duroty/ejb/Admin"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="admin"
 *   role-link="admin"
 */
public class AdminBean implements SessionBean {
    /**
         *
         */
    private static final long serialVersionUID = 560800489913732930L;

    /** The session context */
    private SessionContext context;

    /**
     * DOCUMENT ME!
     */
    private transient AdminManager manager;

    /**
     * DOCUMENT ME!
     */
    private transient Context ctx;

    /**
     * the hibernate default factory
     */
    private String hibernateSessionFactory = null;

    /**
     * Creates a new AdminBean object.
     */
    public AdminBean() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * Set the associated session context. The container calls this method
     * after the instance creation.
     *
     * The enterprise bean instance should store the reference to the context
     * object in an instance variable.
     *
     * This method is called with no transaction context.
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void setSessionContext(SessionContext newContext)
        throws EJBException {
        context = newContext;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbRemove() throws EJBException, RemoteException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbActivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub
    }

    /**
     * An ejbCreate method as required by the EJB specification.
     *
     * The container calls the instance?s <code>ejbCreate</code> method whose
     * signature matches the signature of the <code>create</code> method invoked
     * by the client. The input parameters sent from the client are passed to
     * the <code>ejbCreate</code> method. Each session bean class must have at
     * least one <code>ejbCreate</code> method. The number and signatures
     * of a session bean?s <code>create</code> methods are specific to each
     * session bean class.
     *
     * @throws CreateException Thrown if method fails due to system-level error.
     *
     * @ejb.create-method
    * @ejb.permission
    *         role-name = "admin"
     *
     */
    public void ejbCreate() throws CreateException {
        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap mail = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));

            this.hibernateSessionFactory = (String) mail.get(Constants.HIBERNATE_SESSION_FACTORY);

            manager = new AdminManager();
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
    * Add User
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "admin"
    *
    * @throws AdminException Thrown if method fails due to system-level error.
    */
    public void addUser(UserObj obj) throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            manager.addUser(hfactory.openSession(), obj);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * Update User
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public void updateUser(UserObj obj) throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            manager.updateUser(hfactory.openSession(), obj);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * Delete User
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public void deleteUsers(Integer[] idints) throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            manager.deleteUsers(hfactory.openSession(), idints);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * Get User
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public UserObj getUser(Integer idint) throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.getUser(hfactory.openSession(), idint);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * Get All Users
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public SearchUsersObj allUsers(int page, int byPage)
        throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.allUsers(hfactory.openSession(), page, byPage);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * Suggest Users
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public SearchUsersObj suggestUsers(String token, int page, int byPage)
        throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.suggestUsers(hfactory.openSession(), token, page,
                byPage);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * Get Roles
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public Vector roles() throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.roles(hfactory.openSession());
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * checkUser
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public boolean checkUser(String username) throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.checkUser(hfactory.openSession(), username);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }

    /**
     * confirmPassword
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "admin"
     *
     * @throws AdminException Thrown if method fails due to system-level error.
     */
    public boolean confirmPassword(String password, String confirmPassword)
        throws AdminException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.confirmPassword(hfactory.openSession(), password,
                confirmPassword);
        } catch (NamingException e) {
            throw new AdminException(e);
        } catch (Exception e) {
            if (e instanceof AdminException) {
                throw (AdminException) e;
            }

            throw new AdminException(e);
        } finally {
        }
    }
}
