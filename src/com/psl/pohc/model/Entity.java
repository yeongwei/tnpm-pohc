package com.psl.pohc.model;

public class Entity {
  public final String DOMAIN_NAME;
  public final String NETWORK_OBJECT;
  public final String TABLE_NAME;
  public final String KEY_COLUMN_NAME;
  public final String DOMAIN_ID;

  public Entity(String domainName, String networkObject, String tableName,
      String keyColumnName, String domainId) {
    DOMAIN_NAME = domainName;
    NETWORK_OBJECT = networkObject;
    TABLE_NAME = tableName;
    KEY_COLUMN_NAME = keyColumnName;
    DOMAIN_ID = domainId;
  }

  public String asNameValuePair() {
    StringBuffer sb = new StringBuffer();
    sb.append(Column.DOMAIN_NAME.name()).append("=")
        .append(DOMAIN_NAME).append(" ");
    sb.append(Column.NETWORK_OBJECT.name()).append("=")
        .append(NETWORK_OBJECT).append(" ");
    sb.append(Column.TABLE_NAME.name()).append("=")
        .append(TABLE_NAME).append(" ");
    sb.append(Column.KEY_COLUMN_NAME.name()).append("=")
        .append(KEY_COLUMN_NAME).append(" ");
    sb.append(Column.DOMAIN_ID.name()).append("=").append(DOMAIN_ID);
    return sb.toString();
  }
  
  public enum Column {
    DOMAIN_NAME, NETWORK_OBJECT, TABLE_NAME, KEY_COLUMN_NAME, DOMAIN_ID;

    public int getIndex() {
      switch (this) {
      case DOMAIN_NAME:
        return 0;
      case NETWORK_OBJECT:
        return 1;
      case TABLE_NAME:
        return 2;
      case KEY_COLUMN_NAME:
        return 3;
      case DOMAIN_ID:
        return 4;
      default:
        return -1;
      }
    }
  }
}
