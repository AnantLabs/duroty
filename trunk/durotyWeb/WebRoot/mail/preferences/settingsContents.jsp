<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
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
			<td width="100%" class="title">
				<span class="titleActive" style="padding-left: 10px;">
					<fmt:message key="mail.settings" />
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/contacts.drt"><fmt:message key="mail.contacts" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/labels.drt"><fmt:message key="mail.labels" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/filters.drt"><fmt:message key="mail.filters" /></a>
				</span>
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/identities.drt"><fmt:message key="mail.identities" /></a>
				</span>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<html:form action="mail/preferences/updateSettings.drt" method="post" onsubmit="return true;" style="padding: 0px; margin: 0px;" target="util" enctype="multipart/form-data">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
									<fmt:message key="mail.settings.name" />
							</td>
							<td width="100%">
								<html:text property="name" size="50" maxlength="255"/>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
									<fmt:message key="mail.settings.contactEmail" />
							</td>
							<td width="100%">
								<html:text property="contactEmail" size="50" maxlength="255"/>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.signature" />
							</td>
							<td width="100%">
								<html:textarea property="signature" style="width: 100%; height: 100px;"></html:textarea>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.language" />
							</td>
							<td width="100%">
								<html:select property="language" size="1">
									<html:option value="en">English</html:option>
									<html:option value="ca">Català</html:option>
									<html:option value="es">Castellano</html:option>
								</html:select>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.vacationResponderActive" />
							</td>
							<td width="100%">
								<html:checkbox property="vacationResponderActive" />
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.vacationResponderSubject" />
							</td>
							<td width="100%">
								<html:text property="vacationResponderSubject" size="50" maxlength="255"/>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.vacationResponderBody" />
							</td>
							<td width="100%">
								<html:textarea property="vacationResponderBody" style="width: 100%; height: 100px;"></html:textarea>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.htmlMessage" />
							</td>
							<td width="100%">
								<html:checkbox property="htmlMessage" />
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">	
								<fmt:message key="mail.settings.byPage" />
							</td>
							<td width="100%">
								<html:text property="byPage" size="10" maxlength="3"/>
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">
								<fmt:message key="mail.settings.spamTolerance" />
							</td>
							<td width="100%">
								<html:checkbox property="spamTolerance" />
							</td>
						</tr>
						<tr><td colspan="2" style="padding-top: 5px; padding-bottom: 5px;"><hr style="border: solid 1px #38afe2;"/></td></tr>
						<tr>
							<td nowrap="nowrap" style="font-size:13px; padding-top: 2px; padding-bottom: 2px; padding-left: 15px;">								
							</td>
							<td width="100%">
								<html:submit>
									<fmt:message key="mail.settings.update" />
								</html:submit>
							</td>
						</tr>
					</table>
				</html:form>
			</td>
		</tr>
		<tr>
			<td width="100%" class="title">
			</td>
		</tr>
	</table>
</div>
<!-- FI CONTENTS -->
