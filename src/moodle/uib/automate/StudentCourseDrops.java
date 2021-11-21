package moodle.uib.automate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import blackboard.db.OracleDBConnect;
import blackboard.util.StudentEnrol;
import blackboard.util.XMLBuilder;

public class StudentCourseDrops extends UIB {

	private boolean found = true;
	private String term;
	XMLBuilder build;
    private boolean documentCreated;
    private ArrayList moodleList;
    private String Id;
    
	public StudentCourseDrops(){
		super();
	}
	public void setCurrentTerm(String term){
		this.term=term;
	}
	public boolean docCreated(){
		return documentCreated;
	}
	public void setDocCreated(boolean flag){
		documentCreated=flag;
	}
	public void  setXMLBuilder(XMLBuilder build){
	 this.build = build;	
	}
	public void accessEnrollments(String id){
		
		xmlDoc = "";
		setIsHandle(false);
		this.Id=id;
	    String params = "&username="+id; 
	    
	    sendNewWebServiceXML("https://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/user.php","get_user_course_recent_activity",params);
	    
		
		
	}
	public void setDocumentString(String docStr){
		this.docStr = docStr;
	}
	public void getUser(String id){
		
		found = true;
		
		try {
		setIsHandle(false);
		xmlDoc = "";
		this.Id=id;
		String params = "&value="+id.trim(); 
		
		sendNewWebServiceXML("https://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/user.php","get_user",params);
		} catch (Exception ex){
			ex.printStackTrace();
			found = false;
			
		}
	    
	}
	public boolean userFound(){
		return found;
	}
	public void getDocument(String semester) throws Exception {
		// TODO Auto-generated method stub

	}
    public void sendNewWebServiceXML(String webServiceurl, String handle, String params){
    	
		String token = "@las";
		 
		try{
			
			
			if (isHandle){
			    urlParameters  = "method=" + handle + "&token=" + token + "&xml="+URLEncoder.encode(docStr, "UTF-8");
			} else {
				
				urlParameters  = "token=" + token+"&method=" + handle + params;
			    
			}
			
			serverAddress = new URL(webServiceurl);
			
		    connection = (HttpURLConnection)serverAddress.openConnection(); 
		   // crash.setUIBConnection(connection);
		    
		    connection.setRequestMethod("POST");
		    connection.setDoInput(true);
		    connection.setRequestProperty("Content-Length", "" + 
		         Integer.toString(urlParameters.getBytes().length));
		 connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		 

		 connection.setUseCaches (false);
         connection.setDoInput(true);
         connection.setDoOutput(true);
         
         //Send request
         DataOutputStream wr = new DataOutputStream (
         connection.getOutputStream ());
         wr.writeBytes (urlParameters);
         wr.flush ();
         wr.close ();
         
         //Get Response 
        
         InputStream is = connection.getInputStream();
         BufferedReader rd = new BufferedReader(new InputStreamReader(is));
         String line;
         StringBuffer response = new StringBuffer(); 
         
         while((line = rd.readLine()) != null) {
           
           xmlDoc = xmlDoc+line;
           
           
           
           response.append(line);
           response.append('\r');
           
         }
         
        
         
         if (!handle.equals("get_user")){
           if (!isHandle){
             xmlDoc = xmlDoc.substring(xmlDoc.indexOf("null")+1, xmlDoc.length());
             //convertXMLString();
             
           }
         } 
         rd.close();
         connection.disconnect();
		}
		 catch(MalformedURLException ex){
			 found=false;
			 connection.disconnect();
		 }
		 catch (IOException ioe){
			 found=false;
			 connection.disconnect();
		 } catch (Exception e){
			 found=false;
			 connection.disconnect();
		 }
	
	  
	
	}
    public String getResult(){
    	return xmlDoc;
    }
    public void convertXMLString(String userType){
    	
    	documentCreated = false;
    	boolean flag = false;
    	ByteArrayInputStream stream = new ByteArrayInputStream(xmlDoc.getBytes());
    	
    	DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(stream);
			NodeList nodes = doc.getElementsByTagName("course");

			moodleList = new ArrayList();

			for (int i = 0; i < nodes.getLength(); i++) {
				
				StudentEnrol enrol = new StudentEnrol();

				enrol.setId(Id);
				Element element = (Element) nodes.item(i);

			      NodeList name = element.getElementsByTagName("fullname");
			     
			       for (int j = 0; j < name.getLength(); j++){
			    	  
			    	  Element elementFullName = (Element)name.item(j);
			    	  Node node = elementFullName.getFirstChild();
			    	  elementValue = node.getNodeValue().toString();
			    	  
			    	  if (elementValue.indexOf("MDSC") >= 0)
			    		flag = true;
			    		  
			    	  
			    	  if (elementValue.indexOf("MetaCourse") < 0){
			    	      
			    	        crnValue = elementValue.substring(elementValue.indexOf("CRN: ") + 5, elementValue.length());
			    	        enrol.setCrn(crnValue);
			    		  
			    	  }
			    	  
			    	  
			      }
			      
			      NodeList idNumber = element.getElementsByTagName("idnumber");
			      
			      for (int k = 0; k < idNumber.getLength(); k++){
			    	  
			    	  Element elementIDNumber = (Element)idNumber.item(k);
			    	  Node node = elementIDNumber.getFirstChild();
			    	  
			    	  if (node != null){
			    	    if (node.getNodeValue().toString().indexOf("L00") < 0){ 
			    	     idNumberValue = node.getNodeValue().toString();
			    	     enrol.setCrseCode(idNumberValue);
			    	     
			    	    }
			    	    else
			    	     idNumberValue = null;	  
			    	  } else{
			    		 idNumberValue = null;
			    	  }
			    		  
			    	
			      }
			     
			      //if (elementValue.indexOf(term) >= 0){
			      if (elementValue.indexOf(term) < 0){
			    	  if (idNumberValue != null){
			    		  build.createDocument(Id, idNumberValue, userType);
			    		  documentCreated = true;
			    		 
			    	  } 
			      }
			      if ((idNumberValue != null) && (elementValue.indexOf(term) == 0)){
			    	  if (!flag){
			            moodleList.add(enrol);
			    	  }
			    	  else { 
			    		flag = false;
			    	  }
			      }
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
    }
    public ArrayList getMoodleCourseList(){
    	return moodleList;
    }
    public void resetMoodleCourseList(){
    	moodleList = null;
    }
	public boolean crnFound(String crn, String id){
		found=false;
		OracleDBConnect db = new OracleDBConnect();
		found = db.crnFound(crn, id);
		db.closeConnection();
		return found;
	}

}
