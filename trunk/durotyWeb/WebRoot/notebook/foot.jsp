<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
		%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:message key="foot.contact" />
<br />
<br />
<a href="http://www.spreadfirefox.com/?q=affiliates&amp;id=0&amp;t=64" target="_blank">
	<img border="0" alt="Get Firefox!" title="Get Firefox!" src="<%=basePath%>images/getfirefox.gif" />
</a>