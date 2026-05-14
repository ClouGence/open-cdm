# CloudDM Deployment Guide

This document combines three types of CloudDM deployment guidance into a single guide: online image deployment, China registry acceleration, and local deployment after packaging from source.

CloudDM supports two runtime modes: **Standalone (Alone)** and **Cluster (Console + Sidecar)**. Supported deployment methods include **install packages**, **Docker**, and **Kubernetes**.

> If you build from the source repository locally, the install packages, Docker Compose files, and Kubernetes yml files described in this document will be generated automatically under `open-cdm/package/build` after running `open-cdm/package/package.sh --build --docker`. No manual authoring is required.

---

## 1. Version and Deployment Overview

Current repository version: **`3.0.7`**

| Dimension | Supported Content |
|-----------|-------------------|
| Runtime modes | Alone, Console + Sidecar |
| Deployment methods | Install packages, Docker, Kubernetes |
| Online image registries | Global `docker.io/bladepipe`<br/>China `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence` |
| Local packaging output directory | `open-cdm/package/build` |

### 1.1 Global Images

Global images are hosted on Docker Hub and are intended for regions outside mainland China:

| Component | Image |
|-----------|-------|
| Alone | `bladepipe/cgdm-alone:3.0.7` |
| Console | `bladepipe/cgdm-console:3.0.7` |
| Sidecar | `bladepipe/cgdm-sidecar:3.0.7` |

### 1.2 China Images

China images are hosted on Alibaba Cloud Container Registry and are intended for mainland China network environments:

| Component | x86_64 / amd64 | arm64 |
|-----------|----------------|-------|
| Alone | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.0.7-amd64` | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.0.7-arm64` |
| Console | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:3.0.7-amd64` | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:3.0.7-arm64` |
| Sidecar | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:3.0.7-amd64` | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:3.0.7-arm64` |

---

## 2. Local Packaging and Deployment Artifacts

If you plan to deploy from the source repository locally, it is recommended to package first under `open-cdm/package`.

### 2.1 Generate Install Packages Only

```bash
cd open-cdm/package
./package.sh --build
```

This generates:

- `cgdm-alone.tar.gz`
- `cgdm-console.tar.gz`
- `cgdm-sidecar.tar.gz`

### 2.2 Generate Install Packages, Docker Images, and Deployment Manifests

```bash
cd open-cdm/package

# All architectures
./package.sh --build --docker

# x86_64 only
./package.sh --build --docker x86_64

# arm64 only
./package.sh --build --docker arm64
```

After execution, `open-cdm/package/build` will automatically contain:

- Install packages: `cgdm-*.tar.gz`
- Offline images: `docker-*.tar`
- Docker Compose files: `docker-alone-*.yml`, `docker-cluster-*.yml`
- Kubernetes manifests: `k8s-alone-*.yml`, `k8s-cluster-*.yml`

Typical artifacts:

```text
cgdm-alone.tar.gz
cgdm-console.tar.gz
cgdm-sidecar.tar.gz
docker-alone-x86_64-3.0.7.tar
docker-console-x86_64-3.0.7.tar
docker-sidecar-x86_64-3.0.7.tar
docker-alone-x86_64-3.0.7.yml
docker-cluster-x86_64-3.0.7.yml
k8s-alone-x86_64-3.0.7.yml
k8s-cluster-x86_64-3.0.7.yml
```

---

## 3. Standalone (Alone) Deployment

Standalone mode runs the web console, Sidecar, and metadata database together in a single container or a single install package. It is suitable for personal evaluation, small-team trials, and local integration testing.

### 3.1 Install Package Deployment

#### Use the locally built install package

```bash
tar -xzf cgdm-alone.tar.gz
cd cgdm-alone
bin/startup.sh
```

After the first startup, open the browser and visit:

```text
http://localhost:8222
```

The system automatically enters the initialization wizard. After completing database initialization and creating the administrator account, the system is ready to use.

### 3.2 Docker Deployment

#### Global registry

```bash
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:3.0.7
```

#### China registry acceleration

```bash
docker run -d --name cgdm-alone -p 8222:8222 \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.0.7
```

#### Persistent data volumes

Using Docker volumes:

```bash
docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  bladepipe/cgdm-alone:3.0.7
```

Mounting host directories:

```bash
mkdir -p /data/cgdm/{conf,logs,data,mysql}

docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v /data/cgdm/conf:/root/cgdm/alone/conf \
  -v /data/cgdm/logs:/root/cgdm/alone/logs \
  -v /data/cgdm/data:/root/cgdm/alone/data \
  -v /data/cgdm/mysql:/var/lib/mysql \
  bladepipe/cgdm-alone:3.0.7
```

#### Compose deployment after local packaging

```bash
cd open-cdm/package/build
docker load -i docker-alone-x86_64-3.0.7.tar
docker compose -f docker-alone-x86_64-3.0.7.yml up -d
```

