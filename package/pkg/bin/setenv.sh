#!/bin/bash
# -----------------------------------------------------------------------------
#Console out
LOG_PATH="$APP_HOME/logs"
DATA_PATH="$APP_HOME/data"
mkdir -p $LOG_PATH

#JPDA
#JPDA_ENABLE="jpda"
JPDA_TRANSPORT="dt_socket"
JPDA_ADDRESS="8000"
JPDA_SUSPEND="n"

# JVM opts
JAVA_OPTS="${JAVA_OPTS} -server"
#JAVA_OPTS="${JAVA_OPTS} -Xloggc:${LOG_PATH}/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG_PATH}/java.hprof"
JAVA_OPTS="${JAVA_OPTS} -Dapp.logPath=${LOG_PATH} -DJM.LOG.PATH=${LOG_PATH} -Dapp.data=${DATA_PATH}"
JAVA_OPTS="${JAVA_OPTS} -Xms1024m -Xmx2048m"
#JAVA_OPTS="${JAVA_OPTS} -XX:+UseParallelGC"
JAVA_OPTS="${JAVA_OPTS} -XX:-UseAdaptiveSizePolicy -XX:SurvivorRatio=2 -XX:NewRatio=1 -XX:ParallelGCThreads=6"
JAVA_OPTS="${JAVA_OPTS} -XX:-OmitStackTraceInFastThrow"
JAVA_OPTS="${JAVA_OPTS} -XX:+DisableExplicitGC"
JAVA_OPTS="${JAVA_OPTS} -Dsun.net.client.defaultConnectTimeout=10000 -Dsun.net.client.defaultReadTimeout=30000"
JAVA_OPTS="${JAVA_OPTS} -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"

# SETENV NEW OPTS
JAVA_OPTS="${JAVA_OPTS} -Dnetwork.aliyun.vpc=false"
JAVA_OPTS="${JAVA_OPTS} -Dnetwork.aliyun.internal=false"
JAVA_OPTS="${JAVA_OPTS} -DdriverDiscovery=false"
JAVA_OPTS="${JAVA_OPTS} -Djava.security.properties=${APP_HOME}/conf/java-security.properties"