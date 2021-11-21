package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blackboard.util.DateFormatter;

public class OracleConnect {

    private Connection conn;

    public OracleConnect(){
    try {
			//simdb
		    Class.forName("oracle.jdbc.driver.OracleDriver");
	        String url = "jdbc:oracle:thin:@kronos2.cavehill.uwi.edu:1521:prodch";
	        conn = DriverManager.getConnection(url,"baninst1", "ban_8_admin");
	        conn.setAutoCommit(true);


		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (SQLException sqle){
           sqle.printStackTrace();
       }
    }
    public String getCurrentTerm(){
		 String term = null;

			String sqlstmt = "select max(stvterm_code) as maxtermcode from stvterm where stvterm_start_date < ? and stvterm_end_date > ?";
			DateFormatter df = new DateFormatter();

			try {

		     	  PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
				  prepStmt.setDate(1, java.sql.Date.valueOf((df.getSimpleDate())));
			      prepStmt.setDate(2, java.sql.Date.valueOf(df.getSimpleDate()));

			      ResultSet rs = prepStmt.executeQuery();
			      while (rs.next()){
			    	  term = rs.getString(1);
			      }
			      if (term.indexOf("40")>=0){
			    	  term=null;
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
    public void collectStudentHolds(){
    	
    
    }
    public String getCRNKey(String term, String subj, String courseNumber){

        String crnKey = null;
        
        String sqlstmt = "SELECT CRN_KEY FROM BANINST1_AS_STUDENT_REGISTRATION_DETAIL " +
                "WHERE TERM_CODE_KEY = ? AND SUBJ_CODE = ? AND COURSE_NUMBER = ?";

        try {
        PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
        prepStmt.setString(1, term);
        prepStmt.setString(2, subj);
        prepStmt.setString(3, courseNumber);

        ResultSet rs = prepStmt.executeQuery();

        while (rs.next()){
			    	  crnKey = rs.getString(1);
		}

        } catch (SQLException e){
            e.printStackTrace();
        }
        return crnKey;
    }
    public void closeDb(){
        try {
         conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
          OracleConnect oc = new  OracleConnect();
          oc.getCRNKey("200810", "ACCT", "2017");
    }

}

