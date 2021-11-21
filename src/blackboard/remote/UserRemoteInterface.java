package blackboard.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import blackboard.util.UserEnrol;

public interface UserRemoteInterface extends Remote {

	
	  public ArrayList getUsers(String crseNumb, String subjCode) throws RemoteException;
	  public UserEnrol getUserEnrol(String id) throws RemoteException;
	  
}
