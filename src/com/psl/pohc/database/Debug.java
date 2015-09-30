package com.psl.pohc.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
    d.run(args[0]);
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

  public void run(String option) throws Exception {
    LOGGER.info(String.format("About to run %s.", option));
    switch (Option.valueOf(option)) {
    case POHC_SHOW_ALL_TABLE_AND_VIEWS_FROM_SMDBUSER3:
      Connection connection = POHC.connection;
      DatabaseMetaData databaseMetadata = connection.getMetaData();
      ResultSet resultSet = databaseMetadata.getTables(
            null, "SMDBUSER3", null, null);
      while(resultSet.next()) {
        StringBuffer result = new StringBuffer();
        result
          .append("TABLE_SCHEM|_|").append(resultSet.getString("TABLE_SCHEM"))
          .append("|_|")
          .append("TABLE_NAME|_|").append(resultSet.getString("TABLE_NAME"));
        LOGGER.info(result.toString());
      }
      break;
    default:
      LOGGER.warning("There are not matching options.");
      break;
    }
  }

  enum Option {
    POHC_SHOW_ALL_TABLE_AND_VIEWS_FROM_SMDBUSER3
  }
}
