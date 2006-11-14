<%@ page language="java" pageEncoding="UTF-8"%>
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

<html>
	<head>
		<script language="javascript" type="text/javascript">	
			var user = '<c:out value="${user}" />';						
		
			var groups = new Array();
			<c:if test="${!empty groups}">
				<c:forEach items="${groups}" var="group">
					groups['<c:out value="${group.name}" />'] = '<c:out value="${group.emails}" escapeXml="No"/>';
				</c:forEach>
			</c:if>
		</script>

		<!-- The AJAX Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax.js"></script>

		<!-- The AJAX Dynamic List Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/ajax-dynamic-list.js"></script>

		<!-- The Utils Library -->
		<script type="text/javascript" src="<%=basePath%>javascript/utils.js"></script>

		<link href="<%=basePath%>style/mail_invitate_style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<html:form action="mail/invitateToChat.drt" method="post" enctype="multipart/form-data">
			<h1>
				<fmt:message key="chat.invite" />
			</h1>
			<table width="100%" border="0" cellspacing="2" cellpadding="2" class="composeArea">
				<c:if test="${!empty identities}">
					<tr>
						<td nowrap="nowrap" align="right">
							<fmt:message key="mail.from" />
						</td>
						<td width="100%">
							<html:select size="1" property="identity">
								<c:forEach items="${identities}" var="identity">
									<c:set var="selected" value="" />
									<c:if test="${identity.important}">
										<c:set var="selected" value="selected='selected'" />
									</c:if>
									<option value="<c:out value='${identity.idint}' />" <c:out value='${selected}' />>
										<c:out value='${identity.email}' />
										,
										<c:out value='${identity.name}' />
									</option>
								</c:forEach>
							</html:select>
							&nbsp;&nbsp;
							<fmt:message key="mail.priority" />
							<html:select size="1" property="priority">
								<option value="normal" selected="selected">
									<fmt:message key="mail.priority.normal" />
								</option>
								<option value="high">
									<fmt:message key="mail.priority.high" />
								</option>
								<option value="low">
									<fmt:message key="mail.priority.low" />
								</option>
							</html:select>
						</td>
					</tr>
				</c:if>
				<tr>
					<td nowrap="nowrap" align="right" valign="top">
						<fmt:message key="mail.to" />
					</td>
					<td width="100%">
						<textarea name="to" onkeyup="ajax_showOptions('<%=basePath%>mail/suggestContacts.drt', this, 'suggestContactsChat', event);" style="width: 90%; height: 60px;"></textarea>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" colspan="2" align="center" valign="top">
						<br />
						<br />
						<html:submit styleClass="buttonCompose">
							<fmt:message key="general.send" />
						</html:submit>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>
