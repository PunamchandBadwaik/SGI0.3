<!DOCTYPE html>
<%@page import="com.dexpert.calendaryear.CalendarBean"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="org.apache.struts2.views.jsp.TagUtils"%>
<%@page import="com.dexpert.feecollection.main.users.affiliated.AffBean"%>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<%
	//checking session
	 LoginBean loginUser = new LoginBean();
	loginUser = (LoginBean) session.getAttribute("loginUserBean");
	AffBean affBean = (AffBean) session.getAttribute("instBean");
	String profile = (String) session.getAttribute("sesProfile");
	ValueStack vs=TagUtils.getStack(pageContext);
    CalendarBean calendarBean=(CalendarBean) vs.findValue("calendarBean");
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

<body onload="showOnlyOneRow()">
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
					<form action="savePaymentCycles">
						<div class="box col-md-12">
							<div class="box-inner">
								<div class="box-header well">
									<h2>
										<i class="glyphicon glyphicon-info-sign"></i>Calendar Year
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
													<th>Payment Cycles</th>
													<th>Start Month</th>
													<th>Start Day</th>
													<th>End Day</th>

												</tr>
											</thead>
											<tbody>

												<%
													Integer numOfPayCycl=calendarBean.getNumberOfPaymentCycle(); 
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																												                                               
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																												                                             for(int i=1;i<=numOfPayCycl;i++)  
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																												                                               
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																												                                               {
												%>



												<tr id="<%=i%>">

													<!-- Day dropdown -->
													<input type="hidden"
														name="paymentCycleBeans[<%=i-1%>].cycleId" value="<%=i%>">
													<td>Payment Cycle <%=i%></td>
													<td><select data-rel="chosen" style="width: 150px;"
														required="required"
														name="paymentCycleBeans[<%=i-1%>].cycleStartMonth"
														id="strMonth<%=i%>"
														onchange="monthSelected(this.value,<%=i%>)" size="1">
															<option value="">--Select Month--</option>
															<option value="01">January</option>
															<option value="02">February</option>
															<option value="03">March</option>
															<option value="04">April</option>
															<option value="05">May</option>
															<option value="06">June</option>
															<option value="07">July</option>
															<option value="08">August</option>
															<option value="09">September</option>
															<option value="10">October</option>
															<option value="11">November</option>
															<option value="12">December</option>
													</select></td>
													<td><select style="width: 150px;"
														name="paymentCycleBeans[<%=i-1%>].cycleStratDay"
														id="DDAY<%=i%>" onchange="startDaySelected(<%=i%>)"
														required="required">
															<option value="">--Select day--</option>
													</select></td>
													<td><select multiple="multiple" size="2"
														name="paymentCycleBeans[<%=i-1%>].cycleStratDay"
														id="dueDate<%=i%>" onchange="" required="required">
															<option value="">--Select Due date--</option>
													</select></td>



													<%-- <td><select data-rel="chosen" style="width: 150px;"
														required="required"
														name="paymentCycleBeans[<%=i%>].cycleEndMonth"
														id="endMonth<%=i%>"
														onchange="endMonthSelected(this.value,<%=i%>)" size="1">
															<option value="">--Select Month--</option>
															<option value="01">January</option>
															<option value="02">February</option>
															<option value="03">March</option>
															<option value="04">April</option>
															<option value="05">May</option>
															<option value="06">June</option>
															<option value="07">July</option>
															<option value="08">August</option>
															<option value="09">September</option>
															<option value="10">October</option>
															<option value="11">November</option>
															<option value="12">December</option>
													</select></td>

 --%>
													<%-- <td><select style="width: 150px;" required="required"
														name="paymentCycleBeans[<%=i%>].cycleEndDay"
														id="EndDay<%=i%>" onchange="endDaySelected(<%=i%>)">
															<option value="">--Select day--</option>

													</select></td>

									 --%>
												</tr>
												<%
													}
												%>


											</tbody>
										</table>


									</div>


								</div>
							</div>
						</div>
						<div class="col-md-12">
							<button type="submit" onclick="validateYearsAndDates()"
								class="btn btn-success">Save Cycle</button>

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
						<button type="button" class="close" data-dismiss="modal">Ã?</button>
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
	    var invalidateselection=false;
	    var numberOfCycle=<%=calendarBean.getNumberOfPaymentCycle()%>;
	    function checkMonthOrder(value,id )
	    {
	    	var result=true;
	        value=parseInt(value);
	    	for(i=1;i<numberOfCycle;i++){
	    	var inLoopMonth=document.getElementById("strMonth"+i).value;	
	    	inLoopMonth=parseInt(inLoopMonth);
	    	if(i<id && value<=inLoopMonth){
	    	alert("invalid month selection");
	    	result=false;
	    	return result;
	    	} else if(i>id && value>=inLoopMonth){
	    	alert("invalid month selection please try again");
	    	result=false;
	    	return result;
	    	}	
	    }
	    	
	    }
		function monthSelected(value,id) {
			var result=checkMonthOrder(value,id );
			if(result==false){
			return false;	
			}
			var today = new Date();
			var days = new Date(today.getFullYear(), value * 1, -.1).getDate();
			document.getElementById("DDAY"+id).options.length = 0;
		    //document.getElementById("EndDay"+id).options.length = 0;
			var select = document.getElementById("DDAY"+id);
			var opt=document.createElement("option");
			opt.value ="";
			opt.innerHTML ="select Day";
			select.appendChild(opt);
			for (var i = 1; i <= days; i++) {
				var	opt = document.createElement("option");
				opt.value = i;
				opt.innerHTML = i;
				select.appendChild(opt);
				}
		var select1=document.getElementById("dueDate"+id)
			var opt=document.createElement("option");
			opt.value ="";
			opt.innerHTML ="select Day";
			select1.appendChild(opt);
			for (var i = 1; i <= days; i++) {
				var	opt = document.createElement("option");
				opt.value = i;
				opt.innerHTML = i;
				select1.appendChild(opt);
				}
			

		}

			function startDaySelected(id){
		
			validateAllSelectBox(id);
	    	
		        }
		function  showOnlyOneRow()
		{
			
		
			for(i=2;i<=<%=calendarBean.getNumberOfPaymentCycle()%>;i++){
		  
				
			}
			
			
		}
		
	</script>


</body>
</html>
