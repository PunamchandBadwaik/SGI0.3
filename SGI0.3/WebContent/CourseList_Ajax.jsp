<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
					<td>Select Course</td>
					<td><select name="appBean1.course" id="courseId"
						onchange="" data-rel="chosen" style="width: 240px;">
							<option value="">---Select Course---</option>
							<s:if test="%{!listOfCourse.isEmpty()||listOfCourse.size()>0}">
							<option value="All">All Course</option>
							</s:if>
							<s:iterator value="listOfCourse" status="var">
								<option value="<s:property/>"><s:property /></option>
							</s:iterator>
					</select></td>

				</tr>


				<tr>
					<td><strong>Select Fee Name</strong></td>
					<td><select name="feeName" id="feeId" onchange=""
						data-rel="chosen" style="width: 240px;">
							<option value="">---Select Fees---</option>
							<s:iterator value="feeNameList" status="var">
								<option value="<s:property/>"><s:property /></option>
							</s:iterator>

					</select></td>



				</tr>

			</table>

		</div>
    </div>


</body>
</html>