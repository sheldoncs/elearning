package blackboard.util;

import java.util.Properties;
import java.util.TimerTask;

import moodle.automate.metacourse.UIBMetaCourse;
import moodle.http.crash.Crash;
import moodle.http.crash.Shutdownable;
import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;


public class MetaCourseTask extends TimerTask {

	private String effTerm;
	private Properties endPoint;
	private int ii;
	private Crash crash;
	
	public MetaCourseTask(String effTerm,
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
		    if (!df.getDay().trim().equals("Sun") && !df.getDay().trim().equals("Sat")){ 
		      	 
		    	OracleDBConnect db = new OracleDBConnect(); 
		    	String semester=db.getCurrentTerm();
				MySQLConnection connect = new MySQLConnection();
				connect.connectMoodleDb();
				
		    	UIBMetaCourse course = new UIBMetaCourse();
		    	course.setLoadType("Exams");
		    	course.startDocumentRead(semester);
		    	
		    	db.closeConnection();
		    	connect.closeConnection();
		    }
		} catch (Exception ex){
			ex.printStackTrace();
			MessageLogger.out.println("Error Meta Course "+ex.getMessage());
			SendMail sm = new SendMail("epayments@cavehill.uwi.edu","sheldon.spencer@gmail.com","Meta Course Server Shutdown ...");
			sm.send();
			
		}
	}
}
