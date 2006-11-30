<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

			%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="mainContainer">
	<div id="header">	
		<div id="addNewFeed" class="hide">
			<form method="post" action="#" enctype="multipart/form-data">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td colspan="4">
							<strong><fmt:message key="mail.home.newFeed" /></strong>
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							RSS url:
						</td>
						<td colspan="4" width="100%">
							<input type="text" name="rssUrl" style="width: 90%;" value="" maxlength="255">
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap" align="left">
							Items:
						</td>
						<td>
							<input type="text" name="items" value="10" size="2" maxlength="2">
						</td>
						<td nowrap="nowrap" align="left">
							&nbsp;<fmt:message key="mail.home.refresh" />:
						</td>
						<td align="left">
							<input type="text" name="reloadInterval" value="10" size="2" maxlength="2">
						</td>
						<td nowrap="nowrap" align="left">
							&nbsp;<fmt:message key="mail.home.minute" />
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							<fmt:message key="mail.home.height" />:
						</td>
						<td>
							<input type="text" name="height" value="0" size="2" maxlength="3">
						</td>
						<td>
							<input type="button" onclick="createFeed(this.form)" value="<fmt:message key="general.send" />">
						</td>
					</tr>

				</table>
			</form>
		</div>
	</div>
	<div id="floatingBoxParentContainer">

	</div>
</div>
