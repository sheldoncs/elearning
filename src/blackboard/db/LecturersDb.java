package blackboard.db;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import blackboard.util.DateFormatter;
import blackboard.util.Lecturers;

public class LecturersDb extends OracleDBConnect {

	private String lecturer;
	
	public LecturersDb (){
		
		super();
		
	}
	public Lecturers getNames(String id){
		Lecturers lecturer = new Lecturers();
		
		String sqlstmt = "SELECT SPRIDEN.SPRIDEN_ID, SPRIDEN.SPRIDEN_LAST_NAME, SPRIDEN.SPRIDEN_FIRST_NAME " +
        "FROM SPRIDEN " +
        "WHERE (SPRIDEN.SPRIDEN_ID = ?)";
		
		try {
			  
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			prepStmt.setString(1, id);
			
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()){
		       
		       lecturer.setId(rs.getString(1));
		       lecturer.setFirstname(rs.getString(3));
		       lecturer.setLastname(rs.getString(2));
		      
		    }
		}
		catch(SQLException e){
				  e.printStackTrace();
		}
		
		return lecturer;
		
	}
	public ArrayList getLecturerAndCourse(){
		ArrayList l = new ArrayList();
		
		String sqlstmt = "SELECT DISTINCT SIRASGN.SIRASGN_TERM_CODE, SPRIDEN.SPRIDEN_ID, SPRIDEN.SPRIDEN_LAST_NAME, SPRIDEN.SPRIDEN_FIRST_NAME, " +
		                 "SPRIDEN.SPRIDEN_MI, SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB, SIRASGN.SIRASGN_PIDM " +
		                 "FROM (SIRASGN INNER JOIN SPRIDEN ON SIRASGN.SIRASGN_PIDM = SPRIDEN.SPRIDEN_PIDM) " +
		                 "INNER JOIN (SSBSECT INNER JOIN SCBCRSE ON (SSBSECT.SSBSECT_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB) " +
		                 "AND (SSBSECT.SSBSECT_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE)) ON (SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN) " +
		                 "AND (SIRASGN.SIRASGN_TERM_CODE = SSBSECT.SSBSECT_TERM_CODE) " +
		                 "WHERE (((SIRASGN.SIRASGN_TERM_CODE)=?) AND (SPRIDEN.SPRIDEN_CHANGE_IND) Is Null)";
		
		try {
		  
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			prepStmt.setString(1, getCurrentTerm());
			
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()){
		       Lecturers lecturer = new Lecturers();
		       lecturer.setId(rs.getString(2));
		       
		       if (rs.getString(5) != null){
		        lecturer.setLecturers(rs.getString(4) + " " +rs.getString(5) + " " +rs.getString(3));
		       } else {
		    	   lecturer.setLecturers(rs.getString(4) + " " +rs.getString(3));
		       }
		       lecturer.setCourse(rs.getString(6)+rs.getString(7));
		       lecturer.setFirstname(rs.getString(4));
		       lecturer.setLastname(rs.getString(3));
		       l.add(lecturer);
		    }
		}
		catch(SQLException e){
				  e.printStackTrace();
		}
		
		return l;
	}
	public String findLecturer(String firstname, String lastname){

		String spridenid="";
		
		String sqlstmt = "SELECT DISTINCT SIRASGN.SIRASGN_TERM_CODE, SPRIDEN.SPRIDEN_ID, SPRIDEN.SPRIDEN_LAST_NAME, SPRIDEN.SPRIDEN_FIRST_NAME, " +
        "SPRIDEN.SPRIDEN_MI, SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB, SIRASGN.SIRASGN_PIDM " +
        "FROM (SIRASGN INNER JOIN SPRIDEN ON SIRASGN.SIRASGN_PIDM = SPRIDEN.SPRIDEN_PIDM) " +
        "INNER JOIN (SSBSECT INNER JOIN SCBCRSE ON (SSBSECT.SSBSECT_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB) " +
        "AND (SSBSECT.SSBSECT_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE)) ON (SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN) " +
        "AND (SIRASGN.SIRASGN_TERM_CODE = SSBSECT.SSBSECT_TERM_CODE) " +
        "WHERE (((SIRASGN.SIRASGN_TERM_CODE)=?) AND (SPRIDEN.SPRIDEN_CHANGE_IND) Is Null AND ((SPRIDEN.SPRIDEN_FIRST_NAME)=?) AND " +
        " ((SPRIDEN.SPRIDEN_LAST_NAME)=?))";
		
		try {
			  
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			prepStmt.setString(1, getCurrentTerm());
			prepStmt.setString(2, firstname);
			prepStmt.setString(3, lastname);
			
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()){
		       spridenid = rs.getString(2);
		    }
			prepStmt.close();
		}
		catch(SQLException e){
				  e.printStackTrace();
		}
		return spridenid;
	}
	public boolean lecturerAvailable(String id){
		
		boolean available = false;
		
		String sqlstmt = "SELECT DISTINCT SIRASGN.SIRASGN_TERM_CODE, SPRIDEN.SPRIDEN_ID, SPRIDEN.SPRIDEN_LAST_NAME, SPRIDEN.SPRIDEN_FIRST_NAME, " +
        "SPRIDEN.SPRIDEN_MI, SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB, SIRASGN.SIRASGN_PIDM " +
        "FROM (SIRASGN INNER JOIN SPRIDEN ON SIRASGN.SIRASGN_PIDM = SPRIDEN.SPRIDEN_PIDM) " +
        "INNER JOIN (SSBSECT INNER JOIN SCBCRSE ON (SSBSECT.SSBSECT_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB) " +
        "AND (SSBSECT.SSBSECT_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE)) ON (SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN) " +
        "AND (SIRASGN.SIRASGN_TERM_CODE = SSBSECT.SSBSECT_TERM_CODE) " +
        "WHERE (((SIRASGN.SIRASGN_TERM_CODE)=?) AND (SPRIDEN.SPRIDEN_CHANGE_IND) Is Null AND SPRIDEN.SPRIDEN_ID = ?)";
		
		try {
			  
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			prepStmt.setString(1, getCurrentTerm());
			prepStmt.setString(2, id);
			
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()){
		       available = true;
		       lecturer = rs.getString(4)+" "+rs.getString(3);
		    }
			prepStmt.close();
		}
		catch(SQLException e){
				  e.printStackTrace();
		}
		
		return available;
	}
	public String getLecturer(){
		return lecturer;
	}
	public String getCurrentTerm(){
		 String term = null;
			
			String sqlstmt = "select max(stvterm_code) as maxtermcode from stvterm where stvterm_start_date <= ? and stvterm_end_date >= ?";
			DateFormatter df = new DateFormatter();
			
			try {
				System.out.println(df.getSimpleDate());
				  PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
				  prepStmt.setDate(1, java.sql.Date.valueOf((df.getSimpleDate())));
			      prepStmt.setDate(2, java.sql.Date.valueOf(df.getSimpleDate()));
			     
			      ResultSet rs = prepStmt.executeQuery();
			      while (rs.next()){
			    	  System.out.println(rs.getString(1));
			    	  term = rs.getString(1);
			      }
			      if (term != null){
			        if (term.indexOf("40")>=0){
			    	  term=null;
			        }
			      }
			      
			      if (term == null){
						sqlstmt = "select min(stvterm_code) as maxtermcode from stvterm where stvterm_start_date > ?";
						df = new DateFormatter();
						prepStmt = conn.prepareStatement(sqlstmt);
						prepStmt.setDate(1, java.sql.Date.valueOf((df.getSimpleDate())));
						
						rs = prepStmt.executeQuery();
					    while (rs.next()){
					    	  term = rs.getString(1);
					    }
				  }
			      
			      
			      rs.close();
			      prepStmt.close();
			}
			catch(SQLException e){
				  e.printStackTrace();
			}
			
			
			return term;
	 }
	public void closeDb(){
		try {
		  conn.close();	
		}
	    catch(SQLException e){
		  e.printStackTrace();
	   }
	}
	public static void main(String[] args) throws SQLException {
		LecturersDb db = new LecturersDb();
		db.lecturerAvailable("20003843");
		System.out.println(db.findLecturer("Betty", "Rohlehr"));
		//System.out.println(db.findLecturer("Chatoyer ", "Bobb"));
		  
			
	} 
}
