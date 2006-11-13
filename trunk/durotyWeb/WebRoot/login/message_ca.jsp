<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Locale" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
request.setAttribute("auxlocale", new Locale("ca"));
%>

<fmt:setLocale value='${auxlocale}' scope="session" />

<div>
	<p style="font-size: 14px;">
		Duroty és un servei de feeds personals, gestió de correu electrònic, gestió d'enllaços i pròximament disposarà també de gestió d'agenda electrònica i indexació de fitxers de servidor.
	</p>

	<p style="font-size: 14px;">
		Duroty està orientat a petites i mitjanes empreses que necessiten sistemes d'informació i gestió del coneixement per a disminuir la barrera d'accés a la informació.
		<br>
		<br>
		Utilitza sistemes de cerca de missatges simples i avançades semblant al cercador de webs Google, però amb la possibilitat de cercar dins dels fitxers adjunts (word, powerpoint, excel, pdf, html, etc.) a més a més dels camps típics (from, to, subject,
		body, etc.)
		<br>
	</p>
	<p style="font-size: 14px;">
		La principal funcionalitat i utilitat de duroty és la gestió del correu electrònic. Duroty indexa el contingut dels attachments dels missatges de correu dels usuaris, essent possible fer cerques per contingut en aquests fitxers.&nbsp;

	</p>
	<p style="font-size: 14px;">
		La interfície de consulta de totes les aplicacions utilitza tecnologia AJAX (combinació de DHTML, CSS, Javascript, XML i llenguatges de servidor). A més la interfície es presenta inicialment amb 3 idiomes (Català, Castellà i Anglès), essent fàcil la
		traducció a altres idiomes.
	</p>
	<p style="font-size: 14px;">
		El servidor està desenvolupat íntegrament en Java.
	</p>
	<p style="font-size: 14px;">
		Duroty suporta els navegadors Firefox, Internet Explorer i Mozilla
	</p>

	<p style="font-size: 14px;;">
		Per més informació pot consultar la documentación tècnica <a href="http://www.duroty.com" target="_blank">AQUÍ</a> o bé escriure a <a href="mailto:duroty@duroty.com">duroty@duroty.com</a>.
	</p>
</div>
