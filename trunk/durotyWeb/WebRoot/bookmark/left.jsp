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

<div id="boolmarklet" style="font-size: 13px; padding-bottom: 10px; text-align: center;">
	<a href="javascript:(function(){var a=window,b=document,c=encodeURIComponent,d=a.open(%22<%=basePath%>bookmark/bookmarklet.drt?&url=%22+c(b.location)+%22&title=%22+c(b.title),%22bkmk_popup%22,%22left=%22+((a.screenX||a.screenLeft)+10)+%22,top=%22+((a.screenY||a.screenTop)+10)+%22,height=500px,width=700px,resizable=1,alwaysRaised=1%22);a.setTimeout(function(){d.focus()},300)})();" onclick="alert('<fmt:message key="bookmarklet.instructions" />');return false;">Bookmarklet</a>
</div>

<div id="buttonCompose" style="padding-bottom: 5px; padding-right: 5px;">	
	<input type="button" name="compose" class="buttonCompose" value="<fmt:message key="bookmark.insert" />" onclick="javascript:showHideElement('idFormBookmark');" /><br/>
</div>

<div id="keywords" class="keywords">
	<div class="labelsTitle" onclick="javascript:Bookmark.showHideLabels();" style="cursor: pointer;">
		<img id="imgOpenCloseLabels" src="<%=basePath%>images/opentriangle.gif" border="0" />&nbsp;&nbsp;<fmt:message key="bookmark.keywords" />
	</div>
	<div id="labels" class="labels" style="display: block;">
		<c:forEach var="label" items="${keywords}">
			<div id="lab<c:out value="${label}" />" onclick="javascript:Bookmark.displayLocation('label<c:out value="${label}" />:0*0!0');">
				<font class="label"><c:out value="${label}" /></font>
			</div>
		</c:forEach>
	</div>
</div>