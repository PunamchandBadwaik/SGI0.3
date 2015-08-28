<!DOCTYPE html>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
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

	if (cookie.getName().equals("loginUser"))
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

<style type="text/css">
#tags {
	float: left;
	border: 1px solid #ccc;
	padding: 5px;
	font-family: Arial;
}

#tags span.tag {
	cursor: pointer;
	display: block;
	float: left;
	color: #fff;
	background: #689;
	padding: 5px;
	padding-right: 25px;
	margin: 4px;
}

#tags span.tag:hover {
	opacity: 0.7;
}

#tags span.tag:after {
	position: absolute;
	content: "x";
	border: 1px solid;
	padding: 0 4px;
	margin: 3px 0 10px 5px;
	font-size: 10px;
}

#tags input {
	background: #eee;
	border: 0;
	margin: 4px;
	padding: 7px;
	width: auto;
}
</style>
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

					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-info-sign"></i> Indicate All
									Possible Parameter Values
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
												<th></th>
												<th></th>
												<th></th>
											</tr>
										</thead>
										<tbody>
											<tr>

												<td>1</td>
												<td>Parameter Scope</td>
												<input hidden="hidden" id="paramID"
													value='<s:property value="lookupdata.lookupId"/>'>
												<td><s:property value="lookupdata.lookupScope" /></td>
											</tr>
											<tr>
												<td>2</td>
												<td>Parameter Name</td>
												<td><s:property value="lookupdata.lookupName" /></td>
											</tr>
											<tr>
												<td>3</td>
												<td>Parameter type</td>
												<td><s:property value="lookupdata.lookupType" /></td>
											</tr>
											<tr>
												<td>4</td>
												<td>Description</td>
												<td><s:property value="lookupdata.lookupDesc" /></td>
											</tr>
											<tr>
											<td colspan="3">
											<table>
											<tbody>
											<s:iterator value="lookupdata.fvBeansList">
											<tr><td>1</td>
											<td> <s:property value="value"/> </td>
											<td><button class="btn btn-sm">x</button></td>
											</tr>
											</s:iterator>
											</tbody>
											</table>
											</td>
											
											</tr>
											<tr>
												<td>4</td>
												<td>Possible Values</td>
												<td><div id="tags">
														<input type="text" value=""
															placeholder="Enter a possible value" />
													</div></td>
											</tr>
										</tbody>
									</table>


								</div>


							</div>
						</div>
					</div>
					<div class="col-md-12">
						<button type="button" onclick="saveValues()"
							class="btn btn-success">Save Parameter</button>

						<button onclick="window.close()" class="btn btn-info">Close
						</button>
						<button onclick="window.history.back();" class="btn btn-default">Back</button>

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
		var values = {};

		$(function() {

			$('#tags input').on('keyup', function(e) {
				if (/(188|13)/.test(e.which)) {

					var txt = this.value.replace(/[^a-zA-Z]/g, '');
					AddToArray(txt);
					if (txt) {
						$(this).before('<span class="tag">' + txt + '</span>');
					}
					this.value = "";
				}
			})

			$('#tags').on('click', '.tag', function() {

				RemoveFromValues($(this).text());
				$(this).remove();
			});

		});

		function AddToArray(value) {
			/* alert("value received from jquery function "+value); */
			values[value] = value;
			/* alert(JSON.stringify(values)); */

		}

		function RemoveFromValues(value) {
			delete values[value];
			
		}

		function saveValues() {
			var id = document.getElementById("paramID").value;
			var dataArray = new Array;
			for ( var value in values) {
				dataArray.push(values[value]);
			}
			window.location = "saveParamValues?values=" + dataArray
					+ "&paramId=" + id;
		}
	</script>
</body>
</html>
