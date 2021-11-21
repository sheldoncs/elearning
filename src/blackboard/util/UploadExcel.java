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


public class UploadExcel {

	FileInputStream fileIn;
	HSSFSheet sheet;
	ArrayList<UserEnrol> list = new ArrayList<UserEnrol>();
	public UploadExcel(String path, String filename){
		
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
			rowcnt++;
			UserEnrol details = new UserEnrol();
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

					if (cellcnt == 1) {

						details.setId(stringType);

					} else if (cellcnt == 2) {

						details.setFirstName(stringType);

					} else if (cellcnt == 3) {

						details.setLastName(stringType);

					}
					if (cellcnt == 4) {
						details.setEmail(stringType);
					} else if (cellcnt == 5) {

						// 

					}

				}
				//details.setEmail("brittney.stluce@myfiveislands.uwi.edu");
				details.setAuth("manual");
				details.setCity("Antigua");
				details.setCountry("An");
				details.setPassword("bb@2020");
				details.setSuspend(0);
				details.setAction("update");
				if (details.getFirstName() != null) {
			     	list.add(details);
				}

			}
		
	}
	public ArrayList<UserEnrol> getList(){
		return list;
	}
	public static void main(String[] args){
	
		UploadExcel u = new UploadExcel("c:/temp/","fiveisland");
		u.setupWorkBook();
		u.iterateSheet();
		
	}
}
