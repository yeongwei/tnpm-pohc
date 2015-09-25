package com.psl.pohc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestPohc {
  public static void main (String[] args) throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    
    Properties properties = new Properties();
    properties.put("user", "hkamali");
    properties.put("password", "hkamali");
    
    Connection connection = 
        DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1522:smdbdev",
            properties);
    
    String SQL = "SELECT * FROM SMDBUSER3.UFM_OUTAGE WHERE SUBSYSTEM = 'NODE B-UMTS-MSPHO-DXB'";
    
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(SQL);
    
    while (resultSet.next()) {
      String nodeNames = resultSet.getObject("NODE_NAMES").toString();
      String[] nodeNameList = splitByNewLine(nodeNames);
      for (String x : nodeNameList) {
        System.out.print(x + "|_|");
      }
      System.out.println();
      
      // OUTAGE_START and OUTAGE_END is Oracle Type Date and JDBC java.sql.Timestamp
      /*
      Object obj = resultSet.getObject("OUTAGE_START");
      System.out.println(obj.getClass()); // class java.sql.Timestamp
      */
    }
  }
  
  // Delimiter should be a String that expects Regexp value
  private static String[] splitByNewLine (String value) {
    return value.split("\\r?\\n");
  }
}
