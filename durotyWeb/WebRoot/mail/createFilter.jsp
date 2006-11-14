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

<html:form action="mail/insertFilter.drt" method="post" onsubmit="showLoading();return true;" style="padding: 0px; margin: 0px;" target="util" enctype="multipart/form-data">
	<div id="divCreateFilter" class="hide" style="width: 100%;">
		<br />
		<table width="100%" border="0" cellspacing="0" cellpadding="2" style="border: 4px solid #008000;" align="center">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="4" style="background-color: #baefba;">
						<tr>
							<td width="100%">
								<fmt:message key="mail.label.infoFilter" />
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td width="100%">
								<table border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.from" />&nbsp;</b>
										</td>
										<td width="50%" style="text-align: left;">
											<html:text property="from" size="20" style="width: 90%;" />
										</td>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.hasWords" />:&nbsp;</b>
										</td>
										<td width="50%">
											<html:text property="hasWords" size="20" style="width: 90%;" />
										</td>
									</tr>
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.to" /> (to + cc):&nbsp;</b>
										</td>
										<td width="50%" style="text-align: left;">
											<html:text property="to" size="20" style="width: 90%;" />
										</td>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.doesntHaveWords" />&nbsp;</b>
										</td>
										<td width="50%">
											<html:text property="doesntHaveWords" size="20" style="width: 90%;" />
										</td>
									</tr>
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.subject" />&nbsp;</b>
										</td>
										<td width="50%" style="text-align: left;">
											<html:text property="subject" size="20" style="width: 90%;" />
										</td>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.hasAttachment" />&nbsp;</b>
										</td>
										<td width="50%">
											<html:checkbox property="hasAttachment" />
										</td>
									</tr>
								</table>
								<br />
								<table width="100%" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="100%" style="text-align: center;">
											<fmt:message key="mail.filter.operator" />
											&nbsp;
											<html:checkbox property="operator" />
										</td>
									</tr>
								</table>
								<br />
								<table width="100%" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="100%" style="text-align: center;">
											<input type="button" class="BUTTON" value="<fmt:message key="mail.filter.test" />" onclick="Mail.displayLocation('testFilter:0*0!0', null);" />
											&nbsp;&nbsp;
											<input type="button" class="BUTTON" value="<fmt:message key="mail.filter.action.next" />" onclick="javascript:nextStep();" />
											&nbsp;&nbsp;
											<input type="button" class="BUTTON" value="<fmt:message key="mail.filter.hide" />" onclick="hideElement('divCreateFilter');" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<div id="divFilterActions" class="hide">
		<br />
		<table width="100%" border="0" cellspacing="0" cellpadding="2" style="border: 4px solid #008000;" align="center">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="4" style="background-color: #baefba;">
						<tr>
							<td width="100%">
								<span onclick="showHideElement('divCreateFilter');hideElement('divFilterActions');" style="cursor: pointer; color: blue; text-decoration: underline; font-weight: bold;"><fmt:message key="mail.button.back" /></span>
								&nbsp;&nbsp;
								<fmt:message key="mail.filter.actions" />
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td width="100%">
								<table border="0" cellspacing="2" cellpadding="2" align="center">
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<html:checkbox property="archive" />

										</td>
										<td style="text-align: left;">
											<b><fmt:message key="mail.filter.action.skipInbox" />&nbsp;</b>
										</td>
									</tr>
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<html:checkbox property="important" />

										</td>
										<td style="text-align: left;">
											<b><fmt:message key="mail.filter.action.star" />&nbsp;</b>
										</td>
									</tr>
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.filter.action.forward" />&nbsp;</b>
										</td>
										<td style="text-align: left;">
											<html:text property="forward" size="20" style="width: 90%;" />
										</td>
									</tr>
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<html:checkbox property="trash" />
										</td>
										<td style="text-align: left;">
											<b><fmt:message key="mail.filter.action.trash" />&nbsp;</b>
										</td>
									</tr>
									<tr>
										<td style="text-align: right;" nowrap="nowrap">
											<b><fmt:message key="mail.filter.label" />&nbsp;</b>
										</td>
										<td style="text-align: left;" nowrap="nowrap">
											<c:set var="selected" value="selected='selected'" />
											<html:select property="label">												
												<c:forEach items="${labels}" var="item">
													<option value="${item.idint}" <c:out value="${selected}" />><c:out value="${item.name}" /></option>
													<c:set var="selected" value="" />
												</c:forEach>
											</html:select>
											&nbsp;&nbsp;
											<fmt:message key="mail.label.create" />
											&nbsp;
											<html:text property="name" maxlength="100" size="20"/>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="center">
											<html:submit styleClass="BUTTON"><fmt:message key="mail.filter.create" /></html:submit>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</html:form>
<script language="javascript" type="text/javascript">
	function nextStep() {	
		if ((document.formFilter.from.value == null || document.formFilter.from.value == "") && 
			(document.formFilter.to.value == null || document.formFilter.to.value == "") && 
			(document.formFilter.subject.value == null || document.formFilter.subject.value == "") && 
			(document.formFilter.hasWords.value == null || document.formFilter.hasWords.value == "") && 
			(document.formFilter.doesntHaveWords.value == null || document.formFilter.doesntHaveWords.value == "")) {
			alert('<fmt:message key="mail.label.chooseCriterion" />');
			document.formFilter.from.focus();
		} else {
			showHideElement('divFilterActions');
			showHideElement('divCreateFilter');
		}
	}
</script>
