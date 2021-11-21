package blackboard.open.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import blackboard.util.Courses;
import blackboard.util.Users;

public abstract class LoadXML {

	
	protected DocumentBuilderFactory docBuilderFactory;
	protected DocumentBuilder docBuilder;
	protected Document doc;
	protected NodeList listOfGroups;
	protected NodeList listOfPersons;
	protected Courses courses;
	protected Users users;
	
	public LoadXML(){
		
		
        
		//ParseConfigurationException
	}
	public void loadXML(String XMLPath){
		try {
			
			docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse (new File(XMLPath));
        
            
		} catch (ParserConfigurationException pce){
			
		} catch (SAXParseException err){
			System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(" " + err.getMessage ());
		}catch (IOException ioe){
			ioe.printStackTrace();
		} 
		catch (SAXException e){
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		}
	}
	public void parseXML(){
		
		doc.getDocumentElement ().normalize ();
		System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

		listOfGroups = doc.getElementsByTagName("group");
        int totalPersons = listOfGroups.getLength();
        //System.out.println("Total no of groups : " + totalPersons);
	}
	
	public abstract boolean locateChildID();
	
	public abstract boolean locateParentID();
	
	public abstract void setBlackboardObject(Object cs);
	
	public static void main (String argv []){
		
	}
}
