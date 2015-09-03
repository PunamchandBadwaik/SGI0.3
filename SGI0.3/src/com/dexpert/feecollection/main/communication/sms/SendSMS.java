package com.dexpert.feecollection.main.communication.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

public class SendSMS {

	public String sendSMS(String recepient, String message) {
		String user = "spectra";
		String password = "research1";
		String senderId = "sanchi";
		String msg = "Welcome To FeeDesk ";
		String detail = msg + message;

		URLConnection connection = null;
		String turl = "http://bhashsms.com/api/sendmsg.php?user=" + user + "&pass=" + password + "&sender=" + senderId
				+ "&phone=" + recepient + "&text=" + detail + "&priority=ndnd&stype=normal";

		try {
			// Create connection
			URL url = new URL(turl);
			connection = url.openConnection();

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(turl.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(turl);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if
															// not Java 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {

			}
		}

	}
}