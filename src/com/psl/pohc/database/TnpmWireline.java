package com.psl.pohc.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TnpmWireline extends Tnpm {

  public TnpmWireline(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }
  
  @Override
  boolean getInventory() {
    this.INVENTORY_STORE = new ArrayList<String>();
    String SQL = "SELECT STR_NAME FROM SE_DESC ROWNUM = 10";
    
    Statement statement;
    ResultSet resultSet;
    try {
      statement = this.connection.createStatement();
      resultSet = statement.executeQuery(SQL);
      
      while (resultSet.next()) {
        INVENTORY_STORE.add(resultSet.getString("STR_NAME"));
      }
    } catch (Exception ex) {
      
    }
    
    return true;
  }
}
