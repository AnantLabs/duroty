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
		<meta http-equiv="keywords" content="duroty,gmail,lucene,jboss,mail,email,j2ee,pop3,imap,java,webmail,network" />
		<meta http-equiv="description" content="Duroty System is the Gmail Open Source. Duroty is a service of personal feeds, management of the mail, management of bookmarks and next will have also management of electronic agenda and indexing of files." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />	
		
		<script language="javascript">					
			function wfs() {			
				try {
					if (parent!=window && parent.wfs) {
						return false;
					}
				}catch(e){}	
				
				return true;
			}
			
			function lj() {
				main.location.replace("<%=basePath%>notebook/notebook.drt");
			}													
			
			if (wfs()) {
				document.write('<frameset onload=lj() rows="*,100%,*" border="0"><frame name="main" id="main" src="<%=basePath%>blanko.html" frameborder="0" noresize scrolling="yes"><frame name="loading" id="loading" src="<%=basePath%>loading.jsp" frameborder="0" noresize><frame name="blank" id="blank" src="<%=basePath%>blanko.html" frameborder="0" noresize></frameset>');
			}
		</script>
		<noscript>
			Duroty System is the Gmail Open Source.<br/><br/>
			Duroty is a service of personal feeds, management of the mail, management of bookmarks and next will have also management of electronic agenda and indexing of files.<br/><br/>
			JavaScript must be enabled in order for you to use Duroty. However, it seems JavaScript is either disabled or not supported by your browser.
		</noscript>
	</head>
</html>
