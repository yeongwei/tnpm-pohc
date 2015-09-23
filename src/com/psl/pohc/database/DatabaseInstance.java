package com.psl.pohc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseInstance {
  protected Connection connection;
  private static boolean autoCommit = false;
  
  public DatabaseInstance (
      String driverClass, 
      String host, String port, String sid, 
      String user, String password) throws Exception {
    try {
      Class.forName(driverClass);
      connection = DriverManager.getConnection(
          Jdbc.getUrl(driverClass, host, port, sid),
          createCredentials(user, password));
      connection.setAutoCommit(autoCommit);
      assert connection.isClosed() == false;
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
      throw ex;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }
  
  private Properties createCredentials(String user, String password) {
    Properties properties = new Properties();
    properties.put("user", user);
    properties.put("password", password);
    return properties;
  }
  
  public boolean isActive() {
    try {
      return !connection.isClosed();
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  
  public boolean commit() {
    try {
      connection.commit();
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
}

class Jdbc {
  public static String getUrl(String driverClass, String host, String port, String sid) {
    if (driverClass.equals("oracle.jdbc.driver.OracleDriver")) {
      return "jdbc:oracle:thin:@" + host + ":"  + port + ":" + sid;
    } else {
      return "jdbc:noDriverFound:" + host + ":"  + port + ":" + sid;
    }
  }
}
