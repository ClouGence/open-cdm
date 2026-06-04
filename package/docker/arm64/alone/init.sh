#!/bin/bash

set -euo pipefail

# adjust os timezone
echo 'Asia/Shanghai' > /etc/timezone

MYSQL_EMBEDDED=${MYSQL_EMBEDDED:-true}
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-123456}
MYSQL_DATADIR=${MYSQL_DATADIR:-/var/lib/mysql}
MYSQL_SOCKET=${MYSQL_SOCKET:-/run/mysqld/mysqld.sock}
MYSQL_PID_FILE=${MYSQL_PID_FILE:-/run/mysqld/mysqld.pid}
MYSQL_INIT_MARKER=${MYSQL_INIT_MARKER:-$MYSQL_DATADIR/.clouddm_mysql_initialized}
DB_HOST=${DB_HOST:-127.0.0.1}
DB_PORT=${DB_PORT:-3306}
DB_DATABASE=${DB_DATABASE:-cdmgr}
DB_USERNAME=${DB_USERNAME:-root}
DB_PASSWORD=${DB_PASSWORD:-$MYSQL_ROOT_PASSWORD}
WAIT_DB_TIMEOUT_SECONDS=${WAIT_DB_TIMEOUT_SECONDS:-120}
WAIT_MYSQL_STOP_SECONDS=${WAIT_MYSQL_STOP_SECONDS:-30}

sql_escape() {
  printf '%s' "$1" | sed "s/'/''/g"
}

sed_replacement_escape() {
  printf '%s' "$1" | sed -e 's/[\/&\\]/\\&/g'
}

mysql_sql() {
  if mysql --protocol=socket --socket="$MYSQL_SOCKET" -uroot -e "SELECT 1" >/dev/null 2>&1; then
    mysql --protocol=socket --socket="$MYSQL_SOCKET" -uroot "$@"
  else
    mysql --protocol=socket --socket="$MYSQL_SOCKET" -uroot -p"$MYSQL_ROOT_PASSWORD" "$@"
  fi
}

is_embedded_mysqld_pid() {
  local pid="$1"
  local cmdline

  if [ -z "$pid" ] || ! kill -0 "$pid" 2>/dev/null; then
    return 1
  fi

  cmdline=$(tr '\0' ' ' < "/proc/$pid/cmdline" 2>/dev/null || true)
  case "$cmdline" in
    *mysqld*"--datadir=$MYSQL_DATADIR"*|*mysqld*"--socket=$MYSQL_SOCKET"*|*mysqld*"--pid-file=$MYSQL_PID_FILE"*) return 0 ;;
    *) return 1 ;;
  esac
}

embedded_mysqld_pid() {
  local proc
  local pid

  if [ -f "$MYSQL_PID_FILE" ]; then
    pid=$(cat "$MYSQL_PID_FILE" 2>/dev/null || true)
    if is_embedded_mysqld_pid "$pid"; then
      printf '%s\n' "$pid"
      return 0
    fi
  fi

  for proc in /proc/[0-9]*; do
    pid="${proc#/proc/}"
    if is_embedded_mysqld_pid "$pid"; then
      printf '%s\n' "$pid"
      return 0
    fi
  done

  return 1
}

setup_mysql_directories() {
  mkdir -p "$MYSQL_DATADIR" /run/mysqld
  chown -R mysql:mysql "$MYSQL_DATADIR" /run/mysqld
}

cleanup_mysql_runtime_files() {
  local pid

  if pid=$(embedded_mysqld_pid); then
    echo "embedded mysql is still running: ${pid}"
    return
  fi

  rm -f "$MYSQL_SOCKET" "$MYSQL_SOCKET.lock" "$MYSQL_PID_FILE"
}

initialize_mysql_data() {
  if [ -d "$MYSQL_DATADIR/mysql" ]; then
    return
  fi

  echo "initializing embedded mysql data dir: $MYSQL_DATADIR"
  mysqld --initialize-insecure --user=mysql --datadir="$MYSQL_DATADIR"
}

start_embedded_mysql() {
  echo "starting embedded mysql on 127.0.0.1:${DB_PORT}"
  mysqld \
    --user=mysql \
    --datadir="$MYSQL_DATADIR" \
    --socket="$MYSQL_SOCKET" \
    --pid-file="$MYSQL_PID_FILE" \
    --port="$DB_PORT" \
    --bind-address=127.0.0.1 \
    --character-set-server=utf8mb4 \
    --collation-server=utf8mb4_unicode_ci \
    --default-time-zone=+08:00 \
    --daemonize
}

