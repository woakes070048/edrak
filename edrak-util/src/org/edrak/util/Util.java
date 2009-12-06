package org.edrak.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    
    
    public static void fixLog() {

	 System.setProperty("org.apache.commons.logging.log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.defaultlog","ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.level","ERROR");
        System.setProperty("org.apache.commons.logging.log.level","ERROR");
	 System.setProperty(".level","ERROR");
	
   }
    
    public static String getStackTrace(Throwable aThrowable) {
	    final Writer result = new StringWriter();
	    final PrintWriter printWriter = new PrintWriter(result);
	    aThrowable.printStackTrace(printWriter);
	    return result.toString();
	  }
    
    public static String removeLastComa(String str) {
	if (str == null || str.equals(""))
		return "";
	str = str.trim();
	if (str.lastIndexOf(',') == str.length() - 1) {

		return str.substring(0, str.length() - 1);

	}

	return str;

}
    
    static public String getDateTime(long date){
        SimpleDateFormat f=new SimpleDateFormat("d/MM/yyyy - HH:mm");
        return f.format(new Date(date));
    }
    
    static public String getDate(long date){
        SimpleDateFormat f=new SimpleDateFormat("d/MM/yyyy");
        return f.format(new Date(date));
    }

    
  static public String getNormalizedDate(String date){
	
	if (date==null) return "";
	date=date.replace('-', '/'); 
	date=date.replace('\\', '/');
	
	return date;
    }
    

  static public String getNormalizedPath(String path){
	
	if (path==null) return "";
	 
	path=path.replace('\\', '/');
	
	if (!path.endsWith("/")) path+="/";
	
	return path;
  }

  
  public static String getCurrentYear(){
      SimpleDateFormat f=new SimpleDateFormat("yyyy");
      return f.format(new Date(System.currentTimeMillis()));
      
  }
   
  
  

  public static String getCurrentDay(){
      SimpleDateFormat f=new SimpleDateFormat("d");
      return f.format(new Date(System.currentTimeMillis()));
      
  }
  
  

  public static String getCurrentMonth(){
      SimpleDateFormat f=new SimpleDateFormat("MM");
      return f.format(new Date(System.currentTimeMillis()));
      
  }
    public static Long nowL(){
	return new Long(System.currentTimeMillis());
    }

    public static int compareDate(String strDate1,String strDate2){
      	
      	// accept date as string in the form dd/MM/yyyy
      	//return 0 if equal
      	//return 1 if strDate1 greater(after) strDate2
      	//return -1 if strDate1 smaller(before) strDate2
      	//return -2 if any date is empty or not an date
      	
      	if (isEmpty(strDate1) || isEmpty(strDate2)) return -2;
      	
      	String temp1[]=strDate1.split("/");
      	
      	if (temp1==null || temp1.length!=3) return -2;
      	
      	String temp2[]=strDate2.split("/");
      	
      	if (temp2==null || temp2.length!=3) return -2;
      	
      	try {
    	
      		int y1=Integer.parseInt(temp1[2]);
      		int m1=Integer.parseInt(temp1[1]);
      		int d1=Integer.parseInt(temp1[0]);
      		
      		
      		int y2=Integer.parseInt(temp2[2]);
      		int m2=Integer.parseInt(temp2[1]);
      		int d2=Integer.parseInt(temp2[0]);
      		
      		
      		if (y1>y2) return 1;
      		if (y1<y2) return -1;
      		
      		if (m1>m2) return 1;
      		if (m1<m2) return -1;
      		
      		if (d1>d2) return 1;
      		if (d1<d2) return -1;
      		
      		return 0;
      		
      		
      	
      	}catch (Exception e) {
      		
      	}
      	
      	return -2;
      }


    public static boolean isEmpty(String str){
    	
    	if (str==null) return true;
    	
    	if (str.trim().equals("")) return true;
    	
    	return false;
    }

}
