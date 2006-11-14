<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Locale" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
request.setAttribute("auxlocale", new Locale("en"));
%>

<fmt:setLocale value='${auxlocale}' scope="session" />

<div>
	<p style="font-size: 14px;">
		Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation.
	</p>
	<p style="font-size: 14px;">
		Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information.
	</p>
	<p style="font-size: 14px;">
		It uses simple and advanced message searching systems similar to the ones used by Google webs, but it also allows to search in attached files (word, powerpoint, excel, pdf, html, etc.) in addition to the normal fields such as from, to, subject, body,
		etc.
	</p>
	<p style="font-size: 14px;">
		The main functionality and use of duroty is email management. Duroty indexes the content of attachments of user's messages, being possible to search by content in these files.
	</p>

	<p style="font-size: 14px;">
		The interface of all applications uses AJAX (combination of DHTML, CSS; Javascript, XML and server languages. Furthermore, the interface is available in 3 languages (Catalan, Spanish and English).
	</p>
	<p style="font-size: 14px;">
		The server is completely developed with Java.
	</p>
	<p style="font-size: 14px;">
		Duroty supports Firefox, Internet Explorer and Mozilla
	</p>
	<p style="font-size: 14px;">
		For more information you can consult the technical documentation <a href="http://www.duroty.com" target="_blank">HERE</a> or write to <a href="mailto:duroty@duroty.com">duroty@duroty.com</a>
	</p>
</div>
