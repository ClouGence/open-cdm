#!/usr/bin/env bash
set -euo pipefail

doris_home="/opt/apache-doris"
official_entrypoint="/usr/local/bin/entry_point.sh"
runtime_entrypoint="/tmp/doris-stable-entrypoint.sh"
node_host="${DORIS_NODE_HOST:-$(hostname)}"
fe_heap_size="${DORIS_FE_HEAP_SIZE:-2048m}"

# Doris 3.x refuses to start BE when the Docker host has swap enabled. The
# check is not useful inside this test container and disabling host swap would
# affect every database container on the machine.
export SKIP_CHECK_ULIMIT=true

getent hosts "${node_host}" >/dev/null 2>&1 || {
  printf 'Doris stable hostname cannot be resolved: %s\n' "${node_host}" >&2
  exit 1
}

fe_conf="${doris_home}/fe/conf/fe.conf"
sed -i '/^[[:space:]]*enable_fqdn_mode[[:space:]]*=/d' "${fe_conf}"
printf '\nenable_fqdn_mode = true\n' >>"${fe_conf}"

# The all-in-one image defaults to an 8 GiB FE heap. A smaller deterministic
# heap keeps the regression fixture usable alongside the other DB containers.
sed -i -E \
  "s/-Xmx[0-9]+[mMgG][[:space:]]+-Xms[0-9]+[mMgG]/-Xmx${fe_heap_size} -Xms${fe_heap_size}/g" \
  "${fe_conf}"

# The upstream all-in-one entrypoint registers FE/BE as 127.0.0.1. Replace
# those identities with the stable Compose hostname so persisted BDB metadata
# survives container recreation and Docker IP changes.
sed \
  -e "s/MASTER_FE_IP=\"127\.0\.0\.1\"/MASTER_FE_IP=\"${node_host}\"/" \
  -e "s/CURRENT_BE_IP=\"127\.0\.0\.1\"/CURRENT_BE_IP=\"${node_host}\"/" \
  -e "s/CURRENT_BROKER_IP=\"127\.0\.0\.1\"/CURRENT_BROKER_IP=\"${node_host}\"/" \
  -e '/echo "priority_networks = 127\.0\.0\.1\/24"/d' \
  "${official_entrypoint}" >"${runtime_entrypoint}"
chmod 755 "${runtime_entrypoint}"

exec bash "${runtime_entrypoint}" "$@"
