<%@ page import="java.util.Date" %>
<%
response.setHeader("Expires", (new Date((new Date()).getTime() + 315360000)).toString());
response.setHeader("Pragma", "public");
response.setHeader("Cache-Control", "max-age=315360000");
%>

<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";		
			%>


<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<div id="llenya" class="show">
<div id="borderHead">
	<div style="margin-bottom: 5px;">
		<div id="navigation_right" style="text-align:right; float: right; width: 50%; height: auto; margin-left: 0%; margin-right: 5px;">
			<div id="month" style="margin-right: 2px; float:right; background-color: #e8eef7; text-align: center; width: 50px;">Month</div>			
			<div id="week" style="margin-right: 2px; float:right; background-color: #e8eef7; text-align: center; width: 50px;">Week</div>	
			<div id="day" style="margin-right: 2px; float:right; background-color: #e8eef7; text-align: center; width: 50px;">Day</div>		
		</div>
		<div id="navigation_left" style="margin: 0pt 50% 0pt 0pt;">	
			<img src="<%=basePath%>calendar/images/btn_prev.gif" border="0" />&nbsp;<img src="<%=basePath%>calendar/images/btn_next.gif" border="0" />
				<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />
		        <jsp:useBean id="current" class="java.util.Date" />
		        <fmt:formatDate value="${current}" type="both" dateStyle="full"/>
		</div>
	</div>
	<b class="rtop"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
</div>
<div id="contentsMesh" style="border-left: 10px solid #c3d9ff;">
	<div id="contentsHead" style="">
		<table border="0" cellpadding="0" cellspacing="0" style="width: 99.9%; border-right: 15px solid #c3d9ff; table-layout: fixed;">
			<tr>
				<td class="ctopcolleft">&nbsp;</td>
				<td class="ctopcol">Dilluns</td>
				<td class="ctopcol">Dimarts</td>
				<td class="ctopcol">Dimecres</td>
				<td class="ctopcol">Dijous</td>
				<td class="ctopcol">Divendres</td>
				<td class="ctopcol">Dissabte</td>
				<td class="ctopcol">Diumenge</td>		
			</tr>
			
			<tr>
				<td class="ctopcolleft">&nbsp;</td>
				<td class="ctopcol2">		
					<div class="elementAllDay">La merda de la muntanya no fa pudor encara que la remenis amb un bastó</div>			
					<div class="elementAllDay">La merda de la muntanya no fa pudor encara que la remenis amb un bastó</div>
					<div class="elementAllDay">La merda de la muntanya no fa pudor encara que la remenis amb un bastó</div>
				</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">
				</td>
				<td class="ctopcol2">&nbsp;</td>		
			</tr>
			
			<tr>
				<td colspan="8" style="background: #c3d9ff; width: 100%; height: 5px;"></td>
			</tr>		
		</table>
	</div>
	<div id="contentsBody" style="overflow: auto; width: 99.9%">
		<table border="0" cellpadding="0" cellspacing="0" style="width: 99.9%; table-layout: fixed;">		
			<c:forEach begin="0" end="23" var="i">
				<tr>
					<td class="ccolleft"><c:out value="${i}" />:00</td>
					<td onclick="alert('1 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>
					<td onclick="alert('2 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>
					<td onclick="alert('3 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>
					<td onclick="alert('4 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>
					<td onclick="alert('5 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>
					<td onclick="alert('6 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>
					<td onclick="alert('7 / <c:out value="${i}" />');" class="ccol">&nbsp;</td>		
				</tr>
			</c:forEach>
		</table>
	</div>
</div>
<div id="borderFoot">
<div style="border-left: 10px solid #c3d9ff; height: 5px; background-color: #c3d9ff;">
</div>
<b class="rbottom"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
</div>
</div>