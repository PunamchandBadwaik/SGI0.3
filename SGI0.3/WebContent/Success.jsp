<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	//checking session
	LoginBean loginUser = new LoginBean();
	loginUser = (LoginBean) session.getAttribute("loginUserBean"); String profile=(String)session.getAttribute("sesProfile");

	if (loginUser == null) {
		response.sendRedirect("Login.jsp");

		return;

	}
	String usercookie = null;
	String sessionID = null;
	String dispchar = "display:none";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("loginUser"))
				usercookie = cookie.getValue();
			if (cookie.getName().equals("JSESSIONID"))
				sessionID = cookie.getValue();
		}
	} else {
		sessionID = session.getId();
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FeeDesk</title>
</head>
<body>

	<%
		String msg = (String) request.getAttribute("msg");
		String link=(String)request.getAttribute("redirectlink");
	%>


	<%
		if (msg != null) {
	%>


	<div
		style="color: green; text-align: center; font-weight: bold; font-size: x-large;">
		<%=msg%>
	</div>

	<%
		}
	%>
	<%
		if (link != null) {
	%>
<a href=<%=link%>>Okay!</a>
<%
		}
	%>
</body>
</html>