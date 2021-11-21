package blackboard.util;

public class Courses {

	  private String uniqueID;
	  private String parentID;
	  private String lcTypeID;
	  private String courseTitle;
	  private String sectionTitle;
	  private String longDesc;
	  private String shortDesc;
	  private String startDate;
	  private String endDate;
	  private String user;
	  private String employeeID;
	  private String term;
	  private String campCode;
	  
	  public String getCampCode() {
		return campCode;
	}
	public void setCampCode(String campCode) {
		this.campCode = campCode;
	}
	public void setUser(String s){
		  this.user=s;
	  }
      public String getUser(){
		  return user;
	  }
	  public void setUniqueID(String s){
		  this.uniqueID=s;
	  }
	  public String getUniqueID(){
		  return uniqueID;
	  }
	  public void setEmployeeID(String s){
		  this.employeeID=s;
	  }
	  public String getEmployeeID(){
		  return employeeID;
	  }
	  public void setParentID(String s){
		  this.parentID=s;
	  }
	  public String getParentID(){
		  return parentID;
	  }
	  public void setlcTypeID(String s){
		  this.lcTypeID=s;
	  }
	  public String getlcTypeID(){
		  return lcTypeID;
	  }
	  public void setCourseTitle(String s){
		  this.courseTitle=s;
	  }
	  public String getCourseTitle(){
		  return courseTitle;
	  }
	  public void setSectionTitle(String s){
		  this.sectionTitle=s;
	  }
	  public String getSectionTitle(){
		  return sectionTitle;
	  }
	  public void setLongDesc(String s){
		  this.longDesc=s;
	  }
	  public String getLongDesc(){
		  return longDesc;
	  }
	  public void setShortDesc(String s){
		  this.shortDesc=s;
	  }
	  public String getShortDesc(){
		  return shortDesc;
	  }
	  public void setStartDate(String s){
		  this.startDate=s;
	  }
	  public String getStartDate(){
		  return startDate;
	  }
	  public void setEndDate(String s){
		  this.endDate=s;
	  }
	  public String getEndDate(){
		  return endDate;
	  }
	  public void setTerm(String s){
		  this.term=s;
	  }
	  public String getTerm(){
		  return term;
	  }
}
