package moodle.automate.enrol;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import blackboard.util.Padding;
import blackboard.util.SetupCourse;
import blackboard.util.StudentEnrol;
import blackboard.util.StudentUploadExcel;
import moodle.automate.user.ExcelUser;
import moodle.uib.automate.UIB;
import oracle.net.aso.d;
import transfer.email.SendMail;


public class UIBEnrol extends UIB  {

	
	private HttpURLConnection connection;
	private URL serverAddress;
	private DocumentBuilder builder;
	private Document doc;
	private String usertype;
	private String loadType;
    private StudentUploadExcel excel;
    private String[] courseDates;
    
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	public OracleDBConnect getDBConnect(){
		return db;
	}
	public MySQLConnection getMySQLConnect(){
		return connect;
	}
	public void setUserType(String s){
		
		usertype = s;
		
	}
public void getDocument(String semester){
		
		ArrayList tempList = new ArrayList();
		Iterator tempIterator = null;
		int cnt = 0;
		int total = 0;
		int filecnt = 0;
		ArrayList list = null;
        loadType = "ClassRoom";
		
        courseDates = connect.returnCourseStartdate(db,semester);
		if (!courseDates[0].equals("")){
			
		
		MoodleLoadOptions mlo = connect.getLoadOptions();
		mlo.setLecturer(0);
		mlo.setStudent(1);
		HashMap hmap = connect.returnMoodleObjects();
		SetupCourse sc = connect.returnCourseSettings();
	
		if (sc.isUpload()){
		  db.setUpload(sc.isUpload());
		  db.setCrseCode(sc.getCrseCode());
		  db.setCrseNumb(sc.getCrseNumb());
		  db.setSeqNumb(sc.getSeqNumb());
		}
		
		db.setMoodleObjects(hmap);
		
		MessageLogger.out.println("Student = " + mlo.getStudent()+ " Lecturer = "+mlo.getLecturer());
		if (mlo.getStudent() == 1 && mlo.getLecturer() == 0){
			 
			    /*excel = new StudentUploadExcel("c:/temp/","fiveisland2");
			    excel.setupWorkBook();
			    excel.iterateSheet();
			    list = excel.getList();*/
			
//			list = db.gatherEnrollments();
            list = this.generateEnrolShell();
//            list = db.gatherExamShells(semester);
		} else if (mlo.getLecturer() == 1 && mlo.getStudent() == 0){
				list = db.gatherLectEnrollments();
		} else if (mlo.getLecturer() == 1 && mlo.getStudent() == 1){
			
			list = new ArrayList();
			
			
			ArrayList <StudentEnrol> studentList =  db.gatherEnrollments();
		
			Iterator studentIterator = studentList.iterator();
			while (studentIterator.hasNext()){
				StudentEnrol enrol = (StudentEnrol)studentIterator.next();
				enrol.setUserType("student");
				list.add(enrol);
				
			}
			int x = 0;
			x=5;
			ArrayList lectList = db.gatherLectEnrollments();
			Iterator lectIterator = lectList.iterator();
			while (lectIterator.hasNext()){
				StudentEnrol enrol = (StudentEnrol)lectIterator.next();
				enrol.setUserType("lecturer");
				
				list.add(enrol);
				
			}
			MessageLogger.out.println("List Size of Student = "+studentList.size());
			MessageLogger.out.println("List Size of Lecturer = "+lectList.size());
			MessageLogger.out.println("List Size of both Lecturer and Student = "+list.size());
		}
		
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
		      
			  HashMap<String, ExcelEnrol> excelEnrolList = new HashMap<String,ExcelEnrol>();
			  
			  while (tempIterator.hasNext()){
			   
				  Element datumElement = doc.createElement("datum");
			      rootElement.appendChild(datumElement);
			      StudentEnrol studEnrol = (StudentEnrol)tempIterator.next(); 
			      
			      datumElement.setAttribute("action", studEnrol.getAction());
			      Iterator miterator = fieldlist.iterator();
			      
			      ExcelEnrol excelEnrol = new ExcelEnrol();
			      excelEnrol.setAction("add");
			      
			      while (miterator.hasNext()){ 
			      
			    	  String val = miterator.next().toString();
			    	  
			    	 if (studEnrol.getAction() == null){
			    		 
			    		 System.out.println(studEnrol.getId());
			    		 
			    	 }
			    	 if (studEnrol.getAction().equals("add")) {
			    	   
			    	   Element mappingElement = doc.createElement("mapping");
				       rootElement.appendChild( mappingElement);
				       mappingElement.setAttribute("name",val);
				       mappingElement.appendChild(doc.createTextNode(getFieldValue(val,studEnrol, excelEnrol)));
				       datumElement.appendChild(mappingElement);
				       
				      
				       
			    	 } else if (studEnrol.getAction().equals("drop") && 
			    			             ((val.equals("course") || 
			    			                 (val.equals("username") || 
			    			                  	   (val.equals("role")))))){
			    		   
			    		   Element mappingElement = doc.createElement("mapping");
					       rootElement.appendChild( mappingElement);
					       mappingElement.setAttribute("name",val);
					       mappingElement.appendChild(doc.createTextNode(getFieldValue(val,studEnrol, excelEnrol)));
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
		     MessageLogger.out.println("success at this point");
		    //sendWebServiceXML("https://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/enroll.php","handle",null);
		     		     
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
			   crash.crashXRun("UIB Connection Has Crashed ...Parser Configuration ..."+ex.getMessage());
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
	private String getFieldValue(String field, StudentEnrol studEnrol, ExcelEnrol excelEnrol){

          String fieldValue  = "";
    
          if  (field.equals("course")){
        	  fieldValue = studEnrol.getTermCode() + studEnrol.getSubjCode() +
                      studEnrol.getCrseCode() + "-" + studEnrol.getSeqNumb();
             
        	  if (!loadType.equals("Exams")) {
	               
        	  } else if (loadType.equals("Exams")) {
        		  fieldValue = studEnrol.getTermCode() + studEnrol.getSubjCode() +
                          studEnrol.getCrseCode() + "-" + studEnrol.getCampusCode()+ "-Exams";
        	  }
             
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
    	getDocument(semester);
    }

public  ArrayList<StudentEnrol> generateEnrolShell(){
		
		ArrayList<StudentEnrol> list = new ArrayList<StudentEnrol>();
		Padding pad = new Padding();
		HashMap<Integer,Integer> hmap = new HashMap<Integer,Integer>();
				
		for (int i =1; i<601;i++) {
			String paddedNum = pad.lPad(Integer.toString(i),"0",3);
			StudentEnrol enrol = new StudentEnrol();
            enrol.setTermCode("202020");			
			enrol.setSubjCode("ELPT");
			enrol.setCrseCode("2021");
			enrol.setCrn("ELPT21");
			enrol.setSeqNumb("L01");
			enrol.setUserType("student");
			enrol.setAction("add");
			enrol.setEmail("elpt"+paddedNum+"@cavehill.uwi.edu");
			enrol.setId("ELPT"+paddedNum);
			
			list.add(enrol);
			
		}
		
		return list;
	}

   public void outputCSV () throws IOException {
	   ArrayList<StudentEnrol> enrolList = generateEnrolShell();
	   String dataString = "";
	   FileWriter fileWriter = new FileWriter("c:/temp/upload/troy.csv");
	    PrintWriter printWriter = new PrintWriter(fileWriter,false);
	    dataString = "username,password";
	    printWriter.println(dataString);
	    
	    Iterator<StudentEnrol> ii = enrolList.iterator();
	    while (ii.hasNext()) {
	    	StudentEnrol ec = ii.next();
	    	dataString=ec.getId()+","+ec.getId();
	    	printWriter.println(dataString);
	    }
	    printWriter.close();
   }
    public static void main(String[] args) throws IOException {
		
		UIBEnrol enrol = new UIBEnrol();
		//enrol.outputCSV();
		
		enrol.setLoadType("");
		OracleDBConnect db = new OracleDBConnect();
	    MySQLConnection connect = new MySQLConnection();
	    connect.connectMoodleDb();
	    
	    enrol.setOracleConnect(db);
	    enrol.setMySQLCOnnect(connect);
		//enrol.setUserType("lecturer");
//		enrol.startDocumentRead(args[0]);
	    enrol.startDocumentRead("202030");
		//enrol.setUserType("lecturer");
		//enrol.startDocumentRead();
		
		System.exit(0);
		//System.out.println(enrol.convertXMLFileToString(enrol.getDocument(), "d:\\temp\\enrol.xml"));
		//Document doc = course.getDocument(null);
		//doc.
		//System.out.println(doc.toString());
		
	}
}
