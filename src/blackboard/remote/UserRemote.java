package blackboard.remote;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blackboard.db.OracleDBConnect;
import blackboard.util.UserEnrol;

public class UserRemote implements UserRemoteInterface {

	public ArrayList getUsers(String crseNumb, String subjCode)
			throws RemoteException {
		// TODO Auto-generated method stub
		
		 ArrayList list = new ArrayList();
		 OracleDBConnect db = new OracleDBConnect();
	     UserEnrol uEnrol = new UserEnrol();
		 
		 uEnrol.setAction("create");
		 uEnrol.setAuth("radius");
		 uEnrol.setCity("Bridgetown");
		 uEnrol.setCountry("Barbados");
		 uEnrol.setEmail("sheldon.spencer@cavehill.uwi.edu");
		 uEnrol.setFirstName("Sheldon");
		 uEnrol.setLastName("Spencer");
		 uEnrol.setId("20003569");
		 uEnrol.setPassword("NetworkPassword");
		 list.add(uEnrol);
		
		 return list;
	}
	 public UserEnrol getUserEnrol(String id){
		 UserEnrol uEnrol = new UserEnrol();
		 
		 uEnrol.setAction("create");
		 uEnrol.setAuth("radius");
		 uEnrol.setCity("Bridgetown");
		 uEnrol.setCountry("Barbados");
		 uEnrol.setEmail("sheldon.spencer@cavehill.uwi.edu");
		 uEnrol.setFirstName("Sheldon");
		 uEnrol.setLastName("Spencer");
		 uEnrol.setId("20003569");
		 uEnrol.setPassword("NetworkPassword");
		 
		 return uEnrol;
	 }
}
