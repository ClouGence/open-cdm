ARG BASE_IMAGE=clougence/cgdm-x86_64-base:local
FROM ${BASE_IMAGE}

# CloudDM Sidecar Service Port (config: server.port)
ENV APP_WEB_PORT=8080
# Sidecar AK/SK/WSN (global_conf.properties)
ENV DM_CLIENT_AK=%APP_CLIENT_AK%
ENV DM_CLIENT_SK=%APP_CLIENT_SK%
ENV DM_CLIENT_WSN=%APP_CLIENT_WSN%
# CloudDM HA Service Name (config: clouddm.console.host)
ENV APP_SERVE_NAME=%APP_SERVE_NAME%
# Sidecar Service Port (config: clouddm.console.port)
ENV APP_SERVE_PORT=%APP_SERVE_PORT%

# copy data to container
ADD build/cgdm-sidecar.tar.gz /root/
COPY docker/x86_64/sidecar/sidecar.properties /docker-entrypoint-init/copy_sidecar.properties
COPY docker/x86_64/sidecar/global_conf.properties /docker-entrypoint-init/copy_global_conf.properties
COPY docker/x86_64/sidecar/init.sh /docker-entrypoint-init/init.sh
COPY docker/x86_64/sidecar/checker.sh /root/cgdm/sidecar/bin/checker.sh
RUN chmod +x /docker-entrypoint-init/init.sh /root/cgdm/sidecar/bin/checker.sh \
    && rm -rf /root/cgdm/sidecar/logs \
    && mkdir -p /root/cgdm/sidecar/logs \
    && rm -f /root/cgdm/sidecar/conf/sidecar.properties \
    && rm -f /root/cgdm/sidecar/conf/global_conf.properties \
    && mkdir -p /root/default_conf \
    && cp -a /root/cgdm/sidecar/conf/. /root/default_conf/

ENTRYPOINT ["/docker-entrypoint-init/init.sh"]
WORKDIR /root
