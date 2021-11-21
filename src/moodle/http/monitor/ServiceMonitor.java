/*
 * Created on 2004/8/5
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package moodle.http.monitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import blackboard.util.MessageLogger;





/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServiceMonitor extends HttpServlet implements Monitor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final long TIME2START = 10000;

	ServiceStarter st;
	
	Timer timer;
	
	URL url;
	
	String monitoredPoint;
	String contextPath;
	String bindingKey;
	String serviceKey;
	
	int crashCount = 0;
	
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        System.out.println("Start Here"); 
        timer = new Timer();
        try {
        	 
        	  Properties endPoint = (Properties)servletConfig.getServletContext().getAttribute("EndPointProperties");
    		
        	 
        	  MessageLogger.setErr(new PrintStream(new FileOutputStream(new File(endPoint.getProperty("LogFile").toString()))));
        	  MessageLogger.setOut(new PrintStream(new FileOutputStream(new File(endPoint.getProperty("LogFile").toString()))));
        	
        	  MessageLogger.out.println("Start Here");
        	  
        } catch(Exception e) {
              e.printStackTrace(MessageLogger.out);
        }
        
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doGet(req,resp);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	resp.setContentType("text/html");
    	PrintWriter out = resp.getWriter();

    	
    	String command = req.getParameter("command");
    	MessageLogger.out.println("request url = "+req.getRequestURL()+" "+command);
    	if(command != null) {
    		if(command.equals("startComponent")) {
    			if(startComponent())
    				returnMessage(out,"OK - Started application");
    			else
    				returnMessage(out,"FAIL - No context exists");
    		} else if(command.equals("startMonitor")) {
    			if(startMonitor(req.getParameter("contextPath"))) 
    				returnMessage(out,"OK - Start monitoring");
    			else
    				returnMessage(out,"FAIL - fail to start monitoring");
    		}
    	} else {
    		
    		MessageLogger.out.println("Message Logger Start Here");
    		returnMessage(out,inspection());
    	}
    }
    
    private String inspection() {
    	if(st == null || url == null)
    		return "Status : sleeping";
    	else
    		return "Status : monitoring Component at "+contextPath+"<BR>"+
			       "         local file:"+url.getFile()+"<BR>"+
			       "         times crashed:"+crashCount;
    		
    }
    
    private void returnMessage(PrintWriter out,String sMessage) {
    	out.println(sMessage);
    }
    
    private boolean startMonitor(String contextPath) {
    	
    	
    	ServletContext context = getServletContext().getContext(contextPath);
    	MessageLogger.out.println("Start Monitoring");
    	if(context != null) {
    	   url = (URL)context.getAttribute("classURL");
    	   monitoredPoint = (String)context.getAttribute("MonitoredPoint");
    	   bindingKey = (String)context.getAttribute("BindingKey");
    	   serviceKey = (String)context.getAttribute("ServiceKey");
    	   
    	   MessageLogger.out.println("monitoredPoint = "+monitoredPoint);
    	   
    	   this.contextPath = contextPath;
    	
    	   st = new ServiceStarter(url);
    	   
    	   MessageLogger.out.println("Monitored Point Today = "+monitoredPoint);
    	   MonitorTask task = new MonitorTask(this,st,monitoredPoint);
    	   task.setBindingKey(bindingKey);
    	   task.setServiceKey(serviceKey);
    	   MessageLogger.out.println("binding Key = "+bindingKey + " Service Key =  "+serviceKey);
    	   timer.schedule(task, 0, TIME2START);
    	   return true;
    	}
    	return false;
    }
    
    public boolean startComponent() {
    	return st.restartServlet();
    	//return st.startServlet();
    }
    
    public void addCrashCount() {
    	crashCount ++;
    }
}
