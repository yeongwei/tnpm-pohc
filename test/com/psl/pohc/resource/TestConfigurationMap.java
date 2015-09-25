package com.psl.pohc.resource;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConfigurationMap {
  private static final String configurationFile = 
      "D:\\development\\_assignment\\pohc\\test\\configuration.properties";
  private final ConfigurationMap CONFIGURATION_MAP = 
      new ConfigurationMap(configurationFile);
  
  @Test public void testHasError() {
    assertFalse(CONFIGURATION_MAP.hasError());
  }
  
  @Test public void testGet() {
    assertTrue(CONFIGURATION_MAP.get("pohc.host") != null);
  }
}
