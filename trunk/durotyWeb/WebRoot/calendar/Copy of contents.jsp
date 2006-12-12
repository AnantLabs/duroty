<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<b class="rtop"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
<div style="border-left: 10px solid #007ed1; height: 25px; background-color: #007ed1;">
	BUTTONS & ACTIONS
</div>
<div id="merda" style="border-left: 10px solid #007ed1;">
	<div id="merda2" style="border: 1px solid #000; overflow: auto; width: 99.7%">
		<div>
			<div style="float: left; width: 7%;">&nbsp;</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Dilluns</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Dimarts</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Dimecres</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Dijous</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Divendres</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Dissabte</div>
			<div style="border: 1px solid #C7E1F3; float: left; width: 13%;">Diumenge</div>		
		</div>
		
		<div>
			<div style="float: left; width: 7%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>
			<div style="background-color: #ccc; border: 1px solid #C7E1F3; float: left; width: 13%;">&nbsp;</div>		
		</div>
		
		<div>
			<div style="float: left; width: 7%;">&nbsp;</div>
			<div style="float: left; width: 13%;">&nbsp;<br/></div>
			<div style="float: left; width: 13%;">&nbsp;</div>
			<div style="float: left; width: 13%;">&nbsp;</div>
			<div style="float: left; width: 13%;">&nbsp;</div>
			<div style="float: left; width: 13%;">&nbsp;</div>
			<div style="float: left; width: 13%;">&nbsp;</div>
			<div style="float: left; width: 13%;">&nbsp;</div>
		</div>
		
		<c:forEach begin="0" end="23" var="i">
			<div>
				<div style="border: 1px solid #C7E1F3; height:50px; float: left; width: 7%;"><c:out value="${i}" />:00</div>
				<div onclick="alert('1 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>
				<div onclick="alert('2 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>
				<div onclick="alert('3 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>
				<div onclick="alert('4 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>
				<div onclick="alert('5 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>
				<div onclick="alert('6 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>
				<div onclick="alert('7 / <c:out value="${i}" />');" style="border: 1px solid #C7E1F3; height:50px; float: left; width: 13%;">&nbsp;</div>		
			</div>
		</c:forEach>
	</div>
</div>
<div style="border-left: 10px solid #007ed1; height: 5px; background-color: #007ed1;">
</div>
<b class="rbottom"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>