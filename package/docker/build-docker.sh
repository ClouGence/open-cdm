#!/bin/bash
# ============================================================================
# build-docker.sh — Build CloudDM Docker images using buildx (multi-arch)
#
# Usage:
#   ./build-docker.sh VERSION                       # build host arch, --load
#   ./build-docker.sh VERSION --platform=all        # build x86_64+arm64, save tars
#   ./build-docker.sh VERSION --platform=x86_64     # build x86_64 only, save tar
#   ./build-docker.sh VERSION --platform=arm64      # build arm64 only, save tar
#
# Output:
#   docker image clougence/cgdm-{service}:{arch}-{VERSION}   (always loaded locally)
#   PACKAGE_BUILD_DIR/docker-{service}-{arch}-{VERSION}.tar  (when --platform set)
# ============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PACKAGE_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
PACKAGE_BUILD_DIR="$PACKAGE_DIR/build"
SERVICES=(console sidecar alone)

case "${1:-}" in
  -h|--help) sed -n '2,15p' "$0"; exit 0 ;;
esac

VERSION="${1:-local}"
shift || true

PLATFORMS=()
PLATFORM_SPECIFIED=0

for arg in "$@"; do
  case "$arg" in
    --platform=all)    PLATFORMS=(x86_64 arm64); PLATFORM_SPECIFIED=1 ;;
    --platform=x86_64) PLATFORMS=(x86_64); PLATFORM_SPECIFIED=1 ;;
    --platform=arm64)  PLATFORMS=(arm64); PLATFORM_SPECIFIED=1 ;;
    -h|--help) sed -n '2,15p' "$0"; exit 0 ;;
    *) echo "unknown argument: $arg"; exit 1 ;;
  esac
done

# default: host arch only, no tar export
if [ "$PLATFORM_SPECIFIED" -eq 0 ]; then
  case "$(uname -m)" in
    x86_64|amd64) PLATFORMS=(x86_64) ;;
    arm64|aarch64) PLATFORMS=(arm64) ;;
    *) echo "ERROR: cannot detect host arch ($(uname -m))"; exit 1 ;;
  esac
fi

[ -z "$VERSION" ] && { echo "ERROR: missing VERSION"; exit 1; }

docker_platform()  { case "$1" in x86_64) echo "linux/amd64" ;; arm64) echo "linux/arm64" ;; esac; }
image_tag()        { echo "${1}-${2}"; }

require_package_artifacts() {
  for file_name in cgdm-console.tar.gz cgdm-sidecar.tar.gz cgdm-alone.tar.gz; do
    [ -f "$PACKAGE_BUILD_DIR/$file_name" ] || {
      echo "ERROR: missing $PACKAGE_BUILD_DIR/$file_name → run package/package.sh --build first"
      exit 1
    }
  done
}

stage_build_context() {
  local ctx="$1"
  rm -rf "$ctx"
  mkdir -p "$ctx"
  cp "$PACKAGE_BUILD_DIR/cgdm-console.tar.gz" "$ctx/"
  cp "$PACKAGE_BUILD_DIR/cgdm-sidecar.tar.gz" "$ctx/"
  cp "$PACKAGE_BUILD_DIR/cgdm-alone.tar.gz" "$ctx/"
  cp -r "$SCRIPT_DIR/shared" "$ctx/shared"
}

ensure_builder() {
  docker buildx inspect cgdm-builder >/dev/null 2>&1 || \
    docker buildx create --name cgdm-builder --use >/dev/null
  docker buildx use cgdm-builder >/dev/null
}

build_service_image() {
  local svc="$1" plat="$2" ctx="$3"
  local tag; tag="clougence/cgdm-${svc}:$(image_tag "$plat" "$VERSION")"
  local dockerfile="$SCRIPT_DIR/${svc}.Dockerfile"
  echo "  building $svc: $tag ($(docker_platform "$plat"))"
  docker buildx build \
    --platform="$(docker_platform "$plat")" \
    --tag "$tag" \
    --file "$dockerfile" \
    --load \
    "$ctx"
}

export_service_image() {
  local svc="$1" plat="$2"
  local tag; tag="clougence/cgdm-${svc}:$(image_tag "$plat" "$VERSION")"
  local output="$PACKAGE_BUILD_DIR/docker-${svc}-$(image_tag "$plat" "$VERSION").tar"
  echo "  exporting $tag → $output"
  docker save "$tag" -o "$output"
}

generate_per_platform_yml() {
  local plat="$1"
  for name in alone cluster; do
    for kind in docker k8s; do
      local src="$SCRIPT_DIR/${kind}-${name}.yml"
      local dst="$PACKAGE_BUILD_DIR/${kind}-${name}-$(image_tag "$plat" "$VERSION").yml"
      if [ -f "$src" ]; then
        sed "s|\${build_version}|$(image_tag "$plat" "$VERSION")|g" "$src" > "$dst"
        echo "  generated: $dst"
      fi
    done
  done
}

host_platform() {
  case "$(uname -m)" in
    x86_64|amd64) echo "x86_64" ;;
    arm64|aarch64) echo "arm64" ;;
    *) echo "" ;;
  esac
}

print_local_run_commands() {
  local plat="${PLATFORMS[0]}"
  local host; host="$(host_platform)"
  for p in "${PLATFORMS[@]}"; do
    [ "$p" = "$host" ] && plat="$p" && break
  done
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

CTX_DIR="$PACKAGE_BUILD_DIR/docker-ctx"
stage_build_context "$CTX_DIR"

for plat in "${PLATFORMS[@]}"; do
  echo "=== Building platform: $plat ==="
  for svc in "${SERVICES[@]}"; do
    build_service_image "$svc" "$plat" "$CTX_DIR"
  done
  if [ "$PLATFORM_SPECIFIED" -eq 1 ]; then
    for svc in "${SERVICES[@]}"; do
      export_service_image "$svc" "$plat"
    done
    generate_per_platform_yml "$plat"
  fi
done

echo "Docker build completed. platforms=${PLATFORMS[*]} version=${VERSION}"
print_local_run_commands
