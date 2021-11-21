package blackboard.db.users;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import blackboard.db.MySQLConnection;
import blackboard.db.LecturersDb;
import blackboard.db.OracleDBConnect;
import blackboard.util.BannerStudentInfo;
import blackboard.util.Courses;
import blackboard.util.Lecturers;
import blackboard.util.SendMail;
import blackboard.util.Users;

public class UserDb extends MySQLConnection {

	private FileOutputStream fileOutputStream;
	private Writer wr;
	private  BufferedWriter out;
	public UserDb(){
		super();
	}
    public int getNextNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM USERS";
		
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
    public String selectUserFileNumber() throws SQLException{
		
    	String file = "";
    	
		String sqlStmt="SELECT user FROM userfile";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			file = "users"+"_"+rs.getString(1);
		}
		
		return file;
		
    }
    public void insertUserFileNumber(String fileNumber){
    	
    	deleteUserFileNumber();
    	
    	String sqlstmt = "insert into userfile (id,user) values ( ? , ?)";
	    
		 try {
      if (conn == null){
      	System.out.println("Null Connection");
      }
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setInt(1, 1);
		prepStmt.setString(2, fileNumber);
		
		prepStmt.execute("use blackboard");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
    }
    public void insertUsers(Courses courses){
    	String sqlstmt = "insert into USERS (id, unique_id, login_id, first_name, last_name, email_addr, password ) values ( ? , ? , ?, ?, ?, ?, ?)";
	    LecturersDb db = new LecturersDb();
	    Lecturers lecturers =db.getNames(courses.getEmployeeID().trim());
	   
	    if (lecturers.getFirstname() != null && lecturers.getLastname() != null)
	    {
		   try {
          
       
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	
		         prepStmt.setInt(1, getNextNumber());
		         prepStmt.setString(2, courses.getEmployeeID());
		         prepStmt.setString(3, courses.getEmployeeID());
		         prepStmt.setString(4, lecturers.getFirstname());
		         prepStmt.setString(5, lecturers.getLastname());
		         prepStmt.setString(6, "");
		         prepStmt.setString(7, lecturers.getFirstname().substring(0, 1).toLowerCase()+lecturers.getLastname().toLowerCase());
		
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		      } catch (SQLException ex){
			     ex.printStackTrace();
		      }
	    }
   } 
    public void insertUsersFromExcel(Courses courses){
    	String sqlstmt = "insert into USERS (id, unique_id, login_id, first_name, last_name, email_addr, password ) values ( ? , ? , ?, ?, ?, ?, ?)";
	    LecturersDb db = new LecturersDb();
	    Lecturers lecturers =db.getNames(courses.getEmployeeID().trim());
	   
	    if (lecturers.getFirstname() != null && lecturers.getLastname() != null)
	    {
		   try {
          
       
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	
		         prepStmt.setInt(1, getNextNumber());
		         prepStmt.setString(2, courses.getEmployeeID());
		         prepStmt.setString(3, courses.getEmployeeID());
		         prepStmt.setString(4, lecturers.getFirstname());
		         prepStmt.setString(5, lecturers.getLastname());
		         prepStmt.setString(6, "");
		         prepStmt.setString(7, lecturers.getFirstname().substring(0, 1).toLowerCase()+lecturers.getLastname().toLowerCase());
		
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		      } catch (SQLException ex){
			     ex.printStackTrace();
		      }
	    }
   }
    public void prepareForOutput(){
    	try {
    		fileOutputStream = new FileOutputStream("c:/BUHolds.txt");
    		wr = new OutputStreamWriter(fileOutputStream);
    	} catch (FileNotFoundException exc){
	    	  exc.printStackTrace();
	    }
    	try {
            
    		out = new BufferedWriter(new FileWriter("c:/BUHolds.txt", false));
            
        } catch (IOException e) {
        }

    }
    public void insertUsersFromExcel(BannerStudentInfo bsi){
    	String sqlstmt = "insert into USERS (id, unique_id, login_id, first_name, last_name, email_addr, password ) values ( ? , ? , ?, ?, ?, ?, ?)";
	   
	     try {
          
       
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	
		         if (getUniqueID(bsi.getId()) != null){
		          
		          prepStmt.setInt(1, getNextNumber());
		          prepStmt.setString(2, getUniqueID(bsi.getId()));
		          prepStmt.setString(3, bsi.getId());
		          prepStmt.setString(4, bsi.getFirstname());
		          prepStmt.setString(5, bsi.getLastname());
		          prepStmt.setString(6, "onhold@cavehill.uwi.edu");
		          prepStmt.setString(7, "holdon");
		
	         	  prepStmt.execute("use blackboard");
		
		          prepStmt.executeUpdate();
		          prepStmt.close();
		         
		         } else {
		        	 out.write(bsi.getId());
		        	 out.newLine();
		         }
		   
		      } catch (SQLException ex){
			     ex.printStackTrace();
		      } catch (IOException exc){
		    	  exc.printStackTrace();
			    }
	  
   } 
   public void closeWrite(){
	   try {
	   wr.close();
	   out.close();
	   fileOutputStream.close();
	   } catch (IOException ex){
		   ex.printStackTrace();
	   }
   }
   public void insertNonCampusUsers(Users users){
	   String sqlstmt = "insert into USERS (id, unique_id, login_id, first_name, last_name, email_addr, password ) values ( ? , ? , ?, ?, ?, ?, ?)";
	    
		   try {
         
      
		         PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
	
		         prepStmt.setInt(1, getNextNumber());
		         prepStmt.setString(2, users.getUniqueID());
		         prepStmt.setString(3, users.getLoginID());
		         prepStmt.setString(4, users.getFirstname());
		         prepStmt.setString(5, users.getLastname());
		         prepStmt.setString(6, "");
		         prepStmt.setString(7, users.getPassword());
		
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		      } catch (SQLException ex){
			     ex.printStackTrace();
		      }
	    
   }
   
   private String getUniqueID(String loginid){
	   String sqlStmt="SELECT userid, loginid FROM blackboardusers WHERE loginid = ?";
	   String uniqueId = null;
	   
	   try {
	       PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		   prepStmt.setString(1, loginid);
		
		   prepStmt.execute("use blackboard");
		   ResultSet rs = prepStmt.executeQuery();
		
	 	   while (rs.next()){
			uniqueId = rs.getString(1);
		   }
	       } catch (SQLException ex){
		     ex.printStackTrace();
	       }
		
		return uniqueId;
	   
   }
   private void deleteUserFileNumber(){
	    String sqlstmt = "delete from userfile";
	    
		    try {
	             PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
	     	} catch (SQLException ex){
			     ex.printStackTrace();
		    }
	}
    public void deleteUsers(){
        String sqlstmt = "delete from users";
	    
		try {
                 PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		         
	         	 prepStmt.execute("use blackboard");
		
		         prepStmt.executeUpdate();
		         prepStmt.close();
		   
		} catch (SQLException ex){
			     ex.printStackTrace();
		}
    }
    public ArrayList selectUsers(String id) throws SQLException{
		
    	ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT UNIQUE_ID, LOGIN_ID, FIRST_NAME, LAST_NAME, PASSWORD FROM USERS WHERE UNIQUE_ID = ?";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		prepStmt.setString(1, id);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			Users user = new Users();
			user.setUniqueID(rs.getString(1));
			user.setLoginID(rs.getString(2));
			user.setFirstname(rs.getString(3));
			
			user.setLastname(rs.getString(4));
			user.setPassword(rs.getString(5));
			a.add(user);
		}
		
		return a;
		
    }
    public void updateUser(String id, OracleDBConnect oDb){
    	if (userFound(id)){
    		
    		String sqlstmt = "UPDATE USERS SET email_addr = ?, password = ?, holdreleased = ? where login_id = ?";
    	try{	
    		OracleDBConnect db = new OracleDBConnect(); 
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		prepStmt.setString(1, "");
    		System.out.println(db.getStudentPassword(id));
    		prepStmt.setString(2, db.getStudentPassword(id));
    		prepStmt.setBoolean(3, true);
    		prepStmt.setString(4, id);
    		
    		prepStmt.execute("use blackboard");
    		
	         prepStmt.executeUpdate();
	         prepStmt.close();
	         
	        
	         //SendMail sm = new SendMail("epayments@cavehill.uwi.edu", 
				//	 "sheldon.spencer@gmail.com", "test email");
	         String s = db.getEmailAddress(id);
	         System.out.println(id +","+db.getEmailAddress(id));
	         if (s != null){
	           if ((s.indexOf("hotmail") ==-1)){
	             SendMail sm = new SendMail("epayments@cavehill.uwi.edu", 
	        	  	 db.getEmailAddress(id), "Blackboard Reactivation");
		 	 
		        sm.send();
	           }
    	    }
		    db.closeConnection();
		    
    	    } catch (SQLException ex){
		     ex.printStackTrace();
	        }
        
    	}
    }
    public void flagUploadedUsers(String id){
    	
    	String sqlstmt = "UPDATE USERS SET alreadyuploaded = ? where login_id = ?";
    	
    	try{	
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		prepStmt.setBoolean(1, true);
    		prepStmt.setString(2, id);
    		
    		prepStmt.execute("use blackboard");
    		
	        prepStmt.executeUpdate();
	        prepStmt.close();
    		
    	} catch (SQLException ex){
		     ex.printStackTrace();
        }
    }
    public boolean alreadyLoaded(String id){
    	
    	boolean loaded = false;
    	
    	String sqlstmt = "select alreadyuploaded from users where login_id = ?";
    	
    	try{	
    		
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		prepStmt.setString(1, id);
    		
    		prepStmt.execute("use blackboard");
    		ResultSet rs = prepStmt.executeQuery();
    		
    		while (rs.next()){
    		
    			loaded = rs.getBoolean(1);
    	  		
    		}
    		
    	} catch (SQLException ex){
		     ex.printStackTrace();
        }
    	
    	return loaded;
    }
    private boolean userFound(String id){
    	
       boolean found = false;
    	
       String sqlStmt="SELECT UNIQUE_ID, LOGIN_ID, FIRST_NAME, LAST_NAME, PASSWORD FROM USERS WHERE LOGIN_ID = ? AND holdreleased <> ?";
		
        try {
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		prepStmt.setString(1, id);
		prepStmt.setBoolean(2, true);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		  while (rs.next()){
			
			found = true;  
		  }
		  
        } catch (SQLException ex){
        	ex.printStackTrace();
        }
        
    	return found;
    	
    }
    public ArrayList selectUsers() throws SQLException{
		
    	ArrayList a = new ArrayList();
    	
		String sqlStmt="SELECT UNIQUE_ID, LOGIN_ID, FIRST_NAME, LAST_NAME, EMAIL_ADDR, PASSWORD FROM USERS";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			Users user = new Users();
			user.setUniqueID(rs.getString(1));
			user.setLoginID(rs.getString(2));
			user.setFirstname(rs.getString(3));
			user.setLastname(rs.getString(4));
			user.setEmail(rs.getString(5));
			user.setPassword(rs.getString(6));
			a.add(user);
		}
		
		return a;
		
    }
    public void closeDb(){
    	try {
    	  conn.close();
    	} catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
    	
    	

    }
    public static void main(String[] args) throws RemoteException,SQLException {
    	UserDb db = new UserDb();
    	//db.userFound("19926511");
    	//db.updateUser("19926511");
    	
    }
    
}