If the deployment machine and packaging machine are the same and the image has not been cleaned up, you can also run `docker compose` directly.

#### Common directories and initialization notes

| Path | Purpose |
|------|---------|
| `/root/cgdm/alone/conf` | Configuration files (`alone.properties`) |
| `/root/cgdm/alone/logs` | Runtime logs |
| `/root/cgdm/alone/data` | Runtime data |
| `/var/lib/mysql` | Embedded MySQL data directory |

The `MYSQL_EMBEDDED`, `MYSQL_ROOT_PASSWORD`, and `DB_*` environment variables are used as initialization parameters and default database connection parameters. On the first visit to the web page, you can review and modify the database connection information in the wizard. After confirmation, the system automatically creates the schema and the administrator account.

### 3.3 Kubernetes Deployment

#### Use the automatically generated yml files from local packaging

```bash
cd open-cdm/package/build

# x86_64
kubectl apply -f k8s-alone-x86_64-3.0.7.yml

# arm64
kubectl apply -f k8s-alone-arm64-3.0.7.yml
```

By default, the generated manifests create:

- `cgdm` namespace
- MySQL Service and StatefulSet
- Alone PVC, Service, and Deployment

> The default Service type is `ClusterIP`. If external access is required, adjust it to `NodePort`, `LoadBalancer`, or Ingress according to your environment.

---

## 4. Cluster (Console + Sidecar) Deployment

Cluster mode consists of two independent components, **Console** and **Sidecar**, and is suitable for team collaboration, large-scale data source management, and multi-node access.

### 4.1 Install Package Deployment

#### Install Console

```bash
tar -xzf cgdm-console.tar.gz
cd cgdm-console
bin/startup.sh
```

#### Configure and install Sidecar

```bash
tar -xzf cgdm-sidecar.tar.gz
cd cgdm-sidecar
bin/startup.sh
```

The recommended deployment order is:

1. Start Console first
2. Sign in to Console and complete initialization
3. Add a Sidecar machine in Console and generate `AK / SK / WSN`
4. Configure the generated parameters for Sidecar, then start or restart Sidecar

### 4.2 Docker Deployment

#### Online image deployment

Docker Compose is recommended for cluster mode. Example with global images:

```yaml
services:
  dm_mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: cdmgr
      MYSQL_ROOT_PASSWORD: Replace with your password

  dm_console:
    image: bladepipe/cgdm-console:3.0.7
    ports:
      - "8222:8222"
    environment:
      APP_WEB_PORT: 8222
      APP_WEB_JWT: "Replace this with a random string"
      APP_SERVE_NAME: dm_console
      APP_SERVE_PORT: 8008
      DB_HOST: dm_mysql
      DB_PORT: 3306
      DB_DATABASE: cdmgr
      DB_USERNAME: root
      DB_PASSWORD: Replace with your password

  dm_sidecar:
    image: bladepipe/cgdm-sidecar:3.0.7
    environment:
      APP_WEB_PORT: 8080
      DM_CLIENT_AK: "Obtain after creating a Sidecar in Console"
      DM_CLIENT_SK: "Obtain after creating a Sidecar in Console"
      DM_CLIENT_WSN: "Obtain after creating a Sidecar in Console"
      APP_SERVE_NAME: dm_console
      APP_SERVE_PORT: 8008
```

For China deployment, simply replace the images with:

- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:3.0.7-amd64`
- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:3.0.7-amd64`

#### Compose deployment after local packaging

```bash
cd open-cdm/package/build
docker load -i docker-console-x86_64-3.0.7.tar
docker load -i docker-sidecar-x86_64-3.0.7.tar
docker compose -f docker-cluster-x86_64-3.0.7.yml up -d
```

By default this starts:

- `dm_mysql`
- `dm_console`
- `dm_sidecar`

> The generated `docker-cluster-*.yml` already include the base ports, volumes, and environment variables. However, `DM_CLIENT_AK / SK / WSN` still need to be replaced with the actual values obtained from Console. In production, you should also replace the default passwords and JWT values.

### 4.3 Kubernetes Deployment

#### Use the automatically generated yml files from local packaging

```bash
cd open-cdm/package/build

# x86_64
kubectl apply -f k8s-cluster-x86_64-3.0.7.yml

# arm64
kubectl apply -f k8s-cluster-arm64-3.0.7.yml
```

By default, the generated manifests create:

- `cgdm` namespace
- MySQL Service and StatefulSet
- Console PVC, Service, and Deployment
- Sidecar PVC, Service, and Deployment

By default, the Console web service is exposed as `ClusterIP` on port `8222`. If external access is required, adjust the Service or Ingress configuration according to your environment.

---

## 5. Access and Initialization

Whether you use Alone or Cluster mode, the default web console address is:

```text
http://localhost:8222
```

On the first visit, the initialization wizard appears. After database initialization and administrator account creation are completed, the system enters the full application.

