package com.duroty.session;

import com.duroty.utils.log.DLog;

import java.rmi.RemoteException;

import java.util.Enumeration;

import javax.ejb.EJBObject;
import javax.ejb.RemoveException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class SessionManager {
    /**
     * Creates a new SessionManager object.
     */
    private SessionManager() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isSessionOpened(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static HttpSession openSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            session = request.getSession(true);
        }

        return session;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object getObject(String name, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            return (Object) session.getAttribute(name);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public static void setObject(String name, Object value,
        HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute(name, value);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public static void removeObject(String name, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     */
    public static void invalidate(HttpServletRequest request,
        HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Enumeration en = session.getAttributeNames();

            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();

                Object obj = session.getAttribute(name);

                if (obj instanceof EJBObject) {
                    EJBObject new_name = (EJBObject) obj;

                    try {
                        new_name.remove();
                    } catch (RemoteException e) {
                        DLog.log(DLog.DEBUG, new_name.getClass(), e.getMessage());
                    } catch (RemoveException e) {
                        DLog.log(DLog.DEBUG, new_name.getClass(), e.getMessage());
                    }
                }

                session.removeAttribute(name);
            }

            session.invalidate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     */
    public static void invalidate(HttpSession session) {
        if (session != null) {
            Enumeration en = session.getAttributeNames();

            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();

                Object obj = session.getAttribute(name);

                if (obj instanceof EJBObject) {
                    EJBObject new_name = (EJBObject) obj;

                    try {
                        new_name.remove();
                    } catch (RemoteException e) {
                        DLog.log(DLog.INFO, new_name.getClass(), e.getMessage());
                    } catch (RemoveException e) {
                        DLog.log(DLog.INFO, new_name.getClass(), e.getMessage());
                    }
                }

                session.removeAttribute(name);
            }

            session.invalidate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            return session.getId();
        } else {
            return null;
        }
    }
}
