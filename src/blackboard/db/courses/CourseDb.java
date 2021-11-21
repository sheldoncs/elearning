package blackboard.db.courses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import blackboard.db.MySQLConnection;
import blackboard.util.Courses;

public class CourseDb extends MySQLConnection {

	
	public CourseDb(){
		super();
	}
    public int getNextNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM COURSES";
		
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
    public void insertCourseFileNumber(String fileNumber){
    	
    	deleteCourseFileNumber();
    	
    	String sqlstmt = "insert into lcsfile (id,lcs ) values ( ? , ?)";
	    
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

    public void insertParent(Courses courses){
    	String sqlstmt = "insert into COURSES (id,unique_id,parent_id,lc_type, title,long_desc ) values ( ? , ? , ?, ?, ?, ?)";
	    
		 try {
       if (conn == null){
       	System.out.println("Null Connection");
       }
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setInt(1, getNextNumber());
		prepStmt.setString(2, courses.getParentID().replaceAll(" ", "").toUpperCase());
		prepStmt.setString(3, "X");
		prepStmt.setString(4, "C");
		prepStmt.setString(5, courses.getCourseTitle());
		prepStmt.setString(6, courses.getLongDesc());
		
		prepStmt.execute("use blackboard");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
   } 
   public void insertSections(Courses courses){
    	String sqlstmt = "insert into COURSES (id,unique_id,parent_id,lc_type, title,long_desc ) values ( ? , ? , ?, ?, ?, ?)";
    	    
		 try {
        if (conn == null){
        	System.out.println("Null Connection");
        }
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setInt(1, getNextNumber());
		prepStmt.setString(2, courses.getUniqueID().replaceAll(" ", "").toUpperCase());
		prepStmt.setString(3, courses.getParentID().replaceAll(" ", "").toUpperCase());
		prepStmt.setString(4, "S");
		prepStmt.setString(5, courses.getSectionTitle());
		prepStmt.setString(6, "Lecturer : "+courses.getUser());
		
		prepStmt.execute("use blackboard");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
    }
   
    public ArrayList selectCourses() throws SQLException{
		
    	ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT UNIQUE_ID, PARENT_ID,LC_TYPE,TITLE,LONG_DESC FROM COURSES WHERE LC_TYPE = ?";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		prepStmt.setString(1, "C");
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			Courses courses = new Courses();
			courses.setUniqueID(rs.getString(1));
			courses.setParentID(rs.getString(2));
			courses.setlcTypeID(rs.getString(3));
			courses.setCourseTitle(rs.getString(4));
			courses.setLongDesc(rs.getString(5));
			a.add(courses);
		}
		
		return a;
		
    }
    public ArrayList selectSections() throws SQLException{
		
    	ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT UNIQUE_ID, PARENT_ID,LC_TYPE,TITLE,LONG_DESC FROM COURSES WHERE LC_TYPE = ?";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		prepStmt.setString(1, "S");
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			Courses courses = new Courses();
			courses.setUniqueID(rs.getString(1));
			courses.setParentID(rs.getString(2));
			courses.setlcTypeID(rs.getString(3));
			courses.setSectionTitle(rs.getString(4));
			courses.setLongDesc(rs.getString(5));
			a.add(courses); 
	    }
		
		return a;
    }
    private void deleteCourseFileNumber(){
        String sqlstmt = "delete from lcsfile";
	    
		try {
                 PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		} catch (SQLException ex){
			     ex.printStackTrace();
		}
    }
    public String selectCourseFileNumber() throws SQLException{
		
    	String file = "";
    	
		String sqlStmt="SELECT lcs FROM lcsfile";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			file = "lcs"+"_"+rs.getString(1);
		}
		
		return file;
		
    }
    public void deleteCourses(){
        String sqlstmt = "delete from courses";
	    
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
