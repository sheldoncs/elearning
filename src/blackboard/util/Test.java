package blackboard.util;

import java.rmi.RemoteException;

import moodle.uib.automate.StudentCourseDrops;

public class Test {

	public static void main(String[] args) throws RemoteException {
		
		StudentCourseDrops cd = new StudentCourseDrops();
		cd.accessEnrollments("416001908");
	}
}
