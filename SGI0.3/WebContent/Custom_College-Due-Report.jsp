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
					<li><a id="saveProfileTagId" onclick=""
						href="EditUserDetail.jsp">Settings</a></li>
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
			<!-- cart button starts -->
			<%-- <div class="btn-group pull-right">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class=" glyphicon glyphicon-shopping-cart"></i><span
						class="hidden-sm hidden-xs"> Cart</span> <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a href="College-Payment-Summary.html">Proceed To
							Checkout</a></li>
					<li class="divider"></li>
					<li><a href="#"
						onclick='window.open("Cart.html", "MyCart", "width=500,height=900")'>View
							Cart</a></li>
				</ul>
			</div> --%>
			<!-- cart button ends -->
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
				<!-- <div>
					<ul class="breadcrumb">
						<li><a href="#">Home</a></li>
						<li><a href="#">Dashboard</a></li>
					</ul>
				</div> -->




				<!--/row-->
				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-list-alt"></i> College Due Report
									Detail
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">
									<div class="row">
										<div class="col-md-12">
											<button class="btn btn-sm btn-info pull-right"
												onclick='window.open("LockFeature.jsp", "CollegeForm", "width=500,height=700")'>
												<i class="fa fa-plus"></i> Print Report
											</button>
										</div>
									</div>
									<!---Content-->


									<%
										if (profile.contentEquals("SU")) {
									%>
									<div>

										<div class="controls">
											<table>
												<tr>
													<td>Select UniverSity</td>
													<td><select name="university" id="universityName"
														onchange="universitySelected(this.value)"
														data-rel="chosen" style="width: 240px;">
															<option value="">---Select University---</option>
															<s:iterator value="parBeans">
																<option value="<s:property value="parInstName"/>"><s:property
																		value="parInstName" /></option>
															</s:iterator>




													</select></td>


												</tr>
												
												
												
												
												
												

											</table>

										</div>




									</div>


									<p class="btn-group">
									<div id="collegeList">

										<div class="controls">
											<table>
												<tr>
													<td>Select College Name</td>
													<td><select name="appBean1.course" id="courseId"
														onchange="collegeSelected(this.value)" data-rel="chosen"
														style="width: 240px;">
															<option value="">---Select College---</option>

													</select></td>

												</tr>

											</table>


										</div>




									</div>
									</p>
									<p class="btn-group">
									<div id="courseList">

										<div class="controls">
											<table>
												<tr>
													<td>Select Course</td>
													<td><select name="appBean1.course" id="courseId"
														onchange="" data-rel="chosen" style="width: 240px;">
															<option value="">---Select Course---</option>
													</select></td>


												</tr>

											</table>

										</div>




									</div>
									</p>
									<p class="btn-group">
									<div id="feeName">

										<div class="controls">
											<table>
												<tr>
													<td>Select Fee Name</td>
													<td><select name="fee" id="feeId"
														onchange="" data-rel="chosen" style="width: 240px;">
															<option value="">---Select Fees---</option>


													</select></td>

												</tr>
											</table>

										</div>




									</div>
									</P>


									<%
										}
									%>

									<%
										if (profile.contentEquals("Parent")) {
									%>
									<p class="btn-group">
									<div>

										<div class="controls">
											<table>
												<tr>
													<td>Select College Name</td>
													<td><select name="college" id="collegeName"
														onchange="collegeSelected(this.value)" data-rel="chosen"
														style="width: 240px;">
															<option value="">---Select College---</option>
															<option value="All">All College</option>
															<s:iterator value="affBeansList">
																<option value="<s:property value="instName"/>"><s:property
																		value="instName" /></option>
															</s:iterator>
													</select></td>

												</tr>

											</table>


										</div>




									</div>
									</p>
									<p class="btn-group">
									<div id="courseList">

										<div class="controls">
											<table>
												<p class="btn-group">
												<tr>
													<td>Select Course</td>
													<td><select name="appBean1.course" id="courseId"
														onchange="" data-rel="chosen" style="width: 240px;">
															<option value="">---Select Course---</option>
													</select></td>


												</tr>
												</p>
												<tr>
													<td>Select Fee Name</td>
													<td><select name="appBean1.course" id="feeId"
														onchange="" data-rel="chosen" style="width: 240px;">
															<option value="">---Select Fees---</option>


													</select></td>

												</tr>
								

											</table>

										</div>




									</div>
									</p>
									



									<%
										}
									%>
									<%
										if (profile.contentEquals("Affiliated")) {
									%>

									<p class="btn-group">
									<div>

										<div class="controls">
											<table>
												<tr>
													<td><strong>Select Course</strong></td>
													<td><select name="course" id="courseId"
														onchange="" data-rel="chosen"
														style="width: 240px;">

															<option value="">---Select Course---</option>
															<option value="All">All Course</option>
															<s:iterator value="listOfCourse" status="var">
																<option value="<s:property/>"><s:property /></option>
															</s:iterator>
													</select></td>


												</tr>

											</table>

										</div>




									</div>
									</p>
									<p class="btn-group">
									
									<div id="feeName">

										<div class="controls">
											<table >
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
									</P>



									<%
										}
									%>
									<div>
										<input type="button" onclick="searchDues()" class="btn btn-info" value="Search">

									</div>
									<div id="dueReport"></div>

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

							<!-- <footer>
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
						<script
							src='bower_components/fullcalendar/dist/fullcalendar.min.js'></script>

						<!-- data table plugin -->
						<script src='js/jquery.dataTables.min.js'></script>

						<!-- select or dropdown enhancer -->
						<script src="bower_components/chosen/chosen.jquery.min.js"></script>
						<!-- plugin for gallery image view -->
						<script src="bower_components/colorbox/jquery.colorbox-min.js"></script>
						<!-- notification plugin -->
						<script src="js/jquery.noty.js"></script>
						<!-- library for making tables responsive -->
						<script
							src="bower_components/responsive-tables/responsive-tables.js"></script>
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
						<script type="text/javascript">
							function universitySelected(universityName) {
								alert(universityName);
								if (universityName == "") {
									return false
								}

								var ajax = true;
								var query = "?universityName=" + universityName
										+ "&ajax=" + ajax;
								var xmlhttp;
								if (window.XMLHttpRequest) {
									xmlhttp = new XMLHttpRequest();
								} else {
									xmlhttp = new ActiveXObject(
											"Microsoft.XMLHTTP");
								}
								xmlhttp.onreadystatechange = function() {
									if (xmlhttp.readyState == 4
											&& xmlhttp.status == 200) {
										document.getElementById("collegeList").innerHTML = xmlhttp.responseText;
									}
								}
								xmlhttp.open("GET",
										"getValForDropDown" + query, true);
								xmlhttp.send();

							}
							function collegeSelected(collegeName) {

								if (collegeName == "") {
									return false
								}
								var ajax = true;
								var query = "?collegeName=" + collegeName
										+ "&ajax=" + ajax;
								var xmlhttp;
								if (window.XMLHttpRequest) {
									xmlhttp = new XMLHttpRequest();
								} else {
									xmlhttp = new ActiveXObject(
											"Microsoft.XMLHTTP");
								}
								xmlhttp.onreadystatechange = function() {
									if (xmlhttp.readyState == 4
											&& xmlhttp.status == 200) {
										document.getElementById("courseList").innerHTML = xmlhttp.responseText;
									}
								}
								xmlhttp.open("GET",
										"getValForDropDown" + query, true);
								xmlhttp.send();

							}
							function courseSelected(courseName) {

								if (courseName == "") {
									return false;
								}
								var ajax = true;
								var query = "?courseName=" + courseName
										+ "&ajax=" + ajax;
								var xmlhttp;
								if (window.XMLHttpRequest) {
									xmlhttp = new XMLHttpRequest();
								} else {
									xmlhttp = new ActiveXObject(
											"Microsoft.XMLHTTP");
								}
								xmlhttp.onreadystatechange = function() {
									if (xmlhttp.readyState == 4
											&& xmlhttp.status == 200) {
										document.getElementById("feeName").innerHTML = xmlhttp.responseText;
									}
								}
								xmlhttp.open("GET",
										"getValForDropDown" + query, true);
								xmlhttp.send();

							}
							function searchDues() {
								var courseName = null;
								var feeName = null;
								var universityName = null;
								var collegeName = null;
								var query = null;
						<%if(profile.contentEquals("SU")){%>
							universityName = document
										.getElementById("universityName").value;
								courseName = document
										.getElementById("courseId").value == null ? ""
										: document.getElementById("courseId").value;
								feeName = document.getElementById("feeId").value;
								collegeName = document
										.getElementById("collegeName").value;
								query = "?universityName=" + universityName
										+ "&collegeName=" + collegeName
										+ "&courseName=" + courseName
										+ "&feeName=" + feeName;
						<%}else if(profile.contentEquals("Affiliated")) 
								{%>
							courseName = document
										.getElementById("courseId").value == null ? ""
										: document.getElementById("courseId").value;
								feeName = document.getElementById("feeId").value;
								query = "?universityName=" + universityName
										+ "&collegeName=" + collegeName
										+ "&courseName=" + courseName
										+ "&feeName=" + feeName;
						<%}
						else if(profile.contentEquals("Parent")) {%>
							courseName = document
										.getElementById("courseId").value == null ? ""
										: document.getElementById("courseId").value;
								feeName = document.getElementById("feeId").value;
								collegeName = document
										.getElementById("collegeName").value;
								query = "?universityName=" + universityName
										+ "&collegeName=" + collegeName
										+ "&courseName=" + courseName
										+ "&feeName=" + feeName;
						<%}%>
							
								var xmlhttp;
								if (window.XMLHttpRequest) {
									xmlhttp = new XMLHttpRequest();
								} else {
									xmlhttp = new ActiveXObject(
											"Microsoft.XMLHTTP");
								}
								xmlhttp.onreadystatechange = function() {
									if (xmlhttp.readyState == 4
											&& xmlhttp.status == 200) {
										document.getElementById("dueReport").innerHTML = xmlhttp.responseText;
									}
								}
								xmlhttp.open("GET", "CustomCollegeDueReport"
										+ query, true);
								xmlhttp.send();

							}
						</script>
</body>
</html>
