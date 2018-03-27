package com.sandisk;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {

		public void sendfromEC2(String subject, String body)throws Exception {
		    final String FROM = "xx@gmail.com";   // Replace with your "From" address. This address must be verified.
		    final String TO = "xx@gmail.com";  // Replace with a "To" address. If your account is still in the 
		                                                       // sandbox, this address must be verified.
		    
		    String BODY = body;
		    String SUBJECT = subject;
		    
		    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
// 		    final String SMTP_USERNAME = "AKIAIGZJIGMSKYHW2UCQ";  // Replace with your SMTP username.
// 		    final String SMTP_PASSWORD = "AgpZ38NRcByxClz/2k0StYjq0Dt7YFpgrwTOxtnDUQ0X";  // Replace with your SMTP password.
		    
		    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
		    final String HOST = "email-smtp.us-west-2.amazonaws.com";    
		    
		    // The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
		    // STARTTLS to encrypt the connection.
		    final int PORT = 25;
		    
		    
	        // Create a Properties object to contain connection configuration information.
	    	Properties props = System.getProperties();
	    	props.put("mail.transport.protocol", "smtps");
	    	props.put("mail.smtp.port", PORT); 
	    	
	    	// Set properties indicating that we want to use STARTTLS to encrypt the connection.
	    	// The SMTP session will begin on an unencrypted connection, and then the client
	        // will issue a STARTTLS command to upgrade to an encrypted connection.
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.starttls.enable", "true");
	    	props.put("mail.smtp.starttls.required", "true");

	        // Create a Session object to represent a mail session with the specified properties. 
	    	Session session = Session.getDefaultInstance(props);

	        // Create a message with the specified information. 
	        MimeMessage msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(FROM));
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
	        msg.setSubject(SUBJECT);
	        msg.setContent(BODY,"text/html");
	            
	        // Create a transport.        
	        Transport transport = session.getTransport();
	                    
	        // Send the message.
	        try
	        {
	            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
	            
	            // Connect to Amazon SES using the SMTP username and password you specified above.
	            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
	        	
	            // Send the email.
	            transport.sendMessage(msg, msg.getAllRecipients());
	            System.out.println("Email sent!");
	        }
	        catch (Exception ex) {
	            System.out.println("The email was not sent.");
	            System.out.println("Error message: " + ex.getMessage());
	        }
	        finally
	        {
	            // Close and terminate the connection.
	            transport.close();        	
	        }
		    
		    
			
			
		}
	
    	
		
		 public static void send(String sub,String msg){
			 
			 String from = "xx@gmail.com";
			 String password = "xx";
			 String to = "xx";
			 
			 
	          //Get properties object    
	          Properties props = new Properties();    
	          props.put("mail.smtp.host", "smtp.gmail.com");    
	          props.put("mail.smtp.socketFactory.port", "465");    
	          props.put("mail.smtp.socketFactory.class",    
	                    "javax.net.ssl.SSLSocketFactory");    
	          props.put("mail.smtp.auth", "true");    
	          props.put("mail.smtp.port", "465");    
	          //get Session   
	          Session session = Session.getDefaultInstance(props,    
	           new javax.mail.Authenticator() {    
	           protected PasswordAuthentication getPasswordAuthentication() {    
	           return new PasswordAuthentication(from,password);  
	           }    
	          });    
	          //compose message    
	          try {    
	           MimeMessage message = new MimeMessage(session);    
	           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
	           message.setSubject(sub);    
	           message.setText(msg);    
	           //send message  
	           Transport.send(message);    
	           System.out.println("message sent successfully");    
	          } catch (MessagingException e) {throw new RuntimeException(e);}    
	             
	    }  
		
    	public void sendFromGMail(String subject, String body) {
	    	String from = "xx";
			String pass = "xx"; 
			String[] to = {"xx@gmail.com"};			
									
			
	        Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", from);
	        props.put("mail.smtp.password", pass);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");
	
	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setContent(body,"text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }

  }
}




