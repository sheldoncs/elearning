package blackboard.db;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import blackboard.util.BannerStudentInfo;
import blackboard.util.DateFormatter;
import blackboard.util.MessageLogger;
import blackboard.util.StringUtils;



public class BannerSelector {

	private Connection idconn;
	private String connectString;
	public BannerSelector(String connectString){
		try
	      {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
		idconn = DriverManager.getConnection(connectString,"20003569","kentish11");
		
		
	    } catch (Exception e){
	    	  MessageLogger.out.println(e.getMessage());
	      }
		System.out.println("BannerSelector,Connected To Access");

	}
	public void setConnectionString(String connectString){

		this.connectString = connectString;
		
		try
	      {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
		idconn = DriverManager.getConnection(connectString);
		
		
	    } catch (Exception e){
	    	  MessageLogger.out.println(e.getMessage());
	      }
		System.out.println("BannerSelector,Connected To Access");
	}
	public void selectStudentsOnHold(){
		String sqlstmt = 
			"SELECT SATURN_SPRIDEN.SPRIDEN_ID, SATURN_SPRIDEN.SPRIDEN_LAST_NAME, " +
		    "SATURN_SPRIDEN.SPRIDEN_FIRST_NAME, SATURN_SGBSTDN.SGBSTDN_STST_CODE, " +
		    "SATURN_SPRHOLD.SPRHOLD_HLDD_CODE, SATURN_SGBSTDN.SGBSTDN_CAMP_CODE, " +
		    "SATURN_SGBSTDN.SGBSTDN_COLL_CODE_1, SATURN_SGBSTDN.SGBSTDN_SITE_CODE, " +
		    "SATURN_SGBSTDN.SGBSTDN_LEVL_CODE, SATURN_SPBPERS.SPBPERS_BIRTH_DATE " +
            "FROM SATURN_SPBPERS INNER JOIN (SATURN_SPRIDEN INNER JOIN (SATURN_SPRHOLD " +
            "INNER JOIN SATURN_SGBSTDN ON SATURN_SPRHOLD.SPRHOLD_PIDM = SATURN_SGBSTDN.SGBSTDN_PIDM) " +
            "ON SATURN_SPRIDEN.SPRIDEN_PIDM = SATURN_SGBSTDN.SGBSTDN_PIDM) " +
            "ON SATURN_SPBPERS.SPBPERS_PIDM = SATURN_SPRIDEN.SPRIDEN_PIDM " +
            "WHERE (((SATURN_SPRHOLD.SPRHOLD_HLDD_CODE)=?) AND " + 
            "((SATURN_SPRHOLD.SPRHOLD_TO_DATE)>= ?) AND " +
            "((SATURN_SGBSTDN.SGBSTDN_TERM_CODE_EFF)=?) AND + " +
            "((SATURN_SPRIDEN.SPRIDEN_CHANGE_IND) Is Null))";
        int cnt = 0;
		try {
		     PreparedStatement prepStmt = idconn.prepareStatement(sqlstmt);
		     DateFormatter df = new DateFormatter();
		     
		     prepStmt.setString(1, "BU");
		     prepStmt.setDate(2, java.sql.Date.valueOf(df.getSimpleDate()));
		     prepStmt.setString(3,"200820");
		     
		     ResultSet rs = prepStmt.executeQuery();
		     while (rs.next()) {
		    	 cnt++;
		    	 System.out.println(cnt);
		     }
		} catch (SQLException ex){
			ex.printStackTrace();
		}
	}
	public ArrayList getDataList() throws SQLException {
		
			ArrayList l = new ArrayList();
			StringUtils su = new StringUtils();
			String selectStatement = "select * from Activedirectorystudentlookup order by id";
			PreparedStatement prepStmt = idconn.prepareStatement(selectStatement);

			
			ResultSet rs = prepStmt.executeQuery();
			String initial = null; 
			    
			
				while (rs.next()) {
					
					BannerStudentInfo s = new BannerStudentInfo();
                    
					s.setId(rs.getString(1));
					s.setTerm(rs.getString(2));
					s.setLastname(rs.getString(3));
					s.setFirstname(rs.getString(4));
					
					if (initial != null){
					  initial = rs.getString(5);
					  s.setInitial(initial);
					}
					
					s.setLevel(rs.getString(6));
					s.setFaculty(rs.getString(7));
					s.setCampusCode(rs.getString(8));
					s.setAction(rs.getString(9));
					s.setBirthDate(rs.getDate(10));
					s.setMajor(rs.getString(11));
					s.setCountry(rs.getString(12));
					s.setEmail(rs.getString(13));
					s.setPin(rs.getString(14));
                    
					l.add(s);
				}
			

			return l;
	}
	public static void main(String[] args) throws RemoteException,SQLException {
		
		BannerSelector sl = new BannerSelector("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};"+
				"DBQ=C:\\Documents and Settings\\sheldon.spencer\\Desktop\\Banner MSAccess\\Banner Reports.mdb");
		  sl.selectStudentsOnHold(); 
		 
			
	}
}
