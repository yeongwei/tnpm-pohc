#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Please provide value for -configurationFile, -instanceName";
    exit -1;
fi

CONFIG_FILE="";
case "$1" in
    -configurationFile)
        if [ -f "$2" ]; then
            CONFIG_FILE=$2;
        else
            echo $2 "not found.";
            exit -1;
        fi
        ;;
    *)
        echo "-configurationFile not provided";
        exit -1;
        ;;
esac;

INSTANCE_NAME=`date +%s`;
case "$3" in
    -instanceName)
        if [ -z "$4" ]; then
            echo "-instanceName value not provided";
            exit -1;
        else
             INSTANCE_NAME=$4;
        fi
        ;;
    *)
        echo "-instanceName not provided";
        exit -1;
        ;;
esac;

if [ -z $JAVA_HOME ]; then
    echo "JAVA_HOME is not set!";
    exit -1;
fi 

CURR_DIR=`pwd`
LOG_DIR=$CURR_DIR"/log/"
LIB_DIR=$CURR_DIR"/lib/"
CONFIG_DIR=$CURR_DIR"/configuration/"

CONSOLE_LOG=$LOG_DIR"pohc-console-"$INSTANCE_NAME"-"`date +%Y.%m.%d-%H.%M.%S`".log"

CONFIG_FILE_PROP="-Dconfiguration.file="
CONFIG_FILE_PATH=$CONFIG_FILE

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

CMD="$JAVA_HOME/bin/java 
-cp $JAVA_CP 
$JAVA_ARG 
$JAVA_MAIN"

echo "About to execute: " $CMD

`$CMD &> $CONSOLE_LOG` 


