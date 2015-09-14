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

		<div class="controls">
			<table>
				<tr>
					<td>Select College Name</td>
					<td><select name="college" id="collegeName"
						onchange="collegeSelected(this.value)" data-rel="chosen" style="width: 240px;">
							<option value="">---Select College---</option>
                            <s:if test="%{!affBeansList.isEmpty()||affBeansList.size()>0}">
                            <option value="All">All College</option>
                            </s:if>
                            <s:iterator value="affBeansList">
                            <option value="<s:property value="instName"/>"><s:property value="instName"/></option>
                            </s:iterator>

					</select></td>

				</tr>
			</table>

		</div>




	</div>


</body>
</html>