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

<div id="lab0" class="labelSelected" onclick="parent.frames['body'].Files.checkTrash();parent.frames['body'].Files.getFiles(-1, 0, 0, -1, -2, '');FilesLeft.parseLeft('lab0');"><fmt:message key="files.all" /></div>
<c:forEach var="label" items="${labels}">	
	<div id="lab<c:out value="${label.idint}" />" class="label" onclick="parent.frames['body'].Files.checkTrash();parent.frames['body'].Files.getFiles(-1, <c:out value="${label.idint}" />, 0, -1, -2, '');FilesLeft.parseLeft('lab<c:out value="${label.idint}" />');">
		<c:out value="${label.name}" />
	</div>	
</c:forEach>