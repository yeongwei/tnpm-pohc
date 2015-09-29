package com.psl.pohc.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.psl.pohc.model.PohcDefinition;

public class TnpmWireline extends Tnpm {

  private PreparedStatement PREPARED_STATEMENT;
  
  public TnpmWireline(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }

  @Override
  boolean getInventory() throws Exception {
    String sql = "SELECT STR_NAME FROM ELMT_DESC WHERE STR_NAME = ?";
    PREPARED_STATEMENT = this.connection.prepareStatement(sql);
    return true;
  }
  
  @Override
  boolean checkIfExist(PohcDefinition pohcDefinition) {
    boolean found = false;
   
    try {
      PREPARED_STATEMENT.setString(1, pohcDefinition.NODE_NAMES);
      ResultSet resultSet = PREPARED_STATEMENT.executeQuery();
      if (resultSet.next()) {
        String node = resultSet.getString(1);
        if (node.equals(pohcDefinition.NODE_NAMES)) {
          found = true;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return found;
  }
}
