package com.psl.pohc.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import com.psl.pohc.Main;
import com.psl.pohc.resource.ConfigurationMap;

public class Debug {

  private final Logger LOGGER = Logger.getLogger(Main.class.getName());
  private ConfigurationMap CONFIGURATION_MAP;
  private Pohc POHC;

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      throw new Exception("No option as input provided.");
    }

    Debug d = new Debug();
    d.run(args);
  }

  public Debug() throws Exception {
    CONFIGURATION_MAP = new ConfigurationMap();

    POHC = new Pohc(CONFIGURATION_MAP.get("pohc.host"),
        CONFIGURATION_MAP.get("pohc.db.port"),
        CONFIGURATION_MAP.get("pohc.db.schema"),
        CONFIGURATION_MAP.get("pohc.db.user"),
        CONFIGURATION_MAP.get("pohc.db.password"));
    POHC.setTableName(CONFIGURATION_MAP.get("pohc.db.table"));

    LOGGER.info("Object initialized successfully.");
  }

  public void run(String... option) throws Exception {
    LOGGER.info(String.format("About to run %s.", option[0]));
    Connection connection;
    DatabaseMetaData databaseMetadata;
    Statement statement;
    ResultSet resultSet;
    boolean status;
    switch (Option.valueOf(option[0])) {
    case POHC_SHOW_ALL_TABLE_AND_VIEWS_FROM_SMDBUSER3:
      connection = POHC.connection;
      databaseMetadata = connection.getMetaData();
      resultSet = databaseMetadata.getTables(null, "SMDBUSER3", null,
          null);
      while (resultSet.next()) {
        StringBuffer result = new StringBuffer();
        result.append("TABLE_SCHEM|_|")
            .append(resultSet.getString("TABLE_SCHEM")).append("|_|")
            .append("TABLE_NAME|_|").append(resultSet.getString("TABLE_NAME"));
        LOGGER.info(result.toString());
      }
      break;
    case POHC_SHOW_ALL_TABLE_AND_VIEWS_FROM_SMDBUSER:
      connection = POHC.connection;
      databaseMetadata = connection.getMetaData();
      resultSet = databaseMetadata.getTables(null, "SMDBUSER", null,
          null);
      while (resultSet.next()) {
        StringBuffer result = new StringBuffer();
        result.append("TABLE_SCHEM|_|")
            .append(resultSet.getString("TABLE_SCHEM")).append("|_|")
            .append("TABLE_NAME|_|").append(resultSet.getString("TABLE_NAME"));
        LOGGER.info(result.toString());
      }
      break;
    case POHC_SELECT_FROM_TABLE_BASED_ON_NAME:
      connection = POHC.connection;
      statement = connection.createStatement();
      LOGGER.info("About to execute Test SQL.");
      resultSet = statement.executeQuery("SELECT ROWNUM RNUM FROM " + option[1]);
      LOGGER.info("About iterate results.");
      int count = 1;
      while (resultSet.next()) {
        StringBuffer result = new StringBuffer();
        result.append(count).append("|_|").append(resultSet.getObject(1));
        LOGGER.info(result.toString());
        count += 1;
      }
      break;
    case JDBC: // supports oracle only
      LOGGER.info("Only Oracle supported for now.");
      // CMD - JDBC HOST_NAME PORT SID USERNAME PASSWORD SQL
      Properties info = new Properties();
      info.put("user", option[4]);
      info.put("password", option[5]);
      connection = DriverManager.getConnection(
          Jdbc.getUrl("oracle.jdbc.driver.OracleDriver", option[1], option[2], option[3]), 
          info);
      statement = connection.createStatement();
      status = statement.execute(option[6]);
      if (status) {
        LOGGER.info("Execution has Result Set.");
      } else {
        LOGGER.info("Execution has NO Result Set.");
      }
      break;
    default:
      LOGGER.warning("There are not matching options.");
      break;
    }
  }

  enum Option {
    POHC_SHOW_ALL_TABLE_AND_VIEWS_FROM_SMDBUSER3, POHC_SHOW_ALL_TABLE_AND_VIEWS_FROM_SMDBUSER,
    POHC_SELECT_FROM_TABLE_BASED_ON_NAME, JDBC;
  }
}
