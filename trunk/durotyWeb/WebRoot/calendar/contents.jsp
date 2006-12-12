<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div style="">
	BUTTONS & ACTIONS
</div>
<b class="rtop"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
<div id="merda" style="border-left: 10px solid #c3d9ff;">
	<div id="merda2" style="overflow: auto; width: 99.9%">
		<table border="0" cellpadding="0" cellspacing="0" style="width: 99.9%;">
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
				<td class="ctopcol2">asdasdas<br/>asdasdasd&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>
				<td class="ctopcol2">&nbsp;</td>		
			</tr>
			
			<tr>
				<td colspan="8" style="background: #c3d9ff; width: 100%; height: 5px;"></td>
			</tr>		
		
			<c:forEach begin="0" end="23" var="i">
				<tr>
					<td class="ccolleft"><c:out value="${i}" />:00</td>
					<td onclick="alert('1 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>
					<td onclick="alert('2 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>
					<td onclick="alert('3 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>
					<td onclick="alert('4 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>
					<td onclick="alert('5 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>
					<td onclick="alert('6 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>
					<td onclick="alert('7 / <c:out value="${i}" />');" style="border: 1px solid #c3d9ff; height:50px; width: 13%;">&nbsp;</td>		
				</tr>
			</c:forEach>
		</table>
	</div>
</div>
<div style="border-left: 10px solid #c3d9ff; height: 5px; background-color: #c3d9ff;">
</div>
<b class="rbottom"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>