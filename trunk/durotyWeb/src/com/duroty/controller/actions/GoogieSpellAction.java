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
/**
 *
 */
package com.duroty.controller.actions;

import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.duroty.constants.Constants;
import com.duroty.constants.ExceptionCode;
import com.duroty.utils.exceptions.ExceptionUtilities;
import com.duroty.utils.log.DLog;


/**
 * @author durot
 *
 */
public class GoogieSpellAction extends DefaultAction {
    /**
     * DOCUMENT ME!
     */
    private static final String googleUrl = "www.google.com";

    /**
     *
     */
    public GoogieSpellAction() {
        super();

        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doInit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doInit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ActionMessages errors = new ActionMessages();

        PostMethod post = null;

        try {
        	Enumeration enumeration = request.getParameterNames();
        	while (enumeration.hasMoreElements()) {
        		String name = (String) enumeration.nextElement();
        		String value = (String) request.getParameter(name);
        		DLog.log(DLog.WARN, this.getClass(), name + " >> " + value);
        	}
        	
        	
            String text =
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?><spellrequest textalreadyclipped=\"0\" ignoredups=\"0\" ignoredigits=\"1\" ignoreallcaps=\"1\"><text>" +
                request.getParameter("check") + "</text></spellrequest>";

            String lang = request.getParameter("lang");
            
            String id = request.getParameter("id");
            String cmd = request.getParameter("cmd");

            String url = "https://" + googleUrl + "/tbproxy/spell?lang=" +
                lang + "&hl=" + lang;

            post = new PostMethod(url);
            post.setRequestBody(text);
            post.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");

            HttpClient client = new HttpClient();
            int result = client.executeMethod(post);

            // Display status code
            System.out.println("Response status code: " + result);

            // Display response
            System.out.println("Response body: ");

            String resp = post.getResponseBodyAsString();

            System.out.println(resp);
            
            String goodieSpell = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
            
            Vector matches = getMatches(resp);
            
            if (matches.size() <= 0) {
            	goodieSpell = goodieSpell + "<res id=\"" + id + "\" cmd=\"" + cmd + "\" />";
            } else {
            	goodieSpell = goodieSpell + "<res id=\"" + id + "\" cmd=\"" + cmd + "\">";
            	
            	StringBuffer buffer = new StringBuffer();
            	
            	for (int i = 0; i < matches.size(); i++) {
            		if (buffer.length() > 0) {
            			buffer.append("+");
            		}
            		
            		String aux = (String) matches.get(i);
            		aux = aux.substring(aux.indexOf(">") + 1, aux.length());
            		aux = aux.substring(0, aux.indexOf("<"));
            		aux = aux.trim().replaceAll("\\s+", "\\+");
            		
            		buffer.append(aux);
            	}
            	
            	goodieSpell = goodieSpell + buffer.toString() + "</res>";
            	
            }

            request.setAttribute("googieSpell", goodieSpell);
        } catch (Exception ex) {
            String errorMessage = ExceptionUtilities.parseMessage(ex);

            if (errorMessage == null) {
                errorMessage = "NullPointerException";
            }

            errors.add("general",
                new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX +
                    "general", errorMessage));
            request.setAttribute("exception", errorMessage);
            doTrace(request, DLog.ERROR, getClass(), errorMessage);
        } finally {
            try {
                post.releaseConnection();
            } catch (Exception e) {
            }
        }

        if (errors.isEmpty()) {
            doTrace(request, DLog.INFO, getClass(), "OK");

            return mapping.findForward(Constants.ACTION_SUCCESS_FORWARD);
        } else {
            saveErrors(request, errors);

            return mapping.findForward(Constants.ACTION_FAIL_FORWARD);
        }
    }
    
    protected Vector getMatches(String text) {
    	Pattern pattern = Pattern.compile("<c o=\\\"([^\\\"]*)\\\" l=\\\"([^\\\"]*)\\\" s=\\\"([^\\\"]*)\\\">([^<]*)<\\/c>", Pattern.CASE_INSENSITIVE);
    	Matcher matcher = pattern.matcher(text);
    	
    	Vector vector = new Vector();
    	
    	while (matcher.find()) {
    		int groupCount = matcher.groupCount();
    		vector.addElement(matcher.group(0));
    		for (int i = 0; i < groupCount; i++) {
    			DLog.log(DLog.WARN, this.getClass(), "GROUP: " + i + " >> " + matcher.group(i));
    		}
    	}
    	
    	return vector;
    }

    /* (non-Javadoc)
     * @see com.duroty.controller.actions.DefaultAction#doTrace(javax.servlet.http.HttpServletRequest, int, java.lang.Class, java.lang.String)
     */
    protected void doTrace(HttpServletRequest request, int level, Class classe,
        String message) throws Exception {
        // TODO Auto-generated method stub
    }
}
