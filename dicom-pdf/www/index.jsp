<%@ page language="java" contentType="text/html; charset=Cp1251"
	pageEncoding="Cp1251" import="java.util.*"%>

<%@ page import="java.util.*"%>

<jsp:useBean id="util" scope="page" class="org.psystems.dicom.pdf.Utils" />


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Cp1251">
<title>Список шаблонов</title>
</head>
<body>

<h1> Шаблоны:</h1>

	<%
		for (String file : util.getPDFTemplates()) {
	%>
		<a href="/pdf/<%= file %>"> <%=file %> </a> <br>
	<% } %>



</body>
</html>