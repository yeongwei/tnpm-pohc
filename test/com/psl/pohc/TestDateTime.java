package com.psl.pohc;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TestDateTime {
  public static void main (String[] args) throws Exception {
    System.out.println(
        "new java.util.Date will always give you current Date/Time with System TimeZone" 
            + getCurrentDateWithSystemTimeZone());
    getCurrentDateGMTString();
  }
  
  private static java.util.Date getCurrentDateWithSystemTimeZone() {
    java.util.Date date = new java.util.Date();
    return date;
  }
  
  private static String getCurrentDateGMTString() throws Exception {   
    // If GMT is needed, always set it
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    
    java.util.Date date = getCurrentDateWithSystemTimeZone();
    String dateTime = sdf.format(date);
    System.out.println(
        "By using SimpleDateFormat with TimeZone, "
        + "the Date/Time will be offset to TimeZone set " 
            + dateTime);
    
    // It seems like Date will always look at System TimeZone
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    java.util.Date date2 = sdf2.parse(dateTime);
    System.out.println(
        "SimpleDateFormat will always append the System TimeZone "
        + date2);
    
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf3.setTimeZone(TimeZone.getTimeZone("GMT"));
    java.util.Date date3 = sdf3.parse(dateTime);
    System.out.println(
        "If SimpleDateFormat is givent TimeZone information, "
        + "it will attempt to offset according to System TimeZone "
        + date3);
    
    return dateTime;
  }
}
