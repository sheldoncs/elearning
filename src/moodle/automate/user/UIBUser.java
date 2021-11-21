package moodle.automate.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.util.AsciiStringRebuild;
import blackboard.util.MessageLogger;
import blackboard.util.MoodleLoadOptions;
import blackboard.util.Padding;
import blackboard.util.UploadExcel;
import blackboard.util.UserEnrol;
import moodle.uib.automate.UIB;
import transfer.email.SendMail;


public class UIBUser extends UIB {

	
	private HttpURLConnection connection;
	private URL serverAddress;
	private DocumentBuilder builder;
	private Document doc;
	private String userType;
	private OracleDBConnect db;
	private MySQLConnection connect;
    private UploadExcel excel;	
    private ArrayList<ExcelUser> eulist = new ArrayList<ExcelUser>();
    private HashMap<String, String> hashThink = new HashMap<String, String>();
	public UIBUser (){
		super();
		
	}
	public void setOracleConnect(OracleDBConnect db){
		this.db = db;
	}
	public void setMySQLCOnnect(MySQLConnection connect){
		this.connect = connect;
	}
	public void setUserType(String s){
		this.userType=s;
	}
	public void getDocument(String semester) throws Exception {
		
//		MessageLogger.setErr(new PrintStream(new FileOutputStream(new File("C:\\temp\\upload\\logs\\adduser.log"))));
//	   	MessageLogger.setOut(new PrintStream(new FileOutputStream(new File("C:\\temp\\upload\\logs\\adduser.log"))));
		
		ArrayList tempList = new ArrayList();
		Iterator tempIterator = null;
		int cnt = 0;
		int total = 0;
		int filecnt = 0;
		ArrayList list = new ArrayList();
		
		MessageLogger.out.println("Adding user...");
		HashMap hmap = connect.returnMoodleObjects();
		db.setMoodleObjects(hmap);
		db.setAction(action);
		MoodleLoadOptions mlo = connect.getLoadOptions();
		mlo.setStudent(1);
		mlo.setLecturer(0);
//		hashThink = db.getEthinkUsers(hashThink);
		if (mlo.getStudent() == 1 && mlo.getLecturer() == 0){
			  /**Remove Comments when testing is completed**/
			  list = this.generateMPTStudentShell();
//			  list = db.gatherStudentUsers();
			    /*
			    excel = new UploadExcel("c:/temp/","fiveisland2");
			    excel.setupWorkBook();
			    excel.iterateSheet();
			    list = excel.getList();*/
			   
		} else if (mlo.getLecturer() == 1 && mlo.getStudent() == 0){
			    /*excel = new UploadExcel("c:/temp/","fiveisland2");
			    excel.setupWorkBook();
			    excel.iterateSheet();
			    list = excel.getList();*/
//				list = db.gatherCALecturerUsers();	
				list = db.gatherNOTCALecturerUsers(list);	
		} else if (mlo.getLecturer() == 1 && mlo.getStudent() == 1){
		
				list = new ArrayList();
				
				ArrayList<UserEnrol> studentList = db.gatherStudentUsers();
				Iterator<UserEnrol> studentIterator = studentList.iterator();
				while (studentIterator.hasNext()){
					UserEnrol enrol = (UserEnrol)studentIterator.next();
					list.add(enrol);
				}
				
//				ArrayList lectList = db.gatherCALecturerUsers();	
				ArrayList lectList = new ArrayList();
				lectList = db.gatherNOTCALecturerUsers(lectList);	
				
				Iterator lectIterator = lectList.iterator();
				while (lectIterator.hasNext()){
					UserEnrol enrol = (UserEnrol)lectIterator.next();
					list.add(enrol);
				}
				
//				MessageLogger.out.println("List Size of Student = "+studentList.size());
				MessageLogger.out.println("List Size of Lecturer = "+lectList.size());
				MessageLogger.out.println("List Size of both Lecturer and Student = "+list.size());
			
		}
		
		ArrayList fieldlist = connect.getMoodleFieldList("user");
		
		
		try {
		 
		   builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		   
		   Iterator iterator = list.iterator();
		   MessageLogger.out.println("List Size =" + list.size());
		   
		   for  (cnt=0; cnt<= 1000; cnt++){
			   doc = builder.newDocument();
			  Element rootElement = doc.createElement("data");
			  
			  
			  
			  
			  while (tempList.size() < 2000){
				  if (iterator.hasNext()){
					  UserEnrol uEnrol = (UserEnrol)iterator.next();
					  
				    tempList.add(uEnrol);
				    total++;
				    
				    if (total == list.size()){
				      
					  break;
				    }
				  }
			  }
			  
			  
			  tempIterator = tempList.iterator();
		      System.out.println("temp List = "+ tempList.size());
			  while (tempIterator.hasNext()){
			   
				  Element datumElement = doc.createElement("datum");
			      rootElement.appendChild(datumElement);
			      UserEnrol userEnrol = (UserEnrol)tempIterator.next(); 
			      
			      datumElement.setAttribute("action", userEnrol.getAction());
			      Iterator miterator = fieldlist.iterator();
			      ExcelUser eu = new ExcelUser();
			      while (miterator.hasNext()){ 
			      
			    	  String val = miterator.next().toString();
			    	  
			    	  if (userEnrol.getAction().equals("create")){	 
			    	 
			    		  Element mappingElement = doc.createElement("mapping");
				          rootElement.appendChild( mappingElement);
				          mappingElement.setAttribute("name",val);
				          mappingElement.appendChild(doc.createTextNode(getFieldValue(val,userEnrol,eu)));
				          datumElement.appendChild(mappingElement);
				          
			    	  } else if ((userEnrol.getAction().equals("delete") && val.equals("username"))){
			    		  
			    		 Element mappingElement = doc.createElement("mapping");
					     rootElement.appendChild( mappingElement);
					     mappingElement.setAttribute("name",val);
					     
					     mappingElement.appendChild(doc.createTextNode(getFieldValue(val,userEnrol,eu)));
					     datumElement.appendChild(mappingElement);
			    	  
			    	  } else if (userEnrol.getAction().equals("update")){
			    	  
			    		  if (userEnrol.getId().equals("400004601"))
								System.out.println();
							
			    		  
							if (val.equals("suspended")) {
								
								Element mappingElement = doc.createElement("mapping");
								rootElement.appendChild(mappingElement);
								mappingElement.setAttribute("name", val);
								
								

								mappingElement.appendChild(doc.createTextNode(getFieldValue(val,userEnrol,eu)));

								datumElement.appendChild(mappingElement);
								
								if (userEnrol.getSuspend() == 1) {
									
									SendMail sm = new SendMail(userEnrol.getEmail(), "");
									
									if (!connect.isSuspended(userEnrol.getId())){
										connect.saveSuspended(userEnrol.getId());
										sm.setStatus("c");
										
										/***********************Send Email to Student temporarily blocked**************/
										//sm.send(userEnrol.getId(),userEnrol.getFirstName(),userEnrol.getLastName());
										/***********************Send Email to Student temporarily blocked**************/
									}

								}  else if (userEnrol.getSuspend() == 0) {
									
									SendMail sm = new SendMail(userEnrol.getEmail(), "");
									
									if (connect.isSuspended(userEnrol.getId())){
										sm.setStatus("r");
										connect.removeSuspended(userEnrol.getId());
										
										/***********************Send Email to Student temporarily blocked**************/
										//sm.send(userEnrol.getId(),userEnrol.getFirstName(),userEnrol.getLastName());
										/***********************Send Email to Student temporarily blocked**************/
										
										
									}
									
									
								}
								
							} else if (val.equals("username")) {
									Element mappingElement = doc.createElement("mapping");
									rootElement.appendChild(mappingElement);
									mappingElement.setAttribute("name", val);
									
									mappingElement.appendChild(doc.createTextNode(getFieldValue(val,userEnrol,eu)));
									datumElement.appendChild(mappingElement);
							 } else  if ((!val.equals("username"))) {
								 Element mappingElement = doc.createElement("mapping");
						          rootElement.appendChild( mappingElement);
						          mappingElement.setAttribute("name",val);
						          mappingElement.appendChild(doc.createTextNode(getFieldValue(val,userEnrol,eu)));
						          datumElement.appendChild(mappingElement);	 
							 }
			    	  }
				  
			      }
			      
			      eulist.add(eu);
		      }
			 
//				  Iterator<ExcelUser> ii = eulist.iterator();
//				  while (ii.hasNext())
//				  {
//					  ExcelUser eu = ii.next();
//					  if (!hashThink.containsKey(eu.getUsername())) {
//		    	        db.insertIntoEthinkUsers(eu.getUsername(), eu.getFirstname(), eu.getLastname(), eu.getEmail());
//		    	        hashThink.put(eu.getUsername().trim(), eu.getUsername().trim());
//					  }
//				  }
//				  eulist.clear();
      			  ExcelUser.usingDataOutputStream(eulist);
			  
			
		    	  tempList.clear();
//			  
//		      doc.appendChild(rootElement);
//		      filecnt++;
			  
		      //docStr = convertXMLFileToString(doc, "c:\\temp\\user" +".xml");
		      //docStr = convertXMLFileToString(doc, "d:\\temp\\user" +".xml");
//		      docStr = convertXMLFileToString(doc, "c:\\temp\\user" +filecnt +".xml");
		      //docStr = convertXMLFileToString(doc, "c:\\tmp\\user" +".xml");
//		      setIsHandle(true);
		      //setParameters("get_user", "00683111");
		      
//		      docStr = docStr.trim();
		      /**/
		     //sendWebServiceXML("https://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/user.php","handle",null);
		     //sendWebServiceXML("https://myelearningcavehilldev.mrooms3.net/blocks/conduit/webservices/rest/user.php","handle",null);
		     
//		      doc = null;
//		      
//		      MessageLogger.out.println("total = "+total+" "+"List Size = " +list.size()+" Counter = "+cnt);	
//		      
		      if (total == list.size()){ 
				  break;
			   }
		      
		      }
		      
		}
		
		catch(ParserConfigurationException ex){
			   db.closeConnection();
			   connect.closeConnection();
			   crash.crashXRun("UIB Connection Has Crashed ...Parser Configuration ...");
			   SendMail sm = new SendMail("sheldon.spencer@gmail.com","User server Server Shutdown ... " + "Parser Configuration");
			   sm.setStatus("b");
			   sm.send("","","");
			   ex.printStackTrace();
			   
		}catch (Exception e){
			crash.crashXRun("User Exception ...");
			SendMail sm = new SendMail("sheldon.spencer@gmail.com","User Exception ...Moodle User Crash");
			
			sm.send("","","");
			e.printStackTrace();
			
		}
		MessageLogger.out.println("Adding User Completed...");
		//closeConnections();
	}
	public void closeConnections(){
		db.closeConnection();
	    connect.closeConnection();
	}
	private String getFieldValue(String field, UserEnrol userEnrol,ExcelUser eu){
          
		  AsciiStringRebuild asr = new AsciiStringRebuild(); 
          String fieldValue  = "";
         
          if (field.equals("city")){
        	  
        	  if (!userEnrol.getAction().equals("delete")){
        	     if (userEnrol.getCity() == null){
        		  fieldValue = "Bridgetown";
        	     } else {
        		    fieldValue = userEnrol.getCity();  
        	     }
        	     fieldValue = asr.rebuildString(fieldValue);
        	  }
        	  
          } else if (field.equals("country")){
        	  
        	  if (!userEnrol.getAction().equals("delete")){
        	    if ( userEnrol.getCountry() == null){
        		  fieldValue = "Barbados";
        	    } else {
        		  fieldValue = userEnrol.getCountry();  
        	    }
        	    fieldValue = fieldValue.substring(0, 2);
        	    fieldValue = asr.rebuildString(fieldValue);
        	  }
        	  
          } else if (field.equals("email")){
        	 
        	  if (!userEnrol.getAction().equals("delete")){
        	    if (userEnrol.getEmail() == null){
        		  fieldValue = "Email not present";
        	    } else {
        	      fieldValue = userEnrol.getEmail();
        	    }
        	    fieldValue = asr.rebuildString(fieldValue);
        	  }
        	  eu.setEmail(fieldValue);
          } else if (field.equals("idnumber")){
        	  if (!userEnrol.getAction().equals("delete")){
        	    //fieldValue = userEnrol.getId().toLowerCase();
        		fieldValue = userEnrol.getId();
        	    eu.setUsername(fieldValue);
        	  }
          } else if (field.equals("firstname")){
        	  if (!userEnrol.getAction().equals("delete")){
        		  
        		  fieldValue = userEnrol.getFirstName();
        		  
        		  fieldValue = asr.rebuildString(fieldValue);
        		  
        	  }
        	  eu.setFirstname(fieldValue);
          }else if (field.equals("lastname")){
        	  if (!userEnrol.getAction().equals("delete")){
        		  fieldValue = userEnrol.getLastName();
        		  fieldValue = asr.rebuildString(fieldValue);
        	  }
        	  eu.setLastname(fieldValue);
          }else if (field.equals("username")){
        	  fieldValue = userEnrol.getId().toLowerCase();
        	  eu.setUsername(fieldValue);
          }else if (field.equals("password")){
        	  if (!userEnrol.getAction().equals("delete")){
        	    fieldValue = userEnrol.getPassword();
        	  }
        	  eu.setPassword(fieldValue);
          }else if (field.equals("auth")){
        	  if (!userEnrol.getAction().equals("delete")){
        	    fieldValue = userEnrol.getAuth();
        	    fieldValue = asr.rebuildString(fieldValue);
        	  }
        	  eu.setAuth("ldap");
          } else if (field.equals("suspended")){
        	  if (!userEnrol.getAction().equals("delete")){
        		  fieldValue = Integer.toString(userEnrol.getSuspend());
        		  asr.rebuildString(fieldValue);
        	  }
        	  eu.setSuspended(0);
          } 
          
         
          return fieldValue;

    }
	//MPT01 – MPT250  
	private ArrayList<UserEnrol> generateELPTStudentShell(){
		
		ArrayList<UserEnrol> list = new ArrayList<UserEnrol>();
		Padding pad = new Padding();
		HashMap<Integer,Integer> hmap = new HashMap<Integer,Integer>();
		
		for (int i =1; i<601;i++) {
			String paddedNum = pad.lPad(Integer.toString(i),"0",3);
			UserEnrol user = new UserEnrol();
			user.setId("ELPT"+paddedNum);
			user.setLastName("ELPT"+paddedNum);
			user.setFirstName("ELPT"+paddedNum);
			user.setEmail("ELPT"+paddedNum+"@cavehill.uwi.edu");
			user.setCountry("Barbados");
			user.setCity("Bridgetown");
			user.setAuth("manual");
			
			int random=0;
			String rnd="";
			while (true) {
			  random = 1-((int)Math.round((Math.random())*(1-601)));
//			  rnd = pad.lPad(Integer.toString(random),"0",4);
				  if (!hmap.containsKey(random)) {
				    break;
				  }
			}
			
			user.setPassword("ELPT"+random);
			hmap.put(random,random);
			user.setAction("create");
			user.setSuspend(0);
			list.add(user);
		}
		
		return list;
	}
private ArrayList<UserEnrol> generateMPTStudentShell(){
		
		ArrayList<UserEnrol> list = new ArrayList<UserEnrol>();
		Padding pad = new Padding();
		HashMap<Integer,Integer> hmap = new HashMap<Integer,Integer>();
		
		for (int i =251; i<=350;i++) {
			String paddedNum = pad.lPad(Integer.toString(i),"0",3);
			UserEnrol user = new UserEnrol();
			user.setId("MPT"+paddedNum);
			user.setLastName("MPT"+paddedNum);
			user.setFirstName("MPT"+paddedNum);
			user.setEmail("MPT"+paddedNum+"@cavehill.uwi.edu");
			user.setCountry("Barbados");
			user.setCity("Bridgetown");
			user.setAuth("manual");
			
			int random=0;
			String rnd="";
			while (true) {
			  random = 251-((int)Math.round((Math.random())*(251-350)));
//			  rnd = pad.lPad(Integer.toString(random),"0",4);
				  if (!hmap.containsKey(random)) {
				    break;
				  }
			}
			
			user.setPassword("MPT"+random);
			hmap.put(random,random);
			user.setAction("create");
			user.setSuspend(0);
			list.add(user);
		}
		
		return list;
	}

