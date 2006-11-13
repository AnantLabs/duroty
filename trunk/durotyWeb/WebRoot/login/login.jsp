<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.duroty.cookie.CookieManager" %>

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
		<title>Duroty System</title>
		<meta http-equiv="keywords" content="duroty,gmail,lucene,jboss,mail,email,j2ee,pop3,imap,java,webmail,network" />
		<meta http-equiv="description" content="Duroty System is the Gmail Open Source. Duroty is a service of personal feeds, management of the mail, management of bookmarks and next will have also management of electronic agenda and indexing of files." />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />		
		
		<!-- The AJAX Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>
		
		<!-- The Utils Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/utils.js"></script>

		<link href="<%=basePath%>style/login_style.css" rel="stylesheet" type="text/css" />
		
		<script language="javascript" type="text/javascript">
			try {
				if (parent && parent.document.body.rows) {
					parent.document.body.rows = "100%,*,*";
				}
			} catch (e) {
				top.location.href = "<%=basePath%>index.jsp";
			}
		
			function setLocale(_locale) {
				window.location.href = "<%=basePath%>language/setLocale.drt?language=" + _locale;
			}
			
			function autoLogin() {
				var username = false;
				var password = false;
				<%
				Cookie username = CookieManager.getCookie("c_username", request);
				Cookie password = CookieManager.getCookie("c_password", request);		
				if (username != null && username.getValue() != null) {
					%>
					username = "<%=username.getValue()%>";
					<%
				}
				if (password != null && password.getValue() != null) {
					%>
					password = "<%=password.getValue()%>";
					<%
				}
				%>						
				if (username && password) {
					document.fsecuritycheck.j_username.value = username;
					document.fsecuritycheck.j_password.value = password;
					
					document.fsecuritycheck.submit();
				}
			}
		</script>
	</head>
	<body onload='setTimeout("autoLogin()", 500);'>
		<div id="mesh">
			<table width="100%" align="center" border="0" cellspacing="0" cellpadding="0" class="mesh">
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="headLeft">
									<img src="<%=basePath%>images/duroty_anim.gif" border="0" />
								</td>
								<td width="100%" class="headCenter">
									<fmt:message key="general.welcome" />
								</td>
							</tr>
							<tr>
								<td height="2" colspan="2"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="contents" colspan="2">
									<!-- BODY -->
									<c:set var="dlanguage" value="en" />
									<c:choose>
										<c:when test="${!empty sessionScope['org.apache.struts.action.LOCALE']}">
											<c:set var="dlanguage" value="${sessionScope['org.apache.struts.action.LOCALE']}" />
										</c:when>
									</c:choose>
									<c:import url="/login/message_${dlanguage}.jsp" />									
									<!-- FI BODY -->
								</td>
								<td class="right" nowrap="nowrap">
									<!-- RIGHT -->
									<br />
									<br />									
									<table width="100%" border="0" cellspacing="0" cellpadding="3" style="border: 1px solid #d5ad3f;">
										<tr>
											<td width="100%">
												<form method="post" action="j_security_check" name="fsecuritycheck" style="margin: 0px; padding: 0px;">
												<table width="100%" border="0" cellspacing="0" cellpadding="4" style="background-color: #eeeeee;">
														<tr>
															<td width="100%" style="text-align: center; font-weight: bold;" colspan="2">
																<fmt:message key="general.access" />
															</td>
														</tr>
														<tr>
															<td width="100%" height="4" colspan="2"></td>
														</tr>
														<tr>
															<td style="text-align: right;" nowrap="nowrap">
																<fmt:message key="general.username" />:
															</td>
															<td width="100%" style="text-align: left;">
																<input type="text" name="j_username" size="17" maxlength="64" style="width: 95%;" />
															</td>
														</tr>
														<tr>
															<td style="text-align: right;" nowrap="nowrap">
																<fmt:message key="general.password" />:
															</td>
															<td width="100%" style="text-align: left;">
																<input type="password" name="j_password" size="17" maxlength="64" style="width: 95%;" />
															</td>
														</tr>													
													<tr>
														<td style="text-align: right;" nowrap="nowrap">
															<input type="checkbox" name="j_remember" value="true" id="remember" />
														</td>
														<td width="100%" style="text-align: left;">
															<fmt:message key="general.remember" />
														</td>
													</tr>
													<tr>
														<td style="text-align: right;" nowrap="nowrap">
														</td>
														<td width="100%" style="text-align: left;">
															<input type="submit" style="background-color: #fbcc4b;" name="enviar" value="<fmt:message key="general.login" />" />
															<div class="loading" id="loading"><fmt:message key="general.loading" /></div>
														</td>
													</tr>
													<tr>
														<td style="text-align: right;" nowrap="nowrap">
														</td>
														<td width="100%" style="text-align: left;">
															<select name="j_language" size="1" onchange="javascript:setLocale(this.options[this.selectedIndex].value);">
																<option value="0"><fmt:message key="general.language.choose" /></option>
																<c:choose>
																	<c:when test="${sessionScope['org.apache.struts.action.LOCALE'] == 'en'}">
																		<option value="en" selected="selected">English</option>
																		<option value="ca">Català</option>
																		<option value="es">Castellano</option>
																	</c:when>
																	<c:when test="${sessionScope['org.apache.struts.action.LOCALE'] == 'es'}">
																		<option value="en" selected="selected">English</option>
																		<option value="ca">Català</option>
																		<option value="es" selected="selected">Castellano</option>
																	</c:when>
																	<c:when test="${sessionScope['org.apache.struts.action.LOCALE'] == 'ca'}">
																		<option value="en">English</option>
																		<option value="ca" selected="selected">Català</option>
																		<option value="es">Castellano</option>
																	</c:when>
																	<c:otherwise>
																		<option value="en" selected="selected">English</option>
																		<option value="ca">Català</option>
																		<option value="es">Castellano</option>
																	</c:otherwise>
																</c:choose>																
															</select>
														</td>
													</tr>
													<tr>
														<td width="100%" height="4" colspan="2"></td>
													</tr>
													<tr>
														<td width="100%" style="text-align: center; padding-right: 5px; padding-left: 5px;" colspan="2">
															<a href="<%=basePath%>login/forgotPassword.jsp"><fmt:message key="general.forgot.password" /></a>
														</td>
													</tr>
													<tr>
														<td width="100%" style="text-align: center; padding-right: 5px; padding-left: 5px;" colspan="2">
															<a href="<%=basePath%>login/register.jsp" target="_top"><fmt:message key="general.register" /></a>
														</td>
													</tr>
													<tr>
														<td width="100%" height="4" colspan="2"></td>
													</tr>													
												</table>
												</form>
											</td>
										</tr>
									</table>
									<!-- FI RIGHT -->
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="foot">
						<!-- FOOT -->
						<!-- FI FOOT -->
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
