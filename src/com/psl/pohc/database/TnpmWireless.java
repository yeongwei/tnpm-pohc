package com.psl.pohc.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.psl.pohc.model.Entity;

public class TnpmWireless extends Tnpm {

  public TnpmWireless(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }

  @Override
  boolean getInventory() {
    this.INVENTORY_STORE = new ArrayList<String>();

    for (Entity e : this.ENTITY_MAP.flatten()) {
      StringBuffer sql = new StringBuffer();
      sql.append("SELECT ").append(e.KEY_COLUMN_NAME).append(" FROM ")
          .append(e.TABLE_NAME);

      try {
        Statement statement;
        ResultSet resultSet;
        try {
          statement = this.connection.createStatement();
          resultSet = statement.executeQuery(sql.toString());

          while (resultSet.next()) {
            INVENTORY_STORE.add(resultSet.getString(e.KEY_COLUMN_NAME));
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    this.LOGGER.info(String.format("Found %d inventory object(s).",
        INVENTORY_STORE.size()));
    
    return true;
  }
}
