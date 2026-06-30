#!/bin/bash
set -euo pipefail

# ---------------------------------------------------------------------------
# build-desktop.sh — Build CloudDM macOS desktop app (.dmg)
#
# Usage:
#   ./build-desktop.sh              # Build for current arch
#   ./build-desktop.sh --skip-build # Skip frontend/backend build (dev only)
#
# Prerequisites:
#   - JDK 17+
#   - Node.js 22+
#   - npm
#
# Output: dist/CloudDM-<version>-macOS-<arch>.dmg
# ---------------------------------------------------------------------------

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/backend"
PACKAGE_DIR="$SCRIPT_DIR/package"
FRONTEND_DIR="$SCRIPT_DIR/frontend"
DESKTOP_DIR="$SCRIPT_DIR/desktop"
BUILD_DIR="$DESKTOP_DIR/.build"

SKIP_BUILD=false
if [[ "${1:-}" == "--skip-build" ]]; then
  SKIP_BUILD=true
fi

# Detect version
VERSION=$(grep '^cg\.clouddm\.main\.version=' "$BACKEND_DIR/gradle.properties" | cut -d'=' -f2 | tr -d '[:space:]')
ARCH=$(uname -m)
echo "=== CloudDM Desktop Builder v${VERSION} ($ARCH) ==="

# ---------------------------------------------------------------------------
# Step 1: Build frontend
# ---------------------------------------------------------------------------
if [ "$SKIP_BUILD" = false ]; then
  echo ""
  echo "--- Step 1/6: Build frontend ---"
  cd "$FRONTEND_DIR"
  npm install --no-audit --no-fund
  npm run build:dm
  cd "$SCRIPT_DIR"
else
  echo ""
  echo "--- Step 1/6: Build frontend (skipped) ---"
fi

# ---------------------------------------------------------------------------
# Step 2: Build backend alone tgz
# ---------------------------------------------------------------------------
if [ "$SKIP_BUILD" = false ]; then
  echo ""
  echo "--- Step 2/6: Build backend ---"
  cd "$PACKAGE_DIR"
  # PlSqlParser.java (~11MB) needs large javac heap; keep workers low on memory-tight machines.
  export GRADLE_OPTS="${GRADLE_OPTS:-} -Xmx8192m -XX:MaxMetaspaceSize=1g -Dfile.encoding=UTF-8"
  export GRADLE_MAX_WORKERS="${GRADLE_MAX_WORKERS:-4}"
  bash package.sh --build
  cd "$SCRIPT_DIR"
else
  echo ""
  echo "--- Step 2/6: Build backend (skipped) ---"
fi

# ---------------------------------------------------------------------------
# Step 3: Stage files
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 3/6: Stage files ---"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

TGZ_PATH="$PACKAGE_DIR/build/cgdm-alone.tar.gz"
if [ ! -f "$TGZ_PATH" ]; then
  echo "ERROR: alone tgz not found at $TGZ_PATH"
  echo "Make sure package/package.sh --build completed successfully."
  exit 1
fi

echo "Extracting alone tgz..."
tar -xzf "$TGZ_PATH" -C "$BUILD_DIR/"
mv "$BUILD_DIR/cgdm/alone" "$BUILD_DIR/backend"
rmdir "$BUILD_DIR/cgdm"

# ---------------------------------------------------------------------------
# Slim down: remove unnecessary files for desktop distribution
# ---------------------------------------------------------------------------
echo ""
echo "--- Slimming down... ---"

# 1. Remove plugins not useful for desktop users
#    inner-auth-define is required — registers auth categories (CAT_RDP_DS, etc.) for sidebar menu
KEEP_PLUGINS="
  ds-mysql ds-mariadb ds-postgres ds-oracle ds-sqlserver
  ds-clickhouse ds-mongodb ds-redis ds-dameng
  ds-doris ds-starrocks ds-hologres
  ds-gauss ds-greenplum ds-hana
  ds-adb ds-tidb ds-oceanbase ds-polardb ds-selectdb
  plus-provider-ldap plus-provider-oidc
  plus-sec-rules plus-faker plus-file-convert
  inner-auth-define inner-driver-loader inner-website
"

