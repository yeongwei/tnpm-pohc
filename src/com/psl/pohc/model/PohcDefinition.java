package com.psl.pohc.model;


public class PohcDefinition {
 
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
      String subSystem, String region, String system, String groupName, String id,
      String phase, String nodeNames, String status, String domain, String outageStart,
      String outageEnd) {
    
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
  
  public String asNameValuePair() {
    StringBuffer sb = new StringBuffer();
    
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
