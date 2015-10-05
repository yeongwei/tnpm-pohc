package com.psl.pohc.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import com.psl.pohc.resource.ConfigurationMap;

public class PohcSample {
  
  private final String DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
  private final Logger LOGGER = Logger.getLogger(PohcSample.class.getName());
  private final Random RANDOM = new Random();
  
  private ConfigurationMap CONFIGURATION_MAP;
  private DatabaseInstance TNPM_STAGING;
  
  public static void main(String[] args) throws Exception {
    PohcSample pohcSample = new PohcSample();
    pohcSample.run();
  }
  
  public PohcSample() throws Exception {
    CONFIGURATION_MAP = new ConfigurationMap();
    TNPM_STAGING = new DatabaseInstance(DRIVER_CLASS,
        CONFIGURATION_MAP.get("tnpm.host"), CONFIGURATION_MAP.get("tnpm.db.port"), CONFIGURATION_MAP.get("tnpm.db.schema"),
        CONFIGURATION_MAP.get("tnpm.db.user"), CONFIGURATION_MAP.get("tnpm.db.password"));
  }
  
  public void run() throws Exception {
    
    LOGGER.info("STARTED");
    
    int cellCount;
    int eutranCellCount;
    
    Connection TNPM_STAGING_CONN = TNPM_STAGING.connection;
    ResultSet resultSet = TNPM_STAGING_CONN.createStatement()
      .executeQuery("SELECT COUNT(CELL_ID) as CELL_ID_COUNT FROM NC_CELL");
    resultSet.next();
    cellCount = resultSet.getInt("CELL_ID_COUNT");
    LOGGER.info(String.format("There are %d CELLs.", cellCount));
    
    resultSet = TNPM_STAGING_CONN.createStatement()
        .executeQuery("SELECT COUNT(EUTRANCELL_ID) as EUTRANCELL_ID_COUNT FROM NC_EUTRANCELL");
    resultSet.next();
    eutranCellCount = resultSet.getInt("EUTRANCELL_ID_COUNT");
    LOGGER.info(String.format("There are %d EUTRAN CELLs.", eutranCellCount));
    
    double tenPercent = 0.1;
    int totalNumberOfCell = (int) (cellCount * tenPercent);
    int totalNumberOfEutranCell = (int) (eutranCellCount * tenPercent);
    LOGGER.info(String.format("About to pick up %d CELLs.", totalNumberOfCell));
    LOGGER.info(String.format("About to pick up %d EUTRAN CELLs.", totalNumberOfEutranCell));
    
    ArrayList<Integer> cellRnum = new ArrayList<Integer>();
    ArrayList<Integer> eutranCellRnum = new ArrayList<Integer>();
    
    int oracleMaxNumberOfExpressionInList = 1000;
    generateRandomNumber(1, totalNumberOfCell, oracleMaxNumberOfExpressionInList, cellRnum);
    generateRandomNumber(1, totalNumberOfEutranCell, oracleMaxNumberOfExpressionInList, eutranCellRnum);
    
    LOGGER.info(String.format("Will pick up %d CELLs.", cellRnum.size()));
    LOGGER.info(String.format("Will pick up %d EUTRAN CELLs.", eutranCellRnum.size()));
    
    StringBuffer INSERT_POHC = new StringBuffer();
    INSERT_POHC.append("INSERT INTO TEST_POHC_COGNOS "
        + "(NODE_NAMES, OUTAGE_START, OUTAGE_END) "
        + "values "
        + "(?, '05-OCT-15', '15-OCT-15')");
    PreparedStatement INSERT_POHC_PS = TNPM_STAGING.connection.prepareStatement(INSERT_POHC.toString());
    
    StringBuffer CELL_SQL = new StringBuffer();
    CELL_SQL
      .append("SELECT RNUM, CELL_ID FROM (SELECT ROWNUM RNUM, CELL_ID FROM NC_CELL) tbl ")
      .append("WHERE RNUM in ").append(makePredicate(cellRnum, "CELL")); 
    
    resultSet = TNPM_STAGING_CONN.createStatement().executeQuery(CELL_SQL.toString());
    while (resultSet.next()) {
      String cellId = resultSet.getString("CELL_ID");
      //LOGGER.info(String.format("Found CELL_ID of %s.", cellId));
      INSERT_POHC_PS.setString(1, cellId);
      INSERT_POHC_PS.addBatch();
    }
    
    StringBuffer EUTRAN_CELL_SQL = new StringBuffer();
    EUTRAN_CELL_SQL
      .append("SELECT RNUM, EUTRANCELL_ID FROM (SELECT ROWNUM RNUM, EUTRANCELL_ID FROM NC_EUTRANCELL) tbl ")
      .append("WHERE RNUM in ").append(makePredicate(eutranCellRnum, "EUTRANCELL")); 
    
    resultSet = TNPM_STAGING_CONN.createStatement().executeQuery(EUTRAN_CELL_SQL.toString());
    while (resultSet.next()) {
      String eutranCellId = resultSet.getString("EUTRANCELL_ID");
      //LOGGER.info(String.format("Found EUTRANCELL_ID of %s.", eutranCellId));
      INSERT_POHC_PS.setString(1, eutranCellId);
      INSERT_POHC_PS.addBatch();
    }
    
    int[] result = INSERT_POHC_PS.executeBatch();
    int totalResult = 0;
    for (int r : result) {
      totalResult = + r;
    }
    
    TNPM_STAGING.commit();
    LOGGER.info(String.format("There are %d results with total of %d.", result.length, totalResult));
    
    LOGGER.info("ENDED");
  }
  
  private String makePredicate(ArrayList<Integer> x, String forWhat) {
    StringBuffer predicate = new StringBuffer("(").append(x.get(0));
    for (int i = 1; i < x.size(); i++) {
      if (i == (x.size() - 1)) {
        predicate.append(",").append(x.get(i)).append(")");
      } else {
        predicate.append(",").append(x.get(i));
      }
    }
    //LOGGER.info(String.format("CELLs predicate %s.", predicate.toString()));\
    return predicate.toString();
  }
  
  private ArrayList<Integer> generateRandomNumber(int lowerBound, int higherBound, int totalNumberNeeded, ArrayList<Integer> x ) {
    /*
    if (totalNumberNeeded == currentNumber) {
      return x;
    } else {
      int newRandomNumber = generateRandomNumber(lowerBound, higherBound);
      if (x.contains(newRandomNumber)) {
        return generateRandomNumber(lowerBound, higherBound, totalNumberNeeded, currentNumber,  x);
      } else {
        return generateRandomNumber(lowerBound, higherBound, totalNumberNeeded, currentNumber + 1,  x);
      }
    } 
    */
    boolean finished = false;
    int counter = 0;
    while (!finished) {
      int newNumber = RANDOM.nextInt(higherBound - lowerBound) + lowerBound;
      if (x.contains(newNumber)) {
        //continue;
        counter += 1;
      } else {
        x.add(newNumber);
        counter += 1;
      }
      
      if (counter == totalNumberNeeded) {
        finished = true;
      }
    }
    
    return x;
  }
  
  private int generateRandomNumber(int lowerBound, int higherBound) {
    return RANDOM.nextInt(higherBound - lowerBound) + lowerBound;
  }
}
