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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />">

	<head>
		<base href="<%=basePath%>" />
		<title><tiles:insert name="title" /></title>
		<meta http-equiv="keywords" content="duroty,gmail,lucene,jboss,mail,email,j2ee,pop3,imap,java,webmail,network" />
		<meta http-equiv="description" content="Duroty System is the Gmail Open Source. Duroty is a service of personal feeds, management of the mail, management of bookmarks and next will have also management of electronic agenda and indexing of files." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />

		<!-- The X Cross-Browser DHTML Library -->
		<script language="JavaScript" src="<%=basePath%>javascript/x/x_core.js"></script>
		<script language="JavaScript" src="<%=basePath%>javascript/x/x_dom.js"></script>
		<script language="JavaScript" src="<%=basePath%>javascript/x/x_event.js"></script>

		<!-- Load the DhtmlHistory and HistoryStorage APIs -->
		<script type="text/javascript" src="<%=basePath%>javascript/dhtmlHistory.js"></script>

		<!-- The AJAX Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>

		<!-- The Utils Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/utils.js"></script>
				
		<!-- The Home Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/dragable-boxes.js"></script>
		
		<!-- The Home Library -->
		<script type="text/javascript" src="<%=basePath%>home/javascript/home.js"></script>

		<link href="<%=basePath%>style/home_style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<script language="JavaScript">
			if (window == top) {
				alert('It is not allowed to see the frame individually');
				top.location.href = "<%=basePath%>index.jsp";
			}
		</script>		
		<div id="cornerLoading">
			<fmt:message key="general.loading" />
		</div>
		<div id="mesh" class="show">
			<table width="0" border="0" cellspacing="0" cellpadding="0" class="mesh">
				<tr>
					<td>						
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>								
								<td class="contents" width="100%">
									<!-- BODY -->
									<tiles:insert name="contents" />
									<!-- FI BODY -->
								</td>
							</tr>
							<tr>
								<td class="foot">
									<!-- FOOT -->
									<tiles:insert name="foot" />
									<!-- FI FOOT -->
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<iframe src="" id="util" name="util" width="0" frameborder="0"></iframe>
		<iframe src="<%=basePath%>home/session.jsp" id="session" name="session" width="0" frameborder="0"></iframe>
	</body>
</html>
