package transfer.email;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator
{

    public PasswordAuthentication getPasswordAuthentication()
    {
    	String username = "ePayments@cavehill.uwi.edu";
        String password = "@HeWild1024";
        
        return new PasswordAuthentication(username, password);
    }
}