FROM ubuntu:24.04

ARG DEBIAN_FRONTEND=noninteractive
ARG APT_MIRROR=""
ARG JDK_URL="https://download.java.net/java/GA/jdk26.0.1/458fda22e4c54d5ba572ab8d2b22eb83/8/GPL/openjdk-26.0.1_linux-x64_bin.tar.gz"
ARG JDK_SHA256_URL="${JDK_URL}.sha256"

ENV JAVA_HOME=/opt/openjdk-26
ENV PATH="$JAVA_HOME/bin:$PATH"

RUN if [ -n "$APT_MIRROR" ]; then \
        sed -i \
          -e "s|http://archive.ubuntu.com/ubuntu|$APT_MIRROR|g" \
          -e "s|https://archive.ubuntu.com/ubuntu|$APT_MIRROR|g" \
          -e "s|http://security.ubuntu.com/ubuntu|$APT_MIRROR|g" \
          -e "s|https://security.ubuntu.com/ubuntu|$APT_MIRROR|g" \
          /etc/apt/sources.list /etc/apt/sources.list.d/*.sources 2>/dev/null || true; \
    fi \
    && apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl fontconfig tzdata \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /opt /docker-entrypoint-init "$JAVA_HOME" \
    && curl -fsSL "$JDK_URL" -o /tmp/openjdk26.tar.gz \
    && expected_sha256=$(curl -fsSL "$JDK_SHA256_URL" | tr -d '[:space:]') \
    && actual_sha256=$(sha256sum /tmp/openjdk26.tar.gz | awk '{print $1}') \
    && [ "$actual_sha256" = "$expected_sha256" ] \
    && tar -xzf /tmp/openjdk26.tar.gz --strip-components=1 -C "$JAVA_HOME" \
    && rm -f /tmp/openjdk26.tar.gz
