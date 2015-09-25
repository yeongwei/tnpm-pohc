package com.psl.pohc.model;

import java.sql.Date;

public class PohcDefinition {

  public final String SUBSYSTEM;
  public final String REGION;
  public final String SYSTEM;
  public final String GROUPNAME;
  public final String ID;
  public final String PHASE;
  public final String NODE_NAMES; // Use for delimited and non delimited values
  public final String STATUS;
  public final String DOMAIN;
  public final Date OUTAGE_START;
  public final Date OUTAGE_END;

  private Type TYPE = Type.NONE;
  private Inventory INVENTORY = Inventory.NONE;

  public PohcDefinition(String id, 
      String subSystem, String region, String system,
      String groupName, String phase, String nodeNames,
      String status, String domain, Date outageStart, Date outageEnd) {

    ID = id;
    SUBSYSTEM = subSystem;
    REGION = region;
    SYSTEM = system;
    GROUPNAME = groupName;
    PHASE = phase;
    NODE_NAMES = nodeNames;
    STATUS = status;
    DOMAIN = domain;
    OUTAGE_START = outageStart;
    OUTAGE_END = outageEnd;

  }

  public String asNameValuePair() {
    StringBuffer sb = new StringBuffer();

    sb.append("ID=\"" + ID + "\" ");
    sb.append("SUB_SYSTEM=\"" + SUBSYSTEM + "\" ");
    sb.append("REGION=\"" + REGION + "\" ");
    sb.append("SYSTEM=\"" + SYSTEM + "\" ");
    sb.append("GROUP_NAME=\"" + GROUPNAME + "\" ");
    sb.append("PHASE=\"" + PHASE + "\" ");
    sb.append("NODE_NAMES=\"" + NODE_NAMES + "\" ");
    sb.append("STATUS=\"" + STATUS + "\" ");
    sb.append("DOMAIN=\"" + DOMAIN + "\" ");
    sb.append("OUTAGE_START=\"" + OUTAGE_START + "\" ");
    sb.append("OUTAGE_END=\"" + OUTAGE_END + "\" ");
    sb.append("TYPE=\"" + TYPE + "\" ");
    sb.append("INVENTORY=\"" + INVENTORY + "\" ");

    return sb.toString();
  }

  public boolean setType(Type type) {
    TYPE = type;
    return true;
  }

  public Type getType() {
    return TYPE;
  }
  
  public boolean setInventory(Inventory inventory) {
    INVENTORY = inventory;
    return true;
  }

  public Inventory getInventory() {
    return INVENTORY;
  }
  public enum Type {
    INSERT, UPDATE, NONE;

    public String getString() {
      switch (this) {
      case INSERT:
        return "I";
      case UPDATE:
        return "U";
      case NONE:
        return "N";
      default:
        return "X";
      }
    }
  }
  
  public enum Inventory {
    MATCH, NO_MATCH, NONE;
  }
}