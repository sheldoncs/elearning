package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import blackboard.util.FieldNamesProperties;
import blackboard.util.MessageLogger;
import blackboard.util.SpecialCharacters;




public class SpecialCharsDb {
	
	private Connection conn;
	
	
	  public SpecialCharsDb(){

		  try {//exportexcelDB
				
				 Context initContext = new InitialContext();
				 Context envContext  = (Context)initContext.lookup("java:/comp/env");
				 DataSource ds = (DataSource)envContext.lookup("jdbc/exportexcelDB");
				 conn = ds.getConnection();
				 MessageLogger.out.println("Connection has been made");
				} catch (NamingException ex){
					ex.printStackTrace();
					MessageLogger.out.println(ex.getMessage());
				} catch (SQLException sqle){
					sqle.printStackTrace();
					MessageLogger.out.println(sqle.getMessage());
				}
		  
	 }
	  
	 public SpecialCharsDb(String mysqlconnect, String username, String password){
		 
		 super();
		  
		  try {
				Class.forName("com.mysql.jdbc.Driver");
			
			    conn = DriverManager.getConnection("jdbc:mysql://owl2:3305/exportexcel",  "admin", "kentish");
				//conn = DriverManager.getConnection(mysqlconnect, username, password);
				conn.setAutoCommit(true);
		        

			} catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (SQLException ex){
				ex.printStackTrace();
			}
		 
	 }
	 
	 public ArrayList listSpecialCharacters(){
		 
		 ArrayList specialCharacterList = new ArrayList(); 
		 
		 
		 String selectStatement = "select specialcharacter,alphacharacter from specialcharacters";
		 
		 try {
		   PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			
		   prepStmt.execute("use exportexcel");
		   ResultSet rs = prepStmt.executeQuery();
		   
		   while (rs.next()){
			   SpecialCharacters sc = new SpecialCharacters();
			   sc.setSpecialCharacter(rs.getString(1));
			   sc.setAlphaCharacter(rs.getString(2));
			   specialCharacterList.add(sc);
			   
		   }
		   
		   
		 } catch (SQLException ex){
			 ex.printStackTrace();
		 }
		 return specialCharacterList;
	 }
	 	 public ArrayList getFieldNames(){
		 ArrayList list = new ArrayList();
		 
		 
		 
		 
		 try {
				String selectStatement = "select name, size from exportfieldnames";
				PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			
				
				prepStmt.execute("use exportexcel");
				ResultSet rs = prepStmt.executeQuery();
         
				while (rs.next()){
					FieldNamesProperties prop = new FieldNamesProperties();
					prop.setNames(rs.getString(1));
					prop.setSize(rs.getDouble(2));
					list.add(prop);
				}
				
				
			} catch (SQLException e) {
				MessageLogger.out
						.println("Error on email search=" + e.getMessage());

			}
		 
		 return list;
	 }
	 
	 
		 public void closeConnections() {
			try {
				conn.close();
			} catch (SQLException e) {
				MessageLogger.out.println(e.getMessage());
			}
		}
	 public static void main(String[] args) {
		  
		    //StudentExportDb  exportDb = new StudentExportDb("jdbc:mysql://localhost:3305/exportexcel",  "admin", "kentish");
			//BannerStudentDb rc = new BannerStudentDb();
		    //BannerStudentDb rc = new BannerStudentDb("com.mysql.jdbc.Driver");
			//rc.collectContinuingInfo();
			//rc.collectInfo();
			//rc.getListSize();
			//exportDb.insertToExportTable(rc.getList());
			//exportDb.listSpecialCharacters();
			
				
		}
}
