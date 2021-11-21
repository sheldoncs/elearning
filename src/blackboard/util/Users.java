package blackboard.util;

public class Users {

	private String uniqueID;
	private String loginID;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	
	public void setUniqueID(String s){
		this.uniqueID=s;
	}
	public String getUniqueID(){
		return uniqueID;
	}
	public void setLoginID(String s){
		this.loginID=s;
	}
	public String getLoginID(){
		return loginID;
	}
	public void setFirstname(String s){
		this.firstname=s;
	}
	public String getFirstname(){
		return firstname;
	}
	public void setLastname(String s){
		this.lastname=s;
	}
	public String getLastname(){
		return lastname;
	}
	public void setEmail(String s){
		this.email=s;
	}
	public String getEmail(){
		return email;
	}
	public void setPassword(String s){
		this.password=s;
	}
	public String getPassword(){
		return password;
	}
}
