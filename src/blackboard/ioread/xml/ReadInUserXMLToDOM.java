package blackboard.ioread.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import blackboard.open.xml.LoadXML;
import blackboard.users.BlackboardUsers;
import blackboard.util.Users;

public class ReadInUserXMLToDOM extends LoadXML{

	private String xmlBeginLine;
	private String xmlBeginTag;
	private String xmlEndTag;
	private BufferedWriter out;
	private String xmlString=""; 
	int cnt =0; 
	public ReadInUserXMLToDOM(){
		super();
	}
	public void ReadIn(){
		try {
			BlackboardUsers bUsers = new BlackboardUsers();
	        BufferedReader in = new BufferedReader(new FileReader("xml/snapshot_export.xml"));
	        out = new BufferedWriter(new FileWriter("xml/part.xml", false));
	        String str;
	        while ((str = in.readLine()) != null) {
	            
	        	
	        	if (str.indexOf("?xml")>=0){
	        		xmlBeginLine = str;
	        		
	        	} else
	        	if (str.indexOf("<enterprise")>=0){
	        		xmlBeginTag = str;
	        		
	        		
	        		xmlEndTag = "</enterprise>";
	            } else
	        	if (str.indexOf("</person>") >= 0){
	        		out.write(xmlString);
	        		out.write(str);
	        		out.newLine();
	        		out.write(xmlEndTag);
	        		out.close();
	        		parse(bUsers);
	        		out = new BufferedWriter(new FileWriter("xml/part.xml", false));
	        		xmlString="";
	        	} else {
	        		if (xmlString.indexOf(xmlBeginLine) < 0){
	        			xmlString = xmlString + xmlBeginLine +"\n";
	        			
	        		}
	        		if (xmlString.indexOf(xmlBeginTag) < 0){
	        			xmlString = xmlString + xmlBeginTag + "\n";
	        		}
	        		xmlString = xmlString + str + "\n";
	        	}
	        	
	        }
	        in.close();
	        bUsers.closeDb();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }

	}
	public void parseXML(String str){
		 try {
		        
		        
		        out.write("aString");
		        out.close();
		    } catch (IOException e) {
		    }

	}
    public void parse(BlackboardUsers bUsers){
    	NodeList textSourceList=null;
    	NodeList textIDList =null;
    	NodeList textFNList=null;
    	NodeList  textFamilyList=null;
    	NodeList  textGivenList=null;
    	
    	loadXML("xml/part.xml");
		doc.getDocumentElement ().normalize ();
		listOfPersons = doc.getElementsByTagName("person");
		System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

		
		NodeList listOfSourcedid = doc.getElementsByTagName("sourcedid");
		for(int s=0; s< listOfSourcedid.getLength() ; s++){
			Node firstSourceDIDNode = listOfSourcedid.item(s);
			if (firstSourceDIDNode != null){
	              if(firstSourceDIDNode.getNodeType() == Node.ELEMENT_NODE){
	         
	            	  Element firstIDElement = (Element)firstSourceDIDNode;
	            	  
	            	  NodeList IDList = firstIDElement.getElementsByTagName("id");
	                  Element IDElement = (Element)IDList.item(0);
	                  textIDList = IDElement.getChildNodes();
	                  
	                  
	                  NodeList sourceList = firstIDElement.getElementsByTagName("source");
	                  Element sourceElement = (Element)sourceList.item(0);
	                  textSourceList = sourceElement.getChildNodes();
	                  
	              }
			}
		}
		NodeList listOfUserids = doc.getElementsByTagName("userid");
		Element useridElement = (Element)listOfUserids.item(0);
        NodeList textUserIdList = useridElement.getChildNodes();
        
        
        NodeList listOfNames = doc.getElementsByTagName("name");
        for(int s=0; s< listOfNames.getLength() ; s++){
        	Node firstNameNode = listOfNames.item(s);
        	if (firstNameNode != null){
        		if(firstNameNode.getNodeType() == Node.ELEMENT_NODE){
        			
        			Element firstNameElement = (Element)firstNameNode;
        			
        			NodeList fnList = firstNameElement.getElementsByTagName("fn");
	                Element fnElement = (Element)fnList.item(0);
	                textFNList = fnElement.getChildNodes();
	                
	                NodeList ListOfN = firstNameElement.getElementsByTagName("n");
	                for(int i=0; i< ListOfN.getLength() ; i++){
	                	Node nNode =  ListOfN.item(i);
	                	if (nNode != null){
	                		if(nNode.getNodeType() == Node.ELEMENT_NODE){
	                			Element nElement = (Element)nNode;	
	                			
	                			NodeList nList = nElement.getElementsByTagName("family");
	        	                Element familyElement = (Element)nList.item(0);
	        	                textFamilyList = familyElement.getChildNodes();
	        	                
	                			NodeList givenList = nElement.getElementsByTagName("given");
	        	                Element givenElement = (Element)givenList.item(0);
	        	                textGivenList = givenElement.getChildNodes();
	                			
	                		}
	                	}
	                	
	                	
	                }
        			
        		}
        		
        	}
        }
        cnt++;
        System.out.println("Count =" +cnt +" ID="+((Node)textIDList.item(0)).getNodeValue().trim().toString() +
        		 " Source="+((Node)textSourceList.item(0)).getNodeValue().trim().toString() + 
        		 " User Id = "+ ((Node)textUserIdList.item(0)).getNodeValue().trim().toString() +
        		 " Firstname = "+((Node)textFNList.item(0)).getNodeValue().trim().toString() +
        		 " Family = "+((Node)textFamilyList.item(0)).getNodeValue().trim().toString()+
        		 " Given = "+((Node)textGivenList.item(0)).getNodeValue().trim().toString());
        
        
        Users users = new Users();
        users.setLoginID(((Node)textUserIdList.item(0)).getNodeValue().trim().toString());
        users.setUniqueID(((Node)textIDList.item(0)).getNodeValue().trim().toString());
        users.setFirstname(((Node)textGivenList.item(0)).getNodeValue().trim().toString());
        users.setLastname(((Node)textFamilyList.item(0)).getNodeValue().trim().toString());
        bUsers.insertCurrentUsers(users);
        //bUsers.closeConnection();
        
    }
        
    public boolean locateChildID(){
    	return true;
    }
	
	public boolean locateParentID(){
		return true;
	}
	
	public void setBlackboardObject(Object cs){
		
	}
	
	public static void main(String[] args) 
	{
		ReadInUserXMLToDOM xml = new ReadInUserXMLToDOM();
		
		xml.ReadIn();
	}
}
