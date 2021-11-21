package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

class DB
{
    public DB() {}

    public void dbConnect(String db_connect_string, String db_userid, String db_password)
    {
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
            System.out.println("connected");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void testmySQLConnect() {
    	try
        {
    	Class.forName("com.mysql.jdbc.Driver");

    	  Connection conn = DriverManager.getConnection("jdbc:mysql://websrv3:3306/tourism",  "ctourism", "credittourism");
    	  conn.setAutoCommit(true);
    	  Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    	 // String sql = "select * from countries order by country";
    	  //Vector v = new Vector();
    	  //ResultSet rs = stmt.executeQuery(sql);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) 
	{
		  DB db = new DB();
	      db.testmySQLConnect();
	}
} 
