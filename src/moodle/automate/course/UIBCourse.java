package moodle.automate.course;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import moodle.uib.automate.UIB;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import transfer.email.SendMail;


import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.util.Course;
import blackboard.util.DateFormatter;
import blackboard.util.MessageLogger;
import blackboard.util.SetupCourse;


public class UIBCourse extends UIB {

	
	private HttpURLConnection connection;
	private URL serverAddress;
	private DocumentBuilder builder;
	private Document doc;
	private String usertype;
	private HashMap charMap = new HashMap();
	private String startDate;
	private OracleDBConnect db;
	private MySQLConnection connect;
	private ArrayList<ExcelCourse> elist = new ArrayList<ExcelCourse>();
	String[] courseDates;
	public UIBCourse(){
		super();
	}
	public void setOracleConnect(OracleDBConnect db){
		this.db = db;
	}
	public void setMySQLCOnnect(MySQLConnection connect){
		this.connect = connect;
	}
	public void getDocument(String semester) throws Exception {
		
		MessageLogger.setErr(new PrintStream(new FileOutputStream(new File("C:\\temp\\upload\\logs\\course.log"))));
	   	MessageLogger.setOut(new PrintStream(new FileOutputStream(new File("C:\\temp\\upload\\logs\\course.log"))));
	   	  
		ArrayList tempList = new ArrayList();
		Iterator tempIterator = null;
		int cnt = 0;
		int total = 0;
		int filecnt = 0;
		ArrayList list = null;
		
		courseDates = connect.returnCourseStartdate(db,semester);
		
		//startDate = "08/06/2020";
		
		SetupCourse sc = connect.returnCourseSettings();
		
		if (sc.isUpload()){
			  db.setUpload(sc.isUpload());
			  db.setCrseCode(sc.getCrseCode());
			  db.setCrseNumb(sc.getCrseNumb());
		}
		
		if (!courseDates[0].equals("")){
			
		
		HashMap hmap = connect.returnMoodleObjects();
		db.setMoodleObjects(hmap);
		
//		list = db.gatherCourse(semester);
		list = db.gatherExamShells(semester);
//		list = db.getNonAcademicCourse();
		//list = this.PushManualCourse();
		
		ArrayList fieldlist = connect.getMoodleFieldList("course");
		
		
		try {
		 
		   builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		   
		   Iterator iterator = list.iterator();
		   MessageLogger.out.println("List Size =" + list.size());
		   
		   if (connect.loadCourses()) {
			   //99
		    for  (cnt=0; cnt<=99 ; cnt++){
			   doc = builder.newDocument();
			  Element rootElement = doc.createElement("data");
			  
			 //2000
			  while (tempList.size() < 300){
				  if (iterator.hasNext()){
				    tempList.add((Course)iterator.next());
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
			      Course course = (Course)tempIterator.next(); 
			      
			      
			      
			      datumElement.setAttribute("action", course.getAction());
			      Iterator miterator = fieldlist.iterator();
			      ExcelCourse eCourse = new ExcelCourse();
			      while (miterator.hasNext()){ 
			    	  
			    	  String val = miterator.next().toString();
			    	  if (((course.getAction().equals("delete")) && val.equals("idnumber")) || (course.getAction().equals("create") || course.getAction().equals("update"))){
			    	    String s = "";	  
				         Element mappingElement = doc.createElement("mapping");
				         rootElement.appendChild( mappingElement);
				         mappingElement.setAttribute("name",val);
				     
				         String fieldValue = getFieldValue(val,course,eCourse,db);
				         MessageLogger.out.println(fieldValue);
				         
				         mappingElement.appendChild(doc.createTextNode(fieldValue));
				         
				         datumElement.appendChild(mappingElement);
			    	 }
			
			      }
			      elist.add(eCourse);
			      
		      }
			  ArrayList<ExcelCourse> tempArray = null; 
			  
//			  for (int j = 0; j < elist.size(); j++) {
//			    tempArray.add( elist.get(j));
//			  }
			  ExcelCourse.usingDataOutputStream(elist); 
			    tempList.clear();
			  
		        doc.appendChild(rootElement);
		        filecnt++;
		        
		       // docStr = convertXMLFileToString(doc, "c:\\tmp\\course.xml");
		        //docStr = convertXMLFileToString(doc, "c:\\temp\\course.xml");
		        this.setIsHandle(true);
		        //docStr = convertXMLFileToString(doc, "c:\\tmp\\course" +filecnt +".xml");
		        //docStr = convertXMLFileToString(doc, "c:\\temp\\course" +filecnt +".xml");
		        docStr = convertXMLFileToString(doc, "c:\\temp\\course" +filecnt +".xml");
		        
	
		        //sendWebServiceXML("https://myelearningcavehilldev.mrooms3.net/blocks/conduit/webservices/rest/course.php","handle",null); 
		       // sendWebServiceXML("https://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/course.php","handle",null); 
		        docStr = null;
		        doc = null;
		     
		        MessageLogger.out.println("total = "+total+" "+"List Size = " +list.size()+" Counter = "+cnt);	
		      
		        if (total == list.size()){ 
				  break;
			     }
		      
		       }
		    }
		    db.closeConnection();
		    connect.closeConnection();
		}
		catch(ParserConfigurationException ex){
			   db.closeConnection();
			   connect.closeConnection();
			   crash.crashXRun("UIB Connection Has Crashed ...Parser Configuration ...");
			   SendMail sm = new SendMail("sheldon.spencer@gmail.com","Course server Server Shutdown ... " + "Parser Configuration");
			   sm.setStatus("b");
			   sm.send("","","");
			   ex.printStackTrace();
			
		}
		
		} else {
			MessageLogger.out.println("Course semester and date do not yet exist!!");
			db.closeConnection();
		    connect.closeConnection();
		}
		MessageLogger.out.println("Completed");
	}
	public void setUserType(String s){
		
		usertype = s;
		
	}
	private String getFieldValue(String field, Course course, ExcelCourse eCourse,OracleDBConnect db){

		  String fieldValue  = "";
    
//          if (field.equals("templatecourse")){
//        	  fieldValue = course.getTermCode() + " - " +"COURXXX" + " - "  +  "Work-Life Balance II Exams"; 
//        	 
//        	  eCourse.setTemplateCourse(fieldValue);
//        	  
//          } else if (field.equals("category")){
		  if (field.equals("category")){
        	    String termdesc = course.getTermCode() +" - " +db.getTermDesc(course.getTermCode()).replaceAll("/", "-");
        	    if (termdesc.indexOf("Summer") > 0) {
        	    
        	      termdesc = course.getTermCode() +" - " + "Summer School" + " "+db.getTermDesc(course.getTermCode()).substring(0, db.getTermDesc(course.getTermCode()).lastIndexOf("Summer") - 1).replaceAll("/", "-"); 	
        	      
        	      
        	    } else {
        	    	String desc = db.getTermDesc(course.getTermCode()).replaceAll("/", "-");
        	    	if  (!course.getTermCode().equals("201905")){
        	    	   termdesc = course.getTermCode() +" - " +desc.substring(desc.indexOf("Semester"), desc.length()) + " "+ desc.substring(0, desc.indexOf("Semester"));
        	    	   termdesc = termdesc.trim();
        	    	} 
        	    	
        	    	
        	    }
        	    eCourse.setCategory(termdesc);
                fieldValue = termdesc;
                
          } else if (field.equals("enrollable")){
             
                fieldValue = "0";
                
                
          } else if (field.equals("fullname")){

        	   //MessageLogger.out.println(course.getCrseNumb()+" "+course.getSubjCode()+" "+course.getTermCode());
                if (!course.isHasConc()) {        
	        	    fieldValue = course.getTermCode()+" - "+course.getSubjCode() + course.getCrseNumb() + " - " +
	                             course.getCrseTitle().replaceAll("&", "and") ;
	                             
//	                             + " - " + "CRN: " +
//	                             course.getCrn();
                } else {
                	fieldValue = course.getTermCode()+" - "+course.getSubjCode() + course.getCrseNumb() + " - " +
                            course.getCrseTitle().replaceAll("&", "and") + " - "+ course.getConcName(); 
                            
//                            + " - " + "CRN: " +
//                            course.getCrn();
                }
        	    
        	    
        	    eCourse.setFullname(fieldValue);
          } else if (field.equals("guest")){
                fieldValue = "0";
          } else if (field.equals("idnumber")){
        	  
        	  if (!course.isHasConc()) {  
//                  fieldValue = course.getTermCode()+course.getSubjCode() + course.getCrseNumb() +"-" + course.getCampCode() +"-Exam";
                  fieldValue = course.getTermCode() + course.getSubjCode() +
                          course.getCrseNumb() + "-" + course.getSeqNumb();
        	  } else {
        		  fieldValue = course.getTermCode() + course.getSubjCode() +
                          course.getCrseNumb() + "-" 
                          +course.getConcCode()+"-"+ course.getSeqNumb();
        	  }
               eCourse.setIdnumber(fieldValue);
                
          } else if (field.equals("numsections")){
                fieldValue = "13";
          } else if (field.equals("shortname")){
        	  if (!course.isHasConc()) {  
//        		  202010MEDC4320-C-K06
//                fieldValue = course.getTermCode()+course.getSubjCode() + course.getCrseNumb() +"-" + course.getCampCode() + "-Exam";
        		  fieldValue = course.getSubjCode() + course.getCrseNumb() + "-" + course.getSeqNumb() + " - " + course.getTermCode();
        	    } else {
        	    	fieldValue = course.getSubjCode() + course.getCrseNumb() + "-" + course.getConcCode()+"-" + course.getSeqNumb() + " - " + course.getTermCode();
        	    }
                eCourse.setShortname(fieldValue);
          } else if (field.equals("startdate")){
                DateFormatter formatter = new DateFormatter();
                
        		try {
//                	SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy");
                	//startDate = course.getStartDate();
//                	Date dte = dateFormat.parse(startDate);
        		   
//        		    fieldValue = Long.toString(formatter.convertDateToLong(dte));
//        		    eCourse.setStartdate(fieldValue);
        		    eCourse.setStartdate(courseDates[0]);
//        		    Date dt = formatter.convertLongToDate(Long.parseLong(fieldValue));
        		  
        		} catch (Exception e){
        			e.printStackTrace();
        		}
          }
        else if  (field.equals("enddate")){
        		eCourse.setEnddate(courseDates[1]);
        		
          } else if (field.equals("summary")){
        	    
                if (db.gatherNarrative(course.getSubjCode(),course.getCrseNumb()) != null){
                    //fieldValue =  db.gatherNarrative(course.getSubjCode(),course.getCrseNumb()).replaceAll("&", "and");
                    //fieldValue = fieldValue.replaceAll("¿", "");
                    //fieldValue = fieldValue.replaceAll("¬", "");
                	//setSummary(db.gatherNarrative(course.getSubjCode(),course.getCrseNumb()));
                	//fieldValue = getSummary();
                	fieldValue = "";
                    
                } else {
                    fieldValue = " ";
                    
                }
          } else if (field.equals("visible")){
                fieldValue = "1";
                eCourse.setVisible(fieldValue);
          }

       
        return fieldValue;

    }
	private ArrayList<Course> PushManualCourse() {
	Course course = new Course();
	ArrayList<Course> list = new ArrayList<Course>();
	/*
	course.setCollDesc("CPDLL");
	course.setCrseNumb("114");
	course.setSubjCode("PDLL");
	course.setDeptDesc("CPDLL");
	course.setTermCode("201930");
	course.setCrn("30003");
	course.setSeqNumb("L01");
	course.setCrseTitle("Digital Marketing Short");
	course.setAction("create");
	list.add(course);
	
	course.setCollDesc("CPDLL");
	course.setCrseNumb("083");
	course.setSubjCode("PDLL");
	course.setDeptDesc("CPDLL");
	course.setTermCode("201930");
	course.setCrn("30011");
	course.setSeqNumb("L01");
	course.setCrseTitle("Supervisory Management Skills Short");
	course.setAction("create");
	list.add(course);
	*/
	course.setCollDesc("ELPT");
	course.setCrseNumb("2020");
	course.setSubjCode("ELPT");
	course.setDeptDesc("ELPT");
	course.setTermCode("201930");
	course.setCrn("ELPT20");
	course.setSeqNumb("L01");
	course.setCrseTitle("English Language Proficiency Test");
	course.setAction("create");
	list.add(course);
	return list;
}
	
	

    public static void main(String[] args) throws Exception {
     
      
   	  
    	MessageLogger.out.println("Begin Course Download ...");
    	OracleDBConnect db = new OracleDBConnect(); 
		MySQLConnection connect = new MySQLConnection();
		connect.connectMoodleDb();
    	 //PHYS1205
		UIBCourse crse = new UIBCourse();
		crse.setMySQLCOnnect(connect);
		crse.setOracleConnect(db);
//		crse.startDocumentRead(args[0]);
		crse.startDocumentRead("202030");
		MessageLogger.out.println("End Course Download ...");
//		System.exit(0);
//	    
//    	
//    	Integer[][] mCells = new Integer[6][7];
//    	for (int i = 0; i <mCells.length; i++){
//    		System.out.println(mCells[i].length);	
//    	}
    	
		
		
	}
}
