package blackboard.util;

import java.util.Properties;
import java.util.TimerTask;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;

import transfer.email.SendMail;

import moodle.automate.user.UIBUser;
import moodle.http.crash.Crash;
import moodle.http.crash.Shutdownable;


public class MoodleUserTask extends TimerTask {

	private String effTerm;
	private Properties endPoint;
	private int ii;
	private Crash crash;
	private String serviceKey;
	private String bindingKey;
	private OracleDBConnect db;
	private MySQLConnection connect;
	
	public String getBindingKey() {
		return bindingKey;
	}
	public void setBindingKey(String bindingKey) {
		this.bindingKey = bindingKey;
	}
	public String getServiceKey() {
		return serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}
	public MoodleUserTask(String effTerm,Properties endPoint){
		
		super();
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
		       
			    db = new OracleDBConnect(); 
				MySQLConnection connect = new MySQLConnection();
				connect.connectMoodleDb();
			    
				crash.setDb(db);
			    crash.setMySQLConntection(connect);
			    
			    
				UIBUser user = new UIBUser();
				user.setCrash(crash);
				
				user.setMySQLCOnnect(connect);
				user.setOracleConnect(db);
				
				user.setAction("create");
				user.startDocumentRead(db.getCurrentTerm());
			    
//			    user.setAction("update");
//			    user.startDocumentRead(db.getCurrentTerm());
			    
			    user.closeConnections();
		    
		    }
		} catch (Exception ex){
			ex.printStackTrace();
			MessageLogger.out.println("Error Moodle User "+ex.getMessage());
			
			SendMail sm = new SendMail("sheldon.spencer@gmail.com","server Server Shutdown ... " + "User Server Crash ... ");
			sm.setStatus("b");
			sm.send("","","");
			crash.crashXRun("User Server Crash...");
		}
	}
}
