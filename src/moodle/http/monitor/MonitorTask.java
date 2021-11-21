/*
 * Created on 2004/8/5
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package moodle.http.monitor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

import blackboard.db.MySQLConnection;
import blackboard.util.MessageLogger;



//import java.io.IOException;
//import java.io.FileInputStream;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MonitorTask extends TimerTask {
	
	String monitoredPoint;
	ServiceStarter sv;
	Monitor monitor;
	MySQLConnection connection;
	String bindingKey;
	String serviceKey;
	
    public MonitorTask(Monitor monitor,ServiceStarter sv, String monitoredPoint) {
	    this.sv = sv;
	    this.monitor = monitor;
	    this.monitoredPoint = monitoredPoint;
    }
	public void setBindingKey(String k) {
		this.bindingKey = k;
	}
	public void setServiceKey(String k) {
		this.serviceKey = k;
	}
	public void run() {
         connection = new MySQLConnection();
         connection.connectMoodleDb();
		 /*
        try {
      	  MessageLogger.setErr(new PrintStream(new FileOutputStream(new File("C:\\Monitor.txt"))));
      	
      } catch(Exception e) {
            e.printStackTrace(MessageLogger.out);
      }
        */ 
		try{
	    	MessageLogger.out.println("Starting validate");
		    MessageLogger.out.println(monitoredPoint);
		    
         	if(!sv.validateComponent(monitoredPoint)) {
         		
         		
         		MessageLogger.out.println("component fail");
         		
         		
         		
         		if(!sv.restartServlet()) {
         			
//         		if(!sv.startServlet()) {
         			MessageLogger.out.println("Fail to restart");
         			
         		} else {
         		  	
         			monitor.addCrashCount();
         	        MessageLogger.out.println("success restart");	
         		}
         		
         	}
         	String prev_time = connection.Inquiry(bindingKey, serviceKey);
            long diff = timeDiff(prev_time); 	
         	if (diff > 20) {
         		
         		  if(!sv.restartServlet()) {
         			
//             		if(!sv.startServlet()) {
             			MessageLogger.out.println("Fail to restart");
             			
             		} else {
             		  	
             			monitor.addCrashCount();
             	        MessageLogger.out.println("success restart");	
             		}
         	}
	    } catch (Exception e){
	    	e.printStackTrace(MessageLogger.out);
	    }
		
		connection.closeConnection();
	}
    private long timeDiff (String prev) {
    	
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = new Date();
     	
		Date d1 = null;
		Date d2 = null;
		long diffMinutes = 0;
		try {
			d1 = format.parse(prev);
			//Fri Jun 21 15:24:11 BOT 2019
			SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
             Locale.ENGLISH);
			
			Date parsedDate = sdf.parse(date.toString());
			SimpleDateFormat print = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			MessageLogger.out.println(print.format(parsedDate));
			
			d2 = format.parse(print.format(parsedDate));

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			//long diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			//long diffHours = diff / (60 * 60 * 1000) % 24;
			//long diffDays = diff / (24 * 60 * 60 * 1000);

			MessageLogger.out.println(diffMinutes + " minutes, ");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diffMinutes;
    }
}
