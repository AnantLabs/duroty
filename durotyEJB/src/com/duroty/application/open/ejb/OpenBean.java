package com.duroty.application.open.ejb;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.hibernate.SessionFactory;

import com.duroty.application.admin.utils.UserObj;
import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.open.manager.OpenManager;
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
 * @ejb.bean name="Open"
 *           display-name="Name for Open"
 *           description="Description for Open"
 *           jndi-name="duroty/ejb/Open"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="guest"
 *   role-link="guest"
 */
public class OpenBean implements SessionBean {
    /**
         *
         */
    private static final long serialVersionUID = -1097291280732935100L;

    /** The session context */
    private SessionContext context;

    /**
     * the context jndi
     */
    private transient Context ctx = null;

    /**
     * SMTP Factory
     */
    private String smtpSessionFactory = null;

    /**
     * the hibernate default factory
     */
    private String hibernateSessionFactory = null;

    /**
     * DOCUMENT ME!
     */
    private transient OpenManager manager = null;

    /**
     * DOCUMENT ME!
     */
    private String defaultEmailDomain = null;

    /**
     * Creates a new OpenBean object.
     */
    public OpenBean() {
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
     *         role-name = "guest"
     *
     */
    public void ejbCreate() throws CreateException {
        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap mail = (HashMap) ctx.lookup((String) options.get(
                        Constants.MAIL_CONFIG));

            this.hibernateSessionFactory = (String) mail.get(Constants.HIBERNATE_SESSION_FACTORY);
            this.smtpSessionFactory = (String) mail.get(Constants.DUROTY_MAIL_FACTOTY);
            this.defaultEmailDomain = (String) mail.get(Constants.DEFAULT_EMAIL_DOMAIN);

            manager = new OpenManager();
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Forgot password
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "guest"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void forgotPassword(String data) throws EJBException {
        SessionFactory hfactory = null;
        Session msession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            msession = (Session) ctx.lookup(smtpSessionFactory);

            manager.forgotPassword(hfactory.openSession(), msession, data);
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
        }
    }

    /**
     * Validate username
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "guest"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public boolean existUser(String username) throws EJBException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            return manager.existUser(hfactory.openSession(), username);
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
        }
    }

    /**
     * Register new user
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "guest"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void register(UserObj userObj) throws EJBException {
        SessionFactory hfactory = null;
        Session msession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            msession = (Session) ctx.lookup(smtpSessionFactory);

            manager.register(hfactory.openSession(), msession, userObj,
                defaultEmailDomain);
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
        }
    }
}
