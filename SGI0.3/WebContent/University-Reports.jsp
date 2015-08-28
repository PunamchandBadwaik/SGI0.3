<!DOCTYPE html>
<html lang="en">
<head>

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

		<a href="http://www.feedesk.in/" target="blank"> <img alt="FeeDesk Logo" src="img/feeDesk_logo.png"
				style="width: 150px; height: 53px; margin-left: 20px;" />
			</a>

			<!-- user dropdown starts -->
			<div class="btn-group pull-right">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					<i class="glyphicon glyphicon-user"></i><span
						class="hidden-sm hidden-xs"> <username>(Username)</span>
					<span class="caret"></span>
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
						<div class="nav-sm nav nav-stacked"></div>
						<ul class="nav nav-pills nav-stacked main-menu">
							<li class="nav-header">Main</li>
							<li><a class="ajax-link" href='<%=session.getAttribute("dashLink").toString()%>'><i
									class="glyphicon glyphicon-home"></i><span> Dashboard</span></a></li>
							<li><a class="ajax-link" href="University-MyColleges.html"><i
									class="fa fa-building"></i><span> My Colleges</span></a></li>
							<li><a class="ajax-link" href="University-FeeConfig.html"><i
									class="fa fa-building"></i><span> Fee Configuration</span></a></li>
							<li><a class="ajax-link" href="University-Reports.jsp"><i
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
				<!-- <div>
					<ul class="breadcrumb">
						<li><a href="#">Home</a></li>
						<li><a href="#">Reports</a></li>
					</ul>
				</div> -->

				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-list-alt"></i> Quick Reports
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
										<!--      <button class="btn btn-default" onclick="showDailyRequests()">Today's Student Requests</button>
                    <button class="btn btn-default" onclick='window.open("University-Report.html", "University Report", "height=1080,width=1920")'>Payments This Month</button>
                    <button class="btn btn-default">Other Relevant Quick Links</button> -->
										<button class="btn btn-default"
											onclick="window.open('ShowCollegeReport','College Report','width=1500 height=800')">College
											Report</button>
									</p>



								</div>


							</div>
						</div>
					</div>
				</div>
				<!--/row-->


				<!-- <div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-search"></i> Custom Reports
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">
									-Content
									<div id="ReportTypeBox" class=" col-md-4"
										>
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Report Type</label>

											<div class="controls">
												<select id="selectReportType" onchange="showFeeTypeBox(this.value)">
													<option selected="selected" disabled="disabled">Select
														Report Type</option>
														<optgroup  label="College Reports">
														<option value="college">Due Payment Reports</option>
													<option value="college">Payment History Reports</option>
														</optgroup>
													<optgroup label="Student Reports">
													<option value="student">Student Request Reports</option>
													<option value="student">Student Payment Reports</option>
													</optgroup>
												</select>
											</div>
										</div>
									</div>
									<div class="col-lg-10 col-md-10"></div>
									<div id="FeeTypeBoxStudent" class=" col-md-4"  style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Fee Type</label>

											<div class="controls">
												<select id="selectFeeTypeStudent" onchange="showMonthBox()">
													<option selected="selected" disabled="disabled">Select
														Fee Type</option>
														<option value="3">All</option>
													<option value="1">Duplicate Certificate</option>
													<option value="2">Duplicate Marksheet</option>
													<option value="3">Convocation Fee</option>
													
												</select>
											</div>
										</div>
									</div>
									<div id="FeeTypeBoxCollege" class=" col-md-4"  style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Fee Type</label>

											<div class="controls">
												<select id="selectFeeTypeCollege" onchange="showArea()" >
													<option selected="selected" disabled="disabled">Select
														Fee Type</option>
														<option value="3">All</option>
													<option value="1">Admission Fee</option>
													<option value="2">Examination Fee</option>
													<option value="3">Affiliation Fee</option>
												</select>
											</div>
										</div>
									</div>
									<div class="col-lg-10 col-md-10"></div>
									<div id="AreaBox" class=" col-md-4"
										style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Area</label>

											<div class="controls">
												<select id="selectAreaType" onchange="showCollegeBox()">
													<option selected="selected" disabled="disabled">Select
														Area</option>
														<option>All</option>
													<option>Area A</option>
													<option>Area B</option>
													<option>Area C</option>
													
												</select>
											</div>
										</div>
									</div>
									<div class="col-lg-10 col-md-10"></div>
									<div id="CollegeBox" class=" col-md-4"
										style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												College</label>

											<div class="controls">
												<select id="selectCollege" onchange="showDepartmentBox()">
													<option selected="selected" disabled="disabled">Select
														College</option>
														<option>All</option>
													<option>College A</option>
													<option>College A</option>
													<option>College A</option>
													
												</select>
											</div>
										</div>
									</div>
									<div class="col-lg-10 col-md-10"></div>
									<div id="DeptBox" class=" col-md-4"
										style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Department</label>

											<div class="controls">
												<select id="selectDept" onchange="showFacultyBox()">
													<option selected="selected" disabled="disabled">Select
														Department</option>
														<option>All Departments</option>
													<option>Health Sciences</option>
													<option>Physical Sciences</option>
													<option>Commerce</option>
													<option>Arts</option>
													
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-lg-10 col-md-10"></div>
									<div id="FacultyBox" class=" col-md-4"
										style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Faculty</label>

											<div class="controls">
												<select  id="selectFacultyType" onchange="showStreamBox()">
													<option selected="selected" disabled="disabled">Select
														Faculty Type</option>
													<option>All</option>
													<option>Engineering</option>
													<option>MCA</option>
													<option>BCA</option>
												</select>
											</div>
										</div>
									</div>
									<div class="col-lg-10 col-md-10"></div>
									<div id="StreamBox" class=" col-md-4"
										style="display: none">
										<div class="control-group">
											<label class="control-label" for="selectError">Select
												Stream</label>

											<div class="controls">
												<select  id="selectStreamType" onchange="showMonthBox()">
													<option selected="selected" disabled="disabled">Select
														Stream</option>
													<option>All</option>
													<option>Computer Engineering</option>
													<option>Electronics</option>
													<option>IT</option>
													<option>Mechanical</option>
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-lg-10 col-md-10"></div>
									<div id="MonthBox" class=" col-md-4" style="display: none">
										<div class="control-group">

											<div class="controls">
												<label for="DistrictAutocomplete">From</label> <input
													id="DistrictAutocomplete" type="date" class="form-control">
											</div>
											<div class="controls">
												<label for="DistrictAutocomplete">To</label> <input
													id="DistrictAutocomplete" type="date" class="form-control">
											</div>
											<div class="whitespace"></div>
											<div class="controls">
												<button class="btn btn-info"
													onclick='window.open("University-Report.html", "University Report", "height=1080,width=1920")'>Generate
													Report</button>
											</div>
										</div>
									</div>
									<div class="col-lg-10 col-md-10"></div>

									
								</div>


							</div>
						</div>
					</div>
				</div> -->

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
			window.open("UniversityReportStudent.html", "Dail Report",
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

</body>
</html>
