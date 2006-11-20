/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/
package com.duroty.application.chat.manager;

import com.duroty.application.bookmark.exceptions.BookmarkException;
import com.duroty.application.chat.exceptions.ChatException;
import com.duroty.application.chat.exceptions.NotAcceptChatException;
import com.duroty.application.chat.exceptions.NotLoggedInException;
import com.duroty.application.chat.exceptions.NotOnlineException;
import com.duroty.application.chat.utils.BuddyObj;
import com.duroty.application.chat.utils.ConversationsObj;
import com.duroty.application.mail.exceptions.MailException;
import com.duroty.application.mail.manager.SendMessageThread;

import com.duroty.hibernate.BuddyList;
import com.duroty.hibernate.Conversations;
import com.duroty.hibernate.Identity;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Users;

import com.duroty.utils.GeneralOperations;
import com.duroty.utils.mail.MessageUtilities;
import com.duroty.utils.mail.RFC2822Headers;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.UnsupportedEncodingException;

import java.nio.charset.Charset;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class ChatManager {
    /**
     * DOCUMENT ME!
     */
    private static final int FLAG_BOLD = 1;

    /**
     * DOCUMENT ME!
     */
    private static final int FLAG_ITALIC = 2;

    /**
     * DOCUMENT ME!
     */
    private String charset = null;

    /**
     * DOCUMENT ME!
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     * Creates a new ChatManager object.
     *
     * @param chat DOCUMENT ME!
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public ChatManager(HashMap chat)
        throws ClassNotFoundException, InstantiationException, 
            IllegalAccessException {
        super();

        if (charset == null) {
            charset = MimeUtility.javaCharset(Charset.defaultCharset()
                                                     .displayName());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param recipient DOCUMENT ME!
     * @param message DOCUMENT ME!
     *
     * @throws ChatException DOCUMENT ME!
     * @throws BookmarkException DOCUMENT ME!
     */
    public void send(Session hsession, javax.mail.Session msession,
        String sender, String recipient, String message, int flag)
        throws ChatException {
        try {
            if (isOnline(hsession, sender, recipient) > 0) {
                switch (flag) {
                case FLAG_BOLD:
                    message = "<b>" + message + "</b>";

                    break;

                case FLAG_ITALIC:
                    message = "<i>" + message + "</i>";

                    break;

                default:
                    break;
                }

                Users userSender = getUser(hsession, sender);
                Users userRecipient = getUser(hsession, recipient);

                Conversations conversations = new Conversations();
                conversations.setConvStamp(new Date());
                conversations.setConvMessage(message);
                conversations.setUsersByConvSenderIdint(userSender);
                conversations.setUsersByConvRecipientIdint(userRecipient);
                conversations.setConvMessage(message);

                hsession.save(conversations);
                hsession.flush();

                sendMail(hsession, msession, userSender, userRecipient, message);
            } else {
                throw new NotOnlineException();
            }
        } catch (Exception e) {
            throw new ChatException(e);
        } catch (OutOfMemoryError e) {
            throw new ChatException(e);
        } catch (Throwable e) {
            throw new ChatException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param userSender DOCUMENT ME!
     * @param userRecipient DOCUMENT ME!
     */
    private void sendMail(Session hsession, javax.mail.Session msession,
        Users userSender, Users userRecipient, String message) {
        try {
            String sender = userSender.getUseUsername();
            String recipient = userRecipient.getUseUsername();

            Identity identitySender = getIdentity(hsession, userSender);
            Identity identityRecipient = getIdentity(hsession, userRecipient);

            HtmlEmail email = new HtmlEmail();
            InternetAddress _from = new InternetAddress(identitySender.getIdeEmail(),
                    identitySender.getIdeName());
            InternetAddress _replyTo = new InternetAddress(identitySender.getIdeReplyTo(),
                    identitySender.getIdeName());
            InternetAddress[] _to = MessageUtilities.encodeAddresses(identityRecipient.getIdeEmail(),
                    null);

            if (_from != null) {
                email.setFrom(_from.getAddress(), _from.getPersonal());
            }

            if (_replyTo != null) {
                email.addReplyTo(_replyTo.getAddress(), _replyTo.getPersonal());
            }

            if ((_to != null) && (_to.length > 0)) {
                HashSet aux = new HashSet(_to.length);
                Collections.addAll(aux, _from);
                Collections.addAll(aux, _to);
                email.setTo(aux);
            }

            email.setCharset(charset);
            email.setSubject("Chat " + sender + " >> " + recipient);
            email.setHtmlMsg(message);

            calendar.setTime(new Date());

            String minute = "30";

            if (calendar.get(Calendar.MINUTE) >= 30) {
                minute = "60";
            }

            String value = String.valueOf(calendar.get(Calendar.YEAR)) +
                String.valueOf(calendar.get(Calendar.MONTH)) +
                String.valueOf(calendar.get(Calendar.DATE)) +
                String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + minute +
                String.valueOf(userSender.getUseIdint() +
                    userRecipient.getUseIdint());
            String reference = "<" + value + ".JavaMail.duroty@duroty" + ">";
            email.addHeader(RFC2822Headers.IN_REPLY_TO, reference);
            email.addHeader(RFC2822Headers.REFERENCES, reference);

            email.addHeader("X-DBox", "CHAT");

            Date now = new Date();
            email.setSentDate(now);

            email.setMailSession(msession);
            email.buildMimeMessage();

            MimeMessage mime = email.getMimeMessage();

            int size = MessageUtilities.getMessageSize(mime);

            if (controlQuota(hsession, userSender, size)) {
                //messageable.saveSentMessage(null, mime, userSender);
                Thread thread = new Thread(new SendMessageThread(email));
                thread.start();
            }
        } catch (UnsupportedEncodingException e) {
        } catch (MessagingException e) {
        } catch (EmailException e) {
        } catch (Exception e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param away DOCUMENT ME!
     *
     * @throws ChatException DOCUMENT ME!
     */
    public String ping(Session hsession, String username, int away)
        throws ChatException {
        try {
            Vector buddiesOnline = new Vector();
            Vector buddiesOffline = new Vector();
            Vector buddies = new Vector();
            Vector messages = new Vector();
            JSONObject json = new JSONObject();

            Users user = getUser(hsession, username);

            if (user.getUseIsOnline() >= 3) {
                user.setUseLastPing(new Date());
                hsession.update(user);
                hsession.flush();
            } else if (user.getUseLastState() > 0) {
                user.setUseIsOnline(user.getUseLastState());
                user.setUseLastPing(new Date());
                hsession.update(user);
                hsession.flush();
            } else {
                user.setUseIsOnline(0);
                user.setUseCustomMessage(null);
                user.setUseLastPing(new Date());
                hsession.update(user);
                hsession.flush();

                throw new NotLoggedInException();
            }

            json.put("state", user.getUseIsOnline());
            json.put("lastState", user.getUseLastState());
            json.put("awayMessage", user.getUseCustomMessage());

            Criteria crit2 = hsession.createCriteria(Conversations.class);
            crit2.add(Restrictions.eq("usersByConvRecipientIdint", user));

            ScrollableResults scroll2 = crit2.scroll();

            int numMessages = 0;

            while (scroll2.next()) {
                Conversations conversations = (Conversations) scroll2.get(0);
                messages.addElement(new ConversationsObj(
                        conversations.getUsersByConvSenderIdint()
                                     .getUseUsername(),
                        conversations.getConvMessage()));

                hsession.delete(conversations);

                numMessages++;
            }

            hsession.flush();

            json.put("numMessages", new Integer(numMessages));
            json.put("messages", messages);

            Criteria crit1 = hsession.createCriteria(BuddyList.class);
            crit1.add(Restrictions.eq("usersByBuliOwnerIdint", user));
            crit1.add(Restrictions.eq("buliActive", new Boolean(true)));
            crit1.addOrder(Order.desc("buliLastDate"));

            ScrollableResults scroll1 = crit1.scroll();

            while (scroll1.next()) {
                BuddyList buddyList = (BuddyList) scroll1.get(0);
                Users buddy = buddyList.getUsersByBuliBuddyIdint();

                if (buddy.isUseActive()) {
                    String name = buddy.getUseName();

                    if (StringUtils.isBlank(name)) {
                        name = buddy.getUseUsername();
                    }

                    if (buddy.getUseIsOnline() == 0) {
                        buddiesOffline.addElement(new BuddyObj(name,
                                buddy.getUseUsername(), buddy.getUseIsOnline()));
                    } else {
                        buddiesOnline.addElement(new BuddyObj(name,
                                buddy.getUseUsername(), buddy.getUseIsOnline()));
                    }
                }
            }

            if (!buddiesOnline.isEmpty()) {
                buddies.addAll(buddiesOnline);
            }

            if (!buddiesOffline.isEmpty()) {
                buddies.addAll(buddiesOffline);
            }

            json.put("buddy", buddies);

            return json.toString();
        } catch (Exception e) {
            throw new ChatException(e);
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
     * @throws ChatException DOCUMENT ME!
     */
    public void login(Session hsession, String username)
        throws ChatException {
        try {
            Users user = getUser(hsession, username);

            user.setUseIsOnline(1);
            user.setUseLastState(1);
            user.setUseCustomMessage(null);
            hsession.update(user);
            hsession.flush();
        } catch (Exception e) {
            throw new ChatException(e);
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
     * @throws ChatException DOCUMENT ME!
     */
    public void signin(Session hsession, String username)
        throws ChatException {
        try {
            Users user = getUser(hsession, username);

            user.setUseIsOnline(user.getUseLastState());
            hsession.update(user);
            hsession.flush();
        } catch (Exception e) {
            throw new ChatException(e);
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
     * @throws ChatException DOCUMENT ME!
     */
    public void logout(Session hsession, String username)
        throws ChatException {
        try {
            Users user = getUser(hsession, username);
            user.setUseIsOnline(0);
            hsession.update(user);
            hsession.flush();
        } catch (Exception e) {
            throw new ChatException(e);
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
     * @throws ChatException DOCUMENT ME!
     */
    public void signout(Session hsession, String username)
        throws ChatException {
        try {
            Users user = getUser(hsession, username);
            user.setUseIsOnline(0);
            user.setUseCustomMessage(null);
            user.setUseLastState(0);
            hsession.update(user);
            hsession.flush();
        } catch (Exception e) {
            throw new ChatException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ChatException DOCUMENT ME!
     */
    public boolean checkUser(Session hsession, String username, String name)
        throws ChatException {
        try {
            Users user = getUser(hsession, name);

            if (user != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ChatException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ChatException DOCUMENT ME!
     */
    protected int isOnline(Session hsession, String sender, String recipient)
        throws ChatException {
        try {
            Criteria criteria = hsession.createCriteria(Users.class);
            criteria.add(Restrictions.eq("useUsername", recipient));
            criteria.add(Restrictions.eq("useActive", new Boolean(true)));

            Users userRecipient = (Users) criteria.uniqueResult();

            if (userRecipient == null) {
                return 0;
            } else {
                //cal mirar si el recipent ens té com buddy
                Users userSender = getUser(hsession, sender);

                criteria = hsession.createCriteria(BuddyList.class);
                criteria.add(Restrictions.eq("usersByBuliOwnerIdint",
                        userRecipient));
                criteria.add(Restrictions.eq("usersByBuliBuddyIdint", userSender));
                criteria.add(Restrictions.eq("buliActive", new Boolean(true)));

                BuddyList buddyList = (BuddyList) criteria.uniqueResult();

                if (buddyList != null) {
                    return userRecipient.getUseIsOnline();
                } else {
                    throw new NotAcceptChatException();
                }
            }
        } catch (NotAcceptChatException e) {
            throw e;
        } catch (Exception e) {
            throw new ChatException(e);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @throws ChatException DOCUMENT ME!
     */
    public void setState(Session hsession, String username, int state,
        String message) throws ChatException {
        try {
            Users user = getUser(hsession, username);
            user.setUseIsOnline(state);

            if (state < 3) {
                //si no es idle recordem l'últim state
                user.setUseLastState(state);
            }

            user.setUseCustomMessage(message);
            hsession.update(user);
            hsession.flush();
        } catch (Exception e) {
            throw new ChatException(e);
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected Users getUser(Session hsession, String repositoryName)
        throws Exception {
        try {
            Criteria criteria = hsession.createCriteria(Users.class);
            criteria.add(Restrictions.eq("useUsername", repositoryName));
            criteria.add(Restrictions.eq("useActive", new Boolean(true)));

            return (Users) criteria.uniqueResult();
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param users DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected Identity getIdentity(org.hibernate.Session hsession, Users users)
        throws Exception {
        try {
            Criteria criteria = hsession.createCriteria(Identity.class);
            criteria.add(Restrictions.eq("ideDefault", new Boolean(true)));
            criteria.add(Restrictions.eq("ideActive", new Boolean(true)));
            criteria.add(Restrictions.eq("users", users));

            return (Identity) criteria.uniqueResult();
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param user DOCUMENT ME!
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected boolean controlQuota(org.hibernate.Session hsession, Users user,
        int size) throws Exception {
        int maxQuotaSize = getMaxQuotaSize(hsession, user);
        int usedQuotaSize = getUsedQuotaSize(hsession, user) + size;

        int count = maxQuotaSize - usedQuotaSize;

        if (count < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DMailException DOCUMENT ME!
     */
    protected int getMaxQuotaSize(org.hibernate.Session hsession, Users user)
        throws MailException {
        try {
            Criteria crit = hsession.createCriteria(MailPreferences.class);
            crit.add(Restrictions.eq("users", user));

            MailPreferences preferences = (MailPreferences) crit.uniqueResult();

            return preferences.getMaprQuotaSize();
        } catch (Exception ex) {
            return 0;
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DMailException DOCUMENT ME!
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
