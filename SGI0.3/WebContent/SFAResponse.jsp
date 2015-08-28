<!doctype html public "-//w3c/dtd HTML 4.0//en">
<html>
<head>
    <title>SFA - Response from ePG Web Server</title>
</head>
<body bgcolor="lightyellow">
    <%@ page session="true" import="com.opus.epg.sfa.java.*,java.util.*,java.io.*,java.sql.Timestamp" info="SFA - JSP " contentType="text/html" %>
<%
   
   
    String astrResponseMethod= request.getMethod(); 
    String strMerchantId= "00001203";
    String astrDirectoryPath="c://key//";
    String astrClearData = null;	
    String respcd =null;
    String respmsg = null;
    String AuthIdCode = null;
    String RRN=null;
    String MerchantTxnId =null;
    String TxnRefNo =null;
    String Cookie=null;
    String FDMSScore=null;
    String FDMSResult= null;
    
    
    Hashtable oHashtable=null;
    if(astrResponseMethod.equals("POST")||astrResponseMethod.equals("post")){
	  
	String astrResponseData= request.getParameter("DATA");
	astrClearData =validateEncryptedData(astrResponseData,astrDirectoryPath,strMerchantId);
	oHashtable=new Hashtable();
   
   	StringTokenizer oStringTokenizer=new StringTokenizer(astrClearData,"&");
   	   while(oStringTokenizer.hasMoreElements()){
		String strData = (String)oStringTokenizer.nextElement();
		StringTokenizer oObj1=new StringTokenizer(strData,"=");
		String strKey=(String)oObj1.nextElement();
		String strValue=(String)oObj1.nextElement();
		oHashtable.put(strKey,strValue);
   	    }
 
   	  respcd=(String) oHashtable.get("RespCode");
   	  respmsg = ((String)oHashtable.get("Message")).replace('+',' ');
   	  AuthIdCode =(String) oHashtable.get("AuthIdCode");
   	  RRN = (String)oHashtable.get("RRN");
   	  MerchantTxnId = (String)oHashtable.get("TxnID");
   	  TxnRefNo      = (String)oHashtable.get("ePGTxnID");
   	  Cookie	= (String)oHashtable.get("Cookie");
   	  FDMSResult	= (String)oHashtable.get("FDMSResult");
   	  FDMSScore 	= (String)oHashtable.get("FDMSScore");
 
   }  
  
if(respcd.equalsIgnoreCase("0"))
  {
%>   
<p align="left"><font size="4" color="#FF0011"> Thank You For Visiting Indian Rail.. </font></p>
<%
}

%>


<br>
<p align="center"><font size="6" color="#0000FF">Your  Transaction Details are </font></p>
<hr>
<h4><font face="Verdana">
    Response code            :<%=respcd%> <br>
<h4>Response Message         :<%=respmsg%><br>
<h4>Transaction Reference No :<%=TxnRefNo%><br>
<h4>Merchant Transaction Id  :<%=MerchantTxnId%><br>
<h4>Authrization Id Code     :<%=AuthIdCode%><br>
<h4>RRN 	             :<%=RRN%><br>
<h4>Cookie                   :<%=Cookie%><br>
<h4>FDMSResult        	     :<%=FDMSResult%>
<h4>FDMSScore        	     :<%=FDMSScore%>


 <%!
 
  public String validateEncryptedData(String astrResponseData,String astrDirectoryPath,String strMerchantId) throws Exception {
 	EPGMerchantEncryptionLib    oEncryptionLib = new EPGMerchantEncryptionLib();
 	String astrClearData = null;
 	try {
 	    	  FileInputStream oFileInputStream =  new FileInputStream(new File(astrDirectoryPath + strMerchantId+".key"));
 	    	  BufferedReader oBuffRead = new BufferedReader(new InputStreamReader(oFileInputStream));
 	    	  String strModulus = oBuffRead.readLine();
 	    	  if(strModulus == null) {
 	    		throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
 	    	  }
 		  strModulus = decryptMerchantKey(strModulus, strMerchantId);
 		  if(strModulus == null) {
 		 	throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
 		  }
 		  String strExponent = oBuffRead.readLine();
 		  if(strExponent == null) {
 			throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
 		  }
 		 strExponent = decryptMerchantKey(strExponent, strMerchantId);
 		 if(strExponent == null) {
 		 	throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
 		 }
 		astrClearData =oEncryptionLib.decryptDataWithPrivateKeyContents(astrResponseData,strModulus,strExponent);
      		
      	}catch(Exception oEx){
      		oEx.printStackTrace();
      	}
 	finally {
 		return astrClearData;
 	}
    }
 
    public String decryptMerchantKey(String astrData , String astrMerchantId) throws Exception {
		return(decryptData(astrData, (astrMerchantId+astrMerchantId).substring(0, 16)));
    }


    public String decryptData(String strData , String strKey)throws Exception {
   	if(strData==null || strData==""){
		return null;
	}
	if(strKey==null || strKey==""){
		return null;
	}
	EPGCryptLib moEPGCryptLib = new EPGCryptLib();
	String strDecrypt=moEPGCryptLib.Decrypt(strKey, strData);
	return strDecrypt;
    }

%>

</body>
</html>