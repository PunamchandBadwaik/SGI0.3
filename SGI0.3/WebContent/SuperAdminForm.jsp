<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
<%-- <%
	//checking session
	LoginBean loginUser = new LoginBean();
	loginUser = (LoginBean) session.getAttribute("loginUserBean"); String profile=(String)session.getAttribute("sesProfile");

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

			if (cookie.getName().equals("loginUser"))
				usercookie = cookie.getValue();
			if (cookie.getName().equals("JSESSIONID"))
				sessionID = cookie.getValue();
		}
	} else {
		sessionID = session.getId();
	}
%> --%>
<meta charset="utf-8">
<title>FeeDesk</title>
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


				<div class="row">
					<form action="registerSuperAdmin" method="post">
						<div class="box col-md-12">
							<div class="box-inner">
								<div class="box-header well">
									<h2>
										<i class="glyphicon glyphicon-info-sign"></i> Super Admin Form
									</h2>

									<div class="box-icon">

										<a href="#" class="btn btn-minimize btn-round btn-default"><i
											class="glyphicon glyphicon-chevron-down"></i></a>

									</div>
								</div>
								<div class="box-content row">
									<div class="col-lg-12 col-md-12 animated fadeIn">


										<table class="table table-condensed">
											<thead>

												<tr>
													<th><input type="hidden"
														name="superAdmin.loginBean.profile" value="Super Admin">
													</th>
													<th></th>
													<th></th>

												</tr>
											</thead>
											<tbody>
												<tr>

													<td><strong>Name</strong></td>
													<td><div id="the-basics" class="has-success">
															<input required="required" id="firstName"
																name="superAdmin.firstName" placeholder="First Name"
																type="text" class="form-control">
														</div></td>

												</tr>
												<tr>

													<td><strong>Middle Name</strong></td>
													<td><div id="the-basics" class="has-success">
															<input  id="MiddleName"
																name="superAdmin.midName" placeholder="Middle Name"
																type="text" class="form-control">

														</div></td>

												</tr>
												<tr>

													<td><strong>Last Name</strong></td>
													<td><div id="the-basics" class="has-success">
															<input required="required" id="LastName"
																name="superAdmin.lstName" placeholder="Last Name"
																type="text" class="form-control">

														</div></td>

												</tr>

												<tr>

													<td><strong>Gender</strong></td>
													<td><div id="the-basics" class="has-success">



															<input type="radio" required="required"
																name="superAdmin.gender" id="userPrefixMr" value="Male">Male
															&nbsp;&nbsp;&nbsp;<input type="radio" required="required"
																name="superAdmin.gender" id="userPrefixMrs"
																value="Female">Female









														</div></td>

												</tr>
												<tr>

													<td><strong>Age</strong></td>
													<td><div id="the-basics" class="has-success">
															<input id="PrinciName" name="superAdmin.age"
																pattern="[0-9]*" required="required" placeholder="Age"
																type="text" class="form-control">

														</div></td>

												</tr>
												<tr>

													<td><strong>Mobile Number</strong></td>
													<td><div id="the-basics" class="has-success">
															<input required="required" id="Contact"
																name="superAdmin.mobileNum" maxlength="10"
																placeholder="Mobile Number" pattern="[789][0-9]{9}"
																type="tel" class="form-control">

														</div></td>

												</tr>
												<tr>

													<td><strong>Email Id</strong></td>
													<td><div id="the-basics" class="has-success">
															<input required="required" id="emailId"
																name="superAdmin.emailId" placeholder="Email ID"
																type="email" class="form-control">

														</div></td>

												</tr>
												<tr>

													<td><strong>Address</strong></td>
													<td><div id="the-basics" class="has-success">
															<textarea required="required" id="address"
																name="superAdmin.address" placeholder="Address"
																class="form-control"></textarea>

														</div></td>

												</tr>






											</tbody>
										</table>


									</div>


								</div>
							</div>
						</div>
						<div class="col-md-12">
							<button type="submit" class="btn btn-success">Save Super
								Admin</button>

							<button onclick="window.close()" class="btn btn-info">Close
							</button>

						</div>
					</form>
				</div>

				<!--/row-->

				<!--/row-->
				<!-- content ends -->
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

		<!-- <footer class="row">
			<p class="col-md-9 col-sm-9 col-xs-12 copyright">
				&copy; <a href="http://dexpertsystems.com" target="_blank">Dexpert
					Systems Pvt. Ltd</a>
			</p>

			<p class="col-md-3 col-sm-3 col-xs-12 powered-by">
				Powered by: <a href="http://dexpertsystems.com">Dexpert</a>
			</p>
		</footer> -->

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

	<script>
		function OpenSummaryInParent() {
			window.opener.location = "College-Payment-Summary.html";
			window.close();

		}
	</script>
</body>
</html>
