ARG BASE_IMAGE=clougence/cgdm-x86_64-base:local
FROM ${BASE_IMAGE}

ARG MYSQL_MAJOR=8.0

# CloudDM Web Service Port (config: server.port)
ENV APP_WEB_PORT=8222
# CloudDM Web JWT secret(config: jwt.secret)
ENV APP_WEB_JWT=jwt67843ad4s118123ycgve45uk12ghd3vli4u510fd9z35hec2hegre876n1+g&3#s-a8s+\$!2o
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

# copy data to container
ADD build/cgdm-alone.tar.gz /root/
COPY docker/x86_64/alone/alone.properties /docker-entrypoint-init/copy_alone.properties
COPY docker/x86_64/alone/init.sh /docker-entrypoint-init/init.sh
RUN chmod +x /docker-entrypoint-init/init.sh \
    && apt-get update \
    && apt-get install -y --no-install-recommends mysql-server \
    && mysqld --version | grep -Fq "Ver ${MYSQL_MAJOR}." \
    && rm -rf /var/lib/apt/lists/* \
    && find /var/lib/mysql -mindepth 1 -maxdepth 1 -exec rm -rf {} + \
    && rm -rf /root/cgdm/alone/logs \
    && mkdir -p /root/cgdm/alone/logs \
    && mkdir -p /var/lib/mysql /run/mysqld \
    && chown -R mysql:mysql /var/lib/mysql /run/mysqld \
    && rm -f /root/cgdm/alone/conf/alone.properties

ENTRYPOINT ["/docker-entrypoint-init/init.sh"]
WORKDIR /root
