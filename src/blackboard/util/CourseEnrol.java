package blackboard.util;

public class CourseEnrol {

	private String spridenId;
	private String subjCode;
	private String crseCode;
	private String crn;
	
	public void setId(String s){
		spridenId=s;
	}
    public String getSpridenId(){
	   return spridenId;	
	}
    public void setSubjCode(String s){
		subjCode=s;
	}
    public String getSubjCode(){
		return subjCode;
	}
    public void setCrseCode(String s){
 	   crseCode = s;	
 	}
    public String getCrseCode(){
	   return crseCode;	
	}
    public void setCrn(String s){
  	   crn = s;	
  	}
     public String getCrn(){
 	   return crn;	
 	}
}
