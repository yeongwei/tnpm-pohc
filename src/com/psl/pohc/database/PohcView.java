package com.psl.pohc.database;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class PohcView extends DatabaseInstance {
  
  private final static String POHC_VIEW_FQN = "POHC_VIEW";
  private final Logger LOGGER = Logger.getLogger(PohcView.class.getName());
  
  private String DELIMITER_REGEXP = "\\r?\\n";
  
  public PohcView(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
    LOGGER.info("Object has initialized successfully.");
  }
  
  public boolean insert(PohcDefinition pohcDefinition) {
    StringBuffer insertSql = new StringBuffer();
    insertSql
      .append("INSERT INTO ").append(POHC_VIEW_FQN).append(" ")
      .append("(ID, SUBSYSTEM, REGION, SYSTEM, GROUPNAME, PHASE, NODE_NAME, STATUS, DOMAIN, OUTAGE_START, OUTAGE_END) ").append(" ")
      .append("VALUES (")
      .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?")
      .append(")");
    ArrayList<PohcDefinition> pohcDefinitions = parse(pohcDefinition);
    
    PreparedStatement preparedStatement;
    
    try {
      LOGGER.info(String.format("Preparing statement for SQL - %s", insertSql));
      preparedStatement = connection.prepareStatement(insertSql.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    
    try {
      LOGGER.info("Setting prepared statement");
      for (PohcDefinition df : pohcDefinitions) {
        preparedStatement.setString(1, df.ID);
        preparedStatement.setString(2, df.SUBSYSTEM);
        preparedStatement.setString(3, df.REGION);
        preparedStatement.setString(4, df.SYSTEM);
        preparedStatement.setString(5, df.GROUPNAME);
        preparedStatement.setString(6, df.PHASE);
        preparedStatement.setString(7, df.NODE_NAMES);
        preparedStatement.setString(8, df.STATUS);
        preparedStatement.setString(9, df.DOMAIN);
        preparedStatement.setDate(10, df.OUTAGE_START);
        preparedStatement.setDate(11, df.OUTAGE_END);
        
        preparedStatement.addBatch();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    
    try {
      LOGGER.info("Executing prepared statement");
      executedResult = preparedStatement.executeBatch(); 
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    
    return true;
  }
  
  public ArrayList<PohcDefinition> parse(PohcDefinition pohcDefinition) {
    ArrayList<PohcDefinition> x = new ArrayList<PohcDefinition>();
    String[] exploded = explode(pohcDefinition.NODE_NAMES);
    for (String y : exploded) {
      x.add(
          new PohcDefinition(
              pohcDefinition.ID, pohcDefinition.SUBSYSTEM, pohcDefinition.REGION,
              pohcDefinition.SYSTEM, pohcDefinition.GROUPNAME, pohcDefinition.PHASE,
              y, pohcDefinition.STATUS, pohcDefinition.DOMAIN, pohcDefinition.OUTAGE_START,
              pohcDefinition.OUTAGE_END));
    }
    return x;
  }
  
  public String[] explode(String value) {
    String[] exploded = value.split(DELIMITER_REGEXP);
    return exploded;
  }
  
  public boolean setDelimiter(String regexp) {
    DELIMITER_REGEXP = regexp;
    return true;
  }
  
  public String getDelimiter() {
    return DELIMITER_REGEXP;
  }
}
