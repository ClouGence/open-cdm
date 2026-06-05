#! /bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BACKEND_DIR="$REPO_ROOT/backend"
cd "$BACKEND_DIR"

TARGET="${1:-all}"

usage() {
	cat <<'EOF'
Usage: ./all_build.sh [all|web|plugin <module>|help]

Targets:
  all              Full CloudDM build and publish flow. Default.
  web              Build cgdm-web web resources only.
  plugin <module>  Rebuild one plugin package via :<module>:customFatJar.
                   Example: ./all_build.sh plugin plus-provider-ldap
  help             Show this help message.

Notes:
  Normal distribution/package builds are intentionally full-build only.
  Use the plugin target only for modules that provide customFatJar.
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

build_plugin() {
	local module="${1:-}"
	if [[ -z "$module" ]]; then
		echo "missing plugin module name" >&2
		usage >&2
		exit 1
	fi

	module="${module#:}"
	if [[ "$module" == *:* ]]; then
		echo "plugin module must be a single Gradle module name, e.g. plus-provider-ldap" >&2
		exit 1
	fi

	echo "start to build CloudDM plugin($module)"
	./gradlew ":${module}:customFatJar" --rerun-tasks --max-workers=8
}

case "$TARGET" in
	all)
		build_all
		;;
	web)
		build_web
		;;
	plugin)
		build_plugin "${2:-}"
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
