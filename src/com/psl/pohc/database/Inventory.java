package com.psl.pohc.database;

import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;
import com.psl.pohc.resource.EntityMap;

public class Inventory {

  private final Logger LOGGER = Logger.getLogger(Inventory.class.getName());
  private Tnpm TNPM;

  public Inventory(String instance, String host, String port, String schema,
      String user, String password) throws Exception {
    if (instance.equals("wireless")) {
      TNPM = new TnpmWireless("oracle.jdbc.driver.OracleDriver", host, port,
          schema, user, password);
    } else if (instance.equals("wireline")) {
      TNPM = new TnpmWireline("oracle.jdbc.driver.OracleDriver", host, port,
          schema, user, password);
    } else {
      throw new Exception("Undefined instance.");
    }
    LOGGER.info("Object has initialized.");
  }

  public PohcDefinition lookup(PohcDefinition pohcDefinition) {
    if (TNPM.checkIfExist(pohcDefinition)) {
      pohcDefinition.setInventory(PohcDefinition.Inventory.MATCH);
      LOGGER.finest(String.format(
          "Node name of %s has MATCH within inventory.",
          pohcDefinition.NODE_NAMES));
    } else {
      pohcDefinition.setInventory(PohcDefinition.Inventory.NO_MATCH);
      LOGGER.warning(String.format(
          "Node name of %s has NO MATCH within inventory.",
          pohcDefinition.NODE_NAMES));
    }
    return pohcDefinition;
  }

  public boolean setEntityMap(EntityMap entityMap) {
    return TNPM.setEntityMap(entityMap);
  }

  public boolean init() throws Exception {
    return TNPM.getInventory();
  }
}
