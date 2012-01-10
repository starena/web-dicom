<%@ page language="java" contentType="text/html; charset=Cp1251"
	pageEncoding="Cp1251" import="java.util.*"%>

<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.psystems.dicom.commons.*"%>

<jsp:useBean id="util" scope="page" class="org.psystems.dicom.pdf.Utils" />


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Cp1251">
<title>Список шаблонов</title>
</head>
<body>

	<h1>Шаблоны:</h1>

	<%
		for (ConfigTemplate template : util.getPDFTemplates()) {
			String url = URLEncoder.encode(template.getName(),"UTF-8");
			
			
	%>
	<a href="/pdf/<%= url%>" target="new">
		(<%=template.getModality()%>) <%=template.getDescription()%> </a>
	<br>
	<%
		}
	%>



</body>
</html>