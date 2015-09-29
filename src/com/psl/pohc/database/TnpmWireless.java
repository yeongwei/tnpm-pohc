package com.psl.pohc.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;

import com.psl.pohc.model.Entity;
import com.psl.pohc.model.PohcDefinition;

public class TnpmWireless extends Tnpm {

  private ArrayList<Entity> ENTITIES = new ArrayList<Entity>();
  private ArrayList<PreparedStatement> PREPARED_STATEMENTS = new ArrayList<PreparedStatement>();

  public TnpmWireless(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }

  @Override
  boolean getInventory() throws Exception {
    ENTITIES = this.ENTITY_MAP.flatten();
    for (Entity entity : ENTITIES) {
      StringBuffer sql = new StringBuffer();
      sql.append("SELECT ").append(entity.KEY_COLUMN_NAME).append(", ")
          .append(getKeyColumnWithName(entity.KEY_COLUMN_NAME)).append(" ")
          .append("FROM ").append(entity.TABLE_NAME).append(" ")
          .append("WHERE ").append(entity.KEY_COLUMN_NAME).append(" = ?")
          .append(" ").append("OR ")
          .append(getKeyColumnWithName(entity.KEY_COLUMN_NAME)).append(" = ?");

      PreparedStatement preparedStatement;
      preparedStatement = this.connection.prepareStatement(sql.toString());
      PREPARED_STATEMENTS.add(preparedStatement);
    }
    return true;
  }

  @Override
  boolean checkIfExist(PohcDefinition pohcDefinition) {
    boolean found = false;
    for (PreparedStatement preparedStatement : PREPARED_STATEMENTS) {
      try {
        preparedStatement.setString(1, pohcDefinition.NODE_NAMES);
        preparedStatement.setString(2, pohcDefinition.NODE_NAMES);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
          String ID = resultSet.getString(1);
          String NAME = resultSet.getString(2);
          if (ID.equals(pohcDefinition.NODE_NAMES)
              || NAME.equals(pohcDefinition.NODE_NAMES)) {
            found = true;
            break;
          }
        }
      } catch (SQLSyntaxErrorException ex) {
        /*
        switch (ex.getErrorCode()) {
        case 942:
          LOGGER.warning(String.format(
              "%s does not have table / view exits. Hints from node %s.",
              preparedStatement.toString(), pohcDefinition.NODE_NAMES));
          break;
        default:
          LOGGER.severe(String.format(
              "Database vendor error code %s not defined.", ex.getErrorCode()));
          break;
        }
        */
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    if (!found) {
      LOGGER.warning(String.format("Node %s is undetermined.",
          pohcDefinition.NODE_NAMES));
    }

    return found;
  }

  private String getKeyColumnWithName(String keyColumn) {
    return keyColumn.replace("_ID", "_NAME");
  }
}
