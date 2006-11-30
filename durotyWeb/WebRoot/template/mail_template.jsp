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
			String basePath1 = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";		
basePath = path + "/";					
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
		<base href="<%=basePath1%>" />
		<title><tiles:insert name="title" /></title>
		<meta http-equiv="keywords" content="jordi marquès, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />
		
		
		<script language="javascript" type="text/javascript">	
			var user = '<c:out value="${user}" />';
				
			<c:choose>
				<c:when test="${!empty preferences && preferences.htmlMessage}">
					htmlMessage = true;
				</c:when>
				<c:otherwise>
					var htmlMessage = false;
				</c:otherwise>
			</c:choose>
		
			var groups = new Array();
			<c:if test="${!empty groups}">
				<c:forEach items="${groups}" var="group">
					groups['<c:out value="${group.name}" />'] = '<c:out value="${group.emails}" escapeXml="No"/>';
				</c:forEach>
			</c:if>	
		</script>

		<!-- The X Cross-Browser DHTML Library -->
		<script language="JavaScript" src="<%=basePath%>javascript/x/x_core.js"></script>
		<script language="JavaScript" src="<%=basePath%>javascript/x/x_dom.js"></script>
		<script language="JavaScript" src="<%=basePath%>javascript/x/x_event.js"></script>
		<!-- Load the DhtmlHistory and HistoryStorage APIs -->
		<script type="text/javascript" src="<%=basePath%>javascript/dhtmlHistory.js"></script>
		<!-- The Sarissa Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/sarissa/sarissa.js"></script>
		<!-- The AJAX Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>
		<!-- The AJAX Dynamic List Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax-dynamic-list.js"></script>
		<!-- The Utils Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/utils.js"></script>		
		
		<!-- The Calendar Library -->
		<style type="text/css">@import url(<%=basePath%>javascript/jscalendar/calendar-blue2.css);</style>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/lang/calendar-<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/jscalendar/calendar-setup.js"></script>

		<!-- tinyMCE -->
		<script language="javascript" type="text/javascript" src="<%=basePath%>javascript/tiny_mce/tiny_mce_src.js"></script>
		
		<script language="javascript" type="text/javascript">
			tinyMCE.init({
				mode : "exact",
				theme : "advanced",
				/*plugins : "spellchecker",*/
				/*spellchecker_languages : "+English=en,Spanish=es,Catalan=ca",*/
				language : "<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />",
				content_css : "<%=basePath%>style/mail_htmlarea_full.css",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				theme_advanced_path_location : "bottom",
				/*theme_advanced_buttons3_add : "spellchecker",*/
				add_form_submit_trigger : false,
				submit_patch : false,
				debug : false
			});
		</script>
		<!-- /tinyMCE -->
		<c:if test="${!empty preferences}">
			<script language="javascript" type="text/javascript">
				var messagesByPage = <c:out value="${preferences.messagesByPage}" />;
				var signature = '<c:out value="${preferences.signature}" escapeXml="No"/>';
			</script>
		</c:if>
		
		<script src="<%=basePath%>javascript/flash.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/prototype.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/effects.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/window.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/chat_room.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/md5.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/xhconn.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/json.js" type="text/javascript"></script>		
		<script src="<%=basePath%>javascript/chat/obj_ajax_im.js" type="text/javascript"></script>
		<script src="<%=basePath%>javascript/chat/initialize.js" type="text/javascript"></script>
		
		<!-- The Mail Library -->
		<script type="text/javascript" src="<%=basePath%>mail/javascript/mail.js"></script>				
		
		<link href="<%=basePath%>style/default.css" rel="stylesheet" type="text/css" />
		<link href="<%=basePath%>style/alphacube.css" rel="stylesheet" type="text/css" />
		<link href="<%=basePath%>style/ajax_im.css" rel="stylesheet" type="text/css" />
		<link href="<%=basePath%>style/windows.css" rel="stylesheet" type="text/css" />
		<link href="<%=basePath%>style/mail_style.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript" src="<%=basePath%>javascript/googiespell/AJS.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/googiespell/googiespell.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/googiespell/cookiesupport.js"></script>
	</head>
	<body id="twocolumn-left">						
		<script language="JavaScript">			
			if (window == top) {			
				alert('It is not allowed to see the frame individually');
				top.location.href = "<%=basePath%>mail/index.jsp";
			}
		</script>		
		<div id="cornerLoading"><fmt:message key="general.loading" /></div>		
		<div class="hide">
			<form action="<%=basePath%>mail/help_ca.html" name="formAux" method="post" target="util" enctype="multipart/form-data">
				<input type="hidden" name="aux" value="" />
			</form>
		</div>				
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
									<form action="" method="post" onsubmit="javascript:Mail.displayLocation('simpleSearch:0*0!0', null);return false;" style="margin: 0px; padding: 0px;" enctype="multipart/form-data">
										<table border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td nowrap="nowrap" style="padding-left: 5px; padding-top: 5px;">
													<img src="<%=basePath%>images/duroty.gif" border="0" align="middle" />
												</td>
												<td nowrap="nowrap" align="right" style="font-size: 12px;">
													<input type="text" size="25" name="token" id="token" class="BUTTON" />
													<br/>
													<fmt:message key="mail.search.excludeTrash" />&nbsp;
												</td>
												<td nowrap="nowrap" align="left" style="padding-left: 3px;">
													<input type="button" name="btnSearch" class="BUTTON" value="<fmt:message key="general.search" />" onclick="javascript:Mail.displayLocation('simpleSearch:0*0!0', null);" style="margin-bottom: 3px;"/>
													<br/>
													<input type="checkbox" value="true" id="excludeTrash"/>
												</td>
												<td nowrap="nowrap" style="font-size: 10px; padding-left: 3px;">
													<a href="javascript:openwin('<%=basePath%>mail/help_<c:out value='${sessionScope["org.apache.struts.action.LOCALE"]}' />.html', 'Help', 700, 600);">- <fmt:message key="general.help" /></a>
													<br />
													<span onclick="showHideElement('divAdvancedSearch');" style="cursor: pointer; color: blue; text-decoration: underline;">- <fmt:message key="general.advancedSearch" /></span>
													<br />
													<span onclick="showHideElement('divCreateFilter');hideElement('divFilterActions');" style="cursor: pointer; color: blue; text-decoration: underline;">- <fmt:message key="mail.filter.create" /></span>
												</td>
											</tr>
										</table>										
									</form>
								</div>								
							</div>
							<!-- advancedSearch -->
							<tiles:insert name="advancedSearch" />
							<tiles:insert name="createFilter" />
							<!-- fi advancedSearch -->
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
									<p>&nbsp;</p>
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
		
		<div id="sound"></div>
		<iframe src="" id="util" name="util" width="0" frameborder="0"></iframe>
		<iframe src="<%=basePath%>mail/session.jsp" id="session" name="session" width="0" frameborder="0"></iframe>		
	</body>
</html>
