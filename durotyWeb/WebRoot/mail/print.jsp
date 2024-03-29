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
		<base href="<%=basePath%>">
		<title>Print Message</title>

		<title>Duroty System</title>	
		<meta http-equiv="keywords" content="jordi marqu�s, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />
		
		<link href="<%=basePath%>style/mail_style.css" rel="stylesheet" type="text/css" />

		<script language="javascript" type="text/javascript">
		
			function setContents() {	
				var contentsPrint = document.getElementById("contentsPrint");
				
				var tableReadMessage = opener.document.getElementById("tableReadMessage");
				tableReadMessage = tableReadMessage.cloneNode(true);
				if (contentsPrint && tableReadMessage) {					
					contentsPrint.innerHTML = contentsPrint.innerHTML + tableReadMessage.innerHTML;
				}
			}
		
		</script>
	</head>

	<body onload="setContents();">
		<div id="divPrint" style="text-align: center">
			<br />
			<input type="button" name="print" value="Print Message" onclick="top.print();" />
			<br />
			<br />
		</div>
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader" align="center">
				<tr>
					<td id="contentsPrint" width="100%" class="areaWithPadding">
					</td>
				</tr>
			</table>
		</div>
		<br />
	</body>
</html>