    public static void main(String[] args) throws Exception {
		
    	
    	 OracleDBConnect db = new OracleDBConnect();
	     MySQLConnection connect = new MySQLConnection();
	     
	     //connect.connectMoodleDbTest();
	     connect.connectMoodleDb();
	    
	     
    	 UIBUser uibUser = new UIBUser();
    	 uibUser.setOracleConnect(db);
    	 uibUser.setMySQLCOnnect(connect);
    	//uibUser.setAction("update");
 		 //uibUser.getDocument();
 		
    	  
 		uibUser.setAction("create");
 		//uibUser.startDocumentRead();
	     
     
 		//uibUser.setAction("delete");
		//uibUser.startDocumentRead(args[0]);
 		uibUser.startDocumentRead("202110");
 		uibUser.closeConnections();
 		
    	 //uibUser.setIsHandle(false);
    	 //uibUser.setParameters("get_user", "00683111");
   	     
    	 //uibUser.sendWebServiceXML("http://myelearning.cavehill.uwi.edu/uib/public/rest/enrol","get_user");
   	     
      	// DteFormatter df = new DteFormatter();
	    
	    
	    
		//UIBUser user = new UIBUser();
		//user.setOracleConnect(db);
		//user.setMySQLCOnnect(connect);
		//user.setUserType("student");
		//user.startDocumentRead();
		
		//user.setUserType("lecturer");
		//user.startDocumentRead();
		System.exit(0);
		//System.out.println(enrol.convertXMLFileToString(enrol.getDocument(), "d:\\temp\\enrol.xml"));
		//Document doc = course.getDocument(null);
		//doc.
		//System.out.println(doc.toString());
		
	}
}
