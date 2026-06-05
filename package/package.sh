#!/bin/bash
# ============================================================================
# package.sh — CloudDM 构建和打包脚本
#
# 用法:
#   ./package.sh --build                       # 仅编译 + tgz 打包
#   ./package.sh --docker                      # 仅 Docker 镜像(双平台)
#   ./package.sh --docker x86_64               # 仅 Docker 镜像(x86_64)
#   ./package.sh --docker arm64                # 仅 Docker 镜像(arm64)
#   ./package.sh --docker arm64 --mirrors      # 使用内置 Ubuntu 镜像源构建 Docker 镜像
#   ./package.sh --build --docker              # 编译 + tgz + Docker 全平台
#   ./package.sh --build --docker x86_64       # 编译 + tgz + Docker(x86_64)
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
      cat <<'HELP'
usage: package.sh [--build] [--docker [x86_64|arm64]] [--mirrors]

  --build           compile and create tgz packages
  --docker          build Docker images for all platforms (requires tgz)
  --docker arm64    build Docker images for arm64 only
  --docker x86_64   build Docker images for x86_64 only
  --mirrors         use built-in Ubuntu mirrors for Docker base image apt sources

Combine both: ./package.sh --build --docker
HELP
      exit 0
      ;;
    *) echo "unknown argument: $arg"; exit 1 ;;
  esac
done

# 无参数打印帮助
if [ "$DO_BUILD" -eq 0 ] && [ "$DO_DOCKER" -eq 0 ]; then
  echo "version: $VERSION"
  echo ""
  cat <<'HELP'
Usage: ./package.sh [OPTIONS]...

Build modes (at least one required):
  --build               compile + tgz packaging (Gradle build)
  --docker [ARCH]       build Docker images (requires package artifacts)
                          ARCH: x86_64 | arm64 (default: all platforms)
  --mirrors             use built-in Ubuntu mirrors for Docker base image apt sources

Examples:
  ./package.sh --build                      compile & package only
  ./package.sh --docker                     build all Docker images
  ./package.sh --build --docker             compile + all Docker images
  ./package.sh --build --docker --mirrors   compile + all Docker images with built-in mirrors
  ./package.sh --build --docker x86_64      compile + x86_64 Docker images only
  ./package.sh --docker arm64 --mirrors     Docker arm64 images with built-in mirrors

Prerequisites:
  Gradle + JDK configured
  Docker installed and running
HELP
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
