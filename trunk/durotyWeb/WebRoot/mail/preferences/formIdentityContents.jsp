
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

<div id="divInfo">
</div>
<br />
<div id="idFormSettings">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formSettings">
		<tr>
			<td width="100%" class="title">
				<span style="padding-left: 10px;">
					<a href="<%=basePath%>mail/preferences/settings.drt"><fmt:message key="mail.settings" /></a>
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
				<span class="titleActive" style="padding-left: 10px; padding-right: 10px;">
					<fmt:message key="mail.identities" />
				</span>
			</td>
		</tr>
		<tr>
			<td width="100%" class="area">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="formFilter">
					<tr>
						<td width="100%">
							<h3>
								<fmt:message key="mail.identity.create.title" />
							</h3>
						</td>
					</tr>
					<tr>
						<td width="100%">
							<hr style="border: solid 1px #38afe2;" />
						</td>
					</tr>
					<tr>
						<td width="100%">
							<html:form action="mail/preferences/createIdentity.drt" method="post" onsubmit="return true;" style="padding: 0px; margin: 0px;" target="util" enctype="multipart/form-data">
								<table border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td nowrap="nowrap">
											<b><fmt:message key="mail.identity.name" />:</b> &nbsp;&nbsp;
										</td>
										<td nowrap="nowrap">
											<html:text property="name" maxlength="100" size="30" />
										</td>
									</tr>
									<tr>
										<td nowrap="nowrap">
											<b><fmt:message key="mail.identity.email" />:</b> &nbsp;&nbsp;
										</td>
										<td nowrap="nowrap">
											<html:text property="email" maxlength="100" size="30" />
										</td>
									</tr>
									<tr>
										<td nowrap="nowrap">
											<b><fmt:message key="mail.identity.replyTo" />:</b> &nbsp;&nbsp;
										</td>
										<td nowrap="nowrap">
											<html:text property="replyTo" maxlength="100" size="30" />
										</td>
									</tr>
									<tr>
										<td nowrap="nowrap">
											<b><fmt:message key="mail.identity.default" />:</b> &nbsp;&nbsp;
										</td>
										<td nowrap="nowrap">
											<html:checkbox property="default" value="true" />
										</td>
									</tr>
									<tr>
										<td nowrap="nowrap">
										</td>
										<td nowrap="nowrap">
											<html:submit styleClass="BUTTON">
												<fmt:message key="mail.identity.create" />
											</html:submit>
										</td>
									</tr>
								</table>
							</html:form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>