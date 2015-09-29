#!/bin/bash

CURR_DIR=`pwd`
LOG_DIR=$CURR_DIR"/log/"
LIB_DIR=$CURR_DIR"/lib/"
CONFIG_DIR=$CURR_DIR"/configuration/"

CONSOLE_LOG=$LOG_DIR"pohc-console.log"

CONFIG_FILE_PROP="-Dconfiguration.file="
CONFIG_FILE_PATH=$CONFIG_DIR"configuration.properties"

ENTITY_MAP_FILE_PROP="-Dconfiguration.entity.map="
ENTITY_MAP_FILE_PATH=$CONFIG_DIR"EntityMap.csv"

JAVA_LOGGING_ARG="-Djava.util.logging.config="
JAVA_LOGGING_FILE=$CONFIG_DIR"logging.properties"

JAVA_FULL_CLS_PATH=""
for j in `ls -1 $LIB_DIR | grep jar`; do
    JAVA_FULL_CLS_PATH+=$LIB_DIR
    JAVA_FULL_CLS_PATH+=$j
    JAVA_FULL_CLS_PATH+=":"
done;

JAVA_MAIN="com.psl.pohc.Main"
JAVA_CP=$JAVA_FULL_CLS_PATH

JAVA_ARG="$JAVA_LOGGING_ARG$JAVA_LOGGING_FILE 
$CONFIG_FILE_PROP$CONFIG_FILE_PATH 
$ENTITY_MAP_FILE_PROP$ENTITY_MAP_FILE_PATH"

if [ -z $JAVA_HOME ]; then
    echo "JAVA_HOME is not set!";
    exit -1;
fi    

CMD="$JAVA_HOME/bin/java 
-cp $JAVA_CP 
$JAVA_ARG 
$JAVA_MAIN"

echo "About to execute: " $CMD

`$CMD &> $CONSOLE_LOG` 


