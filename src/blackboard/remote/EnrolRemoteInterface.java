package blackboard.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import blackboard.util.StudentEnrol;

public interface EnrolRemoteInterface extends Remote {

	
	  public ArrayList getCourseEnrollment(String id) throws RemoteException;
	  public ArrayList getStudentsEnrolledInCourse(String subjCode, String crseNumb) throws RemoteException;
	  public StudentEnrol getStudentEnrol() throws RemoteException;
}
