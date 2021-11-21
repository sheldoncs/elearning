package blackboard.util;

import java.util.HashMap;
import java.util.Properties;
import java.util.TimerTask;

import moodle.automate.enrol.UIBEnrol;
import moodle.http.crash.ComponentCrashException;
import moodle.http.crash.Crash;
import moodle.http.crash.Shutdownable;
import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.db.StudentExportDb;


public class DropEnrolTask extends TimerTask {

	private String effTerm;
	private Properties endPoint;
	private int ii;
	Crash crash;
	
	
	public DropEnrolTask(String effTerm,
			                              Properties endPoint){
		
		this.effTerm=effTerm;
		this.endPoint = endPoint;
		
	}
	public void setServer(Shutdownable server){
		crash = new Crash(server);
	}
	
	public void run() {
		
		try {
			
			
			
		    DteFormatter df = new DteFormatter();
		    String str = df.printTime();
		    
		    MessageLogger.out.println("Start Time " + df.printTime() );
		    MessageLogger.out.println("");
		    
		    if (!df.getDay().trim().equals("Sun") && !df.getDay().trim().equals("Sat")){ 
		    	
		    	OracleDBConnect db = new OracleDBConnect(); 
		    	MySQLConnection connect = new MySQLConnection();
		    	connect.connectMoodleDb();
		    	HashMap hmap = connect.returnMoodleObjects();
		        db.setMoodleObjects(hmap);	
		    	db.dropEnrollments();
				db.closeConnection();
				connect.closeConnection();
				
				MessageLogger.out.println("Drop Completed for " + df.printTime() );
		    }
		} catch (Exception ex){
			ex.printStackTrace();
			MessageLogger.out.println("Error Moodle Drop "+ex.getMessage());
			SendMail sm = new SendMail("epayments@cavehill.uwi.edu","sheldon.spencer@gmail.com","Drop Server Crash ...");
			sm.send();
			
		}
	}
}
