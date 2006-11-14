<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<html:form action="login/register.drt" method="post" style="padding: 0px; margin: 0px;" enctype="multipart/form-data">
	<table width="100%" border="0" cellspacing="0" cellpadding="4" style="background-color: #eeeeee;">
		<c:if test="${!empty exception}">
			<tr>
				<td width="100%" colspan="2" style="font-size: 13px; color: red; text-align: center;">
					<fmt:message key="${exception}" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
			</td>
			<td width="100%">
				<img src="jcaptcha" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="general.captcha.code" />
			</td>
			<td width="100%">
				<input type='text' name='j_captcha_response' size="30" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.username" />
			</td>
			<td width="100%">
				<html:text property="username" size="30" maxlength="200" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				
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
				
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.name" />
			</td>
			<td width="100%">
				<html:text property="name" size="30" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				<fmt:message key="admin.contactEmail" />
			</td>
			<td width="100%">
				<html:text property="email" size="30" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				
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
				
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
				
			</td>
			<td width="100%">
				<html:submit><fmt:message key="general.send" /></html:submit>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top: 5px; padding-bottom: 5px;">
				
			</td>
		</tr>
	</table>
</html:form>
