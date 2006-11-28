<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
		%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="buttonCompose">
	<input type="button" name="compose" class="buttonCompose" value="<fmt:message key="general.compose" />" onclick="javascript:Mail.displayLocation('compose:0*0!0', null);" /><br/>
</div>

<div id="folders" class="folders">
	<c:forEach var="folder" items="${folders}">
		<div id="folder<c:out value="${folder.name}" />" class="folder" onclick="javascript:Mail.displayLocation('<c:out value="${folder.name}" />:0*0!0', null);">
			<span class="linkUnderline"><fmt:message key="folder.${folder.name}" /></span>
			<span id="info<c:out value="${folder.name}" />" style="padding-left:5px; font-weight: bold;"></span>
		</div>
	</c:forEach>
</div>

<br/>

<div class="labelsTitle" onclick="javascript:Mail.showHideLabels();" style="cursor: pointer;">
	<img id="imgOpenCloseLabels" src="<%=basePath%>images/opentriangle.gif" border="0" />&nbsp;&nbsp;<fmt:message key="mail.labels" />
</div>
<div class="labels">
	<div id="labelsContents" style="display: block;">
		<table class="labelsContents" cellpadding="1" cellspacing="0" width="100%">
			<tr>
				<td nowrap="nowrap">
					<div id="dlabel">		
						<c:forEach var="label" items="${labels}">
							<div id="lab<c:out value="${label.idint}" />" style="overflow: hidden; text-overflow:ellipsis; cursor: pointer; padding-top: 1px; padding-bottom: 1px;" onclick="javascript:Mail.displayLocation('label<c:out value="${label.idint}" />:0*0!0');" title="<c:out value="${label.name}" />">
								<font class="label"><c:out value="${label.name}" /></font><span id="infoLab<c:out value="${label.idint}" />" style="padding-left:5px; font-weight: bold;"></span>
							</div>
						</c:forEach>
					</div>
				</td>
			</tr>
		</table>		
	</div>	
</div>
<div class="labelsTitle">
	<form action="<%=basePath%>mail/preferences/formLabel.drt" method="post" style="margin: 0px; padding: 0px;" enctype="multipart/form-data">
		<input type="text" size="20" name="label" maxlength="100" style="width: 95%;" value="<fmt:message key="mail.label.create" />" onfocus="clearLabel(this, '');" onblur="clearLabel(this, '<fmt:message key="mail.label.create" />');"/>
	</form>
</div>

<br/>

<div class="dbuddyTitle" onclick="javascript:Mail.showHideBuddies();" style="cursor: pointer;">
	<img id="imgOpenCloseBuddy" src="<%=basePath%>images/opentriangle_white.gif" border="0" />&nbsp;&nbsp;<fmt:message key="chat.buddy" />
</div>
<div class="dbuddy">	
	<div id="statusSettings">	
		<table class="labelsContents" cellpadding="1" cellspacing="0" width="100%">
			<tr>
				<td nowrap="nowrap">
					<div id="myBuddy" class="myBuddy"  style="overflow: hidden; text-overflow:ellipsis; cursor: pointer;">
						<c:out value="${user}" />
					</div>
					<span id="curStatus" style="overflow: hidden; text-overflow:ellipsis; cursor: pointer;" onclick="toggleStatusList();return false;" onfocus="awayHasFocus=true;this.hideFocus=true;" onblur="awayHasFocus=false;"><img src="images/user_available.gif" border="0" /><fmt:message key="chat.available" /></span>
				</td>
			</tr>
		</table>
	</div>
	<div id="dbuddyContents" style="display: block;">
		<table class="tableMessages" cellpadding="1" cellspacing="0" width="100%">
			<tr>
				<td nowrap="nowrap" style="border-bottom: 1px solid rgb(187, 187, 187);">
					<div id="dbuddy">		
					</div>
				</td>
			</tr>
		</table>		
	</div>	
	<div id="statusList">
		<span id="state1" onclick="Chat.setStatus(1, null, 1);return false;"><img src="images/user_available.gif" border="0" />&nbsp;<fmt:message key="chat.available" /></span>
		<hr/>
		<span id="state2" onclick="Chat.setStatus(2, '<fmt:message key="chat.busy.javascript" />', 2);return false;"><img src="images/user_busy.gif" border="0" />&nbsp;<fmt:message key="chat.busy" /></span>
		<span id="state3" onclick="Chat.setStatus(2, '<fmt:message key="chat.backlater.javascript" />', 3);return false;"><img src="images/user_busy.gif" border="0" />&nbsp;<fmt:message key="chat.backlater" /></span>
		<span id="state4" onclick="Chat.setStatus(2, '<fmt:message key="chat.awayfrom.javascript" />', 4);return false;"><img src="images/user_busy.gif" border="0" />&nbsp;<fmt:message key="chat.awayfrom" /></span>
		<hr/>
		<span id="state5" onclick="Chat.customAway();$('statusList').style.display='none';return false;"><img src="images/user_busy.gif" border="0" />&nbsp;<fmt:message key="chat.custom" /></span>
		<hr/>
		<span id="state6" onclick="Chat.setStatus(0, '<fmt:message key="chat.signout.javascript" />', 6);return false;"><img src="images/user_offline.gif" border="0" />&nbsp;<fmt:message key="chat.signout" /></span>		
	</div>	
	<span id="state7" class="hide" onclick="Chat.setStatus(3, '<fmt:message key="chat.idle.javascript" />', 7);return false;"><fmt:message key="chat.idle" /></span>
</div>
<div class="dbuddyTitle" style="text-align: center;">
	<input type="button" name="inviteToChat" value="<fmt:message key="chat.invite" />" onclick="javascript:openwin('<%=basePath%>mail/invitateToChat.jsp', 'CHAT', 700, 400);"/>
</div>

<script language="javascript" type="text/javascript">	
	function clearLabel(text, value) {
		text.value = value;
	}
</script>