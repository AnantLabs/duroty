<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session" />

<div>
	<p style="font-size: 14px;;">
		<b>Duroty System es el Gmail de código libre.</b>
	</p>
	<p style="font-size: 14px;;">
		Duroty es un servicio de feeds personales, gestión del correo electrónico, gestión de enlaces favoritos y proximamente dispondrá también de gestión de agenda electrónica e indexación de ficheros.
	</p>
	<p style="font-size: 14px;;">
		Duroty está orientado a pequeñas y medianas empresas que necesiten sistemas de información y gestión del conocimiento para disminuir la barrera de acceso a la información.
	</p>
	<p style="font-size: 14px;;">
		Utiliza sistemas de búsqueda de mensajes simples y avanzadas parecido al buscador de webs Google, pero con la posibilidad de buscar dentro de los ficheros adjuntos recibidos (word, powerpoint, excel, pdf, html, etc.) además de los campos típicos (from,
		to, subject, body, etc.)
	</p>

	<p style="font-size: 14px;;">
		La interfaz de consulta de todas las aplicaciones utiliza tecnología AJAX (combinación de DHTML, CSS, Javascript, XML y lenguajes de servidor). Además la interfaz se presenta inicialmente con 3 idiomas (Català, Castellano y English) siendo fácil la
		traducción a otros idiomas. El servidor está desarrollado íntegramente en Java.
	</p>
	<p style="font-size: 14px;;">
		Para más información puede consultar la documentación técnica <a href="http://www.duroty.com" target="_blank">AQUÍ</a> o escribir a <a href="mailto:duroty@gmail.com">duroty@gmail.com</a>.
	</p>
</div>
