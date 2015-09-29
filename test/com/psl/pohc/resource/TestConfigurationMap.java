package com.psl.pohc.resource;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConfigurationMap {
  private static final String configurationFile = 
      "D:\\development\\_assignment\\pohc\\resource\\configuration.properties";
  private final ConfigurationMap CONFIGURATION_MAP;
  
  public TestConfigurationMap() throws Exception {
    CONFIGURATION_MAP = new ConfigurationMap(configurationFile);
  }
  
  @Test public void testGet() {
    assertTrue(CONFIGURATION_MAP.get("pohc.host") != null);
  }
}
