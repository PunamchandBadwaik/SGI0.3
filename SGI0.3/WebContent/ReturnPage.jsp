<%@page import="org.apache.struts2.components.Else"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SGI</title>
</head>
<body onload="waitSubmit()">
	<h2></h2>
	<%
		String paymentMode = request.getParameter("payMode");

		String pgRespCode = request.getParameter("pgRespCode") == null ? "-99" : request.getParameter("pgRespCode");
		String atomTxnId = request.getParameter("atomTxnNo");
		String sabPaisaTxnId = request.getParameter("SabPaisaTxId");

		String authCode = request.getParameter("authIdCode");
		String clientTxnId = request.getParameter("clientTxnId");
		String amount = request.getParameter("amount");
		String issureReffNo = request.getParameter("issuerRefNo");
		String firstName = request.getParameter("firstName");
		String lstName = request.getParameter("lastName");
		String mobile = request.getParameter("mobileNo");
		String email = request.getParameter("email");
		//String pgName = request.getParameter("pg") == null ? "" : request.getParameter("pg");
	%>

	<%
		if (pgRespCode.equals("Ok")) {
	%>


	<div align="center">Congratulations, your payment has been
		successful, will automatically redirected your home page...</div>


	<%
		} else {
	%>



	<%
		}
	%>




	<br>
	<br>
		<div style="text-align: center;">
<br>
		<span style="color: red; font-weight: bold;">Please, don't
			close or refresh page</span> <br> <br><img src="img/loader.gif"
			title="img/loader.gif" height="100px" width="150px">
	</div>

	<script type="text/javascript">
			function waitSubmit() {
				
				
				 window.setTimeout(function(){
						window.location = "RetrieveUserSession?RPS=<%=pgRespCode%>&payMode=<%=paymentMode%>&txnID=<%=clientTxnId%>";
							}, 01);
		}
	</script>
</body>
</html>