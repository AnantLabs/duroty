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


<table style="background-color: #d7c5bf;" border="0" cellpadding="2" width="100%">
	<tbody>
		<tr>
			<td style="font-size: 13px; text-align: left; color: black;" width="100%">
				<fmt:message key="bookmark.matching" />:
				&nbsp;<b><c:out value="${param.token}" /></b>
				&nbsp;&nbsp;
				<c:if test="${!empty param.token}">
					<a href="http://www.google.com/search?q=<c:out value="${param.token}" />" target="_blank">Search in Google</a>
				</c:if>
			</td>
			<td style="font-size: 13px; text-align: right; color: black;" nowrap="nowrap">
				<c:out value="${time}" />&nbsp;<fmt:message key="bookmark.seconds" />
			</td>
		</tr>
	</tbody>
</table>

<c:if test="${!empty didyoumean}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader">
		<tr>
			<td width="100%" class="didyoumean">
				<fmt:message key="general.didyoumean" />
				:&nbsp;
				<c:out value="${didyoumean}" />
			</td>
		</tr>
	</table>
</c:if>

<div id="formAux" class="hide">
	<form action="" method="post" name="formAux" target="util" enctype="multipart/form-data">
		<input type="hidden" name="idint" />
	</form>
</div>

<c:choose>
	<c:when test="${!empty bookmarks}">
		<div id="divBookmarks" class="divBookmarks">
			<c:forEach items="${bookmarks}" var="bookmark">
				<div class="bookmarkTitle">
					<c:choose>
						<c:when test="${bookmark.flagged}">
							<img src="<%=basePath%>images/star_on_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" onclick="javascript:unflag('<c:out value="${bookmark.idint}" />');" style="cursor: pointer;" />
						</c:when>
						<c:otherwise>
							<img src="<%=basePath%>images/star_off_2.gif" alt="<fmt:message key="mail.flagged" />" title="<fmt:message key="mail.flagged" />" border="0" onclick="javascript:flag('<c:out value="${bookmark.idint}" />');" style="cursor: pointer;" />
						</c:otherwise>
					</c:choose>
					<a href="<c:out value="${bookmark.url}" />" target="_blank"><c:out value="${bookmark.titleHighlight}" escapeXml="No" /></a>
				</div>
				<c:if test="${!empty bookmark.contentsHighlight}">
				<div class="bookmarkContents">
					<c:out value="${bookmark.contentsHighlight}" escapeXml="No" />
				</div>
				</c:if>
				<div class="bookmarkUrl" title="<c:out value="${bookmark.url}" />">
					<c:out value="${bookmark.url}" />
				</div>
				<div class="bookmarkContents">
					<c:out value="${bookmark.comments}" escapeXml="No"/>
				</div>
				<div class="bookmarkKeywords">
					<c:out value="${bookmark.keywords}" /> (<fmt:formatDate dateStyle="full" value="${bookmark.insertDate}"/>)&nbsp;<span class="linkUnderline" onclick="javascript:openwin('<%=basePath%>bookmark/formBookmark.drt?idint=<c:out value="${bookmark.idint}" />', 'Update', 700, 500);"><fmt:message key="bookmark.update" /></span>&nbsp;<span class="linkUnderline" onclick="javascript:deleteBookmark('<c:out value="${bookmark.idint}" />')"><fmt:message key="bookmark.delete" /></span>
				</div>
				<br/>
			</c:forEach>
		</div>
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
					<fmt:message key="bookmark.datanotfound" />
				</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
<form action="" method="post" onsubmit="document.getElementById('token').value=this.token.value;Bookmark.displayLocation('search:0*0!0', null);return false;" name="form2" enctype="multipart/form-data">
	<table style="border-top: 1px solid #800000; background-color: #d7c5bf; padding-top: 10px; padding-bottom: 10px;" border="0" cellpadding="2" width="100%">
		<tbody>
			<tr>
				<td style="font-size: 13px; text-align: center; color: black;" width="100%">
					<c:out value="${pagination}" escapeXml="No"/>&nbsp;&nbsp;
					<input type="text" size="25" name="token" class="BUTTON" value="<c:out value="${param.token}" />"/>&nbsp;<input type="button" name="btnSearch" class="BUTTON" value="<fmt:message key="general.search" />" onclick="document.getElementById('token').value=this.form.token.value;Bookmark.displayLocation('search:0*0!0', null);" />
				</td>					
			</tr>
		</tbody>
	</table>
</form>