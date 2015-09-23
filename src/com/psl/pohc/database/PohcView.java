package com.psl.pohc.database;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class PohcView extends DatabaseInstance {
  
  private final static String POHC_VIEW_FQN = "POHC_VIEW";
  private final static String DATE_FORMAT = "yyyy-mm-dd";
  private final Logger LOGGER = Logger.getLogger(PohcView.class.getName());
  
  private String DELIMITER_REGEXP = "\\r?\\n";
  
  public PohcView(String driverClass, String host, String port, String sid,
      String user, String password) throws Exception {
    super(driverClass, host, port, sid, user, password);
    LOGGER.info("Object has initialized successfully.");
  }
  
  public void insert(PohcDefinition pohcDefinition) {
    
  }
  
  public ArrayList<PohcDefinition> parse(PohcDefinition pohcDefinition) {
    ArrayList<PohcDefinition> x = new ArrayList<PohcDefinition>();
    String[] exploded = explode(pohcDefinition.NODE_NAMES);
    for (String y : exploded) {
      x.add(
          new PohcDefinition(
              pohcDefinition.ID, pohcDefinition.SUBSYSTEM, pohcDefinition.REGION,
              pohcDefinition.SYSTEM, pohcDefinition.GROUPNAME, pohcDefinition.PHASE,
              y, pohcDefinition.STATUS, pohcDefinition.DOMAIN, pohcDefinition.OUTAGE_START,
              pohcDefinition.OUTAGE_END));
    }
    return x;
  }
  
  public String[] explode(String value) {
    String[] exploded = value.split(DELIMITER_REGEXP);
    return exploded;
  }
  
  public boolean setDelimiter(String regexp) {
    DELIMITER_REGEXP = regexp;
    return true;
  }
  
  public String getDelimiter() {
    return DELIMITER_REGEXP;
  }
}
