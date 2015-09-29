package com.psl.pohc.database;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class PohcView extends DatabaseInstance {

  private final Logger LOGGER = Logger.getLogger(PohcView.class.getName());

  private String DELIMITER_REGEXP = "\\r?\\n";

  public PohcView(String host, String port, String sid, String user,
      String password) throws Exception {
    this("oracle.jdbc.driver.OracleDriver", host, port, sid, user, password);
  }

  public PohcView(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
    this.TABLE_FQN = "POHC_VIEW";
    LOGGER.info("Object has initialized successfully.");
  }

  public boolean execute(PohcDefinition pohcDefinition) {
    if (pohcDefinition.getType().equals(PohcDefinition.Type.INSERT)) {
      return insert(pohcDefinition);
    } else if (pohcDefinition.getType().equals(PohcDefinition.Type.UPDATE)) {
      return update(pohcDefinition);
    } else {
      return false;
    }
  }

  public boolean insert(PohcDefinition pohcDefinition) {
    StringBuffer insertSql = new StringBuffer();
    insertSql
        .append("INSERT INTO ")
        .append(TABLE_FQN)
        .append(" ")
        .append(
            "(ID, SUBSYSTEM, REGION, SYSTEM, GROUPNAME, PHASE, NODE_NAME, STATUS, DOMAIN, OUTAGE_START, OUTAGE_END) ")
        .append(" ").append("VALUES (")
        .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?").append(")");

    PreparedStatement preparedStatement;

    try {
      LOGGER.finest(String.format("Preparing statement for SQL - %s", insertSql));
      preparedStatement = connection.prepareStatement(insertSql.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

    try {
      LOGGER.finest("Setting prepared statement");

      preparedStatement.setString(1, pohcDefinition.ID);
      preparedStatement.setString(2, pohcDefinition.SUBSYSTEM);
      preparedStatement.setString(3, pohcDefinition.REGION);
      preparedStatement.setString(4, pohcDefinition.SYSTEM);
      preparedStatement.setString(5, pohcDefinition.GROUPNAME);
      preparedStatement.setString(6, pohcDefinition.PHASE);
      preparedStatement.setString(7, pohcDefinition.NODE_NAMES);
      preparedStatement.setString(8, pohcDefinition.STATUS);
      preparedStatement.setString(9, pohcDefinition.DOMAIN);
      preparedStatement.setDate(10, pohcDefinition.OUTAGE_START);
      preparedStatement.setDate(11, pohcDefinition.OUTAGE_END);

      preparedStatement.addBatch();
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

    try {
      LOGGER.finest("Executing prepared statement");
      preparedStatement.executeBatch();
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * FIXME: Need to find out what is updatable and what is not
   * 
   * @param pohcDefinition
   * @return
   */
  public boolean update(PohcDefinition pohcDefinition) {
    StringBuffer updateSql = new StringBuffer();
    updateSql.append("UPDATE ").append(TABLE_FQN).append(" SET ")
        .append("PHASE = ?").append(", ").append("STATUS = ?").append(", ")
        .append("OUTAGE_START = ?").append(", ").append("OUTAGE_END = ?")
        .append(" ").append("WHERE ID = ?");

    PreparedStatement preparedStatement;

    try {
      LOGGER.finest(String.format("Preparing statement for SQL - %s", updateSql));
      preparedStatement = connection.prepareStatement(updateSql.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

    try {
      LOGGER.finest("Setting prepared statement");
      
      preparedStatement.setString(1, pohcDefinition.PHASE);
      preparedStatement.setString(2, pohcDefinition.STATUS);
      preparedStatement.setDate(3, pohcDefinition.OUTAGE_START);
      preparedStatement.setDate(4, pohcDefinition.OUTAGE_END);
      preparedStatement.setString(5, pohcDefinition.ID);

      preparedStatement.addBatch();
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

    try {
      LOGGER.info("Executing prepared statement");
      preparedStatement.executeBatch();
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
      String polishedRecord = y.replaceAll("^\\s+", "");
      polishedRecord = polishedRecord.replaceAll("\\s+$", "");
      
      PohcDefinition newDefinition = new PohcDefinition(pohcDefinition.ID,
          pohcDefinition.SUBSYSTEM, pohcDefinition.REGION,
          pohcDefinition.SYSTEM, pohcDefinition.GROUPNAME,
          pohcDefinition.PHASE, polishedRecord, pohcDefinition.STATUS,
          pohcDefinition.DOMAIN, pohcDefinition.OUTAGE_START,
          pohcDefinition.OUTAGE_END);
      newDefinition.setType(pohcDefinition.getType());
      x.add(newDefinition);
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
