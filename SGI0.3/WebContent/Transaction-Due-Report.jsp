<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>

<%
	//checking session
	LoginBean loginUser = new LoginBean();

	loginUser = (LoginBean) session.getAttribute("loginUserBean"); 
	String profile=(String)session.getAttribute("sesProfile");

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
						<div class="nav-sm nav nav-stacked"></div>
						<ul class="nav nav-pills nav-stacked main-menu">
							<li class="nav-header">Main</li>


							<li><a class="ajax-link"
								href='<%=session.getAttribute("dashLink").toString()%>'><i
									class="glyphicon glyphicon-home"></i><span> Dashboard</span></a></li>

							<%
								if (profile.contentEquals("Affiliated")) {
							%><li><a class="ajax-link" href="StudentTotalRecord"><i
									class="glyphicon glyphicon-home"></i><span> Students</span></a></li>


							<%
								}
							%>

							<%
								if (profile.contentEquals("CollegeOperator")) {
							%><li><a class="ajax-link" href="StudentEnrollmentNo.jsp"><i
									class="fa fa-list-alt"></i><span> Students Fee Payment</span></a></li>
							<li><a class="ajax-link" href="Operator-Reports.jsp"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>
							<%
								}
							%>


							<%
								if (profile.contentEquals("Student")) {
							%>
							<li><a class="ajax-link" href="Operator-Reports.jsp"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>
							<%
								}
							%>



							<%
								if (profile.contentEquals("SU")) {
							%>
							<li><a class="ajax-link" href="UniversityDetailRecord"><i
									class="fa fa-building"></i><span> Parent Institute</span></a></li>

							<li><a class="ajax-link" href="getCollegeList"><i
									class="fa fa-building"></i><span> Affiliated Institutes</span></a></li>

							<li><a class="ajax-link" href="Admin-FeeConfig.jsp"><i
									class="fa fa-building"></i><span> Fee Configuration</span></a></li>


							<li><a class="ajax-link" href="UniversityDetailRecord"><i
									class="fa fa-building"></i><span> College Operator</span></a></li>


							<li><a class="ajax-link" href="Admin-Reports.jsp"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>



							<%
								}
							%>

							<%
								if (profile.contentEquals("Parent")) {
							%>
							<li><a class="ajax-link" href="GetCollegeListOnUniversity"><i
									class="fa fa-building"></i><span> Affiliated Institutes</span></a></li>

							<%-- <li><a class="ajax-link" href="UniversityDetailRecord"><i
									class="fa fa-building"></i><span> College Operator</span></a></li>
 --%>
							<li><a class="ajax-link" href="Admin-FeeConfig.jsp"><i
									class="fa fa-building"></i><span> Fee Configuration</span></a></li>

							<li><a class="ajax-link" href="Admin-Reports.jsp"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>





							<%
								}
							%>


							<%
								if (profile.contentEquals("Affiliated")){
							%>

							<li><a class="ajax-link" href="CollegeOperatorDetail"><i
									class="fa fa-building"></i><span> College Operator</span></a></li>
							<li><a class="ajax-link" href="Admin-Reports.jsp"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>
							<%
								}
							%>






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
						<li><a href="#">Dashboard</a></li>
					</ul>
				</div> -->




				<!--/row-->
				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-list-alt"></i> Transaction Report
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">
									<div class="row"></div>
									<!---Content-->


									<table
										class="table table-condensed table-striped table-bordered bootstrap-datatable datatable responsive">
										<thead>
											<tr>
												<th width="7%">Sr. No.</th>
												<th>Student UIN</th>
												<th>Transaction ID</th>
												<th>Mode Of Tranaction</th>
												<th>Payee Name</th>
												<!-- <th>Payee Mobile</th> -->
												<!-- <th>Payee Email</th> -->
												<!-- <th>Payee Address</th> -->
												<th>Amount</th>
												<th>Status</th>

												<th>Transaction Date</th>



											</tr>
										</thead>
										<tbody>
											<%
												int i = 1;
											%>
											<s:iterator value="transactionDetailsForReport">
												<tr>
													<td><%=i%></td>

													<td class="center"><s:set var="val">
															<s:property value="studentEnrollmentNumber" />
														</s:set> <s:if test='%{#val=="NA"}'>

															<a href="#"
																onclick="window.open('showBulkTrans?transId=<s:property value="txnId" />','Bulk Transaction','width=700,height=500')"
																title="View Detail"> <span
																style="margin-left: 10px;"><s:property
																		value="studentEnrollmentNumber" /></span>

															</a>


														</s:if> <s:else>
															<span style="margin-left: 10px;"><s:property
																	value="studentEnrollmentNumber" /></span>

														</s:else></td>
													<td class="center"><span style="margin-left: 10px;"><s:property
																value="txnId" /></span></td>
													<td class="center"><span style="margin-left: 10px;"><s:property
																value="paymentMode" /></span></td>

													<td><span style="margin-left: 10px;"><s:property
																value="payeeFirstName" />&nbsp;&nbsp;<s:property
																value="payeeLstName" /></span></td>
													<%-- <td><span style="margin-left: 10px;"><s:property
																value="payeeMob" /></span></td> --%>
													<%-- <td><span style="margin-left: 10px;"><s:property
																value="payeeEmail" /></span></td> --%>
													<%-- <td><span style="margin-left: 10px;"><s:property
																value="payeeAdd" /></span></td> --%>
													<td>Rs. <span style="margin-left: 10px;"><s:property
																value="payeeAmount" /></span></td>
													<td><span style="margin-left: 10px;"><s:property
																value="status" /></span></td>
													<td><span style="margin-left: 10px;"><s:property
																value="transDate" /></span></td>
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
		function showSearchResults() {
			document.getElementById("SearchResultBox").style.display = "block";
			document.getElementById("CollegeDetailBox").style.display = "none";
		}
		function showDetails(id) {

			//var id=document.getElementById("instId").value;

			window.open("ViewCollegeDetails?instId=" + id, "CollegeDetails",
					"width=700,height=900");
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

		$('#the-basics .typeahead').typeahead({
			hint : true,
			highlight : true,
			minLength : 1
		}, {
			name : 'states',
			displayKey : 'value',
			source : substringMatcher(states)
		});
	</script>

</body>
</html>
