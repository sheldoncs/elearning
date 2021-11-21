package blackboard.util;

public class Lecturers {
   
	private String lecturers;
	private String course;
	private String id;
	private String firstname;
	private String lastname;
	private String loginId;
	
	public void setId(String s){
	   this.id=s;
	}
	public String getId(){
	   return id;
	}
	public void setLecturers(String s){
	   this.lecturers=s;
    }
    public String getlecturers(){
	   return lecturers;
    }
    public void setCourse(String s){
 	   this.course=s;
    }
    public String getCourse(){
 	   return course;
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
    public void setLoginId(String s){
        this.loginId=s;	 
   }
   public String getLoginId(){
     	 return loginId;
   }
}
