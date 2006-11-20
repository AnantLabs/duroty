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


package com.duroty.application.open.manager;

import com.duroty.application.admin.utils.UserObj;
import com.duroty.application.mail.exceptions.MailException;

import com.duroty.hibernate.Identity;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Roles;
import com.duroty.hibernate.UserRole;
import com.duroty.hibernate.Users;

import com.duroty.utils.GeneralOperations;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.mail.HtmlEmail;

import org.hibernate.Criteria;

import org.hibernate.criterion.Restrictions;

import java.nio.charset.Charset;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class OpenManager {
    /**
     * Creates a new OpenManager object.
     */
    public OpenManager() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param msession DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void forgotPassword(org.hibernate.Session hsession,
        Session msession, String data) throws Exception {
        try {
            int pos = data.indexOf('@');

            if (pos > -1) {
                data = data.substring(0, pos);
            }

            Criteria criteria = hsession.createCriteria(Users.class);
            criteria.add(Restrictions.eq("useUsername", data));

            Users user = (Users) criteria.uniqueResult();

            if (user != null) {
                InternetAddress _to = new InternetAddress(user.getUseEmail(),
                        user.getUseName());

                Criteria crit = hsession.createCriteria(Identity.class);
                crit.add(Restrictions.eq("users", user));
                crit.add(Restrictions.eq("ideActive", new Boolean(true)));
                crit.add(Restrictions.eq("ideDefault", new Boolean(true)));

                Identity identity = (Identity) crit.uniqueResult();
                InternetAddress _from = new InternetAddress(identity.getIdeEmail(),
                        identity.getIdeName());

                Iterator it = user.getMailPreferenceses().iterator();
                MailPreferences mailPreferences = (MailPreferences) it.next();

                sendData(msession, _from, _to, user.getUseUsername(),
                    user.getUsePassword(), mailPreferences.getMaprSignature());
            }
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param msession DOCUMENT ME!
     * @param username DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public boolean existUser(org.hibernate.Session hsession, String username)
        throws Exception {
        try {
            Criteria criteria = hsession.createCriteria(Users.class);
            criteria.add(Restrictions.eq("useUsername", username));

            Users user = (Users) criteria.uniqueResult();

            if (user != null) {
                return true;
            } else {
                return false;
            }
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hsession DOCUMENT ME!
     * @param msession DOCUMENT ME!
     * @param userObj DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws MailException DOCUMENT ME!
     */
    public void register(org.hibernate.Session hsession, Session msession,
        UserObj userObj, String defaultEmailDomain) throws Exception {
        try {
            Users users = new Users();
            users.setUseActive(false);
            users.setUseEmail(userObj.getEmail());
            users.setUseLanguage(userObj.getLanguage());
            users.setUseName(userObj.getName());
            users.setUsePassword(userObj.getPassword());
            users.setUseRegisterDate(new Date());
            users.setUseUsername(userObj.getUsername());

            hsession.save(users);
            hsession.flush();

            MailPreferences mailPreferences = new MailPreferences();
            mailPreferences.setUsers(users);
            mailPreferences.setMaprHtmlMessage(true);
            mailPreferences.setMaprMessagesByPage(20);
            mailPreferences.setMaprQuotaSize(0);
            mailPreferences.setMaprSignature("--\n" + userObj.getName());
            mailPreferences.setMaprSpamTolerance(100);
            mailPreferences.setMaprVacationActive(false);

            hsession.save(mailPreferences);
            hsession.flush();

            Identity identity = new Identity();
            identity.setIdeActive(true);
            identity.setIdeCode(RandomStringUtils.randomAlphanumeric(25));
            identity.setIdeDefault(true);
            identity.setIdeEmail(userObj.getUsername() + "@" +
                defaultEmailDomain);
            identity.setIdeName(userObj.getName());
            identity.setIdeReplyTo(userObj.getUsername() + "@" +
                defaultEmailDomain);
            identity.setUsers(users);

            hsession.save(identity);
            hsession.flush();

            InternetAddress from = new InternetAddress(userObj.getEmail(),
                    userObj.getName());

            Criteria crit = hsession.createCriteria(Roles.class);
            crit.add(Restrictions.eq("rolName", "admin"));

            Roles role = (Roles) crit.uniqueResult();

            InternetAddress[] to = new InternetAddress[role.getUserRoles().size()];
            Iterator it = role.getUserRoles().iterator();
            int i = 0;

            while (it.hasNext()) {
                UserRole userRole = (UserRole) it.next();
                Users aux = userRole.getId().getUsers();
                Criteria criteria = hsession.createCriteria(Identity.class);
                criteria.add(Restrictions.eq("ideDefault", new Boolean(true)));
                criteria.add(Restrictions.eq("ideActive", new Boolean(true)));
                criteria.add(Restrictions.eq("users", aux));

                Identity id = (Identity) criteria.uniqueResult();

                to[i] = new InternetAddress(id.getIdeEmail(), id.getIdeName());
                i++;
            }

            notifyToAdmins(msession, from, to,
                userObj.getUsername() + " (" + userObj.getName() + ")");
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     * @param repositoryName DOCUMENT ME!
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     * @param subject DOCUMENT ME!
     * @param body DOCUMENT ME!
     *
     * @throws MailException DOCUMENT ME!
     */
    private void sendData(Session msession, InternetAddress from,
        InternetAddress to, String username, String password, String signature)
        throws Exception {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setMailSession(msession);

            email.setFrom(from.getAddress(), from.getPersonal());
            email.addTo(to.getAddress(), to.getPersonal());

            email.setSubject("Duroty System");
            email.setHtmlMsg("<p>Username: <b>" + username +
                "</b></p><p>Password: " + password + "<b></b></p><p>" +
                signature + "</p>");

            email.setCharset(MimeUtility.javaCharset(Charset.defaultCharset()
                                                            .displayName()));

            email.send();
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param msession DOCUMENT ME!
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     * @param username DOCUMENT ME!
     * @param password DOCUMENT ME!
     * @param signature DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void notifyToAdmins(Session msession, InternetAddress from,
        InternetAddress[] to, String user) throws Exception {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setMailSession(msession);

            email.setFrom(from.getAddress(), from.getPersonal());

            HashSet aux = new HashSet(to.length);
            Collections.addAll(aux, to);
            email.setTo(aux);

            email.setSubject("User register in Duroty System");
            email.setHtmlMsg(
                "<p>The user solicits register into the system</p><p>The user is: <b>" +
                user + "</b></p>");

            email.setCharset(MimeUtility.javaCharset(Charset.defaultCharset()
                                                            .displayName()));

            email.send();
        } finally {
        }
    }
}
