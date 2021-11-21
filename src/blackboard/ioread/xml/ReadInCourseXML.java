package blackboard.ioread.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import blackboard.courses.BlackboardCourses;
import blackboard.open.xml.LoadXML;
import blackboard.util.Courses;
import blackboard.util.Users;

public class ReadInCourseXML extends LoadXML{

	private String xmlBeginLine;
	private String xmlBeginTag;
	private String xmlEndTag;
	private BufferedWriter out;
	private String xmlString=""; 
	private boolean groupOpen;
	private boolean isRelationship;
	private int relCnt;
	
	int cnt =0; 
	public ReadInCourseXML(){
		super();
	}
	public void ReadIn(){
		try {
	        BufferedReader in = new BufferedReader(new FileReader("xml/snapshot_export.xml"));
	        out = new BufferedWriter(new FileWriter("xml/part.xml", false));
	        String str;
	        while ((str = in.readLine()) != null) {
	            
	        	if (str.indexOf("URN:X-WEBCT-VISTA-V1:59234606-7f00-0001-0176-6d9c38fcf10d") >= 0){
	        		System.out.println();
	        	}
	        	if (str.indexOf("?xml")>=0){
	        		xmlBeginLine = str;
	        		
	        	} else
	        	if (str.indexOf("<enterprise")>=0){
	        		xmlBeginTag = str;
	        		
	        		
	        		xmlEndTag = "</enterprise>";
	            } else if (str.indexOf("<group>") >= 0){
	            	groupOpen = true;
	            	xmlString = xmlBeginLine+"\n"+xmlBeginTag+"\n"+str+"\n";
	            }else 
	        	if (str.indexOf("</group>") >= 0){
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
	        			isRelationship = false;	
						relCnt = 0;
	        		}
	        	} else {
	        		
	        		if (groupOpen){
	        			
	        			
	        			if (str.indexOf("<relationship") >= 0){
	        			   isRelationship = true;
	        			   relCnt++;
	        			} 
	        			
	        			if (isRelationship){
	        				if (str.indexOf("<id>") >= 0){
	        					
	        					if (relCnt == 1){
	        					 str = str.replaceAll("id", "courserelationid");
	        					 
	        					} else if (relCnt == 2){
	        						str = str.replaceAll("id", "termrelationid");
	        						isRelationship = false;	
	        						relCnt = 0;
	        					}
	        					
	        				    
	        				}
	        				
	        					
	        			}	
	        				
	        			
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
    	NodeList textShortDescriptionList =null;
    	NodeList textlongDescriptionList = null;
    	NodeList textTermList = null;
    	NodeList textParentList  = null;
    	int sourceIdCnt = 0;
    	int sourceRelationshipCnt = 0;
    	
    	Courses courses = new Courses();
    	
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
	                    courses.setUniqueID((((Node)textIDList.item(0)).getNodeValue().trim().toString()));
	            	  
	            	  
	                    NodeList sourceList = firstIDElement.getElementsByTagName("source");
	                    Element sourceElement = (Element)sourceList.item(0);
	                    textSourceList = sourceElement.getChildNodes();
	            	  }
	              }
			}
		}
		NodeList listOfdescription = doc.getElementsByTagName("description");
		for(int s=0; s< listOfdescription.getLength() ; s++){
			Node firstDescriptionNode = listOfdescription.item(s);
			if (firstDescriptionNode != null){ 
			   if(firstDescriptionNode.getNodeType() == Node.ELEMENT_NODE){
				
				   Element firstDescriptionElement = (Element)firstDescriptionNode;
				   
				   NodeList shortDescriptionList = firstDescriptionElement.getElementsByTagName("short");
				   Element shortDescriptionElement = (Element)shortDescriptionList.item(0);
				   textShortDescriptionList = shortDescriptionElement.getChildNodes();
				   courses.setShortDesc(((Node)textShortDescriptionList.item(0)).getNodeValue().trim().toString());
				   
				   if (firstDescriptionElement.hasAttribute("long")){
				     NodeList longDescriptionList = firstDescriptionElement.getElementsByTagName("long");
				     Element longDescriptionElement = (Element)longDescriptionList.item(0);
				     textlongDescriptionList = longDescriptionElement.getChildNodes();
				     courses.setLongDesc(((Node)textlongDescriptionList.item(0)).getNodeValue().trim().toString());
				   }
			   }
			   
			}
		}
		
		
		
