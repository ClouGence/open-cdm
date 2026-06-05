#!/bin/bash

current_path=$(pwd)
case "$(uname)" in
Linux)
  bin_abs_path=$(readlink -f $(dirname $0))
  ;;
*)
  bin_abs_path=$(
    cd $(dirname $0)
    pwd
  )
  ;;
esac
base=${bin_abs_path}/..
echo $base
export LANG=en_US.UTF-8
export BASE=$base

tryClearCheckerLog() {
  filezise=$(ls -l $base/logs/checker.log | awk '{ print $5}')
  if [ $filezise -gt 8192 ]; then
    date=`date +%Y-%m-%d@%H:%M:%S`
    echo "" > $base/logs/checker.log
    echo $date": doClearCheckerLog." >> $base/logs/checker.log
  fi
}

tryRestart() {
  TEST_PID=$(ps aux | grep -v "grep" | grep "org.codehaus.plexus.classworlds.launcher.Launcher" | grep "sidecar" | awk '{print $2}')
  date=`date +%Y-%m-%d@%H:%M:%S`
  if [ "$TEST_PID" = "" ]; then
    echo $date":Sidecar is not running,try restart sidecar at " $base/bin/startup.sh >> $base/logs/checker.log
    $base/bin/startup.sh
  else
    echo $date":Sidecar is running successfully, wait to next round check ..." >> $base/logs/checker.log
  fi
}

while true; do
  date=`date +%Y-%m-%d@%H:%M:%S`
  echo $date":Begin to check whether sidecar is alive" >> $base/logs/checker.log
  tryClearCheckerLog
  tryRestart
  sleep 5
done
