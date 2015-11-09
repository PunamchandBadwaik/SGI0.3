<!DOCTYPE html >
<%-- <%@page import="org.apache.jasper.compiler.Node.DoBodyAction"%> --%>
<%@page import="com.dexpert.feecollection.main.users.applicant.AppBean"%>
<%@page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%@page import="com.dexpert.feecollection.main.users.LoginBean"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="org.apache.struts2.views.jsp.TagUtils"%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@page import="com.dexpert.feecollection.main.fee.PaymentDuesBean"%>

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

<body onload="">
	<%
ValueStack vs =TagUtils.getStack(pageContext);
AppBean appBean =(AppBean)vs.findValue("app1");
HashMap<Integer,Integer> hm = new HashMap<Integer, Integer>();
//Integer cId= appBean.getAffBeanStu().getInstId();
Double amountAfterDiscount=(Double)vs.findValue("amountAfterDiscount");
double finalAmountToBePaid=(Double)vs.findValue("finalAmountToBePaid");
ArrayList<PaymentDuesBean> duesBean=(ArrayList<PaymentDuesBean>)vs.findValue("paymentDuesBeans");
%>


	<%
		int i = 1;
		int k=1;
	%>
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
						class="hidden-sm hidden-xs"><%=loginUser.getUserName()%></span> <span
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

		</div>
	</div>
	<!-- topbar ends -->
	<div class="ch-container">
		<div class="row">

			<!-- left menu starts -->
			<div class="col-sm-2 col-lg-2">
				<div class="sidebar-nav">
					<div class="nav-canvas">
						<%-- <div class="nav-sm nav nav-stacked"></div>
						<ul class="nav nav-pills nav-stacked main-menu">
							<li class="nav-header">Main</li>
							<li><a class="ajax-link"
								href='<%=session.getAttribute("dashLink").toString()%>'><i
									class="glyphicon glyphicon-home"></i><span> Dashboard</span></a></li>
							<%
								if (!profile.contentEquals("Student")) {
							%><li><a class="ajax-link" href="UniversityDetailRecord"><i
									class="fa fa-building"></i><span> Parent Institute</span></a></li>
							<%
								}
							%>
							<%
								if (!profile.contentEquals("Affiliated")){
							%>
							<li><a class="ajax-link" href="GetCollegeListOnUniversity"><i
									class="fa fa-building"></i><span> Affiliated Institutes</span></a></li>
							<%
								}
							%>
							<%
								if (profile.contentEquals("Affiliated")){
							%><li><a class="ajax-link" href="StudentTotalRecord"><i
									class="glyphicon glyphicon-home"></i><span> Student</span></a></li>
							<%
								}
							%>
							<%
								if (!profile.contentEquals("Affiliated")){
							%>
							<li><a class="ajax-link" href="Admin-FeeConfig.jsp"><i
									class="fa fa-building"></i><span> Fee Configuration</span></a></li>
							<%
								}
							%>
							<%
								if (profile.contentEquals("Affiliated")){
							%><li><a class="ajax-link" href="getInstDues"><i
									class="fa fa-list-alt"></i><span> Fee Payment</span></a></li>
							<%
								}
							%>
							<li><a class="ajax-link" href="Operator-Reports.jsp"><i
									class="fa fa-list-alt"></i><span> Reports</span></a></li>
						</ul> --%>
					<jsp:include page="menu_Student.jsp"></jsp:include>	
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
				<!-- 	<div>
					<ul class="breadcrumb">
						<li><a href="#">Home</a></li>
						<li><a href="#">Dashboard</a></li>
					</ul>
				</div> -->


				<div class="row">
					<div class="box col-md-12">
						<div class="box-inner">
							<div class="box-header well">
								<h2>
									<i class="glyphicon glyphicon-info-sign"></i> Student Fee
									Detail
								</h2>

								<div class="box-icon">

									<a href="#" class="btn btn-minimize btn-round btn-default"><i
										class="glyphicon glyphicon-chevron-up"></i></a>

								</div>
							</div>
							<div class="box-content row">
								<div class="col-lg-12 col-md-12 animated fadeIn">

									<table class="table table-condensed"
										style="font-weight: bold; font-size: large;">
										  <%-- <tr>

											<td>School Name</td>
											<td><s:property value="app1.affBeanStu.instName" /> <input
												type="hidden" id="collegeId" name="collegeId"
												value="<%=cId%>"></td>
										</tr>
										<tr>

											<td>Student UIN</td>
											<td><s:property value="app1.enrollmentNumber" /> <input
												type="hidden" id="collegeId" name="collegeId"
												value="<%=cId%>"></td>
										</tr>   --%>
										<tr>

											<td>Student Name</td>
											<td><s:property value="app1.aplFirstName" />&nbsp;<s:property
													value="app1.aplLstName" /></td>
											<td><input type="hidden"
												value='<s:property value="app1.isHosteler" />'
												id="isHosteler" /></td>
										</tr>

										<s:iterator value="app1.applicantParamValues" var="x">
											<tr>
												<td><strong><s:property
															value="#x.lookupname.lookupName" /></strong></td>
												<td><s:property value="value" /></td>

											</tr>




										</s:iterator>




									</table>
									<table class="table table-condensed">
										<tr>
											<th>Sr. No.</th>
											<th>Fee Name</th>
											<!-- <th>Payee Name</th>
											<th>Due Date</th>
											<th>Date Calculated</th>-->
											<th>Total fee Payment</th>
											<th>Net Due</th>
											<th>Payment to Date</th>
											<th></th>
											<th>Payable Amount</th>
										</tr>

										<%Iterator<PaymentDuesBean> itr=duesBean.iterator(); 
                                           while(itr.hasNext()){
                                       PaymentDuesBean paymentDue=itr.next();
                                    	  %>

										<%double totalDue=paymentDue.getTotal_fee_amount() ;%>
										<%if(totalDue!=0) {%>
										<%-- <td><td><span style="color: green; font-weight: bold;">Fees
													NOT Applicable</span></td> </td> --%>

										<tr>

											<td><%=i%></td>
											<td style="display: none"><%=paymentDue.getFeeId() %><input
												type="hidden" value='<%=paymentDue.getFeeId() %>'
												id="feeId[<%=i%>]"></td>
											<% hm.put(i,paymentDue.getSequenceId()); %>
											<td><%=paymentDue.getFeeName() %></td>
											<%-- <td><s:property value="payee" /></td>
												<td><s:property value="dueDate" /></td>
												<td><s:property value="dateCalculated" /></td> --%>
											<td><%=paymentDue.getTotal_fee_amount()%></td>
											<%double netDue=paymentDue.getNetDue(); %>
											<% if(netDue==0&&totalDue>0){%>
											<td><span style="color: green; font-weight: bold;">Fees
													Completed</span></td>
											<%-- <td><%=netDue %>
											
											</td> --%>
											<%}else {%><%-- <%else{%> --%>
											<td><%=netDue %></td>
											<input type="hidden" value='<%=netDue %>'
												id="payableamount[<%=i%>]" />
											<%} %>

											<%-- <%} %> --%>
											<%--  <input type="hidden" value='<%=netDue %>'
													id="payableamount[<%=i%>]"/>
											 --%>
											<td><%=paymentDue.getPayments_to_date()==null?0:paymentDue.getPayments_to_date() %></td>
											<td><div class="checkbox">
													<% if(netDue!=0&&totalDue>0){%>
													<label> <input type="checkbox"
														id="checkId[<%=i %>]" onclick="showTextBox(<%=i%>)"
														class="btn btn-check">Check to Add to Payment
													</label>
												</div></td>

											<td>
												<%-- <% if(cId==2){ %>
											<input type="text" 
												name="FeePaid" id="FeePaid[<%=i%>]" style="display: none;" readonly="readonly" 
												onchange="callFun(this.value)"> 
												
												<input type="hidden" id="lmtFeesForLPS" value='<s:property value="totalDueOFStudent" />'>
												<%}else { %> --%> <input type="text" style="display: none;"
												name="FeePaid" id="FeePaid[<%=i%>]"
												onchange="callFun(this.value)"> <input type="hidden"
												id="lmtFeesForLPS"
												value='<s:property value="totalDueOFStudent" />'> <%-- <%} %> --%>



												<%-- <input type="text" style="display: none;"
												name="FeePaid" id="FeePaid[<%=i%>]"
												onchange="callFun(this.value)"> --%> <script
													type="text/javascript">
													
													function showTextBox(k) {
														
														var feeTxtId = document.getElementById("FeePaid["+k+"]");
														//alert('feeTxtId is '+feeTxtId);
														var myCheckId = document.getElementById("checkId["+k+"]");
														//alert('myCheckId is '+myCheckId);
														var v=document.getElementById("payableamount["+k+"]").value;
														//alert('v is '+v);
														var d = parseFloat(document.getElementById("FeePaid["+k+"]").value);
														
														if(myCheckId.checked){
															
															feeTxtId.value=v;
															var h=0;
															if(document.getElementById("totalPaidAmount").value=='NaN' || document.getElementById("totalPaidAmount").value==''){
																h=0;
															}
															else{
																h=document.getElementById("totalPaidAmount").value;
															}
															document.getElementById("totalPaidAmount").value=parseFloat(h)+parseFloat(feeTxtId.value);
															feeTxtId.style.display = myCheckId.checked ? "block" : "none";
													
														}
														else{
															
															
															var h=0;
															
															if(isNaN(d)){																
																d=0;
															}
															if(document.getElementById("totalPaidAmount").value=='NaN' || document.getElementById("totalPaidAmount").value==''){
																h=0;
															}
															else{
																h=document.getElementById("totalPaidAmount").value;
																//document.getElementById("totalPaidAmount").value=parseFloat(h)-parseFloat(feeTxtId.value);
																document.getElementById("totalPaidAmount").value=parseFloat(h)-parseFloat(d);
																feeTxtId.value=0;
															}
															
															
															
															feeTxtId.style.display = myCheckId.checked ? "block" : "none";	
														}
														
			
													}
													
													</script> <script type="text/javascript">
														function callFun(fee) {
															var j = 1;
															var g = 0;
															
															
															for (j = 1; j <= <%=i%>; j++) {
																/* alert("in loop");
																alert(document.getElementById("FeePaid["+ j+ "]").value); */
																if(document.getElementById("FeePaid["+j+"]").value=='NaN' || document.getElementById("FeePaid["+j+"]").value==''){
																	g=g;
																}
																else{
																	g=g+parseFloat(document.getElementById("FeePaid["+j+"]").value);
																}
																
																
																/* alert(g); */

															}
															document.getElementById("totalPaidAmount").value=g; 
															
															
														}
													</script> <input type="hidden" name="paymentDueStr"
												id="paymentDueStr" value="" />
											</td>
											<%
													i++;
														k = i;
												%>

										</tr>
										<%} %>



										<%} %>

										<%-- <s:iterator value="app1.paymentDues">


											<tr>

												<td><%=i%></td>
												<td style="display: none"><s:property value="feeId" /><input
													type="hidden" value='<s:property value="feeId" />'
													id="feeId[<%=i%>]"></td>
												<td><s:property value="feeName" /></td>
												<td><s:property value="payee" /></td>
												<td><s:property value="dueDate" /></td>
												<td><s:property value="dateCalculated" /></td>
                                                  <td>
                                               
											  <s:property value="total_fee_amount" /> 
												 
												 
												 </td> 
												<td><s:set var="netDue">
														<s:property value="netDue" />
													</s:set> <s:if test='%{netDue=="0"}'>
														<span style="color: green; font-weight: bold;">Fees
															Completed</span>
													</s:if> <s:else>
														<s:property value="netDue" /></s:else>
													 <input type="hidden" value='<s:property value="netDue" />'
													id="payableamount[<%=i%>]"/></td>


												<td><s:property value="payments_to_date" /></td>
												<td><div class="checkbox">
		                                          <label> <input type="checkbox"
														id="checkId[<%=i %>]" onclick="showTextBox(<%=i%>)"
															class="btn btn-check">Check to Add to Payment
														</label>
													</div></td>
												<td><input type="text" style="display: none;"
													name="FeePaid" id="FeePaid[<%=i%>]"
													onchange="callFun(this.value)"> <script
														type="text/javascript">
													
													function showTextBox(k) {
														
														var feeTxtId = document.getElementById("FeePaid["+k+"]");
														var myCheckId = document.getElementById("checkId["+k+"]");
														var v=document.getElementById("payableamount["+k+"]").value;
														
														if(myCheckId.checked){
															
															feeTxtId.value=v;
															var h=0;
															if(document.getElementById("totalPaidAmount").value=='NaN' || document.getElementById("totalPaidAmount").value==''){
																h=0;
															}
															else{
																h=document.getElementById("totalPaidAmount").value;
															}
															document.getElementById("totalPaidAmount").value=parseFloat(h)+parseFloat(feeTxtId.value);
															feeTxtId.style.display = myCheckId.checked ? "block" : "none";
														}
														else{
															
															
															var h=0;
															if(document.getElementById("totalPaidAmount").value=='NaN' || document.getElementById("totalPaidAmount").value==''){
																h=0;
															}
															else{
																h=document.getElementById("totalPaidAmount").value;
																document.getElementById("totalPaidAmount").value=parseFloat(h)-parseFloat(feeTxtId.value);
																feeTxtId.value=0;
															}
															
															feeTxtId.style.display = myCheckId.checked ? "block" : "none";	
														}
														
			
													}
													
													</script> <script type="text/javascript">
														function callFun(fee) {
															var j = 1;
															var g = 0;
															
															
															for (j = 1; j <= <%=i%>; j++) {
																/* alert("in loop");
																alert(document.getElementById("FeePaid["+ j+ "]").value); */
																if(document.getElementById("FeePaid["+j+"]").value=='NaN' || document.getElementById("FeePaid["+j+"]").value==''){
																	g=g;
																}
																else{
																	g=g+parseFloat(document.getElementById("FeePaid["+j+"]").value);
																}
																
																
																/* alert(g); */

															}
															document.getElementById("totalPaidAmount").value=g; 
															
															
														}
													</script> <input type="hidden" name="paymentDueStr"
													id="paymentDueStr" value="" /></td>
												<%
													i++;
														k = i;
												%>

											</tr>
										</s:iterator> --%>

										<%}%>
										<%if(amountAfterDiscount>0) {%>
										<tr>
											<td>***</td>
											<td>Discount</td>
											<td>-<s:property value="discountedAmount" /></td>
											<td>-<s:property value="discountedAmount" /></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
										<%}%>
										<tr>
											<td></td>
											<td><span style="font-size: 20px; font-weight: bold;">Total
													Fees Amount</span></td>
											<td><span
												style="font-size: x-large; font-weight: bold; color: purple;">
													<s:property value="totalNetFees" />
											</span></td>
											<td><span
												style="font-size: x-large; font-weight: bold; color: purple;"><s:property
														value="totalDueOFStudent" /> <input type="hidden"
													value='<s:property value="totalDueOFStudent" />'
													id="totalNetDues"> </span></td>

											<td><span
												style="font-size: x-large; font-weight: bold; color: purple;"><s:property
														value="paymentDone" /></span></td>
											<td><span style="font-size: 20px; font-weight: bold;">Fees
													To be Paid</span></td>
											<td><input type="text" style="color:"
												id="totalPaidAmount" readonly="readonly"></td>

										</tr>

										<tr>
											<td colspan="9"><span style="float: right;"> <input
													type="button" class="btn btn-danger" id="btnPayment"
													value="Proceed to Payment" onclick="return validateFees()"></span>

												<script type="text/javascript">
														
												       function validateFees() { 
															 
															 
															 var tuitionFeePending=parseFloat(document.getElementById("payableamount[1]").value);
															 var lmtFee=document.getElementById("lmtFeesForLPS").value;
															 var collegeId=document.getElementById("collegeId").value;
															
													
															 if(collegeId==7){ var minimumAmountMustPaid=document.getElementById("isHosteler").value=="Yes"?lmtFee:lmtFee;
														}else{var minimumAmountMustPaid=document.getElementById("isHosteler").value=="Yes"?29000:100;}
														  
															
															//alert("tuitionFeePending is"+tuitionFeePending);
															var tuitionFeeBeingPaid = parseFloat(document.getElementById("FeePaid[1]").value);
															// alert("tuitionFeeBeingPaid now is"+tuitionFeeBeingPaid); 
															var totalBeingPaid =parseFloat(document.getElementById("totalPaidAmount").value);
															//alert("totalBeingPaid is"+totalBeingPaid);
															var totalNetDue=parseFloat(document.getElementById("totalNetDues").value);
															totalBeingPaid=isNaN(totalBeingPaid)?0:totalBeingPaid;
															var sequenceValidationCheckResult=true;
															if(totalNetDue<0){
																alert("Dues Must not be Less than 0");
																return false;																
																
															}
													var amountAfterDiscount=<%=amountAfterDiscount%>;
													if(amountAfterDiscount>0){
													totalBeingPaid><%=finalAmountToBePaid%>
													alert("please Pay Only"+<%=finalAmountToBePaid%>);
													return false;
													}		
															
															/* if(totalBeingPaid<29000){
																alert("Please select a higher Fee and amount to pay");
																return false;																
																
															} */															
															
															if(totalBeingPaid==0){
																alert("Please select a Fee and amount to pay");
																return false;																
																
															}
															/*
															if(isNaN(tuitionFeeBeingPaid)){
																
																if(totalBeingPaid==0 || isNaN(totalBeingPaid)){
																	alert("Please select a Fee and amount to pay");
																	return false;
																}
																else{
																	
																    tuitionFeeBeingPaid=0;
																}
															}
															*/
															if(collegeId==2){if(totalBeingPaid<minimumAmountMustPaid){
																alert("Please pay the Total Fees : "+minimumAmountMustPaid);
																return false;
																}
															}else{if(totalBeingPaid<minimumAmountMustPaid){
																	alert("Please pay at least :"+minimumAmountMustPaid);
																	return false;
																	}}
															
															
															var j=0;
															var t=0;
															for(j=1;j<<%=i%>;j++){
																// if the fee in loop is not being paid in full
																
																var loopFeeBeingPaid=parseFloat(document.getElementById("FeePaid["+j+"]").value);
																 if(isNaN(loopFeeBeingPaid)){
																	 loopFeeBeingPaid=0;
																 }
																 var loopFeePending=parseFloat(document.getElementById("payableamount["+j+"]").value);
																 if(isNaN(loopFeePending)){
																	 loopFeePending=0;
																 }																 
																if(loopFeeBeingPaid<loopFeePending){
																	
																   	for (t=j+1;t<<%=i%>;t++){
																		 var innerLoopFeeBeingPaid=parseFloat(document.getElementById("FeePaid["+t+"]").value);
																		 
																		 if(isNaN(innerLoopFeeBeingPaid)){
																			 innerLoopFeeBeingPaid=0;
																		 }
																   		if(innerLoopFeeBeingPaid>0){
																   			sequenceValidationCheckResult=false;
																			break;
																   		}
																   		else{
																   			
																   			sequenceValidationCheckResult=true;
																   		}
																   		
																   	}
																	
																}
																
														   		if(sequenceValidationCheckResult==false){
														   			break;
														   		}
																
															}
														/*
															if(tuitionFeeBeingPaid<tuitionFeePending && tuitionFeeBeingPaid<totalBeingPaid){
																
																alert("Tuition Fees must be cleared ahead of paying any other Fees, please reassign payable amounts!");
																return false;
															}   
															*/		 		 
																										 
																  
													        if(sequenceValidationCheckResult==false){
													        	
													   			alert("Please clear Fees in top down order first");
													   			return false;
													        }
															else{
																
																var t=1;
																var dueStr="";
																for(t=1;t<<%=i%>;t++){
																	if(t==<%=i-1%>){
																		if(document.getElementById("FeePaid["+t+"]").value!=''){
																			dueStr=dueStr+document.getElementById("feeId["+t+"]").value+"~"+document.getElementById("FeePaid["+t+"]").value;
																		}
																	}
																	else{
																		if(document.getElementById("FeePaid["+t+"]").value!=''){
																			dueStr=dueStr+document.getElementById("feeId["+t+"]").value+"~"+document.getElementById("FeePaid["+t+"]").value+"!";
																	  }
																	}
																}
																//alert("due str prepared is"+dueStr);
																
																
																
															
																
																document.getElementById("paymentDueStr").value=dueStr;
																var queryString="?dueString="+dueStr+"&totalPaidAmount="+totalBeingPaid;
																window.location="StudentPayment"+queryString;
																return true;
															}
														}
														
													</script></td>
										</tr>
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

		<!--   <footer>
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


</body>
</html>
