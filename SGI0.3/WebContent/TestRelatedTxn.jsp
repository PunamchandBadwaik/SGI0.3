<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
<TITLE>TRANSACTION DETAILS</TITLE>
</HEAD>

<BODY bgcolor='#83a1C6'>
<%@ page language="java" import="java.sql.Timestamp,com.opus.epg.sfa.java.*" session="false" isErrorPage="false" %>



<%
	com.opus.epg.sfa.java.Merchant oMerchant 	= new com.opus.epg.sfa.java.Merchant();

	PostLib oPostLib	= new PostLib();


	if("Submit".equals(request.getParameter("actionchanged"))){

			oMerchant.setMerchantRelatedTxnDetails(
						request.getParameter("MerchantID")
						,null
						,null
						,request.getParameter("MerchantTxnID")
						,request.getParameter("RootSysRefNum")
						,request.getParameter("RootPRefNum")
						,request.getParameter("RootAuthID")
						,null
						,null
						,request.getParameter("CurrCode")
						,request.getParameter("MessageType")
						,request.getParameter("Amount")
						,null
						,null
						,null
						,null
						,null
						,null
					);

			PGResponse    oPgResp=oPostLib.postRelatedTxn(oMerchant);
			out.println("Response Code: " + oPgResp.getRespCode()+ "<br>");
			out.println("Response Message : " + oPgResp.getRespMessage()+ "<br>");
			out.println("MerchantTxnId : " + oPgResp.getTxnId()+ "<br>");
			out.println("ePGTxnId :" + oPgResp.getEpgTxnId()+ "<br>");
			out.println("AuthIdCode :" + oPgResp.getAuthIdCode()+ "<br>");
			out.println("RRN :" + oPgResp.getRRN()+ "<br>");
			out.println(" CVRESP Code :" + oPgResp.getCVRespCode()+ "<br>");
			return;

     }
%>
		<form name="frmRelated">
		<input type="hidden" name="actionchanged" value="">

		<table align="center" width="40%" border="3" >

			<tr>
				<td  colspan=2 align="center">
				  Related Test Transaction
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
				Enter Vendor ID :
				</td>
				<td>
				<input type="text" name="Vendor" size="15" maxlength="15" value="00002542">
				</td>
			 </tr>
			 <tr>
			 	<td>
					Enter Partner ID :
				</td>
				<td>
					<input type="text" name="Partner" size="15" maxlength="15" value="00002542">
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
				<td>
		    		   Enter Language Code:
		    		</td>
		    		<td>
		    		     <input type="text" name="LanguageCode" size="15" maxlength="15" value="java">
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
					Enter Root Sys Ref No :
				</td>
				<td>
				    <input type="text" name="RootSysRefNum"  size="15" maxlength="15" value="200610040920190">
				</td>
			</tr>
			<tr>
				<td>
					Enter Root RRN No :
				</td>
				<td>
					<input type="text" name="RootPRefNum"  size="15" maxlength="15" value="000000001240">
				</td>
			</tr>
			<tr>
				<td>
					Enter Root Auth Code:
				</td>
				<td>
					<input type="text" name="RootAuthID"  size="15" maxlength="15" value="001240">
				</td>
			</tr>
			<tr>
				<td>
					Enter Response Method:
				</td>
				<td>
					<input type="text" name="RespMethod"  size="15" maxlength="15" value="POST">
				</td>
			</tr>
			<tr>
				<td>
					Enter Response URL   :
				</td>
				<td>
					<input type="text" name="RespURL"  size="15" maxlength="50" value="http://10.10.10.238:8080/SFAResponse.jsp">
				</td>
			</tr>
			<tr>
				<td>
					Enter Curr Code :
				</td>
				<td>
					<input type="text" name="CurrCode"  size="15" maxlength="5" value="INR">
				</td>
			</tr>
			<tr>
				<td>
					Enter the Amount:
				</td>
				<td>
					<input type="text" name="Amount"   size="15" maxlength="5" value="100">
				</td>
			</tr>
			<tr>
				<td>
					Enter GMT offset:
				</td>
				<td>
					<input type="text" name="Gmtoffset"   size="15" maxlength="5" value="GMT+05:30">
				</td>
			</tr>
			<tr>
				<td>
					Select Message Type:
				</td>
				<td>
				<select name=MessageType size=1>
					<option value="req.Authorization" selected>AUTH</option>
					<option value="req.Refund">Refund</option>
					<option value="req.VoidSale">Void Sale</option>
					<option value="req.VoidPreAuth">Void PreAuth</option>
					<option value="req.VoidAuth">Void Auth</option>
					<option value="req.VoidRefund">Void Refund</option>
				</select><br>
				</td>
			</tr>
			<tr>
				<td>
					Enter Ext1:
				</td>
				<td>
					<input type="text" name="Ext1" size="10" maxlength="10" value="Ext1">
				</td>
			</tr>
			<tr>
				<td>
					Enter Ext2:
				</td>
				<td>
					<input type="text" name="Ext2" size="10" maxlength="10" value="Ext2">
				</td>
			</tr>
			<tr>
				<td>
					Enter Ext3:
				</td>
				<td>
					<input type="text" name="Ext3" size="10" maxlength="10" value="Ext3">
				</td>
			</tr>
			<tr>
				<td>
					Enter Ext4:
				</td>
				<td>
					<input type="text" name="Ext4" size="10" maxlength="10" value="Ext4">
				</td>
			</tr>
			<tr>
				<td>
					Enter Ext5:
				</td>
				<td>
					<input type="text" name="Ext5" size="10" maxlength="10" value="Ext5a">
				</td>
			</tr>
			<tr>
				<td colspan=2 align= "center">
					<input type="button" name="btnSub" value="Pay" onClick="JavaScript:onClk_Submit();">
				</td>
			</tr>
		</table>
		</form>
<script language="javascript">
function onClk_Submit()
{
	TxnRefNo = document.frmRelated.RootSysRefNum.value;

	if (TxnRefNo.length > 0)
	{

		document.frmRelated.actionchanged.value="Submit";
		document.frmRelated.method = "POST";
		document.frmRelated.submit();
	}
	else
	{
		alert ("field is mandatory");
		return;
	}
}


</script>


</BODY>

</HTML>
