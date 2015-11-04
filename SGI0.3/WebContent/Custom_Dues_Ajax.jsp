<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="org.apache.struts2.views.jsp.TagUtils"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
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
	<%
		String errMessage = (String) request.getAttribute("msg");
		ActionContext.getContext().put("errMessage", errMessage);
		ValueStack vs = TagUtils.getStack(pageContext);
		List<Object[]> studentDues = (List<Object[]>) vs.findValue("totalDuesOFCollege");
	%>
	<div>



		<s:if
			test='%{totalDuesOFCollege.isEmpty()|| totalDuesOFCollege.size()<1}'>


			<s:if test="#request.errMessage!=null">
				<div
					style="color: red; text-align: center; font-weight: bold; font-size: medium;">Not
					Available For Super Admin Please Check Other Reports</div>
			</s:if>

			<s:else>
				<div
					style="color: red; text-align: center; font-weight: bold; font-size: medium;">
					No Due Available</div>
			</s:else>
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
					<%-- <s:iterator value="totalDuesOFCollege" var="duesArray">
						<tr>
							<td><%=i%></td>

							<td class="center"><s:property value="#duesArray[0]" /> <input
								type="hidden" id="feeName"
								value='<s:property value="#duesArray[0]" />'></td>

							<td></td>
							<td class="center">Rs.<s:property value="#duesArray[1]" />	</td>
							<td></td>
							<td class="center">Rs.<s:property value="#duesArray[2]" />
							</td>
							<td></td>
							<td colspan="2" class="center">Rs.<s:property
									value="#duesArray[3]" />
							</td>

						</tr>

						<%
							i++;
						%>
					</s:iterator>
 --%>
					<%
						Iterator<Object[]> iterator = studentDues.iterator();

							while (iterator.hasNext()) {
								Object[] studentDue = iterator.next();
					%>
					<tr>
						<td><%=i%></td>

						<td class="center"><%=studentDue[0]%><input
							type="hidden" id="feeName"
							value='<s:property value="#duesArray[0]" />'></td>

						<td></td>
						<%
							Object originalDue = studentDue[1] == null ? 0.0 : studentDue[1];
						%>
						<td class="center">Rs.<%=BigDecimal.valueOf(Double.valueOf(originalDue.toString())).toPlainString()%>
						</td>
						<td></td>
						<%
							Object paymentToDate = studentDue[2] == null ? 0.0 : studentDue[2];
						%>
						<td class="center">Rs.<%=BigDecimal.valueOf(Double.valueOf(paymentToDate.toString())).toPlainString()%>
						</td>
						<td></td>
						<%
							Object netDue = studentDue[3] == null ? 0.0 : studentDue[3];
						%>
						<td colspan="2" class="center">Rs.<%=BigDecimal.valueOf(Double.valueOf(netDue.toString())).toPlainString()%>
						</td>

					</tr>


					<%
						}
					%>

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