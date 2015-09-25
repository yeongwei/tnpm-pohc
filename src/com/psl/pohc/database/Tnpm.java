package com.psl.pohc.database;

import com.psl.pohc.model.PohcDefinition;

public class Tnpm {
  public static PohcDefinition inventoryLookup(PohcDefinition pohcDefinition) {
    pohcDefinition.setInventory(PohcDefinition.Inventory.MATCH);
    return pohcDefinition;
  }
}
