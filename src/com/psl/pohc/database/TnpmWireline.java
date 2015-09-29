package com.psl.pohc.database;

import com.psl.pohc.model.PohcDefinition;

public class TnpmWireline extends Tnpm {

  public TnpmWireline(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
  }
  
  @Override
  boolean checkIfExist(PohcDefinition pohcDefinition) {
    return true;
  }
}
