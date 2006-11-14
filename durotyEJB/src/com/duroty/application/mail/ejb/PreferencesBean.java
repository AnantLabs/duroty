package com.duroty.application.mail.ejb;

import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.manager.PreferencesManager;
import com.duroty.application.mail.utils.ContactObj;
import com.duroty.application.mail.utils.FilterObj;
import com.duroty.application.mail.utils.IdentityObj;
import com.duroty.application.mail.utils.LabelObj;
import com.duroty.application.mail.utils.PreferencesObj;
import com.duroty.application.mail.utils.SearchContactsObj;
import com.duroty.application.mail.utils.SearchGroupsObj;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

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
 * @ejb.bean name="Preferences"
 *           display-name="Name for Preferences"
 *           description="Description for Preferences"
 *           jndi-name="duroty/ejb/Preferences"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="mail"
 *   role-link="mail"
 */
public class PreferencesBean implements SessionBean {
    /**
     *
     */
    private static final long serialVersionUID = -4374304672624985709L;

    /** The session context */
    private SessionContext context;

    /**
    * the manager del BEAN
    */
    private transient PreferencesManager manager = null;

    /**
     * the context jndi
     */
    private transient Context ctx = null;

    /**
     * the hibernate default factory
     */
    private String hibernateSessionFactory = null;

    /**
     * Creates a new PreferencesBean object.
     */
    public PreferencesBean() {
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

            manager = new PreferencesManager(mail);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Get user mail preferences
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public PreferencesObj getPreferences() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.getPreferences(hfactory.openSession(), username);
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
     * Update user mail preferences
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void setPreferences(PreferencesObj preferencesObj)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.setPreferences(hfactory.openSession(), username,
                preferencesObj);
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
     * Get user mail identities
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public Vector getIdentities() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.getIdentities(hfactory.openSession(), username);
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
     * Suggest contacts for token
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public Vector suggestContacts(String token) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.suggestContacts(hfactory.openSession(), username,
                token);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Search contacts for token
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public SearchContactsObj searchContacts(String token, int page, int byPage,
        int order, String extra) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.searchContacts(hfactory.openSession(), username,
                token, page, byPage, order, extra);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Search groups for token
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public SearchGroupsObj searchGroups(String token, int page, int byPage,
        int order, String extra) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.searchGroups(hfactory.openSession(), username,
                token, page, byPage, order, extra);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Get all contacts
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public SearchContactsObj getContacts(int page, int byPage, int order,
        String extra) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.getContacts(hfactory.openSession(), username, page,
                byPage, order, extra);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Get all groups
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public SearchGroupsObj getGroups(int page, int byPage, int order,
        String extra) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            return manager.getGroups(hfactory.openSession(), username, page,
                byPage, order, extra);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Delete contacts by idint
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void deleteContacts(Integer[] idints) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.deleteContacts(hfactory.openSession(), username, idints);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Delete group by idint
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void deleteGroups(Integer[] idints) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.deleteGroups(hfactory.openSession(), username, idints);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Update contact
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void updateContact(ContactObj contactObj) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.updateContact(hfactory.openSession(), username, contactObj);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Create label and assign messages filter to it
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void createLabel(LabelObj label) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.createLabel(hfactory.openSession(), username, label);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Update label and assign messages filter to it
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void updateLabel(LabelObj label) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.updateLabel(hfactory.openSession(), username, label);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Delete label
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void deleteLabel(Integer idint) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.deleteLabel(hfactory.openSession(), username, idint);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Create filter for label and assign messages filter to it
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void createFilter(FilterObj filter) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.createFilter(hfactory.openSession(), username, filter);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Create filter for label and assign messages filter to it
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void updateFilter(FilterObj filter) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.updateFilter(hfactory.openSession(), username, filter);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
        } finally {
        }
    }

    /**
     * Delete filter
     *
     * @ejb.interface-method view-type = "remote"
     *
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws EJBException Thrown if method fails due to system-level error.
     */
    public void deleteFilter(Integer idint) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String username = context.getCallerPrincipal().getName();

            manager.deleteFilter(hfactory.openSession(), username, idint);
        } catch (NamingException e) {
            throw new MailException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof MailException) {
                throw (MailException) e;
            }

            throw new MailException(e.getMessage(), e);
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
     * Get user filters
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public Vector getFilters() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getFilters(hfactory.openSession(), repositoryName);
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
     * Get user filter by idint
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public FilterObj getFilter(Integer idint) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getFilter(hfactory.openSession(), repositoryName,
                idint);
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
     * Add Contact
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void addContact(String action, ContactObj contactObj)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.addContact(hfactory.openSession(), repositoryName, action,
                contactObj);
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
     * Add Group
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void addGroup(String action, String name, String emails)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.addGroup(hfactory.openSession(), repositoryName, action,
                name, emails);
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
     * Create identity
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public String createIdentity(IdentityObj identityObj)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.createIdentity(hfactory.openSession(),
                repositoryName, identityObj);
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
     * Set identity to default
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void setIdentityDefault(int idint) throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.setIdentityDefault(hfactory.openSession(), repositoryName,
                idint);
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
     * Get default identity
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public IdentityObj getIdentityDefault() throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.getIdentityDefault(hfactory.openSession(),
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
     * Validate identity
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void validateIdentity(String repositoryName, String code)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            manager.validateIdentity(hfactory.openSession(), repositoryName,
                code);
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
     * Delete identity
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "mail"
     *
     * @throws MailException Thrown if method fails due to system-level error.
     */
    public void deleteIdentity(int idint)
        throws MailException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            
            String repositoryName = context.getCallerPrincipal().getName();

            manager.deleteIdentity(hfactory.openSession(), repositoryName, idint);
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
