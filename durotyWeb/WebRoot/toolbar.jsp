<%@ page import="java.util.Date" %>
<%
response.setHeader("Expires", (new Date((new Date()).getTime() + 315360000)).toString());
response.setHeader("Pragma", "public");
response.setHeader("Cache-Control", "max-age=315360000");
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<table border="0" cellspacing="2" cellpadding="2" align="right">
	<tr>
		<td style="white-space: nowrap; font-size: 11px;">	
			<%
				if (request.isUserInRole("member")) {
					%>					
					<a href="<%=basePath%>index.jsp" target="_top">Home</a>&nbsp;
					<%
				}
			%>
			<%
				if (request.isUserInRole("mail")) {
					%>					
					<a href="<%=basePath%>mail/" target="_top">Mail</a><b><span id="infoTopINBOX"></span></b>&nbsp;
					<%
				}
			%>
			<%
				if (request.isUserInRole("mail")) {
					%>					
					<a href="<%=basePath%>files/" target="_top">Files</a>&nbsp;
					<%
				}
			%>
			<%
				if (request.isUserInRole("bookmark")) {
					%>					
					<a href="<%=basePath%>bookmark/" target="_top">Bookmarks</a>&nbsp;
					<%
				}
			%>
			<%
				if (request.isUserInRole("bookmark")) {
					%>					
					<a href="<%=basePath%>notebook/" target="_top">Notebook</a>&nbsp;
					<%
				}
			%>
			<%
				if (request.isUserInRole("admin")) {
					%>
					&nbsp;|&nbsp;
					<a href="<%=basePath%>admin/" target="_top">Admin</a>
					<%
				}
			%>
		</td>
		<td style="white-space: nowrap; font-size: 11px;">
		</td>
		<td class="toolbarInfo" style="white-space: nowrap; padding-right: 5px; font-size: 11px;">
			&nbsp;
			<b><c:out value="${user}" /></b>
			&nbsp;
			<a href="<%=basePath%>mail/preferences/settings.drt" target="main"><fmt:message key="general.settings" /></a>
			&nbsp;
			<a href="<%=basePath%>login/logout.drt" target="_top"><fmt:message key="general.logout" /></a>
		</td>
	</tr>
</table>
