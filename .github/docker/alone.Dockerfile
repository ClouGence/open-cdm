# syntax=docker/dockerfile:1.7
# Multi-arch Dockerfile for the CloudDM Alone (standalone) service.
# Bundles a MySQL 8.0 server from Ubuntu Noble repositories so the image runs
# end-to-end without an external database.
# Build context expected layout:
#   <ctx>/cgdm-alone.tar.gz
#   <ctx>/shared/alone/{alone.properties,init.sh}
FROM eclipse-temurin:21-jre-noble

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates curl fontconfig tzdata \
        mysql-server-core-8.0 mysql-client-core-8.0 \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /docker-entrypoint-init /var/lib/mysql /run/mysqld \
    && chown -R mysql:mysql /var/lib/mysql /run/mysqld

# CloudDM Web Service Port (config: server.port)
ENV APP_WEB_PORT=8222
# CloudDM Web JWT secret (config: jwt.secret)
ENV APP_WEB_JWT=jwt67843ad4s118123ycgve45uk12ghd3vli4u510fd9z35hec2hegre876n1g3sa8s2o
# CloudDM HA Service Name (config: clouddm.rsocket.dns)
ENV APP_SERVE_NAME=127.0.0.1
# Sidecar Service Port (config: clouddm.rsocket.console.port)
ENV APP_SERVE_PORT=8008
# embedded mysql defaults; set MYSQL_EMBEDDED=false to keep using an external database
ENV MYSQL_EMBEDDED=true
ENV MYSQL_ROOT_PASSWORD=123456
ENV MYSQL_DATADIR=/var/lib/mysql
ENV DB_HOST=127.0.0.1
ENV DB_PORT=3306
ENV DB_DATABASE=cdmgr
ENV DB_USERNAME=root

ADD cgdm-alone.tar.gz /root/
COPY shared/alone/alone.properties /docker-entrypoint-init/copy_alone.properties
COPY shared/alone/init.sh /docker-entrypoint-init/init.sh
RUN chmod +x /docker-entrypoint-init/init.sh \
    && rm -rf /root/cgdm/alone/logs \
    && mkdir -p /root/cgdm/alone/logs \
    && rm -f /root/cgdm/alone/conf/alone.properties \
    && mkdir -p /root/default_conf \
    && cp -a /root/cgdm/alone/conf/. /root/default_conf/

ENTRYPOINT ["/docker-entrypoint-init/init.sh"]
WORKDIR /root
