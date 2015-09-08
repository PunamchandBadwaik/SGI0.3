<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<html lang="en">
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>

<meta charset="utf-8">
<title>FeeDesk</title>
<style type="text/css">
#errors {
	color: red;
	font-size: medium;
	font-weight: bold;
	text-align: center;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description"
	content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
<meta name="author" content="Muhammad Usman">

<!-- The styles -->
<link id="bs-css" href="css/bootstrap-cerulean.min.css" rel="stylesheet">

<link href="css/charisma-app.css" rel="stylesheet">
<link href='bower_components/fullcalendar/dist/fullcalendar.css'
	rel='stylesheet'>
<link href='bower_components/fullcalendar/dist/fullcalendar.print.css'
	rel='stylesheet' media='print'>
<link href='bower_components/chosen/chosen.min.css' rel='stylesheet'>
<link href='bower_components/colorbox/example3/colorbox.css'
	rel='stylesheet'>
<link href='bower_components/responsive-tables/responsive-tables.css'
	rel='stylesheet'>
<link
	href='bower_components/bootstrap-tour/build/css/bootstrap-tour.min.css'
	rel='stylesheet'>
<link href='css/jquery.noty.css' rel='stylesheet'>
<link href='css/noty_theme_default.css' rel='stylesheet'>
<link href='css/elfinder.min.css' rel='stylesheet'>
<link href='css/elfinder.theme.css' rel='stylesheet'>
<link href='css/jquery.iphone.toggle.css' rel='stylesheet'>
<link href='css/uploadify.css' rel='stylesheet'>
<link href='css/animate.min.css' rel='stylesheet'>
<link href="bower_components/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<!-- jQuery -->
<script src="bower_components/jquery/jquery.min.js"></script>

<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<!-- The fav icon -->
<link rel="shortcut icon" href="img/favicon.ico">

</head>
<%
	//checking session
	LoginBean loginUser = new LoginBean();
	loginUser = (LoginBean) session.getAttribute("loginUserBean");
	String profile = (String) session.getAttribute("sesProfile");

	if (loginUser == null) {
		response.sendRedirect("Login.jsp");

		return;

	}
	String usercookie = null;
	String sessionID = null;
	String dispchar = "display:none";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("user"))
				usercookie = cookie.getValue();
			if (cookie.getName().equals("JSESSIONID"))
				sessionID = cookie.getValue();
		}
	} else {
		sessionID = session.getId();
	}
%>

<body>
	<!-- topbar starts -->

	<!-- topbar ends -->
	<div class="ch-container">
		<div class="row">


			<noscript>
				<div class="alert alert-block col-md-12">
					<h4 class="alert-heading">Warning!</h4>

					<p>
						You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
							target="_blank">JavaScript</a> enabled to use this site.
					</p>
				</div>
			</noscript>

			<div id="content" class="col-lg-10 col-sm-10">
				<!-- content starts -->
				<div></div>

				<%
					String msg = (String) request.getAttribute("msg");
				%>


				<%
					if (msg != null) {
				%>


				<div
					style="color: red; text-align: center; font-weight: bold; font-size: large;">
					<%=msg%>
				</div>

				<div></div>
				<%
					}
				%>

				<s:if test="hasActionErrors()">
					<div id="errors">
						<s:actionerror />
					</div>
				</s:if>







				<form onsubmit="showProgress()" id="bulkForm"
					action="AddBulkColleges" enctype="multipart/form-data"
					method="post">

					<div class="row">
						<div class="box col-md-12">
							<div class="box-inner">
								<div class="box-header well">
									<h2>
										<i class="glyphicon glyphicon-info-sign"></i> College Total
										Dues

									</h2>

									<div class="box-icon">

										<a href="#" class="btn btn-minimize btn-round btn-default"><i
											class="glyphicon glyphicon-chevron-down"></i></a>

									</div>
								</div>

								<div class="box-content row">
									<div class="col-lg-12 col-md-12 animated fadeIn">

										<s:if test='%{totalDuesOfStudent.isEmpty()|| totalDuesOfStudent.size()<1}'>
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
														<th>Enrollment Number</th>
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
													<s:iterator value="totalDuesOfStudent" var="duesArray">
														<tr>
															<td><%=i%></td>

															<td class="center"> <s:property value="#duesArray[0]" /> <input
																type="hidden" id="feeName"
																value='<s:property value="#duesArray[0]" />'></td>

															<td></td>
															<td class="center">Rs. <s:property
																	value="#duesArray[2]" default="0" /></td>
															<td></td>
															<td class="center">Rs. <s:property
																	value="#duesArray[3]" default="0" /></td>
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
														<td colspan="2">Rs. <s:property value="totalNetDuesOFCollegeStudent" /></td>


													</tr>
												</tbody>
											</table>
											<table
												class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">



											</table>
										</s:else>





									</div>


								</div>

							</div>
						</div>



						<button onclick="window.close()" class="btn btn-info">Close</button>





					</div>
				</form>



			</div>
			<!--/#content.col-md-0-->
		</div>
		<!--/fluid-row-->



		<hr>

		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">

			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h3>Settings</h3>
					</div>
					<div class="modal-body">
						<p>Here settings can be configured...</p>
					</div>
					<div class="modal-footer">
						<a href="#" class="btn btn-default" data-dismiss="modal">Close</a>
						<a href="#" class="btn btn-primary" data-dismiss="modal">Save
							changes</a>
					</div>
				</div>
			</div>
		</div>



	</div>
	<!--/.fluid-container-->

	<!-- external javascript -->

	<script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

	<!-- library for cookie management -->
	<script src="js/jquery.cookie.js"></script>
	<!-- calender plugin -->
	<script src='bower_components/moment/min/moment.min.js'></script>
	<script src='bower_components/fullcalendar/dist/fullcalendar.min.js'></script>

	<!-- data table plugin -->
	<script src='js/jquery.dataTables.min.js'></script>

	<!-- select or dropdown enhancer -->
	<script src="bower_components/chosen/chosen.jquery.min.js"></script>
	<!-- plugin for gallery image view -->
	<script src="bower_components/colorbox/jquery.colorbox-min.js"></script>
	<!-- notification plugin -->
	<script src="js/jquery.noty.js"></script>
	<!-- library for making tables responsive -->
	<script src="bower_components/responsive-tables/responsive-tables.js"></script>
	<!-- tour plugin -->
	<script
		src="bower_components/bootstrap-tour/build/js/bootstrap-tour.min.js"></script>
	<!-- star rating plugin -->
	<script src="js/jquery.raty.min.js"></script>
	<!-- for iOS style toggle switch -->
	<script src="js/jquery.iphone.toggle.js"></script>
	<!-- autogrowing textarea plugin -->
	<script src="js/jquery.autogrow-textarea.js"></script>
	<!-- multiple file upload plugin -->
	<script src="js/jquery.uploadify-3.1.min.js"></script>
	<!-- history.js for cross-browser state change on ajax -->
	<script src="js/jquery.history.js"></script>
	<!-- application script for Charisma demo -->
	<script src="js/charisma.js"></script>


</body>
</html>
