<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div>
		<s:if
			test='%{totalDuesOfStudent.isEmpty()|| totalDuesOfStudent.size()<1}'>
			<div
				style="color: red; text-align: center; font-weight: bold; font-size: medium;">
				No Due Available For This College</div>
		</s:if>
		<s:else>
			<table
				class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
				<thead>
					<tr>
						<th>Sr. No.</th>
						<th>Fee Name</th>
						<th></th>
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
					<s:iterator value="totalDuesOFCollege" var="duesArray">
						<tr>
							<td><%=i%></td>

							<td class="center"><s:property value="#duesArray[0]" /> <input
								type="hidden" id="feeName"
								value='<s:property value="#duesArray[0]" />'></td>

							<td></td>
							<td class="center">Rs. <s:property value="#duesArray[1]"
									default="0" /></td>
							<td></td>
							<td class="center">Rs. <s:property value="#duesArray[2]"
									default="0" /></td>
							<td></td>
							<td colspan="2" class="center">Rs. <s:property
									value="#duesArray[3]" default="0" /></td>

						</tr>

						<%
							i++;
						%>
					</s:iterator>
					<%-- <tr>
						<td></td>
						<td></td>
						<td><span style="font-weight: bold;">Total Original
								Due</span></td>
						<td>Rs. <s:property value="totalOriginalDues" /></td>
						<td><span style="font-weight: bold;">PTD</span></td>
						<td>Rs. <s:property value="totalPaymentToDate" /></td>
						<td><span style="font-weight: bold;">TND</span></td>
						<td colspan="2">Rs. <s:property
								value="totalNetDuesOFCollegeStudent" /></td>


					</tr> --%>
				</tbody>
			</table>
			<table
				class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">



			</table>
		</s:else>




	</div>
</body>
</html>