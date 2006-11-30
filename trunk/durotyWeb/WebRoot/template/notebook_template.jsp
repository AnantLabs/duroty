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
		<meta http-equiv="keywords" content="jordi marquès, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
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
				
		<!-- The notebook Library -->
		<script type="text/javascript" src="<%=basePath%>notebook/javascript/notebook.js"></script>

		<link href="<%=basePath%>style/notebook_style.css" rel="stylesheet" type="text/css" />
	</head>
	<body id="twocolumn-left">
		<script language="JavaScript">
			if (window == top) {
				alert('It is not allowed to see the frame individually');
				top.location.href = "<%=basePath%>notebook/index.jsp";
			}
		</script>
		<div id="cornerLoading"><fmt:message key="general.loading" /></div>
		
		<div id="container">
			<div class="wrapper">
				<div id="header">
					<div class="wrapper">
						<div id="page-title">
							<div id="g_title">
								<div id="navigation_right" style="float: right; width: 50%; height: auto; margin-left: 0%; margin-right: 0%;">
									<!-- toolbar -->
									<tiles:insert name="toolbar" />
									<!-- fi toolbar-->
								</div>
								<div id="navigation_left" style="margin: 0pt 50% 0pt 0pt;">		
									<form action="" method="post" onsubmit="javascript:Notebook.displayLocation('search:0*0!0', null);return false;" enctype="multipart/form-data">
										<table border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td nowrap="nowrap" style="padding-left: 5px; padding-top: 5px;">
													<img src="<%=basePath%>images/duroty.gif" border="0" align="middle" />
												</td>
												<td nowrap="nowrap" align="right">
													<input type="text" size="25" name="token" id="token" class="BUTTON" />&nbsp;
												</td>
												<td nowrap="nowrap" align="left" style="padding-left: 3px;">
													<input type="button" name="btnSearch" class="BUTTON" value="<fmt:message key="general.search" />" onclick="javascript:Notebook.displayLocation('search:0*0!0', null);" />
												</td>
												<td nowrap="nowrap" style="font-size: 10px; padding-left: 5px;">
													<a href="javascript:openwin('<%=basePath%>bookmark/help_<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />.html', 'Help', 700, 600);">- <fmt:message key="general.help" /></a>
													<br/>
													<a href="javascript:(function(){var a=window,b=document,c=encodeURIComponent,d=a.open(%22<%=basePath%>bookmark/bookmarklet.drt?&url=%22+c(b.location)+%22&title=%22+c(b.title),%22bkmk_popup%22,%22left=%22+((a.screenX||a.screenLeft)+10)+%22,top=%22+((a.screenY||a.screenTop)+10)+%22,height=500px,width=700px,resizable=1,alwaysRaised=1%22);a.setTimeout(function(){d.focus()},300)})();" onclick="alert('<fmt:message key="bookmarklet.instructions" />');return false;">- Bookmarklet</a>
												</td>
											</tr>
										</table>										
									</form>
								</div>								
							</div>
						</div>
						<div style="clear: both;"></div>
						<div id="g_description">
							<div id="navigation_right" style="float: right; height: auto; margin-left: 0%; margin-right: 15px;">
								<!-- NOW -->
								<tiles:insert name="date" />
								<!-- FI NOW -->
							</div>
							<div id="navigation_left" style="margin: 0pt 0% 0pt 0pt;">		
								<div id="divInfo" class="hide"></div>
							</div>	
							
						</div>
						<div style="clear: both;"></div>
					</div>
				</div>
				<!-- /wrapper -->
				<!-- /header -->
				<div id="sidebar">
					<div class="wrapper">
						<div class="links">
							<div class="wrapper">
								<div id="g_sidebar">
									<!-- LEFT -->
									<tiles:insert name="left" />
									<!-- FI LEFT -->
								</div>
							</div>
							<div style="clear: both;"></div>
						</div>
						<!-- /wrapper -->
						<!-- /links -->
					</div>
				</div>
				<!-- /wrapper -->
				<!-- /sidebar -->
				<div id="main-content">
					<div class="wrapper">
						<div class="content-item">
							<div id="g_body">
								<!-- BODY -->
								<tiles:insert name="contents" />
								<!-- FI BODY -->
								<!-- FOOT -->
								<div class="foot">
								<tiles:insert name="foot" />
								</div>
								<!-- FI FOOT -->
							</div>
						</div>
						<div style="clear: both;"></div>
					</div>
				</div>
				<!-- /wrapper -->
				<!-- /main-content -->				
				<div id="footer">
					<div class="wrapper">
						<div id="g_footer">							
						</div>
						<div style="clear: both;"></div>
					</div>
				</div>
				<!-- /wrapper -->
				<!-- /footer -->
			</div>
		</div>
		<!-- /wrapper -->
		<!-- /container -->

		<div id="extraDiv1"><span></span></div>
		<div id="extraDiv2"><span></span></div>
		<div id="extraDiv3"><span></span></div>
		<div id="extraDiv4"><span></span></div>
		<div id="extraDiv5"><span></span></div>
		<div id="extraDiv6"><span></span></div>
		
		<iframe src="" id="util" name="util" width="0" frameborder="0"></iframe>
		<iframe src="<%=basePath%>notebook/session.jsp" id="session" name="session" width="0" frameborder="0"></iframe>
	</body>
</html>
