<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<tiles:insert template="/template/mail_preferences_template.jsp">

    <tiles:put name="title" direct="true">
        <fmt:message key="general.Title" />
    </tiles:put>
    
    <tiles:put name="author" direct="true">
    	<fmt:message key="general.Author" />
    </tiles:put>
    
    <tiles:put name="copyright" direct="true">
        <fmt:message key="general.Copyright" />
    </tiles:put>
    
    <tiles:put name="generator" direct="true">
        <fmt:message key="general.Generator" />
    </tiles:put>
    
    <tiles:put name="keywords" direct="true">
    	<fmt:message key="general.Keywords" />
    </tiles:put>
    
    <tiles:put name="description" direct="true">
    	<fmt:message key="general.Description" />
    </tiles:put>

    <tiles:put name="date" direct="true">
        <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />
        <jsp:useBean id="current" class="java.util.Date" />
        <fmt:formatDate value="${current}" type="date" dateStyle="full"/>
    </tiles:put>

    <tiles:put name="toolbar" content="/toolbar.jsp" />
    
    <tiles:put name="contents" content="/mail/preferences/formUpdateFilterContents.jsp" />

    <tiles:put name="foot" content="/mail/foot.jsp" />

</tiles:insert>