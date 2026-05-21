#! /bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

TARGET="${1:-all}"

usage() {
	cat <<'EOF'
Usage: ./all_build.sh [all|web|help]

Targets:
  all   Full CloudDM build and publish flow. Default.
  web   Build cgdm-web web resources only.
  help  Show this help message.
EOF
}

build_all() {
	echo 'start to build CloudDM(ALL)'
	./gradlew clean
	./gradlew -Pprofile=dev -Ptarget=none -PbuildFrontend=true buildx local -x test --rerun-tasks --parallel --max-workers=16
	./gradlew -PbuildFrontend=true publishToMavenLocal --parallel --max-workers=16
}

build_web() {
	echo 'start to build CloudDM(web only)'
	./gradlew -PbuildFrontend=true :cgdm-web:processResources --parallel --max-workers=16
}

case "$TARGET" in
	all)
		build_all
		;;
	web)
		build_web
		;;
	help|-h|--help)
		usage
		;;
	*)
		echo "unknown build target: $TARGET" >&2
		usage >&2
		exit 1
		;;
esac

# --refresh-dependencies