		NodeList listOfRelationship = doc.getElementsByTagName("relationship");
		
		
		
		sourceRelationshipCnt = listOfRelationship.getLength();
		System.out.println("relationship count"+sourceRelationshipCnt);
		for(int s=0; s< listOfRelationship.getLength() ; s++){
			Node firstRelationshipNode = listOfRelationship.item(s);
			if (firstRelationshipNode != null){ 
				
				if(firstRelationshipNode.getNodeType() == Node.ELEMENT_NODE){
					
					
					if (sourceRelationshipCnt == listOfRelationship.getLength()){
					  
					  sourceRelationshipCnt--;	
					  Element firstSourceElement = (Element)firstRelationshipNode;
					
					  NodeList sourceList = firstSourceElement.getElementsByTagName("source");
					  Element sourceElement = (Element)sourceList.item(0);
					  textSourceList = sourceElement.getChildNodes();
					
					  NodeList parentidList = firstSourceElement.getElementsByTagName("courserelationid");
					  Element parentidElement = (Element)parentidList.item(0);
					  textParentList = parentidElement.getChildNodes();
					  courses.setParentID(((Node)textParentList.item(0)).getNodeValue().trim().toString());
					
					}
					
				}
				
			}
		}
		NodeList listOfRelationship1 = doc.getElementsByTagName("relationship");
		sourceRelationshipCnt = listOfRelationship1.getLength();
		for(int s=0; s< listOfRelationship1.getLength() ; s++){
			Node firstRelationshipNode1 = listOfRelationship1.item(s);
			if (firstRelationshipNode1 != null){ 
				
				if(firstRelationshipNode1.getNodeType() == Node.ELEMENT_NODE){
					
					if (listOfRelationship1.getLength()>1){			
				    	if (sourceRelationshipCnt == 1){
						
						
					    courses.setlcTypeID("S");
					    Element firstSourceElement = (Element)firstRelationshipNode1;
					
					    NodeList sourceList = firstSourceElement.getElementsByTagName("source");
					    Element sourceElement = (Element)sourceList.item(0);
					    textSourceList = sourceElement.getChildNodes();
					
					    NodeList relidList = firstSourceElement.getElementsByTagName("termrelationid");
					    Element relidElement = (Element)relidList.item(0);
					    textTermList = relidElement.getChildNodes();
					    courses.setTerm(((Node)textTermList.item(0)).getNodeValue().trim().toString()); 
					 }
				  }
				}
				sourceRelationshipCnt--;
				
			}
		}
		if (courses.getlcTypeID() == null){
			courses.setlcTypeID("C");
		}
        cnt++;
       // if (courses.getlcTypeID().equals("S")){
        System.out.println("Count =" +cnt +" ID="+courses.getUniqueID() +
        	" Short Desc= "+ courses.getShortDesc() + " Long Description = " + courses.getLongDesc() +
        		 " Parent ID = " +courses.getParentID() + " Term = "+ courses.getTerm() + " Learning Context Type = " + courses.getlcTypeID()); 
       // }		 
        BlackboardCourses bCourses = new BlackboardCourses();
        
       //courses.setUniqueID(((Node)textIDList.item(0)).getNodeValue().trim().toString());
        bCourses.insertCurrentCourses(courses);
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
		ReadInCourseXML xml = new ReadInCourseXML();
		
		xml.ReadIn();
	}
}
