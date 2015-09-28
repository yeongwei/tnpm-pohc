package com.psl.pohc.database;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;
import com.psl.pohc.resource.EntityMap;

public abstract class Tnpm  extends DatabaseInstance {

  protected EntityMap ENTITY_MAP;
  protected ArrayList<String> INVENTORY_STORE;
  protected Logger LOGGER = Logger.getLogger(Tnpm.class.getName());
  
  public Tnpm(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }

  boolean setEntityMap(EntityMap entityMap) {
    ENTITY_MAP = entityMap;
    return true;
  }
  
  boolean getInventory() {
    return true;
  }

  void reloadInventory() {
  }

  boolean checkIfExist(PohcDefinition pohcDefinition) {    
    return INVENTORY_STORE.contains(pohcDefinition.NODE_NAMES);
  }
}
