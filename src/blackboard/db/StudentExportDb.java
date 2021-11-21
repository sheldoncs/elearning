package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blackboard.util.MessageLogger;


public class StudentExportDb {
	
	private Connection conn;
	
	  public StudentExportDb(){

		  
		  try {
				Class.forName("com.mysql.jdbc.Driver");
			
			    conn = DriverManager.getConnection("jdbc:mysql://owl1:3305/exportexcel",  "admin", "kentish");
				//conn = DriverManager.getConnection(mysqlconnect, username, password);
				conn.setAutoCommit(true);
		        

			} catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (SQLException ex){
				ex.printStackTrace();
			}
		      
	 }
	  
	 public StudentExportDb(String mysqlconnect, String username, String password){
		 
		
		  
		  try {
				Class.forName("com.mysql.jdbc.Driver");
			
			    conn = DriverManager.getConnection("jdbc:mysql://owl1:3305/exportexcel",  "admin", "kentish");
				//conn = DriverManager.getConnection(mysqlconnect, username, password);
				conn.setAutoCommit(true);
		        

			} catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (SQLException ex){
				ex.printStackTrace();
			}
		 
	 }
	 
	 public String getTestResult(){
		 
		 String result = "False";
		 
         String selectStatement = "select testvalue from testtable";
		 
		 try {
			 
		     PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
		   
		   
		   prepStmt.execute("use exportexcel");
		   ResultSet rs = prepStmt.executeQuery();
		     
		   if (rs.next()){
			
			   if (rs.getString(1).equals("True")){
				   result = "True";
			   } else {
				   result = "False";
			   }
			   
		   }
		   
		   
		 } catch (SQLException ex){
			 ex.printStackTrace();
		 }
		 
		 
		 return result;
	 }
		 public void closeConnections() {
			try {
				conn.close();
			} catch (SQLException e) {
				MessageLogger.out.println(e.getMessage());
			}
		}
	 public static void main(String[] args) {
		  
		    StudentExportDb  exportDb = new StudentExportDb("jdbc:mysql://owl1:3305/exportexcel",  "admin", "kentish");
		    exportDb.getTestResult();
		    
			
				
		}
}
