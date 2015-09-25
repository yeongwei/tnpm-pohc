package com.psl.pohc.resource;

import org.junit.Test;

import com.psl.pohc.resource.EntityMap;

public class TestEntityMap {
  
  EntityMap ENTITY_MAP;
  
  public TestEntityMap() {
    ENTITY_MAP = new EntityMap();
  }
  
  @Test public void testEntityMapCreation() {
    assert(ENTITY_MAP != null);
  }
  
  @Test public void testEntityMapHasMobileCore() {
    assert(ENTITY_MAP.hasEntity("mobileCore"));
    assert(ENTITY_MAP.hasEntity("Mobilecore"));
    assert(ENTITY_MAP.getEntitiesFor("mobileCore").size() > 0);
  }
}
