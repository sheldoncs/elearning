package blackboard.util;

import java.util.Properties;
import java.util.TimerTask;

import moodle.automate.enrol.UIBEnrol;
import moodle.http.crash.ComponentCrashException;
import moodle.http.crash.Crash;
import moodle.http.crash.Shutdownable;
import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.db.StudentExportDb;


public class MoodleEnrolTask extends TimerTask {

	private String effTerm;
	private Properties endPoint;
	private int ii;
	Crash crash;
	
	
	public MoodleEnrolTask(String effTerm,
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
		    String dteStr = df.printTime();
		    //if (!df.getDay().trim().equals("Sun") && !df.getDay().trim().equals("Sat")){ 
		      	 
		    	OracleDBConnect db = new OracleDBConnect(); 
		    	
				MySQLConnection connect = new MySQLConnection();
				connect.connectMoodleDb();
				
				crash.setDb(db);
				crash.setMySQLConntection(connect);
			    
				/*Crash Tester*/
				/*StudentExportDb exportDb = new StudentExportDb("", "admin", "kentish");
				crash.setStudentExport(exportDb);
			    if (exportDb.getTestResult().equals("True"))   
			    	throw new ComponentCrashException("Component Has Crashed");
			    /*Crash Tester*/
		    	
				UIBEnrol enrol = new UIBEnrol();
		    	enrol.setCrash(crash);
		    	
		    	enrol.setOracleConnect(db);
		    	enrol.setMySQLCOnnect(connect);
		    	
		    	enrol.startDocumentRead(db.getCurrentTerm());
		    	
		    //}
		} catch (Exception ex){
			ex.printStackTrace();
			MessageLogger.out.println("Error Moodle Enrol "+ex.getMessage());
			SendMail sm = new SendMail("epayments@cavehill.uwi.edu","sheldon.spencer@gmail.com","Enrollment Server Shutdown ...");
			sm.send();
			crash.crashXRun("Enrollment Server Crash ...");
		}
	}
}

/*Enrollment Server Crash ...
Starting to Destroy Component ....
Component is shutting down now ........
Component Destroyed*/
