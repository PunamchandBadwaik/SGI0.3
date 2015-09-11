
<!DOCTYPE html>

<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="en">

<head>

<meta charset="utf-8">
<title>Fee Collection Portal- Reports</title>
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
	<div class="navbar navbar-default" role="navigation">

		<div class="navbar-inner">
			<button type="button" class="navbar-toggle pull-left animated flip">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>

			<a class="navbar-brand" href="index-College.html"> <img
				alt="Charisma Logo" src="img/logo20.png" class="hidden-xs" /> <span>Fee
					Collection Portal</span></a>

			<!-- user dropdown starts -->
			<div class="btn-group pull-right">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class="glyphicon glyphicon-user"></i><span
						class="hidden-sm hidden-xs"> <%=loginUser.getUserName()%></span>
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a href="#">Profile</a></li>
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
						<div class="nav-sm nav nav-stacked"></div>
						<ul class="nav nav-pills nav-stacked main-menu">
							<li class="nav-header">Main</li>
							<li><a class="ajax-link" href="index-Admin.jsp"><i
									class="glyphicon glyphicon-home"></i><span> Dashboard</span></a></li>
							<li><a class="ajax-link" href="getCollegeList"><i
									class="fa fa-building"></i><span> My Colleges</span></a></li>
							<li><a class="ajax-link" href="Admin-FeeConfig.jsp"><i
									class="fa fa-building"></i><span> Fee Configuration</span></a></li>
							<li><a class="ajax-link" href="Admin-Reports.html"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>
						</ul>
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
				<div>
					<ul class="breadcrumb">
						<li><a href="#">Home</a></li>
						<li><a href="#">Fee Configuration</a></li>
					</ul>
				</div>

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
										<button class="btn btn-default">Fee Templates</button>
										<button class="btn btn-default" onclick='ShowFeeValues()'>Fee
											Values</button>
										<button class="btn btn-default" onclick='ShowFeeParameters()'>Fee
											Parameters</button>
									</p>


								</div>


							</div>
						</div>
					</div>
				</div>
				<!--/row-->

				<!-- Fee Parameters Row -->
				<div class="row" id="FeeParametersBox" style="display: none">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-list-alt"></i> Fee Parameters
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">



									<p class="btn-group">
									<div class="btn-group">
										<button
											onclick='window.open("ParamForm.jsp", "Admin Report", "height=1080,width=1000")'
											class="btn btn-default ">Add New Parameter</button>

									</div>
									<button class="btn btn-default"
										onclick='window.open("Admin-Report.html", "Admin Report", "height=1080,width=1920")'>Search
										Parameters</button>

									</p>

									<table
										class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
										<thead>
											<tr>
												<th>Sr. No.</th>
												<th>Parameter Type</th>
												<th>Paramter Name</th>

												<th>Actions</th>
											</tr>
										</thead>
										<tbody>
											<%
												int i = 1;
											%>
											<s:iterator value="lookupBeansList">

												<tr>

													<td><%=i%></td>
													<td><s:property value="lookupName" /></td>
													<td class="center"><s:property value="lookupType" /></td>
													<td class="center"><a class="btn btn-success btn-sm"
														href="#"> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i> View
													</a> <a class="btn btn-info btn-sm" href="#"> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
													</a> <a class="btn btn-danger btn-sm" href="#"> <i
															class="glyphicon glyphicon-zoom-in icon-white"></i>
															Delete
													</a></td>


												</tr>




												<%
													i++;
												%>
											</s:iterator>
										</tbody>
									</table>
								</div>


							</div>
						</div>
					</div>
				</div>
				<!--/row-->

				<!--/row-->
				<!-- Fee Values Row -->
				<div class="row" id="FeeValuesBox" style="display: none">
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
										<button class="btn btn-default">Search Fees</button>

									</p>

									<table
										class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
										<thead>
											<tr>
												<th>Sr. No.</th>
												<th>Fee Name</th>
												<th>Paid By</th>
												<th>Actions</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>1</td>
												<td>Admission Fee</td>
												<td>College</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>2</td>
												<td>Affiliation Fee</td>
												<td>College</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>3</td>
												<td>Examination Fee</td>
												<td>College</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>

											<tr>
												<td>4</td>
												<td>Renewal Fee</td>
												<td>College</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>5</td>
												<td>Rechecking Fee</td>
												<td>Student</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>6</td>
												<td>Verification Fee</td>
												<td>Student</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>7</td>
												<td>Convocation Fee</td>
												<td>Student</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>8</td>
												<td>Duplicate Marksheet Fee</td>
												<td>Student</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
											<tr>
												<td>9</td>
												<td>Duplicate Certificate Fee</td>
												<td>Student</td>
												<td class="center"><a class="btn btn-success btn-sm"
													href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> View
												</a> <a class="btn btn-info btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Edit
												</a> <a class="btn btn-danger btn-sm" href="#"> <i
														class="glyphicon glyphicon-zoom-in icon-white"></i> Delete
												</a></td>
											</tr>
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

		<footer>
			<!-- 	<p class="col-md-9 col-sm-9 col-xs-12 copyright">
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
	<!-- TypeAhead Script -->
	<script src="js/typeahead.bundle.js"></script>
	<script>
		var reportType = null;
		var feeType = null;

		//1
		function showFeeTypeBox(x) {
			reportType = x;
			if (x == "college") {
				document.getElementById("FeeTypeBoxCollege").style.display = "block";
				document.getElementById("FeeTypeBoxStudent").style.display = "none";
				$("#selectFeeTypeCollege").chosen({
					disable_search_threshold : 10
				})
			} else if (x == "student") {
				document.getElementById("FeeTypeBoxStudent").style.display = "block";
				document.getElementById("FeeTypeBoxCollege").style.display = "none";
				$("#selectFeeTypeStudent").chosen({
					disable_search_threshold : 10
				})
			}
			document.getElementById("AreaBox").style.display = "none";
			document.getElementById("MonthBox").style.display = "none";
			document.getElementById("DeptBox").style.display = "none";
			document.getElementById("StreamBox").style.display = "none";
			document.getElementById("FacultyBox").style.display = "none";
			document.getElementById("CollegeBox").style.display = "none";

		}

		//2
		function showArea() {
			document.getElementById("MonthBox").style.display = "none";
			document.getElementById("DeptBox").style.display = "none";
			document.getElementById("StreamBox").style.display = "none";
			document.getElementById("FacultyBox").style.display = "none";
			document.getElementById("CollegeBox").style.display = "none";
			document.getElementById("AreaBox").style.display = "block";
			$("#selectAreaType").chosen({
				disable_search_threshold : 10
			})
		}

		//3
		function showCollegeBox() {

			document.getElementById("MonthBox").style.display = "none";
			document.getElementById("DeptBox").style.display = "none";
			document.getElementById("StreamBox").style.display = "none";
			document.getElementById("FacultyBox").style.display = "none";
			document.getElementById("CollegeBox").style.display = "block";

			$("#selectCollege").chosen({
				disable_search_threshold : 10
			})
		}

		//4
		function showDepartmentBox() {

			document.getElementById("StreamBox").style.display = "none";
			document.getElementById("FacultyBox").style.display = "none";
			document.getElementById("DeptBox").style.display = "block";
			document.getElementById("MonthBox").style.display = "none";

			$("#selectDept").chosen({
				disable_search_threshold : 10
			})
		}

		//5

		function showFacultyBox() {

			document.getElementById("MonthBox").style.display = "none";
			document.getElementById("StreamBox").style.display = "none";
			document.getElementById("FacultyBox").style.display = "block";

			$("#selectFacultyType").chosen({
				disable_search_threshold : 10
			})
		}

		//6
		function showStreamBox() {

			document.getElementById("MonthBox").style.display = "none";
			document.getElementById("StreamBox").style.display = "block";

			$("#selectStreamType").chosen({
				disable_search_threshold : 10
			})
		}

		//7
		function showMonthBox() {

			document.getElementById("MonthBox").style.display = "block";
			$("#selectMonth").chosen({
				disable_search_threshold : 10
			})
		}

		function showDailyRequests() {
			window.open("AdminReportStudent.html", "Dail Report",
					"width=1920,height=1080");
		}
	</script>

	<script>
		var substringMatcher = function(strs) {
			return function findMatches(q, cb) {
				var matches, substrRegex;

				// an array that will be populated with substring matches
				matches = [];

				// regex used to determine if a string contains the substring `q`
				substrRegex = new RegExp(q, 'i');

				// iterate through the pool of strings and for any string that
				// contains the substring `q`, add it to the `matches` array
				$.each(strs, function(i, str) {
					if (substrRegex.test(str)) {
						// the typeahead jQuery plugin expects suggestions to a
						// JavaScript object, refer to typeahead docs for more info
						matches.push({
							value : str
						});
					}
				});

				cb(matches);
			};
		};

		var states = [ 'Bangalore', 'Mysore', 'Tumkur', 'Belgaum', 'BG Nagar',
				'Hubli', 'Bijapur', 'Gulbarga', 'Bellary', 'Kolar' ];

		var colleges = [
				'M001 Bangalore Medical College',
				'M002 Dr. B.R Ambedkar Medical College',
				'M003 Institute of Aerospace Medicine',
				'M004 St. Johns Medical College',
				'M005 Kempegowda Institute Of Medical Sciences',
				'M006 Government Medical College',
				'M007 Siddartha Medical College',
				'M008 J.N Medical College',
				'M009 Adichunchanagiri Institute of Medical Sciences',
				'M010 MS Ramaiah Medical College',
				'M011 Sri Jayadeva Institute of Cardiovascular Sciences & Research',
				'M012 Kidwai Institute of Oncolgy',
				'M013 Al-Ameen Medical College', 'M014 M.R.Medical College',
				'M015 Vijayanagar Institute Of Medical Sciences',
				'M016 Command Hospital', 'M017 JSS Medical College',
				'M018 Devaraj Urs Medical College' ];

		$('#the-basics .typeahead').typeahead({
			hint : true,
			highlight : true,
			minLength : 1
		}, {
			name : 'states',
			displayKey : 'value',
			source : substringMatcher(states)
		});
		$('#the-college .typeahead').typeahead({
			hint : true,
			highlight : true,
			minLength : 1
		}, {
			name : 'states',
			displayKey : 'value',
			source : substringMatcher(colleges)
		});
		$("#selectReportType").chosen({
			disable_search_threshold : 10
		})
	</script>
	<script type="text/javascript">
		function ShowFeeValues() {
			document.getElementById("FeeValuesBox").style.display = "block";
			document.getElementById("FeeParametersBox").style.display = "none";

		}
		function ShowFeeParameters() {
			document.getElementById("FeeValuesBox").style.display = "none";
			document.getElementById("FeeParametersBox").style.display = "block";

		}
	</script>
</body>
</html>
