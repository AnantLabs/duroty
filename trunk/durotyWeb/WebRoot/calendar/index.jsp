<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<tiles:insert template="/template/calendar_template.jsp">

    <tiles:put name="title" direct="true">
        <fmt:message key="general.Title" />
    </tiles:put>
    
    <tiles:put name="toolbar" content="/toolbar.jsp" />
    
    <tiles:put name="left" content="/calendar/left.jsp" />
    
    <tiles:put name="contents" content="/calendar/contents.jsp" />
    
    <tiles:put name="foot" content="/calendar/foot.jsp" />

</tiles:insert>
