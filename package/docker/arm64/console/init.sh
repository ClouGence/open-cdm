#!/bin/bash

# adjust os timezone
echo 'Asia/Shanghai' > /etc/timezone

DB_HOST=${DB_HOST:-}
DB_PORT=${DB_PORT:-3306}
DB_DATABASE=${DB_DATABASE:-cdmgr}
DB_USERNAME=${DB_USERNAME:-}
DB_PASSWORD=${DB_PASSWORD:-}
WAIT_DB_TIMEOUT_SECONDS=${WAIT_DB_TIMEOUT_SECONDS:-120}

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

# first-time config generation (Flyway handles DB init on startup)
DST_CONF_FILE=/root/cgdm/console/conf/console.properties
if [ ! -f "$DST_CONF_FILE" ]; then
  echo "first startup: generating console.properties from env."
  mkdir -p /root/cgdm/console/conf
  sed -e "s/%APP_WEB_PORT%/$APP_WEB_PORT/g" \
      -e "s/%APP_WEB_JWT%/$APP_WEB_JWT/g" \
      -e "s/%APP_SERVE_NAME%/$APP_SERVE_NAME/g" \
      -e "s/%APP_SERVE_PORT%/$APP_SERVE_PORT/g" \
      -e "s/%DB_HOST%/$DB_HOST/g" \
      -e "s/%DB_PORT%/$DB_PORT/g" \
      -e "s/%DB_DATABASE%/$DB_DATABASE/g" \
      -e "s/%DB_USERNAME%/$DB_USERNAME/g" \
      -e "s/%DB_PASSWORD%/$DB_PASSWORD/g" \
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