---

## 6. Deployment Recommendations

- For local evaluation or quick verification, prefer **Alone + Docker**
- For team usage and multi-node access, prefer **Cluster + Docker Compose / Kubernetes**
- For staging or production, prefer the Compose and Kubernetes manifests generated after local packaging, and then adjust the image registry, storage, passwords, and Service exposure method according to your environment
- For mainland China environments, prefer Alibaba Cloud image addresses to reduce pull failures and timeouts

---

## 7. Image Publishing and Channel-Specific Deployment File Generation

In addition to runtime deployment, the repository currently provides a set of scripts for channel-specific yml generation and image publishing. These scripts depend on install packages and offline images that have already been built under `open-cdm/package/build` and do not compile source code themselves.

### 7.1 Current Script Locations

| Task | Entry Script | Description |
|------|--------------|-------------|
| Generate China / Global Compose and Kubernetes yml | `open-cdm/package/docker/build-docker-yml.sh` | Reads templates from the current directory and outputs to `open-cdm/package/build` |
| Publish China images | `open-cdm/package/publish-china.sh` | Reads offline image tar files from `open-cdm/package/build` |
| Publish Global images | `open-cdm/package/publish-global.sh` | Reads offline image tar files from `open-cdm/package/build` |

If you only need to deploy locally packaged artifacts, you can directly use the auto-generated `docker-*.yml` and `k8s-*.yml` files under `open-cdm/package/build`. If you need channel-specific manifests with full China or Global image prefixes, use the scripts described here.

### 7.2 Environment Preparation and Credentials

Prerequisites:

- Docker available
- JDK 21+
- Node.js
- Git

You can first verify that the Docker daemon is working normally:

```bash
docker info
```

The publish scripts read registry credentials from `~/.gradle/gradle.properties`. The current scripts directly read username and password. The China and Global registry and namespace values are based on built-in script defaults. The Global namespace can be overridden through the `DOCKER_NAMESPACE` environment variable.

```properties
# China Registry (Alibaba Cloud Container Registry)
cgdm.docker.china.username=your_aliyun_username
cgdm.docker.china.password=your_aliyun_fixed_password

# Global Registry (Docker Hub)
cgdm.docker.global.username=your_dockerhub_username
cgdm.docker.global.password=your_dockerhub_token
```

The current built-in defaults are:

- China registry: `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com`
- China namespace: `clougence`
- Global registry: `docker.io`
- Global namespace: `bladepipe`

### 7.3 Recommended Workflow

The recommended order consists of three steps:

1. Run `./package.sh --build --docker` under `open-cdm/package` to generate install packages, offline images, and base manifests.
2. If you need China / Global channel-specific yml files, run `build-docker-yml.sh`.
3. If you need to push images to a remote registry, run `publish-china.sh` or `publish-global.sh`.

#### Generate channel-specific yml

Entry point:

```bash
cd /worker_space/dm/open-cdm/package

# Auto-detect built platforms and generate both China and Global yml sets
./docker/build-docker-yml.sh

# Generate x86_64 only
./docker/build-docker-yml.sh --platform=x86_64

# Generate China channel only
./docker/build-docker-yml.sh --platform=x86_64 --target=china
```

You can also run it directly in the docker template directory:

```bash
cd /worker_space/dm/open-cdm/package/docker

# Generate dual-platform, dual-channel output
./build-docker-yml.sh --platform=x86_64,arm64 --target=china,global
```

The script replaces `clougence/cgdm-*:${build_version}` with:

- China: `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-*:<version>-<arch>`
- Global: `docker.io/bladepipe/cgdm-*:<version>-<arch>`

#### Publish images

China:

```bash
cd /worker_space/dm/open-cdm/package
./publish-china.sh --platform=x86_64
```

Global:

```bash
cd /worker_space/dm/open-cdm/package
./publish-global.sh --platform=x86_64,arm64
```

### 7.4 Script Behavior Description

- `build-docker-yml.sh` detects platforms from `open-cdm/package/build` and replaces image prefixes and version suffixes according to the target channel.
- The output directory of `open-cdm/package/docker/build-docker-yml.sh` is `open-cdm/package/build`.
- The publish scripts prefer local Docker images. If an image does not exist, they automatically run `docker load` from `open-cdm/package/build/*.tar`.
- When publishing multi-platform images at the same time, the scripts automatically create and push a manifest.

### 7.5 Frequently Asked Questions

If publishing fails with `missing ... tar -> run package/package.sh --docker first`, it means the corresponding image tar file does not yet exist under `open-cdm/package/build`. Run this first:

```bash
cd /worker_space/dm/open-cdm/package
./package.sh --build --docker x86_64
```

If the script fails when `--platform` is omitted, it means the script requires all default platforms to have already been fully built. To publish a single platform only, specify it explicitly:

```bash
./publish-china.sh --platform=x86_64
./publish-global.sh --platform=x86_64
```