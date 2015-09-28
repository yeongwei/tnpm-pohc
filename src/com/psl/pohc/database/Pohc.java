package com.psl.pohc.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class Pohc extends DatabaseInstance {

  private final Logger LOGGER = Logger.getLogger(Pohc.class.getName());
  
  public Pohc(String host, String port, String sid,
      String user, String password) throws Exception {
    this("oracle.jdbc.driver.OracleDriver", host, port, sid, user, password);
  }
  
  public Pohc(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
    this.TABLE_FQN = "SMDBUSER3.UFM_OUTAGE";
    LOGGER.info("Object has initialized successfully.");
  }

  public ArrayList<PohcDefinition> getPohcRecords(int limit, int offset) throws Exception {
    ArrayList<PohcDefinition> x = new ArrayList<PohcDefinition>();
    
    StringBuffer sql = new StringBuffer();
    
    sql
      .append("SELECT * FROM ( ")
      .append("SELECT ")
      .append("ROWNUM RNUM, ID, SUBSYSTEM, REGION, SYSTEM, GROUPNAME, PHASE, NODE_NAMES, STATUS, DOMAIN, OUTAGE_START, OUTAGE_END ")
      .append("FROM ").append(TABLE_FQN).append(" ")
      .append(") tbl ");
      
    if (limit > 0 && offset > 0) {
      sql
        .append("WHERE ")
        .append("RNUM > ").append(offset).append(" ")
        .append("AND ")
        .append("RNUM <= ").append(offset + limit);
    } else {
      LOGGER.warning("Query does not have limit and offest predicates.");
    }
    
    LOGGER.finest(String.format("About to prepare SQL - %s", sql.toString()));
    
    Statement statement;
    ResultSet resultSet;
    
    try {
      statement = this.connection.createStatement();
      resultSet = statement.executeQuery(sql.toString());
      
      while (resultSet.next()) {
        x.add(parsePohcRecord(resultSet));
      }
      LOGGER.info(
          String.format("%d record object(s) created.", x.size()));
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } 
    
    return x;
  }
  
  private PohcDefinition parsePohcRecord(ResultSet rs) throws Exception {    
    return new PohcDefinition(
        rs.getString("ID"),
        rs.getString("SUBSYSTEM"),
        rs.getString("REGION"),
        rs.getString("SYSTEM"),
        rs.getString("GROUPNAME"),
        rs.getString("PHASE"),
        rs.getString("NODE_NAMES"),
        rs.getString("STATUS"),
        rs.getString("DOMAIN"),
        rs.getDate("OUTAGE_START"),
        rs.getDate("OUTAGE_END")
        );
  }
  
  public int getTotaNumberOfRows() {
    StringBuffer sql = new StringBuffer();
    sql
      .append("SELECT MAX(RNUM) as MAX_RNUM FROM (")
      .append("SELECT ROWNUM RNUM FROM ").append(TABLE_FQN)
      .append(") tbl");
    
    Statement statement;
    ResultSet resultSet;
    
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sql.toString());
      resultSet.next();
      return resultSet.getInt("MAX_RNUM");
    } catch (Exception ex) {
      ex.printStackTrace();
      return 0;
    }
  }
}
