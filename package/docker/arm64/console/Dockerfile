ARG BASE_IMAGE=clougence/cgdm-arm64-base:local
FROM ${BASE_IMAGE}

# CloudDM Web Service Port (config: server.port)
ENV APP_WEB_PORT=8222
# CloudDM Web JWT secret(config: jwt.secret)
ENV APP_WEB_JWT=jwt67843ad4s118123ycgve45uk12ghd3vli4u510fd9z35hec2hegre876n1+g&3#s-a8s+\$!2o
# CloudDM HA Service Name (config: clouddm.rsocket.dns)
ENV APP_SERVE_NAME=127.0.0.1
# Sidecar Service Port (config: clouddm.rsocket.console.port)
ENV APP_SERVE_PORT=8008
# metadata database
ENV DB_HOST=dm_mysql
ENV DB_PORT=3306
ENV DB_DATABASE=clouddm
ENV DB_USERNAME=root
ENV DB_PASSWORD=123456

# copy data to container
ADD build/cgdm-console.tar.gz /root/
COPY docker/arm64/console/console.properties /docker-entrypoint-init/copy_console.properties
COPY docker/arm64/console/init.sh /docker-entrypoint-init/init.sh
RUN chmod +x /docker-entrypoint-init/init.sh \
    && rm -rf /root/cgdm/console/logs \
    && mkdir -p /root/cgdm/console/logs \
    && rm -f /root/cgdm/console/conf/console.properties

ENTRYPOINT ["sh", "/docker-entrypoint-init/init.sh"]
WORKDIR /root
