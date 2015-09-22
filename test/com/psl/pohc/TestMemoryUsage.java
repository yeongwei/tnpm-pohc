package com.psl.pohc;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.psl.pohc.model.PohcDefinition;

public class TestMemoryUsage {
  public static void main(String[] args) {
    int numberOfObjects = 1000000;
    ArrayList<PohcDefinition> x = new ArrayList<PohcDefinition>();
    
    Date currentDate = new Date();
    String random = UUID.randomUUID().toString();
    
    printMemoryUsage();
    
    for (int i = 0; i < numberOfObjects; i++) {
      x.add(
          new PohcDefinition(
              currentDate,
              random, random, random, random, random, random, random, random, random, random, random));
    }
    
    printMemoryUsage();
    /*
    for (PohcDefinition z : x) {
      System.out.println(z.asNameValuePair());
    }
    */
  }
  
  public static void printMemoryUsage() {
    int k = 1024;
    Runtime runtime = Runtime.getRuntime();
    NumberFormat format = NumberFormat.getInstance();

    StringBuilder sb = new StringBuilder();
    long maxMemory = runtime.maxMemory();
    long allocatedMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();

    sb.append("free memory: " + format.format(freeMemory / k) + " ");
    sb.append("allocated memory: " + format.format(allocatedMemory / k) + " ");
    sb.append("max memory: " + format.format(maxMemory / k) + " ");
    sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / k));
    
    System.out.println(sb);
  }
}