BACKEND_PLUGINS="$BUILD_DIR/backend/plugins"
if [ -d "$BACKEND_PLUGINS" ]; then
  for jar in "$BACKEND_PLUGINS"/*.jar; do
    name=$(basename "$jar" -lib.jar)
    keep=false
    for kp in $KEEP_PLUGINS; do
      if [ "$name" = "$kp" ]; then keep=true; break; fi
    done
    if [ "$keep" = false ]; then
      echo "  Remove plugin: $name"
      rm "$jar"
    fi
  done
fi
echo "  Plugins after trim: $(ls "$BACKEND_PLUGINS"/*.jar 2>/dev/null | wc -l)"

# 2. Remove non-macOS native jars from libs
LIBS_DIR="$BUILD_DIR/backend/libs"
if [ -d "$LIBS_DIR" ]; then
  # netty quic native libs - keep only macOS
  rm -f "$LIBS_DIR"/netty-codec-native-quic-*-linux-*.jar 2>/dev/null
  rm -f "$LIBS_DIR"/netty-codec-native-quic-*-windows-*.jar 2>/dev/null
  echo "  Removed non-macOS native jars"
fi

echo "  Libs size after trim: $(du -sh "$LIBS_DIR" 2>/dev/null | cut -f1)"

# 3. Rewrite alone.properties for desktop — dedicated ports, TCP JDBC to bundled MySQL
ALONE_PROPS="$BUILD_DIR/backend/conf/alone.properties"
if [ -f "$ALONE_PROPS" ]; then
  sed -i '' 's/server\.port=[0-9]*/server.port=18222/' "$ALONE_PROPS"
  sed -i '' 's/clouddm\.rsocket\.console\.port=[0-9]*/clouddm.rsocket.console.port=18008/' "$ALONE_PROPS"
  sed -i '' 's|spring\.datasource\.jdbcurl=.*|spring.datasource.jdbcurl=jdbc:mysql://127.0.0.1:3307/cdmgr?useSSL=false\&allowPublicKeyRetrieval=true\&characterEncoding=utf8\&serverTimezone=Asia/Shanghai|' "$ALONE_PROPS"
  sed -i '' 's/^spring\.datasource\.username=.*/spring.datasource.username=root/' "$ALONE_PROPS"
  sed -i '' 's/^spring\.datasource\.password=.*/spring.datasource.password=cgdm/' "$ALONE_PROPS"
  echo "  Patched alone.properties for desktop ports"
fi

# Copy Electron app files
cp "$DESKTOP_DIR/main.js" "$BUILD_DIR/"
cp "$DESKTOP_DIR/preload.js" "$BUILD_DIR/"
cp "$DESKTOP_DIR/loading.html" "$BUILD_DIR/"
cp "$DESKTOP_DIR/electron-builder.yml" "$BUILD_DIR/"

# Copy and set version in package.json
sed "s/\"version\": \"0.0.0\"/\"version\": \"$VERSION\"/" "$DESKTOP_DIR/package.json" > "$BUILD_DIR/package.json"

# ---------------------------------------------------------------------------
# Step 4: Bundle MySQL
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 4/6: Bundle MySQL ---"
bash "$DESKTOP_DIR/scripts/download-mysql.sh" "$BUILD_DIR/mysql"

# Strip unnecessary MySQL share files
MYSQL_SHARE="$BUILD_DIR/mysql/share"
if [ -d "$MYSQL_SHARE" ]; then
  echo "  Stripping MySQL share..."
  rm -rf "$MYSQL_SHARE"/charsets/*.xml 2>/dev/null
  rm -rf "$MYSQL_SHARE"/charsets/README 2>/dev/null
  rm -rf "$MYSQL_SHARE"/dictionary.txt 2>/dev/null
  rm -rf "$MYSQL_SHARE"/install_rewriter.sql 2>/dev/null
  rm -rf "$MYSQL_SHARE"/uninstall_rewriter.sql 2>/dev/null
  echo "  MySQL size after trim: $(du -sh "$BUILD_DIR/mysql" 2>/dev/null | cut -f1)"
fi

# ---------------------------------------------------------------------------
# Step 5: Generate icon & install Electron deps
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 5/6: Generate icon & install deps ---"
mkdir -p "$BUILD_DIR/assets"
python3 "$DESKTOP_DIR/scripts/generate-icon.py" "$BUILD_DIR/assets/icon.png" --source "$FRONTEND_DIR/public/dm.ico"

cd "$BUILD_DIR"
npm install --no-audit --no-fund

# ---------------------------------------------------------------------------
# Step 6: Build .dmg
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 6/6: Build .dmg ---"
export ELECTRON_MIRROR="${ELECTRON_MIRROR:-https://npmmirror.com/mirrors/electron/}"
npx electron-builder --mac --config electron-builder.yml

# ---------------------------------------------------------------------------
# Done
# ---------------------------------------------------------------------------
cd "$SCRIPT_DIR"
mkdir -p "$SCRIPT_DIR/dist"

DMG=$(ls "$BUILD_DIR/dist"/*.dmg 2>/dev/null | head -1)
if [ -n "$DMG" ]; then
  cp "$DMG" "$SCRIPT_DIR/dist/"
  echo ""
  echo "=== Done ==="
  echo "DMG: dist/$(basename "$DMG")"
  echo "Size: $(du -h "$DMG" | cut -f1)"
else
  echo "ERROR: .dmg not found in $BUILD_DIR/dist/"
  echo "Check above for electron-builder errors."
  exit 1
fi

# Cleanup
rm -rf "$BUILD_DIR"
