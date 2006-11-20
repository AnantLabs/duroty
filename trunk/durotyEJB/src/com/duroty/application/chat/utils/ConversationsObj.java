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
package com.duroty.application.chat.utils;

import java.io.Serializable;


/**
 * @author Jordi Marquès
 * @version 1.0
*/
public class ConversationsObj implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8409570939975944525L;

    /**
     * DOCUMENT ME!
     */
    private String sender;

    /**
     * DOCUMENT ME!
     */
    private String message;

    /**
     * Creates a new ConversationsObj object.
     *
     * @param sender DOCUMENT ME!
     * @param recipient DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public ConversationsObj(String sender, String message) {
        super();

        // TODO Auto-generated constructor stub
        this.sender = sender;
        this.message = message;
    }

    /**
    * Creates a new ConversationsObj object.
    */
    public ConversationsObj() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMessage() {
        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSender() {
        return sender;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sender DOCUMENT ME!
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
}
