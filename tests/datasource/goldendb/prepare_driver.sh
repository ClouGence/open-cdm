#!/usr/bin/env bash

set -euo pipefail

readonly MYSQL_DRIVER_VERSION="5.1.46.86"
readonly MYSQL_DRIVER_FILE="gdb_mysql-connector-java-${MYSQL_DRIVER_VERSION}.jar"
readonly MYSQL_EXPECTED_SHA256="7866aad6ce083ff231e0a9869e17fe0cae667657b3bc0e359666887d3daf5211"
readonly ORACLE_DRIVER_VERSION="5.1.46.77"
readonly ORACLE_DRIVER_FILE="gdb_mysql-connector-java-oracle-${ORACLE_DRIVER_VERSION}.jar"
readonly ORACLE_EXPECTED_SHA256="d082aa95fd31b2bd5e80584d187c3b3c1c4736443b12a6282d471d4b3006ecc2"

if [[ $# -lt 1 || $# -gt 2 ]]; then
    echo "Usage: $0 <GoldenDB client driver zip> [CloudDM driver root]" >&2
    exit 2
fi

driver_archive=$1
script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
repo_root=$(cd "$script_dir/../../.." && pwd)
workspace_root=$(cd "$repo_root/.." && pwd)
driver_root=${2:-"$workspace_root/data/alone/drivers"}
if [[ ! -f "$driver_archive" ]]; then
    echo "GoldenDB driver archive not found: $driver_archive" >&2
    exit 2
fi

work_dir=$(mktemp -d "${TMPDIR:-/tmp}/goldendb_driver_prepare.XXXXXX")
cleanup() {
    find "$work_dir" -depth -delete
}
trap cleanup EXIT
outer_dir="$work_dir/outer"
client_dir="$work_dir/client"
mysql_dir="$work_dir/mysql"
oracle_dir="$work_dir/oracle"
mkdir -p "$outer_dir" "$client_dir" "$mysql_dir" "$oracle_dir"

unzip -j -q "$driver_archive" '*/versions/ZXCLOUD-GoldenDB-Client-DriverV1.0.02.zip' -d "$outer_dir"
client_archive="$outer_dir/ZXCLOUD-GoldenDB-Client-DriverV1.0.02.zip"
unzip -j -q "$client_archive" '*/ZXCLOUD-GoldenDB-Java-ConnectorV2.1P5.zip' -d "$client_dir"
unzip -j -q "$client_archive" '*/ZXCLOUD-GoldenDB-Java-Oracle-ConnectorV1.3P1.zip' -d "$client_dir"
unzip -j -q "$client_dir/ZXCLOUD-GoldenDB-Java-ConnectorV2.1P5.zip" "*/${MYSQL_DRIVER_FILE}" -d "$mysql_dir"
unzip -j -q "$client_dir/ZXCLOUD-GoldenDB-Java-Oracle-ConnectorV1.3P1.zip" "*/${ORACLE_DRIVER_FILE}" -d "$oracle_dir"

verify_and_install() {
    local mode=$1
    local driver_jar=$2
    local expected_sha256=$3
    local driver_family=$4
    local driver_version=$5
    local driver_file=$6
    local actual_sha256

    actual_sha256=$(shasum -a 256 "$driver_jar" | awk '{print $1}')
    if [[ "$actual_sha256" != "$expected_sha256" ]]; then
        echo "GoldenDB $mode driver SHA-256 mismatch: $actual_sha256" >&2
        exit 1
    fi
    if ! jar tf "$driver_jar" | grep -qx 'com/goldendb/jdbc/Driver.class'; then
        echo "GoldenDB $mode JDBC driver class is missing." >&2
        exit 1
    fi

    local target_dir="$driver_root/$driver_family/$driver_version"
    local target_jar="$target_dir/$driver_file"
    install -d "$target_dir"
    install -m 0644 "$driver_jar" "$target_jar"

    echo "GoldenDB $mode JDBC driver prepared."
    echo "mode=$mode"
    echo "version=$driver_version"
    echo "sha256=$actual_sha256"
    echo "path=$target_jar"
}

verify_and_install "MySQL" "$mysql_dir/$MYSQL_DRIVER_FILE" "$MYSQL_EXPECTED_SHA256" \
    "GoldenDB MySQL JDBC Driver" "$MYSQL_DRIVER_VERSION" "$MYSQL_DRIVER_FILE"
verify_and_install "Oracle" "$oracle_dir/$ORACLE_DRIVER_FILE" "$ORACLE_EXPECTED_SHA256" \
    "GoldenDB Oracle JDBC Driver" "$ORACLE_DRIVER_VERSION" "$ORACLE_DRIVER_FILE"
