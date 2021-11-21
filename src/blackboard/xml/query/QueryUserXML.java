package blackboard.xml.query;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import blackboard.open.xml.LoadXML;
import blackboard.util.Courses;
import blackboard.util.Users;

public class QueryUserXML extends LoadXML {

	public QueryUserXML(){
		super();
	}
	public boolean locateChildID(){
		boolean located = false;
		
		
		NodeList listOfSourcedid = doc.getElementsByTagName("sourcedid");
		System.out.println("Number of Sourcedids = " +listOfSourcedid.getLength());
		for(int s=0; s< listOfSourcedid.getLength() ; s++){
			
			Node firstSourceDIDNode = listOfGroups.item(s);
            
			if (firstSourceDIDNode != null){
              if(firstSourceDIDNode.getNodeType() == Node.ELEMENT_NODE){
            	Element firstIDElement = (Element)firstSourceDIDNode; 

                //-------
                NodeList IDList = firstIDElement.getElementsByTagName("id");
                Element IDElement = (Element)IDList.item(0);
                
                NodeList textIDList = IDElement.getChildNodes();
                System.out.println("Users from List = " +((Node)textIDList.item(0)).getNodeValue().trim().toString()+" "+ "Courses from Database = " +users.getUniqueID().replaceAll(" ", "")+" Counter = "+s );
                if (((Node)textIDList.item(0)).getNodeValue().trim().toString().equals(users.getUniqueID().replaceAll(" ", ""))){ 
                    located = true;
                	System.out.println("id : " + ((Node)textIDList.item(0)).getNodeValue().trim());
                    break;
                }  
             }
		  }
			
		}
		
		return located;
	}
	public boolean locateParentID(){
     boolean located = false;
		
		
		NodeList listOfSourcedid = doc.getElementsByTagName("sourcedid");
		System.out.println("Number of Sourcedids = " +listOfSourcedid.getLength());
		for(int s=0; s< listOfSourcedid.getLength() ; s++){
			
			Node firstSourceDIDNode = listOfGroups.item(s);
            
			if (firstSourceDIDNode != null){
              if(firstSourceDIDNode.getNodeType() == Node.ELEMENT_NODE){
            	Element firstIDElement = (Element)firstSourceDIDNode; 

                //-------
                NodeList IDList = firstIDElement.getElementsByTagName("id");
                Element IDElement = (Element)IDList.item(0);
                
                NodeList textIDList = IDElement.getChildNodes();
                
                if (((Node)textIDList.item(0)).getNodeValue().trim().toString().equals(courses.getParentID().replaceAll(" ", "").toUpperCase())){ 
                    located = true;
                	System.out.println("id : " + ((Node)textIDList.item(0)).getNodeValue().trim());
                    break;
                }
                
             }
		  }
			if (s == 1897){
				System.out.println("Stop here");
				break;
			}	
		}
		
		return located;
	}
	public void setBlackboardObject(Object cs){
		this.users=(Users)cs;
	}
	public static void main (String argv []){
		LoadXML ldXML = new QueryUserXML();
		ldXML.parseXML();
		Courses cs = new Courses();
		cs.setUniqueID("ECON3051.200610");
		
		ldXML.setBlackboardObject(cs);
		
		ldXML.locateChildID();
	}
}
