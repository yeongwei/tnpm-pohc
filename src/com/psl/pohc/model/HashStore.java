package com.psl.pohc.model;

import java.util.ArrayList;
import java.util.HashMap;

public class HashStore extends HashMap<String, ArrayList<String>> {
  
  private static final long serialVersionUID = 1L;

  /** 
   * Check if key has value then return else create empty
   */
  public ArrayList<String> hasKey(String key) {
    if (!this.containsKey(key)) {
      this.put(key, new ArrayList<String>());
    }
    return this.get(key);
  }
  
  /**
   * Check if value exist then return key else null
   */
  public String hasValue(String value) {
    String keyFound = null;
    
    for (Object key : this.keySet()) {
      if (this.get(key).contains(value)) {
        keyFound = key.toString();
        break;
      }
    }
    
    return keyFound;
  }
  
  /**
   *  Adds new value based on key
   */
  public ArrayList<String> addEntry(String key, String value) {
    ArrayList<String> store = hasKey(key);
    store.add(value);
    this.put(key, store);
    
    return store; // is this safe ?
  }
  
  /**
   * Adds new entry based on key and store
   */
  public ArrayList<String> addEntry(String key, ArrayList<String> store) {
    this.put(key, store);
    return store;
  }
  
  /**
   * Remove entry based on key and value
   */
  public ArrayList<String> removeEntry(String key, String value) {
    ArrayList<String> store = hasKey(key);
    
    if (store.size() == 0) {
      return store;
    } else {
      store.remove(value);
      addEntry(key , store);
      return store;
    }
  }
  
  /**
   * Remove entry based on key only 
   */
  public boolean removeEntry(String key) {
    this.remove(key);
    return this.containsKey(key);
  }
  
  /**
   * Remove entry based on value only (is this useful ???)
   *//*
  public ArrayList<String> removeEntry(String value) {
    
  }*/
}
