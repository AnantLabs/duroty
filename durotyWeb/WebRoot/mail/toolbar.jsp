
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
		<td style="white-space: nowrap;">			
			<a href="<%=basePath%>index.jsp" target="_top">
				Home
			</a>
			&nbsp;|&nbsp;
			<a href="<%=basePath%>mail/" target="_top">
				Mail
			</a>
			&nbsp;|&nbsp;
			<a href="<%=basePath%>bookmark/" target="_top">
				Bookmarks
			</a>
			&nbsp;|&nbsp;
			<a href="<%=basePath%>notebook/" target="_top">
				Notebook
			</a>
			<%
				if (request.isUserInRole("admin")) {
					%>
					&nbsp;|&nbsp;
					<a href="<%=basePath%>admin/" target="_top">
					Admin
					</a>
					<%
				}
			%>
		</td>
		<td style="white-space: nowrap;">
		</td>
		<td class="toolbarInfo" style="white-space: nowrap;">
			&nbsp;
			<b><c:out value="${user}" /></b>
			&nbsp;
			<a href="<%=basePath%>mail/preferences/settings.drt"><fmt:message key="general.settings" /></a>
			&nbsp;
			<a href="<%=basePath%>login/logout.drt" target="_top"><fmt:message key="general.logout" /></a>
		</td>
	</tr>
</table>
