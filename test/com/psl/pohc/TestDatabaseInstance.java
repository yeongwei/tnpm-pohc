package com.psl.pohc;

import java.util.ArrayList;

import org.junit.Test;

import com.psl.pohc.database.DatabaseInstance;
import com.psl.pohc.database.Pohc;
import com.psl.pohc.model.PohcDefinition;

public class TestDatabaseInstance {
  
  @Test public void testDatabaseInstanceCreation() throws Exception {
    DatabaseInstance dbI = new DatabaseInstance(
        "oracle.jdbc.driver.OracleDriver", 
        "10.211.50.18", 
        "1521",
        "vtdb",
        "virtuo", 
        "Virtuo01");
    assert(dbI.isActive());
  }
  
  @Test public void testPohc() throws Exception {
    Pohc pohc = new Pohc(
        "oracle.jdbc.driver.OracleDriver", 
        "localhost", 
        "1522",
        "smdbdev",
        "hkamali", 
        "hkamali");
    assert(pohc.isActive());
    
    ArrayList<PohcDefinition> x = pohc.getPohcRecords(0, 0);
    assert(x.size() < 0);
    
    assert(x.get(0).OUTAGE_START.matches("dddd-dd-dd"));
    assert(x.get(0).OUTAGE_END.matches("dddd-dd-dd"));
    
    x = pohc.getPohcRecords(1, 1);
    assert(x.size() == 1);
  }
}
