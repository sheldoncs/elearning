package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
//	connectionString="Data Source=SQLSRVTEMP5.cavehill.uwi.edu;Initial Catalog=CHOL_Override_Requests;
//	Integrated Security=False;user id=CHOL_COR_User;password=ch0lu$er007;
	public SQLConnection ()
	{
		String connectionUrl = "jdbc:sqlserver://SQLSRVTEMP5.cavehill.uwi.edu:1433;databaseName=CHOL_Override_Requests;user=CHOL_COR_User;password=ch0lu$er007";
		try {
			Connection con = DriverManager.getConnection(connectionUrl);
			 Statement stmt = con.createStatement();
			 
		            String SQL = "SELECT  * FROM overrides";
		            ResultSet rs = stmt.executeQuery(SQL);

		            // Iterate through the data in the result set and display it.
		            while (rs.next()) {
		                System.out.println("read");
		            }
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws SQLException {
		SQLConnection conn = new SQLConnection();
	}
}
