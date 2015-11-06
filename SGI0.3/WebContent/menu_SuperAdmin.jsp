<li class="nav-header">Main</li>
<div class="nav nav-stacked"></div>
<ul class="nav nav-pills nav-stacked main-menu">
	<li><a class="ajax-link"
		href='<%=session.getAttribute("dashLink").toString()%>'><i
			class="glyphicon glyphicon-home"></i><span> Dashboard</span></a></li>
	<li><a class="ajax-link" href="UniversityDetailRecord"><i
			class="fa fa-building"></i><span> Parent Institute</span></a></li>
	<li><a class="ajax-link" href="GetCollegeListOnUniversity"><i
			class="fa fa-building"></i><span> Affiliated Institutes</span></a></li>
	<li><a class="ajax-link" href="Admin-FeeConfig.jsp"><i
			class="fa fa-building"></i><span> Fee Configuration</span></a></li>
	<li><a class="ajax-link" href="CollegeOperatorDetail"><i
			class="fa fa-building"></i><span> Institute Operator</span></a></li>
	<li><a class="ajax-link" href="Admin-Reports.jsp"><i
			class="fa fa-list-alt"></i><span> Reports</span></a></li>

</ul>
