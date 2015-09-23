package com.psl.pohc.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class PohcArchive extends DatabaseInstance {

  private final static String POHC_ARCHIVE_TABLE_FQN = "POHC_ARCHIVE";
  private final static String DATE_FORMAT = "yyyy-mm-dd";
  private final Logger LOGGER = Logger.getLogger(PohcArchive.class.getName()); 
  
  public PohcArchive(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }
  
  public boolean checkIfExist(String id, String outageStart, String outageEnd) {
    boolean exist = false;
    
    StringBuffer sql = new StringBuffer();
    sql
      .append("SELECT ROWNUM RNUM FROM ").append(POHC_ARCHIVE_TABLE_FQN).append(" ")
      .append("WHERE ")
      .append("ID = '").append(id).append("' ")
      .append("AND ")
      .append("OUTAGE_START = TO_DATE('").append(outageStart).append("', '").append(DATE_FORMAT).append("') ")
      .append("AND ")
      .append("OUTAGE_END = TO_DATE('").append(outageEnd).append("', '").append(DATE_FORMAT).append("')");
    
    Statement statement;
    ResultSet resultSet;
    int recordCount = 0;
    
    try {
      statement = this.connection.createStatement();
      LOGGER.info(String.format("About to execute SQL - %s", sql.toString()));
      resultSet = statement.executeQuery(sql.toString());
      
      while (resultSet.next()) {
        recordCount += 1;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    if (recordCount > 0) {
      exist = true;
    }
    
    return exist;
  }
  
  public boolean insert(long timestamp, PohcDefinition pohcDefinition) {
    if (!pohcDefinition.getType().equals(PohcDefinition.Type.INSERT)) {
      LOGGER.warning(
          String.format("Object does not have INSERT type. %s", 
              pohcDefinition.asNameValuePair()));
      return false;
    }
    
    StringBuffer dml = new StringBuffer();
    
    dml
      .append("INSERT INTO ").append(POHC_ARCHIVE_TABLE_FQN).append(" ")
      .append("(RECORDED_TIMESTAMP, TYPE, ID, SUBSYSTEM, REGION, GROUPNAME, PHASE, NODE_NAMES, DOMAIN, OUTAGE_START, OUTAGE_END)").append(" ")
      .append("VALUES (")
      .append(timestamp).append(", ")
      .append("'").append(pohcDefinition.getType().getString()).append("', ")
      .append("'").append(pohcDefinition.ID).append("', ")
      .append("'").append(pohcDefinition.SUBSYSTEM).append("', ")
      .append("'").append(pohcDefinition.REGION).append("', ")
      .append("'").append(pohcDefinition.GROUPNAME).append("', ")
      .append("'").append(pohcDefinition.PHASE).append("', ")
      .append("'").append(pohcDefinition.NODE_NAMES).append("', ")
      .append("'").append(pohcDefinition.DOMAIN).append("', ")
      .append("TO_DATE('").append(pohcDefinition.OUTAGE_START).append("', '").append(DATE_FORMAT).append("'), ")
      .append("TO_DATE('").append(pohcDefinition.OUTAGE_END).append("', '").append(DATE_FORMAT).append("')")
      .append(")");
    
    Statement statement;
    int result = -1;
    boolean status = false;
    
    try {
      statement = this.connection.createStatement();
      LOGGER.info(String.format("About to execute SQL - %s", dml.toString()));
      result = statement.executeUpdate(dml.toString());
    } catch (Exception ex) {
      ex.printStackTrace(); 
      return false;
    }
    
    if (result == 1) {
      status = true;
    }
    
    return status;
  }
}
