package transfer.email;


import java.text.DecimalFormat;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import blackboard.util.DateFormatter;



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
	private String status; 
	private float balance;
	private String orderID;
	private boolean isBalanceLetter;
	
	public SendMail(String to, String subject){
		
		this.to = to;
		this.subject = subject;
		
	}
	
	public void setOrderID(String orderID){
		this.orderID=orderID;
	}
	public void send(String Id, String firstname, String lastname){
		//205.214.197.101
		Authenticator auth = new SMTPAuthenticator();
		
		Properties props = new Properties();
		//props.put("mail.smtp.host", "webservermail.cavehill.uwi.edu");
		//props.put("mail.smtp.port", "25");
		//props.put("mail.smtp.auth", "false");

		Session mailSession = Session.getDefaultInstance(props,auth);
		Message simpleMessage = new MimeMessage(mailSession);
		
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		
		try {
			fromAddress = new InternetAddress("studentaccounts@cavehill.uwi.edu");
			//fromAddress = new InternetAddress("sheldon.spencer@cavehill.uwi.edu");
			toAddress = new InternetAddress(to);
			
			
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			
			simpleMessage.setSubject("Financial Hold On Elearning Account");
			
			 if (this.getStatus().equals("c")){
			   simpleMessage.setContent(returnSuspensionLetter(Id,firstname,lastname), "text/html");  
			 } else if (this.getStatus().equals("r")){
				 simpleMessage.setContent( returnRenistatementLetter(Id,firstname,lastname), "text/html");  
			 } if (this.getStatus().equals("b")){
				 simpleMessage.setContent("Moodle Server Crash", "text/html");
			 }
			
			Transport.send(simpleMessage);			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void setIsBalanceLetter(boolean b){
	   	isBalanceLetter = b;
	}
	
	public void setStudentID(String string){
		this.studentID=string;
	}
	public void setBalance (float balance){
		this.balance=balance;
		
	               
	}
	public void setStatus(String s){
		status = s;
	}
	private String getStatus (){
		return status;
	}
	
	public void testEmail(){
	
	}
	public String returnSuspensionLetter(String Id, String firstname, String lastname){
		DateFormatter f = new DateFormatter();
		
		String msg = "";
		msg = "<center><b>Cave Hill - Campus</center></b><br/><br/>";
		msg = msg+"<b>From</b>:studentaccounts@cavehill.uwi.edu <br>";
		msg = msg + "<b>To</b>:" +firstname+ "."+lastname +"@mycavehill.uwi.edu<br/>";
		msg = msg + "<br>";
	    msg = msg + "Dear "+ firstname.toLowerCase() + " " +lastname.toLowerCase() +",";
	    msg = msg + "You have a Financial Hold for non-payment of fees. As a result, your access to e-learning and some CHOL online<br/><br/>";
	    msg = msg + "services has been temporarily suspended.<br/><br/>";
	    msg = msg + "The Bursary (Student Accounts)<br/>";
	    msg = msg + "The University of the West Indies<br/>";
	    msg = msg + "Cave Hill Campus<br/>";
	    msg = msg + "Barbados<br><br/>";
	    
	 
	   
	    
	    return msg;
	}

	public String returnRenistatementLetter(String Id, String firstname, String lastname){
		DateFormatter f = new DateFormatter();
		DecimalFormat formatter = new DecimalFormat("0.00");
		String str = "";
		
		String msg = "";
		msg = "<b><center>Cave Hill - Campus</center></b><br><br>";
		msg = msg+"<b>From</b>:studentaccounts@cavehill.uwi.edu <br>";
		msg = msg + "<b>To</b>:" +firstname+ "."+lastname +"@mycavehill.uwi.edu<br/>";
		msg = msg + "<br>";
	    msg = msg + "Dear "+ firstname + " " +lastname +",";
	    msg = msg + "Your account is now in good standing and your access to eLearning CHOL services has been reactivated.<br/>Thank you for your payment.<br/><br/>";
	    msg = msg + "<br><br>";
	    msg = msg + "The Bursary (Student Accounts)<br/>";
	    msg = msg + "The University of the West Indies<br/>";
	    msg = msg + "Cave Hill Campus<br/>";
	    msg = msg + "Barbados<br><br/>";
	    
	   
	   
	    
	    return msg;
	}
	
	 public static void main(String[] args) {
	 
	/*	 
     TransactionForm frm = new TransactionForm();	 
     
     frm.setCardName("Sheldon C Spencer");
	 frm.setCardAmount("100.00");
	 */	 
	 SendMail sm = new SendMail(
				 "sheldon.spencer@gmail.com", "test email");
	 	 
	 sm.setStatus("c");	 
	 sm.send("20003569","Sheldon","Spencer");
     
		 
	 }
}
