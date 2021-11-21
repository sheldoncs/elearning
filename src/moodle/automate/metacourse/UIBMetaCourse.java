package moodle.automate.metacourse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import moodle.automate.course.ExcelCourse;
import moodle.http.crash.Crash;
import moodle.uib.automate.UIB;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.util.Course;
import blackboard.util.DateFormatter;
import blackboard.util.MessageLogger;

public class UIBMetaCourse extends UIB {
	
	private HttpURLConnection connection;
	private URL serverAddress;
	private DocumentBuilder builder;
	private Document doc;
	private String usertype;
	private HashMap charMap = new HashMap();
	private String startDate;
	private String loadType; 
	private OracleDBConnect db;
	private String termDesc;
	private String[] courseDates;
	private ArrayList<ExcelCourse> elist = new ArrayList<ExcelCourse>();
	
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	public void getDocument(String semester) throws IOException {
		// TODO Auto-generated method stub
		ArrayList tempList = new ArrayList();
		Iterator tempIterator = null;
		int cnt = 0;
		int total = 0;
		int filecnt = 0;
		ArrayList list = null;
		db = new OracleDBConnect(); 
		
		
		
		MySQLConnection connect = new MySQLConnection();
		connect.connectMoodleDb();
		courseDates = connect.returnCourseStartdate(db,semester);
		
		
		if (!courseDates[0].equals("")){
			
		
		
		HashMap hmap = connect.returnMoodleObjects();
		db.setMoodleObjects(hmap);
		
		if (loadType.equals("MetaCourse")){
		     list = db.gatherMetaCourse();
		} else {
			 list = db.gatherExamShells(semester);
		}
		ArrayList fieldlist = connect.getMoodleFieldList("course");
		
		
		try {
		 
		   builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		   
		   Iterator iterator = list.iterator();
		   System.out.println("List Size =" + list.size());
		   
		   if (connect.loadCourses()) {
			   
		    for  (cnt=0; cnt<=1000 ; cnt++){
			   doc = builder.newDocument();
			  Element rootElement = doc.createElement("data");
			  
			 
			  while (tempList.size() < 2000){
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
			      
		         	      
			      //course.setAction("update");
			      datumElement.setAttribute("action", course.getAction());
			      Iterator miterator = fieldlist.iterator();
			      ExcelCourse eCourse = new ExcelCourse();
			      while (miterator.hasNext()){ 
			    	  String val = miterator.next().toString();
			    	  if (((course.getAction().equals("remove")) && val.equals("shortname")) || (course.getAction().equals("create") || course.getAction().equals("change"))){
			    		  
				         Element mappingElement = doc.createElement("mapping");
				         rootElement.appendChild( mappingElement);
				     
				         mappingElement.setAttribute("name",val);
				     
				         
				         mappingElement.appendChild(doc.createTextNode(getFieldValue(val,course,eCourse,db)));
				         
				         datumElement.appendChild(mappingElement);
			    	 }
			
			      }
			      elist.add(eCourse);
		      }
			  ExcelCourse.usingDataOutputStream(elist); 
			  tempList.clear();
			    
		        doc.appendChild(rootElement);
		        filecnt++;
		        
		        //docStr = convertXMLFileToString(doc, "c:\\tmp\\course.xml");
		        //docStr = convertXMLFileToString(doc, "c:\\temp\\course.xml");
		        
		        docStr = convertXMLFileToString(doc, "c:\\temp\\metacourse" +".xml");
		        this.setIsHandle(true);
		        
		        crash = new Crash();
//		        sendWebServiceXML("http://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/course.php","handle",null);
		        //sendWebServiceXML("https://myelearningcavehilldev.mrooms3.net/blocks/conduit/webservices/rest/course.php","handle",null); 
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
			   ex.printStackTrace();
		}
		
		} else {
			MessageLogger.out.println("Course semester and date do not yet exist!!");
			db.closeConnection();
		    connect.closeConnection();
		}

	}
    public void setUserType(String s){
		
		usertype = s;
		
	}
	private String getFieldValue(String field, Course course, ExcelCourse eCourse,OracleDBConnect db){

		String fieldValue  = "";
       
        if (field.equals("category")){
      	    String termdesc = course.getTermCode() +" - " +db.getTermDesc(course.getTermCode()).replaceAll("/", "-");
      	    if (termdesc.indexOf("Summer") > 0) {
      	    
      	      termdesc = course.getTermCode() +" - " + "Summer School" + " "+db.getTermDesc(course.getTermCode()).substring(0, db.getTermDesc(course.getTermCode()).lastIndexOf("Summer") - 1).replaceAll("/", "-"); 	
      	      
      	      
      	    } else {
      	    	
      	    	String desc = db.getTermDesc(course.getTermCode()).replaceAll("/", "-");
      	    	termdesc = course.getTermCode() +" - " +desc.substring(desc.indexOf("Semester"), desc.length()) + " "+ desc.substring(0, desc.indexOf("Semester")) + " MetaCourse";
      	    	termdesc = termdesc.trim();
      	    	
      	    }
      	    
              fieldValue = termdesc;
              eCourse.setCategory("1466");            
             
        } else if (field.equals("enrollable")){
           
              fieldValue = "0";
        } else if (field.equals("fullname")){

        	if (loadType.equals("MetaCourse")){
	        	 MessageLogger.out.println(course.getCrseNumb()+" "+course.getSubjCode()+" "+course.getTermCode()); 
	      	    fieldValue = course.getTermCode()+" - "+course.getSubjCode() + course.getCrseNumb() + " - " +
	                           course.getCrseTitle().replaceAll("&", "and") + " MetaCourse" + " - " + "L00";
        	} else {
        		fieldValue = course.getTermCode()+" - "+course.getSubjCode() + course.getCrseNumb() + " - " +
                        course.getCrseTitle().replaceAll("&", "and") + " Exams";
        	}
      	    
      	    eCourse.setFullname(fieldValue);
        } else if (field.equals("guest")){
              fieldValue = "0";
        } else if (field.equals("idnumber")){
        	if (loadType.equals("MetaCourse")){
	              fieldValue = course.getTermCode() + course.getSubjCode() +
	                             course.getCrseNumb() + "-" + "L00";
        	} else {
        		 fieldValue = course.getTermCode() + course.getSubjCode() +
                         course.getCrseNumb() + "-Exams";
        	}
        	   eCourse.setIdnumber(fieldValue);
        } else if (field.equals("numsections")){
              fieldValue = "13";
        } else if (field.equals("shortname")){
//        	if (loadType.equals("MetaCourse")){
//              fieldValue = course.getSubjCode() + course.getCrseNumb() + "-" + "L00" + " - " + course.getTermCode();
//        	} else {
//        		fieldValue = course.getSubjCode() + course.getCrseNumb() + "-" + course.getTermCode()+ "-Exams";
//        	}
        	if (loadType.equals("MetaCourse")){
	              fieldValue = course.getTermCode() + course.getSubjCode() +
	                             course.getCrseNumb() + "-" + "L00";
      	   } else {
      		 fieldValue = course.getTermCode() + course.getSubjCode() +
                       course.getCrseNumb() + "-Exams";
      	  }
        	eCourse.setShortname(fieldValue);
        } else if (field.equals("startdate")){
        	DateFormatter formatter = new DateFormatter();
            
    		try {
//            	SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
//            	Date dte = dateFormat.parse(startDate);
    		    //fieldValue = Long.toString(formatter.getIntDate(dte));
    		    //Date dt = formatter.convertLongToDate(Long.parseLong(fieldValue));
    		    fieldValue = courseDates[0];
    		    eCourse.setStartdate(courseDates[0].replace("/", "-"));
//    		    Date dt = formatter.convertLongToDate(Long.parseLong(fieldValue));
//    		    System.out.println(dt);
    		    
      		} catch (Exception e){
      			e.printStackTrace();
      		}
      		
        } 
        else if  (field.equals("enddate")){
    		eCourse.setEnddate(courseDates[1].replace("/", "-"));
       } 
        else if (field.equals("summary")){
      	    
              //if (db.gatherNarrative(course.getSubjCode(),course.getCrseNumb()) != null){
                //  fieldValue =  db.gatherNarrative(course.getSubjCode(),course.getCrseNumb()).replaceAll("&", "and");
                // fieldValue = fieldValue.replaceAll("¿", "");
                //  fieldValue = fieldValue.replaceAll("¬", "");
                

                  
              //} else {
                  fieldValue = " ";
                 
              //}
        } else if (field.equals("visible")){
              fieldValue = "1";
              eCourse.setVisible(fieldValue);
        }
  
      
      return fieldValue;

     }
	
	 public static void main(String[] args) throws Exception {
			
	    	UIBMetaCourse crse = new UIBMetaCourse();
	    	crse.setLoadType("MetaCourse");
			crse.startDocumentRead("202010");
			System.exit(0);
		    
	}
}
