package com.dexpert.feecollection.main.communication.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {
	
	private final String USER_AGENT = "Chrome/44.0.2403.157";
	public SendSMS() {
	}

	// create an account on ipipi.com with the given username and password

	public void msgsend(String recipient, String message) {

		try {
			

			String username = "spectra";
			String password = "research1";
			String sender = "sanchi";
			String requestUrl = "http://bhashsms.com/api/sendmsg.php?user=" + username + "&pass=" + password + "&sender=" + sender + "&phone=" + recipient + "&text=" + message + "&priority=ndnd&stype=normal";
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("GET");
			uc.setRequestProperty("User-Agent", USER_AGENT);
			/*int responseCode = uc.getResponseCode();
			System.out.println("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(uc.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("Success message :" + uc.getResponseMessage());

			System.out.println("Message Url is ::" + requestUrl);
			
			*/
			
	        int responseCode = uc.getResponseCode();
	        System.out.println("GET Response Code :: " + responseCode);
	        if (responseCode == HttpURLConnection.HTTP_OK) { // success
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    uc.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	 
	            // print result
	            System.out.println(response.toString());
	        } else {
	            System.out.println("GET request not worked");
	        }
			
			
		} catch (Exception ex) {
			System.out.println("Failure Message ::" + ex.getMessage());
		}
	}
}