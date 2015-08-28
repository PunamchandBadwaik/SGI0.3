<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
<TITLE>TRANSACTION DETAILS</TITLE>
</HEAD>

<BODY bgcolor='#83a1C6'>
<%@ page language="java" import="java.util.*,java.sql.Timestamp,com.opus.epg.sfa.java.*" session="false" isErrorPage="false" %>



<%
	com.opus.epg.sfa.java.Merchant oMerchant 	= new com.opus.epg.sfa.java.Merchant();

	PostLib oPostLib	= new PostLib();


	if("Submit".equals(request.getParameter("actionchanged"))){


	oMerchant.setMerchantOnlineInquiry(request.getParameter("MerchantID")
					   ,request.getParameter("MerchantTxnID")
					  );

			PGSearchResponse    oPgSearchResp=oPostLib.postStatusInquiry(oMerchant);
			ArrayList oPgRespArr = oPgSearchResp.getPGResponseObjects();
			System.out.println("PGSearchResponse received from payment gateway:"+ oPgSearchResp.toString());
			int index=0;
			if (oPgRespArr !=  null){
			%>
			<table align="center" width="40%" border="2" >
			<td>
				Response Code
			</td>
			<td>
				Response Message
			</td>
			<td>
				Txn Id
			</td>
			<td>
				Epg Txn Id
			</td>
			<td>
				AuthIdCode
			</td>
			<td>
				RRN
			</td>
			<td>
				TxnType
			</td>
			<td>
				TxnDateTime
			</td>
			
			<td>
				Cv resp Code
			</td>

			<%


				for (index=0 ; index< oPgRespArr.size(); index++){
					PGResponse oPgResp = (PGResponse)oPgRespArr.get(index);
					System.out.println("PGResponse object:" + oPgResp.toString());
			%>



				<tr>
				     <td>
					<%
					out.println(oPgResp.getRespCode());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getRespMessage());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getTxnId());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getEpgTxnId());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getAuthIdCode());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getRRN());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getTxnType());
					%>
					</td>
				     <td>
					<%
					out.println(oPgResp.getTxnDateTime());
					%>
				     </td>
				     
				     <td>
					<%
					out.println(oPgResp.getCVRespCode());
					%>
				     </td>
				<tr>
			<%
				}
			%>
							</table>

			<%
			}
			else{

			%>
				<table align="center" width="40%" border="2" >

					<tr>
					     <td>
						<%
						out.println(oPgSearchResp.toString()+"<br>");
						%>
					     </td>
					<tr>

				</table>
			<%
			}
			return;

     }
%>
		<form name="frmSearch">
		<input type="hidden" name="actionchanged" value="">

		<table align="center" width="40%" border="3" >

			<tr>
				<td  colspan=2 align="center">
				  Test Online Inquiry
				</td>
			</tr>

			<tr>
			     <td>
				       Enter Merchant ID :
			      </td>
			      <td>
				  <input type="text" name="MerchantID" size="15" maxlength="15" value="00001148">
			      </td>
			 </tr>

			 <tr>
			 	<td>
			 	   Enter Merchant Txn ID:
			 	</td>
			 	<td>
			 	   <input type="text" name="MerchantTxnID" size="15" maxlength="15" value="<%=System.currentTimeMillis()%>">
			 	</td>
			 </tr>

			<tr>
				<td colspan=2 align= "center">
					<input type="button" name="btnSub" value="Search" onClick="JavaScript:onClk_Submit();">
				</td>
			</tr>
		</table>
		</form>
<script language="javascript">
function onClk_Submit()
{


		document.frmSearch.actionchanged.value="Submit";
		document.frmSearch.method = "POST";
		document.frmSearch.submit();

}


</script>


</BODY>

</HTML>
