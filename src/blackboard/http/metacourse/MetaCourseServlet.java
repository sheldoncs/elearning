package blackboard.http.metacourse;

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

import moodle.automate.enrol.UIBEnrol;
import moodle.http.monitor.ServiceStarter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import blackboard.remote.MoodleRemote;
import blackboard.util.MessageLogger;
import blackboard.util.MetaCourseTask;
import blackboard.util.MoodleCourseTask;
import blackboard.util.RefreshingTask;
import blackboard.util.StudentEnrol;

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
public class MetaCourseServlet extends JAXMServlet{
	
	private ArrayList list;
	private Timer leasingTimer;
	private Timer courseTimer;
	private int interval;
	private String effTerm;
	private String stuid;
	private String subjCode;
	private String crseNumb;
	private ServiceStarter sv;
	
	public void init(ServletConfig servletConfig) throws ServletException {
		
		super.init(servletConfig);
		interval = 60;
		
		/*
		Properties uddiProperties = (Properties)servletConfig.getServletContext().getAttribute("UDDIProperties");
		RefreshingTask rtask = new RefreshingTask(uddiProperties);
		registerLeasingTask(rtask);
		
        Properties endPoint = (Properties)servletConfig.getServletContext().getAttribute("EndPointProperties");
		
        URL url = this.getClass().getResource("/"+this.getClass().getName().replace('.','/')+".class");
		servletConfig.getServletContext().setAttribute("classURL",url);
		servletConfig.getServletContext().setAttribute("MonitoredPoint",endPoint.getProperty("AccessPoint"));

		sv = new ServiceStarter(url);
		 
		sv.startMonitor(endPoint.getProperty("Monitor"),endPoint.getProperty("ContextPath")); 
		*/
		MetaCourseTask task = new MetaCourseTask(effTerm, endPoint);
		
		//task.setServer(this);
		
		courseTimer = new Timer();
		courseTimer.schedule(task,0,interval *  60 * 1000);
        
	}
		
		
   protected Document performTask(String requestName,int id,Iterator parameter) {
		
	  
			try {
				
				HashMap hmap = parseParameter(parameter,requestName);
				
				if (requestName.equals("getStudentsEnrolledInCourse")){
					
					subjCode = hmap.get("subjCode").toString();
					crseNumb = hmap.get("crseNumb").toString();
					
					MessageLogger.out.println("Subject Code = " +subjCode+ " Course Number = "+crseNumb);
					
					MoodleRemote mRemote = new MoodleRemote();
					list = mRemote.getStudentsEnrolledInCourse(subjCode, crseNumb);
					
					
				} else if (requestName.equals("getCourseEnrollment")){
					stuid = hmap.get("id").toString(); 	
				}
				
				
				   
				 if (requestName.equals("getCourseEnrollment")){ 
		             return serializeCourseListOfStudents(id, list);      
				 } else if (requestName.equals("getStudentsEnrolledInCourse")){
					 return serializeStudentListOfCourses(id, list); 
				 }
				  //return serialize(id, rmi.getBookInfo());
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
	        	
	        	if (requestName.equals("getCourseEnrollment")){
	        	   if(subElementName.getLocalName().equals("String_1"))
	        	      hmParameter.put("id",subElement.getValue());
	        	}
	        	else if (requestName.equals("getStudentsEnrolledInCourse")){
	        	  if(subElementName.getLocalName().equals("String_1"))
	        	    hmParameter.put("subjCode",subElement.getValue());
	        	  if(subElementName.getLocalName().equals("String_2"))
		        	    hmParameter.put("crseNumb",subElement.getValue());
	        	}
	        	
	        	
	        }
	        
	        return hmParameter;
		}
		
