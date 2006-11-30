<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

		%>

<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="divAdvancedSearch" class="hide" style="margin-left: 10px; margin-right: 10px; padding-top: 10px;">
	<FORM action="" method="get" name="formAdvancedSearch" onsubmit="Mail.displayLocation('advancedSearch:0*0!0', null);return false;" style="margin: 0px; padding: 0px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="2" style="border: 4px solid #fbcc4b;" align="center">
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellspacing="3" cellpadding="4" style="background-color: #fbf9c1;">
						<tr>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.from" />:&nbsp;</b>
							</td>
							<td width="50%" style="text-align: left;">
								<input type="text" name="from" size="20" style="width: 90%;" />
							</td>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.hasWords" />:&nbsp;</b>
							</td>
							<td width="50%" nowrap="nowrap" style="font-size: 10px;">
								<input type="text" name="hasWords" size="20" style="width: 50%;" />&nbsp;<input type="checkbox" name="hasWordsInBody" checked="checked" />&nbsp;<fmt:message key="mail.hasWords.inBody" />&nbsp;<input type="checkbox" name="hasWordsInAttachment" checked="checked" />&nbsp;<fmt:message key="mail.hasWords.inAttachments" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.to" />:&nbsp;</b>
							</td>
							<td width="50%" style="text-align: left;">
								<input type="text" name="to" size="20" style="width: 90%;" />
							</td>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.fileType" />:&nbsp;</b>
							</td>
							<td width="50%">
								<select size="1" name="filetype">
									<option value="" selected="selected"></option>
									<option value="text/plain">.txt</option>
									<option value="text/html">.html</option>
									<option value="text/xml">.xml</option>
									<option value="application/pdf">.pdf</option>
									<option value="application/msword application/x-msword">.doc</option>
									<option value="application/vnd.ms-powerpoint">.ppt</option>
									<option value="application/vnd.ms-excel application/msexcell application/msexcel">.xls</option>
									<option value="application/msaccess">.mdb</option>
									<option value="image/jpeg image/pjpeg">.jpeg</option>
									<option value="image/gif">.gif</option>
									<option value="image/bmp">.bmp</option>
									<option value="application/x-zip-compressed application/zip">.zip</option>
									<option value="application/rar">.rar</option>
									<option value="application/x-gzip">.gzip</option>
									<option value="message/rfc822">.rfc822</option>
									<option value="video/x-ms-wmv">.wmv</option>
									<option value="audio/mpeg">.mpeg</option>
									<option value="application/vnd.sun.xml.calc">.calc</option>
									<option value="application/vnd.sun.xml.writer">.writer</option>
									<option value="application/vnd.oasis.opendocument.presentation">.presentation</option>
									<option value="application/vnd.oasis.opendocument.text">.opendocument</option>
									<option value="image/x-psd">.psd</option>
									<option value="application/x-mozilla-bookmarks">.bookmarks</option>
									<option value="audio/x-ms-wma">.wma</option>
									<option value="video/x-ms-asf">.asf</option>
									<option value="application/octet-stream">.octet-stream</option>
									<option value="message/delivery-status">.delivery-status</option>
								</select>
							</td>
						</tr>
						<tr>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.subject" />:&nbsp;</b>
							</td>
							<td width="50%" style="text-align: left;">
								<input type="text" name="subject" size="20" style="width: 90%;" />
							</td>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.hasAttachment" />:&nbsp;</b>
							</td>
							<td width="50%">
								<input type="checkbox" name="hasAttachment" />
							</td>
						</tr>
						<tr>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.labels" />:&nbsp; </b>
							</td>
							<td width="50%" style="text-align: left;">
								<select name="label" size="1" style="width: 90%;">
									<option value="0" selected="selected">
										<fmt:message key="mail.label.select" />
									</option>
									<c:forEach var="label" items="${labels}">
										<option value="<c:out value="${label.idint}" />"><c:out value="${label.name}" /></option>
									</c:forEach>
								</select>
							</td>
							<td style="text-align: right;" nowrap="nowrap">
								<select name="fixDate" size="1">
									<option value="0" selected="selected">
										<fmt:message key="mail.date" />
									</option>
									<option value="1">
										<fmt:message key="mail.date.today" />
									</option>
									<option value="2">
										<fmt:message key="mail.date.1day" />
									</option>
									<option value="3">
										<fmt:message key="mail.date.lastWeek" />
									</option>
									<option value="4">
										<fmt:message key="mail.date.lastMonth" />
									</option>
								</select>
							</td>
							<td width="50%">
								<img src="<%=basePath%>images/cal.gif" id="trigger" style="cursor: pointer; border: 1px solid red;" title="Seleccionar Fechas" onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />
								&nbsp;<fmt:message key="mail.date.begin" />:
								<input type="text" name="startDate" size="10" />
								&nbsp;<fmt:message key="mail.date.end" />:
								<input type="text" name="endDate" size="10" />
							</td>
	
						</tr>
						<tr>
							<td style="text-align: right;" nowrap="nowrap">
								<b><fmt:message key="mail.boxes" />:&nbsp;</b>
							</td>
							<td width="50%" style="text-align: left;">
	
								<select name="box" size="1" style="width: 90%;">
									<option value="0" selected="selected">
										<fmt:message key="mail.selectBox" />
									</option>
									<c:forEach var="folder" items="${folders}">
										<option value="<c:out value="${folder.name}" />"><fmt:message key="folder.${folder.name}" /></option>
									</c:forEach>								
								</select>
							</td>
							<td style="text-align: right;" nowrap="nowrap"></td>
							<td width="50%" style="text-align: right;">
								<input type="submit" name="" class="BUTTON" value="<fmt:message key="general.search" /> "/>
								&nbsp;&nbsp;
								<input type="reset" name="" class="BUTTON" value="<fmt:message key="general.form.reset" /> "/>
								&nbsp;&nbsp;
								<input type="button" class="BUTTON" value="<fmt:message key="mail.advancedSearch.hide" />" onClick="showHideElement('divAdvancedSearch')" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</FORM>
