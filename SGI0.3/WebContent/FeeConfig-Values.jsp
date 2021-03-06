<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
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
	int i = 1;
%>
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
	<div class="navbar navbar-default" role="navigation">

		<div class="navbar-inner">
			<button type="button" class="navbar-toggle pull-left animated flip">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>

			<a href="http://www.feedesk.in/" target="blank"> <img
				alt="FeeDesk Logo" src="img/feeDesk_logo.png"
				style="width: 150px; height: 53px; margin-left: 20px;" />
			</a>
			<!-- user dropdown starts -->
			<div class="btn-group pull-right">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class="glyphicon glyphicon-user"></i><span
						class="hidden-sm hidden-xs"> <%=loginUser.getUserName()%></span> <span
						class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a id="saveProfileTagId" onclick="" href="EditUserDetail.jsp">Settings</a></li>
					<li class="divider"></li>
					<li><a href="logOutUser">Logout</a></li>
				</ul>
			</div>
			<!-- user dropdown ends -->

			<!-- theme selector starts -->
			<div class="btn-group pull-right theme-container">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class="glyphicon glyphicon-tint"></i><span
						class="hidden-sm hidden-xs"> </span> <span class="caret"></span>
				</button>
				<ul class="dropdown-menu" id="themes">
					<li><a data-value="classic" href="#"><i class="whitespace"></i>
							Classic</a></li>
					<li><a data-value="cerulean" href="#"><i
							class="whitespace"></i> Cerulean</a></li>
					<li><a data-value="cyborg" href="#"><i class="whitespace"></i>
							Cyborg</a></li>
					<li><a data-value="simplex" href="#"><i class="whitespace"></i>
							Simplex</a></li>
					<li><a data-value="darkly" href="#"><i class="whitespace"></i>
							Darkly</a></li>
					<li><a data-value="lumen" href="#"><i class="whitespace"></i>
							Lumen</a></li>
					<li><a data-value="slate" href="#"><i class="whitespace"></i>
							Slate</a></li>
					<li><a data-value="spacelab" href="#"><i
							class="whitespace"></i> Spacelab</a></li>
					<li><a data-value="united" href="#"><i class="whitespace"></i>
							United</a></li>
				</ul>
			</div>
			<!-- theme selector ends -->

		</div>
	</div>
	<!-- topbar ends -->
	<div class="ch-container">
		<div class="row">

			<!-- left menu starts -->
			<div class="col-sm-2 col-lg-2">
				<div class="sidebar-nav">
					<div class="nav-canvas">
				
						<%
							if (profile.contentEquals("SU")) {
						%>
						<jsp:include page="menu_SuperAdmin.jsp"></jsp:include>
						<%
							}
						%>
						<%
							if (profile.contentEquals("Parent")) {
						%>

						<jsp:include page="menu_Parent.jsp"></jsp:include>
						<%
							}
						%>
						<%
							if (profile.contentEquals("Affiliated")) {
						%>
						<jsp:include page="menu_Institute.jsp"></jsp:include>
						<%
							}
						%>		
					</div>
				</div>
			</div>
			<!--/span-->
			<!-- left menu ends -->

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

				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-list-alt"></i> Links
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">

									<!---Content-->

									<p class="btn-group">
										<button class="btn btn-default"
											onclick='window.open("LockFeature.jsp", "Feature Lock", "height=500,width=500")'>Fee
											Templates</button>
										<button class="btn btn-default"
											onclick='window.location="GetFeesAll"'>Fee Values</button>
										<button class="btn btn-default"
											onclick='window.location="GetAllParameters"'>Fee
											Parameters</button>
									</p>


								</div>


							</div>
						</div>
					</div>
				</div>
				
				<!-- Fee Values Row -->
				<div class="row" id="FeeValuesBox">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-list-alt"></i> Fee Values
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">

									<!---Content-->

									<p class="btn-group">
										<button class="btn btn-default "
											onclick='window.open("GetParamValues", "Fee Form", "height=1080,width=1000")'>Add
											New Fee</button>


									</p>
									
									<table
										class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
										<thead>
											<tr>
												<th>Sr. No.</th>
												<th>Fee ID</th>
												<th>Fee Name</th>
												<th>Paid By</th>
												<th>Actions</th>
											</tr>
										</thead>
										<tbody>
										<s:iterator var="insIt" value="instituteList">
											<s:iterator value="feeSet">
												<tr>
													<td><%=i%></td>
													<td><s:property value="feeId" /></td>
													<td><s:property value="feeName" /> for <s:property value="instName" /></td>
													<td><s:set var="app">
															<s:property value="forApplicant" />
														</s:set> <s:set var="ins">
															<s:property value="forInstitute" />
														</s:set> <s:if test="%{#app==1}">Student</s:if> <s:if
															test="%{#ins==1}">College</s:if></td>
												 <td class="center"><a class="btn btn-success btn-sm"
														href="#" onclick='window.open("getFeeStructure?reqFeeId=<s:property value='feeId' />&reqInsId=<s:property value='instId' />")' 
														> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i> View Structure
													</a> <a class="btn btn-info btn-sm" href="#"
														data-toggle="popover" data-content=""
														title="Feature Locked"
														<%-- onclick='window.open("getFeeStructureEdit?reqFeeId=<s:property value='feeId' />&reqInsId=<s:property value='instId' />")' --%>> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
													</a> <a class="btn btn-danger btn-sm" href="#"
														data-toggle="popover" data-content=""
														title="Feature Locked"> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i>
															Delete
													</a></td>
												</tr>
												<%
													i++;
												%>
												</s:iterator>
											</s:iterator>
										
										
										
										
										
										
										
										
										
										
											<%-- <s:iterator value="fDfeeList2">

												<tr>
													<td><%=i%></td>
													<td><s:property value="feeId" /></td>
													<td><s:property value="feeName" /></td>
													<td><s:set var="app">
															<s:property value="forApplicant" />
														</s:set> <s:set var="ins">
															<s:property value="forInstitute" />
														</s:set> <s:if test="%{#app==1}">Student</s:if> <s:if
															test="%{#ins==1}">College</s:if></td>
												 <td class="center"><a class="btn btn-success btn-sm"
														href="#" onclick='window.open("getFeeStructure?reqFeeId=<s:property value='feeId' />")' data-toggle="popover" data-content=""
														title="Feature Locked"> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i> View Structure
													</a> <a class="btn btn-info btn-sm" href="#"
														
														 onclick='window.open("getFeeStructureEdit?reqFeeId=<s:property value='feeId' />")'> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
													</a> <a class="btn btn-danger btn-sm" href="#"
														data-toggle="popover" data-content=""
														title="Feature Locked"> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i>
															Delete
													</a></td>
												</tr>
												<%
													i++;
												%>
											</s:iterator> --%>


										</tbody>
									</table>
								</div>


							</div>
						</div>
					</div>
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
	<!-- TypeAhead Script -->
	<script src="js/typeahead.bundle.js"></script>

	<script>
		window.onfocus = function() {
			window.location.reload();
		}
	</script>
</body>
</html>
