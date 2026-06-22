FROM ubuntu:24.04

ARG DEBIAN_FRONTEND=noninteractive
ARG APT_MIRROR=""
ARG JDK_URL="https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-aarch64_bin.tar.gz"
ARG JDK_SHA256_URL="${JDK_URL}.sha256"

ENV JAVA_HOME=/opt/openjdk-17
ENV PATH="$JAVA_HOME/bin:$PATH"

RUN if [ -n "$APT_MIRROR" ]; then \
        sed -i \
          -e "s|http://ports.ubuntu.com/ubuntu-ports|$APT_MIRROR|g" \
          -e "s|https://ports.ubuntu.com/ubuntu-ports|$APT_MIRROR|g" \
          /etc/apt/sources.list /etc/apt/sources.list.d/*.sources 2>/dev/null || true; \
    fi \
    && apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl fontconfig tzdata \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /opt /docker-entrypoint-init "$JAVA_HOME" \
    && curl -fsSL "$JDK_URL" -o /tmp/openjdk17.tar.gz \
    && expected_sha256=$(curl -fsSL "$JDK_SHA256_URL" | tr -d '[:space:]') \
    && actual_sha256=$(sha256sum /tmp/openjdk17.tar.gz | awk '{print $1}') \
    && [ "$actual_sha256" = "$expected_sha256" ] \
    && tar -xzf /tmp/openjdk17.tar.gz --strip-components=1 -C "$JAVA_HOME" \
    && rm -f /tmp/openjdk17.tar.gz
