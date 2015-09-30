# TNPM POHC

##

### Introduction

The expectation is to have POHC data to be synchronized into TNPM Wireless and Wireline databases.

### User Guide

#### Build

1. Clone the repository via the `git clone` command
2. Promote lib directory to build path
3. Configure Apache Ant within Eclipse environment
4. Locate `build.xml` and right click then run as Ant Build
5. If all is good, locate the dist/ directory, the following artifacts are expected,
    * lib/
        * ojdbc6.jar
        * pohc-<DSTAMP>.jar   
    * configuration/
        * logging.properties
        * configuration.properties
        * EntityMap.csv
    * runPohc.sh

#### Deployment

1. Transfer the files from #5 above into server. E.g. /var/tmp/pohc
2. Give execution permission to runPohc.sh
3. Use the following command, `export JAVA_HOME=<JAVA_HOME_PATH>; ./runPohc.sh -configurationFile `pwd`"<CONFIG_FILE_PATH>" -instanceName <USER_DEFINED_NAME>`. E.g. `./runPohc.sh -configurationFile `pwd`"/configuration/configuration.properties.wireless.dev" -instanceName wireless.dev`
4. Go into log/ to locate file name that has the pattern, pohc-console-<INSTANCE_NAME>-<yyyy.mm.dd-HH.MM.ss>.log
5. Inspect the logging.properties file for FileHandler logging

### Requirement / Use Case / Specification

#### External

1. Handle up to 10,000 POHC entries
2. Outage is expected to be planned at least 24 hours before. E.g. If a Site is planned for maintenance to happen at 2015-09-20 03:00:00 then the outage request must be sent latest by / at 2015-09-19 03:00:00
3. Explode field values with delimiter into individual rows. Delimiter could be comma or line-break.
4. `SiteName` supercede `SiteId`

#### Internal

1. Trace-ability - Delta as new row with `recordedDate`
2. Audit trail - Class `java.util.logging` FINEST level with `FileHandler`
3. On demand invocation - Cron-job

#### Persona

##### Phase

1. Requestor
2. Approver
3. Final Approver
4. HO Approver (Optional)
5. Completion

#### Activity Status

1. Work-in-progress
2. Work completed
3. Revised (start all over)the

### Pseudo Implementation

* On every invocation instance
  * `KNOWLEDGE` initialization
      * Configuration properties (E.g configuration.properties)
          * pohc.host
          * pohc.db.port
          * pohc.db.user
          * pohc.db.password
          * pohc.db.schema
          * tnpm.host
          * tnpm.db.port
          * tnpm.db.user
          * tnpm.db.password
          * tnpm.db.schema
          * tnpm.instance=wireless or tnpm.instance=wireline
          * config.delimiter=\\r?\\n 
  * Determine number of iteration (E.g. totalNumberOfRows / limit + 1) from `POHC`
  * For each iteration
      * **BLOCK 1**
      * Select all from `POHC` limit by x offset by y
      * Convert all into `PohcDefinition`
      * Check each `PohcDefinition` with `POHC_Archive`
      * If **FOUND** mark as `UPDATE`
      * Else **NOT FOUND** mark as `INSERT`
      * For each `PohcDefinition` executeUpdate into `POHC_Archive`
          * If `UPDATE` then `INSERT INTO POHC_ARCHIVE ('U', *)`
          * If `INSERT` then `INSERT INTO POHC_ARCHIVE ('I', *)`
      * Compute `POHC_Archive_Summary` (E.g. TotalObjectAsInsert: x TotalObjectAsUpdate: y TotalInventoryFailedLookup: z)
      * **BLOCK 2**
      * Explode delimited records as individual row (E.g. NODE_NAMES)
      * Validate each exploded records with TNPM Inventory data **(Place holder implemented)**
      * If **NO MATCH** mark as `INVALID`
      * Via `execute` to determine `insert` or `update` invocation 
      * Compute `POHC_View_Summary` (E.g. TotalObjectInserted: x TotalObjectUpdated: y TotalObjectInvalidated: z)
      * **BLOCK 3**
  	  * Consolidate `POHC_Archive_Summary` and `POHC_View_Summary` results then advice user 

### Resource

#### POHC Exposed Table DDL

**Consumable table by `Cognos Reporting Facility`**

1. Delimited record will be exploded as individual row (E.g NODE_NAME)
2. Combination of ID, NODE_NAME, OUTAGE_START and OUTAGE_END as candidate key

```
CREATE TABLE POHC_VIEW (
	ID VARCHAR2(30),
	SUBSYSTEM VARCHAR2(200),
	REGION VARCHAR2(60),
	SYSTEM VARCHAR2(60),
	GROUPNAME VARCHAR2(60),
	PHASE VARCHAR2(60),
	NODE_NAMES VARCHAR2(255),
	STATUS VARCHAR2(60),
	DOMAIN VARCHAR2(60),
	OUTAGE_START DATE,
	OUTAGE_END DATE,
	CONSTRAINT POHC_VIEW_UNIQUE UNIQUE (ID, NODE_NAMES, OUTAGE_START, OUTAGE_END));
```

### POHC Archive Table DDL

**Persist records from POHC server**

```
CREATE TABLE POHC_ARCHIVE (
	RECORDED_TIMESTAMp NUMBER(13,0),
	TYPE VARCHAR2(10),
	ID VARCHAR2(30),
	SUBSYSTEM VARCHAR2(200),
	REGION VARCHAR2(60),
	SYSTEM VARCHAR2(60),
	GROUPNAME VARCHAR2(60),	
	PHASE VARCHAR2(60),
	NODE_NAMES VARCHAR2(255),
	STATUS VARCHAR2(60),
	DOMAIN VARCHAR2(60),
	OUTAGE_START DATE,
	OUTAGE_END DATE);
```

### Number Java Object Creation to Memory Usage

1. 10000
  * **START** `free memory: 15,325 allocated memory: 15,872 max memory: 253,440 total free memory: 252,893`
  * **END** `free memory: 13,873 allocated memory: 15,872 max memory: 253,440 total free memory: 251,441`
2. 100000
  * **START** `free memory: 15,325 allocated memory: 15,872 max memory: 253,440 total free memory: 252,893`
  * **END** `free memory: 6,002 allocated memory: 15,872 max memory: 253,440 total free memory: 243,570`
3. 1000000
  * **START** `free memory: 15,325 allocated memory: 15,872 max memory: 253,440 total free memory: 252,893`
  * **END** `free memory: 29,367 allocated memory: 111,624 max memory: 253,440 total free memory: 171,183`
4. 10000000
  * **START** `free memory: 15,325 allocated memory: 15,872 max memory: 1,013,632 total free memory: 1,013,085`
  * **END** `free memory: 97,191 allocated memory: 1,013,632 max memory: 1,013,632 total free memory: 97,191`

### Entity Map (Wireless)

#### Columns

`DOMAIN_NAME,NETWORK_OBJECT,NC_TABLE_NAME,KEY_COLUMN_NAME,DOMAIN_ID`

### Future Work

1. Make solution generic to various database interfaces. Currently there are a few parts of the program that expects Oracle behavior this should be made more generic.