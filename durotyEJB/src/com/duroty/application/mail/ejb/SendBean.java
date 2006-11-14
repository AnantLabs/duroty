package com.duroty.application.mail.ejb;

import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.manager.SendManager;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import org.apache.commons.lang.StringUtils;

import org.hibernate.SessionFactory;

import java.nio.charset.Charset;

import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeUtility;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


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
 * @ejb.bean name="Send"
 *           display-name="Name for Send"
 *           description="Description for Send"
 *           jndi-name="duroty/ejb/Send"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="mail"
 *   role-link="mail"
 */
public class SendBean implements SessionBean {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = -762099876246185833L;

    /** The session context */
    private SessionContext context;

    /**
    * the manager del BEAN
    */
    private transient SendManager manager = null;

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
     * Creates a new SendBean object.
     */
    public SendBean() {
        super();
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
     *         role-name = "mail"
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

            manager = new SendManager(mail);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Send message
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void send(String mid, int identity, String to, String cc,
        String bcc, String subject, String body, Vector attachments,
        boolean isHtml, String charset, String priority)
        throws MailException {
        SessionFactory hfactory = null;
        Session session = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            session = (Session) ctx.lookup(smtpSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            if (charset == null) {
                charset = MimeUtility.javaCharset(Charset.defaultCharset()
                                                         .displayName());
            }

            InternetHeaders headers = null;

            if (!StringUtils.isBlank(mid)) {
                headers = manager.getHeaders(hfactory.openSession(),
                        repositoryName, mid);
            }

            manager.send(hfactory.openSession(), session, repositoryName,
                identity, to, cc, bcc, subject, body, attachments, isHtml,
                charset, headers, priority);
        } catch (NamingException e) {
            throw new MailException(e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e);
        } finally {
        }
    }

    /**
     * Save draft message
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void saveDraft(String mid, int identity, String to, String cc,
        String bcc, String subject, String body, Vector attachments,
        boolean isHtml, String charset, String priority)
        throws MailException {
        SessionFactory hfactory = null;
        Session session = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            session = (Session) ctx.lookup(smtpSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            if (charset == null) {
                charset = MimeUtility.javaCharset(Charset.defaultCharset()
                                                         .displayName());
            }

            InternetHeaders headers = null;

            if (!StringUtils.isBlank(mid)) {
                headers = manager.getHeaders(hfactory.openSession(),
                        repositoryName, mid);
            }

            manager.saveDraft(hfactory.openSession(), session, repositoryName,
                identity, to, cc, bcc, subject, body, attachments, isHtml,
                charset, headers, priority);
        } catch (NamingException e) {
            throw new MailException(e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e);
        } finally {
        }
    }

    /**
     * Send identity confirmation
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void sendIdentity(String from, String to, String subject, String body)
        throws MailException {
        Session session = null;

        try {
            session = (Session) ctx.lookup(smtpSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.sendIdentity(session, repositoryName, from, to, subject,
                body);
        } catch (NamingException e) {
            throw new MailException(e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e);
        } finally {
        }
    }
}
