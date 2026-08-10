#!/usr/bin/env bash
set -euo pipefail

runtime_dir=/run/open-cdm-mongo-ssl
mkdir -p "${runtime_dir}"
cat /certs/server.key /certs/server.crt > "${runtime_dir}/server.pem"
chown mongodb:mongodb "${runtime_dir}/server.pem"
chmod 600 "${runtime_dir}/server.pem"

exec docker-entrypoint.sh mongod \
  --bind_ip_all \
  --tlsMode preferTLS \
  --tlsCertificateKeyFile "${runtime_dir}/server.pem" \
  --tlsCAFile /certs/ca.crt \
  --tlsAllowConnectionsWithoutCertificates