</div>

<script type="text/javascript">
	//<![CDATA[
	// the default multiple dates selected, first time the calendar is instantiated
	var MA = [];
    
    function catcalc(cal) {
		var date = cal.date;
		var time = date.getTime()
		// use the _other_ field
		var field = document.getElementById("f_calcdate");
		if (field == cal.params.inputField) {
		    field = document.getElementById("f_date_a");
		    time -= Date.WEEK; // substract one week
		} else {
		    time += Date.WEEK; // add one week
		}
		var date2 = new Date(time);
		field.value = date2.print("%Y-%m-%d %H:%M");
    }

	function closed(cal) {
		// here we'll write the output; this is only for example.  You
		// will normally fill an input field or something with the dates.
		var auxStartDate = document.formAdvancedSearch.startDate;
		var auxEndDate = document.formAdvancedSearch.endDate;

		// reset initial content.
		auxStartDate.value = "";
		auxEndDate.value = "";

		// Reset the "MA", in case one triggers the calendar again.
		// CAREFUL!  You don't want to do "MA = [];".  We need to modify
		// the value of the current array, instead of creating a new one.
		// Calendar.setup is called only once! :-)  So be careful.
		MA.length = 0;

		// walk the calendar's multiple dates selection hash      
		//auxStartDate.value = cal.multiple[0].print("%A, %Y %B %d");
		//auxEndDate.value = cal.multiple[1].print("%A, %Y %B %d");
		
		// walk the calendar's multiple dates selection hash
		var contable = 0;
		for (var i in cal.multiple) {
			var d = cal.multiple[i];
			// sometimes the date is not actually selected, that's why we need to check.
			if (d) {
				// OK, selected.  Fill an input field.  Or something.  Just for example,
				// we will display all selected dates in the element having the id "output".
				//el.innerHTML += d.print("%A, %Y %B %d") + "<br />";
				try {
					if (contable == 0) {
						auxStartDate.value = d.print("%d/%m/%Y");
					} else if (contable == 1) {
						auxEndDate.value = d.print("%d/%m/%Y");
					} else {
						break;
					}
				} catch (e) {}
				
				contable++;

				// and push it in the "MA", in case one triggers the calendar again.
				MA[MA.length] = d;
			}
		}
		
		cal.hide();
		
		return true;
	};

	Calendar.setup({
		showOthers : true,
		ifFormat   : "%d/%m/%Y",
		multiple   : MA, // pass the initial or computed array of multiple dates to be initially selected
		onClose    : closed,
		button     : "trigger"
	});
	//]]>
</script>