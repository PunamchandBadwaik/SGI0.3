<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FeeDesk</title>
</head>
<body onload="CallFun()">


Please wait while redirecting to SabPaisa...

	<%
		String url = (String) request.getAttribute("sabPaisaURL");
	%>
	<script type="text/javascript">

function CallFun()
{
	
	setTimeout(function()
			{
			 
			
 	window.location ="<%=url%>";
			}, 50);

		}
	</script>


</body>
</html>