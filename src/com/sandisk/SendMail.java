package com.sandisk;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {

    	
		
		 public static void send(String sub,String msg){
			 
			 String from = "@gmail.com";
			 String password = "pwd";
			 String to = "@gmail.com";
			 
			 
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
		
    	public void sendFromGMail(String subject, String body, String who) {
	    	String from = "from";
			String pass = "pwd"; 
			String[] to = {"@gmail.com", "@gmail.com"};			
									
			
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
            message.setFrom(new InternetAddress(from, who));
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
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Something wrong with InternetAddress");
		}

  }
}




