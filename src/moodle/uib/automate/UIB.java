package moodle.uib.automate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import blackboard.db.MySQLConnection;
import blackboard.db.OracleDBConnect;
import blackboard.util.StudentEnrol;
import moodle.http.crash.Crash;
import transfer.email.SendMail;


public abstract class UIB {

	protected HttpURLConnection connection;
	protected URL serverAddress;
	private DocumentBuilder builder;
	private Document doc;
	protected String docStr;
	private String usertype;
	private String summary;
	protected Crash crash;
	protected boolean isHandle;
	protected String method;
	protected String value;
	protected String urlParameters;
	protected String xmlDoc;
	protected String elementValue;
	protected HashMap map = new HashMap();
	protected String idNumberValue;
	protected String Id;
	protected String crnValue;
	protected String action;
	protected OracleDBConnect db;
	protected MySQLConnection connect;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public UIB(){
		
	}
	public void setOracleConnect(OracleDBConnect db){
		this.db = db;
	}
	public void setMySQLCOnnect(MySQLConnection connect){
		this.connect = connect;
	}
	public void setIsHandle(boolean t){
		isHandle = t;
	}
	public void setParameters(String method, String value){
		this.method = method;
		this.value = value;
	}
    public void sendWebServiceXML(String webServiceurl, String handle, String params){
    	
		String token = "@las";
		 
		try{
			
			
			if (isHandle){
			    urlParameters  = "method=" + handle + "&token=" + token + "&xml="+URLEncoder.encode(docStr, "UTF-8");
			} else {
				
				urlParameters  = "token=" + token+"&method=" + handle + "value="+params;
			    
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
         DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
         wr.writeBytes (urlParameters);
         wr.flush ();
         wr.close ();
         
         //Get Response 
         //if (params.indexOf("20044051")>=0)
           //System.out.println("Parameters = " +params);
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
             xmlDoc=xmlDoc.replace("null", "");
             convertXMLString();
           }
         } else {
        	 
         }
         
         rd.close();
         connection.disconnect();
		}
		 catch(MalformedURLException ex){
			 crash.crashXRun("UIB Connection Has Crashed ...Malformed URL");
			 SendMail sm = new SendMail("sheldon.spencer@gmail.com","server Server Shutdown ... " + "Malformed URL");
			 sm.setStatus("b");
			 sm.send("","","");
			 ex.printStackTrace(); 
		 }
		 catch (IOException ioe){
			 System.out.println(ioe.getMessage());
			 ioe.printStackTrace();
			 crash.crashXRun("UIB Connection Has Crashed ...IO Exception ...");
			 SendMail sm = new SendMail("sheldon.spencer@gmail.com","server Server Shutdown ... " + "IO Exception");
			 sm.setStatus("b");
			 sm.send("","","");
			 
		 } catch (Exception e){
			 //crash.crashXRun("UIB Connection Has Crashed");
			 //SendMail sm = new SendMail("epayments@cavehill.uwi.edu","sheldon.spencer@gmail.com","server Server Shutdown ... " + "UIB Connection Has Crashed");
			 //sm.send();
			 e.printStackTrace();
		 }
	
	  
	
	}
    private void convertXMLString(){
    	
    	
    	
    	ByteArrayInputStream stream = new ByteArrayInputStream(xmlDoc.getBytes());
    	DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(stream);
			NodeList nodes = doc.getElementsByTagName("course");

			

			for (int i = 0; i < nodes.getLength(); i++) {

				Element element = (Element) nodes.item(i);

			      NodeList name = element.getElementsByTagName("fullname");
			     
			       for (int j = 0; j < name.getLength(); j++){
			    	  
			    	  Element elementFullName = (Element)name.item(j);
			    	  Node node = elementFullName.getFirstChild();
			    	  elementValue = node.getNodeValue().toString();
			    	   
			    	  if (elementValue.indexOf("MetaCourse") < 0){
			    	      //MessageLogger.out.println(Id +" " +elementValue);
			    	      crnValue = elementValue.substring(elementValue.indexOf("CRN: ") + 5, elementValue.length());
			    	  }
			    	  
			    	  
			      }
			      
			      NodeList idNumber = element.getElementsByTagName("idnumber");
			      
			      for (int k = 0; k < idNumber.getLength(); k++){
			    	  
			    	  Element elementIDNumber = (Element)idNumber.item(k);
			    	  Node node = elementIDNumber.getFirstChild();
			    	  
			    	  if (node != null){
			    	    if (node.getNodeValue().toString().indexOf("L00") < 0) 
			    	     idNumberValue = node.getNodeValue().toString();
			    	    else
			    	     idNumberValue = null;	  
			    	  } else{
			    		 idNumberValue = null;
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
		} catch (Exception e){
			e.printStackTrace();
		}
    	
    
    }
    public String getIDNumber(){
    	return idNumberValue;
    }
    public String getCRNValue(){
    	return crnValue;
    }
    public static String getCharacterDataFromElement(Element e) {
        Node child = (Node) e.getFirstChild();
        if (child instanceof CharacterData) {
          CharacterData cd = (CharacterData) child;
          return cd.getData();
        }
        return "";
      }

    public void setCrash(Crash crash){
    	this.crash = crash;
    }
    
    public void startDocumentRead(String semester) throws Exception{
    	getDocument(semester);
    }
    public void setSummary(String s){
    	summary=s;
    }
    public String getSummary(){
    	int cnt = 0;
    	HashMap sumChars = new HashMap();
    	
    	
    	
    	for(cnt=65; cnt<=90 ; cnt++){
    		char c = (char)cnt;
            String s = Character.toString(c);
            sumChars.put(new Integer(cnt), s);
    	}
        for(cnt=97; cnt<=122 ; cnt++){
    		char c = (char)cnt;
            String s = Character.toString(c);
            sumChars.put(new Integer(cnt), s);
    	}	
    	
        char sp = (char)32;
        String s = Character.toString(sp);
    	sumChars.put(new Integer(32),s);
    	
    	char colon = (char)58;
    	s = Character.toString(colon);
    	sumChars.put(new Integer(58),s);
    	
    	char semicolon = (char)59;
    	s = Character.toString(semicolon);
    	sumChars.put(new Integer(59),s); 
    	
    	//40,41
    	char smhyph = (char)45;
    	s = Character.toString(smhyph);
    	sumChars.put(new Integer(45),s);
    	
    	char lrhyph = (char)95;
    	s = Character.toString(lrhyph);
    	sumChars.put(new Integer(95),s);
    	
    	char obrack = (char)40;
    	s = Character.toString(obrack);
    	sumChars.put(new Integer(40),s);
    	
    	char cbrack = (char)41;
    	s = Character.toString(cbrack);
    	sumChars.put(new Integer(41),s);
    	
    	
    	
    	ArrayList letArray = new ArrayList();
    	int g = summary.length();
        String let =summary;
    	 
    	for (int i = 0; i <= summary.length()-1; i ++ ){
    	   
    		  
    		 String frst = let.substring(0, 1);
    		 let = let.substring(1, let.length());
    		 //System.out.println(let);
    		 
    	     if (!sumChars.containsValue(frst)){
    	    	
    	    	 letArray.add(let);
        		  
    	     }
    	}
    	
        
        Iterator it = letArray.iterator();
    	
        
        while (it.hasNext()){
        
        	try {
        	
        	String v = it.next().toString(); 
        	summary.replaceAll(v, " ");
        	
        	} catch (Exception ex){
        		
        		summary = "";
        		
        	}
        }
    	
        
        
        return summary;
    	
    }
    public String convertXMLFileToString(Document doc, String fileName) 
    { 
	  	
      try{ 
        /**
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
        InputStream inputStream = new FileInputStream(new File(fileName)); 
        doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream); 
        **/
    	PrintStream p = null;  
        StringWriter stw = new StringWriter(); 
        Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
        serializer.setOutputProperty("omit-xml-declaration", "yes");
        serializer.transform(new DOMSource(doc), new StreamResult(stw)); 
        
        
        OutputStream outputStream = new FileOutputStream(new File(fileName));
        p = new PrintStream( outputStream );
  
        p.println (stw.toString());

        p.close();

        
        return stw.toString(); 
      } 
      catch (Exception e) { 
        e.printStackTrace(); 
      } 
        return null; 
    }
    public String convertDocumentToString(Document doc, String fileName) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    public void createCSV (ArrayList<StudentEnrol> list) {
    	
    }
	public abstract void getDocument(String semester) throws Exception;
	 
	
}
