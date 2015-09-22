package com.psl.pohc;

import java.util.ArrayList;

import org.junit.Test;

import com.psl.pohc.model.HashStore;

public class TestHashStore {
  
  private static final HashStore TEST_STORE  = new HashStore();
  
  @Test public void TestHasKey () {
    Object obj = TEST_STORE.hasKey("sampleKey");
    assert(obj instanceof ArrayList);
    
    ArrayList<String> x = (ArrayList<String>) obj;
    assert(x.size() == 0);
  }
  
  @Test public void TestAddEntry() {
    assert(TEST_STORE.addEntry("company", "PSL").contains("PSL"));
  }
  
  @Test public void TestHasValue() {
    assert(TEST_STORE.hasValue("PSL").equals("company"));
  }
  
  @Test public void TestAddEntry2() {
    assert(TEST_STORE.addEntry("company", "IBM").contains("IBM"));
  }
  
  @Test public void TestHasValue2() {
    assert(TEST_STORE.hasValue("IBM").equals("company"));
  }
  
  @Test public void TestRemoveEntry() {
    assert(!TEST_STORE.removeEntry("company", "PSL").contains("PSL"));
    assert(!TEST_STORE.get("company").contains("PSL"));
  }
  
  @Test public void TestRemoveEntry2() {
    assert(TEST_STORE.removeEntry("company"));
    assert(!TEST_STORE.containsKey("company"));
  }
}
