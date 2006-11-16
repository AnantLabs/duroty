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

<table style="border-top: 1px solid rgb(0, 128, 0); background-color: rgb(220, 246, 219);" border="0" cellpadding="2" width="100%">
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
	<form action="" method="get" name="formAux" target="util" enctype="multipart/form-data">
		<input type="hidden" name="idint" />
	</form>
</div>

<div id="idFormBookmark" class="hide">
	<br>
	<table style="border: 1px solid rgb(0, 128, 0);" align="center" border="0" cellpadding="3" cellspacing="0" width="100%">
		<tbody>
			<tr>
				<td width="100%">
					<table style="background-color: rgb(239, 239, 239);" border="0" cellpadding="4" cellspacing="0" width="100%">
						<tbody>
							<tr>

								<td width="100%">
									<form action="<%=basePath%>bookmark/insertBookmark.drt" method="post" target="util" style="margin: 0px; padding: 0px;" name="form1" enctype="multipart/form-data">
										<input type="hidden" name="idint" />
										<fmt:message key="bookmark.url" />:&nbsp;
										<input name="url" size="30" value="http://" maxlength="255" type="text" class="BUTTON" />
										&nbsp;&nbsp;
										<fmt:message key="bookmark.title" />:&nbsp;
										<input name="title" value="" size="20" maxlength="100" type="text" class="BUTTON" />
										&nbsp;&nbsp;
										<fmt:message key="bookmark.keywords" />:&nbsp;
										<input name="keywords" value="" size="20" maxlength="100" type="text" class="BUTTON" />
										&nbsp;&nbsp; 
										<fmt:message key="bookmark.starred" />:&nbsp;
										<input name="flagged" value="true" type="checkbox" />
										&nbsp;&nbsp;
										<input onclick="createBookmarkFromForm(this.form)" value="<fmt:message key="general.send" />" class="BUTTON" type="button" />
									</form>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	<br>
	<table style="border: 1px solid rgb(0, 128, 0);" align="center" border="0" cellpadding="3" cellspacing="0" width="100%">
		<tbody>
			<tr>

				<td width="100%">
					<table style="background-color: rgb(239, 239, 239);" border="0" cellpadding="4" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td width="100%">
									<fmt:message key="bookmark.file.title" />
									<form method="post" action="<%=basePath%>bookmark/extractor.drt" style="margin: 0px; padding: 0px;" enctype="multipart/form-data" target="util" name="form3">
										<input name="file" size="30" type="file">
										&nbsp;&nbsp;
										<input onclick="createBookmarkFromFile(this.form)" value="<fmt:message key="general.send" />" class="BUTTON" type="button" />
									</form>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
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
				<div class="bookmarkContents">
					<c:out value="${bookmark.contentsHighlight}" escapeXml="No" />
				</div>
				<div class="bookmarkUrl" title="<c:out value="${bookmark.url}" />">
					<c:out value="${bookmark.url}" />
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
	<table style="border-top: 1px solid rgb(0, 128, 0); border-bottom: 1px solid rgb(0, 128, 0); background-color: rgb(220, 246, 219); padding-top: 10px; padding-bottom: 10px;" border="0" cellpadding="2" width="100%">
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