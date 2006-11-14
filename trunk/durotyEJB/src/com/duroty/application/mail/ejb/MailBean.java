package com.duroty.application.mail.ejb;

import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.manager.MailManager;
import com.duroty.application.mail.utils.Counters;
import com.duroty.application.mail.utils.FolderObj;
import com.duroty.application.mail.utils.MessageObj;

import com.duroty.hibernate.Label;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.mail.MailPart;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

import javax.sql.DataSource;


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
 * @ejb.bean name="Mail"
 *           display-name="Name for Mail"
 *           description="Description for Mail"
 *           jndi-name="duroty/ejb/Mail"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="mail"
 *   role-link="mail"
 */
public class MailBean implements SessionBean {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5290887233457135401L;

    /** The session context */
    private SessionContext context;

    /**
     * the manager del BEAN
     */
    private transient MailManager manager = null;

    /**
     * the context jndi
     */
    private transient Context ctx = null;

    /**
     * the hibernate default factory
     */
    private String hibernateSessionFactory = null;

    /**
     * DOCUMENT ME!
     */
    private String dataSourceName;

    /** report spam address */

    //private String addressSpamReport;

    /** report not spam address */

    //private String addressNotSpamReport;

    /**
     * Creates a new MailBean object.
     */
    public MailBean() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EJBException DOCUMENT ME!
     * @throws RemoteException DOCUMENT ME!
     */
    public void ejbRemove() throws EJBException, RemoteException {
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
            this.dataSourceName = (String) mail.get(Constants.DATA_SOURCE);

            //this.addressSpamReport = (String) mail.get(Constants.MAIL_ADDRESS_SPAM_REPORT);
            //this.addressNotSpamReport = (String) mail.get(Constants.MAIL_ADDRESS_NOT_SPAM_REPORT);
            manager = new MailManager(mail);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Get default folders
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public FolderObj[] getFolders() throws MailException {
        try {
            return manager.getFolders();
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e);
        } finally {
        }
    }

    /**
     * Get user labels
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public Vector getLabels() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getLabels(hfactory.openSession(), repositoryName);
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
     * Get user lists contacts
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public Vector getContactList() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getContactList(hfactory.openSession(), repositoryName);
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
     * Posa amb estat d'arxivats un array de missatges
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void archiveMessages(String[] mids) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.archiveMessages(hfactory.openSession(), repositoryName, mids);
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
     * Borra els labels d'un conjunt de missatges
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void deleteLabelsFromMessages(String[] mids)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.deleteLabelsFromMessages(hfactory.openSession(),
                repositoryName, mids);
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
     * Borra els missatges d'un folder, en el cas que els missatges a borrar sigui TRASH o SPAM
     * es borren definitivament
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void deleteMessagesInFolder(String folderName)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.deleteMessagesInFolder(hfactory.openSession(), repositoryName, folderName);
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
     * Borra els missatges d'un label, en el cas que els missatges a borrar sigui TRASH o SPAM
     * es borren definitivament
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void deleteMessagesInLabel(Integer label) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.deleteMessagesInLabel(hfactory.openSession(), repositoryName, label);
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
     * Borra els missatges donats per mids en el cas que els missatges a borrar sigui TRASH o SPAM
     * es borren definitivament
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void deleteMessages(String[] mids, String folderName)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.deleteMessages(hfactory.openSession(), repositoryName,
                mids, folderName);
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
     * Recupera les propietats (capceleres) d'un missatge identificat per mid
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return String
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public String displayProperties(String mid) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.displayProperties(hfactory.openSession(),
                repositoryName, mid);
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
     * Situa els missatges identificats per mids en un flag característic
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void flagMessages(String[] mids, String flag)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.flagMessages(hfactory.openSession(), repositoryName, mids,
                flag);
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
     * Get messages by folder
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return Vector
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public Vector getMessages(String folderName, int page, int messagesByPage,
        int order, String orderType) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getMessages(hfactory.openSession(), repositoryName,
                folderName, page, messagesByPage, order, orderType);
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
     * Get count messages in folder
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return Vector
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public int getCountMessages(String folderName) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getCountMessages(hfactory.openSession(),
                repositoryName, folderName);
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
     * Get messages by label
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return Vector
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public Vector getMessages(int labIdint, int page, int messagesByPage,
        int order, String orderType) throws MailException {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            hsession = hfactory.openSession();

            Label label = (Label) hsession.load(Label.class,
                    new Integer(labIdint));

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getMessages(hfactory.openSession(), repositoryName,
                label, page, messagesByPage, order, orderType);
        } catch (NamingException e) {
            throw new MailException(e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * Get count messages in label
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return Vector
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public int getCountMessages(int labIdint) throws MailException {
        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            hsession = hfactory.openSession();

            Label label = (Label) hsession.load(Label.class,
                    new Integer(labIdint));

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getCountMessages(hfactory.openSession(),
                repositoryName, label);
        } catch (NamingException e) {
            throw new MailException(e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * Get message by id
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return MessageObj
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public MessageObj getMessage(String mid, boolean readReferences,
        boolean isHtml, boolean displayImages) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getMessage(hfactory.openSession(), repositoryName,
                mid, readReferences, isHtml, displayImages);
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
     * Get attachment by mid and hash part
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return MailPart
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public MailPart getAttachment(String mid, String hash)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getAttachment(hfactory.openSession(),
                repositoryName, mid, hash);
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
     * Get counter for info to boxes and labels
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @return Counters
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public Counters getInfoCounters() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getInfoCounters(hfactory.openSession(),
                repositoryName);
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
     * SPAM message by id
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void spamMessage(String[] mids) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            DataSource dataSource = (DataSource) ctx.lookup(dataSourceName);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.spamMessage(dataSource, hfactory.openSession(),
                repositoryName, mids);
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
     * NOT SPAM message by id
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void notSpamMessage(String[] mids) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            DataSource dataSource = (DataSource) ctx.lookup(dataSourceName);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.notSpamMessage(dataSource, hfactory.openSession(),
                repositoryName, mids);
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
     * Move messages to folder
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void moveMessages(String[] mids, String folder)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.moveMessages(hfactory.openSession(), repositoryName, mids,
                folder);
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
     * Apply label to messages
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void applyLabel(Integer label, String[] mids)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.applyLabel(hfactory.openSession(), repositoryName, label,
                mids);
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
