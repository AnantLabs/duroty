package com.duroty.application.chat.ejb;

import com.duroty.application.chat.exceptions.ChatException;
import com.duroty.application.chat.manager.ChatManager;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;

import org.hibernate.SessionFactory;

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
 * @ejb.bean name="Chat"
 *           display-name="Name for Chat"
 *           description="Description for Chat"
 *           jndi-name="duroty/ejb/Chat"
 *           type="Stateless"
 *           view-type="remote"
 *
 * @ejb.security-role-ref
 *   role-name="chat"
 *   role-link="chat"
 */
public class ChatBean implements SessionBean {
    /**
         *
         */
    private static final long serialVersionUID = -6330998030356143617L;

    /** The session context */
    private SessionContext context;

    /**
     * DOCUMENT ME!
     */
    private transient ChatManager manager;

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
     * Creates a new ChatBean object.
     */
    public ChatBean() {
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
    *         role-name = "chat"
    *
    */
    public void ejbCreate() throws CreateException {
        Map options = ApplicationConstants.options;

        try {
            ctx = new InitialContext();

            HashMap chat = (HashMap) ctx.lookup((String) options.get(
                        Constants.CHAT_CONFIG));

            this.hibernateSessionFactory = (String) chat.get(Constants.HIBERNATE_SESSION_FACTORY);
            this.smtpSessionFactory = (String) chat.get(Constants.DUROTY_MAIL_FACTOTY);

            manager = new ChatManager(chat);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     *  //////////// sending a message ////////////
      // message parts (within array $_POST):  //
      // recipient-  user receiving the message//
      ///////////////////////////////////////////
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "chat"
     *
     * @throws ChatException Thrown if method fails due to system-level error.
     */
    public void send(String recipient, String message, int flag)
        throws ChatException {
        SessionFactory hfactory = null;
        Session msession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            msession = (Session) ctx.lookup(smtpSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.send(hfactory.openSession(), msession, repositoryName,
                recipient, message, flag);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }

    /**
     ///////////// ping the server /////////////
      // note: since the server cannot contact //
      //       the client, the client must     //
      //       ping the server for new msgs    //
      //                                       //
      // ping parts (within array $_POST):     //
      ///////////////////////////////////////////
     *
     * @ejb.interface-method view-type = "remote"
     * @ejb.permission
     *         role-name = "chat"
     *
     * @throws ChatException Thrown if method fails due to system-level error.
     */
    public String ping(int away) throws ChatException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            return manager.ping(hfactory.openSession(), repositoryName, away);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }

    /**
    ///////////// login to the server /////////
      // login parts (within array $_POST):    //
      // from     -  user logging in           //
      // pwd      -  password                  //
      ///////////////////////////////////////////
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "chat"
    *
    * @throws ChatException Thrown if method fails due to system-level error.
    */
    public void login() throws ChatException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.login(hfactory.openSession(), repositoryName);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }

    /**
    ///////////// login to the server /////////
      // login parts (within array $_POST):    //
      // from     -  user logging in           //
      // pwd      -  password                  //
      ///////////////////////////////////////////
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "chat"
    *
    * @throws ChatException Thrown if method fails due to system-level error.
    */
    public void signin() throws ChatException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.signin(hfactory.openSession(), repositoryName);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }

    /**
    ///////////// login to the server /////////
      // login parts (within array $_POST):    //
      // from     -  user logging in           //
      // pwd      -  password                  //
      ///////////////////////////////////////////
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "chat"
    *
    * @throws ChatException Thrown if method fails due to system-level error.
    */
    public void logout() throws ChatException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.logout(hfactory.openSession(), repositoryName);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }

    /**
    ///////////// login to the server /////////
      // login parts (within array $_POST):    //
      // from     -  user logging in           //
      // pwd      -  password                  //
      ///////////////////////////////////////////
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "chat"
    *
    * @throws ChatException Thrown if method fails due to system-level error.
    */
    public void signout() throws ChatException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.signout(hfactory.openSession(), repositoryName);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }

    /**
    ///////////// login to the server /////////
      // login parts (within array $_POST):    //
      // from     -  user logging in           //
      // pwd      -  password                  //
      ///////////////////////////////////////////
    *
    * @ejb.interface-method view-type = "remote"
    * @ejb.permission
    *         role-name = "chat"
    *
    * @throws ChatException Thrown if method fails due to system-level error.
    */
    public void setState(int state, String message) throws ChatException {
        SessionFactory hfactory = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);

            String repositoryName = context.getCallerPrincipal().getName();

            manager.setState(hfactory.openSession(), repositoryName, state,
                message);
        } catch (Exception e) {
            if (e instanceof ChatException) {
                throw (ChatException) e;
            }

            throw new ChatException(e);
        } finally {
        }
    }
}
