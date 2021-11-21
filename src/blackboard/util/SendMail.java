package blackboard.util;


import java.text.DecimalFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMail {

	private String from;
	private String to;
	private String subject;
	private String text;
	private String studentID;
	private String amount;
	private String cardName;
	private String addr1;
	private String addr2;
	private String cardCity;
	private String cardState;;
	private String msg;
	private float balance;
	private String orderID;
	private String filename;
	
	public SendMail(String from, String to, String subject){
		this.from = from;
		this.to = to;
		this.subject = subject;
		
	}
	public void setFileName(String filename){
		this.filename=filename;
	}
	
	public void setOrderID(String orderID){
		this.orderID=orderID;
	}
	public void send(){
		//205.214.197.101
		Authenticator auth = new SMTPAuthenticator();
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "205.214.197.101");
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", "true");

		Session mailSession = Session.getDefaultInstance(props,auth);
		Message simpleMessage = new MimeMessage(mailSession);
		
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		InternetAddress ccAddress = null;
		
		try {
			fromAddress = new InternetAddress(from);
			
			toAddress = new InternetAddress(to);
			ccAddress = new InternetAddress("sheldon.spencer@cavehill.uwi.edu");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			
			
			/*New Code Inserted*/
			simpleMessage.setSubject(subject);
			//simpleMessage.setText(text);
			
			MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(getMsgText());
			simpleMessage.setContent(getMsgText(), "text/html");

			//MimeBodyPart attachmentPart = new MimeBodyPart();
            //FileDataSource fileDataSource = new FileDataSource(filename) {
                
              //  public String getContentType() {
                //    return "application/octet-stream";
                //}
            //};
			
            //attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            //attachmentPart.setFileName(filename);
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            //multipart.addBodyPart(attachmentPart);
            
            simpleMessage.setContent(multipart);
            /*New Code Inserted*/
           
			
			Transport.send(simpleMessage);			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public String getMsgText(){
		String msg = "Students with outstanding fees will be unable to access Blackboard. " +
                     "Access will be reinstated within 48 hours of payment. "; 
		msg = "Your Blackboard Account has been reinstated. Your PASSWORD is in the format FL@ddmmyy and your USERNAME is you student ID.";
        
		return msg;
	}
	public void setStudentID(String string){
		this.studentID=string;
	}
	public void setBalance (float balance){
		this.balance=balance;
		
	               
	}
	

	 public static void main(String[] args) {
	 
		 
		 
	 
     
      	 
	 SendMail sm = new SendMail("epayments@cavehill.uwi.edu", 
				 "sheldon.spencer@gmail.com", "test email");
	 
	 
		 
		 
	 sm.send();
     
		 
//		 String url = "4000";
//		 System.out.println(url.indexOf("$"));
//		 if (url.indexOf("$") >0){
		   
		  // System.out.println(url);
		 }
		 //if (v.equals("https")){
		   //	 url = url.replaceAll("http", "https");
		   //String rightString = url.substring(url.toString().indexOf(":"), url.length());
		   //String concat = "https"+rightString;
		   
		   //System.out.println(concat);
		 //}
	     //System.out.println(url);
	 }

