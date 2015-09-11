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
					<td>Select Fee Name</td>
					<td><select name="feeName" id="feeId"
						onchange="" data-rel="chosen" style="width: 240px;">
							<option value="">---Select Fees---</option>
                            <s:iterator value="feeNameList" status="var">
                            <option value="<s:property/>"><s:property/></option>
                            </s:iterator>

					</select></td>

				</tr>
			</table>

		</div>




	</div>


</body>
</html>