package blackboard.util;

public class Enrollments {

	private String userID;
	private String loginID;
	private String sectionID;
	private String role;
	private String status;
	
	public void setUserID(String s){
		this.userID=s;
	}
	public String getUserID(){
		return userID;
	}
	public void setLoginID(String s){
		this.loginID=s;
	}
	public String getLoginID(){
		return loginID;
	}
	public void setSectionID(String s){
		this.sectionID=s;
	}
	public String getSectionID(){
		return sectionID;
	}
	public void setRole(String s){
		this.role=s;
	}
	public String getRole(){
		return role;
	}
	public void setStatus(String s){
		this.status=s;
	}
	public String getStatus(){
		return status;
	}
}
