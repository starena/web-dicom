<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TEST</title>
</head>

<body>



<jsp:useBean id="util" scope="page"
	class="org.psystems.dicom.browser.server.Util" />




<%
	
	Connection connection = util.getConnection2(getServletContext());
	if(connection!=null) {
	PreparedStatement psSelect = null;
	psSelect = connection.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE");
	
	ResultSet rs = psSelect.executeQuery();
	while (rs.next()) {
		String file = rs.getString("DCM_FILE_NAME");
		long dcmId = rs.getLong("ID");
	%>
	
	<%=file%> <%=dcmId%> <br>
	
	<%	
	}
	
	}

%>



</body>
</html>




