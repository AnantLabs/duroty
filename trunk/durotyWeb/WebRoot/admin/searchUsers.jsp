<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<table width="100%" border="0" cellspacing="0" cellpadding="0">	
	<tr>
		<td valign="top" align="right" style="padding-right: 10px;">
			<c:out value="${pagination}" escapeXml="No"/>
		</td>
	</tr>
</table>
<c:choose>
	<c:when test="${!empty durotyUsers}">
		<html:form action="admin/deleteUsers.drt" method="post" style="padding: 0px; margin: 0px;" target="util" enctype="multipart/form-data">
			<table border="0" cellspacing="0" cellpadding="0">			
				<c:forEach items="${durotyUsers}" var="item">
					<tr>
						<td valign="top" nowrap="nowrap">
							<html:multibox property="idints">
								<c:out value="${item.idint}" />
							</html:multibox>
						</td>
						<td style="padding-left: 10px; padding-bottom: 3px;">
							<span class="linkUnderline" style="font-size: 13px;" onclick="Admin.updateUser(<c:out value="${item.idint}" />);">
								<c:out value='${item.name}' /> &lt;<c:out value='${item.email}' />&gt;
							</span>&nbsp;(<c:out value='${item.usedQuota}' />)
						</td>
					</tr>
				</c:forEach>			
			</table>		
		
			<table border="0" cellspacing="0" cellpadding="0">
				<tr><td style="padding-top: 20px;" colspan="2"></td></tr>
				<tr>
					<td valign="top" nowrap="nowrap">
						<input type="button" class="BUTTON" name="button" value="<fmt:message key="general.selectAll" />" onclick="Admin.selectAll(this.form);" />
					</td>
					<td style="padding-left: 10px; padding-bottom: 3px;">
						<input type="submit" class="BUTTON" value="<fmt:message key="general.delete.selected" />" />
					</td>
				</tr>
			</table>
		</html:form>	
	</c:when>
	<c:otherwise>
		<fmt:message key="ErrorMessages.general.datanotfound" />
	</c:otherwise>
</c:choose>
