/*
 * Created on 2004/7/31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package blackboard.util;

import java.util.Properties;
import java.util.TimerTask;

import javax.xml.rpc.Stub;

import org.uddi4j.client.UDDIProxy;

import PublishStub.PublishService_Impl;
import PublishStub.RemoteInterface;
import blackboard.db.MySQLConnection;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RefreshingTask extends TimerTask {

    private UDDIProxy proxy;

    private String serviceKey;
    private String bindingKey;
    private String accessPoint;
    private Stub stub;
    
	private static final String INQUIRY_URL =  "http://owl1:8081/juddi/inquiry";
	
    //Publish
    //private static final String PUBLISH_URL = "http://localhost:8080/juddi/publish";
	private static final String PUBLISH_URL = "http://owl1:8081/Publish/Publish";
	
	private RemoteInterface h;
    
	public RefreshingTask(Properties properties) {
		try {
			  serviceKey = properties.getProperty("MoodleServiceKey");
			  bindingKey = properties.getProperty("MoodleBindingKey");
			  accessPoint = properties.getProperty("AccessPoint")+"?WSDL";
			  
			  MessageLogger.out.println(serviceKey + " "+bindingKey+" "+accessPoint);
			  
			  /*
			  stub = createProxy();
			  stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY,PUBLISH_URL);
			  h = (RemoteInterface)stub;
			  
			 
			  proxy = ReplicaUDDIProxy.getInstance();
			  proxy = new UDDIProxy(new URL(INQUIRY_URL), new URL(PUBLISH_URL));
			  */
			  
		} catch (Exception e) {
			e.printStackTrace(MessageLogger.out);
		}
	}
	
	private static Stub createProxy()
	{
//			 Note: MyHelloService_Impl is implementation-specific.
			return (Stub) (new PublishService_Impl().getRemoteInterfacePort());
			
	}
	public void run() {

	    try{
	    	MySQLConnection juddiConnect = new MySQLConnection();
			juddiConnect.connectUDDI();
			juddiConnect.publish(bindingKey, serviceKey);
			juddiConnect.closeConnection();
	    	/*
	    	h.publish(serviceKey, bindingKey);
	    	
          	Vector bindings = new Vector();
           	TModelInstanceDetails details = new TModelInstanceDetails();
           	BindingTemplate binding = new BindingTemplate(bindingKey,details, new AccessPoint(accessPoint, "http"));
           	binding.setServiceKey(serviceKey);
           	bindings.add(binding);
           	
         	BindingDetail detail = proxy.save_binding(null, bindings);
         	*/
	    } catch (Exception e){
	    	e.printStackTrace(MessageLogger.out);
	    }
	}
}
