<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>


</head>

<body>
	<%
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + "StudentDueReport.xls");
	%>

	<s:if
		test='%{totalDuesOfStudent.isEmpty()|| totalDuesOfStudent.size()<1}'>
		<div
			style="color: red; text-align: center; font-weight: bold; font-size: medium;">
			No Due Available For This Institute</div>
	</s:if>
	<s:else>
		<table
			class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
			<thead>
				<tr>
					<th>Sr. No.</th>
					<th>UIN</th>
					<th>GR Number</th>
					<th>Original Dues</th>
					<th></th>
					<th>Payment To Date</th>
					<th></th>
					<th colspan="2">Net Dues</th>


				</tr>
			</thead>
			<tbody>

				<%
					int i = 1;
				%>
				<s:iterator value="totalDuesOfStudent" var="duesArray">
					<tr>
						<td><%=i%></td>

						<td class="center"><s:property value="#duesArray[0]" /> <input
							type="hidden" id="feeName"
							value='<s:property value="#duesArray[0]" />'></td>

						<td class="center"><s:property value="#duesArray[4]" /></td>
						<td class="center">Rs. <s:property value="#duesArray[2]"
								default="0" /></td>
						<td></td>
						<td class="center">Rs. <s:property value="#duesArray[3]"
								default="0" /></td>
						<td></td>
						<td colspan="2" class="center">Rs. <s:property
								value="#duesArray[1]" default="0" /></td>

					</tr>

					<%
						i++;
					%>
				</s:iterator>
				<tr>
					<td></td>
					<td></td>
					<td><span style="font-weight: bold;">Total Original Due</span></td>
					<td>Rs. <s:property value="totalOriginalDues" /></td>
					<td><span style="font-weight: bold;">PTD</span></td>
					<td>Rs. <s:property value="totalPaymentToDate" /></td>
					<td><span style="font-weight: bold;">TND</span></td>
					<td colspan="2">Rs. <s:property
							value="totalNetDuesOFCollegeStudent" /></td>


				</tr>
			</tbody>
		</table>

	</s:else>


</body>
</html>
