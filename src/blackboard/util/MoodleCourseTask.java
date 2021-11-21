package blackboard.util;

import java.util.Properties;
import java.util.TimerTask;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;

import moodle.automate.course.UIBCourse;
import moodle.automate.enrol.UIBEnrol;
import moodle.http.crash.Crash;
import moodle.http.crash.Shutdownable;


public class MoodleCourseTask extends TimerTask {

	private String effTerm;
	private Properties endPoint;
	private int ii;
	private Crash crash;
	
	public MoodleCourseTask(String effTerm,
			                              Properties endPoint){
		
		this.effTerm=effTerm;
		this.endPoint = endPoint;
		
	}
	public void setServer(Shutdownable server){
		crash = new Crash(server);
	}
	public void run() {
		
		try {
			   		
		    OracleDBConnect db = new OracleDBConnect(); 
			MySQLConnection connect = new MySQLConnection();
			connect.connectMoodleDb();
			crash.setDb(db);
			crash.setMySQLConntection(connect);
				
		    UIBCourse course = new UIBCourse();
		    course.setCrash(crash);
		    course.setMySQLCOnnect(connect);
		    course.setOracleConnect(db);
				
		    course.startDocumentRead(db.getCurrentTerm());
		    	
		    
		} catch (Exception ex){
			ex.printStackTrace();
			MessageLogger.out.println("Error Moodle Course "+ex.getMessage());
			SendMail sm = new SendMail("epayments@cavehill.uwi.edu","sheldon.spencer@gmail.com","Course Server Shutdown ...");
			sm.send();
			crash.crashXRun("Course Server Crash ...");
		}
	}
}
