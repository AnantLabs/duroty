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

<div id="formCompose" class="hide">
	<div id="layout">
		<b class="rtop"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorder">
		<tr>
		<td width="100%">
		<form action="<%=basePath%>mail/send.drt" name="formCompose" method="post" enctype="multipart/form-data" target="util">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader">
				<tr>
					<td width="100%" class="title">
						<div>
							<input type="button" class="BUTTON" name="send" value="<fmt:message key="mail.send" />" onclick="javascript:submitFormCompose();" />
							&nbsp;&nbsp;
							<input type="button" class="BUTTON" name="saveDraft" value="<fmt:message key="mail.saveDraft" />" onclick="javascript:draftFormCompose();" />
							&nbsp;&nbsp;
							<input type="button" class="BUTTON" name="cancel" value="<fmt:message key="mail.cancel" />" onclick="javascript:Mail.backWithHash();" />
						</div>
					</td>
				</tr>
				<tr>
					<td width="100%" class="area">
						<table width="100%" border="0" cellspacing="2" cellpadding="2" class="composeArea">
							<c:if test="${!empty identities}">
								<tr>
									<td nowrap="nowrap" align="right">
										<fmt:message key="mail.from" />
									</td>
									<td width="100%">
										<select size="1" name="identity">
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
										</select>
										&nbsp;&nbsp;
										<fmt:message key="mail.priority" />
										<select size="1" name="priority">
											<option value="normal" selected="selected">
												<fmt:message key="mail.priority.normal" />
											</option>
											<option value="high">
												<fmt:message key="mail.priority.high" />
											</option>
											<option value="low">
												<fmt:message key="mail.priority.low" />
											</option>
										</select>
									</td>
								</tr>
							</c:if>
							<tr>
								<td nowrap="nowrap" align="right" valign="top">
									<fmt:message key="mail.to" />
								</td>
								<td width="100%">
									<textarea name="to" style="width: 90%; height: 40px;" onkeyup="ajax_showOptions('mail/suggestContacts.drt', this, 'suggestContacts', event);"></textarea>
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap" align="right" valign="top">
									<span style="color: blue; cursor: pointer;" onclick="javascript:showHideElement('tdCC');"><fmt:message key="mail.addCc" /></span>
								</td>
								<td width="100%" id="tdCC" class="hide">
									<textarea name="cc" id="idCc" style="width: 90%; height: 40px;" onkeyup="javascript:ajax_showOptions('mail/suggestContacts.drt', this, 'suggestContacts', event);"></textarea>
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap" align="right" valign="top">
									<span style="color: blue; cursor: pointer;" onclick="javascript:showHideElement('tdBCC');"><fmt:message key="mail.addBcc" /></span>
								</td>
								<td width="100%" id="tdBCC" class="hide">
									<textarea name="bcc" id="idBcc" style="width: 90%; height: 40px;" onkeyup="javascript:ajax_showOptions('mail/suggestContacts.drt', this, 'suggestContacts', event);"></textarea>
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap" align="right">
									<fmt:message key="mail.subject" />
								</td>
								<td width="100%">
									<input type="text" name="subject" style="width: 90%;" />
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap" align="right" valign="top">
									<span style="color: blue; text-decoration: underline; cursor: pointer;" onclick="javascript:Mail.attachFile('attachFile');"><fmt:message key="mail.addAttachment" /></span>&nbsp;
									<img src="<%=basePath%>images/attachment.gif" border="0" style="cursor: pointer;" onclick="javascript:Mail.attachFile('attachFile');" />
								</td>
								<td width="100%" id="attachFile">
								</td>
							</tr>
							<tr>
								<td width="100%" colspan="2" align="center">
									<c:choose>
										<c:when test="${!empty preferences && preferences.htmlMessage}">
											<input type="hidden" name="isHtml" id="isHtmltaBody" value="true" />
										</c:when>
										<c:otherwise>
											<input type="hidden" name="isHtml" id="isHtmltaBody" value="false" />
										</c:otherwise>
									</c:choose>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td style="text-align: right; padding-right: 20px;">
												<DIV style="width: 93%">
												<span style="color: blue; text-decoration: underline; cursor: pointer;" onclick="javascript:Mail.textVersion('taBody');"><fmt:message key="mail.textVersion" /></span>&nbsp;&nbsp;<span style="color: blue; text-decoration: underline; cursor: pointer;" onclick="javascript:Mail.htmlVersion('taBody');"><fmt:message key="mail.htmlVersion" /></span>
												</DIV>
											</td>
										</tr>
										<tr>											
											<td width="100%" nowrap="nowrap">											
												<textarea id="taBody" name="taBody" style="width:93%; height: 400px;" rows="15"></textarea>
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
						<div>
							<input type="button" class="BUTTON" name="send" value="<fmt:message key="mail.send" />" onclick="javascript:submitFormCompose();" />
							&nbsp;&nbsp;
							<input type="button" class="BUTTON" name="saveDraft" value="<fmt:message key="mail.saveDraft" />" onclick="javascript:draftFormCompose();" />
							&nbsp;&nbsp;
							<input type="button" class="BUTTON" name="cancel" value="<fmt:message key="mail.cancel" />" onclick="javascript:Mail.backWithHash();" />
						</div>
					</td>
				</tr>
			</table>
		</form>
		</td>
		</tr>
		</table>
		<b class="rbottom<c:out value="${clazz}" />"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
	</div>
	<script type="text/javascript">
		var controlComposeSubmit = false;
		var controlDraftComposeSubmit = false;
		var controlReplySubmit = false;
		var controlDraftReplySubmit = false;
		
		function submitFormCompose() {
			if (document.formCompose.to.value == null || document.formCompose.to.value == "") {
				document.formCompose.to.focus();
				alert("<fmt:message key='mail.emptyTo' />");
				return false;
			}
			
			if (document.formCompose.subject.value == null || document.formCompose.subject.value == "") {
				document.formCompose.subject.focus();
				if (!confirm("<fmt:message key='mail.emptySubject' />")) {
					return false;
				}
			}
			
			showLoading();
		
			if (!controlComposeSubmit) {						
				controlComposeSubmit = true;
				
				if (htmlMessage) {
					try {
						//tinyMCE.getInstanceById("taBody");
						tinyMCE.triggerSave();
					} catch (e) {
					}
				}
				
				document.formCompose.action = "<%=basePath%>mail/send.drt";
				document.formCompose.submit();
				
				controlComposeSubmit = false;
				
				//Mail.removeTinyMce("taBody");
			} else {
				alert('<fmt:message key='mail.controlSubmit' />');
			}	
			
			return false;
		}
		
		function draftFormCompose() {		
			if (document.formCompose.to.value == null || document.formCompose.to.value == "") {
				document.formCompose.to.focus();
				alert("<fmt:message key='mail.emptyTo' />");
				return false;
			}
			
			if (document.formCompose.subject.value == null || document.formCompose.subject.value == "") {
				document.formCompose.subject.focus();
				if (!confirm("<fmt:message key='mail.emptySubject' />")) {
					return false;
				}
			}
			
			showLoading();
		
			if (!controlDraftComposeSubmit) {						
				controlDraftComposeSubmit = true;
				
				if (htmlMessage) {
					//tinyMCE.getInstanceById("taBody");
					tinyMCE.triggerSave();
				}
				
				document.formCompose.action = "<%=basePath%>mail/saveDraft.drt";
				document.formCompose.submit();
				
				controlDraftComposeSubmit = false;
				
				//Mail.removeTinyMce("taBody");
			} else {
				alert('<fmt:message key='mail.controlSubmit' />');
			}	
			
			return false;
		}
		
		function submitFormReply() {
			if (document.formReply.to.value == null || document.formReply.to.value == "") {
				document.formReply.to.focus();
				alert("<fmt:message key='mail.emptyTo' />");
				return false;
			}
			
			if (document.formReply.subject.value == null || document.formReply.subject.value == "") {
				document.formReply.subject.focus();
				if (!confirm("<fmt:message key='mail.emptySubject' />")) {
					return false;
				}
			}
			
			showLoading();
		
			if (!controlReplySubmit) {						
				controlReplySubmit = true;
				
				if (htmlMessage) {
					//tinyMCE.getInstanceById("taReplyBody");
					tinyMCE.triggerSave();
				}
				
				document.formReply.submit();
				
				controlReplySubmit = false;
				
				//Mail.removeTinyMce("taReplyBody");
			} else {
				alert('<fmt:message key='mail.controlSubmit' />');
			}	
			
			return false;
		}
		
		function draftFormReply() {		
			if (document.formReply.to.value == null || document.formReply.to.value == "") {
				document.formReply.to.focus();
				alert("<fmt:message key='mail.emptyTo' />");
				return false;
			}
			
			if (document.formReply.subject.value == null || document.formReply.subject.value == "") {
				document.formReply.subject.focus();
				if (!confirm("<fmt:message key='mail.emptySubject' />")) {
					return false;
				}
			}
			
			showLoading();
		
			if (!controlDraftReplySubmit) {						
				controlDraftReplySubmit = true;
				
				if (htmlMessage) {
					//tinyMCE.getInstanceById("taReplyBody");
					tinyMCE.triggerSave();
				}
				
				document.formReply.action = "<%=basePath%>mail/saveDraft.drt";
				document.formReply.submit();
				
				controlDraftReplySubmit = false;
				
				//Mail.removeTinyMce("taReplyBody");
			} else {
				alert('<fmt:message key='mail.controlSubmit' />');
			}	
			
			return false;
		}
	</script>
	<div id="durotyCookie" class="hide"><fmt:message key="mail.message.cookie" /></div>
</div>