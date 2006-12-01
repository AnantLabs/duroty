
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


<div id="formReply" class="show">
	<form action="<%=basePath%>mail/send.drt" name="formReply" method="post" enctype="multipart/form-data" target="util">	
		<input type="hidden" name="mid" value="<c:out value="${message.mid}" />" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader">
			<tr>
				<td width="100%" class="title" style="padding-left: 5px; padding-top:5px; padding-bottom: 5px;">
					<div>
						<button style="font-size: 95%; white-space: nowrap; font-weight: bold;" onclick="javascript:submitFormReply();return false;"><fmt:message key="mail.send" /></button>
						&nbsp;
						<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:draftFormReply();return false;"><fmt:message key="mail.saveDraft" /></button>
						&nbsp;
						<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.reply();return false;"><fmt:message key="mail.cancel" /></button>
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
								<fmt:message key="mail.cc" />
							</td>
							<td width="100%">
								<textarea name="cc" id="idCc" style="width: 90%; height: 40px;" onkeyup="ajax_showOptions('mail/suggestContacts.drt', this, 'suggestContacts', event);"></textarea>
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" align="right" valign="top">
								<fmt:message key="mail.bcc" />
							</td>
							<td width="100%">
								<textarea name="bcc" id="idBcc" style="width: 90%; height: 40px;" onkeyup="ajax_showOptions('mail/suggestContacts.drt', this, 'suggestContacts', event);"></textarea>
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
								<span style="color: blue; text-decoration: underline; cursor: pointer;" onclick="javascript:Mail.attachFile('attachReplyFile');return false;"><fmt:message key="mail.addAttachment" /></span>&nbsp;
								<img src="<%=basePath%>images/attachment.gif" border="0" style="cursor: pointer; padding-right: 3px;" onclick="javascript:Mail.attachFile('attachReplyFile');return false;" />
							</td>
							<td width="100%" id="attachReplyFile">
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" align="right" valign="top">								
							</td>
							<td width="100%" id="forwardAttachments" class="hide">
							</td>
						</tr>
						<tr>
							<td width="100%" colspan="2" align="center">
								<c:choose>
									<c:when test="${!empty preferences && preferences.htmlMessage}">
										<input type="hidden" name="isHtml" id="isHtmltaReplyBody" value="true" />
									</c:when>
									<c:otherwise>
										<input type="hidden" name="isHtml" id="isHtmltaReplyBody" value="false" />
									</c:otherwise>
								</c:choose>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td style="text-align: right; padding-right: 20px;">
											<span style="color: blue; text-decoration: underline; cursor: pointer;" onclick="javascript:Mail.textVersion('taReplyBody');return false;"><fmt:message key="mail.textVersion" /></span>&nbsp;&nbsp;<span style="color: blue; text-decoration: underline; cursor: pointer;" onclick="javascript:Mail.htmlVersion('taReplyBody');return false;"><fmt:message key="mail.htmlVersion" /></span>
										</td>
									</tr>
									<tr>
										<td>
											<textarea id="taReplyBody" name="taReplyBody" style="width:99%; height: 400px;" rows="15"></textarea>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="100%" class="title" style="padding-left: 5px; padding-top:5px; padding-bottom: 5px;">
					<div>
						<button style="font-size: 95%; white-space: nowrap; font-weight: bold;" onclick="javascript:submitFormReply();return false;"><fmt:message key="mail.send" /></button>
						&nbsp;
						<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:draftFormReply();return false;"><fmt:message key="mail.saveDraft" /></button>
						&nbsp;
						<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.reply();return false;"><fmt:message key="mail.cancel" /></button>
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>
