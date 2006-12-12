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
		<meta http-equiv="keywords" content="jordi marquès, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />

		<!-- The Calendar Library -->
		<style type="text/css">@import url(<%=basePath%>javascript/jscalendar/calendar-blue2.css);</style>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/lang/calendar-<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/calendar-setup.js"></script>

		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>		
		<!-- The Utils Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/utils.js"></script>				
		
		<!-- The Mail Library -->
		<script type="text/javascript" src="<%=basePath%>files/javascript/files.js"></script>
		
		<script type="text/javascript">
			var controlUpload = false;
			
			function submitFileUpload() {				
				showLoading();
			
				if (!controlUpload) {						
					controlUpload = true;
					
					document.fileUpload.action = "<%=basePath%>files/upload.drt";
					document.fileUpload.submit();
					
					controlUpload = false;
				} else {
					alert('<fmt:message key='mail.controlSubmit' />');
				}	
				
				return false;
			}
		</script>
				
		<link href="<%=basePath%>style/files_style.css" rel="stylesheet" type="text/css" />
	</head>
	<body id="twocolumn-left">						
		<script language="JavaScript">			
			if (window == top) {			
				alert('It is not allowed to see the frame individually');
				top.location.href = "<%=basePath%>files/index.jsp";
			}
		</script>		
		<div id="cornerLoading"><fmt:message key="general.loading" /></div>
		
		<c:import url="/files/advancedSearch.jsp" />
		
		<table id="tableHead" border="0" cellpadding="0" cellspacing="0" width="100%">
			<thead style="background-color: #ede9e3;">
				<td nowrap="nowrap" width="100%" style="border-top: 1px solid #cac1b3; border-left: 1px solid #cac1b3; padding-left: 10px; padding-right: 10px;">
					<fmt:message key="general.select" />:&nbsp;<span class="link" onclick="javascript:Files.selectAll();"><fmt:message key="mail.selectAll" /></span>, <span class="link" onclick="javascript:showHideElement('tableUpload');Files.attachFile('attachFile', true);return false;"><fmt:message key='files.upload' /></span>, <span style="cursor: pointer; color: blue; font-size: 100%; text-decoration: underline;" onclick="javascript:Files.getFiles(-1, -1, -1, -1, -2, -1);return false;"><fmt:message key='mail.button.refresh' /></span>&nbsp;&nbsp;&nbsp;<span id="pagination"></span>
				</td>
				<td nowrap="nowrap" style="border-top: 1px solid #cac1b3; border-right: 1px solid #cac1b3; padding-top: 2px; padding-bottom: 2px;">
					<span id="listLabels"></span>&nbsp;<button id="buttonFolderFiles" style="font-size: 95%; white-space: nowrap;" onclick="javascript:Files.viewFolderFiles();return false;"><fmt:message key='files.viewFiles' /></button>&nbsp;<button id="buttonAllFiles" style="font-size: 95%; white-space: nowrap;" onclick="javascript:Files.viewAll();return false;"><fmt:message key='files.viewAll' /></button>
				</td>
			</thead>
		</table>
		
		<table id="tableUpload" border="0" cellpadding="0" cellspacing="0" width="100%" class="hide">
			<thead style="background-color: #ede9e3;">
				<td nowrap="nowrap" style="border-top: 1px solid #cac1b3; border-left: 1px solid #cac1b3; padding-left: 10px; padding-right: 10px; padding-top: 5px; padding-bottom: 5px;">
					<form action="" name="fileUpload" method="post" enctype="multipart/form-data" target="util">
						<div id="divInfo"></div>
						<div id="attachFile"></div>
					</form>
				</td>
				<td valign="bottom" nowrap="nowrap" width="100%" style="border-top: 1px solid #cac1b3; border-right: 1px solid #cac1b3; padding: 5px;">
					<button id="buttonUploadAnotherFiles" style="font-size: 95%; white-space: nowrap;" onclick="javascript:Files.attachFile('attachFile', false);return false;"><fmt:message key='files.upload.another' /></button>&nbsp;<button id="buttonSendFiles" style="font-size: 95%; white-space: nowrap;" onclick="javascript:submitFileUpload();return false;"><fmt:message key='files.upload.send' /></button>&nbsp;<button id="buttonHideUpload" style="font-size: 95%; white-space: nowrap;" onclick="javascript:showHideElement('tableUpload');return false;"><fmt:message key='files.upload.hide' /></button>
				</td>
			</thead>
		</table>
				
		
		<div id="files"></div>
		<div id="searchFiles"></div>
		
		<iframe src="" id="util" name="util" width="0" frameborder="0"></iframe>
		<iframe src="<%=basePath%>files/session.jsp" id="session" name="session" width="0" frameborder="0"></iframe>		
	</body>
</html>
