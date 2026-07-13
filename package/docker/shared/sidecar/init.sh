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

sync_missing_default_conf() {
  local conf_dir=/root/cgdm/sidecar/conf
  local default_conf_dir=/root/default_conf

  mkdir -p "$conf_dir"
  echo "sync missing default conf from ${default_conf_dir}."
  while IFS= read -r -d '' src; do
    local rel="${src#$default_conf_dir/}"
    local dst="$conf_dir/$rel"
    # The version is image metadata and must follow the running image on upgrade.
    if [ "$rel" = "version" ] && [ -f "$src" ] && [ ! -L "$src" ]; then
      cp -a --remove-destination "$src" "$dst"
    elif [ -d "$src" ] && [ ! -L "$src" ]; then
      mkdir -p "$dst"
    elif [ ! -e "$dst" ] && [ ! -L "$dst" ]; then
      mkdir -p "$(dirname "$dst")"
      cp -a "$src" "$dst"
    fi
  done < <(find "$default_conf_dir" -mindepth 1 -print0)
}

sync_missing_default_conf

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
