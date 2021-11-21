package moodle.automate.user;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import moodle.automate.course.ExcelCourse;

public class ExcelUser {
	
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String auth;
	private int suspended;
    private String password;
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getSuspended() {
		return suspended;
	}

	public void setSuspended(int suspended) {
		this.suspended = suspended;
	}
	public static void usingDataOutputStream(ArrayList<ExcelUser> list) throws IOException 
	{
		
		String fileHeader="username,password,firstname,lastname,auth,email,suspended";
//		String fileHeader="username,firstname,lastname,auth,email,suspended";
		String dataString = "";
	    
	   
	    
	    FileWriter fileWriter = new FileWriter("c:/temp/upload/user.csv");
	    PrintWriter printWriter = new PrintWriter(fileWriter,false);
	    printWriter.println(fileHeader);
	   
	    Iterator<ExcelUser> ii = list.iterator();
	    while (ii.hasNext()) {
	    	ExcelUser ec = ii.next();
	    	dataString=ec.getUsername().toUpperCase()+","+ec.getPassword()+","+ec.getFirstname()+","+ec.getLastname()+","+"ldap"+","+ec.getEmail()+","+"0";
//	    	dataString=ec.getUsername()+","+ec.getFirstname()+","+ec.getLastname()+","+"ldap"+","+ec.getEmail()+","+"0";
	    	printWriter.println(dataString);
	    }
	    printWriter.close();
	}
	

}
