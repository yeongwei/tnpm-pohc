# TNPM POHC

##

### Introduction

The expectation is to have POHC data to be synchronized into TNPM Wireless and Wireline databases.

### Requirement / Use Case / Specification

#### External

1. Handle up to 10,000 POHC entries
2. Outage is expected to be planned at least 24 hours before. E.g. If a Site is planned for maintenance to happen at 2015-09-20 03:00:00 then the outage request must be sent latest by / at 2015-09-19 03:00:00
3. Explode field values with delimiter into individual rows
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
3. Revised (start all over)

### Pseudo Implementation

* On every invocation instance
  * Select all from POHC
  * Convert into `PohcDefinition`(s)
  * Iterate each `PohcDefinition` against `POHC_Archive`
      * If `PohcDefinition ` **FOUND** then mark as `UPDATE`
      * Else mark as `INSERT`
      * Pivot delimited records as individual row
      * Serialize as SQL and push into `SQL_STORE`
      * Save `SQL_STORE` as file
  * Slam `SQL_STORE` onto `POHC_Archive`
  * Compute `POHC_Archive_Summary`
  * Iterate each `PohcDefinition` against `POHC_View`
      * If `PohcDefinition` is an `INSERT` then insert into `POHC_View`
      * Else if `PohcDefinition` is an `UPDATE` then update **accordingly** into `POHC_View`
      * Pivot delimited records as individual row
      * Serialize as SQL and push into `SQL_STORE`
      * Save `SQL_STORE` as file
  * Compute `POHC_View_Summary`
  * Compare `POHC_Archive_Summary` and `POHC_View_Summary` then advice user 