	    private Document serializeStudentListOfCourses(int id, ArrayList list) throws Exception {
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
	    	    studentInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", SERVICE_PREFIX + ":StudentEnrol");  
	    	    
	    	    StudentEnrol stud = (StudentEnrol)iterator.next();
	    	    
	    	    Element actionElement = doc.createElement("action");
	    	    studentInfoElement.appendChild(actionElement);
	    	    actionElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    actionElement.appendChild(doc.createTextNode(stud.getAction()));
	    	    
	    	    Element crnElement = doc.createElement("crn");
	    	    studentInfoElement.appendChild(crnElement);
	    	    crnElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    crnElement.appendChild(doc.createTextNode(stud.getCrn()));
	    	    
	    	    Element crseElement = doc.createElement("crseCode");
	    	    studentInfoElement.appendChild(crseElement);
	    	    crseElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    crseElement.appendChild(doc.createTextNode(stud.getCrseCode()));
	    	    
	    	    Element firstnameElement = doc.createElement("firstname");
	    	    studentInfoElement.appendChild(firstnameElement);
	    	    firstnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    firstnameElement.appendChild(doc.createTextNode(stud.getFirstname()));
	    	    
	    	    Element idElement = doc.createElement("id");
	    	    studentInfoElement.appendChild(idElement);
	    	    idElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    idElement.appendChild(doc.createTextNode(stud.getId()));
	    	    
	    	    Element lastnameElement = doc.createElement("lastname");
	    	    studentInfoElement.appendChild(lastnameElement);
	    	    lastnameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    lastnameElement.appendChild(doc.createTextNode(stud.getLastname()));
	    	    
	    	    Element seqElement = doc.createElement("seqNumb");
	    	    studentInfoElement.appendChild(seqElement);
	    	    seqElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    lastnameElement.appendChild(doc.createTextNode(stud.getSeqNumb()));
	    	    
	    	    Element subjElement = doc.createElement("subjCode");
	    	    studentInfoElement.appendChild(subjElement);
	    	    subjElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    subjElement.appendChild(doc.createTextNode(stud.getSubjCode()));
	    	    
	    	    Element termElement = doc.createElement("termCode");
	    	    studentInfoElement.appendChild(termElement);
	    	    termElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    termElement.appendChild(doc.createTextNode(stud.getTermCode()));
	    	    
	    	}
	    	UIBEnrol enrol = new UIBEnrol();
	    	
	    	if (doc == null)
	    	  MessageLogger.out.println("doc is null");
	    	MessageLogger.out.println("Size of List = "+list.size());
	    	
	    	return doc;
	    }
	    private Document serializeCourseListOfStudents(int id, ArrayList list) throws Exception {
	    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    	Document doc = builder.newDocument();

	    	Element studentListElement = doc.createElement("anyType");
	    	doc.appendChild(studentListElement);
	    	studentListElement.setAttribute("id","ID"+id);
	    	studentListElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type","ns2" + ":arrayList");
	    	studentListElement.setAttribute(SOAP_ENC_PREFIX+":arrayType",XMLSCHEMA_PREFIX+ ":anyType[]");
	    	
	    	Iterator iterator = list.iterator();
	    	
	    	while (iterator.hasNext()){
	    	    Element studentInfoElement = doc.createElement("studentEnrol");
	    	    studentListElement.appendChild(studentInfoElement);
	    	    studentInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", SERVICE_PREFIX + ":StudentEnrol");  
	    	    
	    	    StudentEnrol stud = (StudentEnrol)iterator.next();
	    	    
	    	    
	    	    
	    	    Element idElement = doc.createElement("id");
	    	    studentInfoElement.appendChild(idElement);
	    	    idElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    idElement.appendChild(doc.createTextNode(stud.getId()));
	    	    
	    	    Element subjElement = doc.createElement("subjCode");
	    	    studentInfoElement.appendChild(subjElement);
	    	    subjElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    subjElement.appendChild(doc.createTextNode(stud.getSubjCode()));
	    	    
	    	    Element crseElement = doc.createElement("crseCode");
	    	    studentInfoElement.appendChild(crseElement);
	    	    crseElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    crseElement.appendChild(doc.createTextNode(stud.getCrseCode()));
	    	    
	    	    Element termElement = doc.createElement("termCode");
	    	    studentInfoElement.appendChild(termElement);
	    	    termElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    termElement.appendChild(doc.createTextNode(stud.getTermCode()));
	    	 
	    	    Element crnElement = doc.createElement("crn");
	    	    studentInfoElement.appendChild(crnElement);
	    	    crnElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    crnElement.appendChild(doc.createTextNode(stud.getCrn()));
	    	    
	    	    Element actionElement = doc.createElement("action");
	    	    studentInfoElement.appendChild(actionElement);
	    	    actionElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", XMLSCHEMA_PREFIX + ":string");
	    	    actionElement.appendChild(doc.createTextNode(stud.getAction()));
	    	       
	    	}
	    	
			
	    	return doc;
	    }
	    /*
	    private Document serialize(int id, StudentInfo obj) throws Exception {
	    	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    	Document doc = builder.newDocument();
	    	
	    	Element bookInfoElement = doc.createElement("bookInfo");
	    	doc.appendChild(bookInfoElement);
    	    bookInfoElement.setAttribute("id","ID"+id);
    	    bookInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX+":type", SERVICE_PREFIX + ":BookInfo");
	    	
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
    	    
    	    
    	    
	    	return doc;

	    }
	    */
}
