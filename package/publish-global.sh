#!/bin/bash
# ============================================================================
# publish-global.sh — 发布 CloudDM 镜像到国际区（Docker Hub）
#
# 用法:
#   ./publish-global.sh                         # 自动探测所有已构建平台
#   ./publish-global.sh --platform=x86_64       # 仅推送 x86_64
#   ./publish-global.sh --platform=x86_64,arm64 # 推送双平台
#
# 前置: 运行 package/package.sh --docker 完成编译和镜像构建
# ============================================================================
set -euo pipefail

REGISTRY="docker.io"
NAMESPACE="${DOCKER_NAMESPACE:-bladepipe}"
LABEL="Global"
CRED_PREFIX="global"
SOURCE_NAMESPACE="clougence"
SERVICES=(console sidecar alone)
DEFAULT_PLATFORMS=(arm64 x86_64)
PLATFORMS=()
PLATFORM_SPECIFIED=0

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PACKAGE_DIR="$SCRIPT_DIR"
OPEN_CDM_DIR="$(cd "$PACKAGE_DIR/.." && pwd)"
PKG_BUILD_DIR="$PACKAGE_DIR/build"

log_info()  { echo "[publish] $*"; }
log_warn()  { echo "[publish] WARNING: $*" >&2; }
log_ok()    { echo "[publish] ✔ $*"; }

read_prop() {
  local key="$1"
  local props="${GRADLE_PROPERTIES:-$HOME/.gradle/gradle.properties}"
  [ ! -f "$props" ] && { echo ""; return 0; }
  grep -m1 -E "^\s*${key}\s*=" "$props" 2>/dev/null | sed "s/^\s*${key}\s*=\s*//" | tr -d '[:space:]' || true
}

check_login() { [ -f "$HOME/.docker/config.json" ] && grep -q "$1" "$HOME/.docker/config.json" 2>/dev/null; }

do_login() {
  local r="$1" u="$2" p="$3" l="$4"
  [ -z "$u" ] || [ -z "$p" ] && { log_warn "No credentials for ${l}"; return 0; }
  log_info "Logging into ${l} registry: ${r} ..."
  if echo "$p" | docker login "$r" --username "$u" --password-stdin 2>/dev/null; then
    log_ok "Login to ${r} successful"
  else
    log_warn "Login to ${r} failed. Check credentials in ~/.gradle/gradle.properties"
  fi
}

normalize_arch() { case "$1" in arm64) echo "arm64" ;; x86_64) echo "amd64" ;; esac }
docker_platform()  { case "$1" in x86_64) echo "linux/amd64" ;; arm64) echo "linux/arm64" ;; esac }
target_prefix() { echo "$REGISTRY/$NAMESPACE/cgdm-$1"; }

pkg_tar() { echo "$PKG_BUILD_DIR/docker-${1}-${2}-${3}.tar"; }

has_service_source() {
  local svc="$1" plat="$2" ver="$3"
  docker image inspect "$SOURCE_NAMESPACE/cgdm-${svc}:${plat}-${ver}" >/dev/null 2>&1 && return 0
  local t; t="$(pkg_tar "$svc" "$plat" "$ver")"
  [ -f "$t" ] && [ -s "$t" ]
}

