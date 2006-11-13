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

<c:choose>
	<c:when test="${param.folder == 'SPAM'}">
		<c:set var="clazz" value="spam" />
	</c:when>
	<c:when test="${param.folder == 'TRASH'}">
		<c:set var="clazz" value="spam" />
	</c:when>
	<c:when test="${action == 'label' || action == 'simpleSearch' || action == 'advancedSearch' || action == 'testFilter'}">
		<c:set var="clazz" value="search" />
	</c:when>
	<c:otherwise>
		<c:set var="clazz" value="" />
	</c:otherwise>
</c:choose>
<div id="layout">
	<b class="rtop<c:out value="${clazz}" />"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorder<c:out value="${clazz}" />">
	<tr>
	<td width="100%">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader">
		<c:if test="${!empty didyoumean}">	
			<tr>
				<td width="100%" class="didyoumean">
					<fmt:message key="general.didyoumean" />:&nbsp;<c:out value="${didyoumean}" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td width="100%" class="title<c:out value="${clazz}" />">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="75%" align="left" style="padding-top: 5px;" nowrap="nowrap">
							<c:choose>
								<c:when test="${param.folder == 'SPAM'}">
									<div>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.notSpam' />" name="mail.button.notSpam" onclick="javascript:Mail.notSpam('<%=basePath%>mail/notSpam.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteForever' />" name="mail.button.deleteForever" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
										&nbsp;
										<select name="selectMoveMessages" onchange="javascript:Mail.move('<%=basePath%>mail/move.drt', this.options[this.selectedIndex].value);this.selectedIndex = 0;">
											<option value="" disabled="disabled" selected="selected">
												<fmt:message key='mail.button.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="folder" items="${folders}">
												<option value="folder:<c:out value="${folder.name}" />">
													<fmt:message key="folder.${folder.name}" />
												</option>
											</c:forEach>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<option value="" disabled="disabled">
												<fmt:message key='mail.button.label.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="label" items="${labels}">
												<option value="label:<c:out value="${label.name}" />*<c:out value="${label.idint}" />">
													<c:out value="${label.name}" />
												</option>
											</c:forEach>
										</select>
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteLabels' />" name="mail.button.deleteLabels" onclick="javascript:Mail.deleteLabels('<%=basePath%>mail/deleteLabelsFromMessages.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteAll' />" name="mail.button.deleteAll" onclick="javascript:Mail.deleteAll('<%=basePath%>mail/deleteAll.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />', '<fmt:message key='mail.deleteAll' />');" />
										&nbsp;
										<span style="cursor: pointer; color: blue; font-size: 13px; text-decoration: underline;" onclick="javascript:Mail.refresh();"><fmt:message key='mail.button.refresh' /></span>
									</div>
								</c:when>
								<c:when test="${param.folder == 'TRASH'}">
									<div>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.isSpam' />" name="mail.button.isSpam" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteForever' />" name="mail.button.deleteForever" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
										&nbsp;
										<select name="selectMoveMessages" onchange="javascript:Mail.move('<%=basePath%>mail/move.drt', this.options[this.selectedIndex].value);this.selectedIndex = 0;">
											<option value="" disabled="disabled" selected="selected">
												<fmt:message key='mail.button.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="folder" items="${folders}">
												<option value="folder:<c:out value="${folder.name}" />">
													<fmt:message key="folder.${folder.name}" />
												</option>
											</c:forEach>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<option value="" disabled="disabled">
												<fmt:message key='mail.button.label.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="label" items="${labels}">
												<option value="label:<c:out value="${label.name}" />*<c:out value="${label.idint}" />">
													<c:out value="${label.name}" />
												</option>
											</c:forEach>
										</select>
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteLabels' />" name="mail.button.deleteLabels" onclick="javascript:Mail.deleteLabels('<%=basePath%>mail/deleteLabelsFromMessages.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.purgeTrash' />" name="mail.button.purgeTrash" onclick="javascript:Mail.deleteAll('<%=basePath%>mail/deleteAll.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />', '<fmt:message key='mail.purgeTrash' />');" />
										&nbsp;
										<span style="cursor: pointer; color: blue; font-size: 13px; text-decoration: underline;" onclick="javascript:Mail.refresh();"><fmt:message key='mail.button.refresh' /></span>
									</div>								
								</c:when>
								<c:otherwise>
									<div>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.archive' />" name="mail.button.archive" onclick="javascript:Mail.archive('<%=basePath%>mail/archive.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.isSpam' />" name="mail.button.isSpam" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');" />
										&nbsp;
										<c:if test="${action == 'simpleSearch' || action == 'advancedSearch' || action == 'testFilter'}">
											<input type="button" class="BUTTON" value="<fmt:message key='mail.button.notSpam' />" name="mail.button.notSpam" onclick="javascript:Mail.notSpam('<%=basePath%>mail/notSpam.drt');" />
											&nbsp;
										</c:if>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.delete' />" name="mail.button.delete" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
										&nbsp;
										<select name="selectMoveMessages" onchange="javascript:Mail.move('<%=basePath%>mail/move.drt', this.options[this.selectedIndex].value);this.selectedIndex = 0;">
											<option value="" disabled="disabled" selected="selected">
												<fmt:message key='mail.button.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="folder" items="${folders}">
												<option value="folder:<c:out value="${folder.name}" />">
													<fmt:message key="folder.${folder.name}" />
												</option>
											</c:forEach>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<option value="" disabled="disabled">
												<fmt:message key='mail.button.label.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="label" items="${labels}">
												<option value="label:<c:out value="${label.name}" />*<c:out value="${label.idint}" />">
													<c:out value="${label.name}" />
												</option>
											</c:forEach>
										</select>
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteLabels' />" name="mail.button.deleteLabels" onclick="javascript:Mail.deleteLabels('<%=basePath%>mail/deleteLabelsFromMessages.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteAll' />" name="mail.button.deleteAll" onclick="javascript:Mail.deleteAll('<%=basePath%>mail/deleteAll.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />', '<fmt:message key='mail.deleteAll' />');" />
										&nbsp; 
										<span style="cursor: pointer; color: blue; font-size: 13px; text-decoration: underline;" onclick="javascript:Mail.refresh();"><fmt:message key='mail.button.refresh' /></span>
									</div>
								</c:otherwise>
							</c:choose>
						</td>
						<td width="25%" align="right" style="padding-right: 8px;">
							<select name="orderBy" size="1" onchange="javascript:Mail.order(this.options[this.selectedIndex].value);">
								<option value="" disabled="disabled" selected="selected">
									<fmt:message key='mail.orderBy' />
								</option>
								<option value="1">
									<fmt:message key='mail.orderByDate' />
								</option>
								<option value="2">
									<fmt:message key='mail.orderBySubject' />
								</option>
								<option value="3">
									<fmt:message key='mail.orderBySize' />
								</option>
								<option value="4">
									<fmt:message key='mail.orderByFrom' />
								</option>
								<option value="5">
									<fmt:message key='mail.orderByImportant' />
								</option>
								<option value="6">
									<fmt:message key='mail.orderByUnread' />
								</option>
							</select>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="75%" align="left" style="padding-top: 5px;" nowrap="nowrap">
							<fmt:message key="general.select" />:&nbsp;<span class="link" onclick="javascript:Mail.selectAll();"><fmt:message key="mail.selectAll" /></span>, <span class="link" onclick="javascript:Mail.selectRead();"><fmt:message key="mail.selectRead" /></span>, <span class="link" onclick="javascript:Mail.selectUnread();"><fmt:message key="mail.selectUnread" /></span>, <span class="link" onclick="javascript:Mail.markSelection();"><fmt:message key="mail.selectMark" /></span>, <span class="link" onclick="javascript:Mail.flagImportant('<%=basePath%>mail/flag.drt');"><fmt:message key="mail.flagged" /></span>, <span class="link" onclick="javascript:Mail.flagUnread('<%=basePath%>mail/flag.drt');"><fmt:message key="mail.recent" /></span>
						</td>
						<td width="25%" align="right" style="padding-right: 8px;">
							<c:out value="${pagination}" escapeXml="No"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<c:choose>
					<c:when test="${param.folder == 'TRASH'}">
						<div class="cron">
							<fmt:message key="mail.cronPurgeTrash" />
						</div>
					</c:when>
					<c:when test="${param.folder == 'SPAM'}">
						<div class="cron">
							<fmt:message key="mail.cronPurgeSpam" />
						</div>
					</c:when>
				</c:choose>
				<html:form action="mail/spam.drt" method="post" target="util" style="margin: 0pt; width: 100%;" enctype="multipart/form-data">
					<html:hidden property="folder" />
					<c:choose>
						<c:when test="${!empty messages}">
							<table class="tableMessages" cellpadding="1" cellspacing="0" width="100%">
								<colgroup>
									<col class="cc">
									<col style="width: 20px; font-size: 100%;">
									<col style="width: 20ex; font-size: 100%;">
									<col style="width: 2ex; font-size: 100%;">
									<col style="font-size: 100%;">
									<col style="width: 17px; font-size: 100%;">
									<col style="width: 9.5ex; font-size: 100%;">
								</colgroup>					
								
								<tbody>
								
								<c:set var="count" value="${fn:length(messages)}" />
								<c:forEach items="${messages}" var="message">
									<c:set var="background" value="#f7f7f7" />
									<c:set var="fontWeight" value="normal" />
									<c:set var="title" value="" />
									<c:choose>
										<c:when test="${message.flagged}">
											<c:set var="background" value="#c6e4ff" />
											<c:set var="fontWeight" value="bold" />
											<c:set var="title" value="" />
										</c:when>
										<c:when test="${message.recent}">
											<c:set var="background" value="#ffffcc" />
											<c:set var="fontWeight" value="bold" />
											<c:set var="title" value="new" />
										</c:when>
										<c:otherwise>
											<c:set var="background" value="#f7f7f7" />
											<c:set var="fontWeight" value="normal" />
											<c:set var="title" value="" />
										</c:otherwise>
									</c:choose>
									
									<tr style="background: <c:out value="${background}" />;">
										<td id="td<c:out value='${message.mid}' />" title="<c:out value='${title}' />" style="font-weight: <c:out value="${fontWeight}" />; border-bottom: 1px solid rgb(187, 187, 187); overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
											<html:multibox property="mid" styleId="${message.mid}" onclick="Mail.cacheMid(this);">
												<c:out value="${message.mid}" />
											</html:multibox>
										</td>
										<td nowrap="nowrap" style="font-weight: <c:out value="${fontWeight}" />; border-bottom: 1px solid rgb(187, 187, 187);">
											<c:choose>
												<c:when test="${message.box == 'TRASH'}">
													<img src="<%=basePath%>images/trash.gif" alt="<fmt:message key='mail.button.deleteForever' />" title="<fmt:message key='mail.button.deleteForever' />" border="0" onclick="javascript:document.getElementById('<c:out value="${message.mid}" />').checked=true;Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
												</c:when>
												<c:when test="${message.box == 'SPAM'}">
													<img src="<%=basePath%>images/trash.gif" alt="<fmt:message key='mail.button.deleteForever' />" title="<fmt:message key='mail.button.deleteForever' />" border="0" onclick="javascript:document.getElementById('<c:out value="${message.mid}" />').checked=true;Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
												</c:when>
												<c:when test="${message.flagged}">
													<img src="<%=basePath%>images/star_on_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" onclick="javascript:document.getElementById('<c:out value="${message.mid}" />').checked=true;Mail.flagImportant('<%=basePath%>mail/flag.drt');" />
												</c:when>
												<c:when test="${message.recent}">
													<img src="<%=basePath%>images/star_off_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" onclick="javascript:document.getElementById('<c:out value="${message.mid}" />').checked=true;Mail.flagImportant('<%=basePath%>mail/flag.drt');" />
												</c:when>
												<c:otherwise>
													<img src="<%=basePath%>images/star_off_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" onclick="javascript:document.getElementById('<c:out value="${message.mid}" />').checked=true;Mail.flagImportant('<%=basePath%>mail/flag.drt');" />
												</c:otherwise>
											</c:choose>										
										</td>
										<td onclick="javascript:Mail.displayLocation('read:<c:out value="${message.box}" />*<c:out value="${message.mid}" />!0', null);" title="<c:out value="${message.email}" />" style="font-weight: <c:out value="${fontWeight}" />; border-bottom: 1px solid rgb(187, 187, 187); overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
											<c:choose>
												<c:when test="${message.priority == 'high'}">
													<img src="<%=basePath%>images/high_priority.gif" alt="<fmt:message key="mail.priority.high" />" title="<fmt:message key="mail.priority.high" />" border="0" />
												</c:when>
												<c:when test="${message.priority == 'low'}">
													<img src="<%=basePath%>images/low_priority.gif" alt="<fmt:message key="mail.priority.low" />" title="<fmt:message key="mail.priority.low" />" border="0" />
												</c:when>
												<c:otherwise></c:otherwise>
											</c:choose>	
											<c:out value="${message.email}" />
										</td>
										<td style="border-bottom: 1px solid rgb(187, 187, 187); overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
											&nbsp;
										</td>
										<td onclick="javascript:Mail.displayLocation('read:<c:out value="${message.box}" />*<c:out value="${message.mid}" />!0', null);" title="<c:out value="${message.subject}" />" style="font-weight: <c:out value="${fontWeight}" />; border-bottom: 1px solid rgb(187, 187, 187); overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
											<span class="infoBox">
											(<fmt:message key="folder.${message.box}" /><c:if test="${!empty message.label}">,&nbsp;<c:out value="${message.label}" /></c:if>)
											</span>&nbsp;
											<c:out value="${message.subject}" />
										</td>
										<td onclick="javascript:Mail.displayLocation('read:<c:out value="${message.box}" />*<c:out value="${message.mid}" />!0', null);" style="font-weight: <c:out value="${fontWeight}" />; border-bottom: 1px solid rgb(187, 187, 187); overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
											<c:choose>
												<c:when test="${message.hasAttachment}">
													<img src="<%=basePath%>images/attachment.gif" border="0" alt="<c:out value="${message.size}" />" title="<c:out value="${message.size}" />"/>
												</c:when>
												<c:otherwise>
													&nbsp;
												</c:otherwise>
											</c:choose>
										</td>
										<td onclick="javascript:Mail.displayLocation('read:<c:out value="${message.box}" />*<c:out value="${message.mid}" />!0', null);" style="font-weight: <c:out value="${fontWeight}" />; border-bottom: 1px solid rgb(187, 187, 187); overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
											<span title="<fmt:formatDate value="${message.date}" type="both" dateStyle="full" timeStyle="short" />">
												<c:choose>
													<c:when test="${!empty message.dateStr}">
														<c:out value="${message.dateStr}" />
													</c:when>
													<c:otherwise>
														<fmt:formatDate value="${message.date}" type="both" dateStyle="full" timeStyle="short" />
													</c:otherwise>
												</c:choose>
											</span>
										</td>
									</tr>
								</c:forEach>
								
								</tbody>
							</table>
							<c:if test="${count < 10}">
								<br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
							</c:if>
						</c:when>
						<c:when test="${!empty exception}">
							<table class="tableMessages" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="error">
										<fmt:message key="${exception}" />
									</td>
								</tr>
							</table>
						</c:when>
						<c:otherwise>
							<table class="tableMessages" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="noMail">
										<fmt:message key="mail.noMailInFolder" />
									</td>
								</tr>
							</table>
						</c:otherwise>
					</c:choose>
				</html:form>
			</td>
		</tr>
		<tr>
			<td width="100%" class="title<c:out value="${clazz}" />">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="75%" align="left" style="padding-top: 5px;" nowrap="nowrap">
							<fmt:message key="general.select" />&nbsp;<span class="link" onclick="javascript:Mail.selectAll();"><fmt:message key="mail.selectAll" /></span>, <span class="link" onclick="javascript:Mail.selectRead();"><fmt:message key="mail.selectRead" /></span>, <span class="link" onclick="javascript:Mail.selectUnread();"><fmt:message key="mail.selectUnread" /></span>, <span class="link" onclick="javascript:Mail.markSelection();"><fmt:message key="mail.selectMark" /></span>, <span class="link" onclick="javascript:Mail.flagImportant('<%=basePath%>mail/flag.drt');"><fmt:message key="mail.flagged" /></span>, <span class="link" onclick="javascript:Mail.flagUnread('<%=basePath%>mail/flag.drt');"><fmt:message key="mail.recent" /></span>
						</td>
						<td width="25%" align="right" style="padding-right: 8px;">
							<c:out value="${pagination}" escapeXml="No"/>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="75%" align="left" style="padding-top: 5px;" nowrap="nowrap">
							<c:choose>
								<c:when test="${param.folder == 'SPAM'}">
									<div>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.notSpam' />" name="mail.button.notSpam" onclick="javascript:Mail.notSpam('<%=basePath%>mail/notSpam.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteForever' />" name="mail.button.deleteForever" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
										&nbsp;
										<select name="selectMoveMessages" onchange="javascript:Mail.move('<%=basePath%>mail/move.drt', this.options[this.selectedIndex].value);this.selectedIndex = 0;">
											<option value="" disabled="disabled" selected="selected">
												<fmt:message key='mail.button.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="folder" items="${folders}">
												<option value="folder:<c:out value="${folder.name}" />">
													<fmt:message key="folder.${folder.name}" />
												</option>
											</c:forEach>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<option value="" disabled="disabled">
												<fmt:message key='mail.button.label.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="label" items="${labels}">
												<option value="label:<c:out value="${label.name}" />*<c:out value="${label.idint}" />">
													<c:out value="${label.name}" />
												</option>
											</c:forEach>
										</select>
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteLabels' />" name="mail.button.deleteLabels" onclick="javascript:Mail.deleteLabels('<%=basePath%>mail/deleteLabelsFromMessages.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteAll' />" name="mail.button.deleteAll" onclick="javascript:Mail.deleteAll('<%=basePath%>mail/deleteAll.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />', '<fmt:message key='mail.deleteAll' />');" />
										&nbsp; 
										<span style="cursor: pointer; color: blue; font-size: 13px; text-decoration: underline;" onclick="javascript:Mail.refresh();"><fmt:message key='mail.button.refresh' /></span>
									</div>
								</c:when>
								<c:when test="${param.folder == 'TRASH'}">
									<div>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.isSpam' />" name="mail.button.isSpam" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteForever' />" name="mail.button.deleteForever" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
										&nbsp;
										<select name="selectMoveMessages" onchange="javascript:Mail.move('<%=basePath%>mail/move.drt', this.options[this.selectedIndex].value);this.selectedIndex = 0;">
											<option value="" disabled="disabled" selected="selected">
												<fmt:message key='mail.button.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="folder" items="${folders}">
												<option value="folder:<c:out value="${folder.name}" />">
													<fmt:message key="folder.${folder.name}" />
												</option>
											</c:forEach>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<option value="" disabled="disabled">
												<fmt:message key='mail.button.label.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="label" items="${labels}">
												<option value="label:<c:out value="${label.name}" />*<c:out value="${label.idint}" />">
													<c:out value="${label.name}" />
												</option>
											</c:forEach>
										</select>
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteLabels' />" name="mail.button.deleteLabels" onclick="javascript:Mail.deleteLabels('<%=basePath%>mail/deleteLabelsFromMessages.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.purgeTrash' />" name="mail.button.purgeTrash" onclick="javascript:Mail.deleteAll('<%=basePath%>mail/deleteAll.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />', '<fmt:message key='mail.purgeTrash' />');" />
										&nbsp;
										<span style="cursor: pointer; color: blue; font-size: 13px; text-decoration: underline;" onclick="javascript:Mail.refresh();"><fmt:message key='mail.button.refresh' /></span>
									</div>
								</c:when>
								<c:otherwise>
									<div>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.archive' />" name="mail.button.archive" onclick="javascript:Mail.archive('<%=basePath%>mail/archive.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.isSpam' />" name="mail.button.isSpam" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');" />
										&nbsp;
										<c:if test="${action == 'simpleSearch' || action == 'advancedSearch' || action == 'testFilter'}">
											<input type="button" class="BUTTON" value="<fmt:message key='mail.button.notSpam' />" name="mail.button.notSpam" onclick="javascript:Mail.notSpam('<%=basePath%>mail/notSpam.drt');" />
											&nbsp;
										</c:if>
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.delete' />" name="mail.button.delete" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />');" />
										&nbsp;
										<select name="selectMoveMessages" onchange="javascript:Mail.move('<%=basePath%>mail/move.drt', this.options[this.selectedIndex].value);this.selectedIndex = 0;">
											<option value="" disabled="disabled" selected="selected">
												<fmt:message key='mail.button.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="folder" items="${folders}">
												<option value="folder:<c:out value="${folder.name}" />">
													<fmt:message key="folder.${folder.name}" />
												</option>
											</c:forEach>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<option value="" disabled="disabled">
												<fmt:message key='mail.button.label.move' />
											</option>
											<option value="" disabled="disabled">
												---------------------
											</option>
											<c:forEach var="label" items="${labels}">
												<option value="label:<c:out value="${label.name}" />*<c:out value="${label.idint}" />">
													<c:out value="${label.name}" />
												</option>
											</c:forEach>
										</select>
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteLabels' />" name="mail.button.deleteLabels" onclick="javascript:Mail.deleteLabels('<%=basePath%>mail/deleteLabelsFromMessages.drt');" />
										&nbsp;
										<input type="button" class="BUTTON" value="<fmt:message key='mail.button.deleteAll' />" name="mail.button.deleteAll" onclick="javascript:Mail.deleteAll('<%=basePath%>mail/deleteAll.drt', '<c:out value="${action}" />:<c:out value="${param.folder}" />', '<fmt:message key='mail.deleteAll' />');" />
										&nbsp;
										<span style="cursor: pointer; color: blue; font-size: 13px; text-decoration: underline;" onclick="javascript:Mail.refresh();"><fmt:message key='mail.button.refresh' /></span>
									</div>
								</c:otherwise>
							</c:choose>
						</td>
						<td width="25%" align="right" style="padding-right: 8px;">
							<select name="orderBy" size="1" onchange="javascript:Mail.order(this.options[this.selectedIndex].value);">
								<option value="" disabled="disabled" selected="selected">
									<fmt:message key='mail.orderBy' />
								</option>
								<option value="1">
									<fmt:message key='mail.orderByDate' />
								</option>
								<option value="2">
									<fmt:message key='mail.orderBySubject' />
								</option>
								<option value="3">
									<fmt:message key='mail.orderBySize' />
								</option>
								<option value="4">
									<fmt:message key='mail.orderByFrom' />
								</option>
								<option value="5">
									<fmt:message key='mail.orderByImportant' />
								</option>
								<option value="6">
									<fmt:message key='mail.orderByUnread' />
								</option>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
	</table>
	<b class="rbottom<c:out value="${clazz}" />"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
</div>