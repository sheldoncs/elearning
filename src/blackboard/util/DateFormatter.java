package blackboard.util;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;





  public class DateFormatter {
	  private Date date;
	  java.sql.Date sqlDate;
	  
	public String printDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		date = new Date();
		
		
		String s = formatter.format(date);
		return s;
		
	}
	public String printDateSlash(){
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		date = new Date();
		
		
		String s = formatter.format(date);
		return s;
		
	}
	public String printDateSQLServer(){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

		date = new Date();
		
		
		String s = formatter.format(date);
		return s;
	}
	public String getSimpleDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		date = new Date();
		String s = formatter.format(date);
		return s;
	}
	public String getSimpleMMDDYYDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		date = new Date();
		String s = formatter.format(date);
		return s;
	}
	public String getSimpleDate(Date d){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String s = formatter.format(date);
		return s;
	}
	public String getFormattedDate(Date d){
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

		String s = formatter.format(d);
		return s;
	}
	public long getIntDate(Date date){
		int diff = 0;
		int extraDays = 0;
		int days = 0;
		int month = 0;
		int year = 0;
		int totSeconds = 0;
		
		SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");
		SimpleDateFormat monthFormat=new SimpleDateFormat("mm");
		
		year = Integer.parseInt(yearFormat.format(date));
		diff = year-1970;
		extraDays =  diff / 4;
		days = diff * 365;
		days = days + extraDays;
		
		month = Integer.parseInt(monthFormat.format(date));
		
		
		for (int i=1; i <=(month-1);i++){
			
			Calendar cal = new GregorianCalendar(year, i-1, 1);	
			days = days + cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
			
		}
		SimpleDateFormat dayFormat=new SimpleDateFormat("dd");
		days = days + Integer.parseInt(dayFormat.format(date));
		totSeconds = days * 86400;
		
		return totSeconds;
	}
    public Date convertLongToDate(long lng){
    	
    	Date d = new Date(lng * 1000);
    	createSQLDate(d);
    	return d;
    }
    public long convertDateToLongOld(Date date){
      
    	   SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");
   		   SimpleDateFormat monthFormat=new SimpleDateFormat("mm");
   		   SimpleDateFormat dayFormat=new SimpleDateFormat("dd");
   		
    	  String str_date=dayFormat.format(date)+"/"+monthFormat.format(date) +"/"+yearFormat.format(date);
    	  //
          // Here we have a string date and we want to covert it to long value
          //
          String today = str_date;

          //
          // Create a SimpleDateFormat which will be use to convert the string to
          // a date object.
          //
          DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
          try {
              //
              // The SimpleDateFormat parse the string and return a date object.
              // To get the date in long value just call the getTime method of
              // the Date object.
              //
              Date dated = formatter.parse(today);
              long dateInLong = date.getTime();
              dateInLong = dateInLong /1000; 
              
              System.out.println("date = " + date);                
              System.out.println("dateInLong = " + dateInLong);
              return dateInLong;
          } catch (ParseException e) {
              e.printStackTrace();
          }

    	return 0;
    }
    public long convertDateToLong(Date date){
    try {
    	
    	  SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");
		  SimpleDateFormat monthFormat=new SimpleDateFormat("MM");
		  SimpleDateFormat dayFormat=new SimpleDateFormat("dd");
		  
		  String str_date=dayFormat.format(date)+"/"+monthFormat.format(date) +"/"+yearFormat.format(date);
		  
		  System.out.println(monthFormat.format(date));
		  
    	  //String str_date="11-June-07";
    	  
    	  
    	  DateFormat formatter ; 
    	  
    	  formatter = new SimpleDateFormat("dd/MM/yyyy");
    	  
    	  date = (Date)formatter.parse(str_date); 
    	  long longDate=date.getTime()/1000;
    	  System.out.println("Today is " +longDate );
    	  return longDate;
    	  }
    	     catch (ParseException e){
    	     System.out.println("Exception :"+e); 
    	     }
    	     
    	     return 0;
    }
    private void createSQLDate(Date d){
	    sqlDate = new java.sql.Date(d.getTime()); 
		
	}
	public java.sql.Date getSQLDate(){
		
		return sqlDate;
	}
	public static void main(String[] args) throws RemoteException,SQLException,ParseException {
		DateFormatter tb = new DateFormatter();
		SimpleDateFormat dateFormat=new SimpleDateFormat("mm/dd/yyyy");
		
		System.out.println(tb.getIntDate(dateFormat.parse("01/25/2010")));
		Date d = tb.convertLongToDate(tb.getIntDate(dateFormat.parse("01/25/2010")));
	    System.out.println(tb.getSQLDate());
		
		
	}
}
