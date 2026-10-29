#!/usr/bin/env bash
# Copy the released CloudDM 4.3.0 images from Docker Hub to the China registry.
# Usage: ./docker-sync-global-to-china.sh
set -euo pipefail

VERSION="4.3.0"
CHINA_REGISTRY="cloudcanal-registry.cn-shanghai.cr.aliyuncs.com"

command -v docker >/dev/null 2>&1 || {
  echo "ERROR: Docker is not installed." >&2
  exit 1
}
docker info >/dev/null
docker buildx version >/dev/null
if ! docker push --help | grep -q -- '--platform'; then
  echo "ERROR: Docker must support docker push --platform. Please update Docker." >&2
  exit 1
fi

# Reuse saved credentials; Docker prompts for login if necessary.
docker login "$CHINA_REGISTRY"

sync_service() {
  local service="$1"
  local source_image="docker.io/bladepipe/cgdm-${service}:${VERSION}"
  local target_repository="${CHINA_REGISTRY}/clougence/cgdm-${service}"

  # Push only the requested architecture, including with the containerd image store.
  echo "=== Copying ${service} ${VERSION} (amd64) ==="
  docker pull --platform linux/amd64 "$source_image"
  docker tag "$source_image" "${target_repository}:${VERSION}-amd64"
  docker push --platform linux/amd64 "${target_repository}:${VERSION}-amd64"

  echo "=== Copying ${service} ${VERSION} (arm64) ==="
  docker pull --platform linux/arm64 "$source_image"
  docker tag "$source_image" "${target_repository}:${VERSION}-arm64"
  docker push --platform linux/arm64 "${target_repository}:${VERSION}-arm64"

  echo "=== Publishing ${target_repository}:${VERSION} ==="
  docker buildx imagetools create \
    --tag "${target_repository}:${VERSION}" \
    "${target_repository}:${VERSION}-amd64" \
    "${target_repository}:${VERSION}-arm64"
  docker buildx imagetools inspect "${target_repository}:${VERSION}"
}

sync_service alone
sync_service console
sync_service sidecar

echo "CloudDM ${VERSION}: all three services copied to the China registry."
