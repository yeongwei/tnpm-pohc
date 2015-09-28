package com.psl.pohc.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.psl.pohc.model.Entity;

public class EntityMap {

  private final static String ENTITY_MAP_PROPERTY = "configuration.entity.map";

  private static HashMap<String, ArrayList<Entity>> ENTITY_MAP = new HashMap<String, ArrayList<Entity>>();

  private final Logger LOGGER = Logger.getLogger(EntityMap.class.getName());

  public EntityMap() throws Exception {
    this(System.getProperty(ENTITY_MAP_PROPERTY));
  }

  public EntityMap(String entityMapFileName) throws Exception {
    File entityFile;
    FileInputStream fileInputStream;
    InputStreamReader inputStreamReader;
    BufferedReader inputBufferedReader;

    try {
      entityFile = new File(entityMapFileName);
      fileInputStream = new FileInputStream(entityFile);
      inputStreamReader = new InputStreamReader(fileInputStream);
      inputBufferedReader = new BufferedReader(inputStreamReader);

      String line;
      ArrayList<String> content = new ArrayList<String>();
      while ((line = inputBufferedReader.readLine()) != null) {
        content.add(line);
      }
      inputBufferedReader.close();
      inputStreamReader.close();
      fileInputStream.close();

      for (int i = 1; i < content.size(); i++) {
        String[] entityContent = content.get(i).split(",");
        if (!ENTITY_MAP.containsKey(entityContent[EntityMap.Column.DOMAIN_NAME
            .getIndex()])) {
          ENTITY_MAP.put(
              entityContent[EntityMap.Column.DOMAIN_NAME.getIndex()],
              new ArrayList<Entity>());
        }

        ArrayList<Entity> x = ENTITY_MAP
            .get(entityContent[EntityMap.Column.DOMAIN_NAME.getIndex()]);
        x.add(new Entity(
            entityContent[EntityMap.Column.DOMAIN_NAME.getIndex()],
            entityContent[EntityMap.Column.NETWORK_OBJECT.getIndex()],
            entityContent[EntityMap.Column.NC_TABLE_NAME.getIndex()],
            entityContent[EntityMap.Column.KEY_COLUMN_NAME.getIndex()],
            entityContent[EntityMap.Column.DOMAIN_ID.getIndex()]));
        // Domain Name as keywill always be uppercase
        ENTITY_MAP.put(entityContent[EntityMap.Column.DOMAIN_NAME.getIndex()]
            .toUpperCase(), x);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new Exception(ex.getMessage());
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Exception(ex.getMessage());
    } finally {
      // todo
    }
  }

  public boolean hasEntity(String domain) {
    String domainUpperCase = domain.toUpperCase();
    return ENTITY_MAP.containsKey(domainUpperCase);
  }

  public ArrayList<Entity> getEntitiesFor(String domain) {
    String domainUpperCase = domain.toUpperCase();
    if (ENTITY_MAP.containsKey(domainUpperCase)) {
      return ENTITY_MAP.get(domainUpperCase);
    } else {
      return new ArrayList<Entity>();
    }
  }
  
  public ArrayList<Entity> flatten() {
    ArrayList<Entity> x = new ArrayList<Entity>();
    for (Object domain : ENTITY_MAP.keySet()) {
      x.addAll(ENTITY_MAP.get(domain));
    }
    return x;
  }

  public enum Column {
    DOMAIN_NAME, NETWORK_OBJECT, NC_TABLE_NAME, KEY_COLUMN_NAME, DOMAIN_ID;

    public int getIndex() {
      switch (this) {
      case DOMAIN_NAME:
        return 0;
      case NETWORK_OBJECT:
        return 1;
      case NC_TABLE_NAME:
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