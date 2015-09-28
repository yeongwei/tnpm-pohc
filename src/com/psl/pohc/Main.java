package com.psl.pohc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.psl.pohc.database.Pohc;
import com.psl.pohc.database.PohcArchive;
import com.psl.pohc.database.PohcView;
import com.psl.pohc.database.Tnpm;
import com.psl.pohc.model.PohcDefinition;
import com.psl.pohc.resource.ConfigurationMap;
import com.psl.pohc.resource.EntityMap;

public class Main {
  private final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) throws Exception {
    Main main = new Main();
    main.run();
  }

  private void run() throws Exception {
    ConfigurationMap CONFIGURATION_MAP = new ConfigurationMap();
    EntityMap ENTITY_MAP = new EntityMap();

    Pohc POHC = new Pohc(CONFIGURATION_MAP.get("pohc.host"),
        CONFIGURATION_MAP.get("pohc.db.port"),
        CONFIGURATION_MAP.get("pohc.db.schema"),
        CONFIGURATION_MAP.get("pohc.db.user"),
        CONFIGURATION_MAP.get("pohc.db.password"));

    PohcArchive POHC_ARCHIVE = new PohcArchive(
        CONFIGURATION_MAP.get("tnpm.host"),
        CONFIGURATION_MAP.get("tnpm.db.port"),
        CONFIGURATION_MAP.get("tnpm.db.schema"),
        CONFIGURATION_MAP.get("tnpm.db.user"),
        CONFIGURATION_MAP.get("tnpm.db.password"));

    PohcView POHC_VIEW = new PohcView(CONFIGURATION_MAP.get("tnpm.host"),
        CONFIGURATION_MAP.get("tnpm.db.port"),
        CONFIGURATION_MAP.get("tnpm.db.schema"),
        CONFIGURATION_MAP.get("tnpm.db.user"),
        CONFIGURATION_MAP.get("tnpm.db.password"));

    int BATCH_SIZE = Integer.parseInt(CONFIGURATION_MAP
        .get("config.batch.size"));
    int TOTAL_ITERATIONS = (POHC.getTotaNumberOfRows() / BATCH_SIZE) + 1;
    LOGGER.info(String.format("About to process %d iteration(s).",
        TOTAL_ITERATIONS));

    for (int ITERATION = 0; ITERATION < TOTAL_ITERATIONS; ITERATION++) {
      ArrayList<PohcDefinition> POHC_DEFINITIONS = POHC.getPohcRecords(
          BATCH_SIZE, BATCH_SIZE * ITERATION);

      for (PohcDefinition DF : POHC_DEFINITIONS) {
        if (POHC_ARCHIVE.checkIfExist(DF.ID, DF.OUTAGE_START, DF.OUTAGE_END)) {
          DF.setType(PohcDefinition.Type.UPDATE);
        } else {
          DF.setType(PohcDefinition.Type.INSERT);
        }
      }

      long RECORDED_TIMESTAMP = getCurrentTimestamp();
      LOGGER.info(String.format("Using %d as recorded timestamp.",
          RECORDED_TIMESTAMP));

      LOGGER.info("About to insert into POHC_ARCHIVE.");
      for (PohcDefinition DF : POHC_DEFINITIONS) {
        POHC_ARCHIVE.insert(RECORDED_TIMESTAMP, DF);
      }

      ArrayList<PohcDefinition> EXPLODED_POHC_DEFINITIONS = new ArrayList<PohcDefinition>();
      LOGGER.info("About to explode records.");
      for (PohcDefinition DF : POHC_DEFINITIONS) {
        EXPLODED_POHC_DEFINITIONS.addAll(POHC_VIEW.parse(DF));
      }

      LOGGER.info(String.format("About to further process %d objects.",
          EXPLODED_POHC_DEFINITIONS.size()));

      LOGGER.info("About to lookup inventory.");
      for (PohcDefinition DF : EXPLODED_POHC_DEFINITIONS) {
        Tnpm.inventoryLookup(DF);
      }

      LOGGER.info("About to insert into POHC_VIEW.");
      for (PohcDefinition DF : EXPLODED_POHC_DEFINITIONS) {
        POHC_VIEW.execute(DF);
      }

      LOGGER.info("About to commit all transactions.");
      LOGGER.info(String.format("config.commit=%s",
          CONFIGURATION_MAP.get("config.commit")));
      if (CONFIGURATION_MAP.get("config.commit").equals("true")) {
        POHC_ARCHIVE.commit();
        POHC_VIEW.commit();
        LOGGER.info("Transactions are commited.");
      } else {
        LOGGER.info("Transactions are NOT commited.");
      }
    }
  }

  /** Returns current timestamp in milliseconds */
  private long getCurrentTimestamp() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    return calendar.getTimeInMillis();
  }
}