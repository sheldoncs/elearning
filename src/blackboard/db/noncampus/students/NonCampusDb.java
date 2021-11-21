package blackboard.db.noncampus.students;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import blackboard.db.MySQLConnection;
import blackboard.util.Users;

public class NonCampusDb extends MySQLConnection {

	public NonCampusDb(){
		super();
	}
	public ArrayList getNonCampusInfo() throws SQLException{
       ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT id, lastname,firstname,pin FROM studentexporttable";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);

		
		prepStmt.execute("use exportexcel");
		ResultSet rs = prepStmt.executeQuery();
		
		
		while (rs.next()){
			Users users = new Users();
			users.setUniqueID(rs.getString(1));
			users.setLoginID(rs.getString(1));
			users.setFirstname(rs.getString(3));
			users.setLastname(rs.getString(2));
			
			String F = rs.getString(3).substring(0, 1);
			String L = rs.getString(2).substring(0, 1);
			users.setPassword(F+L+"@"+rs.getString(4));
			a.add(users);
		}
		
		return a;
	}
}
