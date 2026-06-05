#!/bin/bash
# ============================================================================
# build-docker.sh — 构建 CloudDM Docker 镜像（支持交叉编译）
#
# 用法:
#   ./build-docker.sh --platform=all        # 构建所有平台
#   ./build-docker.sh --platform=x86_64     # 仅构建 x86_64
#   ./build-docker.sh --platform=arm64      # 仅构建 arm64
# ============================================================================
set -euo pipefail

UBUNTU_MIRROR_X86_64="http://mirrors.aliyun.com/ubuntu"
UBUNTU_MIRROR_ARM64="http://mirrors.aliyun.com/ubuntu-ports"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PACKAGE_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
PACKAGE_BUILD_DIR="$PACKAGE_DIR/build"
SERVICES=(console sidecar alone)

VERSION="${1:-local}"
PLATFORMS=()
PLATFORM_SPECIFIED=0
USE_MIRRORS=0

for arg in "$@"; do
  case "$arg" in
    --platform=all)  PLATFORMS=(x86_64 arm64); PLATFORM_SPECIFIED=1 ;;
    --platform=x86_64) PLATFORMS=(x86_64); PLATFORM_SPECIFIED=1 ;;
    --platform=arm64)  PLATFORMS=(arm64); PLATFORM_SPECIFIED=1 ;;
    --mirrors) USE_MIRRORS=1 ;;
    -h|--help)
      echo "usage: $0 VERSION [--platform=x86_64|arm64|all] [--mirrors]"; exit 0 ;;
    *) ;;  # first arg is VERSION
  esac
done

[ "$PLATFORM_SPECIFIED" -eq 0 ] && PLATFORMS=(x86_64 arm64)
[ -z "$VERSION" ] && { echo "ERROR: missing VERSION"; exit 1; }

# ---- platform mapping ----
docker_platform()  { case "$1" in x86_64) echo "linux/amd64" ;; arm64) echo "linux/arm64" ;; esac; }
base_image_tag()   { echo "clougence/cgdm-${1}-base:local"; }
image_tag()        { echo "${1}-${2}"; }
apt_mirror() {
  [ "$USE_MIRRORS" -eq 1 ] || return 0
  case "$1" in
    x86_64) echo "$UBUNTU_MIRROR_X86_64" ;;
    arm64) echo "$UBUNTU_MIRROR_ARM64" ;;
  esac
}

require_package_artifacts() {
  for file_name in cgdm-console.tar.gz cgdm-sidecar.tar.gz cgdm-alone.tar.gz; do
    [ -f "$PACKAGE_BUILD_DIR/$file_name" ] || { echo "ERROR: missing $PACKAGE_BUILD_DIR/$file_name → run package/package.sh first"; exit 1; }
  done
}

ensure_builder() {
  # Use the default docker driver (direct host engine).
  # Cross-platform is done via DOCKER_DEFAULT_PLATFORM.
  docker buildx inspect default >/dev/null 2>&1 || true
}

build_base_image() {
  local plat="$1"
  local dockerfile="$SCRIPT_DIR/${plat}/base/Dockerfile"
  local tag; tag="$(base_image_tag "$plat")"
  local mirror; mirror="$(apt_mirror "$plat")"
  echo "  building base image: $tag ($(docker_platform "$plat"))"
  [ -n "$mirror" ] && echo "    apt mirror: $mirror"
  docker build \
    --platform="$(docker_platform "$plat")" \
    --build-arg APT_MIRROR="$mirror" \
    -t "$tag" \
    -f "$dockerfile" \
    "$PACKAGE_DIR"
}

build_service_image() {
  local svc="$1" plat="$2"
  local dockerfile="$SCRIPT_DIR/${plat}/${svc}/Dockerfile"
  local tag; tag="clougence/cgdm-${svc}:$(image_tag "$plat" "$VERSION")"
  echo "  building $svc: $tag ($(docker_platform "$plat"))"
  docker build \
    --platform="$(docker_platform "$plat")" \
    --build-arg BASE_IMAGE="$(base_image_tag "$plat")" \
    -t "$tag" \
    -f "$dockerfile" \
    "$PACKAGE_DIR"
}

export_service_image() {
  local svc="$1" plat="$2"
  local tag; tag="clougence/cgdm-${svc}:$(image_tag "$plat" "$VERSION")"
  local output="$PACKAGE_BUILD_DIR/docker-${svc}-$(image_tag "$plat" "$VERSION").tar"
  echo "  exporting $tag → $output"
  docker save "$tag" -o "$output"
}

generate_compose_files() {
  local plat="$1"
  local compose_src="$SCRIPT_DIR"
  for name in alone cluster; do
    local src="$compose_src/docker-${name}.yml"
    local dst="$PACKAGE_BUILD_DIR/docker-${name}-$(image_tag "$plat" "$VERSION").yml"
    if [ -f "$src" ]; then
      sed "s|\${build_version}|$(image_tag "$plat" "$VERSION")|g" "$src" > "$dst"
      echo "  generated compose: $dst"
    fi
  done
}

generate_k8s_files() {
  local plat="$1"
  local k8s_src="$SCRIPT_DIR"
  for name in alone cluster; do
    local src="$k8s_src/k8s-${name}.yml"
    local dst="$PACKAGE_BUILD_DIR/k8s-${name}-$(image_tag "$plat" "$VERSION").yml"
    if [ -f "$src" ]; then
      sed "s|\${build_version}|$(image_tag "$plat" "$VERSION")|g" "$src" > "$dst"
      echo "  generated k8s: $dst"
    fi
  done
}

host_platform() {
  case "$(uname -m)" in
    x86_64|amd64) echo "x86_64" ;;
    arm64|aarch64) echo "arm64" ;;
    *) echo "" ;;
  esac
}

contains_platform() {
  local expected="$1"
  local plat
  for plat in "${PLATFORMS[@]}"; do
    [ "$plat" = "$expected" ] && return 0
  done
  return 1
}

preferred_local_platform() {
  local host; host="$(host_platform)"
  if [ -n "$host" ] && contains_platform "$host"; then
    echo "$host"
    return
  fi
  echo "${PLATFORMS[0]}"
}

print_local_run_commands() {
  local plat; plat="$(preferred_local_platform)"
  local tag; tag="$(image_tag "$plat" "$VERSION")"
  local image="clougence/cgdm-alone:${tag}"
  local compose_file="$PACKAGE_BUILD_DIR/docker-alone-${tag}.yml"

  cat <<EOF

Local verification commands:
  standalone container:
    docker rm -f cgdm-alone >/dev/null 2>&1 || true; docker run -d --name cgdm-alone -p 8222:8222 -p 8008:8008 ${image}

  compose with persisted volumes:
    docker compose -f ${compose_file} down; docker compose -f ${compose_file} up -d

  open:
    http://localhost:8222/#/initialization
EOF
}

# ============================================================================
# main
# ============================================================================
require_package_artifacts
ensure_builder

for plat in "${PLATFORMS[@]}"; do
  echo "=== Building platform: $plat ==="
  build_base_image "$plat"
  for svc in "${SERVICES[@]}"; do
    build_service_image "$svc" "$plat"
  done
  for svc in "${SERVICES[@]}"; do
    export_service_image "$svc" "$plat"
  done
  generate_compose_files "$plat"
  generate_k8s_files "$plat"
done

echo "Docker build completed. platforms=${PLATFORMS[*]} version=${VERSION}"
print_local_run_commands
