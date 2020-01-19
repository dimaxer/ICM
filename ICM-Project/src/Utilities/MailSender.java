package Utilities;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert.AlertType;

/** This class allows sending mails from the server to remote client mails */
public class MailSender {
	private static final String smtp = "smtp.gmail.com";
	private static final String email = "braude.t16@gmail.com";
	private static final String pass = "ICMAa123456";
	private static final String port = "465";
	private static final String auth = "true";
	private static MailSender singletonInstance = null;

	// Constructors ****************************************************

	/** Constructs an instance of the MailSender singleton. */
	private MailSender() {
		singletonInstance = this;
	}

	// Instance methods ************************************************
	/**
	 * Get the Singleton's Instance
	 * @return MailSender Singleton Instance
	 */
	public static MailSender getInstance() {
		if (singletonInstance == null)
			singletonInstance = new MailSender();
		return singletonInstance;
	}
	
	/**
	 * This method sends the email (or popup) to the recipient.
	 * @param recipient recipient
	 * @param subject subject
	 * @param content content
	 * @param popup popup
	 */
	public void send(String recipient, String subject, String content, boolean popup) {
		if (recipient == "" || subject == "" || content == "" || recipient == null || subject == null || content == null)
			throw new IllegalArgumentException("Illegal Mail Arguments Sent.");
		
		if (popup) {
			Alert.showAlert(AlertType.INFORMATION, "Mail has been sent:\n" + subject, content);
			return;
		}
		
		// Get the session object  
		Properties props = new Properties();
		props.put("mail.smtp.host", smtp);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	     
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
		{protected PasswordAuthentication getPasswordAuthentication() {return new PasswordAuthentication(email,pass);}});  
		
	   	// Compose the Email Content
		try {  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(email));  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient));  
			message.setSubject(subject);  
			message.setText(content);  
	       
			//send the message  
			Transport.send(message);  
	  
			System.out.println("E-mail has been sent successfully...");  
	   
	     } catch (MessagingException e) {e.printStackTrace();} 
	}
}
