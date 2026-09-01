#!/usr/bin/env bash
set -euo pipefail

runtime_dir=/var/opt/mssql/ssl
mkdir -p "${runtime_dir}"
cp /certs/server.crt "${runtime_dir}/server.crt"
cp /certs/server.key "${runtime_dir}/server.key"
chmod 600 "${runtime_dir}/server.crt" "${runtime_dir}/server.key"

/opt/mssql/bin/mssql-conf set network.tlscert "${runtime_dir}/server.crt"
/opt/mssql/bin/mssql-conf set network.tlskey "${runtime_dir}/server.key"
/opt/mssql/bin/mssql-conf set network.tlsprotocols 1.2
/opt/mssql/bin/mssql-conf set network.forceencryption 0

exec /opt/mssql/bin/sqlservr
