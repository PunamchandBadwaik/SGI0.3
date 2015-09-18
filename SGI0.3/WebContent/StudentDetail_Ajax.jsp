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
			test='%{affBean.aplBeanSet.isEmpty()||affBean.aplBeanSet.size()<1}'>

			<div
				style="color: red; text-align: center; font-weight: bold; font-size: medium;">
				No Student Available</div>

		</s:if>
		<s:else>
			<table
				class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
				<thead>
					<tr>
						<th width="7%">Sr. No.</th>
						<th>Enrollment Number</th>

						<th>Student Name</th>
						<th>Mobile Number</th>

						<th>Place</th>
						<th>Institute Name</th>

						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<%
						int i = 1;
					%>
					<s:iterator value="affBean.aplBeanSet">
						<%-- <c:forEach items="${affBean.aplBeanSet}" var="optr"> --%>
						<tr>
							<td><%=i%></td>
							<td class="center"><s:property value="enrollmentNumber" /></td>
							<td class="center"><s:property value="aplFirstName" />&nbsp;<s:property
									value="aplLstName" /></td>
							<td class="center"><s:property value="aplMobilePri" /></td>
							<td class="center"><s:property value="aplAddress" /></td>
							<td class="center"><s:property value="affBean.instName" /></td>

							<td class="center"><a class="btn btn-success btn-sm"
								onclick="window.open('ViewApplicantDetail?applicantId=<s:property value="enrollmentNumber"  />','Applicant Detail','width=500 height=700')">
									<i class="glyphicon glyphicon-zoom-in icon-white"></i> View
							</a></td>
						</tr>

						<%
							i++;
						%>
						<%-- </c:forEach> --%>
					</s:iterator>


				</tbody>
			</table>
		</s:else>

	</div>
</body>
</html>