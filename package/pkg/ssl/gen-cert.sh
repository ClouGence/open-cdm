#!/bin/bash
# Generates a self-signed RSA-4096 certificate for *.cdmgr.com.
# Overwrites server.pem (private key) and server.crt (certificate) in the same directory.

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
KEY_FILE="$SCRIPT_DIR/server.pem"
CERT_FILE="$SCRIPT_DIR/server.crt"

DAYS=1095   # ~3 years
CN="*.cdmgr.com"

openssl req -x509 \
  -newkey rsa:4096 \
  -keyout "$KEY_FILE" \
  -out    "$CERT_FILE" \
  -days   "$DAYS" \
  -nodes \
  -subj   "/CN=$CN"

echo "generated:"
echo "  key : $KEY_FILE"
echo "  cert: $CERT_FILE"
openssl x509 -in "$CERT_FILE" -noout -subject -dates
