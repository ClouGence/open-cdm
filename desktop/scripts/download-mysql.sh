#!/bin/bash
set -euo pipefail

ARCH=$(uname -m)
case "$ARCH" in
  arm64)  MYSQL_ARCH="arm64" ;;
  x86_64) MYSQL_ARCH="x86_64" ;;
  *) echo "Unsupported arch: $ARCH"; exit 1 ;;
esac

MYSQL_VERSION="${MYSQL_VERSION:-8.0.37}"
DOWNLOAD_DIR="${1:?usage: $0 <output-dir>}"
MIRROR="${MYSQL_MIRROR:-https://mirrors.aliyun.com/mysql}"

TARBALL="mysql-${MYSQL_VERSION}-macos14-${MYSQL_ARCH}.tar.gz"
TARBALL_TOP="mysql-${MYSQL_VERSION}-macos14-${MYSQL_ARCH}"
CACHE_DIR="$HOME/.cgdm-desktop/cache"
CACHED_TARBALL="$CACHE_DIR/$TARBALL"

mkdir -p "$CACHE_DIR"

# Download helper: use aria2c for multi-threaded download when available
download_file() {
  local url="$1"
  local output="$2"

  if command -v aria2c &>/dev/null; then
    aria2c \
      -x 16 -s 16 -c \
      --allow-overwrite=true \
      --auto-file-renaming=false \
      --connect-timeout=30 \
      --max-connection-per-server=16 \
      --min-split-size=1M \
      --summary-interval=10 \
      --dir="$(dirname "$output")" \
      --out="$(basename "$output")" \
      "$url"
  else
    echo "Tip: install aria2 (brew install aria2) for faster multi-threaded downloads"
    curl -fSL --connect-timeout 30 --max-time 900 --retry 3 -C - "$url" -o "$output"
  fi
}

# Download if not cached
if [ ! -f "$CACHED_TARBALL" ]; then
  echo "Downloading MySQL ${MYSQL_VERSION} for ${ARCH}..."
  OFFICIAL_URL="https://cdn.mysql.com/Downloads/MySQL-8.0/${TARBALL}"
  MIRROR_URL="${MIRROR}/MySQL-8.0/${TARBALL}"

  if ! download_file "${OFFICIAL_URL}" "${CACHED_TARBALL}.tmp"; then
    echo "Official CDN failed, trying mirror: ${MIRROR_URL}"
    rm -f "${CACHED_TARBALL}.tmp"
    download_file "${MIRROR_URL}" "${CACHED_TARBALL}.tmp"
  fi
  mv "${CACHED_TARBALL}.tmp" "${CACHED_TARBALL}"
else
  echo "MySQL already cached at ${CACHED_TARBALL}"
fi

echo "Extracting MySQL..."
rm -rf "$DOWNLOAD_DIR"
mkdir -p "$DOWNLOAD_DIR"

tar -xzf "$CACHED_TARBALL" -C "$DOWNLOAD_DIR" \
  --strip-components=1 \
  "${TARBALL_TOP}/bin/" \
  "${TARBALL_TOP}/lib/" \
  "${TARBALL_TOP}/share/"

# Remove unnecessary binaries
rm -f "$DOWNLOAD_DIR/bin/mysqlbinlog" \
  "$DOWNLOAD_DIR/bin/mysqlcheck" \
  "$DOWNLOAD_DIR/bin/mysqldump" \
  "$DOWNLOAD_DIR/bin/mysqlimport" \
  "$DOWNLOAD_DIR/bin/mysqlpump" \
  "$DOWNLOAD_DIR/bin/mysqlshow" \
  "$DOWNLOAD_DIR/bin/mysqlslap" \
  "$DOWNLOAD_DIR/bin/mysql_config_editor" \
  "$DOWNLOAD_DIR/bin/mysql_secure_installation" \
  "$DOWNLOAD_DIR/bin/mysql_tzinfo_to_sql" \
  "$DOWNLOAD_DIR/bin/mysqld_multi" \
  "$DOWNLOAD_DIR/bin/mysqld_safe" \
  "$DOWNLOAD_DIR/bin/mysqldumpslow" \
  "$DOWNLOAD_DIR/bin/mysqlrouter" \
  "$DOWNLOAD_DIR/bin/mysqlrouter_keyring" \
  "$DOWNLOAD_DIR/bin/mysqlrouter_passwd" \
  "$DOWNLOAD_DIR/bin/mysqlrouter_plugin_info" \
  "$DOWNLOAD_DIR/bin/comp_err" \
  "$DOWNLOAD_DIR/bin/ibd2sdi" \
  "$DOWNLOAD_DIR/bin/innochecksum" \
  "$DOWNLOAD_DIR/bin/lz4_decompress" \
  "$DOWNLOAD_DIR/bin/my_print_defaults" \
  "$DOWNLOAD_DIR/bin/myisam_ftdump" \
  "$DOWNLOAD_DIR/bin/myisamchk" \
  "$DOWNLOAD_DIR/bin/myisamlog" \
  "$DOWNLOAD_DIR/bin/myisampack" \
  "$DOWNLOAD_DIR/bin/mysql_client_test" \
  "$DOWNLOAD_DIR/bin/mysql_keyring_encryption_test" \
  "$DOWNLOAD_DIR/bin/mysql_server_mock" \
  "$DOWNLOAD_DIR/bin/mysqlxtest" \
  "$DOWNLOAD_DIR/bin/perror" \
  "$DOWNLOAD_DIR/bin/zlib_decompress" \
  2>/dev/null || true

MYSQL_SHARE="$DOWNLOAD_DIR/share"
if [ -d "$MYSQL_SHARE" ]; then
  rm -rf "$MYSQL_SHARE"/charsets/*.xml 2>/dev/null || true
  rm -rf "$MYSQL_SHARE"/charsets/README 2>/dev/null || true
  rm -rf "$MYSQL_SHARE"/dictionary.txt 2>/dev/null || true
  rm -rf "$MYSQL_SHARE"/install_rewriter.sql 2>/dev/null || true
  rm -rf "$MYSQL_SHARE"/uninstall_rewriter.sql 2>/dev/null || true
fi

echo "MySQL extracted to ${DOWNLOAD_DIR}"
