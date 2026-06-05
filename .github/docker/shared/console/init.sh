#!/bin/bash

# adjust os timezone
echo 'Asia/Shanghai' > /etc/timezone

DB_HOST=${DB_HOST:-}
DB_PORT=${DB_PORT:-3306}
DB_DATABASE=${DB_DATABASE:-cdmgr}
DB_USERNAME=${DB_USERNAME:-}
DB_PASSWORD=${DB_PASSWORD:-}
WAIT_DB_TIMEOUT_SECONDS=${WAIT_DB_TIMEOUT_SECONDS:-120}

sed_replacement_escape() {
  printf '%s' "$1" | sed -e 's/[\/&\\]/\\&/g'
}

can_connect_db() {
  if command -v timeout >/dev/null 2>&1; then
    timeout 1 bash -c "</dev/tcp/$1/$2" >/dev/null 2>&1
  else
    bash -c "</dev/tcp/$1/$2" >/dev/null 2>&1
  fi
}

wait_for_db() {
  local host="$1"
  local port="$2"
  local waited=0

  echo "waiting for mysql at ${host}:${port} ..."
  until can_connect_db "$host" "$port"; do
    waited=$((waited + 1))
    if [ "$waited" -ge "$WAIT_DB_TIMEOUT_SECONDS" ]; then
      echo "mysql is still unavailable after ${WAIT_DB_TIMEOUT_SECONDS}s: ${host}:${port}" >&2
      exit 1
    fi
    sleep 1
  done
  echo "mysql is ready: ${host}:${port}"
}

init_conf_dir_if_empty() {
  local conf_dir=/root/cgdm/console/conf
  local default_conf_dir=/root/default_conf

  mkdir -p "$conf_dir"
  if [ -z "$(find "$conf_dir" -mindepth 1 -maxdepth 1 -print -quit)" ]; then
    echo "conf dir is empty, initializing from ${default_conf_dir}."
    cp -a "$default_conf_dir"/. "$conf_dir"/
  fi
}

init_conf_dir_if_empty

# first-time config generation (Flyway handles DB init on startup)
DST_CONF_FILE=/root/cgdm/console/conf/console.properties
if [ ! -f "$DST_CONF_FILE" ]; then
  echo "first startup: generating console.properties from env."
  mkdir -p /root/cgdm/console/conf
  APP_WEB_PORT_SED=$(sed_replacement_escape "${APP_WEB_PORT:-}")
  APP_WEB_JWT_SED=$(sed_replacement_escape "${APP_WEB_JWT:-}")
  APP_SERVE_NAME_SED=$(sed_replacement_escape "${APP_SERVE_NAME:-}")
  APP_SERVE_PORT_SED=$(sed_replacement_escape "${APP_SERVE_PORT:-}")
  DB_HOST_SED=$(sed_replacement_escape "${DB_HOST:-}")
  DB_PORT_SED=$(sed_replacement_escape "${DB_PORT:-}")
  DB_DATABASE_SED=$(sed_replacement_escape "${DB_DATABASE:-}")
  DB_USERNAME_SED=$(sed_replacement_escape "${DB_USERNAME:-}")
  DB_PASSWORD_SED=$(sed_replacement_escape "${DB_PASSWORD:-}")
  sed -e "s/%APP_WEB_PORT%/$APP_WEB_PORT_SED/g" \
      -e "s/%APP_WEB_JWT%/$APP_WEB_JWT_SED/g" \
      -e "s/%APP_SERVE_NAME%/$APP_SERVE_NAME_SED/g" \
      -e "s/%APP_SERVE_PORT%/$APP_SERVE_PORT_SED/g" \
      -e "s/%DB_HOST%/$DB_HOST_SED/g" \
      -e "s/%DB_PORT%/$DB_PORT_SED/g" \
      -e "s/%DB_DATABASE%/$DB_DATABASE_SED/g" \
      -e "s/%DB_USERNAME%/$DB_USERNAME_SED/g" \
      -e "s/%DB_PASSWORD%/$DB_PASSWORD_SED/g" \
    /docker-entrypoint-init/copy_console.properties > "$DST_CONF_FILE"
fi

if [ -n "$DB_HOST" ]; then
  wait_for_db "$DB_HOST" "$DB_PORT"
else
  echo "DB_HOST is empty, skip mysql wait and continue with initialization bootstrap."
fi

# start console (Flyway auto-migrates DB on first boot)
mkdir -p /root/cgdm/console/logs
touch /root/cgdm/console/logs/console.log

/root/cgdm/console/bin/startup.sh
tail -f /root/cgdm/console/logs/console.log
