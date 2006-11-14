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

<br/>
<div id="folders" class="folders">
	<table class="labelsContents" cellpadding="1" cellspacing="0" width="100%">
	<tr>
	<td style="overflow: hidden; width: 100%; white-space: nowrap; font-size: 100%; empty-cells: show; font-family: arial,sans-serif;">
	<div id="labels">	
	<c:forEach var="keyword" items="${keywords}">
		<div class="folder" id="lab<c:out value="${keyword}" />"  onclick="javascript:Notebook.displayLocation('label<c:out value="${keyword}" />:0*0!0', null);">
			<c:out value="${keyword}" />
		</div>
	</c:forEach>
	</div>
	</td>
	</tr>
	</table>
</div>