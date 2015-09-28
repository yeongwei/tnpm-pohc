package com.psl.pohc;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.psl.pohc.model.PohcDefinition;

public class Debug {
  
  private final static Logger LOGGER = Logger.getLogger(Debug.class.getName()); 
  
  public static void dumpPohcDefinition(PohcDefinition pohcDefinitions) {
    LOGGER.finest(pohcDefinitions.asNameValuePair());
  }
  
  public static void dumpPohcDefinition(ArrayList<PohcDefinition> pohcDefinitions) {
    for (PohcDefinition df : pohcDefinitions) {
      LOGGER.finest(df.asNameValuePair());
    }
  }
}
