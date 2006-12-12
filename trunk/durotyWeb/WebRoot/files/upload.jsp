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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />">

	<head>
		<base href="<%=basePath%>" />
	</head>
	<body>
		<SCRIPT LANGUAGE="JavaScript">
			if (window == top) {
				alert('It is not allowed to see the frame individually');
				top.location.href = "<%=basePath%>files/";
			}
		</SCRIPT>
		<c:choose>
			<c:when test="${!empty exception}">
				<script language="javascript" type="text/javascript">
					parent.document.getElementById("divInfo").innerHTML = 'Error: <fmt:message key="${exception}" />';
					parent.document.getElementById("divInfo").className = "showError";
					parent.hideLoading();
				</script>
			</c:when>
			<c:otherwise>
				<script language="javascript" type="text/javascript">
					//parent.document.getElementById("divInfo").innerHTML = '<fmt:message key="mail.delete.ok" />';
					//parent.document.getElementById("divInfo").className = "showInfo";
					parent.hideLoading();
					parent.showHideElement('tableUpload');
					setTimeout("parent.Files.getFiles(-1, -1, -1, -1, -2, -1)", 500);
				</script>
			</c:otherwise>
		</c:choose>
	</body>
</html>
