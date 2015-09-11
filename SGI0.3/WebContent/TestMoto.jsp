<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
<TITLE>TRANSACTION DETAILS</TITLE>
</HEAD>

<BODY bgcolor='#83a1C6'>

<br>
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
	com.opus.epg.sfa.java.PGReserveData oPGReserveData 		= new com.opus.epg.sfa.java.PGReserveData();
	com.opus.epg.sfa.java.CustomerDetails oCustomer = new com.opus.epg.sfa.java.CustomerDetails ();
	com.opus.epg.sfa.java.MerchanDise oMerchanDise = new com.opus.epg.sfa.java.MerchanDise();
	com.opus.epg.sfa.java.SessionDetail oSessionDetail = new com.opus.epg.sfa.java.SessionDetail();
	com.opus.epg.sfa.java.Address oHomeAddress =new com.opus.epg.sfa.java.Address();
	com.opus.epg.sfa.java.Address oOfficeAddress =new com.opus.epg.sfa.java.Address();
	com.opus.epg.sfa.java.AirLineTransaction oAirLineTrans= new com.opus.epg.sfa.java.AirLineTransaction();

	PostLib oPostLib	= new PostLib();

	oMerchant.setMerchantDetails(
			"00004725"       
			,"00004725"	
			,"00004725"		
			,"localhost"          
			, System.currentTimeMillis()+""	
			,"ORD123"	
			, null		
			, null		
			,"INR"		
			,"INV123" 	
			, "req.Preauthorization"	
			, "50"			
			,"GMT+05:30"		
			, "Ext1"		
			, "Ext2"
			, "Ext3"
			, "Ext4"
			, "Ext5a"
			);
				

	oBTA.setAddressDetails(
			"CID"
			,"MahaLakshmi"
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
			"2,Mitali"
			,"Orion Comp"
			,"Aundh Road"
			,"city"
			,"state"
			,"4385435873"
			,"IND"
			, "test@test.com"
			);

	oCI.setCardDetails(
			"MC"
			,"5081264401288025"
			,"111"
			,"2007"
			,"12"
			,"Avinash"
			,"CREDI"
			);


	oMPI.setMPIResponseDetails(
				"01"
				,"NTBlZjRjMThjMjc1NTUxYzk1MTY="
				,"N"
				, "AAAAAAAAAAAAAAAAAAAAAAAAAAA="
				,"84759435"
				,"1245"
				,"356"
				);
				
				
// For Bharosa				
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
	oCustomer.setCustomerDetails("Sandeep", // first name 
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
				   
	
	oAirLineTrans.setAirLineTransactionDetails("10-06-2007", //booking Date				   
					   	   "22-06-2007",//Flight Date
					   	   "13:20",//Flight Time
					   	   "119", //Flight number
					   	   "Sandeep", //Passenger Name
					   	   "1",  //Number if Tickets
					   	   "Y", // PassebgerName and Card name match
					   	   "25c",//PBR
					   	   "Pune", // Sector From
					   	   "Mumbai" //Sector To
					   	   );
					   	   
	
	//PGResponse	oPgResp	=oPostLib.postMOTO(oBTA,oSTA,oMerchant,oMPI,oCI,oPGReserveData,oCustomer,oSessionDetail,null,oMerchanDise);
	PGResponse	oPgResp	=		oPostLib.postMOTO(oBTA,oSTA,oMerchant,oMPI,oCI,oPGReserveData,oCustomer,oSessionDetail,oAirLineTrans,null);
	out.println("Response Code :" + oPgResp.getRespCode()+ "<br>");
	out.println("Response Message : " + oPgResp.getRespMessage()+ "<br>");
	out.println("Merchant Txn Id : " + oPgResp.getTxnId()+ "<br>");
	out.println(" Epg Txn Id :" + oPgResp.getEpgTxnId()+ "<br>");
	out.println(" AuthId Code :" + oPgResp.getAuthIdCode()+ "<br>");
	out.println(" RRN :" + oPgResp.getRRN()+ "<br>");
	out.println(" CVRESP Code :" + oPgResp.getCVRespCode()+ "<br>");
	out.println(" FDMS Result :" + oPgResp.getFDMSResult()+ "<br>");
	out.println(" FDMS Score  :" + oPgResp.getFDMSScore()+ "<br>");


%>


</BODY>

</HTML>