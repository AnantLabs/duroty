<%response.setContentType("text/plain");%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><c:forEach items="${feeds}" var="feed"><c:out value="${feed}" />
</c:forEach>