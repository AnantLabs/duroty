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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />">

	<head>
		<base href="<%=basePath%>" />
		<title></title>
		<meta http-equiv="keywords" content="jordi marqu�s, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />


		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>		
		<!-- The Utils Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/utils.js"></script>				
		
		<!-- The Mail Library -->
		<script type="text/javascript" src="<%=basePath%>files/javascript/filesleft.js"></script>
				
		<link href="<%=basePath%>style/files_left_style.css" rel="stylesheet" type="text/css" />
		
		<script language="javascript" type="text/javascript">	
			function clearLabel(text, value) {
				text.value = value;
			}
		</script>
	</head>
	<body id="twocolumn-left">						
		<script language="JavaScript">			
			if (window == top) {			
				alert('It is not allowed to see the frame individually');
				top.location.href = "<%=basePath%>files/index.jsp";
			}
		</script>
		<div id="cornerLoading"><fmt:message key="general.loading" /></div>
		<div id="labels">
		</div>
		<div id="createLabel">
			<form action="" method="post" style="margin: 0px; padding: 0px;" onsubmit="FilesLeft.createLabel(this.label.value);return false;">
				<input type="text" size="20" name="label" maxlength="100" style="width: 95%;" value="<fmt:message key="mail.label.create" />" onfocus="clearLabel(this, '');" onblur="clearLabel(this, '<fmt:message key="mail.label.create" />');"/>
			</form>
		</div>
		<div id="trash" onclick="parent.frames['body'].Files.getFiles('TRASH', 0, 0, -1, -2, '');FilesLeft.parseLeft(false);">
			<img border="0" src="<%=basePath%>files/images/trash.png" alt="<fmt:message key="folder.TRASH" />" title="<fmt:message key="folder.TRASH" />" />
		</div>
	</body>
</html>
