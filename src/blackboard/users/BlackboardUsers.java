package blackboard.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blackboard.db.MySQLConnection;
import blackboard.util.Users;

public class BlackboardUsers extends MySQLConnection{
  
	private String userid;
	
	public BlackboardUsers(){
		super();
	}
	public void insertCurrentUsers(Users users){
		
		String sqlstmt = "insert into BLACKBOARDUSERS (id,userid,loginid,firstname,lastname) values (?,?,?,?,?)";
	    
		   try {
          
       
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	
		         prepStmt.setInt(1, getNextNumber());
		         prepStmt.setString(2, users.getUniqueID());
		         prepStmt.setString(3, users.getLoginID());
		         prepStmt.setString(4, users.getFirstname());
		         prepStmt.setString(5, users.getLastname());
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		      } catch (SQLException ex){
			     ex.printStackTrace();
		      }
	   
		
	}
	public boolean selectUser(String loginid) throws SQLException{
		boolean found = false;
		
          String sqlStmt="SELECT userid,loginid FROM BLACKBOARDUSERS WHERE loginid = ?";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		 
		prepStmt.setString(1, loginid);
		 
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			found=true;	
			userid = rs.getString(1);
		}
		
		return found;
	}
	public String getUserId(){
		return userid;
	}
    public int getNextNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM BLACKBOARDUSERS";
		
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
