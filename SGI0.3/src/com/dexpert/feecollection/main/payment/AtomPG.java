package com.dexpert.feecollection.main.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Encoder;

public class AtomPG extends HttpServlet {

	public void PayMethod(String vOlnTxnNo,String vPayTo,String vState,String vStateCd,String vOlnAmt,String vOlnPayMode) throws IOException, ParserConfigurationException, SAXException{
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	PrintWriter out = response.getWriter();
	URLConnection con;
	URL url = null;
	String vVenderURL;
	String xmlURL = "";
	String xmlttype = "";
	String xmltempTxnId = "";
	String xmltoken = "";
	String xmltxnStage = "";

	/*String vOlnTxnNo = request.getParameter("iOlnRefNo");
	String vPayTo = request.getParameter("iPayTo");
	String vState = request.getParameter("iState");
	String vStateCd = request.getParameter("iStateCd");
	String vOlnAmt = request.getParameter("iOlnAmt");
	String vOlnPayMode = request.getParameter("iPayMode");*/

	
	out.println("<br><b>Online Reference  : </b>"+vOlnTxnNo);
	out.println("<br><b>Payment To        : </b>"+vPayTo);
	out.println("<br><b>State Name        : </b>"+vState);
	out.println("<br><b>State Code        : </b>"+vStateCd);
	out.println("<br><b>Amount            : </b>"+vOlnAmt);
	out.println("<br><b>Mode              : </b>"+vOlnPayMode);
	out.println("<br><br>");
	

	String b64ClientCode = new BASE64Encoder().encode("shcil".getBytes("UTF-8"));
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
	String CurrDateTime = sdf.format(new Date()).toString();

	vVenderURL = "https://paynetzuat.atomtech.in/paynetz/epi/fts?login=160&pass=Test@123&ttype=NBFundTransfer&prodid=NSE"
			+ "&amt="+vOlnAmt+"&txncurr=INR&txnscamt=0&clientcode="+b64ClientCode+"&txnid="+vOlnTxnNo
			+ "&date="+CurrDateTime+"&custacc=145799635412";

	vVenderURL = vVenderURL.replace(" ","%20");
	System.out.println(vVenderURL);
	out.println("<br>Vender URL is : <br>");
	out.println("<b>"+vVenderURL+"</b>");
	url = new URL(vVenderURL.toString());

	con = url.openConnection(Proxy.NO_PROXY);
	String CntType = con.getContentType();
	System.out.println("Cnt Type "+CntType+ " Length : " + con.getContentLength());

	if (con.getContentLength() != 0 )
	{
		System.out.println("Getting Content");
		
		BufferedReader inBuf = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		String vXMLStr = "";
		while ((inputLine = inBuf.readLine()) != null) 
		{
			System.out.println(inputLine);
			vXMLStr = vXMLStr + inputLine;
		}
		inBuf.close();
		System.out.println("Got Content");
		System.out.println(vXMLStr);
	   
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource isBuf = new InputSource();
		isBuf.setCharacterStream(new StringReader(vXMLStr));
		Document doc = dBuilder.parse(isBuf);
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("RESPONSE");

		for (int tempN = 0; tempN < nList.getLength(); tempN++) 
		{
			Node nNode = nList.item(tempN);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
			  Element eElement = (Element) nNode;
			  System.out.println("URL : " + eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue());
			  xmlURL = eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue();
			  
			  NodeList aList = eElement.getElementsByTagName("param");
			  String vParamName;
			  for (int atrCnt = 0; atrCnt< aList.getLength();atrCnt++)
			  {
				  vParamName = aList.item(atrCnt).getAttributes().getNamedItem("name").getNodeValue();
				  System.out.println("<br>paramName : : " + vParamName);
				  
				  if (vParamName.equals("ttype") )
				  {
					  xmlttype = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
				  }
				  else if (vParamName.equals("tempTxnId") )
				  {
					  xmltempTxnId = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
				  }
				  else if (vParamName.equals("token") )
				  {
					  xmltoken = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
				  }
				  else if (vParamName.equals("txnStage") )
				  {
					  xmltxnStage = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
				  }
			  }
			  out.println("<br><b>URL               : </b>" + xmlURL);
			  out.println("<br><b>param : ttype     : </b>" + xmlttype);
			  out.println("<br><b>param : tempTxnId : </b>" + xmltempTxnId);
			  out.println("<br><b>param : token     : </b>" + xmltoken);
			  out.println("<br><b>param : txnStage  : </b>" + xmltxnStage);
			}
		}//for

		String Atom2Request = xmlURL+"?ttype="+xmlttype+"&tempTxnId="+xmltempTxnId+"&token="+xmltoken+"&txnStage="+xmltxnStage;
		Atom2Request = Atom2Request.replace(" ","%20");
		System.out.println("ATOM 2nd Request : " + Atom2Request);
		out.println("<br>ATOM 2nd Request : <br><b>" + Atom2Request+"</b>");
		response.sendRedirect(Atom2Request);
	}
	out.close();

}

}
