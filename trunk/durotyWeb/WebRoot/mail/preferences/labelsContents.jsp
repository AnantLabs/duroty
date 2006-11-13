
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
				<span class="titleActive" style="padding-left: 10px;">
					<fmt:message key="mail.labels" />
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/filters.drt"><fmt:message key="mail.filters" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/identities.drt"><fmt:message key="mail.identities" /></a>
				</span>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach var="label" items="${labels}">
						<tr>
							<td width="75%" align="left" style="13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<h3><c:out value="${label.name}" /></h3>
								<div id="divFormUpdateLabel<c:out value="${label.idint}" />" class="hide">
									<html:form action="mail/preferences/updateLabel.drt" method="post" enctype="multipart/form-data">
										<html:hidden property="idint" value="${label.idint}" />
										<html:text property="name" size="20" maxlength="100" value="${label.name}"/>
										&nbsp;
										<html:submit><fmt:message key="general.send" /></html:submit>
									</html:form>
								</div>
							</td>
							<td width="25%" align="right">
								<a href="javascript:showHideElement('divFormUpdateLabel<c:out value="${label.idint}" />')"><fmt:message key="mail.label.rename" /></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:deleteLabel(<c:out value="${label.idint}" />);"><fmt:message key="mail.label.delete" /></a>
							</td>
						</tr>
						<tr>
							<td width="100%" colspan="2"><hr style="border: solid 1px #38afe2;"/></td>
						</tr>
					</c:forEach>
					<tr>
						<td width="100%" colspan="2" align="center">
							<br/>
							<form action="<%=basePath%>mail/preferences/formLabel.drt" method="post" style="margin: 0px; padding: 0px;" enctype="multipart/form-data">
								<input type="text" size="20" name="label" maxlength="100" style="width: 50%;" value="<fmt:message key="mail.label.create" />" onfocus="clearLabel(this, '');" onblur="clearLabel(this, '<fmt:message key="mail.label.create" />');"/>
							</form>
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
	function deleteLabel(idint) {
		if (idint && confirm('<fmt:message key="mail.label.confirmDelete" />')) {
			document.location.href = "<%=basePath%>mail/preferences/deleteLabel.drt?idint=" + idint;
		}
	}
	
	function clearLabel(text, value) {
		text.value = value;
	}
</script>
<!-- FI CONTENTS -->
