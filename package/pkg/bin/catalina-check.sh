#!/bin/bash

JAVA_BIN="$1"

if [ -z "$JAVA_BIN" ]; then
  JAVA_BIN="$(command -v java 2>/dev/null)"
fi

if [ -z "$JAVA_BIN" ] || [ ! -x "$JAVA_BIN" ]; then
  echo "Error: cannot find an executable java command for startup check." >&2
  exit 1
fi

JAVA_VERSION_RAW=$("$JAVA_BIN" -version 2>&1 | awk -F '"' '/version/ {print $2; exit}')

if [ -z "$JAVA_VERSION_RAW" ]; then
  echo "Error: unable to detect JDK version from: $JAVA_BIN" >&2
  exit 1
fi

case "$JAVA_VERSION_RAW" in
1.*)
  JAVA_MAJOR=$(printf '%s' "$JAVA_VERSION_RAW" | cut -d '.' -f 2)
  ;;
*)
  JAVA_MAJOR=$(printf '%s' "$JAVA_VERSION_RAW" | cut -d '.' -f 1)
  ;;
esac

if ! printf '%s' "$JAVA_MAJOR" | grep -Eq '^[0-9]+$'; then
  echo "Error: unable to parse JDK major version from: $JAVA_VERSION_RAW" >&2
  exit 1
fi

if [ "$JAVA_MAJOR" -lt 17 ]; then
  echo "Error: CloudDM requires JDK 17 or later. Current version: $JAVA_VERSION_RAW" >&2
  exit 1
fi