package blackboard.util;

public class UserEnrol {
    
	private String city;
	private String country;
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String auth;
	private String action;
	private String userType;
	private int suspend;
	
	public int getSuspend() {
		return suspend;
	}
	public void setSuspend(int suspend) {
		this.suspend = suspend;
	}
	public void setCity(String s){
		this.city = s;
	}
	public String getCity(){
		return city;
	}
	public void setCountry(String s){
		this.country = s;
	}
	public String getCountry(){
		return country;
	}
	public void setId(String s){
		this.id = s;
	}
	public String getId(){
		return id;
	}
	public void setEmail(String s){
		this.email = s;
	}
	public String getEmail(){
		return email;
	}
	public void setFirstName(String s){
		this.firstName = s;
	}
	public String getFirstName(){
		return firstName;
	}
	public void setLastName(String s){
		this.lastName = s;
	}
	public String getLastName(){
		return lastName;
	}
	public void setPassword(String s){
		this.password = s;
	}
	public String getPassword(){
		return password;
	}
	public void setAuth(String s){
		this.auth = s;
	}
	public String getAuth(){
		return auth;
	}
	public void setAction(String s){
		this.action = s;
	}
	public String getAction(){
		return action;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
