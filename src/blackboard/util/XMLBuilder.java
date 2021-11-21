package blackboard.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import moodle.automate.enrol.UIBEnrol;
import moodle.uib.automate.StudentCourseDrops;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLBuilder {

	DocumentBuilder builder;
	Document doc;
	Element rootElement;
	
	public XMLBuilder(){
		builder.newDocument();
	}
	public void clearDocument(){
	
	}
	
	public XMLBuilder(DocumentBuilder builder){
		
		this.builder=builder;
		
		 
	}
	public void initializeRoot(){
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.newDocument();
			rootElement = doc.createElement("data");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createDocument(String studentId, String courseId, String userType){
		
	
			
			Element datumElement = doc.createElement("datum");
		    rootElement.appendChild(datumElement);
		    datumElement.setAttribute("action", "delete");
	        
		    Element mappingCourseElement = doc.createElement("mapping");
		    datumElement.appendChild(mappingCourseElement);
		    mappingCourseElement.setAttribute("name","course");
		    mappingCourseElement.appendChild(doc.createTextNode(courseId));
		    
		    Element mappingRoleElement = doc.createElement("mapping");
		    datumElement.appendChild(mappingRoleElement);
		    mappingRoleElement.setAttribute("name","role");
		    mappingRoleElement.appendChild(doc.createTextNode(userType));
		    
		    Element mappingTimeEndElement = doc.createElement("mapping");
		    datumElement.appendChild(mappingTimeEndElement);
		    mappingTimeEndElement.setAttribute("name","timeend");
		    mappingTimeEndElement.appendChild(doc.createTextNode("0"));
		    
		    Element mappingTimeStartElement = doc.createElement("mapping");
		    datumElement.appendChild(mappingTimeStartElement);
		    mappingTimeStartElement.setAttribute("name","timestart");
		    mappingTimeStartElement.appendChild(doc.createTextNode("0"));
		    
		    Element mappingIDElement = doc.createElement("mapping");
		    datumElement.appendChild(mappingIDElement);
		    mappingIDElement.setAttribute("name","username");
		    mappingIDElement.appendChild(doc.createTextNode(studentId));
		    
		
	}
	public void appendRoot()  {
		
		try {
		
		doc.appendChild(rootElement);
		UIBEnrol enrol = new UIBEnrol();
		String docStr = enrol.convertXMLFileToString(doc, "c:/temp/drop.xml");
		docStr = docStr.trim();
		
		
		StudentCourseDrops drops = new StudentCourseDrops();
		drops.setIsHandle(true);
		drops.setDocumentString(docStr);
		
		
		
		drops.sendWebServiceXML("http://myelearning.cavehill.uwi.edu/blocks/conduit/webservices/rest/enroll.php", "handle", null);
		//drops.sendWebServiceXML("https://myelearningcavehilldev.mrooms3.net/blocks/conduit/webservices/rest/enroll.php", "handle", null);
		MessageLogger.out.println(docStr);
		MessageLogger.out.println(drops.getResult());
		MessageLogger.out.println();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		//com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transform
	}
	public void convertXMLString(){
		
		//filename is filepath string
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("d:/temp/drop.xml")));
			String line;
			StringBuilder sb = new StringBuilder();

			while((line=br.readLine())!= null){
			    sb.append(line.trim());
			}
			StudentCourseDrops drops = new StudentCourseDrops();
			drops.setIsHandle(true);
			drops.setDocumentString(sb.toString());
			drops.sendWebServiceXML("http://myelearning.cavehill.uwi.edu/uib/public/rest/enrol", "handle", null);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws RemoteException,SQLException {
		/*
		OracleDBConnect db = new OracleDBConnect();
		db.dropEnrollments();
		db.closeConnection();
		System.exit(0);*/
		
		XMLBuilder builder = new XMLBuilder();
		builder.convertXMLString();
		System.exit(0);
	}
	
}
