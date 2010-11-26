<%@ page language="java" contentType="text/plain; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<jsp:useBean id="util" scope="page" class="org.psystems.dicom.browser.server.Util" />
<%
	
	String width = (String)request.getParameter("width");
	String height = (String)request.getParameter("height");
	String path = request.getPathInfo().replaceFirst("/", "");
	Connection connection = util.getConnection("main",getServletContext());
	PreparedStatement psSelect = null;
	psSelect = connection.prepareStatement("SELECT * FROM WEBDICOM.STUDY WHERE ID = ? ");
	psSelect.setLong(1, Long.valueOf(path).longValue());
	ResultSet rs = psSelect.executeQuery();
	while (rs.next()) {
		
%>
###ID###<%=rs.getLong("ID") %>
###STUDY_ID###<%=rs.getString("STUDY_ID") %>
###STUDY_UID###<%=rs.getString("STUDY_UID") %>
###STUDY_MODALITY###<%=rs.getString("STUDY_MODALITY") %>
###STUDY_TYPE###<%=rs.getString("STUDY_TYPE") %>
###STUDY_DESCRIPTION###<%=rs.getString("STUDY_DESCRIPTION") %>
###STUDY_DATE###<%=rs.getString("STUDY_DATE") %>
###STUDY_MANUFACTURER_UID###<%=rs.getString("STUDY_MANUFACTURER_UID") %>
###STUDY_MANUFACTURER_MODEL_NAME###<%=rs.getString("STUDY_MANUFACTURER_MODEL_NAME") %>
###STUDY_DOCTOR###<%=rs.getString("STUDY_DOCTOR") %>
###STUDY_OPERATOR###<%=rs.getString("STUDY_OPERATOR") %>
###STUDY_VIEW_PROTOCOL###<%=rs.getString("STUDY_VIEW_PROTOCOL") %>
###STUDY_VIEW_PROTOCOL_DATE###<%=rs.getString("STUDY_VIEW_PROTOCOL_DATE") %>
###STUDY_RESULT###<%=rs.getString("STUDY_RESULT") %>
###DATE_MODIFY###<%=rs.getString("DATE_MODIFY") %>
###PATIENT_ID###<%=rs.getString("PATIENT_ID") %>
###PATIENT_NAME###<%=rs.getString("PATIENT_NAME") %>
###PATIENT_SHORTNAME###<%=rs.getString("PATIENT_SHORTNAME") %>
###PATIENT_SEX###<%=rs.getString("PATIENT_SEX") %>
###PATIENT_BIRTH_DATE###<%=rs.getString("PATIENT_BIRTH_DATE") %>
	
<%
	}
%>
