package blackboard.util;

import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import blackboard.util.UserEnrol;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class StudentUploadExcel {

	FileInputStream fileIn;
	HSSFSheet sheet;
	ArrayList<StudentEnrol> list = new ArrayList<StudentEnrol>();
	public StudentUploadExcel(String path, String filename){
		
		try {
			
			fileIn = new FileInputStream(path + filename
					+ ".xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void setupWorkBook(){
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(fileIn);
			sheet = workbook.getSheet("fiveisland");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void iterateSheet(){
		
		Iterator<HSSFRow> iterator = sheet.rowIterator();
	    int rowcnt = 0;
	    int cellcnt = 0;
		while (iterator.hasNext()) {
			
			cellcnt = 0;
			StudentEnrol details = new StudentEnrol();
			HSSFRow nextRow = iterator.next();
			Iterator<HSSFCell> cellIterator = nextRow.cellIterator();
				
				while (cellIterator.hasNext()) {
					cellcnt++;
					String stringType = "";
					double numType = 0;
					HSSFCell cell = cellIterator.next();
					
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING:
						
						stringType = cell.getStringCellValue().trim();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue());
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						numType = cell.getNumericCellValue();
						break;
					case HSSFCell.CELL_TYPE_BLANK:
						stringType = "000000000";
						break;
					}
				
										
				if (cellcnt == 1){          
					
	                details.setId(stringType);
	               
	            } else if (cellcnt == 2){
	            	
	            	details.setFirstname(stringType);
	            	
	            }else if (cellcnt == 3){
	            	
	            	details.setLastname(stringType);
	            	
	            }if (cellcnt == 4){          
	            	
	            	String subjCode = stringType.substring(0,4);
	            	String crseCode  = stringType.substring(4,8);
	            	details.setSubjCode(subjCode);        
	            	details.setCrseCode(crseCode);
	                 
	            } else if (cellcnt == 5){
	            	
	            	details.setCrn(stringType);
	            	
	            }
	            
			 }	
				
				details.setTermCode("201920");
				details.setAction("add");
				details.setSeqNumb("F01");
				details.setUserType("student");
				
				
	            list.add(details);
			
		}
		
	}
	public ArrayList<StudentEnrol> getList(){
		return list;
	}
	public static void main(String[] args){
	
		StudentUploadExcel u = new StudentUploadExcel("c:/temp/","fiveisland2");
		u.setupWorkBook();
		u.iterateSheet();
		
	}
}
