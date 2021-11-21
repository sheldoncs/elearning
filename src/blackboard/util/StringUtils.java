package blackboard.util;

import java.sql.SQLException;

public class StringUtils {
  private String initials;
  	
  public String calculateInitials(String s){
	  int cntBlank = 0;
	  int checkBlank = 0;
	  String temp = "";
	  //
	  initials = s;
	  while (s.indexOf(" ") > 0)
	  {	  
         cntBlank++;
         s = s.substring(s.indexOf(" ")+1, s.length());
	  }
	  
		 
	  while (checkBlank <= cntBlank)
	  {
	   
		temp = temp + initials.substring(0, 1) + " ";
		initials = initials.substring(initials.indexOf(" ")+ 1, initials.length());
	   
	    checkBlank++;
      }
	  initials = temp;
	  
	  return initials;
  }
  public String calculateName(String s){
	  String a = null;
	  a = s.substring(s.indexOf(":")+1, s.length()).trim();
	  
	  return a;
  }
  public static void main(String[] args) throws ClassNotFoundException,
	SQLException {
	  StringUtils su = new StringUtils();
	  su.calculateInitials("Ian Michael");
	  
  }
}
