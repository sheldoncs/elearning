package creditcard.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




public class TransactionWorkbook{
    private FileOutputStream fileOutputStream;
    private String user;
    private String connect;
    private String password;
    private HSSFSheet sheet;
    private HSSFWorkbook workbook;
    
    private int irow = 0;
    
	public TransactionWorkbook(String connect, String user, String password){
		
		this.connect = connect;   
		this.user = user;
	    this.password = password;
	}
	
	/*
	public void openStudentFile(String path, String sheetName){
		
		int icol = 0;
		
		try {
			   fileOutputStream = new FileOutputStream(path);
		}catch (FileNotFoundException ex){
				  ex.printStackTrace();
		}
		
		
		workbook = new HSSFWorkbook(); 
		
		sheet = workbook.createSheet(sheetName); 
		
		HSSFRow headrow = sheet.createRow((short)irow);
		ArrayList nameList = adsl.getFieldNames();
		

		  Iterator iterate = nameList.iterator();
		  MessageLogger.out.println("Name List Size = " + nameList.size()); 
		  while (iterate.hasNext()){
			  MessageLogger.out.println("Running through header list");
			FieldNamesProperties prop = (FieldNamesProperties)iterate.next();
			headrow.createCell((short)icol).setCellValue(prop.getNames());
			
			icol++;
		  
		}
		adsl.closeConnections();
	}
	
	/*
	public void createStudents(BannerStudentInfo bsi){
		
		
		int icol = 0;
		
		HSSFRow row = sheet.createRow((short)irow); 
		
	    row.createCell((short)icol).setCellValue(bsi.getId()); 
	    row.createCell((short)(icol+1)).setCellValue(bsi.getLastname());
	    row.createCell((short)(icol+2)).setCellValue(bsi.getFirstname());
	    row.createCell((short)(icol+3)).setCellValue(bsi.getInitial());
	    row.createCell((short)(icol+4)).setCellValue(bsi.getEmail());
	    row.createCell((short)(icol+5)).setCellValue(bsi.getFaculty());
	    row.createCell((short)(icol+6)).setCellValue(bsi.getLevel());
	    row.createCell((short)(icol+7)).setCellValue(bsi.getMajor());
	    row.createCell((short)(icol+8)).setCellValue(bsi.getPin());
	    
	    
	    if (bsi.getBirthDate() != null) {
	     NewDateFormatter df = new NewDateFormatter();
	     
	     row.createCell((short)(icol+9)).setCellValue(df.getMMMDate(bsi.getBirthDate()));
	    
	    }
	    else {
	    	row.createCell((short)(icol+9)).setCellValue("");
	    }
	    
	    row.createCell((short)(icol+10)).setCellValue(bsi.getCountry());
	    MessageLogger.out.println("Row Counter = " +irow+" "+ bsi.getId()+","+bsi.getFirstname()+","+bsi.getLastname()+","+bsi.getFaculty()+","+bsi.getPin());
	   
	    irow++;
	}
	public void closeOutput(){
		 
		 try {
			 
			 MessageLogger.out.println("Write Delete File Output");
			 workbook.write(fileOutputStream);
		     fileOutputStream.close();
		 } catch (IOException ex){
			  ex.printStackTrace();
	     }
	}
	*/
	public static void main(String[] args) throws RemoteException,SQLException {
			
	}
}
