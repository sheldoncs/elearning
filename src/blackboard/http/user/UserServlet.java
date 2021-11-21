package blackboard.http.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;

import moodle.http.crash.Shutdownable;
import moodle.http.monitor.ServiceStarter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import blackboard.remote.UserRemote;
import blackboard.util.MessageLogger;
import blackboard.util.MoodleUserTask;
import blackboard.util.RefreshingTask;
import blackboard.util.UserEnrol;

/*
 * Created on Sep 2, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserServlet extends JAXMServlet implements Shutdownable {
	
	private ArrayList list;
	private Timer leasingTimer;
	private Timer userTimer;
	private int interval;
	private String effTerm;
	private ServiceStarter sv;
	
	public void init(ServletConfig servletConfig) throws ServletException {
		
		super.init(servletConfig);
		interval = 60;
		
		Properties uddiProperties = (Properties)servletConfig.getServletContext().getAttribute("UDDIProperties");
		
		MessageLogger.out.println("Service Key  =" + uddiProperties.getProperty("MoodleServiceKey"));
		MessageLogger.out.println("MoodleBindingKey  =" + uddiProperties.getProperty("MoodleBindingKey"));
		
		//RefreshingTask rtask = new RefreshingTask(uddiProperties);
		//registerLeasingTask(rtask);
		
     	
        Properties endPoint = (Properties)servletConfig.getServletContext().getAttribute("EndPointProperties");
        /*
        URL url = this.getClass().getResource("/"+this.getClass().getName().replace('.','/')+".class");
        
		servletConfig.getServletContext().setAttribute("classURL",url);
		servletConfig.getServletContext().setAttribute("MonitoredPoint",endPoint.getProperty("AccessPoint"));
		servletConfig.getServletContext().setAttribute("BindingKey",uddiProperties.getProperty("MoodleBindingKey"));
		servletConfig.getServletContext().setAttribute("ServiceKey",uddiProperties.getProperty("MoodleServiceKey"));
		
		sv = new ServiceStarter(url);
		 
		sv.startMonitor(endPoint.getProperty("Monitor"),endPoint.getProperty("ContextPath"));
		*/
		
		MoodleUserTask task = new MoodleUserTask(effTerm, endPoint);
		
		task.setServiceKey(uddiProperties.getProperty("MoodleServiceKey"));
		task.setBindingKey(uddiProperties.getProperty("MoodleBindingKey"));
		
		task.setServer(this);
		
		userTimer = new Timer();
		userTimer.schedule(task,0,interval *  60 * 1000);
		
		//userTimer.scheduleAtFixedRate(task, firstTime, period)

	}
		
		
   protected Document performTask(String requestName,int id,Iterator parameter) {
		
	        UserRemote userRemote = new UserRemote();
	        HashMap hmParameter = parseParameter(parameter,requestName);
			try {
				   
				   
				 if (requestName.equals("getUsers")){
					 MessageLogger.out.println("==================="+requestName);
					 MessageLogger.out.println(hmParameter.get("crseNumb").toString() + " "+ hmParameter.get("subjCode").toString());
					 
					 list = userRemote.getUsers(hmParameter.get("crseNumb").toString(), hmParameter.get("subjCode").toString());
		             return serializeUserList(id, list);      
				 } if (requestName.equals("getUserEnrol")){
					 
					 MessageLogger.out.println("==================="+requestName);
					 MessageLogger.out.println(hmParameter.get("id").toString() );
					 
					 UserEnrol user = userRemote.getUserEnrol(hmParameter.get("id").toString());
					 return serialize(id, user); 
				 }
				  
			} catch(Exception e) {
				 // DataFormatException
				  e.printStackTrace(MessageLogger.out);
			}

			return null;
	}
	private HashMap parseParameter(Iterator parameter,String requestName) {
		
			HashMap hmParameter = new HashMap();
			
	        while (parameter.hasNext()) {
	        	SOAPElement subElement = (SOAPElement)parameter.next();
	        	Name subElementName = subElement.getElementName();
	       
	        	MessageLogger.out.println("ParameterName ==> "+subElementName.getLocalName());
	        	MessageLogger.out.println(subElement.getValue());
	        	
	        	if (requestName.equals("getUsers")){
	        	   if(subElementName.getLocalName().equals("String_1"))
	        	      hmParameter.put("crseNumb",subElement.getValue());
	        	   if (subElementName.getLocalName().equals("String_2"))
	        		   hmParameter.put("subjCode",subElement.getValue()); 
	        	}
	        	else if (requestName.equals("getUserEnrol")){
	        	  if(subElementName.getLocalName().equals("String_1"))
	        	    hmParameter.put("id",subElement.getValue());
	        	  
	        	}
	        	
	        	
	        }
	        
	        return hmParameter;
		}
		
	    private Document serializeUserList(int id, ArrayList list) throws Exception {
	    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    	Document doc = builder.newDocument();

	    	Element studentListElement = doc.createElement("anyType");
	    	doc.appendChild(studentListElement);
	    	studentListElement.setAttribute("id","ID"+id);
	    	studentListElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type","ns3" + ":arrayList");
	    	studentListElement.setAttribute(SOAP_ENC_PREFIX+":arrayType",XMLSCHEMA_PREFIX+ ":anyType[]");
	    	
	    	Iterator iterator = list.iterator();
	    	
	    	
	    	while (iterator.hasNext()){
	    	    Element studentInfoElement = doc.createElement("item");
	    	    studentListElement.appendChild(studentInfoElement);
	    	    studentInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", SERVICE_PREFIX + ":UserEnrol");  
	    	    
	    	    UserEnrol user = (UserEnrol)iterator.next();
	    	    
	    	    Element actionElement = doc.createElement("action");
	    	    studentInfoElement.appendChild(actionElement);
	    	    actionElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    actionElement.appendChild(doc.createTextNode(user.getAction()));
	    	    
	    	    Element authElement = doc.createElement("auth");
	    	    studentInfoElement.appendChild(authElement);
	    	    authElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    authElement.appendChild(doc.createTextNode(user.getAuth()));
	    	    
	    	    Element cityElement = doc.createElement("city");
	    	    studentInfoElement.appendChild(cityElement);
	    	    cityElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    cityElement.appendChild(doc.createTextNode(user.getCity()));
	    	    
	    	    Element countryElement = doc.createElement("country");
	    	    studentInfoElement.appendChild(countryElement);
	    	    countryElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    countryElement.appendChild(doc.createTextNode(user.getCountry()));
	    	 
	    	    Element emailElement = doc.createElement("email");
	    	    studentInfoElement.appendChild(emailElement);
	    	    emailElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    emailElement.appendChild(doc.createTextNode(user.getEmail()));
	    	    
	    	    Element firstnameElement = doc.createElement("firstName");
	    	    studentInfoElement.appendChild(firstnameElement);
	    	    firstnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    firstnameElement.appendChild(doc.createTextNode(user.getFirstName()));
	    	
	    	    Element idElement = doc.createElement("id");
	    	    studentInfoElement.appendChild(idElement);
	    	    idElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    idElement.appendChild(doc.createTextNode(user.getId()));
	    	    
	    	    Element lastnameElement = doc.createElement("lastName");
	    	    studentInfoElement.appendChild(lastnameElement);
	    	    lastnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    lastnameElement.appendChild(doc.createTextNode(user.getLastName()));
	    	    
	    	    Element passwordElement = doc.createElement("password");
	    	    studentInfoElement.appendChild(passwordElement);
	    	    passwordElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    passwordElement.appendChild(doc.createTextNode(user.getPassword()));
	    	
	    	}
	    	
			
	    	return doc;
	    }
	    /*
	    private Document serializeUser(int id, ArrayList list) throws Exception {
	    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    	Document doc = builder.newDocument();

	    	Element studentListElement = doc.createElement("anyType");
	    	doc.appendChild(studentListElement);
	    	studentListElement.setAttribute("id","ID"+id);
	    	studentListElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type","ns2" + ":arrayList");
	    	studentListElement.setAttribute(SOAP_ENC_PREFIX+":arrayType",XMLSCHEMA_PREFIX+ ":anyType[]");
	    	
	    	Iterator iterator = list.iterator();
	    	
	    	while (iterator.hasNext()){
	    	    Element studentInfoElement = doc.createElement("item");
	    	    studentListElement.appendChild(studentInfoElement);
	    	    studentInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", SERVICE_PREFIX + ":CourseEnrol");  
	    	    
	    	    CourseEnrol crse = (CourseEnrol)iterator.next();
	    	    
	    	    Element idElement = doc.createElement("spridenid");
	    	    studentInfoElement.appendChild(idElement);
	    	    idElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    idElement.appendChild(doc.createTextNode(crse.getSpridenId()));
	    	    
	    	    Element lastnameElement = doc.createElement("subjCode");
	    	    studentInfoElement.appendChild(lastnameElement);
	    	    lastnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    lastnameElement.appendChild(doc.createTextNode(crse.getSubjCode()));
	    	    
	    	    Element firstnameElement = doc.createElement("crseCode");
	    	    studentInfoElement.appendChild(firstnameElement);
	    	    firstnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    firstnameElement.appendChild(doc.createTextNode(crse.getCrseCode()));
	    	 
	    	    Element initialElement = doc.createElement("crn");
	    	    studentInfoElement.appendChild(initialElement);
	    	    initialElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    initialElement.appendChild(doc.createTextNode(crse.getCrn()));
	    	    
	    	       
	    	}
	    	
			
	    	return doc;
	    }
	    */
	    private Document serialize(int id, UserEnrol user) throws Exception {
	    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    	Document doc = builder.newDocument();
	    	
	    	Element studentInfoElement = doc.createElement("userEnrol");
	    	doc.appendChild(studentInfoElement);
	    	studentInfoElement.setAttribute("id","ID"+id);
	    	studentInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", SERVICE_PREFIX + ":UserEnrol");
	    	/*
    	    Element authorElement = doc.createElement("author");
    	    bookInfoElement.appendChild(authorElement);
    	    authorElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    authorElement.appendChild(doc.createTextNode(obj.getAuthor()));
    	    
	    	Element categoryElement = doc.createElement("category");
    	    bookInfoElement.appendChild(categoryElement);
    	    categoryElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    categoryElement.appendChild(doc.createTextNode(obj.getCategory()));
    	    
    	    Element priceElement = doc.createElement("price");
    	    bookInfoElement.appendChild(priceElement);
    	    priceElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":double");
    	    priceElement.appendChild(doc.createTextNode(Double.toString(obj.getPrice())));
    	    
    	    Element titleElement = doc.createElement("title");
    	    bookInfoElement.appendChild(titleElement);
    	    titleElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    titleElement.appendChild(doc.createTextNode(obj.getTitle()));
    	    */
	    	
	    	Element actionElement = doc.createElement("action");
    	    studentInfoElement.appendChild(actionElement);
    	    actionElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    actionElement.appendChild(doc.createTextNode(user.getAction()));
    	    
    	    Element authElement = doc.createElement("auth");
    	    studentInfoElement.appendChild(authElement);
    	    authElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    authElement.appendChild(doc.createTextNode(user.getAuth()));
    	    
    	    Element cityElement = doc.createElement("city");
    	    studentInfoElement.appendChild(cityElement);
    	    cityElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    cityElement.appendChild(doc.createTextNode(user.getCity()));
    	    
    	    Element countryElement = doc.createElement("country");
    	    studentInfoElement.appendChild(countryElement);
    	    countryElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    countryElement.appendChild(doc.createTextNode(user.getCountry()));
    	 
    	    Element emailElement = doc.createElement("email");
    	    studentInfoElement.appendChild(emailElement);
    	    emailElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    emailElement.appendChild(doc.createTextNode(user.getEmail()));
    	    
    	    Element firstnameElement = doc.createElement("firstName");
    	    studentInfoElement.appendChild(firstnameElement);
    	    firstnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    firstnameElement.appendChild(doc.createTextNode(user.getFirstName()));
    	
    	    Element idElement = doc.createElement("id");
    	    studentInfoElement.appendChild(idElement);
    	    idElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    idElement.appendChild(doc.createTextNode(user.getId()));
    	    
    	    Element lastnameElement = doc.createElement("lastName");
    	    studentInfoElement.appendChild(lastnameElement);
    	    lastnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    lastnameElement.appendChild(doc.createTextNode(user.getLastName()));
    	    
    	    Element passwordElement = doc.createElement("password");
    	    studentInfoElement.appendChild(passwordElement);
    	    passwordElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
    	    passwordElement.appendChild(doc.createTextNode(user.getPassword()));
    	    
	    	return doc;

	    }
	    
}
