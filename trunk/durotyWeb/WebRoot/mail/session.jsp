
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
		<title>Duroty System</title>
		<meta http-equiv="keywords" content="duroty,gmail,lucene,jboss,mail,email,j2ee,pop3,imap,java,webmail,network" />
		<meta http-equiv="description" content="Duroty System is the Gmail Open Source. Duroty is a service of personal feeds, management of the mail, management of bookmarks and next will have also management of electronic agenda and indexing of files." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />

		<!-- The Sarissa Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/sarissa/sarissa.js"></script>

		<!-- The AJAX Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>
		
		<!-- The AJAX Library -->
		<script type="text/javascript" src="<%=basePath%>mail/javascript/session.js"></script>
	</head>

	<body>
	</body>
</html>
