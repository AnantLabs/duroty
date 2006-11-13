<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<html:form action="admin/insertUser.drt" method="post" target="util" style="padding: 0px; margin: 0px;" enctype="multipart/form-data">
	<html:hidden property="idint" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.username" />
			</td>
			<td width="100%">
				<html:text property="username" size="50" maxlength="200" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.password" />
			</td>
			<td width="100%">
				<html:password property="password" size="15" maxlength="15" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.confirmPassword" />
			</td>
			<td width="100%">
				<html:password property="confirmPassword" size="15" maxlength="15" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.name" />
			</td>
			<td width="100%">
				<html:text property="name" size="50" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.contactEmail" />
			</td>
			<td width="100%">
				<html:text property="email" size="50" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.emailIdentity" />
			</td>
			<td width="100%">
				<html:text property="emailIdentity" size="50" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.signature" />
			</td>
			<td width="100%">
				<html:textarea property="signature" style="width: 90%; height: 150px"></html:textarea>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.language" />
			</td>
			<td width="100%">
				<html:select property="language" size="1">
					<html:option value="en">English</html:option>
					<html:option value="ca">Català</html:option>
					<html:option value="es">Castellano</html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.vacationActive" />
			</td>
			<td width="100%">
				<html:checkbox property="vacationActive" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.vacationSubject" />
			</td>
			<td width="100%">
				<html:text property="vacationSubject" size="50" maxlength="255"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.vacationBody" />
			</td>
			<td width="100%">
				<html:textarea property="vacationBody" style="width: 90%; height: 150px"></html:textarea>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.htmlMessages" />
			</td>
			<td width="100%">
				<html:checkbox property="htmlMessages" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.byPage" />
			</td>
			<td width="100%">
				<html:text property="byPage" size="10" maxlength="3"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.spamTolerance" />
			</td>
			<td width="100%">
				<html:checkbox property="spamTolerance" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.roles" />
			</td>
			<td width="100%">
				<html:select property="roles" multiple="multiple" size="5">
					<c:forEach items="${userRoles}" var="role">
						<html:option value="${role.idint}"><c:out value="${role.name}" /></html:option>
					</c:forEach>
				</html:select>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.quotaSize" />
			</td>
			<td width="100%">
				<html:text property="quotaSize" size="50" maxlength="200" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.active" />
			</td>
			<td width="100%">
				<html:checkbox property="active" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				
			</td>
			<td width="100%">
				<html:submit><fmt:message key="admin.update" /></html:submit>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				<hr style="border: solid 1px #38afe2;" />
			</td>
		</tr>
	</table>
</html:form>
