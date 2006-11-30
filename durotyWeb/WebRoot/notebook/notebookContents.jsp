<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

			%>	

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="layout">
	<b class="rtop<c:out value="${clazz}" />"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorder">
		<tr>
			<td width="100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableHeader">
					<tr>
						<td width="100%" class="title">
							<input type="button" value="<fmt:message key="bookmark.insert" />" onclick="javascript:openwin('<%=basePath%>bookmark/bookmarklet.drt', 'Bookmarklet', 700, 500);"/>
						</td>	
					</tr>
					<tr>
						<td width="100%" class="area">
							<div id="results" class="results"></div>
						</td>
					</tr>
					<tr>
						<td width="100%" class="title">
							<input type="button" value="<fmt:message key="bookmark.insert" />" onclick="javascript:openwin('<%=basePath%>bookmark/bookmarklet.drt', 'Bookmarklet', 700, 500);"/>
						</td>	
					</tr>	
				</table>
			</td>
		</tr>
	</table>
	<b class="rbottom<c:out value="${clazz}" />"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
</div>

<script type="text/javascript" language="javascript">
	function createBookmarkFromForm(formObj) {
		var controlBookmarkSubmit = false;
	
		if (controlBookmarkSubmit) {
			alert('<fmt:message key="bookmark.controlSubmit" />');
			return false;
		} 
	
		if (formObj.url.value == null || formObj.url.value == "") {
			formObj.url.focus();
			alert('<fmt:message key="bookmark.url.required" />');
			return false;
		}
		
		formObj.submit();
	}
	
	function createBookmarkFromFile(formObj) {
		var controlBookmarkSubmit = false;
	
		if (controlBookmarkSubmit) {
			alert('<fmt:message key="bookmark.controlSubmit" />');
			return false;
		} 
	
		if (formObj.file.value == null || formObj.file.value == "") {
			formObj.file.focus();
			alert('<fmt:message key="bookmark.file.required" />');
			return false;
		}
		
		formObj.submit();
	}
	
	function flag(idint) {
		document.formAux.idint.value = idint;		
		document.formAux.action = "<%=basePath%>bookmark/flagBookmark.drt";
		document.formAux.submit();
	}
	
	function unflag(idint) {
		document.formAux.idint.value = idint;		
		document.formAux.action = "<%=basePath%>bookmark/unflagBookmark.drt";
		document.formAux.submit();
	}
	
	function deleteBookmark(idint) {
		if (!confirm('<fmt:message key="bookmark.delete" />')) {
			return;
		}
		document.formAux.idint.value = idint;		
		document.formAux.action = "<%=basePath%>bookmark/deleteBookmark.drt";
		document.formAux.submit();
	}
</script>