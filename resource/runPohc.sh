#!/bin/bash

CURR_DIR=`pwd`
TMP_DIR="/tmp/"

CONSOLE_LOG=$TMP_DIR"pohc-console.log"
LIB_DIR=$CURR_DIR"/"
CONFIG_FILE=$CURR_DIR"/configuration.properties"
CONFIG_FILE_PROP="-Dconfiguration.file="
ENTITY_MAP_FILE=$CURR_DIR"/EntityMap.csv"
ENTITY_MAP_PROP="-Dconfiguration.entity.map="

JAVA_LOGGING_ARG="-Djava.util.logging.config="
JAVA_LOGGING_FILE=$CURR_DIR"/logging.properties"

JAVA_FULL_CLS_PATH=""
for j in `ls -1 $LIB_DIR | grep jar`; do
    JAVA_FULL_CLS_PATH+=$LIB_DIR
    JAVA_FULL_CLS_PATH+=$j
done;

JAVA_MAIN="com.psl.pohc.Main"
JAVA_CP=$JAVA_FULL_CLS_PATH

JAVA_ARG="$JAVA_LOGGING_ARG$JAVA_LOGGING_FILE \
$CONFIG_FILE_PROP$CONFIG_FILE \ 
$ENTITY_MAP_PROP$ENTITY_MAP_FILE"

if [ -z $JAVA_HOME ]; then
    echo "JAVA_HOME is not set!";
    exit -1;
fi    

CMD="$JAVA_HOME/bin/java \
-cp $JAVA_CP \
$JAVA_ARG \
$JAVA_MAIN &>$CONSOLE_LOG"

`CMD`


