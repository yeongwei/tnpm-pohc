package com.psl.pohc.resource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationMap {
  private final static String CONFIGURATION_PROPERTY = "configuration.file";
  private final Logger LOGGER = Logger.getLogger(ConfigurationMap.class
      .getName());
  private Properties CONFIGURATION;

  public ConfigurationMap() throws Exception {
    this(System.getProperty(CONFIGURATION_PROPERTY));
  }

  public ConfigurationMap(String configurationFileName) throws Exception {
    try {
      LOGGER.info(String.format("About to process %s.", configurationFileName));
      File configurationFile = new File(configurationFileName);
      FileInputStream fileInputStream = new FileInputStream(configurationFile);

      CONFIGURATION = new Properties();
      CONFIGURATION.load(fileInputStream);

      for (Key k : Key.values()) {
        if (!k.needsValidation()) {
          LOGGER
              .info(String.format("Skipping validation for %s.", k.getName()));
          continue;
        }

        LOGGER.info(String.format("Attempt to validate configuration with %s.",
            k.getName()));
        if (CONFIGURATION.containsKey(k.getName())) {
          if (k.equals(Key.TNPM_INSTANCE)) {
            if (!(CONFIGURATION.get(k.getName()).toString().equals("wireless") || CONFIGURATION
                .get(k.getName()).toString().equals("wireline"))) {
              LOGGER
                  .info("tnpm.instance only expect value either wireless or wireline.");
              throw new Exception(k.getMessage());
            }
          }
        } else {
          LOGGER.info(String.format("%s is not found in configuration.",
              k.getName()));
          throw new Exception(k.getMessage());
        }
      }

      for (Default d : Default.values()) {
        LOGGER.info(String.format("About to evaluate default value for %s.",
            Key.valueOf(d.name()).getName()));

        if (CONFIGURATION.containsKey(Key.valueOf(d.name()).getName())) {
          LOGGER.info(String.format("Skipping default value for %s.", Key
              .valueOf(d.name()).getName()));
          continue;
        }

        CONFIGURATION.put(Key.valueOf(d.name()).getName(), d.getValue());
      }

      for (Object x : CONFIGURATION.values()) {
        LOGGER.info("DEBUG" + x.toString());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Exception(ex.getMessage());
    }
  }

  public String get(String key) {
    if (CONFIGURATION.get(key) == null) {
      if (Default.valueOf(key) == null) {
        return null;
      } else {
        return null;
      }
    } else {
      return CONFIGURATION.get(key).toString();
    }
  }

  protected enum Key {
    POHC_HOST, POHC_DB_PORT, POHC_DB_USER, POHC_DB_PASSWORD, POHC_DB_SCHEMA, TNPM_HOST, TNPM_DB_PORT, TNPM_DB_USER, TNPM_DB_PASSWORD, TNPM_DB_SCHEMA, TNPM_INSTANCE, CONFIG_DELIMITER, CONFIG_BATCH_SIZE, CONFIG_COMMIT;

    String getName() {
      switch (this) {
      case POHC_HOST:
        return "pohc.host";
      case POHC_DB_PORT:
        return "pohc.db.port";
      case POHC_DB_USER:
        return "pohc.db.user";
      case POHC_DB_PASSWORD:
        return "pohc.db.password";
      case POHC_DB_SCHEMA:
        return "pohc.db.schema";
      case TNPM_HOST:
        return "tnpm.host";
      case TNPM_DB_PORT:
        return "tnpm.db.port";
      case TNPM_DB_USER:
        return "tnpm.db.user";
      case TNPM_DB_PASSWORD:
        return "tnpm.db.password";
      case TNPM_DB_SCHEMA:
        return "tnpm.db.schema";
      case TNPM_INSTANCE:
        return "tnpm.instance";
      case CONFIG_DELIMITER:
        return "config.delimiter";
      case CONFIG_BATCH_SIZE:
        return "config.batch.size";
      case CONFIG_COMMIT:
        return "config.commit";
      default:
        return null;
      }
    }

    String getMessage() {
      switch (this) {
      case POHC_HOST:
        return "Please provide the POHC hostname / ip.";
      case POHC_DB_PORT:
        return "Please provide the POHC database port.";
      case POHC_DB_USER:
        return "Please provide the POHC database username.";
      case POHC_DB_PASSWORD:
        return "Please provide the POHC database password.";
      case POHC_DB_SCHEMA:
        return "Please provide the POHC database schema to accesss.";
      case TNPM_HOST:
        return "Please provide the TNPM hostname / ip";
      case TNPM_DB_PORT:
        return "Please provide the TNPM database port.";
      case TNPM_DB_USER:
        return "Please provide the TNPM database username.";
      case TNPM_DB_PASSWORD:
        return "Please provide the TNPM database password.";
      case TNPM_DB_SCHEMA:
        return "Please provide the TNPM database schema to access.";
      case TNPM_INSTANCE:
        return "Please provide the TNPM instance (E.g. wireline / wireless).";
      default:
        return null;
      }
    }

    boolean needsValidation() {
      switch (this) {
      case POHC_HOST:
        return true;
      case POHC_DB_PORT:
        return true;
      case POHC_DB_USER:
        return true;
      case POHC_DB_PASSWORD:
        return true;
      case POHC_DB_SCHEMA:
        return true;
      case TNPM_HOST:
        return true;
      case TNPM_DB_PORT:
        return true;
      case TNPM_DB_USER:
        return true;
      case TNPM_DB_PASSWORD:
        return true;
      case TNPM_DB_SCHEMA:
        return true;
      case TNPM_INSTANCE:
        return true;
      default:
        return false;
      }
    }
  }

  protected enum Default {
    CONFIG_BATCH_SIZE, CONFIG_COMMIT;

    String getValue() {
      switch (this) {
      case CONFIG_BATCH_SIZE:
        return "1000";
      case CONFIG_COMMIT:
        return "false";
      default:
        return null;
      }
    }
  }
}
