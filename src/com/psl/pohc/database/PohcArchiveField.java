package com.psl.pohc.database;

public enum PohcArchiveField {
  RECORDED_DATE_TIME, SUB_SYSTEM, REGION, SYSTEM, GROUP_NAME, ID, PHASE, NODE_NAMES, STATUS, DOMAIN, OUTAGE_START, OUTAGE_END;

  /**Colume names used in POHC_VIEW*/
  public String getColumnName() {
    switch (this) {
    case RECORDED_DATE_TIME:
      return "RECORDED_DATE_TIME";
    case SUB_SYSTEM:
      return "SUBSYSTEM";
    case REGION:
      return "REGION";
    case SYSTEM:
      return "SYSTEM";
    case GROUP_NAME:
      return "GROUPNAME";
    case ID:
      return "ID";
    case PHASE:
      return "PHASE";
    case NODE_NAMES:
      return "NODE_NAMES";
    case STATUS:
      return "STATUS";
    case DOMAIN:
      return "DOMAIN";
    case OUTAGE_START:
      return "OUTAGE_START";
    case OUTAGE_END:
      return "OUTAGE_END";
    default:
      return null;
    }
  }
}