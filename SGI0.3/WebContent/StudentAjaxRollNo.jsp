<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

	<table>

		<tr>

			<td ><input name="appBean1.enrollmentNumber"
				id="enrollTextId"
				value='<s:property value="appBean1.enrollmentNumber"/>'
				placeholder="Enrollment Number" type="text" class="form-control">
			</td>

		</tr>
	</table>




</body>


</html>