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
		String msg = request.getParameter("RPS");
		String transId = request.getParameter("txnId");
		String payMode = request.getParameter("PayMode");
	%>
	<%
		if (msg.equals("0")) {
	%>

	<div align="center">Congratulations, your payment has been
		successful, will automatically redirected your home page...</div>

	




	<%
		}

		else {

			if (msg.equals("-99")) {
	%>
	<div align="center">You have cancelled the transaction, will
		automatically redirected your home page...</div>

	<%
		} else {
	%>
	<div align="center">Sorry, your transaction has been declined due
		to some reason, will automatically redirected your home page...</div>
	
	<%
		}
		}
	%>

	

		<br><br>
	<div style="text-align: center;">
		<img src="img/loading.gif" title="img/loading.gif" height="300px" width="400px" >
	</div>

		<script type="text/javascript">
			function waitSubmit() {
				
				
				 window.setTimeout(function(){
						window.location = "RetrieveUserSession?RPS=<%=msg%>&txnID=<%=transId%>&payMode=<%=payMode%>";
								}, 5000);
			}
		</script>
</body>
</html>