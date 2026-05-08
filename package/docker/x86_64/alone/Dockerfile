ARG BASE_IMAGE=clougence/cgdm-x86_64-base:local
FROM ${BASE_IMAGE}

# CloudDM Web Service Port (config: server.port)
ENV APP_WEB_PORT=8222
# CloudDM Web JWT secret(config: jwt.secret)
ENV APP_WEB_JWT=jwt67843ad4s118123ycgve45uk12ghd3vli4u510fd9z35hec2hegre876n1+g&3#s-a8s+\$!2o
# CloudDM HA Service Name (config: clouddm.rsocket.dns)
ENV APP_SERVE_NAME=127.0.0.1
# Sidecar Service Port (config: clouddm.rsocket.console.port)
ENV APP_SERVE_PORT=8008
# metadata database; docker-compose should override these for packaged deployment
ENV DB_HOST=
ENV DB_PORT=3306
ENV DB_DATABASE=cdmgr
ENV DB_USERNAME=

# copy data to container
ADD build/cgdm-alone.tar.gz /root/
COPY docker/x86_64/alone/alone.properties /docker-entrypoint-init/copy_alone.properties
COPY docker/x86_64/alone/init.sh /docker-entrypoint-init/init.sh
RUN chmod +x /docker-entrypoint-init/init.sh \
    && rm -rf /root/cgdm/alone/logs \
    && mkdir -p /root/cgdm/alone/logs \
    && rm -f /root/cgdm/alone/conf/alone.properties

ENTRYPOINT ["sh", "/docker-entrypoint-init/init.sh"]
WORKDIR /root
