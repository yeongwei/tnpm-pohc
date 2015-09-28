package com.psl.pohc.resource;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TestEntityMap {
  private static String ENTITY_FILE_NAME = 
      "D:\\development\\_assignment\\pohc\\src\\EntityMap.csv";
  private EntityMap ENTITY_MAP;
  
  public TestEntityMap() throws Exception {
    ENTITY_MAP = new EntityMap(ENTITY_FILE_NAME);
  }
  
  @Test public void testEntityMapCreation() {
    assertTrue(ENTITY_MAP != null);
  }
  
  @Test public void testEntityMapHasMobileCore() {
    assertTrue(ENTITY_MAP.hasEntity("mobileCore"));
    assertTrue(ENTITY_MAP.hasEntity("Mobilecore"));
    ArrayList<Entity> x = ENTITY_MAP.getEntitiesFor("mobileCore");
    assertTrue(x.size() > 0);
  }
}
