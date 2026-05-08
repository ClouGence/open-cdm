#!/bin/bash

# adjust os timezone
echo 'Asia/Shanghai' > /etc/timezone

CONSOLE_HOST=${APP_SERVE_NAME:-}
CONSOLE_PORT=${APP_SERVE_PORT:-8008}

can_connect_console() {
  if command -v timeout >/dev/null 2>&1; then
    timeout 1 bash -c "</dev/tcp/$1/$2" >/dev/null 2>&1
  else
    bash -c "</dev/tcp/$1/$2" >/dev/null 2>&1
  fi
}

wait_for_console() {
  local host="$1"
  local port="$2"

  if [ -z "$host" ]; then
    echo "APP_SERVE_NAME is empty, skip console wait and continue." >&2
    return
  fi

  echo "waiting for console rsocket at ${host}:${port} ..."
  until can_connect_console "$host" "$port"; do
    sleep 1
  done
  echo "console rsocket is ready: ${host}:${port}"
}

# global_conf.properties
DST_GLOBAL_FILE=/root/cgdm/sidecar/conf/global_conf.properties
if [ ! -f $DST_GLOBAL_FILE ]; then
  echo "init global_conf.properties from env."
  sed -e "s/%DM_CLIENT_AK%/$DM_CLIENT_AK/g" \
      -e "s/%DM_CLIENT_SK%/$DM_CLIENT_SK/g" \
      -e "s/%DM_CLIENT_WSN%/$DM_CLIENT_WSN/g" \
      -e "s/%APP_SERVE_NAME%/$APP_SERVE_NAME/g" \
      -e "s/%APP_SERVE_PORT%/$APP_SERVE_PORT/g" \
    /docker-entrypoint-init/copy_global_conf.properties > $DST_GLOBAL_FILE
fi

# sidecar.properties
DST_CONF_FILE=/root/cgdm/sidecar/conf/sidecar.properties
if [ ! -f $DST_CONF_FILE ]; then
  echo "init sidecar.properties from env."
  sed -e "s/%APP_WEB_PORT%/$APP_WEB_PORT/g" \
    /docker-entrypoint-init/copy_sidecar.properties > $DST_CONF_FILE
fi

# wait console rsocket before starting sidecar main app
wait_for_console "$CONSOLE_HOST" "$CONSOLE_PORT"

# start sidecar
touch /root/cgdm/sidecar/logs/sidecar.log
/root/cgdm/sidecar/bin/startup.sh

nohup /root/cgdm/sidecar/bin/checker.sh &
tail -f /root/cgdm/sidecar/logs/sidecar.log