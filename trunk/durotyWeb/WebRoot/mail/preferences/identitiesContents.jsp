
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

			%>

<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- CONTENTS -->
<div id="divInfo">
</div>
<br/>
<div id="idFormSettings">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formSettings">
		<tr>
			<td width="100%" class="title">
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/settings.drt"><fmt:message key="mail.settings" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/contacts.drt"><fmt:message key="mail.contacts" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/labels.drt"><fmt:message key="mail.labels" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/filters.drt"><fmt:message key="mail.filters" /></a>
				</span>
				<span class="titleActive" style="padding-left: 10px; padding-right: 10px;">
					<fmt:message key="mail.identities" />
				</span>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%" colspan="2">
							<h3>
								<fmt:message key="mail.identities.title" />
							</h3>
						<td>
					</tr>
					<tr>
						<td width="100%" colspan="2"><hr style="border: solid 1px #38afe2;"/></td>
					</tr>
					<c:forEach var="identity" items="${identities}">
						<tr>
							<td width="60%" align="left" style="font-size: 13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.from" />:
								<b>
									<c:if test="${!empty identity.name}">
										<c:out value="${identity.name}" />&nbsp;
									</c:if>
									<c:if test="${!empty identity.email}">
										&lt;<c:out value="${identity.email}" />&gt;&nbsp;
									</c:if>
								</b>
								<br/>
								<fmt:message key="mail.replyTo" />:
								<b>									
									<c:if test="${!empty identity.replyTo}">
										<c:out value="${identity.replyTo}" />&nbsp;
									</c:if>
								</b>
							</td>
							<td width="40%" align="right">
								<c:if test="${identity.important}">
									<b>default</b>&nbsp;&nbsp;
								</c:if>
								<c:if test="${!identity.important}">
									<a href="<%=basePath%>mail/preferences/setDefaultIdentity.drt?idint=<c:out value="${identity.idint}" />" target="util"><fmt:message key="mail.identity.makeDefault" /></a>&nbsp;&nbsp;
								</c:if>
								|&nbsp;&nbsp;<a href="javascript:deleteIdentity(<c:out value="${identity.idint}" />);"><fmt:message key="mail.identity.delete" /></a>
							</td>
						</tr>
						<tr>
							<td width="100%" colspan="2"><hr style="border: solid 1px #38afe2;"/></td>
						</tr>
					</c:forEach>
					<tr>
						<td width="100%" colspan="2" align="center">
							<h3><a href="<%=basePath%>mail/preferences/formIdentity.drt"><fmt:message key="mail.identity.create" /></a></h3>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%" class="title">
			</td>
		</tr>
	</table>
</div>

<script language="javascript" type="text/javascript">
	function deleteIdentity(idint) {
		if (idint && confirm('<fmt:message key="mail.identity.confirmDelete" />')) {
			document.location.target = "util";
			document.location.href = "<%=basePath%>mail/preferences/deleteIdentity.drt?idint=" + idint;
		}
	}
</script>
<!-- FI CONTENTS -->
