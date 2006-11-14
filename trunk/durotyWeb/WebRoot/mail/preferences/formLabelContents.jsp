
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
<div id="idFormLabel">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formLabel">
		<tr>
			<td width="100%" class="title">
				<fmt:message key="mail.labelSettins" />
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<html:form action="mail/preferences/insertLabel.drt" method="post" onsubmit="return true;" style="padding: 0px; margin: 0px;" enctype="multipart/form-data">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="100%">
								<b><fmt:message key="mail.label.name" />:</b> &nbsp;&nbsp;
								<html:text property="name" size="50" />
								&nbsp;&nbsp;
								<html:submit>
									<fmt:message key="mail.label.create" />
								</html:submit>
								<div id="selectFilter" class="show">
									<fmt:message key="mail.label.info" />
									&nbsp;&nbsp;
									<input type="button" value="<fmt:message key="mail.label.addFilter" />" onclick="javascript:showHideElement('selectFilter');showHideElement('formFilter');" />
								</div>
							</td>
						</tr>
						<tr>
							<td width="100%">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td width="100%">
								<div id="formFilter" class="hide">
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
									<fmt:message key="mail.label.create" />
								</html:submit>
							</td>
						</tr>
					</table>
				</html:form>
			</td>
		</tr>
		<tr>
			<td width="100%" class="title">
			</td>
		</tr>
	</table>
</div>
<script language="javascript" type="text/javascript">
	function nextStep() {	
		if ((document.formLabel.from.value == null || document.formLabel.from.value == "") && 
			(document.formLabel.to.value == null || document.formLabel.to.value == "") && 
			(document.formLabel.subject.value == null || document.formLabel.subject.value == "") && 
			(document.formLabel.hasWords.value == null || document.formLabel.hasWords.value == "") && 
			(document.formLabel.doesntHaveWords.value == null || document.formLabel.doesntHaveWords.value == "")) {
			alert('<fmt:message key="mail.label.chooseCriterion" />');
			document.formLabel.from.focus();
		} else {
			showHideElement('formFilterActions');
			showHideElement('create');
		}
	}
</script>
<!-- FI CONTENTS -->
