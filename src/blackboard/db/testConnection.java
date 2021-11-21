package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class testConnection {
	
    public static void main(String[] args) 
    {
        DB db = new DB();
        db.dbConnect(
     "jdbc:jtds:sqlserver://sqlsrvtemp1:1433/tempdb","sa","");
    }
}


