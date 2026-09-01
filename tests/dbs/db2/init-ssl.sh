#!/usr/bin/env bash
set -euo pipefail

db2_home=/database/config/db2inst1
ssl_dir=${db2_home}/ssl
keydb=${ssl_dir}/server.p12
stash=${ssl_dir}/server.sth
label=open-cdm-server
gsk=/opt/ibm/db2/V11.5/gskit/bin/gsk8capicmd_64
export LD_LIBRARY_PATH=/opt/ibm/db2/V11.5/lib64/gskit_db2${LD_LIBRARY_PATH:+:${LD_LIBRARY_PATH}}

mkdir -p "${ssl_dir}"
openssl pkcs12 -export \
  -in /certs/server.crt \
  -inkey /certs/server.key \
  -certfile /certs/ca.crt \
  -name "${label}" \
  -out "${keydb}" \
  -passout pass:123456
"${gsk}" -keydb -stashpw -db "${keydb}" -pw 123456
chown -R db2inst1:db2iadm1 "${ssl_dir}"
chmod 700 "${ssl_dir}"
chmod 600 "${keydb}" "${stash}"

su - db2inst1 -c "db2 update dbm cfg using SSL_SVR_KEYDB '${keydb}'"
su - db2inst1 -c "db2 update dbm cfg using SSL_SVR_STASH '${stash}'"
su - db2inst1 -c "db2 update dbm cfg using SSL_SVR_LABEL '${label}'"
su - db2inst1 -c "db2 update dbm cfg using SSL_SVCENAME 50001"
su - db2inst1 -c "db2 update dbm cfg using SSL_VERSIONS TLSV12"
su - db2inst1 -c 'db2set DB2COMM=TCPIP,SSL'
su - db2inst1 -c 'db2stop force'
su - db2inst1 -c 'db2start'
