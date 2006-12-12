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
	<c:when test="${action == 'simpleSearch' || action == 'advancedSearch' || action == 'testFilter'}">
		<c:set var="clazz" value="search" />
	</c:when>
	<c:otherwise>
		<c:set var="clazz" value="" />
	</c:otherwise>
</c:choose>
<div id="layout">
	<c:choose>
		<c:when test="${!empty message}">		
			<div id="auxAttachments">
				<c:forEach var="attachment" items="${message.attachments}">					
					<div id="fa<c:out value="${attachment.idint}" />" title="<c:out value="${message.mid}" />" class="hide"><c:out value="${attachment.name}" /></div>					
				</c:forEach>
			</div>
					
			<html:form action="mail/spam.drt" method="post" target="util" enctype="multipart/form-data">
				<html:hidden property="mid" value="${message.mid}" />
				<html:hidden property="folder" />
				<html:hidden property="displayLocation" value="read"/>
			</html:form>
			
			<b class="rtop<c:out value="${clazz}" />"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorder<c:out value="${clazz}" />">
			<tr>
			<td width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader">
				<tr>
					<td width="100%" class="title<c:out value="${clazz}" />" style="padding-bottom: 5px">
						<c:choose>
							<c:when test="${param.folder == 'SPAM'}">
								<div>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.backWithHash();return false;"><fmt:message key="mail.button.back" /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.notSpam('<%=basePath%>mail/notSpam.drt');return false;"><fmt:message key='mail.button.notSpam' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', 'folder:<c:out value="${message.box}" />');return false;"><fmt:message key='mail.button.deleteForever' /></button>
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
								</div>
							</c:when>
							<c:when test="${param.folder == 'TRASH'}">
								<div>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.backWithHash();return false;"><fmt:message key="mail.button.back" /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');return false;"><fmt:message key='mail.button.isSpam' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', 'folder:<c:out value="${message.box}" />');return false;"><fmt:message key='mail.button.deleteForever' /></button>
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
								</div>
							</c:when>
							<c:otherwise>
								<div>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.backWithHash();return false;"><fmt:message key="mail.button.back" /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.archive('<%=basePath%>mail/archive.drt');return false;"><fmt:message key='mail.button.archive' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');return false;"><fmt:message key='mail.button.isSpam' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', 'folder:<c:out value="${message.box}" />');return false;"><fmt:message key='mail.button.delete' /></button>
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
								</div>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td width="100%" class="areaWithPadding" id="tableReadMessage">
						<table border="0" cellspacing="0" cellpadding="0" class="tableReadMessage">						
							<tr>
								<td class="subject" id="msgSubject">
									<c:out value="${message.subject}" />
								</td>
							</tr>
							<tr>
								<td class="options">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%">
												<div>
													<span class="from" onclick="javascript:Mail.reply();return false;"> <c:out value="${message.email}" /> </span>
													<c:if test="${message.box == 'DRAFT'}">
														&nbsp;<input type="button" class="buttonEditDraft" name="mail.button.editDraft" value="<fmt:message key="mail.button.editDraft" />" onclick="javascript:Mail.draft();return false;" />
													</c:if>
												</div>
											</td>
											<td nowrap="nowrap" align="right">
												<span class="linkUnderline" onclick="javascript:showHideElement('msgOptions');return false;"><fmt:message key="mail.showOptions" /></span>&nbsp;
												<c:if test="${message.hasAttachment}">
													<img src="<%=basePath%>images/attachment.gif" border="0" />&nbsp;
												</c:if>
												<span title="<fmt:formatDate value="${message.date}" type="both" dateStyle="full" timeStyle="short" />">
													<c:choose>
														<c:when test="${!empty message.dateStr}">
															<c:out value="${message.dateStr}" />
														</c:when>
														<c:otherwise>
															
														</c:otherwise>
													</c:choose>
												</span>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="hide" id="msgOptions">
									<div style="height: 5px;"></div>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableOptions">
										<tr>
											<td width="100%">
												<fmt:message key="mail.from" />: <b id="msgFrom"><c:out value="${message.from}" /></b>
											</td>
										</tr>
										<tr>
											<td width="100%">
												<fmt:message key="mail.replyTo" />: <b id="msgReplyTo"><c:out value="${message.replyTo}" /></b>
											</td>
										</tr>
										<tr>
											<td width="100%">
												<fmt:message key="mail.to" />: <b id="msgTo"><c:out value="${message.to}" /></b>
											</td>
										</tr>
										<c:if test="${!empty message.cc}">
											<tr>
												<td width="100%">
													<fmt:message key="mail.cc" />: <b id="msgCc"><c:out value="${message.cc}" /></b>
												</td>
											</tr>
										</c:if>
										<tr>
											<td width="100%">
												<fmt:message key="mail.date" />: <b id="msgDate"><fmt:formatDate value="${message.date}" type="both" dateStyle="full" timeStyle="short" /></b>
											</td>
										</tr>
										<tr>
											<td style="height: 5px;">
											</td>
										</tr>
										<tr>
											<td width="100%">
												<span class="linkUnderline" onclick="javascript:Mail.displayImages('<c:out value="${message.box}" />', '<c:out value="${message.mid}" />');return false;"><fmt:message key="mail.displayImages" /></span>&nbsp;&nbsp;<span class="linkUnderline" onclick="javascript:Mail.reply();return false;"><fmt:message key="mail.reply" /></span>&nbsp;&nbsp;<span class="linkUnderline" onclick="javascript:Mail.replyToAll();return false;"><fmt:message key="mail.replyToAll" /></span>&nbsp;&nbsp;<span class="linkUnderline" onclick="javascript:Mail.forward();return false;"><fmt:message key="mail.forward" /></span>&nbsp;&nbsp;<span class="linkUnderline" onclick="javascript:Mail.properties('<c:out value="${message.mid}" />');return false;"><fmt:message key="mail.properties" /></span>
											</td>
										</tr>
									</table>
									<div style="height: 5px;"></div>
								</td>
							</tr>
							<tr>
								<td class="hide" id="properties">
									<div id="msgProperties"></div>
									<div style="height: 5px;"></div>
								</td>
							</tr>
							<tr>
								<td class="hide" id="tdReply">
									<c:import url="/mail/reply.jsp" />
								</td>
							</tr>
							<tr>					
								<td id="msgReferencesBefore">
									<c:if test="${!empty message.referencesBefore}">
										<c:forEach items="${message.referencesBefore}" var="reference">										
											<div class="messageReference" onclick="showHideElement('<c:out value="${message.mid}" /><c:out value="${reference.mid}" />');return false;">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<colgroup>
														<col style="width: 100%;"/>
														<col style="white-space: nowrap;"/>
													</colgroup>	
													<tr>
														<td>
															<div style="white-space: nowrap; overflow: hidden; empty-cells: show;">
																<b><c:out value="${reference.email}" /></b>&nbsp;
																<span class="infoBox">
																(<fmt:message key="folder.${reference.box}" /><c:if test="${!empty reference.label}">,&nbsp;<c:out value="${reference.label}" /></c:if>)
																</span>&nbsp;
																<c:out value="${reference.subject}" />
															</div>
														</td>
														<td style="white-space: nowrap; text-align: right; padding-right: 10px;">
															&nbsp;
															<c:if test="${reference.hasAttachment}">
																&nbsp;<img src="<%=basePath%>images/attachment.gif" border="0" />
															</c:if>
															&nbsp;
															<c:out value="${reference.size}" />
															&nbsp;
															<span title="<fmt:formatDate value="${reference.date}" type="both" dateStyle="full" timeStyle="short" />">
																<c:choose>
																	<c:when test="${!empty reference.dateStr}">
																		<c:out value="${reference.dateStr}" />
																	</c:when>
																	<c:otherwise>
																		<fmt:formatDate value="${reference.date}" type="both" dateStyle="full" timeStyle="short" />
																	</c:otherwise>
																</c:choose>
															</span>
														</td>
													</tr>
												</table>
											</div>
											<div id="<c:out value="${message.mid}" /><c:out value="${reference.mid}" />" class="hide">
												<div class="messageReferenceBody">
													<div style="text-align:right;color: blue; text-decoration: underline; cursor:pointer; font-weight: bold;" onclick="javascript:Mail.displayLocation('read:<c:out value="${reference.box}" />*<c:out value="${reference.mid}" />!0', null);return false;">
														<fmt:message key="mail.reference.read" />
													</div>
													<c:out value="${reference.body}" escapeXml="No" />
												</div>
											</div>
										</c:forEach>
									</c:if>
								</td>
							</tr>
							<tr>
								<td class="print">
									<div style="height: 5px;"></div>
									<img src="<%=basePath%>images/decrease.gif" border="0" style="cursor: pointer;" onclick="javascript:decreaseFont('msgBody');return false;" alt="<fmt:message key="mail.button.decrease" />" title="<fmt:message key="mail.button.decrease" />" /><img src="<%=basePath%>images/increase.gif" border="0" style="cursor: pointer;" onclick="javascript:increaseFont('msgBody');return false;" alt="<fmt:message key="mail.button.increase" />" title="<fmt:message key="mail.button.increase" />" />
									&nbsp;&nbsp;
									<img src="<%=basePath%>images/print.gif" border="0" style="cursor: pointer;" onclick="javascript:openDialog('<%=basePath%>mail/print.jsp', 'Print', 750, 600, false);return false;" alt="<fmt:message key="mail.button.print" />"
										title="<fmt:message key="mail.button.print" />" />
								</td>
							</tr>
							<tr>
								<td class="body" id="msgBody">
									<c:out value="${message.body}" escapeXml="No" />
								</td>
							</tr>
							<tr>
								<td style="height: 10px;"></td>
							</tr>
							<c:if test="${message.hasAttachment}">
								<tr>
									<td class="attachment">
										<c:forEach var="attachment" items="${message.attachments}">
											<div>
												<div style="float: left; padding-right: 10px; padding-top: 2px;">
													<img src="<%=basePath%>files/images/<c:out value="${attachment.extension}" />.gif" border="0" alt="<c:out value="${attachment.contentType}" />" title="<c:out value="${attachment.contentType}" />"/>
												</div>
												<div style="">
													<a href="<%=basePath%>mail/attachment/<c:out value="${attachment.name}" />?mid=<c:out value="${message.mid}" />&part=<c:out value="${attachment.idint}" />" target="_blank"><c:out value="${attachment.name}" /></a>
													&nbsp;<c:out value="${attachment.size}" />
												</div>
											</div>
										</c:forEach>
									</td>
								</tr>
							</c:if>							
							<tr>					
								<td id="msgReferencesAfter">
									<c:if test="${!empty message.referencesAfter}">
										<c:forEach items="${message.referencesAfter}" var="reference">										
											<div class="messageReference" onclick="showHideElement('<c:out value="${message.mid}" /><c:out value="${reference.mid}" />');return false;">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<colgroup>
														<col style="width: 100%;"/>
														<col style="white-space: nowrap;"/>
													</colgroup>	
													<tr>
														<td>
															<div style="white-space: nowrap; overflow: hidden; empty-cells: show;">
																<b><c:out value="${reference.email}" /></b>&nbsp;
																<span class="infoBox">
																(<fmt:message key="folder.${reference.box}" /><c:if test="${!empty reference.label}">,&nbsp;<c:out value="${reference.label}" /></c:if>)
																</span>&nbsp;
																<c:out value="${reference.subject}" />
															</div>
														</td>
														<td style="white-space: nowrap; text-align: right; padding-right: 10px;">
															&nbsp;
															<c:if test="${reference.hasAttachment}">
																&nbsp;<img src="<%=basePath%>images/attachment.gif" border="0" />
															</c:if>
															&nbsp;
															<c:out value="${reference.size}" />
															&nbsp;
															<span title="<fmt:formatDate value="${reference.date}" type="both" dateStyle="full" timeStyle="short" />">
																<c:choose>
																	<c:when test="${!empty reference.dateStr}">
																		<c:out value="${reference.dateStr}" />
																	</c:when>
																	<c:otherwise>
																		<fmt:formatDate value="${reference.date}" type="both" dateStyle="full" timeStyle="short" />
																	</c:otherwise>
																</c:choose>
															</span>
														</td>
													</tr>
												</table>
											</div>
											<div id="<c:out value="${message.mid}" /><c:out value="${reference.mid}" />" class="hide">
												<div class="messageReferenceBody">
													<div style="text-align:right;color: blue; text-decoration: underline; cursor:pointer; font-weight: bold;" onclick="javascript:Mail.displayLocation('read:<c:out value="${reference.box}" />*<c:out value="${reference.mid}" />!0', null);return false;">
														<fmt:message key="mail.reference.read" />
													</div>
													<c:out value="${reference.body}" escapeXml="No" />
												</div>
											</div>
										</c:forEach>
									</c:if>
								</td>
							</tr>
	
						</table>
					</td>
				</tr>
				<tr>
					<td width="100%" class="title<c:out value="${clazz}" />" style="padding-top: 5px">
						<c:choose>
							<c:when test="${param.folder == 'SPAM'}">
								<div>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.backWithHash();return false;"><fmt:message key="mail.button.back" /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.notSpam('<%=basePath%>mail/notSpam.drt');return false;"><fmt:message key='mail.button.notSpam' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', 'folder:<c:out value="${message.box}" />');return false;"><fmt:message key='mail.button.deleteForever' /></button>
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
								</div>
							</c:when>
							<c:when test="${param.folder == 'TRASH'}">
								<div>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.backWithHash();return false;"><fmt:message key="mail.button.back" /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');return false;"><fmt:message key='mail.button.isSpam' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', 'folder:<c:out value="${message.box}" />');return false;"><fmt:message key='mail.button.deleteForever' /></button>
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
								</div>
							</c:when>
							<c:otherwise>
								<div>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.backWithHash();return false;"><fmt:message key="mail.button.back" /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.archive('<%=basePath%>mail/archive.drt');return false;"><fmt:message key='mail.button.archive' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.spam('<%=basePath%>mail/spam.drt');return false;"><fmt:message key='mail.button.isSpam' /></button>
									<button style="font-size: 95%; white-space: nowrap;" onclick="javascript:Mail.deleteMessages('<%=basePath%>mail/delete.drt', 'folder:<c:out value="${message.box}" />');return false;"><fmt:message key='mail.button.delete' /></button>
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
								</div>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			</td>
			</tr>
			</table>
			<b class="rbottom<c:out value="${clazz}" />"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
		</c:when>
		<c:otherwise>
			Message is null or deleted
		</c:otherwise>
	</c:choose>
</div>