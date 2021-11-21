/*
 * Created on Jul 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package moodle.http.crash;

import java.net.HttpURLConnection;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.db.StudentExportDb;
import blackboard.util.MessageLogger;





/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Crash {
	private Properties prop;
	private Shutdownable server;
	private HttpServlet servlet;
	private OracleDBConnect db;
	MySQLConnection connect;
	StudentExportDb exportDb;
	private HttpURLConnection connection;
	
	public Crash()
	{
		
	}
	
	public Crash(Shutdownable server)
	{
		this.server = server;
		
	}
	public void setDb(OracleDBConnect db){
	  this.db = db;
	}
	public void setStudentExport(StudentExportDb exportDb){
		this.exportDb = exportDb;
	}
	
	public void setMySQLConntection(MySQLConnection connect){
		this.connect = connect;
	}
	public void setServlet(HttpServlet servlet)
	{
		this.servlet = servlet;
	}
	public void setUIBConnection(HttpURLConnection connection){
		this.connection = connection;
	}
	public void crashXRun(String uibCrh)
	{
		MessageLogger.out.println(uibCrh+" server value = "+server);
		if(server != null)
		{
			
			MessageLogger.out.println("start close connections");
			db.closeConnection();
			connect.closeConnection();
			MessageLogger.out.println("end close connections");
			
	        /*For Crash Tester*/ 
			// exportDb.closeConnections();
	        
			try {
			MessageLogger.out.println("Starting to Destroy Component ....");			
			server.shutdown();
			
			MessageLogger.out.println("Component Destroyed");
			} catch(Exception ex){
				MessageLogger.out.println("Error on Servlet Destruction " + ex.getMessage());
			}
			
			
		}
	}
	public void setServer(Shutdownable server)
	{
		this.server = server;
	}
	
}
