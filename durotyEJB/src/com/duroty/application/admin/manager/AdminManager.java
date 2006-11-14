package com.duroty.application.admin.manager;

import com.duroty.application.admin.exceptions.AdminException;
import com.duroty.application.admin.exceptions.PasswordNullException;
import com.duroty.application.admin.exceptions.UserExistException;
import com.duroty.application.admin.utils.RoleObj;
import com.duroty.application.admin.utils.SearchUsersObj;
import com.duroty.application.admin.utils.UserObj;
import com.duroty.application.mail.exceptions.MailException;

import com.duroty.hibernate.Identity;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Roles;
import com.duroty.hibernate.UserRole;
import com.duroty.hibernate.UserRoleId;
import com.duroty.hibernate.Users;

import com.duroty.utils.GeneralOperations;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class AdminManager {
    /**
     * Creates a new AdminManager object.
     */
    public AdminManager() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param userObj DOCUMENT ME!
     *
     * @throws AdminException DOCUMENT ME!
     */
    public void addUser(Session hsession, UserObj userObj)
        throws AdminException {
        try {
            Users users = new Users();
            users.setUseActive(userObj.isActive());
            users.setUseEmail(userObj.getEmail());
            users.setUseLanguage(userObj.getLanguage());
            users.setUseName(userObj.getName());
            users.setUsePassword(userObj.getPassword());
            users.setUseRegisterDate(userObj.getRegisterDate());
            users.setUseUsername(userObj.getUsername());

            if (userObj.getRoles() != null && userObj.getRoles().length > 0) {
	            Criteria crit = hsession.createCriteria(Roles.class);
	            crit.add(Restrictions.in("rolIdint", userObj.getRoles()));
	
	            ScrollableResults scroll = crit.scroll();
	
	            while (scroll.next()) {
	                UserRole userRole = new UserRole(new UserRoleId(users,
	                            (Roles) scroll.get(0)));
	                users.addUserRole(userRole);
	            }
            }

            hsession.save(users);
            hsession.flush();

            MailPreferences mailPreferences = new MailPreferences();
            mailPreferences.setUsers(users);
            mailPreferences.setMaprHtmlMessage(userObj.isHtmlMessages());
            mailPreferences.setMaprMessagesByPage(userObj.getMessagesByPage());
            mailPreferences.setMaprQuotaSize(userObj.getQuotaSize());
            if (userObj.getSignature() != null) {
            	mailPreferences.setMaprSignature("\n" + userObj.getSignature().replaceAll("'", "\\\\'"));
            }

            if (userObj.isSpamTolerance()) {
                mailPreferences.setMaprSpamTolerance(100);
            } else {
                mailPreferences.setMaprSpamTolerance(-1);
            }

            mailPreferences.setMaprVacationActive(userObj.isVactionActive());
            mailPreferences.setMaprVacationBody(userObj.getVacationBody());
            mailPreferences.setMaprVacationSubject(userObj.getVacationSubject());

            hsession.save(mailPreferences);
            hsession.flush();

            Identity identity = new Identity();
            identity.setIdeActive(true);
            identity.setIdeCode(RandomStringUtils.randomAlphanumeric(25));
            identity.setIdeDefault(true);
            identity.setIdeEmail(userObj.getEmailIdentity());
            identity.setIdeName(userObj.getName());
            identity.setIdeReplyTo(userObj.getEmailIdentity());
            identity.setUsers(users);

            hsession.save(identity);
            hsession.flush();
        } catch (Exception ex) {
            throw new AdminException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public UserObj getUser(Session hsession, Integer idint)
        throws Exception {
        try {
            Criteria crit = hsession.createCriteria(Users.class);
            crit.add(Restrictions.eq("useIdint", idint));

            Users user = (Users) crit.uniqueResult();

            UserObj obj = new UserObj();

            obj.setActive(user.isUseActive());
            obj.setEmail(user.getUseEmail());

            Iterator it = user.getMailPreferenceses().iterator();
            MailPreferences mailPreferences = (MailPreferences) it.next();

            obj.setHtmlMessages(mailPreferences.isMaprHtmlMessage());
            obj.setIdint(user.getUseIdint());
            obj.setLanguage(user.getUseLanguage());
            obj.setMessagesByPage(mailPreferences.getMaprMessagesByPage());
            obj.setName(user.getUseName());
            obj.setPassword(user.getUsePassword());
            obj.setQuotaSize(mailPreferences.getMaprQuotaSize());
            obj.setRegisterDate(user.getUseRegisterDate());

            Integer[] roles = new Integer[user.getUserRoles().size()];

            if (roles.length > 0) {
                it = user.getUserRoles().iterator();

                int i = 0;

                while (it.hasNext()) {
                    UserRole userRole = (UserRole) it.next();
                    roles[i] = new Integer(userRole.getId().getRoles()
                                                   .getRolIdint());
                    i++;
                }
            }

            obj.setRoles(roles);

            obj.setSignature(mailPreferences.getMaprSignature());

            if (mailPreferences.getMaprSpamTolerance() == 100) {
                obj.setSpamTolerance(true);
            } else {
                obj.setSpamTolerance(false);
            }

            obj.setUsername(user.getUseUsername());
            obj.setVacationBody(mailPreferences.getMaprVacationBody());
            obj.setVacationSubject(mailPreferences.getMaprVacationSubject());
            obj.setVactionActive(mailPreferences.isMaprVacationActive());
            
            int usedQuotaSize = getUsedQuotaSize(hsession, user);            
            usedQuotaSize /= 1024;
            if (usedQuotaSize > 1024) {
                usedQuotaSize /= 1024;
                obj.setUsedQuota(usedQuotaSize + " MB");
            } else {
            	obj.setUsedQuota(((usedQuotaSize > 0) ? (usedQuotaSize + "") : "<1") + " kB");
            }

            return obj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public SearchUsersObj allUsers(Session hsession, int page, int byPage)
        throws Exception {
        SearchUsersObj searchUsersObj = new SearchUsersObj();
        Vector users = new Vector();

        try {
            Criteria crit = hsession.createCriteria(Users.class);
            searchUsersObj.setHits(crit.list().size());

            crit = hsession.createCriteria(Users.class);
            crit.setFirstResult(page * byPage);
            crit.setMaxResults(byPage);
            crit.addOrder(Order.asc("useName"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Users user = (Users) scroll.get(0);

                UserObj obj = new UserObj();

                obj.setActive(user.isUseActive());
                obj.setEmail(user.getUseEmail());

                Iterator it = user.getMailPreferenceses().iterator();
                MailPreferences mailPreferences = (MailPreferences) it.next();

                obj.setHtmlMessages(mailPreferences.isMaprHtmlMessage());
                obj.setIdint(user.getUseIdint());
                obj.setLanguage(user.getUseLanguage());
                obj.setMessagesByPage(mailPreferences.getMaprMessagesByPage());
                obj.setName(user.getUseName());
                obj.setPassword(user.getUsePassword());
                obj.setQuotaSize(mailPreferences.getMaprQuotaSize());
                obj.setRegisterDate(user.getUseRegisterDate());

                Integer[] roles = new Integer[user.getUserRoles().size()];

                if (roles.length > 0) {
                    it = user.getUserRoles().iterator();

                    int i = 0;

                    while (it.hasNext()) {
                        UserRole userRole = (UserRole) it.next();
                        roles[i] = new Integer(userRole.getId().getRoles()
                                                       .getRolIdint());
                        i++;
                    }
                }

                obj.setRoles(roles);

                obj.setSignature(mailPreferences.getMaprSignature());

                if (mailPreferences.getMaprSpamTolerance() == 100) {
                    obj.setSpamTolerance(true);
                } else {
                    obj.setSpamTolerance(false);
                }

                obj.setUsername(user.getUseUsername());
                obj.setVacationBody(mailPreferences.getMaprVacationBody());
                obj.setVacationSubject(mailPreferences.getMaprVacationSubject());
                obj.setVactionActive(mailPreferences.isMaprVacationActive());
                
                int usedQuotaSize = getUsedQuotaSize(hsession, user);            
                usedQuotaSize /= 1024;
                if (usedQuotaSize > 1024) {
                    usedQuotaSize /= 1024;
                    obj.setUsedQuota(usedQuotaSize + " MB");
                } else {
                	obj.setUsedQuota(((usedQuotaSize > 0) ? (usedQuotaSize + "") : "<1") + " kB");
                }

                users.addElement(obj);
            }

            searchUsersObj.setUsers(users);

            return searchUsersObj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param token DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public SearchUsersObj suggestUsers(Session hsession, String token,
        int page, int byPage) throws Exception {
        SearchUsersObj searchUsersObj = new SearchUsersObj();
        Vector users = new Vector();

        try {
            Criteria crit = hsession.createCriteria(Users.class);
            crit.add(Restrictions.or(Restrictions.ilike("useName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("useEmail", token, MatchMode.ANYWHERE)));
            searchUsersObj.setHits(crit.list().size());

            crit = hsession.createCriteria(Users.class);
            crit.add(Restrictions.or(Restrictions.ilike("useName", token,
                        MatchMode.ANYWHERE),
                    Restrictions.ilike("useEmail", token, MatchMode.ANYWHERE)));
            crit.setFirstResult(page * byPage);
            crit.setMaxResults(byPage);
            crit.addOrder(Order.asc("useName"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Users user = (Users) scroll.get(0);

                UserObj obj = new UserObj();

                obj.setActive(user.isUseActive());
                obj.setEmail(user.getUseEmail());

                Iterator it = user.getMailPreferenceses().iterator();
                MailPreferences mailPreferences = (MailPreferences) it.next();

                obj.setHtmlMessages(mailPreferences.isMaprHtmlMessage());
                obj.setIdint(user.getUseIdint());
                obj.setLanguage(user.getUseLanguage());
                obj.setMessagesByPage(mailPreferences.getMaprMessagesByPage());
                obj.setName(user.getUseName());
                obj.setPassword(user.getUsePassword());
                obj.setQuotaSize(mailPreferences.getMaprQuotaSize());
                obj.setRegisterDate(user.getUseRegisterDate());

                Integer[] roles = new Integer[user.getUserRoles().size()];

                if (roles.length > 0) {
                    it = user.getUserRoles().iterator();

                    int i = 0;

                    while (it.hasNext()) {
                        UserRole userRole = (UserRole) it.next();
                        roles[i] = new Integer(userRole.getId().getRoles()
                                                       .getRolIdint());
                        i++;
                    }
                }

                obj.setRoles(roles);

                obj.setSignature(mailPreferences.getMaprSignature());

                if (mailPreferences.getMaprSpamTolerance() == 100) {
                    obj.setSpamTolerance(true);
                } else {
                    obj.setSpamTolerance(false);
                }

                obj.setUsername(user.getUseUsername());
                obj.setVacationBody(mailPreferences.getMaprVacationBody());
                obj.setVacationSubject(mailPreferences.getMaprVacationSubject());
                obj.setVactionActive(mailPreferences.isMaprVacationActive());
                
                int usedQuotaSize = getUsedQuotaSize(hsession, user);            
                usedQuotaSize /= 1024;
                if (usedQuotaSize > 1024) {
                    usedQuotaSize /= 1024;
                    obj.setUsedQuota(usedQuotaSize + " MB");
                } else {
                	obj.setUsedQuota(((usedQuotaSize > 0) ? (usedQuotaSize + "") : "<1") + " kB");
                }

                users.addElement(obj);
            }

            searchUsersObj.setUsers(users);

            return searchUsersObj;
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param userObj DOCUMENT ME!
     *
     * @throws AdminException DOCUMENT ME!
     */
    public void updateUser(Session hsession, UserObj userObj)
        throws AdminException {
        try {
            Criteria crit1 = hsession.createCriteria(Users.class);
            crit1.add(Restrictions.eq("useIdint", new Integer(userObj.getIdint())));

            Users users = (Users) crit1.uniqueResult();

            if (users == null) {
                throw new AdminException("The user don't exist");
            }

            Set roles = users.getUserRoles();
            Iterator it = roles.iterator();

            while (it.hasNext()) {
                UserRole ur = (UserRole) it.next();

                //roles.remove(ur);
                hsession.delete(ur);
            }

            users.setUserRoles(new HashSet(0));
            hsession.update(users);
            hsession.flush();

            users.setUseActive(userObj.isActive());
            users.setUseEmail(userObj.getEmail());
            users.setUseLanguage(userObj.getLanguage());
            users.setUseName(userObj.getName());

            if (!StringUtils.isBlank(userObj.getPassword())) {
                users.setUsePassword(userObj.getPassword());
            }

            if (userObj.getRoles() != null && userObj.getRoles().length > 0) {
	            Criteria crit2 = hsession.createCriteria(Roles.class);
	            crit2.add(Restrictions.in("rolIdint", userObj.getRoles()));
	
	            Set set = new HashSet();
	            ScrollableResults scroll = crit2.scroll();
	
	            while (scroll.next()) {
	                UserRole userRole = new UserRole(new UserRoleId(users,
	                            (Roles) scroll.get(0)));
	                set.add(userRole);
	            }
	
	            users.setUserRoles(set);
	
	            hsession.update(users);
	            hsession.flush();
            }

            it = users.getMailPreferenceses().iterator();

            MailPreferences mailPreferences = (MailPreferences) it.next();
            mailPreferences.setUsers(users);
            mailPreferences.setMaprHtmlMessage(userObj.isHtmlMessages());
            mailPreferences.setMaprMessagesByPage(userObj.getMessagesByPage());
            mailPreferences.setMaprQuotaSize(userObj.getQuotaSize());
            if (userObj.getSignature() != null) {
            	mailPreferences.setMaprSignature("\n" + userObj.getSignature().replaceAll("'", "\\\\'"));
            }

            if (userObj.isSpamTolerance()) {
                mailPreferences.setMaprSpamTolerance(100);
            } else {
                mailPreferences.setMaprSpamTolerance(-1);
            }

            mailPreferences.setMaprVacationActive(userObj.isVactionActive());
            mailPreferences.setMaprVacationBody(userObj.getVacationBody());
            mailPreferences.setMaprVacationSubject(userObj.getVacationSubject());

            hsession.update(mailPreferences);
            hsession.flush();
        } catch (Exception ex) {
            throw new AdminException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws AdminException DOCUMENT ME!
     */
    public void deleteUsers(Session hsession, Integer[] idints)
        throws AdminException {
        try {
            Criteria crit = hsession.createCriteria(Users.class);
            crit.add(Restrictions.in("useIdint", idints));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Users users = (Users) scroll.get(0);

                if (users != null) {
                    hsession.delete(users);
                    hsession.flush();
                }
            }
        } catch (Exception ex) {
            throw new AdminException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @throws AdminException DOCUMENT ME!
     */
    public Vector roles(Session hsession) throws AdminException {
        Vector roles = new Vector();

        try {
            Criteria crit = hsession.createCriteria(Roles.class);
            crit.addOrder(Order.asc("rolName"));

            ScrollableResults scroll = crit.scroll();

            while (scroll.next()) {
                Roles role = (Roles) scroll.get(0);
                roles.addElement(new RoleObj(role.getRolIdint(),
                        role.getRolName()));
            }

            return roles;
        } catch (Exception ex) {
            throw new AdminException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws AdminException DOCUMENT ME!
     */
    public boolean checkUser(Session hsession, String username)
        throws AdminException {
        try {
            Criteria crit = hsession.createCriteria(Users.class);
            crit.add(Restrictions.eq("useUsername", username));

            Users users = (Users) crit.uniqueResult();

            if (users != null) {
                throw new UserExistException("ErrorMessages.user.exist");
            }

            return true;
        } catch (AdminException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AdminException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws AdminException DOCUMENT ME!
     */
    public boolean confirmPassword(Session hsession, String password,
        String confirmPassword) throws AdminException {
        try {
            if (StringUtils.isBlank(password) ||
                    StringUtils.isBlank(confirmPassword)) {
                throw new PasswordNullException(
                    "ErrorMessages.password.required");
            }

            password = password.trim();
            confirmPassword = confirmPassword.trim();

            if (!password.equals(confirmPassword)) {
                throw new PasswordNullException(
                    "ErrorMessages.password.confirm");
            }

            return true;
        } catch (AdminException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AdminException(ex);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param user DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    protected int getUsedQuotaSize(org.hibernate.Session hsession, Users user)
        throws MailException {
        try {
            Query query = hsession.getNamedQuery("used-quota-size");
            query.setInteger("user", new Integer(user.getUseIdint()));

            Integer value = (Integer) query.uniqueResult();

            return value.intValue();
        } catch (Exception ex) {
            return 0;
        } finally {
        }
    }
}
