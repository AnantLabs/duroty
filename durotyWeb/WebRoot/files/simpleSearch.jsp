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

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<form name="attachments" style="margin: 0; padding: 0" onsubmit="return false;">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<c:if test="${!empty pagination || !empty didyoumean}">
		<thead style="background-color: #ede9e3;">
			<td colspan="8" style="border: 1px solid #cac1b3; border-bottom: none; padding-bottom:2px; padding-left: 10px; padding-right: 10px; text-align: center;">
				<fmt:message key="general.didyoumean" />: <span style="color:red"><c:out value="${didyoumean}" /></span>&nbsp;&nbsp;<c:out value="${pagination}" escapeXml="No"/>
			</td>
		</thead>
	</c:if>	

	<thead style="background-color: #ede9e3;">
		<td colspan="5" onmouseover="this.style.background = '#f3f3f3';" onmouseout="this.style.background = '#ede9e3';" onclick="Files.getFiles(-1, -1, -1, 0, -1, -1);" width="100%" style="cursor: pointer; border: 1px solid #cac1b3; padding-left: 10px; padding-right: 10px;"><fmt:message key="files.score" />&nbsp;<img src="<%=basePath%>files/images/sort_icon.gif" alt="" title="" border="0"  /></td>
		<td onmouseover="this.style.background = '#f3f3f3';" onmouseout="this.style.background = '#ede9e3';" onclick="Files.getFiles(-1, -1, -1, 3, -1, -1);" nowrap="nowrap" style="cursor: pointer; border: 1px solid #cac1b3; padding-left: 10px; padding-right: 10px;"><fmt:message key="files.size" />&nbsp;<img src="<%=basePath%>files/images/sort_icon.gif" alt="" title="" border="0"  /></td>
		<td onmouseover="this.style.background = '#f3f3f3';" onmouseout="this.style.background = '#ede9e3';" onclick="Files.getFiles(-1, -1, -1, 1, -1, -1);" nowrap="nowrap" style="cursor: pointer; border: 1px solid #cac1b3; padding-left: 10px; padding-right: 10px;"><fmt:message key="files.date" />&nbsp;<img src="<%=basePath%>files/images/sort_icon.gif" alt="" title="" border="0"  /></td>
		<td nowrap="nowrap" style="cursor: pointer; border: 1px solid #cac1b3; padding-left: 10px; padding-right: 10px;">&nbsp;</td>
	</thead>

	<tr><td colspan="8" style="height: 5px;"></td></tr>
	<c:forEach var="file" items="${files}">
		<tr onmouseover="this.style.background = '#CCCCCC';" onmouseout="this.style.background = 'none';">			
			<td nowrap="nowrap" style="padding-left: 10px;">
				<c:out value="${file.score}" />% 
			</td>
			<td nowrap="nowrap" style="padding-left: 10px;">
				<input type="checkbox" name="idints" value="<c:out value="${file.idint}" />" />
			</td>
			<td nowrap="nowrap" style="padding-left: 10px;">
				<c:choose>
					<c:when test="${file.flagged}">
						<img onclick="Files.flag(<c:out value="${file.idint}" />);" src="<%=basePath%>images/star_on_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" style="cursor: pointer;"/>
					</c:when>
					<c:otherwise>
						<img onclick="Files.flag(<c:out value="${file.idint}" />);" src="<%=basePath%>images/star_off_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" style="cursor: pointer;"/>
					</c:otherwise>
				</c:choose>
			</td>
			<td nowrap="nowrap" style="padding-left: 10px;">
				<img src="<%=basePath%>files/images/<c:out value="${file.extension}" />.gif" border="0" alt="<c:out value="${file.contentType}" />" title="<c:out value="${file.contentType}" />"/>
			</td>
			<td nowrap="nowrap" style="padding-left: 10px; width: 100%;">
				<c:set var="info">
						<fmt:message key="folder.${file.box}" />
					</c:set>
					<c:if test="${!empty file.label}">
						<c:set var="info" value="${info}, ${file.label}" />
					</c:if>
					<a href="<%=basePath%>files/download/<c:out value="${file.name}" />?mid=<c:out value="${file.mid}" />&part=<c:out value="${file.part}" />" target="_blank"><c:out value="${file.name}" /></a>&nbsp;<span class="infoBox">(<c:out value="${info}" />)</span>
			</td>	
			
			<td nowrap="nowrap" style="text-align: right; padding-left: 10px; padding-right: 10px;"">
				<div>
					<c:out value="${file.size}" />
				</div>
			</td>
			<td nowrap="nowrap" style="text-align: right; padding-left: 10px; padding-right: 10px;"">
				<span title="<fmt:formatDate value="${file.date}" type="both" dateStyle="full" timeStyle="short" />">
					<c:out value="${file.dateStr}" />
				</span>
			</td>
			<td style="text-align: center;">
				<c:choose>
					<c:when test="${file.box == 'TRASH'}">
						<img onclick="Files.restoreFile(<c:out value="${file.idint}" />);" src="<%=basePath%>images/restore.gif" alt="<fmt:message key='files.restore' />" title="<fmt:message key='files.restore' />" border="0" style="cursor: pointer;" />						
					</c:when>
					<c:otherwise>						
						<img onclick="Files.deleteFile(<c:out value="${file.idint}" />);" src="<%=basePath%>images/trash.gif" alt="<fmt:message key='files.delete' />" title="<fmt:message key='files.delete' />" border="0" style="cursor: pointer;" />
					</c:otherwise>
				</c:choose>				
			</td>
		</tr>
	</c:forEach>

</table>

</form>