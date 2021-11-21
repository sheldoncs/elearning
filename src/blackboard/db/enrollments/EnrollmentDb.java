package blackboard.db.enrollments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import blackboard.db.MySQLConnection;
import blackboard.util.Courses;
import blackboard.util.Enrollments;

public class EnrollmentDb extends MySQLConnection {

	
	public EnrollmentDb(){
		super();
	}
    public int getNextNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM ENROLLMENTS";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		 
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			found=true;
			cnt = rs.getInt(1);
			
		}
		if (!found)
	      cnt=0;		 
	 
		cnt++;
		
		return cnt;
	}
    public int getNextXMLEnrollNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM blackboardenrollments";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		 
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			found=true;
			cnt = rs.getInt(1);
			
		}
		if (!found)
	      cnt=0;		 
	 
		cnt++;
		
		return cnt;
	}
    public String selectEnrollmentFileNumber() throws SQLException{
		
    	String file = "";
    	
		String sqlStmt="SELECT enrollment FROM enrollmentfile";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			file = "enrollments"+"_"+rs.getString(1);
		}
		
		return file;
		
    }
    public void insertEnrollmentFileNumber(String fileNumber){
    	
    	deleteEnrollmentFileNumber();
    	
    	String sqlstmt = "insert into enrollmentfile (id,enrollment) values ( ? , ?)";
	    
		 try {
      if (conn == null){
      	System.out.println("Null Connection");
      }
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setInt(1, 1);
		prepStmt.setString(2, fileNumber);
		
		prepStmt.execute("use blackboard");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
    }
    public void insertEnrollments(Courses courses, int role, int status, String uniqueID){
    	String sqlstmt = "insert into ENROLLMENTS (id, user_id, login_id, section_id, role, status) values ( ? , ? , ?, ?, ?, ?)";
	    
		try {
          
			 //if (courses.getEmployeeID().length()>0){
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		        
		         prepStmt.setInt(1, getNextNumber());
		         prepStmt.setString(2, uniqueID);
		         prepStmt.setString(3, courses.getEmployeeID());
		         prepStmt.setString(4, courses.getUniqueID().replaceAll(" ", ""));
		         prepStmt.setInt(5, role);
		         prepStmt.setInt(6, status);
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		         insertAdminToEnrollment(courses,role,status);
		         //}
		   
		} catch (SQLException ex){
			     ex.printStackTrace();
		}
	    
   } 
  
   private void insertAdminToEnrollment(Courses courses,int role, int status){
	   String sqlstmt = "insert into ENROLLMENTS (id, user_id, login_id, section_id, role, status) values ( ? , ? , ?, ?, ?, ?)";
	    
		try {
         
			     PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		        
		         prepStmt.setInt(1, getNextNumber());
		         prepStmt.setString(2, "URN:X-WEBCT-VISTA-V1:83e4dafe-7f00-0001-01e2-d46149c2e775");
		         prepStmt.setString(3, "cadmin");
		         prepStmt.setString(4, courses.getUniqueID().replaceAll(" ", ""));
		         prepStmt.setInt(5, role);
		         prepStmt.setInt(6, status);
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		        
		   
		} catch (SQLException ex){
			     ex.printStackTrace();
		}
   }
   public void insertEnrollmentsFromXML(Enrollments enrollments){
	   
	   String sqlstmt = "insert into blackboardenrollments (id, user_id, login_id, section_id, role, status) values ( ? , ? , ?, ?, ?, ?)";
	   try {
	   
	     PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	        
         prepStmt.setInt(1, getNextXMLEnrollNumber());
         prepStmt.setString(2, enrollments.getUserID());
         prepStmt.setString(3, enrollments.getLoginID());
         prepStmt.setString(4, enrollments.getSectionID());
         prepStmt.setString(5, enrollments.getRole());
         prepStmt.setString(6, enrollments.getStatus());
         
     	 prepStmt.execute("use blackboard");

         prepStmt.executeUpdate();
         prepStmt.close();
	     
	   } catch (SQLException ex){
		     ex.printStackTrace();
	   }
   }
   public ArrayList selectEnrollments(String role, String id, String courseid) throws SQLException{
		
    	ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT USER_ID, LOGIN_ID, SECTION_ID,ROLE, STATUS FROM ENROLLMENTS WHERE USER_ID = ? AND ROLE = ? AND SECTION_ID = ?";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		prepStmt.setString(1, id);
		prepStmt.setString(2, role);
		prepStmt.setString(3, courseid);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			Enrollments enrol = new Enrollments();
			enrol.setUserID(rs.getString(1));
			enrol.setLoginID(rs.getString(2));
			enrol.setSectionID(rs.getString(3));
			enrol.setRole(rs.getString(4));
			enrol.setStatus(rs.getString(5));
			a.add(enrol);
		}
		
		return a;
		
   }
   public ArrayList selectEnrollments() throws SQLException {
		
    	ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT USER_ID, LOGIN_ID, SECTION_ID,ROLE, STATUS FROM ENROLLMENTS";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			Enrollments enrol = new Enrollments();
			enrol.setUserID(rs.getString(1));
			enrol.setLoginID(rs.getString(2));
			enrol.setSectionID(rs.getString(3));
			enrol.setRole(rs.getString(4));
			enrol.setStatus(rs.getString(5));
			a.add(enrol);
		}
		
		return a;
		
    }
    private void deleteEnrollmentFileNumber(){
    String sqlstmt = "delete from enrollmentfile";
    
	    try {
             PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	         
         	 prepStmt.execute("use blackboard");
	
	         prepStmt.executeUpdate();
	         prepStmt.close();
	   
     	} catch (SQLException ex){
		     ex.printStackTrace();
	    }
    }
    public void deleteEnrollments(){
        String sqlstmt = "delete from enrollments";
	    
		try {
                 PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		} catch (SQLException ex){
			     ex.printStackTrace();
		}
    }
    public void closeDb(){
    	try {
    	  conn.close();
    	} catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
    }
    
}
