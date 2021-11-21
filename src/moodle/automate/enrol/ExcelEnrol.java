package moodle.automate.enrol;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ExcelEnrol {

	private String action;
	private String courseId;
	private String role;
	private String timeend;
	private String timestart;
	private String username;
	private String firstname;
	private String lastname;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	private CourseRole courseRole;
	private ArrayList<CourseRole> alist = new ArrayList<CourseRole>();

	public void addCourseRole(CourseRole courseRole) {
		alist.add(courseRole);
	}

	public ArrayList<CourseRole> getCourseRole() {
		return alist;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	public String getTimestart() {
		return timestart;
	}

	public void setTimestart(String timestart) {
		this.timestart = timestart;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public static void usingDataOutputStream(int hdrCnt, HashMap<String, ExcelEnrol> hmap) throws IOException 
	{
	    String fileHeader = "username,firstname,lastname,email,auth";
		String dataString = "";
	    for (int i = 1; i <= hdrCnt; i++) {
			fileHeader=fileHeader+","+"course"+i +","+"role"+i;
		}
	   
	    
	    FileWriter fileWriter = new FileWriter("c:/temp/upload/enrollment.csv");
	    PrintWriter printWriter = new PrintWriter(fileWriter,false);
	    printWriter.println(fileHeader);
	    
	    
	    Set<String> keys =hmap.keySet();
	    Iterator<String> ii = keys.iterator();
	    int cnt=0;
	    while(ii.hasNext()) {
	    	String key = (String)ii.next();
	    	ExcelEnrol excelEnrol = hmap.get(key);
	    	dataString = excelEnrol.username+","+excelEnrol.firstname+","+excelEnrol.lastname+","+excelEnrol.email+","+"ldap";
	    	ArrayList<CourseRole> list = excelEnrol.getCourseRole();
	    	Iterator<CourseRole> crii = list.iterator();
	    	while (crii.hasNext()) {
	    		cnt++;
	    		CourseRole cr = (CourseRole)crii.next();
	    		dataString=dataString+","+cr.getCourse()+","+cr.getRole();
	    	}
	    	if (cnt<hdrCnt) {
	    		int diff = hdrCnt-cnt;
	    		for (int j=0; j <diff; j++) {
	    			dataString=dataString+","+",";
	    		}
	    	}
	    	cnt=0;
	    	printWriter.println(dataString);
	    }
	    System.out.println("Orientation Enrollment Output Ending....");
	    printWriter.close();
		  
	}

}
