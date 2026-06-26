#!/usr/bin/env bash
set -euo pipefail

SSL_DIR=/certs
RUNTIME_SSL_DIR=/var/lib/postgresql/ssl
mkdir -p "${SSL_DIR}"

if [ ! -f "${SSL_DIR}/ca.crt" ]; then
  openssl req -new -x509 -days 3650 -nodes \
    -subj "/CN=CloudDM Test CA" \
    -keyout "${SSL_DIR}/ca.key" \
    -out "${SSL_DIR}/ca.crt"
fi

if [ ! -f "${SSL_DIR}/server.crt" ]; then
  cat > "${SSL_DIR}/server.cnf" <<'EOF'
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
CN = localhost

[v3_req]
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
DNS.2 = postgres
IP.1 = 127.0.0.1
EOF

  openssl req -new -nodes \
    -keyout "${SSL_DIR}/server.key" \
    -out "${SSL_DIR}/server.csr" \
    -config "${SSL_DIR}/server.cnf"
  openssl x509 -req -days 3650 \
    -in "${SSL_DIR}/server.csr" \
    -CA "${SSL_DIR}/ca.crt" \
    -CAkey "${SSL_DIR}/ca.key" \
    -CAcreateserial \
    -out "${SSL_DIR}/server.crt" \
    -extensions v3_req \
    -extfile "${SSL_DIR}/server.cnf"
fi

if [ ! -f "${SSL_DIR}/client.crt" ]; then
  cat > "${SSL_DIR}/client.cnf" <<'EOF'
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
CN = sslclient

[v3_req]
extendedKeyUsage = clientAuth
EOF

  openssl req -new -nodes \
    -keyout "${SSL_DIR}/client.key" \
    -out "${SSL_DIR}/client.csr" \
    -config "${SSL_DIR}/client.cnf"
  openssl x509 -req -days 3650 \
    -in "${SSL_DIR}/client.csr" \
    -CA "${SSL_DIR}/ca.crt" \
    -CAkey "${SSL_DIR}/ca.key" \
    -CAcreateserial \
    -out "${SSL_DIR}/client.crt" \
    -extensions v3_req \
    -extfile "${SSL_DIR}/client.cnf"
fi

if [ ! -f "${SSL_DIR}/client.pk8" ]; then
  openssl pkcs8 -topk8 -inform PEM -outform DER \
    -in "${SSL_DIR}/client.key" \
    -out "${SSL_DIR}/client.pk8" \
    -nocrypt
fi

mkdir -p "${RUNTIME_SSL_DIR}"
cp "${SSL_DIR}/ca.crt" "${RUNTIME_SSL_DIR}/ca.crt"
cp "${SSL_DIR}/server.crt" "${RUNTIME_SSL_DIR}/server.crt"
cp "${SSL_DIR}/server.key" "${RUNTIME_SSL_DIR}/server.key"

cat > "${RUNTIME_SSL_DIR}/pg_hba.conf" <<'EOF'
local all all trust
hostssl all sslclient 0.0.0.0/0 cert clientcert=verify-full
hostssl all sslclient ::/0 cert clientcert=verify-full
hostssl all all 0.0.0.0/0 scram-sha-256
hostssl all all ::/0 scram-sha-256
hostnossl all all 0.0.0.0/0 scram-sha-256
hostnossl all all ::/0 scram-sha-256
EOF

chown -R postgres:postgres "${RUNTIME_SSL_DIR}"
chmod 700 "${RUNTIME_SSL_DIR}"
chmod 600 "${RUNTIME_SSL_DIR}/server.key"
chmod 644 "${RUNTIME_SSL_DIR}/ca.crt" "${RUNTIME_SSL_DIR}/server.crt" "${RUNTIME_SSL_DIR}/pg_hba.conf"

exec docker-entrypoint.sh postgres \
  -c ssl=on \
  -c ssl_ca_file="${RUNTIME_SSL_DIR}/ca.crt" \
  -c ssl_cert_file="${RUNTIME_SSL_DIR}/server.crt" \
  -c ssl_key_file="${RUNTIME_SSL_DIR}/server.key" \
  -c hba_file="${RUNTIME_SSL_DIR}/pg_hba.conf"
