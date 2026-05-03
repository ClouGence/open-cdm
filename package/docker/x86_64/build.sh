#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PACKAGE_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
PACKAGE_BUILD_DIR="$PACKAGE_DIR/build"
BASE_IMAGE_TAG="clougence/cgdm-x86_64-base:local"

VERSION="${1:-local}"
IMAGE_TAG="x86_64-${VERSION}"

require_package_artifacts() {
  for file_name in cgdm-console.tar.gz cgdm-sidecar.tar.gz cgdm-alone.tar.gz; do
    if [ ! -f "$PACKAGE_BUILD_DIR/$file_name" ]; then
      echo "missing package artifact: $PACKAGE_BUILD_DIR/$file_name"
      echo "run package/package.sh first"
      exit 1
    fi
  done
}

build_base_image() {
  docker build \
    -t "$BASE_IMAGE_TAG" \
    -f "$SCRIPT_DIR/base/Dockerfile" \
    "$PACKAGE_DIR"
}

build_service_image() {
  local service_name="$1"

  docker build \
    --build-arg BASE_IMAGE="$BASE_IMAGE_TAG" \
    -t "clougence/cgdm-${service_name}:${IMAGE_TAG}" \
    -f "$SCRIPT_DIR/$service_name/Dockerfile" \
    "$PACKAGE_DIR"
}

export_service_image() {
  local service_name="$1"
  local full_tag="clougence/cgdm-${service_name}:${IMAGE_TAG}"
  local output_file="$PACKAGE_BUILD_DIR/docker-${service_name}-${IMAGE_TAG}.tar"

  echo "exporting $full_tag → $output_file"
  docker save "$full_tag" -o "$output_file"
}

generate_compose_files() {
  local compose_src="$SCRIPT_DIR/.."
  for name in alone cluster; do
    local src="$compose_src/docker-${name}.yml"
    local dst="$PACKAGE_BUILD_DIR/docker-${name}-${IMAGE_TAG}.yml"
    sed "s|\${build_version}|${IMAGE_TAG}|g" "$src" > "$dst"
    echo "generated compose: $dst"
  done
}

require_package_artifacts
build_base_image
build_service_image console
build_service_image sidecar
build_service_image alone
export_service_image console
export_service_image sidecar
export_service_image alone
generate_compose_files

echo "x86_64 docker build completed. version=${VERSION}"
