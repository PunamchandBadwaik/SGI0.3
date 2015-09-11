<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FeeDesk</title>
</head>
<body onload="window.print()">

	<div>
		<table border="1" style="border-collapse: collapse;" cellpadding="3.5px">
			<tr style="font-size: 13px;">
				<th>Sr.No.</th>
				<th>Inst. Name</th>
				<th>Cnt.No.</th>
				<th>Clg. Email</th>
				<th>Cnt. Person</th>
				<th>Mob.No.</th>
				<th>Inst. Add.</th>
				<th>Dist.</th>

			</tr>
			<%
				int i = 1;
			%>
			<s:iterator value="affInstList">
				<tr style="c">
					<td><span style="margin-left: 2px;font-size:10px;"><%=i%></span></td>
					<td><span style="margin-left: 2px;font-size:10px;"><s:property
								value="instName" /></span></td>

					<td><span style="margin-left:2px;font-size:10px;"><s:property
								value="contactNumber" /></span></td>

					<td><span style="margin-left: 2px;font-size:10px;"><s:property
								value="email" /></span></td>
					<td><span style="margin-left: 2px;font-size:10px;"><s:property
								value="contactPerson" /></span></td>
					<td><span style="margin-left: 2px;font-size:10px;"><s:property
								value="mobileNum" /></span></td>
					<td><span style="margin-left: 2px;font-size:10px;"><s:property
								value="instAddress" /></span></td>
					<td><span style="margin-left: 2px;font-size:10px;"><s:property
								value="place" /></span></td>

				</tr>
				<%
					i++;
				%>

			</s:iterator>

		</table>
	</div>
</body>
</html>