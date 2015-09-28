package com.psl.pohc.database;

import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class Tnpm {
  
  private final Logger LOGGER = Logger.getLogger(Tnpm.class.getName());
  
  public Tnpm() {
    LOGGER.info("Object has initialized.");
  }
  
  public PohcDefinition inventoryLookup(PohcDefinition pohcDefinition) {
    pohcDefinition.setInventory(PohcDefinition.Inventory.MATCH);
    return pohcDefinition;
  }
}
