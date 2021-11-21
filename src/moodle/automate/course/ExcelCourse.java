package moodle.automate.course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ExcelCourse {
	private String shortname;
	private String idnumber;
	private String category;
	private String fullname; 
	private String startdate;
	private String enddate;
	private String visible;
	private String  templateCourse;
	
	public String getTemplateCourse() {
		return templateCourse;
	}
	public void setTemplateCourse(String templateCourse) {
		this.templateCourse = templateCourse;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public static HashMap<String,Resits> usingDataInputStreamResits()  throws IOException {
		 
		String strCurrentLine;
		HashMap<String,Resits> list = new HashMap<String,Resits>();
		
		 FileInputStream input = new FileInputStream("c:/temp/resits/updates/resits.csv");
		 
		 File file = new File("c:/temp/resits/updates/resits.csv"); 
		  
		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  try {
		  while ((strCurrentLine = br.readLine()) != null) {

			    String[] values = strCurrentLine.split(",");
					if (values[2] != null) {
						String code = values[0].substring(0, 4);
						Resits resit = new Resits();
						resit.setCourseCode(values[0]);
						resit.setResitDate(values[2]);
						list.put(values[0], resit);
					}
			}
		  } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			  String str = ex.getMessage();
		  }
		  
		  return list; 
	}
	
	public static void usingDataOutputStream(ArrayList<ExcelCourse> list) throws IOException 
	{
		
		String fileHeader="shortname, idnumber, category, fullname, startdate, enddate, visible, templatecourse, summary";
		String dataString = "";
	    
	   
	    
	    FileWriter fileWriter = new FileWriter("c:/temp/upload/course.csv");
	    PrintWriter printWriter = new PrintWriter(fileWriter,false);
	    printWriter.println(fileHeader);
	   
	    Iterator<ExcelCourse> ii = list.iterator();
	    while (ii.hasNext()) {
	    	ExcelCourse ec = ii.next();
	    	
	    	dataString=ec.getShortname()+","+ec.getIdnumber()+","+ec.getCategory()+","+ec.getFullname().replace(",", "")+","+
	    	ec.getStartdate()+","+ec.getEnddate()+","+ec.getVisible()+","+ec.getTemplateCourse()+","+"";
	    	printWriter.println(dataString);
	    }
	    printWriter.close();
	}
	public static void main(String[] args) throws SQLException, IOException {  
            
             ExcelCourse.usingDataInputStreamResits();
	}
}
