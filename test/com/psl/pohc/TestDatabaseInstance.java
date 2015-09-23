package com.psl.pohc;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.psl.pohc.database.DatabaseInstance;
import com.psl.pohc.database.Pohc;
import com.psl.pohc.database.PohcArchive;
import com.psl.pohc.database.PohcView;
import com.psl.pohc.model.PohcDefinition;

public class TestDatabaseInstance {
  
  DatabaseInstance DBI;
  Pohc POHC;
  PohcArchive POHC_ARCHIVE; 
  PohcView POHC_VIEW;
  
  public TestDatabaseInstance() throws Exception {
    DBI = new DatabaseInstance(
        "oracle.jdbc.driver.OracleDriver", 
        "10.211.50.18", 
        "1521",
        "vtdb",
        "virtuo", 
        "Virtuo01");
    
    POHC = new Pohc(
        "oracle.jdbc.driver.OracleDriver", 
        "localhost", 
        "1522",
        "smdbdev",
        "hkamali", 
        "hkamali");
    
    POHC_ARCHIVE = new PohcArchive(
        "oracle.jdbc.driver.OracleDriver", 
        "10.211.50.18", 
        "1521",
        "vtdb",
        "virtuo", 
        "Virtuo01");
    
    POHC_VIEW = new PohcView(
        "oracle.jdbc.driver.OracleDriver", 
        "10.211.50.18", 
        "1521",
        "vtdb",
        "virtuo", 
        "Virtuo01");
  }
  
  @Test public void testDatabaseInstanceCreation() throws Exception {
    assert(DBI.isActive());
  }
  
  @Test public void testPohc() throws Exception {
    assert(POHC.isActive());
    
    ArrayList<PohcDefinition> x = POHC.getPohcRecords(0, 0);
    assert(x.size() < 0);
    
    assert(x.get(0).OUTAGE_START.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d"));
    assert(x.get(0).OUTAGE_END.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d"));
    
    x = POHC.getPohcRecords(1, 1);
    assert(x.size() == 1);
  }
  
  @Test public void testPohcArchive() throws Exception {
    ArrayList<PohcDefinition> x = POHC.getPohcRecords(0, 0);
    assert(x.size() < 0);
    
    PohcDefinition df = x.get(0);
    boolean found = 
        POHC_ARCHIVE.checkIfExist(df.ID,df.OUTAGE_START, df.OUTAGE_END);
    assert(!found);
    
    long timestamp = new Date().getTime();
    boolean status = POHC_ARCHIVE.insert(timestamp, df);
    assert(!status);
    
    df.setType(PohcDefinition.Type.INSERT);
    assert(df.getType().equals(PohcDefinition.Type.INSERT));
    
    status = POHC_ARCHIVE.insert(timestamp, df);
    assert(status);
    
    status = POHC_ARCHIVE.commit();
    assert(status);
  }
  
  @Test public void testPohcView() throws Exception {
    String[] x = POHC_VIEW.explode("Hello World\nGood day!");
    assert(x.length == 2);
    
    x = POHC_VIEW.explode("Hello World\r\nGood day!");
    assert(x.length == 2);
  }
}
