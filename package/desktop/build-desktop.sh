#!/bin/bash
set -euo pipefail

# ---------------------------------------------------------------------------
# build-desktop.sh — Build CloudDM macOS desktop app (.dmg)
#
# Usage:
#   ./build-desktop.sh              # Build, sign (no notarize)
#   ./build-desktop.sh --skip-build # Skip frontend/backend build
#   ./build-desktop.sh --notarize   # Build, sign & notarize
#
# Prerequisites:
#   - JDK 17+
#   - Node.js 22+
#   - npm
#
# Output: dist/CloudDM-<version>-macOS-<arch>.dmg
# ---------------------------------------------------------------------------

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
BACKEND_DIR="$REPO_ROOT/backend"
PACKAGE_DIR="$REPO_ROOT/package"
FRONTEND_DIR="$REPO_ROOT/frontend"
DESKTOP_DIR="$SCRIPT_DIR"
BUILD_DIR="$DESKTOP_DIR/.build"

SKIP_BUILD=false
NOTARIZE=false
for arg in "$@"; do
  case "$arg" in
    --skip-build) SKIP_BUILD=true ;;
    --notarize)   NOTARIZE=true ;;
  esac
done

ICON_SRC="$DESKTOP_DIR/assets/icon.png"

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
  cd "$REPO_ROOT"
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
  cd "$REPO_ROOT"
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
  # netty quic native libs - remove all (unsigned .jnilib files fail notarization)
  rm -f "$LIBS_DIR"/netty-codec-native-quic-*.jar 2>/dev/null

  # Remove x86_64-only native JARs when building for arm64
  if [ "$ARCH" = "arm64" ]; then
    rm -f "$LIBS_DIR"/*-x86_64.jar 2>/dev/null
  fi
  echo "  Removed non-macOS native jars"

fi

BUILTIN_DRIVERS_DIR="$BUILD_DIR/backend/built-in-drivers"
# Sign native libraries (.jnilib/.dylib) embedded inside JARs for notarization.
# Starting late 2024, Apple requires ALL binaries in the bundle to be signed,
# including those inside ZIP/JAR archives. codesign --deep cannot reach them,
# so we extract → sign → repack with jar.
# Main branch now ships built-in-drivers in alone tgz; scan both libs and that tree.
sign_native_libs_in_jars() {
  local scan_root="$1"
  [ -d "$scan_root" ] || return 0

  echo "  Signing native libs embedded in JARs under $scan_root..."
  local tmp_native_dir
  tmp_native_dir=$(mktemp -d)
  local ntlist_file
  ntlist_file=$(mktemp)
  local jar_count=0

  while IFS= read -r jar; do
    [ -f "$jar" ] || continue
    jar_count=$((jar_count + 1))
    unzip -l "$jar" 2>/dev/null | grep -E '\.(jnilib|dylib)$' | awk '{print $4}' > "$ntlist_file" || true
    [ -s "$ntlist_file" ] || continue
    echo "    $jar"
    while IFS= read -r nf; do
      [ -n "$nf" ] || continue
      echo "      Extracting $nf..."
      unzip -o -d "$tmp_native_dir" "$jar" "$nf" 2>/dev/null || { echo "      ERROR: unzip failed"; continue; }
      echo "      Signing $nf..."
      codesign --force --options runtime --timestamp \
        --sign "Developer ID Application" \
        "$tmp_native_dir/$nf" || { echo "      ERROR: codesign failed"; continue; }
      echo "      Repacking $nf..."
      (cd "$tmp_native_dir" && jar uf "$jar" "$nf") || { echo "      ERROR: jar update failed"; continue; }
      rm -rf "$tmp_native_dir/${nf%%/*}" 2>/dev/null
    done < "$ntlist_file"
  done < <(find "$scan_root" -name '*.jar' -type f | sort)

  rm -rf "$tmp_native_dir" "$ntlist_file"
  echo "  Scanned $jar_count JARs under $scan_root."
}

if security find-identity -v -p basic 2>/dev/null | grep -q "Developer ID Application"; then
  sign_native_libs_in_jars "$LIBS_DIR"
  sign_native_libs_in_jars "$BUILTIN_DRIVERS_DIR"
else
  echo "  Skipping JAR native lib signing (Developer ID Application cert not found)."
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
cp "$DESKTOP_DIR/entitlements.plist" "$BUILD_DIR/"

# Copy and set version in package.json
sed "s/\"version\": \"0.0.0\"/\"version\": \"$VERSION\"/" "$DESKTOP_DIR/package.json" > "$BUILD_DIR/package.json"

# ---------------------------------------------------------------------------
# Step 4: Bundle MySQL
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 4/6: Bundle MySQL ---"
bash "$DESKTOP_DIR/scripts/download-mysql.sh" "$BUILD_DIR/mysql"

# ---------------------------------------------------------------------------
# Step 5: Stage icon & install Electron deps
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 5/6: Stage icon & install deps ---"
mkdir -p "$BUILD_DIR/assets"

cp "$ICON_SRC" "$BUILD_DIR/assets/icon.png"

cd "$BUILD_DIR"
npm install --no-audit --no-fund

# ---------------------------------------------------------------------------
# Step 6: Build .dmg (sign & notarize)
# ---------------------------------------------------------------------------
echo ""
echo "--- Step 6/6: Build .dmg ---"

# Code signing: electron-builder auto-detects Developer ID certs from your keychain.
# Notarization: only when --notarize flag is passed AND the required env vars are set.
if [ "$NOTARIZE" = true ]; then
  if [ -z "${APPLE_ID:-}" ] || [ -z "${APPLE_APP_SPECIFIC_PASSWORD:-}" ] || [ -z "${APPLE_TEAM_ID:-}" ]; then
    echo "ERROR: --notarize requires APPLE_ID, APPLE_APP_SPECIFIC_PASSWORD, and APPLE_TEAM_ID env vars."
    echo "  export APPLE_ID=your-apple-id@email.com"
    echo "  export APPLE_APP_SPECIFIC_PASSWORD=xxxx-xxxx-xxxx-xxxx"
    echo "  export APPLE_TEAM_ID=YOUR_TEAM_ID"
    exit 1
  fi
  echo "Notarization enabled (APPLE_ID=$APPLE_ID, TEAM=$APPLE_TEAM_ID)"
else
  echo "Skipping notarization — add --notarize to enable."
fi

export ELECTRON_MIRROR="${ELECTRON_MIRROR:-https://npmmirror.com/mirrors/electron/}"
npx electron-builder --mac --config electron-builder.yml

# ---------------------------------------------------------------------------
# Done
# ---------------------------------------------------------------------------
cd "$REPO_ROOT"
mkdir -p "$REPO_ROOT/dist"

DMG=$(ls "$BUILD_DIR/dist"/*.dmg 2>/dev/null | head -1)
if [ -n "$DMG" ]; then
  cp "$DMG" "$REPO_ROOT/dist/"
  echo ""
  echo "=== Done ==="
  echo "DMG: dist/$(basename "$DMG")"
  echo "Size: $(du -h "$DMG" | cut -f1)"
  if [ -z "${APPLE_ID:-}" ]; then
    echo ""
    echo "To sign & notarize the next build, set these env vars:"
    echo "  export APPLE_ID=your-apple-id@email.com"
    echo "  export APPLE_APP_SPECIFIC_PASSWORD=xxxx-xxxx-xxxx-xxxx"
    echo "  export APPLE_TEAM_ID=YOUR_TEAM_ID"
    echo ""
    echo "App-specific password: https://appleid.apple.com/account/manage"
  fi
else
  echo "ERROR: .dmg not found in $BUILD_DIR/dist/"
  echo "Check above for electron-builder errors."
  exit 1
fi

# Cleanup
rm -rf "$BUILD_DIR"
