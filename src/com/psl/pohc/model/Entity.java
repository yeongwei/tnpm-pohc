package com.psl.pohc.model;

import com.psl.pohc.resource.EntityMap;

public class Entity {
  public final String DOMAIN_NAME;
  public final String NETWORK_OBJECT;
  public final String NC_TABLE_NAME;
  public final String KEY_COLUMN_NAME;
  public final String DOMAIN_ID;

  public Entity(String domainName, String networkObject, String ncTableName,
      String keyColumnName, String domainId) {
    DOMAIN_NAME = domainName;
    NETWORK_OBJECT = networkObject;
    NC_TABLE_NAME = ncTableName;
    KEY_COLUMN_NAME = keyColumnName;
    DOMAIN_ID = domainId;
  }

  public String asNameValuePair() {
    StringBuffer sb = new StringBuffer();
    sb.append(EntityMap.Column.DOMAIN_NAME.name()).append("=")
        .append(DOMAIN_NAME).append(" ");
    sb.append(EntityMap.Column.NETWORK_OBJECT.name()).append("=")
        .append(NETWORK_OBJECT).append(" ");
    sb.append(EntityMap.Column.NC_TABLE_NAME.name()).append("=")
        .append(NC_TABLE_NAME).append(" ");
    sb.append(EntityMap.Column.KEY_COLUMN_NAME.name()).append("=")
        .append(KEY_COLUMN_NAME).append(" ");
    sb.append(EntityMap.Column.DOMAIN_ID.name()).append("=").append(DOMAIN_ID);
    return sb.toString();
  }
}
