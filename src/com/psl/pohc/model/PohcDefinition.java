package com.psl.pohc.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PohcDefinition {
  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final String TIME_ZONE = "GMT";
  
  public final Date DATE;
  public final String RECORDED_DATE_TIME;
  
  public final String SUB_SYSTEM;
  public final String REGION;
  public final String SYSTEM;
  public final String GROUP_NAME;
  public final String ID;
  public final String PHASE;
  public final String NODE_NAMES;
  public final String STATUS;
  public final String DOMAIN;
  public final String OUTAGE_START;
  public final String OUTAGE_END;
  
  public PohcDefinition(
      Date date,
      String subSystem, String region, String system, String groupName, String id,
      String phase, String nodeNames, String status, String domain, String outageStart,
      String outageEnd) {
    DATE = date;
    RECORDED_DATE_TIME = getGmtDateTime();
    
    SUB_SYSTEM = subSystem;
    REGION = region;
    SYSTEM = system;
    GROUP_NAME = groupName;
    ID = id;
    PHASE = phase;
    NODE_NAMES = nodeNames; 
    STATUS = status;
    DOMAIN = domain;
    OUTAGE_START = outageStart;
    OUTAGE_END = outageEnd;
    
  }
  
  private String getGmtDateTime() {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
    sdf.format(DATE);
    return null;
  }
  
  public String asNameValuePair() {
    StringBuffer sb = new StringBuffer();
    
    sb.append("RECORDED_DATE_TIME=\"" + RECORDED_DATE_TIME + "\" ");
    sb.append("SUB_SYSTEM=\"" + SUB_SYSTEM + "\" ");
    sb.append("REGION=\"" + REGION + "\" ");
    sb.append("SYSTEM=\"" + SYSTEM + "\" ");
    sb.append("GROUP_NAME=\"" + GROUP_NAME + "\" ");
    sb.append("ID=\"" + ID + "\" ");
    sb.append("PHASE=\"" + PHASE + "\" ");
    sb.append("NODE_NAMES=\"" + NODE_NAMES + "\" ");
    sb.append("STATUS=\"" + STATUS + "\" ");
    sb.append("DOMAIN=\"" + DOMAIN + "\" ");
    sb.append("OUTAGE_START=\"" + OUTAGE_START + "\" ");
    sb.append("OUTAGE_END=\"" + OUTAGE_END + "\" ");
    
    return sb.toString();
  }
}
