package blackboard.util;

//jdk1.4.1
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

// using SAX
public class ListerSAX {
	
	private HashMap userIDlist = new HashMap();
	private ArrayList uniqueIdList = new ArrayList();
	private String xml;
	
  class HowToHandler extends DefaultHandler {
    private boolean id = false;
    private boolean userid   = false;
    private String strSourcedid;
    private String strUserid;
    private ArrayList stackUserids = new ArrayList();
    private ArrayList Ids = new ArrayList();
    private boolean membership;
    private boolean member;
    private boolean source;
    private boolean sourcedid;
    private boolean person;
    
    public void startElement(String nsURI, String strippedName,
                            String tagName, Attributes attributes)
       throws SAXException {
        if (tagName.equalsIgnoreCase("id"))
          id = true;
        if (tagName.equalsIgnoreCase("userid"))
          userid = true;
        if (tagName.equalsIgnoreCase("membership"))
        	membership = true;	
        if (tagName.equalsIgnoreCase("member"))
        	member = true;
        if (tagName.equalsIgnoreCase("source"))
        	source = true;
        if (tagName.equalsIgnoreCase("sourcedid"))
        	sourcedid = true;
        if (tagName.equalsIgnoreCase("person"))
        	person = true;
     }
    
    public void characters(char[] ch, int start, int length) {
    	
       if (id) {
       
       id = false;
       Ids.add(new String(ch, start, length));
       strSourcedid=new String(ch, start, length);
     //System.out.println("id: " + new String(ch, start,length));
       
     }
     
        if (userid) {
       	 
        	Users users = new Users();
        	
           //System.out.println("userid: " + new String(ch, start,length));
           userid = false;
           
           strUserid = new String(ch, start,length);
           
           users.setUniqueID(strSourcedid);
           users.setLoginID(strUserid);
           userIDlist.put(strUserid,strSourcedid);
           
       }
        if (sourcedid)
        	System.out.println("sourcedid: " + new String(ch, start,length));
        
    }
     public HashMap getUserIds(){
    	 return userIDlist;
     }
     public ArrayList getSourcedIds(){
    	 return Ids;
     }
    }
    
    public void list( ) throws Exception {
       XMLReader parser =
          XMLReaderFactory.createXMLReader
            ("org.apache.crimson.parser.XMLReaderImpl");
       HowToHandler handler = new HowToHandler( );
       parser.setContentHandler(handler);
       
       parser.parse(xml);
       userIDlist = handler.getUserIds();
       uniqueIdList = handler.getSourcedIds();
    
    }
    public void setXMLFile(String xml){
    	this.xml=xml;
    }
    public HashMap getLister(){
    	return userIDlist;
    }
    public ArrayList getUniqueIDLister(){
    	return uniqueIdList;
    }
    public static void main(String[] args) throws Exception {
       ListerSAX s = new ListerSAX();
       s.list( );
       
       HashMap hMap = s.getLister();
       Iterator i = s.getLister().keySet().iterator();
       //CurrentUsers cUsers = new CurrentUsers();
       while (i.hasNext()){
    	   Users users = new Users();
    	   String loginID = i.next().toString();
    	   users.setLoginID(loginID);
    	   users.setUniqueID(hMap.get(loginID).toString());
    	   ///cUsers.insertCurrentUsers(users);
    	   
    	   System.out.println(users.getUniqueID()+ " "+users.getLoginID());
       }
       //cUsers.closeDb();
       
       }
       
}


