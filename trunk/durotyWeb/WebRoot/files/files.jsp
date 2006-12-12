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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />">
	<head>
		<base href="<%=basePath%>" />
		<title>Duroty System</title>	
		<meta http-equiv="keywords" content="jordi marquès, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />		
		
		<script language="javascript">
			window.onload = initialize;


			function initialize() {
				parent.document.body.rows = "100%,*,*";	
			}
		</script>
	</head>

	<frameset framespacing="0" border="0" frameborder="0" rows="80,*,65">
		<frame name="head" scrolling="no" noresize target="contenido" src="<%=basePath%>files/head.jsp">
		<frameset cols="150,*">
			<frame name="left" target="body" src="<%=basePath%>files/left.jsp">
			<frame name="body" src="<%=basePath%>files/contents.jsp">
		</frameset>
		<frame name="foot" scrolling="no" noresize target="body" src="<%=basePath%>files/foot.jsp">
		<noframes>
			<body>
				<p>Duroty System is the Gmail Open Source. Duroty is a service of personal news feeds, mail management, bookmarks management and next will have also calendar management and indexing server files.</p>
			</body>
		</noframes>
	</frameset>

</html>
