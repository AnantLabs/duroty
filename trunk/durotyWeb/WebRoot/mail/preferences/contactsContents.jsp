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
				<span class="titleActive" style="padding-left: 10px;">
					<fmt:message key="mail.contacts" />
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/labels.drt"><fmt:message key="mail.labels" /></a>
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
					<tr>
						<td width="50%" valign="top">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td valign="top" nowrap="nowrap">
										<input type="text" name="token" value="" id="token"/>
										&nbsp;&nbsp;
										<input type="button" name="button" class="BUTTON" value="<fmt:message key="mail.button.search.contacts" />" onclick="Contacts.displayLocation('searchContacts:0*0!0', null);" />
										&nbsp;&nbsp;
										<input type="button" name="button" class="BUTTON" value="<fmt:message key="mail.button.search.groups" />" onclick="Contacts.displayLocation('searchGroups:0*0!0', null);" />
										&nbsp;&nbsp;
									</td>
									<td>
										<div id="cornerLoading">
											<fmt:message key="general.loading" />
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="50%" valign="top">
						</td>
					</tr>
					<tr>
						<td width="50%"><hr style="border: solid 1px #38afe2;"/></td>
						<td width="50%"><hr style="border: solid 1px #38afe2;"/></td>
					</tr>
					<tr>
						<td width="50%" valign="top">
							<div id="contacts"></div>								
						</td>
						<td width="50%" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="100%" style="padding-left: 10px;">
										<div id="divFormContact" class="hide">
											<html:form action="mail/preferences/addContact.drt" method="post" style="padding: 0px; margin: 0px;" target="util" enctype="multipart/form-data">
												<html:hidden property="action" />
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
															<fmt:message key="mail.settings.contactEmail" />&nbsp;
														</td>
														<td width="100%">
															<html:text property="email" size="50" maxlength="255"/>
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
															<fmt:message key="mail.settings.contactName" />
														</td>
														<td width="100%">
															<html:text property="name" size="50" maxlength="255"/>
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
															<fmt:message key="mail.settings.contactDescription" />
														</td>
														<td width="100%">
															<html:textarea property="description" style="width: 100%; height: 100px;"></html:textarea>
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
														</td>
														<td width="100%" align="right" style="padding-right: 10px;">
															<html:submit styleClass="BUTTON"><fmt:message key="general.send" /></html:submit>															
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
												</table>
											</html:form>
										</div>
										<div id="divFormGroup" class="hide">
											<html:form action="mail/preferences/addGroup.drt" method="post" style="padding: 0px; margin: 0px;" target="util" enctype="multipart/form-data">
												<html:hidden property="action" />
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
															<fmt:message key="mail.settings.groupName" />&nbsp;
														</td>
														<td width="100%">
															<html:text property="name" size="50" maxlength="255"/>
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
															<fmt:message key="mail.settings.groupEmails" />
														</td>
														<td width="100%">
															<html:textarea property="emails" style="width: 100%; height: 100px;" onkeyup="ajax_showOptions('mail/suggestContacts.drt', this, 'suggestContacts', event);"></html:textarea>
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
													<tr>
														<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
														</td>
														<td width="100%" align="right" style="padding-right: 10px;">
															<html:submit styleClass="BUTTON"><fmt:message key="general.send" /></html:submit>															
														</td>
													</tr>
													<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
												</table>
											</html:form>
										</div>
									</td>
								</tr>
								<tr>
									<td width="100%" align="right">
										<input type="button" class="BUTTON" name="addContact" value="<fmt:message key="mail.settings.addContact" />" onclick="newContact();" />
										&nbsp;
										<input type="button" class="BUTTON" name="addContact" value="<fmt:message key="mail.settings.addGroup" />" onclick="newGroup();" />
									</td>
								</tr>
							</table>
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

<script type="text/javascript">
	function setContact(idint, email, name, description) {
		document.formContact.action.value = "update";
		document.formContact.name.value = name;
		document.formContact.email.value = email;
		document.formContact.description.value = description;
	
		hideElement('divFormGroup');
		hideElement('divFormContact');
		showHideElement('divFormContact');		
	}
	
	function setGroup(idint, name, emails) {
		document.formGroup.action.value = "update";
		document.formGroup.name.value = name;
		document.formGroup.emails.value = emails;
	
		hideElement('divFormGroup');
		hideElement('divFormContact');
		showHideElement('divFormGroup');
	}
	
	function newContact() {
		document.formContact.action.value = "insert";
		document.formContact.name.value = "";
		document.formContact.email.value = "";
		document.formContact.description.value = "";
	
		hideElement('divFormGroup');
		hideElement('divFormContact');
		showHideElement('divFormContact');
	}
	
	function newGroup() {
		document.formGroup.action.value = "insert";
		document.formGroup.name.value = "";
		document.formGroup.emails.value = "";
	
		hideElement('divFormGroup');
		hideElement('divFormContact');
		showHideElement('divFormGroup');
	}
</script>
<!-- FI CONTENTS -->
