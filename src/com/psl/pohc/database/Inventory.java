package com.psl.pohc.database;

import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class Inventory {

  private final Logger LOGGER = Logger.getLogger(Inventory.class.getName());
  private Tnpm TNPM;

  public Inventory(String instance, String host, String port, String schema,
      String user, String password) throws Exception {
    if (instance.equals("wireless")) {
      TNPM = new TnpmWireless("oracle.jdbc.driver.OracleDriver", host, port,
          schema, user, password);
    } else if (instance.equals("wireless")) {
      TNPM = new TnpmWireline("oracle.jdbc.driver.OracleDriver", host, port,
          schema, user, password);
    } else {
      throw new Exception("Undefined instance.");
    }
    TNPM.getInventory();
    LOGGER.info("Object has initialized.");
  }

  public PohcDefinition lookup(PohcDefinition pohcDefinition) {
    if (TNPM.checkIfExist(pohcDefinition)) {
      pohcDefinition.setInventory(PohcDefinition.Inventory.MATCH);
    }
    return pohcDefinition;
  }
}
