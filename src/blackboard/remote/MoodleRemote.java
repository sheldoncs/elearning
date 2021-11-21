package blackboard.remote;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blackboard.db.OracleDBConnect;
import blackboard.util.StudentEnrol;


public class MoodleRemote implements EnrolRemoteInterface {

	public ArrayList getCourseEnrollment(String id) throws RemoteException {
		// TODO Auto-generated method stub
		ArrayList list = null;
		StudentEnrol stu = new StudentEnrol();
		stu.setId("1");
		stu.setFirstname("sheldon");
		stu.setLastname("spencer");
		stu.setSubjCode("");
		stu.setCrseCode("");
		stu.setCrn("21874");
		
		list = new ArrayList();
		list.add(stu);
		
		return list;
	}

	public ArrayList getStudentsEnrolledInCourse(String subjCode,
			String crseNumb) throws RemoteException {
		// TODO Auto-generated method stub
		ArrayList list = new ArrayList();
		StudentEnrol stu = new StudentEnrol();
		//OracleDBConnect db = new OracleDBConnect();
		
		stu.setId("1");
		stu.setFirstname("sheldon");
		stu.setLastname("spencer");
		stu.setSubjCode(subjCode);
		stu.setCrseCode(crseNumb);
		stu.setCrn("21874");
		stu.setAction("drop");
		stu.setTermCode("200920");
		stu.setSeqNumb("L");
		
		//list = db.gatherEnrollmentsByCourse(subjCode, crseNumb);
		//db.closeConnection();
		
		list.add(stu);
		
		return list;
	}
	public StudentEnrol getStudentEnrol() throws RemoteException {
		StudentEnrol stu = new StudentEnrol();
		
		stu.setId("1");
		stu.setFirstname("sheldon");
		stu.setLastname("spencer");
		stu.setSubjCode("");
		stu.setCrseCode("");
		stu.setCrn("21874");
		stu.setAction("drop");
		stu.setTermCode("200920");
		stu.setSeqNumb("L");

		return stu;
	}

}
