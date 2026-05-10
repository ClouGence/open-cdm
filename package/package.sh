#!/bin/bash
set -euo pipefail
echo 'start to build CloudDM(MacOS)'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
PACKAGE_BUILD_DIR="$SCRIPT_DIR/build"

# Read version from gradle.properties
VERSION="$(grep '^cg\.clouddm\.main\.version=' "$REPO_ROOT/gradle.properties" | cut -d'=' -f2 | tr -d '[:space:]')"
if [ -z "$VERSION" ]; then
  echo "error: cg.clouddm.main.version not found in gradle.properties"
  exit 1
fi

DOCKER_ARCH=""

for arg in "$@"; do
  case "$arg" in
    --docker=*)
      DOCKER_ARCH="${arg#--docker=}"
      ;;
    --docker)
      case "$(uname -m)" in
        arm64|aarch64) DOCKER_ARCH="arm64" ;;
        x86_64|amd64)  DOCKER_ARCH="x86_64" ;;
        *)
          echo "unsupported architecture: $(uname -m), use --docker=arm64 or --docker=x86_64"
          exit 1
          ;;
      esac
      ;;
    *)
      echo "unknown argument: $arg"
      echo "usage: package.sh [--docker[=arm64|x86_64]]"
      exit 1
      ;;
  esac
done

if [ -n "$DOCKER_ARCH" ] && [ "$DOCKER_ARCH" != "arm64" ] && [ "$DOCKER_ARCH" != "x86_64" ]; then
  echo "invalid docker arch: $DOCKER_ARCH, must be arm64 or x86_64"
  exit 1
fi

echo "version: $VERSION"

rm -rf "$SCRIPT_DIR/pkg/console/build"
rm -rf "$SCRIPT_DIR/pkg/sidecar/build"
rm -rf "$SCRIPT_DIR/pkg/alone/build"
rm -rf "$PACKAGE_BUILD_DIR"

"$REPO_ROOT/gradlew" -p "$REPO_ROOT" -Ptarget=all clean
"$REPO_ROOT/gradlew" -p "$REPO_ROOT" -Ptarget=all -Pprofile=output -PbuildFrontend=true \
  buildx local installDist tgz -x test --rerun-tasks --parallel --max-workers=8

mkdir -p "$PACKAGE_BUILD_DIR"
find "$SCRIPT_DIR/pkg/console/build/dist" -maxdepth 1 -type f ! -name '.DS_Store' -exec cp {} "$PACKAGE_BUILD_DIR/" \;
find "$SCRIPT_DIR/pkg/sidecar/build/dist" -maxdepth 1 -type f ! -name '.DS_Store' -exec cp {} "$PACKAGE_BUILD_DIR/" \;
find "$SCRIPT_DIR/pkg/alone/build/dist" -maxdepth 1 -type f ! -name '.DS_Store' -exec cp {} "$PACKAGE_BUILD_DIR/" \;

echo "[BUILD] completed. version=${VERSION}"
echo ""

if [ -n "$DOCKER_ARCH" ]; then
  echo "[DOCKER] building $DOCKER_ARCH images, version=${VERSION}..."
  bash "$SCRIPT_DIR/docker/$DOCKER_ARCH/build.sh" "$VERSION"
  echo "[DOCKER] $DOCKER_ARCH build completed."
fi
