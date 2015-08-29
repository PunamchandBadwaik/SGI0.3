package com.dexpert.feecollection.main.communication.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSessionBean {

	private int port = 587;
	private String host = "smtp.gmail.com";
	private String from = "feedesk@srslive.in";
	private boolean auth = true;
	private String username = "feedesk@srslive.in";
	private String password = "feedesk@srs";
	private Protocol protocol = Protocol.TLS;
	private boolean debug = true;

	public void sendEmail(String to, String subject, String Username, String Password, String Name) {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		switch (protocol) {
		case SMTPS:
			props.put("mail.smtp.ssl.enable", true);
			break;
		case TLS:
			props.put("mail.smtp.starttls.enable", true);
			break;
		}

		Authenticator authenticator = null;
		if (auth) {
			props.put("mail.smtp.auth", true);
			authenticator = new Authenticator() {
				private PasswordAuthentication pa = new PasswordAuthentication(username, password);

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return pa;
				}
			};
		}

		Session session = Session.getInstance(props, authenticator);

		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject(subject);
			message.setSentDate(new Date());
			/* message.setText(body); */
			Multipart multipart = new MimeMultipart("alternative");

			MimeBodyPart textPart = new MimeBodyPart();
			//If email client does not support html-------------------------
			String textContent = "Username: " + Username + " Password:" + Password;
			//-----------------------------------------------------------------
			textPart.setText(textContent);
			MimeBodyPart htmlPart = new MimeBodyPart();
			String htmlContent = "<html><h1>Welcome " + Name
					
					+ "</h1><p><h3>Please Use the following credentials to login to your Account</h3></p>"
					+ "<p><b>Username:</b> " + Username + "</p>" + "<p><b>Password:</b> " + Password + "</p></html>";
			htmlPart.setContent(htmlContent, "text/html");
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			message.setContent(multipart);
			Transport.send(message);
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}
}