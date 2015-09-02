package com.dexpert.feecollection.main.communication.sms;

import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {
	public SendSMS() {
	}

	// create an account on ipipi.com with the given username and password

	public void msgsend(String recipient, String message) {

		try {

			String username = "9999600509";
			String password = "abechor2014";
			String sender = "AdDesk";
			String requestUrl = "http://bhashsms.com/api/sendmsg.php?user=" + username + "&pass=" + password
					+ "&sender=" + sender + "&phone=" + recipient + "&text" + message + "&priority=ndnd&stype=normal";
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			System.out.println("Success message :" + uc.getResponseMessage());
			uc.disconnect();
		} catch (Exception ex) {
			System.out.println("Failure Message ::" + ex.getMessage());
		}
	}
}