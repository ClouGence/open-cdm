#!/bin/bash
# ============================================================================
# build-docker-yml.sh — 根据 package/docker 模板生成 China / Global 部署 yml
#
# 用法:
#   ./build-docker-yml.sh                           # 自动探测所有已构建平台
#   ./build-docker-yml.sh --platform=x86_64         # 仅生成 x86_64
#   ./build-docker-yml.sh --platform=x86_64,arm64   # 生成双平台
#   ./build-docker-yml.sh --target=china            # 仅 China 目标
#   ./build-docker-yml.sh --target=global           # 仅 Global 目标
#
# 前置: 运行 package/package.sh --docker 完成编译 (用于获取 VERSION)
# ============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PACKAGE_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
OPEN_CDM_DIR="$(cd "$PACKAGE_DIR/.." && pwd)"
TEMPLATE_DIR="$SCRIPT_DIR"
BUILD_DIR="$PACKAGE_DIR/build"

# ---- target definitions ----
# China: Aliyun Container Registry
CHINA_REGISTRY="cloudcanal-registry.cn-shanghai.cr.aliyuncs.com"
CHINA_NAMESPACE="clougence"

# Global: Docker Hub
GLOBAL_REGISTRY="docker.io"
GLOBAL_NAMESPACE="bladepipe"

PLATFORMS=()
TARGETS=()
PLATFORM_SPECIFIED=0

log_info()  { echo "[yml-gen] $*"; }
log_ok()    { echo "[yml-gen] ✔ $*"; }

# ---- helpers ----
image_tag()        { echo "${1}-${2}"; }
normalize_arch()   { case "$1" in arm64) echo "arm64" ;; x86_64) echo "amd64" ;; esac }

png_tar() { echo "$PACKAGE_DIR/build/docker-${1}-${2}-${3}.tar"; }

has_platform_built() {
  local plat="$1" ver="$2"
  local svc
  for svc in console sidecar alone; do
    local t; t="$(png_tar "$svc" "$plat" "$ver")"
    [ -f "$t" ] && [ -s "$t" ] || return 1
  done
  return 0
}

# ---- yml generation ----
generate_yml() {
  local target="$1" registry="$2" namespace="$3" plat="$4" ver="$5"
  local arch; arch="$(normalize_arch "$plat")"
  local full_image="${registry}/${namespace}"
  local build_tag; build_tag="$(image_tag "$plat" "$ver")"

  mkdir -p "$BUILD_DIR"

  # — compose files —
  for name in alone cluster; do
    local src="$TEMPLATE_DIR/docker-${name}.yml"
    local dst="$BUILD_DIR/docker-${name}-${build_tag}-${target}.yml"
    if [ -f "$src" ]; then
      sed "s|clougence/cgdm-|${full_image}/cgdm-|g" "$src" \
        | sed "s|:\${build_version}|:${ver}-${arch}|g" \
        > "$dst"
      log_ok "compose: $(basename "$dst")"
    else
      echo "  WARNING: template not found: $src" >&2
    fi
  done

  # — k8s files —
  for name in alone cluster; do
    local src="$TEMPLATE_DIR/k8s-${name}.yml"
    local dst="$BUILD_DIR/k8s-${name}-${build_tag}-${target}.yml"
    if [ -f "$src" ]; then
      sed "s|clougence/cgdm-|${full_image}/cgdm-|g" "$src" \
        | sed "s|:\${build_version}|:${ver}-${arch}|g" \
        > "$dst"
      log_ok "k8s:    $(basename "$dst")"
    else
      echo "  WARNING: template not found: $src" >&2
    fi
  done
}

# ---- main ----
usage() {
  cat <<'EOF'
用法: ./build-docker-yml.sh [--platform=PLATFORM] [--target=TARGET]

--platform=PLATFORM  x86_64 | arm64 | 逗号分隔 (默认: 自动探测)
--target=TARGET      china | global | china,global (默认: 全部)
-h, --help           显示帮助
EOF
}

for arg in "$@"; do
  case "$arg" in
    --platform=*) PLATFORM_SPECIFIED=1; IFS=',' read -r -a a <<< "${arg#--platform=}"; PLATFORMS+=("${a[@]}") ;;
    --target=*)   IFS=',' read -r -a a <<< "${arg#--target=}"; TARGETS+=("${a[@]}") ;;
    -h|--help) usage; exit 0 ;;
    *) echo "unknown: $arg"; usage; exit 1 ;;
  esac
done

# default targets
[ ${#TARGETS[@]} -eq 0 ] && TARGETS=(china global)

VERSION="${VERSION:-$(grep '^cg\.clouddm\.main\.version=' "$OPEN_CDM_DIR/gradle.properties" | cut -d'=' -f2 | tr -d '[:space:]')}"
[ -z "$VERSION" ] && { echo "ERROR: no version"; exit 1; }

# resolve platforms
if [ "$PLATFORM_SPECIFIED" -eq 1 ]; then
  for plat in "${PLATFORMS[@]}"; do
    if ! has_platform_built "$plat" "$VERSION"; then
      echo "ERROR: platform $plat not fully built (missing tars in $BUILD_DIR)" >&2
      exit 1
    fi
  done
else
  PLATFORMS=()
  for plat in x86_64 arm64; do
    has_platform_built "$plat" "$VERSION" && PLATFORMS+=("$plat")
  done
  [ ${#PLATFORMS[@]} -eq 0 ] && { echo "ERROR: no platform built. Run package/package.sh --docker first." >&2; exit 1; }
  log_info "Detected platforms: ${PLATFORMS[*]}"
fi

# generate
for target in "${TARGETS[@]}"; do
  case "$target" in
    china)
      echo "=== Generating China yml: ${CHINA_REGISTRY}/${CHINA_NAMESPACE} ==="
      for plat in "${PLATFORMS[@]}"; do
        generate_yml "$target" "$CHINA_REGISTRY" "$CHINA_NAMESPACE" "$plat" "$VERSION"
      done
      ;;
    global)
      echo "=== Generating Global yml: ${GLOBAL_REGISTRY}/${GLOBAL_NAMESPACE} ==="
      for plat in "${PLATFORMS[@]}"; do
        generate_yml "$target" "$GLOBAL_REGISTRY" "$GLOBAL_NAMESPACE" "$plat" "$VERSION"
      done
      ;;
    *) echo "unknown target: $target"; usage; exit 1 ;;
  esac
done

echo "✔ All done"