
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
				<span class="titleActive" style="padding-left: 10px;">
					<fmt:message key="mail.filters" />
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/identities.drt"><fmt:message key="mail.identities" /></a>
				</span>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%" colspan="2">
							<h3>
								<fmt:message key="mail.filter.title" />
							</h3>
						<td>
					</tr>
					<tr>
						<td width="100%" colspan="2"><hr style="border: solid 1px #38afe2;"/></td>
					</tr>
					<c:forEach var="filter" items="${filters}">
						<tr>
							<td width="75%" align="left" style="font-size: 13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.filter.matches" />: 
								<b>
									<c:if test="${!empty filter.from}">
										<fmt:message key="mail.from" />:<c:out value="${filter.from}" />&nbsp;
									</c:if>
									<c:if test="${!empty filter.to}">
										<fmt:message key="mail.to" />:<c:out value="${filter.to}" />&nbsp;
									</c:if>
									<c:if test="${!empty filter.subject}">
										<fmt:message key="mail.subject" />:<c:out value="${filter.subject}" />&nbsp;
									</c:if>
									<c:if test="${!empty filter.hasWords}">
										<fmt:message key="mail.hasWords" />:<c:out value="${filter.hasWords}" />&nbsp;
									</c:if>
									<c:if test="${!empty filter.doesntHaveWords}">
										<fmt:message key="mail.doesntHaveWords" />:<c:out value="${filter.doesntHaveWords}" />&nbsp;
									</c:if>
									<c:if test="${filter.hasAttachment}">
										<fmt:message key="mail.hasAttachment" />:<c:out value="${filter.hasAttachment}" />&nbsp;
									</c:if>
									<c:if test="${filter.archive}">
										<fmt:message key="mail.filter.action.skipInbox" />:<c:out value="${filter.archive}" />&nbsp;
									</c:if>
									<c:if test="${filter.important}">
										<fmt:message key="mail.filter.action.star" />:<c:out value="${filter.important}" />&nbsp;
									</c:if>
									<c:if test="${!empty filter.forward}">
										<fmt:message key="mail.filter.action.forward" />:<c:out value="${filter.forward}" />&nbsp;
									</c:if>
									<c:if test="${filter.trash}">
										<fmt:message key="mail.filter.action.trash" />:<c:out value="${filter.trash}" />&nbsp;
									</c:if>
								</b>
								<br/>
								<fmt:message key="mail.filter.label" /> "<b><c:out value="${filter.label.name}" /></b>"
							</td>
							<td width="25%" align="right">
								<a href="<%=basePath%>mail/preferences/formUpdateFilter.drt?idint=<c:out value="${filter.idint}" />"><fmt:message key="mail.filter.edit" /></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:deleteFilter(<c:out value="${filter.idint}" />);"><fmt:message key="mail.filter.delete" /></a>
							</td>
						</tr>
						<tr>
							<td width="100%" colspan="2"><hr style="border: solid 1px #38afe2;"/></td>
						</tr>
					</c:forEach>
					<tr>
						<td width="100%" colspan="2" align="center">
							<h3><a href="<%=basePath%>mail/preferences/formFilter.drt"><fmt:message key="mail.filter.create" /></a></h3>
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
	function deleteFilter(idint) {
		if (idint && confirm('<fmt:message key="mail.filter.confirmDelete" />')) {
			document.location.href = "<%=basePath%>mail/preferences/deleteFilter.drt?idint=" + idint;
		}
	}
</script>
<!-- FI CONTENTS -->
