
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

<div id="divInfo">
</div>
<br />
<div id="idFormSettings">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formSettings">
		<tr>
			<td width="100%" class="title">
				<span style="padding-left: 10px;"> <a href="<%=basePath%>mail/preferences/settings.drt"><fmt:message key="mail.settings" /></a> </span> <span style="padding-left: 10px;"> <a href="<%=basePath%>mail/preferences/contacts.drt"><fmt:message
							key="mail.contacts" /></a> </span> <span style="padding-left: 10px;"> <a href="<%=basePath%>mail/preferences/labels.drt"><fmt:message key="mail.labels" /></a> </span> <span class="titleActive" style="padding-left: 10px;"> <fmt:message
						key="mail.filters" /> </span> <span style="padding-left: 10px;"> <a href="<%=basePath%>mail/preferences/identities.drt"><fmt:message key="mail.identities" /></a> </span>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<!-- CONTENTS -->
				<div id="idFormFilter">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formFilter">
						<tr>
							<td width="100%">
								<h3>
									<fmt:message key="mail.filter.update.title" />
								</h3>
							</td>
						</tr>
						<tr>
							<td width="100%" ><hr style="border: solid 1px #38afe2;"/></td>
						</tr>
						<tr>
							<td width="100%">
								<html:form action="mail/preferences/updateFilter.drt" method="post" onsubmit="return true;" style="padding: 0px; margin: 0px;" enctype="multipart/form-data">
									<html:hidden property="idint" />
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%">
												<b><fmt:message key="mail.label" />:</b>
												&nbsp;&nbsp;
												<html:select property="label">
													<c:forEach items="${labels}" var="item">
														<html:option value="${item.idint}"><c:out value="${item.name}" /></html:option>
													</c:forEach>
												</html:select>
											</td>
										</tr>
										<tr>
											<td width="100%">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td width="100%">
												<div id="formFilter">
													<fmt:message key="mail.label.infoFilter" />
													<br />
													<br />
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
																<input type="button" value="<fmt:message key="mail.filter.action.select" />" onclick="javascript:nextStep();" />
															</td>
														</tr>
													</table>
												</div>
												<div id="formFilterActions" class="hide">
													<fmt:message key="mail.filter.actions" />
													<br />
													<br />
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
													</table>
													<br />
													<br />
												</div>
											</td>
										</tr>
										<tr>
											<td width="100%" style="text-align: center;">
												<html:submit styleId="create" styleClass="hide">
													<fmt:message key="mail.filter.update" />
												</html:submit>
											</td>
										</tr>
									</table>
								</html:form>
							</td>
						</tr>
					</table>
				</div>
				<!-- FI CONTENTS -->
			</td>
		</tr>
	</table>
</div>
<script language="javascript" type="text/javascript">
	function nextStep() {	
		if ((document.formUpdateFilter.from.value == null || document.formUpdateFilter.from.value == "") && 
			(document.formUpdateFilter.to.value == null || document.formUpdateFilter.to.value == "") && 
			(document.formUpdateFilter.subject.value == null || document.formUpdateFilter.subject.value == "") && 
			(document.formUpdateFilter.hasWords.value == null || document.formUpdateFilter.hasWords.value == "") && 
			(document.formUpdateFilter.doesntHaveWords.value == null || document.formUpdateFilter.doesntHaveWords.value == "")) {
			alert('<fmt:message key="mail.label.chooseCriterion" />');
			document.formUpdateFilter.from.focus();
		} else {
			showHideElement('formFilterActions');
			showHideElement('create');
		}
	}
</script>