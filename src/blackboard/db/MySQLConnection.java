package blackboard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import moodle.http.crash.Crash;
import blackboard.db.users.UserDb;
import blackboard.util.BannerStudentInfo;
import blackboard.util.Courses;
import blackboard.util.DateFormatter;
import blackboard.util.FieldNamesProperties;
import blackboard.util.MessageLogger;
import blackboard.util.MoodleLoadOptions;
import blackboard.util.MoodleObjects;
import blackboard.util.SetupCourse;

public class MySQLConnection {
    
	protected Connection conn;
    private Crash crash;
    
	public  MySQLConnection(){
		
	}
	public void setCrash(Crash crash){
		this.crash = crash;
	}
	public void ConnectBlackboardDb(){
		try {
			
			if (conn == null){
			Class.forName("com.mysql.jdbc.Driver");
		     conn = DriverManager.getConnection("jdbc:mysql://owl1:3305/blackboard",  "adminuser", "kentish");
	         conn.setAutoCommit(true);
			}
		
		} catch (SQLException ex){
			crash.crashXRun("MySQL Database Crash ...");
			ex.printStackTrace();
			
		} catch (ClassNotFoundException e){
			crash.crashXRun("MySQL Database Class Not Found Exception Crash ...");
			e.printStackTrace();
		}
	}
	public void qlessConnect(){
       try {
			
			if (conn == null){
			Class.forName("com.mysql.jdbc.Driver");
		     conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qless",  "admin", "kentish");
	         conn.setAutoCommit(true);
			}
		
		} catch (SQLException ex){
			crash.crashXRun("MySQL Database Crash ...");
			ex.printStackTrace();
			
		} catch (ClassNotFoundException e){
			crash.crashXRun("MySQL Database Class Not Found Exception Crash ...");
			e.printStackTrace();
		}
	}
	public void connectLookups(){
		
	}
	public void connectWizzy() {
//		mysql://wizzywig_admin:Kentish_48@wizzywigs.com:3306/wizzywig_db
			try {
//				Class.forName("com.mysql.jdbc.Driver");
//				conn = DriverManager.getConnection("jdbc:mysql:/208.0.97.75:3306/wizzywig_db",  "wizzywig_admin", "kentish1968");
				String DATABASE_URL="postgres://zjykvterfufswe:da1f934d489b13875e4789a122e0fb3a169c33ceeac69bad4da5c3158f1c1024@ec2-52-21-252-142.compute-1.amazonaws.com:5432/d3tse3k3e9dgji";
				String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
				Connection conn = DriverManager.getConnection(DATABASE_URL);
				
				conn.setAutoCommit(true);
			        
				
				} catch (SQLException ex){
					ex.printStackTrace();
					
				}
	}
	public void connectUDDI() {
		try {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://owl1:3305/juddi",  "admin", "kentish");
	    conn.setAutoCommit(true);
	        
		
		} catch (SQLException ex){
			ex.printStackTrace();
			
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	public String Inquiry(String bindingKey, String serviceKey) {
		String dteStr = "";
		String sqlstmt = "select previous_update from binding_template WHERE  binding_key = ? and service_key = ?";
		
		try {
	    	
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		prepStmt.setString(1, bindingKey);
    		prepStmt.setString(2, serviceKey);
    		
    		prepStmt.execute("use juddi");
    		
    		ResultSet rs = prepStmt.executeQuery();
    		if (rs.next()) {
    			
    			dteStr = rs.getString(1);
    		}
    		
    	} catch (SQLException e) {
			MessageLogger.out
			.println("Error on select moodleloadoptions=" + e.getMessage());

        }
    	
    	return dteStr;
	}
	public void publish(String bindingKey, String serviceKey) {
		
		
			  String sqlstmt = "UPDATE binding_template SET previous_update = ? WHERE  binding_key = ? and service_key = ?";
				
			try {
				PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
				Date date = new Date();
				date.toString();
				SimpleDateFormat ft = 
					      new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
					      
				
				prepStmt.setString(1,ft.format(date));
				prepStmt.setString(2,bindingKey);
				prepStmt.setString(3,serviceKey);
				
				prepStmt.executeUpdate();
				prepStmt.close();
				
			} catch (SQLException ex){
				ex.printStackTrace();
			}
		
	}
	
	public void connectExtDb(){
		try {
			
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3305/exportexcel",  "admin", "kentish");
		    conn.setAutoCommit(true);
		        
			
			} catch (SQLException ex){
				ex.printStackTrace();
				
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			}
	}
	public void connectMoodleDb(){
		try {
			
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://owl1:3305/moodleuib",  "admin", "kentish");
		    
			conn.setAutoCommit(true);
		        
			
			} catch (SQLException ex){
				ex.printStackTrace();
				
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			}
	}
	public void connectMoodleDbTest(){
		try {
			
			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://owl1:3305/moodleuib_test",  "admin", "kentish");
		    
			conn.setAutoCommit(true);
		        
			
			} catch (SQLException ex){
				ex.printStackTrace();
				
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			}
	}
	public void updateCrashRestartTime(String serverName, boolean isCrash){
		String sqlstmt = "";
		if (isCrash)
		  sqlstmt = "UPDATE detectcrashrestart SET CRASHTIME = ? WHERE  ACTIVITY = ?";
		else 
			sqlstmt = "UPDATE detectcrashrestart SET RESTARTTIME = ? WHERE  ACTIVITY = ? ";	
		try {
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			Date date = new Date();
			date.toString();
			SimpleDateFormat ft = 
				      new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss a ");
				      
			if (isCrash){
				prepStmt.setString(1,ft.format(date));
				prepStmt.setString(2,serverName);
				
			} else {
				prepStmt.setString(1,ft.format(date));
				prepStmt.setString(2,serverName);
				
			}
			
			prepStmt.executeUpdate();
			prepStmt.close();
			
		} catch (SQLException ex){
			ex.printStackTrace();
		}
		
	}
	
    public void saveSuspended(String id){
    	
    	String sqlstmt = "INSERT INTO suspended (studentid) "
				+ "VALUES (?)";
    	try {
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			
			prepStmt.execute("use moodleuib");
			
			if (!isSuspended (id)){
				prepStmt.setString(1, id);
				prepStmt.executeUpdate();
				prepStmt.close();
			}
    	} catch(Exception ex) {
			ex.printStackTrace();
        }
    	
    }
    public void removeSuspended(String id){
    	
    	String sqlstmt = "delete from suspended where studentid = ? ";
				
    	try {
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			
			prepStmt.execute("use moodleuib");
			
			if (isSuspended (id)){
				prepStmt.setString(1, id);
				prepStmt.executeUpdate();
				prepStmt.close();
			}
    	} catch(Exception ex) {
			ex.printStackTrace();
        }
    	
    }
    public boolean isSuspended (String id){
    	
    	String sqlstmt = "select studentid from suspended where studentid = ? ";
    	boolean found = false;
    	try {
    	
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		prepStmt.setString(1, id);
    		
    		prepStmt.execute("use moodleuib");
    		
    		ResultSet rs = prepStmt.executeQuery();
    		if (rs.next()) {
    			
    			found = true;
    			
    		}
    		
    	} catch (SQLException e) {
			MessageLogger.out
			.println("Error on select moodleloadoptions=" + e.getMessage());

        }
    	
    	return found;
    }
    public void manulSuspendedRemoval(){
    	
    	    OracleDBConnect db = new OracleDBConnect();
        	
        	String sqlstmt = "select studentid from suspended ";
        	
        	try {
        	
        		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
        		
        		
        		prepStmt.execute("use moodleuib");
        		
        		ResultSet rs = prepStmt.executeQuery();
        		while (rs.next()) {
        			
        			String id = rs.getString(1);
        			boolean onhold = db.OnHold(id);
        			if (!onhold)
        				this.removeSuspended(id);
        		}
        		
        	} catch (SQLException e) {
    			MessageLogger.out
    			.println("Error on select moodleloadoptions=" + e.getMessage());

            }
        	
        	db.closeConnection();
       
    	
    }
	public MoodleLoadOptions getLoadOptions(){
		  
		  MoodleLoadOptions mlo = new MoodleLoadOptions();
		  
		  //connectMoodleDb();
		  
		  try {
				String selectStatement = "select l,s from moodleloadoptions";
				PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
				
				
				ResultSet rs = prepStmt.executeQuery();
             
				while (rs.next()) {
				
					mlo.setLecturer(rs.getInt(1));
					mlo.setStudent(rs.getInt(2));
					
				}
				prepStmt.close();
				rs.close();
		    	
		  } catch (SQLException e) {
				MessageLogger.out
				.println("Error on select moodleloadoptions=" + e.getMessage());

	      }
		  
		  
		  return mlo;
	}
	public HashMap returnMoodleObjects(){
		
		
	   
	    HashMap hmap = new HashMap();
	    try {
	    	
	    	String selectStatement = "select obj,action,semester,override from moodleobjects";
			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			
			
			ResultSet rs = prepStmt.executeQuery();
         
			while (rs.next()) {
				
				MoodleObjects mo = new MoodleObjects();
				mo.setObj(rs.getString(1));
				mo.setAction(rs.getString(2));
				mo.setSemester(rs.getString(3));
				mo.setOverride(rs.getBoolean(4));
				
				hmap.put(rs.getString(1), mo);
			}
			prepStmt.close();
			rs.close();
	    	
	    } catch (SQLException ex){
	    	ex.printStackTrace();
	    }
		
	    
	    return hmap;
	    
	}
    public boolean loadCourses(){
		
		boolean load = false;
		
	    this.connectMoodleDb();
	    HashMap hmap = new HashMap();
	    try {
	    	
	    	String selectStatement = "select rel from moodleobjects where obj = ?";
			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			prepStmt.setString(1, "course");
			
			ResultSet rs = prepStmt.executeQuery();
         
			while (rs.next()) {
				
				load = rs.getBoolean(1);
			}
			prepStmt.close();
			rs.close();
	    	
	    } catch (SQLException ex){
	    	ex.printStackTrace();
	    }
		
	    
	    return load;
	    
	}
	public SetupCourse returnCourseSettings(){
		
		this.connectMoodleDb();
		SetupCourse sc = new SetupCourse();
	    
	    try {
	    	
	    	String selectStatement = "select crsecode,crsenumb,upload,seq_no from setupcourse";
			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			
			
			ResultSet rs = prepStmt.executeQuery();
         
			while (rs.next()) {
				
				
				sc.setCrseCode(rs.getString(1));
				sc.setCrseNumb(rs.getString(2));
				sc.setUpload(rs.getBoolean(3));
				sc.setSeqNumb(rs.getString(4));
				
				
			}
	    	prepStmt.close();
	    	rs.close();
	    	
	    } catch (SQLException ex){
	    	ex.printStackTrace();
	    }
		
	    
	    return sc;
	    
		
	}
	public ArrayList getExportInfo(){
		 ArrayList list = new ArrayList();
		 try {
				String selectStatement = "select * from studentexporttable";
				PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
				
				prepStmt.execute("use exportexcel");
				ResultSet rs = prepStmt.executeQuery();
               rs.first();
				while (rs.next()) {
               
					BannerStudentInfo bsi = new BannerStudentInfo();
					bsi.setId(rs.getString(1));
					bsi.setLastname(rs.getString(2));
					bsi.setFirstname(rs.getString(3));
					bsi.setInitial(rs.getString(4));
					bsi.setEmail(rs.getString(5));
					bsi.setFaculty(rs.getString(6));
					bsi.setLevel(rs.getString(7));
					bsi.setMajor(rs.getString(8));
					bsi.setPin(rs.getString(9));
					bsi.setCountry(rs.getString(10));
					
					if (rs.getString(11) != null){ 
					  bsi.setBirthDate(java.sql.Date.valueOf(rs.getString(11)));
					} else {
						bsi.setBirthDate(null);
					}
					bsi.setPIDM(rs.getInt(12));
					bsi.setStudentType(rs.getString(13));
					bsi.setCampus(rs.getString(14));
					
					
					list.add(bsi);
				}		
			} catch (SQLException e) {
				MessageLogger.out
						.println("Error on email search=" + e.getMessage());

			}
			
			return list;
	 }
	public ArrayList getMoodleFieldList(String table){
		
         ArrayList list = new ArrayList();
		 
		 try {
				String selectStatement = "select * from " + table;
				PreparedStatement prepStmt = conn.prepareStatement(selectStatement);		
				
				ResultSet rs = prepStmt.executeQuery();
        
				while (rs.next()){
					System.out.println(rs.getString(1));
					list.add(rs.getString(1));
				}
				
				prepStmt.close();
				rs.close();
				
			} catch (SQLException e) {
				MessageLogger.out
						.println("Error on field list search=" + e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
			}
		 
		 return list;
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
	public Connection getConnection(){
		return conn;
	}
	
	private String getServiceListSQL(){
		
		String sqlStmt="SELECT SERVERCLIENTLIST FROM SERVICELIST";
		
		
		
		return sqlStmt;
	}
	public int getNextNumber() throws SQLException {
		
		boolean found=false;
		int cnt = 0;
		
		String sqlStmt="SELECT MAX(COUNTER) AS MAXCOUNTER FROM SERVICECOUNTER";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		 
		prepStmt.execute("use servicestation");
		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()){
			found=true;
			cnt = rs.getInt(1);
			cnt++;
		}
		if (!found)
	      cnt=0;		 
	 
		cnt++;
		
		return cnt;
	}
	
	public void identifyClient(String client, int id) {
	
		String sqlstmt = "insert into ASSIGNMENTS values ( ? , ? , ?)";;
		 try {
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setInt(1, getMaxAssignment());
		prepStmt.setString(2, client);
		prepStmt.setInt(3, id);

		prepStmt.execute("use servicestation");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
	}
	private int getMaxAssignment(){
		
		boolean found=false;
		int cnt = 0;
	    
		try {
		String sqlStmt="SELECT MAX(ID) AS MAXID FROM ASSIGNMENTS";
		
		PreparedStatement prepStmt = conn.prepareStatement(sqlStmt);
		 
		prepStmt.execute("use servicestation");
		ResultSet rs = prepStmt.executeQuery();
		rs.first();
		while (rs.next()){
			found=true;
			cnt = rs.getInt(1);
			cnt++;
		}
		 if (!found){
	      cnt++;	
		 }
		} catch (SQLException ex){
			ex.printStackTrace();
		}
		return cnt;
	}
	
	public boolean releaseStudentFromHold(){
		
		boolean found = false;
		
		String sqlstmt = "select id,lastname,firstname from onhold" ;
		try {

		  PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		  
		  ResultSet rs = prepStmt.executeQuery();
		  OracleDBConnect db = new OracleDBConnect();
		  UserDb userDb = new UserDb();
		  while (rs.next()){
			
			  if (!db.studentOnHold(rs.getString(1))){
				 
			    	  System.out.println(rs.getString(1));
			    	  userDb.updateUser(rs.getString(1),db);  
			      } else {
			    	  System.out.println("false");
			      }
			  }
			db.closeConnection();
			rs.close();
			userDb.closeConnection();
			userDb.closeDb();
			
		} catch (SQLException ex){
			ex.printStackTrace();
		}
		return found;
	}
	public ArrayList acquireCourses(ArrayList paramList,String id){
		ArrayList list = new ArrayList();
		int count = 0;
		int cnt=0;
		String sqlstmt = "select * from blackboardcourses";
		//COMP, PHYS, MATH and ELET
		
		Iterator iterator = paramList.iterator();
		
		while (iterator.hasNext()){
			iterator.next();
			if (sqlstmt.indexOf("where") >= 0){
				sqlstmt = sqlstmt + " or courseid like ? ";	
			}else {
				sqlstmt = sqlstmt + " where courseid like ? ";
			}
			 
		}
		
		try {
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		iterator = paramList.iterator();
		while (iterator.hasNext()){
			cnt++;
			prepStmt.setString(cnt, "%" +iterator.next().toString() + "%");
		}
		
		 
		prepStmt.execute("use blackboard");
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()){
			count++;
			Courses cs = new Courses();
			cs.setUniqueID(rs.getString(2));
			cs.setEmployeeID(id);
			list.add(cs);
			System.out.println("No of Records = " + count + " Course = " + rs.getString(2));
			
		}
		} catch (SQLException ex){
			ex.printStackTrace();
		}
		
		return list;
	}
	public void addStudentsOnHold(String id, String firstname, String lastname, Date birthdate, String level){
		
		String sqlstmt = "insert into onhold values ( ? , ? , ?, ?, ?, ?, ?)";
		DateFormatter df = new DateFormatter();
		
		 try {
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setString(1, id);
		prepStmt.setString(2, firstname);
		prepStmt.setString(3, lastname);
		prepStmt.setString(4, df.getFormattedDate(birthdate));
		prepStmt.setBoolean(5, false);
		prepStmt.setString(6, level);
		prepStmt.setBoolean(7, false);
		
		prepStmt.execute("use blackboard");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
	}
    public void deleteStudentOnHold(String id){
		
		String sqlstmt = "delete from onhold where id = ?";;
		 try {
		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
		
		prepStmt.setString(1, id);
		
		prepStmt.execute("use blackboard");
		prepStmt.executeUpdate();
		prepStmt.close();
		 } catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
	}
    public void updateStudentOnHold(String id, boolean found){
    	
    	String sqlstmt = "update onhold set found = ? where id = ?";
    	
    	try {
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		
    		prepStmt.setBoolean(1, found);
    		prepStmt.setString(2, id);
    		
    		prepStmt.execute("use blackboard");
    		prepStmt.executeUpdate();
    		prepStmt.close();
    		 } catch (SQLException ex){
    			 ex.printStackTrace();
    			 
    		 }
    }
	public void closeConnection(){
		try {
			conn.close();
		}catch (SQLException ex){
			 ex.printStackTrace();
			 
		 }
	}
    public String[] returnCourseStartdate(OracleDBConnect db, String semester){
		String[] dates = new String[2];
    	String term = db.getCurrentTerm();
    	if (semester!="") {
    		term=semester;
    	}
		HashMap hmap = returnMoodleObjects();
		
		MoodleObjects mo = (MoodleObjects)hmap.get("course");
		
		if (mo.getOverride()){
			term = mo.getSemester();
		}
		String courseStartDate = "";
		String courseEndDate="";
	    try {
	    	//setupcoursedate
	    	String selectStatement = "select coursedate,enddate from setupcoursedate where semester = ?";
			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			prepStmt.setString(1, term);
			
			ResultSet rs = prepStmt.executeQuery();
         
			while (rs.next()) {
				
				courseStartDate = rs.getString(1);
				courseEndDate=rs.getString(2);
				dates[0]=courseStartDate;
				dates[1]=courseEndDate;
			}
			prepStmt.close();
	    	rs.close();
	    	
	    } catch (SQLException ex){
	    	ex.printStackTrace();
	    }
	    
		
	    
	    return dates;
	    
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//COMP, PHYS, MATH and ELET
		MySQLConnection db = new MySQLConnection();
		db.connectWizzy();
		db.connectUDDI();
		
		//db.connectMoodleDb();
		//db.manulSuspendedRemoval();
		//db.updateCrashRestartTime("ENROL", false);
		
		//db.releaseStudentFromHold();
		//ArrayList alist = new ArrayList();
		//alist.add("COMP");
		//alist.add("PHYS");
		//alist.add("MATH");
		//alist.add("ELET");
		//db.acquireCourses(alist,"200000815");
		
		
	}
}