resolve_platforms() {
  if [ "$PLATFORM_SPECIFIED" -eq 1 ]; then
    local plat svc
    for plat in "${PLATFORMS[@]}"; do
      for svc in "${SERVICES[@]}"; do
        if ! has_service_source "$svc" "$plat" "$VERSION"; then
          echo "ERROR: platform $plat is missing $SOURCE_NAMESPACE/cgdm-${svc}:${plat}-${VERSION} (and no tar in package/build/)" >&2
          exit 1
        fi
      done
    done
    return 0
  fi

  local plat svc missing=()
  for plat in "${DEFAULT_PLATFORMS[@]}"; do
    for svc in "${SERVICES[@]}"; do
      if ! has_service_source "$svc" "$plat" "$VERSION"; then
        missing+=("$SOURCE_NAMESPACE/cgdm-${svc}:${plat}-${VERSION}")
      fi
    done
  done

  if [ ${#missing[@]} -gt 0 ]; then
    echo "ERROR: --platform not specified, all platforms (${DEFAULT_PLATFORMS[*]}) required, but missing:" >&2
    for m in "${missing[@]}"; do echo "  - $m (or tar in package/build/)" >&2; done
    echo "Run package/package.sh --docker to build all platforms, or use --platform=x86_64 to publish only x86_64." >&2
    exit 1
  fi

  PLATFORMS=("${DEFAULT_PLATFORMS[@]}")
  log_info "Publishing all platforms: ${PLATFORMS[*]}"
}

ensure_image() {
  local svc="$1" plat="$2" ver="$3"
  local tag="$SOURCE_NAMESPACE/cgdm-${svc}:${plat}-${ver}"
  docker image inspect "$tag" >/dev/null 2>&1 && return 0
  local t; t="$(pkg_tar "$svc" "$plat" "$ver")"
  [ -f "$t" ] && [ -s "$t" ] || { echo "ERROR: missing $t → run package/package.sh --docker first"; exit 1; }
  echo "  loading $t"; docker load -i "$t" >/dev/null
}

clean_unknown_manifest() {
  local tag="$1" inspect refs args=()
  inspect="$(docker manifest inspect "$tag" 2>/dev/null)" || return 0
  local has_unknown
  has_unknown="$(echo "$inspect" | python3 -c "
import json,sys
d=json.load(sys.stdin)
if d.get('mediaType','').endswith('image.index.v1+json'):
    for m in d.get('manifests',[]):
        p=m.get('platform',{})
        if p.get('os')=='unknown' and p.get('architecture')=='unknown':
            print('yes'); sys.exit(0)
print('no')
" 2>/dev/null)"
  [ "$has_unknown" != "yes" ] && return 0
  echo "  cleaning unknown attestation from $tag ..."
  refs="$(echo "$inspect" | python3 -c "
import json,sys
d=json.load(sys.stdin)
for m in d.get('manifests',[]):
    p=m.get('platform',{})
    if p.get('os')!='unknown' and p.get('architecture')!='unknown':
        print(f'{d.get(\"mediaType\",\"\")}@{m[\"digest\"]}')
" 2>/dev/null)"
  while IFS= read -r ref; do [ -n "$ref" ] && args+=("$ref"); done <<< "$refs"
  [ ${#args[@]} -gt 0 ] && BUILDX_NO_DEFAULT_ATTESTATIONS=1 docker buildx imagetools create --tag "$tag" "${args[@]}" 2>/dev/null || true
}

push_service() {
  local svc="$1" plat="$2" ver="$3"
  local arch="$(normalize_arch "$plat")"
  local src="$SOURCE_NAMESPACE/cgdm-${svc}:${plat}-${ver}"
  local dst="$(target_prefix "$svc"):${ver}-${arch}"
  ensure_image "$svc" "$plat" "$ver"
  echo "  pushing $src → $dst"

  if BUILDX_NO_DEFAULT_ATTESTATIONS=1 docker buildx build \
       --provenance=false --sbom=false \
       --platform "$(docker_platform "$plat")" \
       --tag "$dst" \
       - <<< "FROM $src" 2>/dev/null; then
    BUILDX_NO_DEFAULT_ATTESTATIONS=1 docker push "$dst"
  else
    docker tag "$src" "$dst"
    BUILDX_NO_DEFAULT_ATTESTATIONS=1 docker push "$dst"
  fi
  clean_unknown_manifest "$dst"
}

push_manifest() {
  local svc="$1" ver="$2"; shift 2
  local platforms=("$@")
  local pre; pre="$(target_prefix "$svc")"
  local tag="${pre}:${ver}" imgs=()
  for p in "${platforms[@]}"; do imgs+=("${pre}:${ver}-$(normalize_arch "$p")"); done
  clean_unknown_manifest "$tag"
  BUILDX_NO_DEFAULT_ATTESTATIONS=1 docker buildx imagetools create --tag "$tag" "${imgs[@]}" 2>/dev/null || {
    docker manifest rm "$tag" >/dev/null 2>&1 || true
    docker manifest create "$tag" "${imgs[@]}"
    for p in "${platforms[@]}"; do docker manifest annotate "$tag" "${pre}:${ver}-$(normalize_arch "$p")" --arch "$(normalize_arch "$p")"; done
    docker manifest push "$tag"
  }
  echo "  manifest: $tag"
}

publish_all() {
  local ver="$1"
  echo "=== Publishing to ${LABEL}: ${REGISTRY}/${NAMESPACE} | v=${ver} platforms=${PLATFORMS[*]} ==="
  for p in "${PLATFORMS[@]}"; do
    for s in "${SERVICES[@]}"; do push_service "$s" "$p" "$ver"; done
  done
  [ ${#PLATFORMS[@]} -gt 1 ] && for s in "${SERVICES[@]}"; do push_manifest "$s" "$ver" "${PLATFORMS[@]}"; done
  echo "✔ ${LABEL} done"
}

usage() {
  cat <<'EOF'
用法: ./publish-global.sh [--platform=PLATFORM]

--platform=PLATFORM  平台: x86_64 | arm64 | 逗号分隔（默认: 自动探测 package/build/）
-h, --help           显示帮助
EOF
}

for arg in "$@"; do
  case "$arg" in
    --platform=*) PLATFORM_SPECIFIED=1; IFS=',' read -r -a a <<< "${arg#--platform=}"; PLATFORMS+=("${a[@]}") ;;
    -h|--help) usage; exit 0 ;;
    *) echo "unknown: $arg"; usage; exit 1 ;;
  esac
done

VERSION="${VERSION:-$(grep '^cg\.clouddm\.main\.version=' "$OPEN_CDM_DIR/gradle.properties" | cut -d'=' -f2 | tr -d '[:space:]')}"
[ -z "$VERSION" ] && { echo "ERROR: no version"; exit 1; }

do_login "$REGISTRY" "$(read_prop "cgdm.docker.${CRED_PREFIX}.username")" "$(read_prop "cgdm.docker.${CRED_PREFIX}.password")" "$LABEL"
resolve_platforms
publish_all "$VERSION"
echo "✔ All done"