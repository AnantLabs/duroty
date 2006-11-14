<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" lang="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />">

	<head>
		<base href="<%=basePath%>" />
		<title>Duroty System</title>
		<meta http-equiv="keywords" content="jordi marquès, duroty, gmail, lucene, jboss, mail, james, tomcat, pop3, imap, java, email, webmail, network" />
		<meta http-equiv="description" content="Duroty is a service of personal feeds, email management, link management and briefly it will also include electronic calendar and server file indexation. Duroty is addressed to SMEs which need information and knowledge management systems in order to diminish the barrier to the access to information." />
		<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
		<meta name="ROBOTS" content="INDEX, FOLLOW" />
		<meta name="ROBOTS" content="INDEX, ALL" />
		<meta name="language" content="<c:out value="${sessionScope['org.apache.struts.action.LOCALE']}" />" />		

		<style type="text/css">	
			/* START CSS NEEDED ONLY IN DEMO */
			body, table, td, tr, div {
				font-family: Arial;	
				font-size: 14px;
			}
			
			div#layout{background: #dcf6db}
			
			b.rtop, b.rbottom{display:block;background: #FFF}
			b.rtop b, b.rbottom b{display:block;height: 1px;
			    overflow: hidden; background: #dcf6db}
			b.r1{margin: 0 5px}
			b.r2{margin: 0 3px}
			b.r3{margin: 0 2px}
			b.rtop b.r4, b.rbottom b.r4{margin: 0 1px;height: 2px}
			
			.BUTTON {
				border: 1px solid black;
			}
		</style>
		
		<!-- tinyMCE -->
		<script language="javascript" type="text/javascript" src="<%=basePath%>javascript/tiny_mce/tiny_mce_src.js"></script>
		
		<script language="javascript" type="text/javascript">
			tinyMCE.init({
				mode : "exact",
				theme : "simple",
				elements: "comments",
				content_css : "<%=basePath%>style/bookmarklet_htmlarea_full.css"
			});
		</script>
		<!-- /tinyMCE -->
	</head>

	<div id="layout">
		<b class="rtop"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
		<span style="font-size: 16px; padding-left: 10px; font-weight: bold;"><fmt:message key="bookmark.insert" /></span>
		<table width="100%" border="0" cellspacing="0" cellpadding="4">
			<tr>
				<td width="100%">
					<form action="<%=basePath%>bookmark/insertBookmarklet.drt" method="post" style="margin: 0px; padding: 0px;" name="bookmarklet" enctype="multipart/form-data">
						<table width="100%" cellspacing="2" cellpadding="2" border="0">
							<tr>
								<td nowrap="nowrap">
									<fmt:message key="bookmark.url" />
									:
								</td>
								<td width="100%">
									<input type="text" name="url" size="30" value="<c:out value='${param.url}' />" maxlength="255" style="width: 99%">
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap">
									<fmt:message key="bookmark.title" />
									:
								</td>
								<td width="100%">
									<input type="text" name="title" value="<c:out value='${param.title}' />" size="20" maxlength="100" style="width: 99%">
								</td>
							</tr>
							<tr>
								<td nowrap="nowrap">
									<fmt:message key="bookmark.keywords" />
									:
								</td>
								<td width="100%">
									<input type="text" name="keywords" value="" size="20" maxlength="100" style="width: 99%">
								</td>
							</tr>
							<c:if test="${!empty keywords}">
								<tr>
									<td nowrap="nowrap">
										<fmt:message key="bookmark.keywords" />
										:
									</td>
									<td width="100%">
										<select size="1" name="aux" onchange="document.bookmarklet.keywords.value=this.options[this.selectedIndex].value">
											<option value="" selected="selected"></option>
											<c:forEach items="${keywords}" var="keyword">
												<option value="<c:out value="${keyword}" />">
													<c:out value="${keyword}" />
												</option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</c:if>
							<tr>
								<td nowrap="nowrap">
									<fmt:message key="bookmark.starred" />
									:
								</td>
								<td width="100%">
									<input type="checkbox" name="flagged" value="true" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<fmt:message key="bookmark.comments" />
									:
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<textarea name="comments" style="border: 1px solid black; width: 99%; height: 150px;"></textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: center; padding-top: 20px;">
									<input type="submit" class="BUTTON" value="<fmt:message key="general.send" />" class="buttonAddBookmark" />
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
		<b class="rbottom"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
	</div>
</html>
