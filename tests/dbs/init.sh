#!/usr/bin/env bash
set -euo pipefail

script_dir="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
compose_file="${script_dir}/docker-compose.yml"
data_dir_input="${DBS_DATA_DIR:-${script_dir}/data}"
if [[ "${data_dir_input}" != /* ]]; then
  data_dir_input="${script_dir}/${data_dir_input}"
fi
data_dir="$(readlink -m -- "${data_dir_input}")"
data_marker="${data_dir}/.open-cdm-dbs-data"
compose_project_name="${COMPOSE_PROJECT_NAME:-$(basename -- "${script_dir}")}"
export DBS_DATA_DIR="${data_dir}"
dm8_image="local/dm8:8-20250506"
dm8_download_url="https://download.dameng.com/eco/dm8/dm8_20250506_x86_rh6_rq_single.tar"
hana1_image="cathy01/hanaexpress1@sha256:118e151468a8d9e4d3a8832716971ec96ee5c1da31ae1cc579daa31982279455"
hana1_license_file="${script_dir}/hana/HXE.txt"
hana2_image="saplabs/hanaexpress:2.00.082.00.20250528.1"
dm8_timeout_seconds=600
hana1_timeout_seconds=600
hana2_timeout_seconds=1200
db2_timeout_seconds=600
hana1_license_pending=0
temp_dir=""

log() {
  printf '[db-init] %s\n' "$*" >&2
}

die() {
  log "错误: $*"
  exit 1
}

cleanup() {
  if [[ -n "${temp_dir}" && -d "${temp_dir}" ]]; then
    rm -rf -- "${temp_dir}"
  fi
}
trap cleanup EXIT

validate_data_dir() {
  case "${data_dir}" in
    /|/tmp|/var|/home|"${script_dir}"|"$(dirname -- "${script_dir}")")
      die "数据目录范围过大或不安全: ${data_dir}"
      ;;
  esac
}

prepare_data_dir() {
  validate_data_dir
  if [[ -e "${data_dir}" && ! -d "${data_dir}" ]]; then
    die "数据目录不是文件夹: ${data_dir}"
  fi
  if [[ -d "${data_dir}" && ! -f "${data_marker}" ]]; then
    existing_entry="$(find "${data_dir}" -mindepth 1 -maxdepth 1 -print -quit)"
    [[ -z "${existing_entry}" ]] || die "数据目录非空且不是本工具创建的目录: ${data_dir}"
  fi
  mkdir -p -- "${data_dir}"
  if [[ ! -f "${data_marker}" ]]; then
    printf 'open-cdm-dbs-data-v1\n' >"${data_marker}"
  fi
}

prepare_storage_dirs() {
  while IFS= read -r relative_dir; do
    [[ -n "${relative_dir}" ]] || continue
    storage_dir="${data_dir}/${relative_dir}"
    if [[ ! -d "${storage_dir}" ]]; then
      mkdir -p -- "${storage_dir}"
      chmod 0777 "${storage_dir}"
    fi
  done <<'EOF'
mysql/5.6
mysql/5.7
mysql/8.0
mysql/8.4
mysql/9.7
tidb/4.0
tidb/5.4
tidb/6.5
tidb/7.5
tidb/8.5
doris/2.1.0/fe-meta
doris/2.1.0/be-storage
doris/2.1.9/fe-meta
doris/2.1.9/be-storage
doris/3.0/fe-meta
doris/3.0/be-storage
dameng/8
postgres/12
postgres/13
postgres/14
postgres/15
postgres/16
postgres/17
postgres/18
hana/1.00.122
hana/2.00.082
sqlserver/2017
sqlserver/2019
sqlserver/2022
oracle/11
oracle/21
oracle/23
starrocks/2.5/fe-meta
starrocks/2.5/be-storage
starrocks/3.5/fe-meta
starrocks/3.5/be-storage
starrocks/4.1/fe-meta
starrocks/4.1/be-storage
db2/9.7
db2/10.5
db2/11.5
mariadb/10.11
mariadb/11.4
mariadb/11.8
clickhouse/20.8
clickhouse/22.8
clickhouse/24.8
clickhouse/26.6
redis/7.2
mongo/6.0
EOF
}

clean_data_dir() {
  validate_data_dir
  [[ -e "${data_dir}" ]] || return 0
  [[ -f "${data_marker}" ]] || die "拒绝清理缺少标记文件的数据目录: ${data_dir}"
  rm -rf -- "${data_dir}"
}

print_usage() {
  cat <<'EOF'
用法：
  ./init.sh                         只显示本帮助，不启动容器
  ./init.sh -all                    启动全部数据库版本和 support 服务
  ./init.sh -simple                 每个数据库家族只启动最新版本
  ./init.sh -ds mysql               启动 MySQL 全部版本
  ./init.sh -ds mysql 8.4           只启动 MySQL 8.4
  ./init.sh -ds mysql 8.4.10        完整镜像版本也会匹配到 MySQL 8.4 profile
  ./init.sh clean                   删除全部容器、网络和本地数据库数据

数据目录：
  默认使用 ./data；可通过 DBS_DATA_DIR=/其它路径 指定其它磁盘目录。

数据源：mysql、tidb、doris、dameng、postgres、hana、sqlserver、oracle、
        starrocks、db2、mariadb、clickhouse

支持的版本（括号内是固定镜像版本）：
  mysql       5.6 (5.6.51), 5.7 (5.7.44), 8.0 (8.0.46), 8.4 (8.4.10), 9.7 (9.7.1)
  tidb        4.0 (4.0.16), 5.4 (5.4.3), 6.5 (6.5.12), 7.5 (7.5.7), 8.5 (8.5.7)
  doris       2.1 (2.1.0), 2.1.9 (2.1.9), 3.0 (3.0.8)
  dameng      8 (8-20250506)
  postgres    12 (12.22), 13 (13.23), 14 (14.23), 15 (15.18), 16 (16.14), 17 (17.10), 18 (18.4)
  hana        1.00.122 (1.00.122.01 SPS12), 2.00.082 (2.00.082.00)
  sqlserver   2017 (14.0.3530.2), 2019 (15.0.4480.2), 2022 (16.0.4265.3)
  oracle      11 (11.2.0.2), 21 (21.3.0), 23 (23.26.2)
  starrocks   2.5 (2.5.21), 3.5 (3.5.20), 4.1 (4.1.3)
  db2         9.7 (9.7.0.0), 10.5 (10.5.0.4), 11.5 (11.5.9.0)
  mariadb     10.11 (10.11.16), 11.4 (11.4.10), 11.8 (11.8.8)
  clickhouse  20.8 (20.8.19.4), 22.8 (22.8.21.38), 24.8 (24.8.14.39), 26.6 (26.6.2.81)

版本参数既可以填写左侧的 profile 版本，也可以填写括号中的完整版本。
EOF
}

if (( $# == 0 )); then
  print_usage
  exit 0
fi

mode=""
requested_ds=""
requested_version=""
case "$1" in
  clean|-clean|--clean)
    (( $# == 1 )) || die "clean 不接受其它参数"
    mode="clean"
    ;;
  -all|--all)
    (( $# == 1 )) || die "-all 不接受其它参数"
    mode="all"
    ;;
  -simple)
    (( $# == 1 )) || die "-simple 不接受其它参数"
    mode="simple"
    ;;
  -ds|--ds)
    (( $# == 2 || $# == 3 )) || die "用法: ./init.sh -ds <datasource> [version]"
    mode="datasource"
    requested_ds="${2#ds-}"
    requested_version="${3:-}"
    ;;
  *)
    print_usage >&2
    die "未知参数: $1"
    ;;
esac

command -v docker >/dev/null 2>&1 || die "未找到 docker 命令"
docker compose version >/dev/null 2>&1 || die "需要 Docker Compose v2"

if [[ "${mode}" == "clean" ]]; then
  validate_data_dir
  if [[ -e "${data_dir}" && ! -f "${data_marker}" ]]; then
    die "拒绝清理缺少标记文件的数据目录: ${data_dir}"
  fi
  log "Clean: 删除 docker-compose.yml 定义的容器、网络和遗留 Docker 卷"
  docker compose -f "${compose_file}" --profile '*' \
    down --volumes --remove-orphans
  mapfile -t legacy_volumes < <(
    docker volume ls \
      --filter "label=com.docker.compose.project=${compose_project_name}" -q
  )
  if (( ${#legacy_volumes[@]} > 0 )); then
    docker volume rm "${legacy_volumes[@]}"
  fi
  log "Clean: 删除本地数据目录 ${data_dir}"
  clean_data_dir
  log "Clean: 清理完成"
  exit 0
fi

prepare_data_dir
prepare_storage_dirs

mapfile -t available_profiles < <(
  docker compose -f "${compose_file}" config --profiles
)

profile_exists() {
  local expected="$1" profile
  for profile in "${available_profiles[@]}"; do
    [[ "${profile}" == "${expected}" ]] && return 0
  done
  return 1
}

selected_profiles=()
case "${mode}" in
  all)
    selected_profiles=("*")
    ;;
  simple)
    selected_profiles=(
      ds-mysql-9.7 ds-tidb-8.5 ds-doris-3.0 ds-dameng-8
      ds-postgres-18 ds-hana-2.00.082 ds-sqlserver-2022 ds-oracle-23
      ds-starrocks-4.1 ds-db2-11.5 ds-mariadb-11.8 ds-clickhouse-26.6
    )
    for profile in "${selected_profiles[@]}"; do
      profile_exists "${profile}" || die "simple 模式引用了不存在的 profile: ${profile}"
    done
    ;;
  datasource)
    datasource_profile="ds-${requested_ds}"
    profile_exists "${datasource_profile}" || die "未知数据源: ${requested_ds}"
    if [[ -z "${requested_version}" ]]; then
      selected_profiles=("${datasource_profile}")
    else
      matches=()
      for profile in "${available_profiles[@]}"; do
        [[ "${profile}" == "${datasource_profile}-"* ]] || continue
        profile_version="${profile#${datasource_profile}-}"
        if [[ "${requested_version}" == "${profile_version}" \
              || "${requested_version}" == "${profile_version}."* \
              || "${requested_version}" == "${profile_version}-"* ]]; then
          matches+=("${profile}")
        fi
      done
      (( ${#matches[@]} > 0 )) || die "${requested_ds} 没有版本 ${requested_version}"
      (( ${#matches[@]} == 1 )) || die "版本 ${requested_version} 不够精确，匹配到: ${matches[*]}"
      selected_profiles=("${matches[0]}")
    fi
    ;;
esac

profile_is_selected() {
  local target="$1" family="$2" profile
  for profile in "${selected_profiles[@]}"; do
    [[ "${profile}" == "*" || "${profile}" == "${target}" \
       || "${profile}" == "${family}" ]] && return 0
  done
  return 1
}

unavailable_services=()

handle_local_archive_image() {
  local profile="$1" service="$2" image="$3"
  profile_is_selected "${profile}" ds-db2 || return 0
  docker image inspect "${image}" >/dev/null 2>&1 && return 0

  if [[ "${mode}" == "datasource" && "${requested_ds}" == "db2" \
        && -n "${requested_version}" ]]; then
    die "${profile} 的 IBM 官方公共容器已退役；请先加载归档镜像并标记为 ${image}"
  fi

  unavailable_services+=("${service}")
  log "警告: 跳过 ${service}；历史镜像 ${image} 尚未加载，其余所选服务继续启动"
}

handle_local_archive_image ds-db2-9.7 ds-db2-97 local/db2:9.7.0.0
handle_local_archive_image ds-db2-10.5 ds-db2-105 local/db2:10.5.0.4

prepare_dm8_image() {
  if docker image inspect "${dm8_image}" >/dev/null 2>&1; then
    log "DM8: 复用本地镜像 ${dm8_image}"
    return
  fi

  command -v curl >/dev/null 2>&1 || die "自动下载 DM8 镜像需要 curl"
  temp_dir="$(mktemp -d "${TMPDIR:-/tmp}/open-cdm-dm8.XXXXXX")"
  image_tar="${temp_dir}/dm8-image.tar"
  log "DM8: 本地无镜像，从达梦官方下载约 606 MiB 介质"
  curl --fail --location --retry 3 --output "${image_tar}" "${dm8_download_url}"

  log "DM8: 加载官方镜像"
  load_output="$(docker load --input "${image_tar}")"
  printf '%s\n' "${load_output}" >&2
  source_image="$(printf '%s\n' "${load_output}" | sed -n 's/^Loaded image: //p' | tail -n 1)"
  if [[ -z "${source_image}" ]]; then
    source_image="$(printf '%s\n' "${load_output}" | sed -n 's/^Loaded image ID: //p' | tail -n 1)"
  fi
  [[ -n "${source_image}" ]] || die "无法识别达梦官方 tar 中的镜像"
  log "DM8: 固定本地标签 ${source_image} -> ${dm8_image}"
  docker tag "${source_image}" "${dm8_image}"
}

start_dm8() {
  prepare_dm8_image
  log "DM8: 通过 docker-compose.yml 启动"
  DM8_IMAGE="${dm8_image}" docker compose -f "${compose_file}" \
    --profile ds-dameng-8 up -d ds-dameng-8

  deadline=$((SECONDS + dm8_timeout_seconds))
  while (( SECONDS < deadline )); do
    if DM8_IMAGE="${dm8_image}" docker compose -f "${compose_file}" exec -T ds-dameng-8 \
        /bin/bash -lc 'printf "select 1;\n" | /opt/dmdbms/bin/disql SYSDBA/"$1"@127.0.0.1:5236 >/dev/null 2>&1' \
        -- "Dameng123!"; then
      log "DM8: 首次初始化完成，可以执行 SQL"
      return
    fi
    log "DM8: 等待首次初始化，已等待 $((dm8_timeout_seconds - (deadline - SECONDS))) 秒"
    sleep 10
  done

  DM8_IMAGE="${dm8_image}" docker compose -f "${compose_file}" logs --tail 100 ds-dameng-8 >&2 || true
  die "DM8 在 ${dm8_timeout_seconds} 秒内未就绪"
}

hana2_ready() {
  HANA2_IMAGE="${hana2_image}" docker compose -f "${compose_file}" exec -T ds-hana-2 \
    /bin/bash -lc 'client=/usr/sap/HXE/HDB90/exe/hdbsql; [[ -x "$client" ]] && "$client" -n 127.0.0.1:39041 -d HXE -u SYSTEM -p "$1" "SELECT 1 FROM DUMMY" >/dev/null 2>&1' \
    -- "Devtester123!" >/dev/null 2>&1
}

clear_hana2_password() {
  HANA2_IMAGE="${hana2_image}" docker compose -f "${compose_file}" exec -T ds-hana-2 \
    rm -f /hana/mounts/password.json >/dev/null 2>&1 || true
}

prepare_hana1_image() {
  if docker image inspect "${hana1_image}" >/dev/null 2>&1; then
    log "HANA 1: 复用本地固定镜像 ${hana1_image}"
  else
    log "HANA 1: 拉取固定 SPS12 社区快照 ${hana1_image}"
    docker pull "${hana1_image}"
  fi
}

hana1_container_id() {
  HANA1_IMAGE="${hana1_image}" docker compose -f "${compose_file}" ps -aq ds-hana-1
}

hana1_hardware_key() {
  HANA1_IMAGE="${hana1_image}" docker compose -f "${compose_file}" exec -T ds-hana-1 \
    /bin/bash --noprofile --norc -c \
    'client=$(find /hana/shared/HXE -type f -name hdbsql | head -n 1); [[ -n "$client" ]] && "$client" -n 127.0.0.1:30013 -u SYSTEM -p "$1" "SELECT HARDWARE_KEY FROM M_LICENSE" 2>/dev/null | sed -n "s/^\"\(.*\)\"$/\1/p" | head -n 1' \
    -- "Hana@1234"
}

hana1_ready() {
  HANA1_IMAGE="${hana1_image}" docker compose -f "${compose_file}" exec -T ds-hana-1 \
    /bin/bash --noprofile --norc -c \
    'client=$(find /hana/shared/HXE -type f -name hdbsql | head -n 1); [[ -n "$client" ]] && "$client" -n 127.0.0.1:30015 -u SYSTEM -p "$1" "SELECT 1 FROM DUMMY" >/dev/null 2>&1' \
    -- "Hana@1234" >/dev/null 2>&1
}

install_hana1_license() {
  container_id="$(hana1_container_id)"
  [[ -n "${container_id}" ]] || die "无法取得 HANA 1 容器 ID"
  log "HANA 1: 安装 ${hana1_license_file}"
  docker cp "${hana1_license_file}" "${container_id}:/tmp/HXE.txt"
  HANA1_IMAGE="${hana1_image}" docker compose -f "${compose_file}" exec -T ds-hana-1 \
    /bin/bash --noprofile --norc -c \
    'client=$(find /hana/shared/HXE -type f -name hdbsql | head -n 1); escaped=$(sed "s/'"'"'/'"'"''"'"'/g" /tmp/HXE.txt); printf "SET SYSTEM LICENSE '\''%s'\'';\n" "$escaped" | "$client" -m -n 127.0.0.1:30013 -u SYSTEM -p "$1"; rm -f /tmp/HXE.txt' \
    -- "Hana@1234"
}

start_hana1() {
  prepare_hana1_image
  if hana1_ready; then
    log "HANA 1: 复用已就绪的 SPS12 实例"
    return
  fi

  log "HANA 1: 通过 docker-compose.yml 启动"
  HANA1_IMAGE="${hana1_image}" docker compose -f "${compose_file}" \
    --profile ds-hana-1.00.122 up -d ds-hana-1

  deadline=$((SECONDS + hana1_timeout_seconds))
  hardware_key=""
  while (( SECONDS < deadline )); do
    if hana1_ready; then
      log "HANA 1: HXE 可以执行 SQL"
      return
    fi
    hardware_key="$(hana1_hardware_key || true)"
    if [[ -n "${hardware_key}" ]]; then
      break
    fi
    log "HANA 1: 等待 SYSTEMDB，已等待 $((hana1_timeout_seconds - (deadline - SECONDS))) 秒"
    sleep 10
  done

  [[ -n "${hardware_key}" ]] || die "HANA 1 在 ${hana1_timeout_seconds} 秒内未启动 SYSTEMDB"
  if [[ ! -f "${hana1_license_file}" ]]; then
    hana1_license_pending=1
    log "HANA 1: 容器已启动，但 SPS12 临时许可证已过期"
    log "HANA 1: hardware key=${hardware_key}；申请 HXE 许可证后保存为 ${hana1_license_file}，再执行 ./init.sh"
    return
  fi

  install_hana1_license
  while (( SECONDS < deadline )); do
    if hana1_ready; then
      log "HANA 1: 许可证已生效，HXE 可以执行 SQL"
      return
    fi
    log "HANA 1: 等待许可证生效和 tenant 启动"
    sleep 10
  done
  die "HANA 1 安装许可证后仍未在 ${hana1_timeout_seconds} 秒内就绪"
}

prepare_hana2_image() {
  log "HANA 2: 按本地测试环境约定接受 SAP HANA Express 许可"
  if docker image inspect "${hana2_image}" >/dev/null 2>&1; then
    log "HANA 2: 复用本地镜像 ${hana2_image}"
  else
    log "HANA 2: 拉取官方镜像 ${hana2_image}"
    docker pull "${hana2_image}"
  fi
}

prepare_hana2_password() {
  password_file="${script_dir}/hana/password.json"
  [[ -f "${password_file}" ]] || die "缺少 HANA 密码文件: ${password_file}"

  hana2_data_dir="${data_dir}/hana/2.00.082"
  mkdir -p -- "${hana2_data_dir}"
  log "HANA 2: 把首次启动密码写入 ${hana2_data_dir}"
  docker run --rm --user root \
    --volume "${hana2_data_dir}:/hana/mounts" \
    --volume "${password_file}:/source/password.json:ro" \
    --entrypoint /bin/bash "${hana2_image}" --noprofile --norc -c \
    'cp /source/password.json /hana/mounts/password.json && chown 12000:79 /hana/mounts /hana/mounts/password.json && chmod 700 /hana/mounts && chmod 600 /hana/mounts/password.json'
}

start_hana2() {
  prepare_hana2_image
  if hana2_ready; then
    clear_hana2_password
    log "HANA 2: 复用已就绪实例，HXE 可以执行 SQL"
    return
  fi

  # HANA consumes this file only during an initial start. Always refreshing it
  # while the instance is not ready also covers a Compose recreate caused by a
  # versioned data-directory change; relying on the old container's Running
  # flag would leave the newly selected empty directory without its password.
  prepare_hana2_password

  log "HANA 2: 通过 docker-compose.yml 启动"
  HANA2_IMAGE="${hana2_image}" docker compose -f "${compose_file}" \
    --profile ds-hana-2.00.082 up -d ds-hana-2

  deadline=$((SECONDS + hana2_timeout_seconds))
  while (( SECONDS < deadline )); do
    if hana2_ready; then
      clear_hana2_password
      log "HANA 2: 首次初始化完成，HXE 可以执行 SQL"
      return
    fi
    container_id="$(HANA2_IMAGE="${hana2_image}" docker compose -f "${compose_file}" ps -aq ds-hana-2)"
    if [[ -n "${container_id}" ]] && [[ "$(docker inspect --format '{{.State.Status}}' "${container_id}")" == "exited" ]]; then
      HANA2_IMAGE="${hana2_image}" docker compose -f "${compose_file}" logs --tail 100 ds-hana-2 >&2 || true
      die "HANA 2 初始化进程已退出；不会通过自动重启掩盖失败"
    fi
    log "HANA 2: 等待首次初始化，已等待 $((hana2_timeout_seconds - (deadline - SECONDS))) 秒"
    sleep 10
  done

  HANA2_IMAGE="${hana2_image}" docker compose -f "${compose_file}" logs --tail 100 ds-hana-2 >&2 || true
  die "HANA 2 Express 在 ${hana2_timeout_seconds} 秒内未就绪"
}

start_selected_services() {
  local service unavailable unavailable_service
  compose_args=(-f "${compose_file}")
  for profile in "${selected_profiles[@]}"; do
    compose_args+=(--profile "${profile}")
  done

  mapfile -t candidate_services < <(
    docker compose "${compose_args[@]}" config --services
  )
  selected_services=()
  for service in "${candidate_services[@]}"; do
    unavailable=0
    for unavailable_service in "${unavailable_services[@]}"; do
      [[ "${service}" != "${unavailable_service}" ]] || unavailable=1
    done
    (( unavailable )) || selected_services+=("${service}")
  done
  (( ${#selected_services[@]} > 0 )) || die "所选范围没有可启动的服务"

  log "Compose: 拉取缺失镜像并启动 profiles=${selected_profiles[*]}"
  DM8_IMAGE="${dm8_image}" HANA1_IMAGE="${hana1_image}" HANA2_IMAGE="${hana2_image}" \
    docker compose "${compose_args[@]}" up -d --pull missing "${selected_services[@]}"
  log "Compose: 已提交 ${#selected_services[@]} 个服务启动"
}

db2_tls_ready() {
  command -v openssl >/dev/null 2>&1 || return 1
  timeout 5 openssl s_client -connect 127.0.0.1:2501 \
    -CAfile "${script_dir}/certs/ca.crt" -verify_return_error </dev/null 2>&1 \
    | grep -q 'Verify return code: 0 (ok)'
}

start_db2_ssl() {
  if db2_tls_ready; then
    log "Db2 11.5: 复用已就绪的 TLS 端口 2501"
    return
  fi

  deadline=$((SECONDS + db2_timeout_seconds))
  while (( SECONDS < deadline )); do
    if docker compose -f "${compose_file}" exec -T ds-db2-115 \
        /bin/bash -lc 'su - db2inst1 -c "db2 connect to TESTDB" >/dev/null 2>&1'; then
      break
    fi
    log "Db2 11.5: 等待数据库就绪，已等待 $((db2_timeout_seconds - (deadline - SECONDS))) 秒"
    sleep 10
  done
  (( SECONDS < deadline )) || die "Db2 11.5 在 ${db2_timeout_seconds} 秒内未就绪"

  log "Db2 11.5: 配置并启动 TLS 端口 2501"
  docker compose -f "${compose_file}" exec -T ds-db2-115 \
    /bin/bash /usr/local/bin/init-ssl.sh

  while (( SECONDS < deadline )); do
    if db2_tls_ready; then
      log "Db2 11.5: TLS 1.2 和 CA 证书校验通过"
      return
    fi
    log "Db2 11.5: 等待 TLS 端口，已等待 $((db2_timeout_seconds - (deadline - SECONDS))) 秒"
    sleep 5
  done
  die "Db2 11.5 TLS 端口 2501 在 ${db2_timeout_seconds} 秒内未就绪"
}

needs_dm8=0
needs_hana1=0
needs_hana2=0
needs_db2_115=0
profile_is_selected ds-dameng-8 ds-dameng && needs_dm8=1
profile_is_selected ds-hana-1.00.122 ds-hana && needs_hana1=1
profile_is_selected ds-hana-2.00.082 ds-hana && needs_hana2=1
profile_is_selected ds-db2-11.5 ds-db2 && needs_db2_115=1

# 特殊镜像只在选择范围包含相应数据源时准备。HANA 2 的首次启动密码
# 必须在 Compose 启动该服务前写入本地数据目录。
(( needs_dm8 == 0 )) || prepare_dm8_image
(( needs_hana1 == 0 )) || prepare_hana1_image
if (( needs_hana2 )); then
  prepare_hana2_image
fi
if (( needs_hana2 )) && ! hana2_ready; then
  prepare_hana2_password
fi
start_selected_services

(( needs_db2_115 == 0 )) || start_db2_ssl
(( needs_dm8 == 0 )) || start_dm8
(( needs_hana1 == 0 )) || start_hana1
(( needs_hana2 == 0 )) || start_hana2
if (( needs_hana1 && hana1_license_pending )); then
  log "所选 Compose 服务已启动；HANA 1 等待 HXE.txt 许可证"
else
  log "所选 Compose 服务已启动；需要专项初始化的服务已完成检查"
fi
