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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />">

	<head>
		<base href="<%=basePath%>" />
		<title><tiles:insert name="title" /></title>
		<meta http-equiv="keywords" content="jordi marquès, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />
		
		<!-- The Calendar Library -->
		<style type="text/css">@import url(<%=basePath%>javascript/jscalendar/calendar-blue.css);</style>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/lang/calendar-<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/calendar-setup.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/nifty/nifty.js"></script>
		<script type="text/javascript" src="<%=basePath%>calendar/javascript/calendar.js"></script>				
				
		<link href="<%=basePath%>style/calendar_style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="header">
			<div id="navigation_right" style="float: right; width: 25%; height: auto; margin-left: 0%; margin-right: 0%;">
				<!-- toolbar -->
				<tiles:insert name="toolbar" />
				<!-- fi toolbar-->
			</div>
			<div id="navigation_left" style="margin: 0pt 10% 0pt 0pt;">		
				<form action="" method="post" onsubmit="javascript:Mail.displayLocation('simpleSearch:0*0!0', null);return false;" style="margin: 0px; padding: 0px;" enctype="multipart/form-data">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td nowrap="nowrap" style="padding-left: 5px; padding-right: 5px; padding-top: 5px;">
								<img src="<%=basePath%>images/duroty.gif" border="0" align="middle" />
							</td>
							<td nowrap="nowrap" align="right" style="">
								<input type="text" size="30" maxlength="1024" name="token" id="token" />
							</td>
							<td nowrap="nowrap" align="left" style="padding-left: 3px;">
								<button style="white-space: nowrap;" onclick="javascript:Mail.displayLocation('simpleSearch:0*0!0', null);"><fmt:message key="general.search" /></button>
							</td>
							<td nowrap="nowrap" style="font-size: 10px; padding-left: 3px;">
								<a href="javascript:openwin('<%=basePath%>mail/help_<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />.html', 'Help', 700, 600);">- <fmt:message key="general.help" /></a>
								<br />
								<span onclick="showHideElement('divAdvancedSearch');" style="cursor: pointer; color: blue; text-decoration: underline;">- <fmt:message key="general.advancedSearch" /></span>
							</td>
						</tr>
					</table>										
				</form>
			</div>	
		</div>
		<div id="content">
			<div id="left">
				<!-- left -->
				<tiles:insert name="left" />
				<!-- fi left-->
			</div>

			<div id="right">
				<!-- contents -->
				<tiles:insert name="contents" />
				<!-- fi contents-->
			</div>
		</div>
		<div id="footer">
			<tiles:insert name="foot" />
		</div>	
	</body>
</html>
