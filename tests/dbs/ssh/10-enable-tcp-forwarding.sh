#!/usr/bin/env sh
set -eu

SSHD_CONFIG=/config/sshd/sshd_config

if grep -q '^Port ' "$SSHD_CONFIG"; then
    sed -i 's/^Port .*/Port 22/' "$SSHD_CONFIG"
else
    printf '\nPort 22\n' >> "$SSHD_CONFIG"
fi

if grep -q '^AllowTcpForwarding ' "$SSHD_CONFIG"; then
    sed -i 's/^AllowTcpForwarding .*/AllowTcpForwarding yes/' "$SSHD_CONFIG"
else
    printf '\nAllowTcpForwarding yes\n' >> "$SSHD_CONFIG"
fi
