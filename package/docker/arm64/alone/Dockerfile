ARG BASE_IMAGE=clougence/cgdm-arm64-base:local
FROM ${BASE_IMAGE}

ARG MYSQL_VERSION=8.0.41
ARG MYSQL_PLATFORM=linux-glibc2.17-aarch64-minimal
ARG MYSQL_DOWNLOAD_URL=https://downloads.mysql.com/archives/get/p/23/file/mysql-${MYSQL_VERSION}-${MYSQL_PLATFORM}.tar.xz

ENV PATH="/usr/local/mysql/bin:$PATH"

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl xz-utils libaio1t64 libncurses6 libnuma1 \
    && groupadd -r mysql \
    && useradd -r -g mysql -s /usr/sbin/nologin mysql \
    && mkdir -p /usr/local/mysql \
    && libaio_path="$(find /usr/lib -name libaio.so.1t64 -print -quit)" \
    && if [ -n "$libaio_path" ]; then \
        libaio_dir="$(dirname "$libaio_path")"; \
        [ -e "$libaio_dir/libaio.so.1" ] || ln -s libaio.so.1t64 "$libaio_dir/libaio.so.1"; \
    fi \
    && curl -fsSL "$MYSQL_DOWNLOAD_URL" -o /tmp/mysql.tar.xz \
    && tar -xJf /tmp/mysql.tar.xz --strip-components=1 -C /usr/local/mysql \
    && mysqld --version | grep -Fq "Ver ${MYSQL_VERSION}" \
    && rm -rf /var/lib/apt/lists/* \
    && rm -f /tmp/mysql.tar.xz \
    && mkdir -p /var/lib/mysql /run/mysqld \
    && chown -R mysql:mysql /usr/local/mysql /var/lib/mysql /run/mysqld

# CloudDM Web Service Port (config: server.port)
ENV APP_WEB_PORT=8222
# CloudDM Web JWT secret(config: jwt.secret)
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

# copy data to container
ADD build/cgdm-alone.tar.gz /root/
COPY docker/arm64/alone/alone.properties /docker-entrypoint-init/copy_alone.properties
COPY docker/arm64/alone/init.sh /docker-entrypoint-init/init.sh
RUN chmod +x /docker-entrypoint-init/init.sh \
    && rm -rf /root/cgdm/alone/logs \
    && mkdir -p /root/cgdm/alone/logs \
    && rm -f /root/cgdm/alone/conf/alone.properties \
    && mkdir -p /root/default_conf \
    && cp -a /root/cgdm/alone/conf/. /root/default_conf/

ENTRYPOINT ["/docker-entrypoint-init/init.sh"]
WORKDIR /root
