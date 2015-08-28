epg	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
<TITLE>TRANSACTION DETAILS</TITLE>
</HEAD>

<BODY bgcolor='#83a1C6'>
<%@ page language="java" import="java.sql.Timestamp,com.opus.epg.sfa.java.*" session="false" isErrorPage="false" %>
<%!
public String getSecureCookie(HttpServletRequest request) {
	        String secureCookie = null;
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) { 
	            for (int i = 0; i < cookies.length; i++) {
	                if (cookies[i].getName().equals("vsc")) {
	                    secureCookie = cookies[i].getValue().trim();
	                    break; 
	                }
	            }
	        }
	        return secureCookie;
	    }
   %>

<%
	com.opus.epg.sfa.java.BillToAddress oBTA 	= new com.opus.epg.sfa.java.BillToAddress();
	com.opus.epg.sfa.java.ShipToAddress oSTA 	= new com.opus.epg.sfa.java.ShipToAddress();
	com.opus.epg.sfa.java.Merchant oMerchant 	= new com.opus.epg.sfa.java.Merchant();
	com.opus.epg.sfa.java.MPIData oMPI 		= new com.opus.epg.sfa.java.MPIData();
	com.opus.epg.sfa.java.CardInfo oCI 		= new com.opus.epg.sfa.java.CardInfo();

	com.opus.epg.sfa.java.PostLib oPostLib	= new com.opus.epg.sfa.java.PostLib();
	com.opus.epg.sfa.java.PGReserveData oPGReserveData	= new com.opus.epg.sfa.java.PGReserveData();
	
	com.opus.epg.sfa.java.CustomerDetails oCustomer = new com.opus.epg.sfa.java.CustomerDetails ();
	com.opus.epg.sfa.java.MerchanDise oMerchanDise = new com.opus.epg.sfa.java.MerchanDise();
	com.opus.epg.sfa.java.SessionDetail oSessionDetail = new com.opus.epg.sfa.java.SessionDetail();
	com.opus.epg.sfa.java.Address oHomeAddress =new com.opus.epg.sfa.java.Address();
	com.opus.epg.sfa.java.Address oOfficeAddress =new com.opus.epg.sfa.java.Address();
	com.opus.epg.sfa.java.AirLineTransaction oAirLineTrans= new com.opus.epg.sfa.java.AirLineTransaction();
		
	
	oMerchant.setMerchantDetails(
					"00004725"
					,"00004725"
					,"00004725"
					,"localhost"
					, System.currentTimeMillis()+""
					,"ORD123"
					//, "http://10.10.10.6/RailwayTicketing/PGResponse.asp"
					, "http://localhost:8080/RGUHS_25062015/SFAResponse.jsp"
					, "POST"
					,"INR"
					,"INV123"
					
					,"req.Sale"
					, "350.50"
					,"GMT+05:30"
					, "Ext1"
					, "true"
					, "Ext3"
					, "Ext4"
					, "Ext5"
					);



	oBTA.setAddressDetails(
			"CID"
			,"CustName"
			,"AddrLine1"
			,"AddrLine2"
			,"AddrLine3"
			,"city"
			,"state"
			,"523466"
			,"IND"
			, "test@test.com"
			);


	oSTA.setAddressDetails(
			"2 Mitali"
			,"Orion Comp"
			,"Aundh Road"
			,"city"
			,"state"
			,"4385435873"
			,"IND"
			,"test@test.com"
			);

	oMPI.setMPIRequestDetails("1245"
				,"$12.45"
				,"418",
				"2"
				,"2 shirts"
				,"12"
				,"20011212"
				,"12"
				,"0"
				,""
				,"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, application/x-shockwave-flash, */*"
				,"Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0)"
				); 

		oHomeAddress.setAddressDetails(
					"2,Sandeep"
					,"Uttam Corner"
					,"Chinchwad"
					,"Pune"
					,"state"
					,"4385435873"
					,"IND"
					, "test@test.com"
					);
					
		oOfficeAddress.setAddressDetails(
						"2,Opus"
						,"MayFairTowers"
						,"Wakdewadi"
						,"Pune"
						,"state"
						,"4385435873"
						,"IND"
						, "test@test.com"
					);
								  
		oCustomer.setCustomerDetails(
		                             "Sandeep", // first name 
					     "patil", //last name
					     oOfficeAddress,
					     oHomeAddress,
					     "9423203297",//mobile number
					     "13-06-2007", // Reg Date
					     "Y" // Billing and shipping address match
					);
		
		oSessionDetail.setSessionDetails(request.getRemoteAddr(), //This Customer ip,merchant need to send it.
							  getSecureCookie(request),  //cookie string
							  request.getLocale().getCountry(),
							  request.getLocale().getLanguage(), 
							  request.getLocale().getVariant() ,
							  request.getHeader ("user-agent")
					  );

		
		oMerchanDise.setMerchanDiseDetails("Computer",
							   "2",
							   "Intel",
							   "P4",
							   "Sandeep Patil", //buyers name
							   "Y" //Buyername and Card name match.
							   );
					   
						   
		
		oAirLineTrans.setAirLineTransactionDetails( "10-06-2007", //booking Date				   
					   	   "22-06-2007", //Flight Date
					   	   "13:20", //Flight Time
					   	   "119",  //Flight number
					   	   "Sandeep", //Passenger Name
					   	   "1", //Number if Tickets
					   	   "Y",// PassebgerName and Card name match
					   	   "25c",//PBR
					   	   "Pune", // Sector From
					   	   "Mumbai"//Sector To
					   	   );

	System.out.println("before PGResponse");
	
	
	System.out.println("before calling postssl ");

	
		//PGResponse oPGResponse = oPostLib.postSSL(oBTA,oSTA,oMerchant,oMPI,response,oPGReserveData,null,null,null,null);
		PGResponse oPGResponse = oPostLib.postSSL(oBTA,oSTA,oMerchant,oMPI,response,oPGReserveData,oCustomer,oSessionDetail,oAirLineTrans,null);
		if(oPGResponse.getRedirectionUrl() != null) {
			String strRedirectionURL = oPGResponse.getRedirectionUrl();
			response.sendRedirect(strRedirectionURL);
			
		}
		else {
			out.println("Error encountered. Error Code : " +oPGResponse.getRespCode() + " . Message " +  oPGResponse.getRespMessage());
		}
	




%>


</BODY>

</HTML>
