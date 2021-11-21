package blackboard.courses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blackboard.db.MySQLConnection;
import blackboard.util.Courses;

public class BlackboardCourses extends MySQLConnection{
  
	public BlackboardCourses(){
		super();
	}
	public void insertCurrentCourses(Courses courses){
		
		String sqlstmt = "insert into blackboardcoursesection (uniqueid,parentid,lctype,title,description) values (?,?,?,?,?)";
	    
		   try {
          
       
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	
		         prepStmt.setString(1, courses.getUniqueID());
		         prepStmt.setString(2, courses.getParentID());
		         prepStmt.setString(3, courses.getlcTypeID());
		         prepStmt.setString(4, courses.getShortDesc());
		         prepStmt.setString(5, courses.getLongDesc());
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		      } catch (SQLException ex){
			     ex.printStackTrace();
		      }
	   
		
	}
	public boolean selectCourses(String courseid) throws SQLException{
		boolean found = false;
		
          String sqlStmt="SELECT courseid FROM BLACKBOARDCOURSES WHERE courseid = ?";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		 
		prepStmt.setString(1, courseid);
		 
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			found=true;	
		}
		
		return found;
	}
    public int getNextNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM BLACKBOARDCOURSES";
		
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
    public void closeDb(){
    	try {
    	  conn.close();
    	} catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
    }
}
