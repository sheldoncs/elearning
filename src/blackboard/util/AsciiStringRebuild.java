package blackboard.util;

public class AsciiStringRebuild {
	
	
	
	public String rebuildString(String s){
		
		String newString = "";
		
		String test = s;

		   for ( int i = 0; i < test.length(); ++i ) {
		     char c = test.charAt( i );
		     int j = (int) c;
		     if ((capitalFlag(j)) || (commonFlag(i)) || fullStopFlag(i) || emailAtFlag(i)) {
		    	 newString = newString + c;
		     } 
		    	 
		   
		   }
		   
		   return newString;
		
	}
	private boolean capitalFlag(int i){
		
		for ( int j = 65; i <= 90; ++i ) {
	     	if (i == j)
	     		return true;
		 }
		return false;
	}
    private boolean commonFlag(int i){
		
		for ( int j = 97; i <= 122; ++i ) {
	     	if (i == j)
	     		return true;
		 }
		return false;
	}
	private boolean fullStopFlag(int i){
		
		if (i == 46)
			return true;
		
		return false;
			
	}
    private boolean emailAtFlag(int i){
		
		if (i == 64)
			return true;
		
		return false;
			
	}	
	

}
