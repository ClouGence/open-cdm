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

# Download if not cached
if [ ! -f "$CACHED_TARBALL" ]; then
  echo "Downloading MySQL ${MYSQL_VERSION} for ${ARCH}..."
  DOWNLOAD_URL="${MIRROR}/MySQL-8.0/${TARBALL}"
  echo "  URL: ${DOWNLOAD_URL}"

  if ! curl -fSL --connect-timeout 30 --max-time 900 --retry 3 -C - "${DOWNLOAD_URL}" -o "${CACHED_TARBALL}.tmp"; then
    # fallback to official CDN
    FALLBACK_URL="https://cdn.mysql.com/Downloads/MySQL-8.0/${TARBALL}"
    echo "Mirror failed, trying official CDN: ${FALLBACK_URL}"
    rm -f "${CACHED_TARBALL}.tmp"
    curl -fSL --connect-timeout 30 --max-time 900 --retry 3 -C - "${FALLBACK_URL}" -o "${CACHED_TARBALL}.tmp"
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

echo "MySQL extracted to ${DOWNLOAD_DIR}"
