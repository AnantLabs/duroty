<?xml version="1.0" encoding="UTF-8"?>

<%
response.setContentType("text/xml");
%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />
<c:choose>
	<c:when test="${!empty exception}">
		<error>
			<![CDATA[
			<c:out value="${exception}" />
			]]>
		</error>
	</c:when>
	<c:otherwise>
		<alert>		
			<newMessages>					
				<newMessageCountInbox value="<c:out value='${counters.inbox}' />" />
				<newMessageCountSpam value="<c:out value='${counters.spam}' />" />
			</newMessages>
		
			<quota>
				<![CDATA[
				<c:out value="${counters.quota}" escapeXml="No"/>
				]]>
			</quota>
			
			<newLabels>
				<c:forEach items="${counters.labels}" var="label">					
					<label idint="<c:out value='${label.idint}' />" count="<c:out value='${label.count}' />" />					
				</c:forEach>
			</newLabels>								
		</alert>
	</c:otherwise>
</c:choose>
