#!/bin/bash
# ============================================================================
# Package.sh - CloudDM Build and Pack Scripts
#
# Usage:
#   ./package.sh --build                       # Only compile + tgz pack
#   ./package.sh --docker                      # Docker mirror only (two platforms)
#   ./package.sh --docker x86_64               # Docker mirror only (x86 64)
#   ./package.sh --docker arm64                # Docker mirror (arm64)
#   ./package.sh --docker arm64 --mirrors      # Use the built in Ubuntu mirror source to build Docker mirrors
#   ./package.sh --build --docker              # Compile + tgz + Docker Full Platform
#   ./package.sh --build --docker x86_64       # Compiled + tgz + Docker(x86_64)
# ============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BACKEND_DIR="$REPO_ROOT/backend"
PACKAGE_BUILD_DIR="$SCRIPT_DIR/build"

VERSION="$(grep '^cg\.clouddm\.main\.version=' "$BACKEND_DIR/gradle.properties" | cut -d'=' -f2 | tr -d '[:space:]')"
[ -z "$VERSION" ] && { echo "error: cg.clouddm.main.version not found"; exit 1; }

DO_BUILD=0
DO_DOCKER=0
DOCKER_ARCH=""
USE_MIRRORS=0

print_help() {
  if [ "${1:-}" = "--with-version" ]; then
    echo "version: $VERSION"
    echo ""
  fi
  cat <<'HELP'
Usage: ./package.sh [--build] [--docker [x86_64|arm64]] [--mirrors]

Build modes (at least one required):
  --build                 Compile backend/frontend and create tgz packages.
  --docker [ARCH]         Build Docker images from package artifacts.
                          ARCH can be x86_64 or arm64.
                          If ARCH is omitted, build all platforms.

Options:
  --mirrors               Use built-in Ubuntu mirror apt sources while building Docker images.
  -h, --help              Show this help.

Common commands:
  ./package.sh --build
      Compile and create console, sidecar and alone tgz packages.

  ./package.sh --docker
      Build Docker images for all platforms from existing tgz packages.

  ./package.sh --docker x86_64
      Build x86_64 Docker images from existing tgz packages.

  ./package.sh --docker arm64
      Build arm64 Docker images from existing tgz packages.

  ./package.sh --build --docker
      Compile, create tgz packages, then build Docker images for all platforms.

  ./package.sh --build --docker x86_64
      Compile, create tgz packages, then build x86_64 Docker images.

  ./package.sh --docker arm64 --mirrors
      Build arm64 Docker images with built-in Ubuntu mirrors.

Generated artifacts:
  package/build/
      clouddm-console-<version>.tar.gz
      clouddm-sidecar-<version>.tar.gz
      clouddm-alone-<version>.tar.gz

Docker build prerequisites:
  Run ./package.sh --build first, unless package/build already contains tgz artifacts.
HELP
}

while [ $# -gt 0 ]; do
  arg="$1"
  case "$arg" in
    --build)    DO_BUILD=1; shift ;;
    --docker)
      DO_DOCKER=1; shift
      if [ $# -gt 0 ] && [[ "$1" != --* ]]; then
        DOCKER_ARCH="$1"; shift
      fi
      ;;
    --mirrors)  USE_MIRRORS=1; shift ;;
    -h|--help)
      print_help
      exit 0
      ;;
    *) echo "unknown argument: $arg"; exit 1 ;;
  esac
done

# No Parameter Printing Help
if [ "$DO_BUILD" -eq 0 ] && [ "$DO_DOCKER" -eq 0 ]; then
  print_help --with-version
  exit 0
fi

echo "version: $VERSION"

# ---- Step 1: Build ----
if [ "$DO_BUILD" -eq 1 ]; then
  echo "=== Build: starting Gradle compile + tgz ==="
  rm -rf "$SCRIPT_DIR/pkg/console/build"
  rm -rf "$SCRIPT_DIR/pkg/sidecar/build"
  rm -rf "$SCRIPT_DIR/pkg/alone/build"
  rm -rf "$PACKAGE_BUILD_DIR"

  "$BACKEND_DIR/gradlew" -p "$BACKEND_DIR" -Ptarget=all clean
  "$BACKEND_DIR/gradlew" -p "$BACKEND_DIR" -Ptarget=all -Pprofile=output -PbuildFrontend=true \
    buildx local installDist tgz -x test --rerun-tasks --parallel --max-workers=8

  mkdir -p "$PACKAGE_BUILD_DIR"
  find "$SCRIPT_DIR/pkg/console/build/dist" -maxdepth 1 -type f ! -name '.DS_Store' -exec cp {} "$PACKAGE_BUILD_DIR/" \;
  find "$SCRIPT_DIR/pkg/sidecar/build/dist" -maxdepth 1 -type f ! -name '.DS_Store' -exec cp {} "$PACKAGE_BUILD_DIR/" \;
  find "$SCRIPT_DIR/pkg/alone/build/dist" -maxdepth 1 -type f ! -name '.DS_Store' -exec cp {} "$PACKAGE_BUILD_DIR/" \;

  echo "[BUILD] done. version=${VERSION}"
fi

# ---- Step 2: Docker ----
if [ "$DO_DOCKER" -eq 1 ]; then
  echo "=== Docker: generating built-in drivers ==="
  "$BACKEND_DIR/gradlew" -p "$BACKEND_DIR" -Ptarget=all :pkg-builtin-drivers:generateBuiltinDrivers

  echo "=== Docker: starting image build ==="
  run_docker_build() {
    local platform_arg="$1"
    if [ "$USE_MIRRORS" -eq 1 ]; then
      bash "$SCRIPT_DIR/docker/build-docker.sh" "$VERSION" "$platform_arg" --mirrors
    else
      bash "$SCRIPT_DIR/docker/build-docker.sh" "$VERSION" "$platform_arg"
    fi
  }

  if [ -z "$DOCKER_ARCH" ]; then
    echo "[DOCKER] building all platforms, version=${VERSION}..."
    run_docker_build --platform=all
  else
    echo "[DOCKER] building $DOCKER_ARCH images, version=${VERSION}..."
    run_docker_build --platform="$DOCKER_ARCH"
  fi
fi
