/**
 *
 */
package com.duroty.task;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jboss.varia.scheduler.Schedulable;

import com.duroty.hibernate.Message;
import com.duroty.hibernate.Users;
import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.lucene.utils.FileUtilities;
import com.duroty.service.Messageable;
import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;


/**
 * @author durot
 *
 */
public class DeleteMessagesTask implements Schedulable {
	private static final String folderDelete = "DELETE";
	
    /**
    * DOCUMENT ME!
    */
    private Context ctx = null;

    /**
     * DOCUMENT ME!
     */
    private boolean init = false;

    /**
     * DOCUMENT ME!
     */
    private Messageable messageable;

    /**
     * DOCUMENT ME!
     */
    private String hibernateSessionFactory;

    /**
     * @throws NamingException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     *
     */
    public DeleteMessagesTask()
        throws NamingException, ClassNotFoundException, InstantiationException, 
            IllegalAccessException, IOException {
        super();

        Map options = ApplicationConstants.options;

        ctx = new InitialContext();

        HashMap mail = (HashMap) ctx.lookup((String) options.get(
                    Constants.MAIL_CONFIG));

        String messageFactory = (String) mail.get(Constants.MESSAGES_FACTORY);

        if ((messageFactory != null) && !messageFactory.trim().equals("")) {
            Class clazz = null;
            clazz = Class.forName(messageFactory.trim());
            this.messageable = (Messageable) clazz.newInstance();
            this.messageable.setProperties(mail);
        }

        this.hibernateSessionFactory = (String) mail.get(Constants.HIBERNATE_SESSION_FACTORY);

        String tempDir = System.getProperty("java.io.tmpdir");

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        FileUtilities.deleteMotLocks(new File(tempDir));
        FileUtilities.deleteLuceneLocks(new File(tempDir));
    }

    /* (non-Javadoc)
     * @see org.jboss.varia.scheduler.Schedulable#perform(java.util.Date, long)
     */
    public void perform(Date arg0, long arg1) {
        if (isInit()) {
            DLog.log(DLog.DEBUG, this.getClass(),
                "DeleteMessagesTask is running and wait.");

            return;
        }

        flush();
    }

    /**
     * DOCUMENT ME!
     */
    private void flush() {
        setInit(true);

        SessionFactory hfactory = null;
        Session hsession = null;

        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();

            Criteria crit = hsession.createCriteria(Message.class);
            crit.add(Restrictions.eq("mesBox", folderDelete));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
            	Message message = (Message) scroll.get(0);
            	
            	Users user = message.getUsers();
				String mid = message.getMesName();

				hsession.delete(message);
				hsession.flush();
				
				this.messageable.deleteMimeMessage(mid, user);

				/*try {
					
				} catch (HibernateException e) {
				} catch (OutOfMemoryError e) {
					System.gc();
				} catch (Exception e) {
				} catch (Throwable e) {
				}*/
				
				Thread.sleep(100);
            }
        } catch (Exception e) {
            System.gc();
            DLog.log(DLog.WARN, this.getClass(), e.getMessage());
        } catch (OutOfMemoryError e) {
            System.gc();
            DLog.log(DLog.WARN, this.getClass(), e.getMessage());
        } catch (Throwable e) {
            System.gc();
            DLog.log(DLog.WARN, this.getClass(), e.getMessage());
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            setInit(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isInit() {
        synchronized (DeleteMessagesTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (DeleteMessagesTask.class) {
            this.init = init;
        }
    }
}
