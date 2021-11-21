package blackboard.ioread.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import blackboard.db.enrollments.EnrollmentDb;
import blackboard.open.xml.LoadXML;
import blackboard.util.Enrollments;

public class ReadInEnrollmentXML extends LoadXML{

	private String xmlBeginLine;
	private String xmlBeginTag;
	private String xmlEndTag;
	private BufferedWriter out;
	private String xmlString=""; 
	private boolean groupOpen;
	private boolean isMember;
	private int relCnt;
	
	int cnt =0; 
	public ReadInEnrollmentXML(){
		super();
	}
	public void ReadIn(){
		try {
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
	            } else if (str.indexOf("<membership>") >= 0){
	            	groupOpen = true;
	            	xmlString = xmlBeginLine+"\n"+xmlBeginTag+"\n"+str+"\n";
	            }else 
	        	if (str.indexOf("</membership>") >= 0){
	        		out.write(xmlString);
	        		out.write(str);
	        		out.newLine();
	        		out.write(xmlEndTag);
	        		out.close();
	        		parse();
	        		out = new BufferedWriter(new FileWriter("xml/part.xml", false));
	        		xmlString="";
	        		groupOpen = false;
	        		if (relCnt == 1){
	        			isMember = false;	
						relCnt = 0;
	        		}
	        	} else {
	        		
	        		if (groupOpen){
	        			
	        			xmlString = xmlString + str + "\n";
	        			
	        		}
	        		
	        	}
	        	
	        }
	        in.close();
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
    public void parse(){
    	NodeList textSourceList=null;
    	NodeList textIDList =null;
    	NodeList textShortUserIDList =null;
    	NodeList textlongDescriptionList = null;
    	NodeList textTermList = null;
    	NodeList textParentList  = null;
    	int sourceIdCnt = 0;
    	int sourceMemberCnt = 0;
    	ArrayList memberList = new ArrayList();
    	Enrollments assignenrollments = new Enrollments();
    	
    	
    	loadXML("xml/part.xml");
		doc.getDocumentElement ().normalize ();
		listOfPersons = doc.getElementsByTagName("group");
		//System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
        
		
		NodeList listOfSourcedid = doc.getElementsByTagName("sourcedid");
		
		sourceIdCnt = listOfSourcedid.getLength();
		
		for(int s=0; s< listOfSourcedid.getLength() ; s++){
			Node firstSourceDIDNode = listOfSourcedid.item(s);
			if (firstSourceDIDNode != null){
	              if(firstSourceDIDNode.getNodeType() == Node.ELEMENT_NODE){
	            	  
	            	  Element firstIDElement = (Element)firstSourceDIDNode;
	            	  
	            	  if (sourceIdCnt == listOfSourcedid.getLength()){
	            		
	            		sourceIdCnt--;  
	            	    NodeList IDList = firstIDElement.getElementsByTagName("id");
	            	    
	                    Element IDElement = (Element)IDList.item(0);
	                    textIDList = IDElement.getChildNodes();
	                   
	            	  }
	              }
			}
		}
		NodeList listOfMembers = doc.getElementsByTagName("member");
		
		sourceMemberCnt = listOfMembers.getLength();
		for(int s=0; s< listOfMembers.getLength() ; s++){
			
			Enrollments enrollments = new Enrollments();
			enrollments.setSectionID(((((Node)textIDList.item(0)).getNodeValue().trim().toString())));
			
			Node firstMemberNode = listOfMembers.item(s);
			if (firstMemberNode != null){ 
			   if(firstMemberNode.getNodeType() == Node.ELEMENT_NODE){
				
				   enrollments.setSectionID(((((Node)textIDList.item(0)).getNodeValue().trim().toString())));
				   Element firstMemberElement = (Element)firstMemberNode;
				   
				   NodeList shortMemberList = firstMemberElement.getElementsByTagName("userid");
				   
				   Element shortUserIDElement = (Element)shortMemberList.item(0);
				   textShortUserIDList = shortUserIDElement.getChildNodes();
				   enrollments.setLoginID(((Node)textShortUserIDList.item(0)).getNodeValue().trim().toString());
				   
				   enrollments.setRole(firstMemberElement.getElementsByTagName("role").item(0).getAttributes().item(0).getNodeValue());
				   
				   NodeList shortIDList = firstMemberElement.getElementsByTagName("id");
				   Element shortIDElement = (Element)shortIDList.item(0);
				   NodeList textUserIDList = shortIDElement.getChildNodes();
				   enrollments.setUserID(((Node)textUserIDList.item(0)).getNodeValue().trim().toString());
				   
				   NodeList shortStatusList = firstMemberElement.getElementsByTagName("status");
				   Element shortStatusElement = (Element)shortStatusList.item(0);
				   NodeList textUserStatusList = shortStatusElement.getChildNodes();
				   enrollments.setStatus(((Node)textUserStatusList.item(0)).getNodeValue().trim().toString());
				   
				   assignenrollments = enrollments;
				   memberList.add(enrollments);
				   
			   }
			   
			}
		}
		
		
        cnt++;
        
        Iterator iterate = memberList.iterator();
        
        while (iterate.hasNext()){
        	
        	Enrollments enrollment = (Enrollments)iterate.next();
        	EnrollmentDb db = new EnrollmentDb();
        	db.insertEnrollmentsFromXML(enrollment);
        	db.closeDb();
        	
        	System.out.println("User ID = " +enrollment.getUserID()+" Login ID = "+enrollment.getLoginID()+ " Section = "+enrollment.getSectionID()+ " Role = "+enrollment.getRole()+ " Status = " + enrollment.getStatus());
            
        }
       
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
		ReadInEnrollmentXML xml = new ReadInEnrollmentXML();
		
		xml.ReadIn();
	}
}
