package blackboard.output.courses;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import blackboard.db.courses.CourseDb;
import blackboard.util.Courses;


public class OutputCourses {
	
	
  public void writeOut() throws SQLException{
	 try {
		 Date dte = new Date();
		 DateFormat formatter = new SimpleDateFormat("yy:MM:dd:HH:mm");
	
		 
		 String dteStr = formatter.format(dte).toString();
		 
		 System.out.println(dteStr.replaceAll(":", ""));
	
		
		 BufferedWriter out = new BufferedWriter(new FileWriter("csv/lcs_" + dteStr.replaceAll(":", "") +".csv",true));
	     CourseDb db = new CourseDb();
	     ArrayList courseList = db.selectCourses();
	     Iterator iterator = courseList.iterator();
         ArrayList sectionList = db.selectSections();
         
	     
	     while (iterator.hasNext()){
	    	 Courses courses = (Courses)iterator.next();
	    	 out.write(courses.getUniqueID()+","+courses.getParentID()+","+ courses.getlcTypeID()+ ","+'"'+courses.getCourseTitle()+'"'+","+'"'+courses.getLongDesc()+'"');
	         out.newLine();
	     }
		 iterator = sectionList.iterator();
		 while (iterator.hasNext()){
	    	 Courses courses = (Courses)iterator.next();
	    	 out.write(courses.getUniqueID()+","+courses.getParentID()+","+ courses.getlcTypeID()+ ","+'"'+courses.getSectionTitle()+'"'+","+'"'+courses.getLongDesc()+'"');
	         out.newLine();
		 }
	     
	     out.close();
	    } catch (IOException e) {
	    	
	    }

  }
  public static void main(String[] args) throws SQLException 
  {
	  OutputCourses a = new OutputCourses();
	  a.writeOut();
	  
  }
}
