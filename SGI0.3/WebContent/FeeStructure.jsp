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
	<table
		class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
		<thead>
			<tr>
				<th colspan="15">Admissions Fee Structure- UnderGraduate
					Department</th>
			</tr>
			<tr>
				<th>Sr. No.</th>
				<th>Department</th>
				<th colspan="3">Admission Fee</th>
				<th colspan="3">Registration Fee</th>
				<th colspan="2">Late Adm. Fee</th>
				<th>Late Submission Fee</th>
				<th>Sports Fee</th>
				<th>Student Welfare Fund Fee</th>
				<th>NSS Fee</th>
				<th>Helinet Fee</th>

			</tr>
			<tr>
				<th></th>
				<th></th>
				<th>India</th>
				<th>NRI & SAARC</th>
				<th>Foreign Candidate</th>
				<th>India</th>
				<th>NRI & SAARC</th>
				<th>Foreign Candidate</th>
				<th>India, NRI & SAARC</th>
				<th>Foreign Candidate</th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td></td>
				<td>Medical</td>
				<td>500</td>
				<td>3000</td>
				<td>US$150</td>
				<td>3000</td>
				<td>5000</td>
				<td>US$150</td>
				<td>200</td>
				<td>500</td>
				<td></td>
				<td>200</td>
				<td>100</td>
				<td>10</td>
				<td>1000</td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td>Dental</td>
				<td>500</td>
				<td>3000</td>
				<td>US$150</td>
				<td>3000</td>
				<td>5000</td>
				<td>US$150</td>
				<td>200</td>
				<td>500</td>
				<td></td>
				<td>200</td>
				<td>100</td>
				<td>10</td>
				<td>1000</td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td>Nursing, Physiotheraphy, Pharmacy, Ayurveda, Homeopathy,
					Unani & Yoga</td>
				<td>300</td>
				<td>3000</td>
				<td>US$150</td>
				<td>2000</td>
				<td>2000</td>
				<td>US$150</td>
				<td>200</td>
				<td>500</td>
				<td></td>
				<td>200</td>
				<td>100</td>
				<td>10</td>
				<td>500</td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td>All Other Paramedical Courses</td>
				<td>300</td>
				<td>3000</td>
				<td>US$150</td>
				<td>2000</td>
				<td>2000</td>
				<td>US$150</td>
				<td>200</td>
				<td>500</td>
				<td></td>
				<td>200</td>
				<td>100</td>
				<td>10</td>
				<td>500</td>
				<td></td>
			</tr>
		</tbody>
	</table>
</body>
</html>