configure_embedded_mysql() {
  local escaped_root_password
  local escaped_db_name
  local escaped_db_user
  local escaped_db_password

  escaped_root_password=$(sql_escape "$MYSQL_ROOT_PASSWORD")
  escaped_db_name=$(sql_escape "$DB_DATABASE")

  mysql_sql <<SQL
ALTER USER 'root'@'localhost' IDENTIFIED BY '${escaped_root_password}';
CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED BY '${escaped_root_password}';
ALTER USER 'root'@'127.0.0.1' IDENTIFIED BY '${escaped_root_password}';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION;
CREATE DATABASE IF NOT EXISTS ${escaped_db_name};
FLUSH PRIVILEGES;
SQL

  if [ "$DB_USERNAME" != "root" ]; then
    escaped_db_user=$(sql_escape "$DB_USERNAME")
    escaped_db_password=$(sql_escape "$DB_PASSWORD")
    mysql_sql <<SQL
CREATE USER IF NOT EXISTS '${escaped_db_user}'@'%' IDENTIFIED BY '${escaped_db_password}';
ALTER USER '${escaped_db_user}'@'%' IDENTIFIED BY '${escaped_db_password}';
GRANT ALL PRIVILEGES ON ${escaped_db_name}.* TO '${escaped_db_user}'@'%';
FLUSH PRIVILEGES;
SQL
  fi
}

stop_embedded_mysql() {
  local pid

  if [ "$MYSQL_EMBEDDED" != "true" ]; then
    return
  fi

  if [ -S "$MYSQL_SOCKET" ]; then
    if mysqladmin --protocol=socket --socket="$MYSQL_SOCKET" -uroot -p"$MYSQL_ROOT_PASSWORD" shutdown >/dev/null 2>&1; then
      wait_for_mysql_exit
      return
    fi
  fi

  if pid=$(embedded_mysqld_pid); then
    echo "stopping embedded mysql process: ${pid}"
    kill -TERM "$pid" >/dev/null 2>&1 || true
    wait_for_mysql_exit
  fi
}

wait_for_mysql_exit() {
  local waited=0
  local pid

  while [ -S "$MYSQL_SOCKET" ] || [ -f "$MYSQL_PID_FILE" ]; do
    pid=$(embedded_mysqld_pid || true)

    if [ -z "$pid" ]; then
      rm -f "$MYSQL_SOCKET" "$MYSQL_SOCKET.lock" "$MYSQL_PID_FILE"
      return
    fi

    waited=$((waited + 1))
    if [ "$waited" -ge "$WAIT_MYSQL_STOP_SECONDS" ]; then
      echo "embedded mysql did not stop after ${WAIT_MYSQL_STOP_SECONDS}s" >&2
      return
    fi
    sleep 1
  done
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

wait_for_embedded_mysql() {
  local waited=0

  echo "waiting for embedded mysql to accept SQL ..."
  until mysql_sql -e "SELECT 1" >/dev/null 2>&1; do
    waited=$((waited + 1))
    if [ "$waited" -ge "$WAIT_DB_TIMEOUT_SECONDS" ]; then
      echo "embedded mysql is still unavailable after ${WAIT_DB_TIMEOUT_SECONDS}s" >&2
      exit 1
    fi
    sleep 1
  done
  echo "embedded mysql is ready."
}

bootstrap_embedded_mysql() {
  setup_mysql_directories
  if [ ! -d "$MYSQL_DATADIR/mysql" ]; then
    initialize_mysql_data
  elif [ ! -f "$MYSQL_INIT_MARKER" ]; then
    echo "existing mysql data dir has no CloudDM init marker, will validate it by starting mysql."
  fi

  cleanup_mysql_runtime_files
  start_embedded_mysql
  wait_for_embedded_mysql
  configure_embedded_mysql
  touch "$MYSQL_INIT_MARKER"
}

init_conf_dir_if_empty() {
  local conf_dir=/root/cgdm/alone/conf
  local default_conf_dir=/root/default_conf

  mkdir -p "$conf_dir"
  if [ -z "$(find "$conf_dir" -mindepth 1 -maxdepth 1 -print -quit)" ]; then
    echo "conf dir is empty, initializing from ${default_conf_dir}."
    cp -a "$default_conf_dir"/. "$conf_dir"/
  fi
}

trap stop_embedded_mysql EXIT
trap 'stop_embedded_mysql; exit 130' INT
trap 'stop_embedded_mysql; exit 143' TERM

init_conf_dir_if_empty

# first-time config generation (Flyway handles DB init on startup)
DST_CONF_FILE=/root/cgdm/alone/conf/alone.properties
if [ ! -f "$DST_CONF_FILE" ]; then
  echo "first startup: generating alone.properties from env."
  mkdir -p /root/cgdm/alone/conf
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
    /docker-entrypoint-init/copy_alone.properties > "$DST_CONF_FILE"
fi

if [ "$MYSQL_EMBEDDED" = "true" ]; then
  DB_HOST=127.0.0.1
  bootstrap_embedded_mysql
elif [ -n "$DB_HOST" ]; then
  wait_for_db "$DB_HOST" "$DB_PORT"
else
  echo "DB_HOST is empty, skip mysql wait and continue with initialization bootstrap."
fi

# start alone (Flyway auto-migrates DB on first boot)
mkdir -p /root/cgdm/alone/logs
touch /root/cgdm/alone/logs/alone.log

/root/cgdm/alone/bin/startup.sh
tail -f /root/cgdm/alone/logs/alone.log
