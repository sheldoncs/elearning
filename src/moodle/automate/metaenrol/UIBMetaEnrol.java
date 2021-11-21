package moodle.automate.metaenrol;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.util.MessageLogger;
import blackboard.util.MoodleLoadOptions;
import blackboard.util.SetupCourse;
import blackboard.util.StudentEnrol;
import moodle.automate.enrol.UIBEnrol;
import moodle.uib.automate.UIB;
import transfer.email.SendMail;

public class UIBMetaEnrol extends UIB {

	private HttpURLConnection connection;
	private URL serverAddress;
	private DocumentBuilder builder;
	private Document doc;
	private String usertype;
	private String loadType;
	private String[] courseDates;
	
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	@Override
	
	public void getDocument(String semester) throws Exception {
		// TODO Auto-generated method stub
		ArrayList tempList = new ArrayList();
		Iterator tempIterator = null;
		int cnt = 0;
		int total = 0;
		int filecnt = 0;
		ArrayList list = null;

		if (connect == null)
		 System.out.println();
		
		courseDates = connect.returnCourseStartdate(db,semester);
		if (!courseDates[0].equals("")){
			
		
		MoodleLoadOptions mlo = connect.getLoadOptions();
		HashMap hmap = connect.returnMoodleObjects();
		SetupCourse sc = connect.returnCourseSettings();
	
		if (sc.isUpload()){
		  db.setUpload(sc.isUpload());
		  db.setCrseCode(sc.getCrseCode());
		  db.setCrseNumb(sc.getCrseNumb());
		  db.setSeqNumb(sc.getSeqNumb());
		}
		
		db.setMoodleObjects(hmap);
		
		MessageLogger.out.println(" Lecturer = "+mlo.getLecturer());
		list = db.gatherLectEnrollments();
		
		int x = 0;
		x=5;
			
			
	   MessageLogger.out.println("List Size of both Lecturer  = "+list.size());
		
		
		ArrayList fieldlist = connect.getMoodleFieldList("enrol");
	
		try {
		 
		   builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		   
		   Iterator iterator = list.iterator();
		   System.out.println("List Size =" + list.size());
		   
		   for  (cnt=0; cnt<= 99; cnt++){
			   doc = builder.newDocument();
			  Element rootElement = doc.createElement("data");
			  
			  //500
			  while (tempList.size() < 2000){
				  if (iterator.hasNext()){
				    tempList.add((StudentEnrol)iterator.next());
				    total++;
				    
				    if (total == list.size()){
					  break;
				    }
				  }
			  }
			  
			  
			  tempIterator = tempList.iterator();
		      
			  while (tempIterator.hasNext()){
			   
				  Element datumElement = doc.createElement("datum");
			      rootElement.appendChild(datumElement);
			      StudentEnrol studEnrol = (StudentEnrol)tempIterator.next(); 
			      
			      datumElement.setAttribute("action", studEnrol.getAction());
			      Iterator miterator = fieldlist.iterator();
			   
			      while (miterator.hasNext()){ 
			      
			    	  String val = miterator.next().toString();
			    	  
			    	 if (studEnrol.getAction() == null){
			    		 
			    		 System.out.println(studEnrol.getId());
			    		 
			    	 }
			    	 if (studEnrol.getAction().equals("add")) {
				       
			    	   Element mappingElement = doc.createElement("mapping");
				       rootElement.appendChild( mappingElement);
				       mappingElement.setAttribute("name",val);
				       mappingElement.appendChild(doc.createTextNode(getFieldValue(val,studEnrol)));
				       datumElement.appendChild(mappingElement);
				       
			    	 } else if (studEnrol.getAction().equals("drop") && 
			    			             ((val.equals("course") || 
			    			                 (val.equals("username") || 
			    			                  	   (val.equals("role")))))){
			    		   
			    		   Element mappingElement = doc.createElement("mapping");
					       rootElement.appendChild( mappingElement);
					       mappingElement.setAttribute("name",val);
					       mappingElement.appendChild(doc.createTextNode(getFieldValue(val,studEnrol)));
					       datumElement.appendChild(mappingElement);
					       
			    	 }
			
			      }
			   
		      }
			  tempList.clear();
			  
		      doc.appendChild(rootElement);
		      filecnt++;
		      

		      //docStr = convertXMLFileToString(doc, "c:/temp/enrol.xml"); 
		      docStr = convertXMLFileToString(doc, "c:\\temp\\enrol" + filecnt +".xml");
		      
		     //docStr = convertXMLFileToString(doc, "c:\\tmp\\enrol" + filecnt +".xml");
		     //docStr = convertXMLFileToString(doc, "c:\\tmp\\enrol.xml");
		     docStr = docStr.trim(); 
		     this.setIsHandle(true);
		     sendWebServiceXML("http://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/enroll.php","handle",null);
		     		     
		     // sendWebServiceXML("https://myelearningcavehilldev.mrooms3.net/blocks/conduit/webservices/rest/enroll.php","handle",null); 
		     /*
		     StudentExportDb exportDb = new StudentExportDb("", "admin", "kentish");
				crash.setStudentExport(exportDb);
			    if (exportDb.getTestResult().equals("True"))   
			    	throw new ComponentCrashException("Component Has Crashed");
		      */
		     
		      docStr = null;
		      doc = null;
		     
		      MessageLogger.out.println("total = "+total+" "+"List Size = " +list.size()+" Counter = "+cnt);	
		      
		       if (total == list.size()){ 
				  break;
			   }
		      
		      }
		   
		      
		      db.closeConnection();
		      connect.closeConnection();
		}
		catch(Exception ex){
			   db.closeConnection();
			   connect.closeConnection(); 
			   crash.crashXRun("UIB Connection Has Crashed ...Parser Configuration ...");
			   SendMail sm = new SendMail("sheldon.spencer@gmail.com","Enrol server Server Shutdown ... " + "Parser Configuration");
			   sm.setStatus("b");
			   sm.send("","","");
			   ex.printStackTrace();
			   
		}
		
		} else {
			MessageLogger.out.println("Course semester and date do not yet exist, so enrollments cannot be created!!");
			db.closeConnection();
		    connect.closeConnection();
		}
	}
	private String getFieldValue(String field, StudentEnrol studEnrol){

        String fieldValue  = "";
  
        if  (field.equals("course")){
              
              fieldValue = studEnrol.getTermCode()+ studEnrol.getSubjCode() +
                                   studEnrol.getCrseCode() + "-" + "L00";
          
          
        } else if (field.equals("role")){
          
      	  fieldValue = studEnrol.getUserType();
      	  
        } else if (field.equals("username")){
          
      	fieldValue = studEnrol.getId();
          
		    
        }else if (field.equals("timestart")){
           fieldValue = "0";
        }else if (field.equals("timeend")){
           fieldValue = "0";
        }
      
        return fieldValue;

  }
	
  public void startDocumentRead(String semester){
  	try {
		getDocument(semester);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  public static void main(String[] args) {
		
		
		
		UIBMetaEnrol enrol = new UIBMetaEnrol();
		OracleDBConnect db = new OracleDBConnect();
	    MySQLConnection connect = new MySQLConnection();
	    connect.connectMoodleDb();
	    
	    enrol.setOracleConnect(db);

	    enrol.setMySQLCOnnect(connect);
		//enrol.setUserType("lecturer");
		enrol.startDocumentRead(args[0]);
		
		//enrol.setUserType("lecturer");
		//enrol.startDocumentRead();
		
		System.exit(0);
		//System.out.println(enrol.convertXMLFileToString(enrol.getDocument(), "d:\\temp\\enrol.xml"));
		//Document doc = course.getDocument(null);
		//doc.
		//System.out.println(doc.toString());
		
	}	
	

}
