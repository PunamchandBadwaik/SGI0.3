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
		response.setHeader("Content-Disposition", "attachment; filename=" + "TransactionReport.xls");
	%>


	<table
		class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
		<thead>
			<tr>
				<th width="7%">Sr. No.</th>
				<th>Student UIN</th>
				<th>Transaction ID</th>
				<th>Mode Of Tranaction</th>
				<th>Payee Name</th>
				<!-- <th>Payee Mobile</th> -->
				<!-- <th>Payee Email</th> -->
				<!-- <th>Payee Address</th> -->
				<th>Amount</th>
				<th>Status</th>

				<th>Transaction Date</th>



			</tr>
		</thead>
		<tbody>
			<%
				int i = 1;
			%>
			<s:iterator value="transactionDetailsForReport">
				<tr>
					<td><%=i%></td>

					<td class="center"><s:set var="val">
							<s:property value="studentEnrollmentNumber" />
						</s:set> <s:if test='%{#val=="NA"}'>

							<a href="#"
								onclick="window.open('showBulkTrans?transId=<s:property value="txnId" />','Bulk Transaction','width=700,height=500')"
								title="View Detail"> <span style="margin-left: 10px;"><s:property
										value="studentEnrollmentNumber" /></span>

							</a>


						</s:if> <s:else>
							<span style="margin-left: 10px;"><s:property
									value="studentEnrollmentNumber" /></span>

						</s:else></td>
					<td class="center"><span style="margin-left: 10px;"><s:property
								value="txnId" /></span></td>
					<td class="center"><span style="margin-left: 10px;"><s:property
								value="paymentMode" /></span></td>

					<td><span style="margin-left: 10px;"><s:property
								value="payeeFirstName" />&nbsp;&nbsp;<s:property
								value="payeeLstName" /></span></td>
					<%-- <td><span style="margin-left: 10px;"><s:property
																value="payeeMob" /></span></td> --%>
					<%-- <td><span style="margin-left: 10px;"><s:property
																value="payeeEmail" /></span></td> --%>
					<%-- <td><span style="margin-left: 10px;"><s:property
																value="payeeAdd" /></span></td> --%>
					<td>Rs. <span style="margin-left: 10px;"><s:property
								value="payeeAmount" /></span></td>
					<td><span style="margin-left: 10px;"><s:property
								value="status" /></span></td>
					<td><span style="margin-left: 10px;"><s:property
								value="transDate" /></span></td>
				</tr>

				<%
					i++;
				%>
			</s:iterator>


		</tbody>
	</table>


</body>
</html>
