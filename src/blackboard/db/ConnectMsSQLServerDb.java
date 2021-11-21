package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import blackboard.util.Courses;

public class ConnectMsSQLServerDb {

	private Connection conn;
	private String db_connect_string;
	private String term;
	private String sectionTitle;
	
	public ConnectMsSQLServerDb(String db_connect_string){
	  this.db_connect_string = db_connect_string;	
	}
	public void setTerm(String term){
	  this.term=term;	
	}
	public void setSectionTitle(String sectTitle){
		this.sectionTitle=sectTitle;
	}
	public void dbConnect(String db_userid, String db_password)
    {
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
            System.out.println("connected");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	public ArrayList acquireCourseInformation (){
	  ArrayList list = new ArrayList();
	  String sqlstmt = "select request_username, request_department, request_webct_id, request_course_code, request_course_title, " +
	                   "request_semester, request_status, request_call_status, request_operator from BLACKBOARDREQUESTS where " +
	                   "request_call_status = ? or request_call_status = ?";
	  
	  try {
		  
	      
		  PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		  prepStmt.setString(1, "Open");
		  prepStmt.setString(2, "In Progress");
		  
	      ResultSet rs = prepStmt.executeQuery();
	      
	      while (rs.next()){
	         Courses courses = new Courses();
	         
	         if (rs.getString(6).equals("Semester 1")){
		    	  term = "200910";
		     } else if (rs.getString(6).equals("Semester 2")){
		    	 term = "200920";
		     }else if (rs.getString(6).equals("Semester 3")){
		    	 term = "200930";
		     }
	         
	         courses.setUniqueID(rs.getString(4).trim()+"."+term);
	         courses.setParentID(rs.getString(4).trim());
	         courses.setSectionTitle("("+"2009 - 2010, " + rs.getString(6)+")");
	         courses.setCourseTitle(rs.getString(4).replaceAll(" ", "").trim()+ " - " + rs.getString(5));
	         courses.setLongDesc(rs.getString(5));
	         courses.setUser(rs.getString(1));
	         courses.setEmployeeID(rs.getString(3));
	         
	         list.add(courses);
	         
	       
	      }
	      
	      rs.close();
	      prepStmt.close();
	}
	catch(SQLException e){
		  e.printStackTrace();
	}
	
	  
	  
	  return list;
	}
	
	public static void main(String[] args) 
	{
		  ConnectMsSQLServerDb db = new ConnectMsSQLServerDb("jdbc:jtds:sqlserver://sqlsrvtemp1:1433/SHD-test");
	      db.dbConnect("shduser","shduser");
	}
}
