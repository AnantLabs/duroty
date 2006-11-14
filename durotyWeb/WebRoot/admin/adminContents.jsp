
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

			%>

<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- CONTENTS -->
<div id="divInfo">
</div>
<br/>
<div id="idFormSettings">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formSettings">		
	<tr>
	<td width="100%" class="area" style="font-size: 13px; font-weight: bold;">
	<fmt:message key="admin.users.management" />
	</td>
	</tr>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formSettings">		
		<tr>
			<td width="100%" class="area">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="35%" valign="top">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td valign="top" nowrap="nowrap">
										<input type="text" name="token" value="" id="token"/>
										&nbsp;&nbsp;
										<input type="button" name="button" class="BUTTON" value="<fmt:message key="admin.search.users" />" onclick="Admin.displayLocation('searchUsers:0*0!0', null);" />
										&nbsp;&nbsp;
										<input type="button" class="BUTTON" name="addUser" value="<fmt:message key="admin.addUser" />" onclick="Admin.insertUser();" />
										&nbsp;&nbsp;
									</td>
									<td width="110">
										<div id="cornerLoading">
											<fmt:message key="general.loading" />
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="65%" valign="top">
						</td>
					</tr>
					<tr>
						<td width="35%"><hr style="border: solid 1px #38afe2;"/></td>
						<td width="65%"><hr style="border: solid 1px #38afe2;"/></td>
					</tr>
					<tr>
						<td width="35%" valign="top">
							<div id="users"></div>								
						</td>
						<td width="65%" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="100%" style="padding-left: 10px;">
										<div id="insertUser" class="hide">											
										</div>
										<div id="updateUser" class="hide">											
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="100%" class="title">
			</td>
		</tr>
	</table>
</div>
<!-- FI CONTENTS -->
