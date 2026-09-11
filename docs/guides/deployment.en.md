# CloudDM Deployment Guide

CloudDM supports **Standalone mode (Alone)** and **Cluster mode (Console + Sidecar)**, with deployment methods including **install packages**, **Docker**, and **Kubernetes**. This guide walks through the complete process from packaging to deployment.
- In Standalone mode, the Web console, Sidecar, and metadata database run together in a single container or a single install package, making it suitable for small-scale use.
- The main characteristic of Cluster mode is unified authorized access for databases across multiple regions.


## 1. Overview

| Dimension | Supported Content |
|-----------|-------------------|
| Runtime modes | Alone, Console + Sidecar |
| Deployment methods | Install package, Docker, Kubernetes |
| Online image registries | Global `docker.io/bladepipe`<br/>China `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence` |
| Local packaging output directory | `open-cdm/package/build` |

### 1.1 Global Images

Global images are hosted on Docker Hub and are suitable for regions outside mainland China:

| Component | Image |
|-----------|-------|
| Alone | `bladepipe/cgdm-alone:<target_version>` |
| Console | `bladepipe/cgdm-console:<target_version>` |
| Sidecar | `bladepipe/cgdm-sidecar:<target_version>` |

### 1.2 China Images

| Component | Image |
|-----------|-------|
| Alone | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:<target_version>` |
| Console | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:<target_version>` |
| Sidecar | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:<target_version>` |

---

## 2. Local Packaging

To deploy locally from the source repository, you should first package the project under `open-cdm/package`.

### 2.1 Generate Install Packages Only

```bash
cd open-cdm/package
./package.sh --build
```

This generates:

- `cgdm-alone.tar.gz`
- `cgdm-console.tar.gz`
- `cgdm-sidecar.tar.gz`

### 2.2 Docker Images and YML

```bash
cd open-cdm/package

# All architectures
./package.sh --build --docker

# x86_64 only
./package.sh --build --docker x86_64

# arm64 only
./package.sh --build --docker arm64
```

After running, `open-cdm/package/build` will automatically contain:

- Install packages: `cgdm-*.tar.gz`
- Offline images: `docker-*.tar`
- Docker Compose files: `docker-alone-*.yml`, `docker-cluster-*.yml`
- Kubernetes files: `k8s-alone-*.yml`, `k8s-cluster-*.yml`

---

## 3. Standalone Mode Deployment

### 3.1 Use the Install Package

```bash
tar -xzf cgdm-alone.tar.gz
cd cgdm-alone
bin/startup.sh
```

After the first startup, open the following address in your browser:

```text
http://localhost:8222
```

The system automatically enters the initialization wizard. After completing database initialization and administrator account creation, the system is ready to use.

### 3.2 Use Docker

```bash
# One-click startup
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:4.2.2

# China registry acceleration
docker run -d --name cgdm-alone -p 8222:8222 \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:4.2.2
```

Persistent data volumes:

```bash
# Use Docker volumes
docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  bladepipe/cgdm-alone:4.2.2

# Mount to host directories
mkdir -p /data/cgdm/{conf,logs,data,mysql}

docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v /data/cgdm/conf:/root/cgdm/alone/conf \
  -v /data/cgdm/logs:/root/cgdm/alone/logs \
  -v /data/cgdm/data:/root/cgdm/alone/data \
  -v /data/cgdm/mysql:/var/lib/mysql \
  bladepipe/cgdm-alone:4.2.2
```

When `/data/cgdm/conf` is empty, the container initializes it with the default configuration files on startup.

### 3.3 Use Docker Compose

After the build is complete, deployment files named `docker-alone-xxx.yml` will appear under `open-cdm/package/build`. Here is one example:

```yml
services:
  dm_alone:
    image: clougence/cgdm-alone:4.2.2
    container_name: cgdm-alone
    restart: always
    ports:
      - "8222:8222"
      - "8008:8008"
    volumes:
      - cgdm_alone_conf:/root/cgdm/alone/conf
      - cgdm_alone_logs:/root/cgdm/alone/logs
      - cgdm_alone_data:/root/cgdm/alone/data
      - cgdm_mysql_data:/var/lib/mysql
    environment:
      APP_WEB_PORT: 8222
      APP_WEB_JWT: "ljgefdgjosdighjeroigh"
      APP_SERVE_NAME: dm_alone
      APP_SERVE_PORT: 8008
      MYSQL_EMBEDDED: "true"
      MYSQL_ROOT_PASSWORD: "123456"
      # Override image defaults with packaged docker deployment values.
      DB_HOST: "127.0.0.1"
      DB_PORT: 3306
      DB_DATABASE: cdmgr
      DB_USERNAME: root
      DB_PASSWORD: 123456

volumes:
  cgdm_alone_conf:
  cgdm_alone_logs:
  cgdm_alone_data:
  cgdm_mysql_data:
```

Save it as `alone-docker-compose.yml`, or start the image directly with the command below in the `build` directory:

```bash
docker compose -f alone-docker-compose.yml up -d
```

### 3.4 Kubernetes Deployment

Kubernetes manifests are generated by the packaging process. The template is located at `open-cdm/package/docker/k8s-alone.yml`, and the generated files are placed under `open-cdm/package/build`. The manifest is suitable for single-replica validation or small-scale use by default, and includes the Web service, configuration/log/data PVCs, and an embedded MySQL data PVC.

```bash
cd open-cdm/package
./package.sh --build --docker x86_64
```

Before deployment, make sure the cluster has an available default `StorageClass`, and that the nodes can pull the images referenced by the manifest. If you use locally built images, import `docker-*.tar` into the container runtime on the cluster nodes first, or push the images to a registry that the cluster can access.

```bash
cd open-cdm/package/build
kubectl apply -f k8s-alone-x86_64-<target_version>.yml
kubectl -n cgdm rollout status deploy/dm-alone
kubectl -n cgdm get pods,svc,pvc
```

By default, the generated manifest creates:

- the `cgdm` namespace
- the Alone PVC, Service, and Deployment
- the `cgdm-mysql-data` PVC for embedded MySQL data persistence

The default Service type is `ClusterIP`. For local validation, use port forwarding:

```bash
kubectl -n cgdm port-forward svc/dm-alone 8222:8222
```

Then open `http://localhost:8222`. If you need long-term external access, choose `NodePort`, `LoadBalancer`, or Ingress according to your cluster environment. In production, also replace the default password and JWT secret, and adjust PVC capacity based on data volume.

---

## 4. Cluster Mode Deployment

Cluster mode consists of two independent components, **Console** and **Sidecar**, and is suitable for team collaboration, large-scale data source management, and multi-node access.

### 4.1 Use the Install Package

Install and start Console first:

```bash
tar -xzf cgdm-console.tar.gz
cd cgdm-console
bin/startup.sh
```

After startup, open the following address in your browser:

```text
http://localhost:8222
```

After initialization is complete, add a Sidecar machine in Console, obtain `AK / SK / WSN`, and then install and start Sidecar:

```bash
# Extract the package
tar -xzf cgdm-sidecar.tar.gz
# Configure AK / SK / WSN
cd cgdm-sidecar/conf
# Start sidecar
bin/startup.sh
```

The recommended deployment order is:

1. Start Console first
2. Sign in to Console and complete initialization
3. Add a Sidecar machine in Console and generate `AK / SK / WSN`
4. Configure the generated parameters for Sidecar, then start or restart Sidecar

### 4.2 Use Docker

```bash
# Create a network
docker network create cgdm-net

# Start MySQL
docker run -d --name dm_mysql \
  --network cgdm-net \
  -p 26000:3306 \
  -e MYSQL_DATABASE=cdmgr \
  -e MYSQL_ROOT_PASSWORD=123456 \
  mysql:8.0 \
  mysqld --character-set-server=utf8mb4 \
         --collation-server=utf8mb4_unicode_ci

# Start Console
docker run -d --name dm_console \
  --network cgdm-net \
  -p 8222:8222 \
  -p 8008:8008 \
  -e APP_WEB_PORT=8222 \
  -e APP_WEB_JWT=ljgefdgjosdighjeroigh \
  -e APP_SERVE_NAME=dm_console \
  -e APP_SERVE_PORT=8008 \
  -e DB_HOST=dm_mysql \
  -e DB_PORT=3306 \
  -e DB_DATABASE=cdmgr \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=123456 \
  bladepipe/cgdm-console:4.2.2

# Start Sidecar
docker run -d --name dm_sidecar \
  --network cgdm-net \
  -e APP_WEB_PORT=8080 \
  -e DM_CLIENT_AK=<replace_with_actual_value> \
  -e DM_CLIENT_SK=<replace_with_actual_value> \
  -e DM_CLIENT_WSN=<replace_with_actual_value> \
  -e APP_SERVE_NAME=dm_console \
  -e APP_SERVE_PORT=8008 \
  bladepipe/cgdm-sidecar:4.2.2
```

For China deployment, simply replace the images with:

- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:4.2.2`
- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:4.2.2`

### 4.3 Use Docker Compose

After the build is complete, deployment files named `docker-cluster-xxx.yml` will appear under `open-cdm/package/build`. Here is one example:

```yml
services:
  dm_mysql:
    image: mysql:8.0
    container_name: cgdm-mysql
    restart: always
    ports:
      - "26000:3306"
    volumes:
      - cgdm_mysql_data:/var/lib/mysql
    environment:
      MYSQL_DATABASE: cdmgr
      MYSQL_ROOT_PASSWORD: 123456
    command: [ "mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]

  dm_console:
    image: clougence/cgdm-console:x86_64-4.2.2
    container_name: cgdm-console
    restart: always
    ports:
      - "8222:8222"
      - "8008:8008"
    depends_on:
      - dm_mysql
    volumes:
      - cgdm_console_conf:/root/cgdm/console/conf
      - cgdm_console_logs:/root/cgdm/console/logs
      - cgdm_console_data:/root/cgdm/console/data
    environment:
      APP_WEB_PORT: 8222
      APP_WEB_JWT: "ljgefdgjosdighjeroigh"
      APP_SERVE_NAME: dm_console
      APP_SERVE_PORT: 8008
      # Override image defaults with packaged docker deployment values.
      DB_HOST: dm_mysql
      DB_PORT: 3306
      DB_DATABASE: cdmgr
      DB_USERNAME: root
      DB_PASSWORD: 123456

  dm_sidecar:
    image: clougence/cgdm-sidecar:x86_64-4.2.2
    container_name: cgdm-sidecar
    restart: always
    depends_on:
      - dm_console
    volumes:
      - cgdm_sidecar_0_conf:/root/cgdm/sidecar/conf
      - cgdm_sidecar_0_logs:/root/cgdm/sidecar/logs
      - cgdm_sidecar_0_data:/root/cgdm/sidecar/data
    environment:
      APP_WEB_PORT: 8080
      # A default Worker is included during the first installation.
      DM_CLIENT_AK: "ak0a2c62tdo1ap2416655mpyx0v36l359p1v5rn782caw8t0qkk1s94b80lfs90"
      DM_CLIENT_SK: "sk6206iy4pb0eydz9hg97jo3tu5d80j97e91bbql65167u8wb75x4ej6e4v4aa4"
      DM_CLIENT_WSN: "wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f"
      APP_SERVE_NAME: dm_console
      APP_SERVE_PORT: 8008

volumes:
  cgdm_console_conf:
  cgdm_console_logs:
  cgdm_console_data:
  cgdm_sidecar_0_conf:
  cgdm_sidecar_0_logs:
  cgdm_sidecar_0_data:
  cgdm_mysql_data:
```

Save it as `cluster-docker-compose.yml`, or start the image directly with the command below in the `build` directory:

```bash
docker compose -f cluster-docker-compose.yml up -d
```

### 4.4 Kubernetes Deployment

Kubernetes manifests are generated by the packaging process. The template is located at `open-cdm/package/docker/k8s-cluster.yml`, and the generated files are placed under `open-cdm/package/build`. The manifest deploys one MySQL, one Console, and one Sidecar by default, and is suitable for quick Cluster mode validation.

```bash
cd open-cdm/package
./package.sh --build --docker x86_64
```

Before deployment, make sure the cluster has an available default `StorageClass`, and that the nodes can pull `mysql:8.0` and the CloudDM images. If you use locally built images, import them into the cluster nodes or push them to an accessible registry first.

```bash
cd open-cdm/package/build
kubectl apply -f k8s-cluster-x86_64-<target_version>.yml
kubectl -n cgdm rollout status statefulset/dm-mysql
kubectl -n cgdm rollout status deploy/dm-console
kubectl -n cgdm rollout status deploy/dm-sidecar
kubectl -n cgdm get pods,svc,pvc
```

By default, the generated manifest creates:

- the `cgdm` namespace
- MySQL Service and StatefulSet
- the MySQL data PVC automatically created by the StatefulSet
- the Console PVC, Service, and Deployment
- the Sidecar PVC, Service, and Deployment

By default, the Console Web service is exposed as `ClusterIP` on port `8222`. For local validation, use port forwarding:

```bash
kubectl -n cgdm port-forward svc/dm-console 8222:8222
```

Then open `http://localhost:8222`. If you need long-term external access from outside the cluster, adjust the Console Service to `NodePort`, `LoadBalancer`, or configure Ingress according to your environment.

The Sidecar manifest includes default `DM_CLIENT_AK`, `DM_CLIENT_SK`, and `DM_CLIENT_WSN` values, which are only suitable for quick validation. In production, initialize Console first, create a Sidecar Worker in Console, write the generated `AK / SK / WSN` values back into the manifest, and redeploy:

```bash
kubectl -n cgdm set env deploy/dm-sidecar \
  DM_CLIENT_AK=<actual_AK> \
  DM_CLIENT_SK=<actual_SK> \
  DM_CLIENT_WSN=<actual_WSN>
```

Common troubleshooting commands:

```bash
kubectl -n cgdm describe pod <pod-name>
kubectl -n cgdm logs deploy/dm-console
kubectl -n cgdm logs deploy/dm-sidecar
kubectl -n cgdm logs statefulset/dm-mysql
```

In production, also replace the default MySQL password and JWT secret, adjust PVC capacity based on data volume, and manage sensitive configuration with `Secret`.

---

## 5. Access and Initialization

Whether you use Alone or Cluster mode, the default Web console address is:

```text
http://localhost:8222
```

On the first visit, the initialization wizard appears. After database initialization and administrator account creation are completed, the system enters the full application. Unless otherwise configured, the default account is `admin@cdmgr.com`.

---

## 6. Image Publishing and Channel-Specific Deployment Files

In addition to runtime deployment itself, the repository also provides a set of scripts for generating channel-specific yml files and publishing images. These scripts depend on the install packages and offline images that have already been built under `open-cdm/package/build` and do not compile source code themselves.

### 6.1 Script Locations

| Task | Entry Script | Description |
|------|--------------|-------------|
| Generate China / Global Compose and Kubernetes yml | `open-cdm/package/docker/build-docker-yml.sh` | Reads templates from the current directory and outputs to `open-cdm/package/build` |
| Publish China images | `open-cdm/package/docker-publish-china.sh` | Reads offline image tar files from `open-cdm/package/build` |
| Publish Global images | `open-cdm/package/docker-publish-global.sh` | Reads offline image tar files from `open-cdm/package/build` |

If you only need to deploy locally packaged artifacts, you can directly use the generated `docker-*.yml` and `k8s-*.yml` files under `open-cdm/package/build`. If you need channel-specific manifests with full image prefixes for China or Global registries, use the scripts described here.

### 6.2 Environment Preparation and Credentials

The publish scripts read registry credentials from `~/.gradle/gradle.properties`.

```properties
# China Registry (Alibaba Cloud Container Registry)
cgdm.docker.china.username=<your_aliyun_username>
cgdm.docker.china.password=<your_aliyun_fixed_password>

# Global Registry (Docker Hub)
cgdm.docker.global.username=<your_dockerhub_username>
cgdm.docker.global.password=<your_dockerhub_token>
```

### 6.3 Publishing Workflow

The process consists of three steps:

1. Run `./package.sh --build --docker` under `open-cdm/package` to generate install packages, offline images, and base manifests.
2. Push the images to the remote registry.
   - `./docker-publish-global.sh` pushes images to Docker Hub.
   - `./docker-publish-china.sh` pushes images to the China registry.
3. Generate channel-specific yml files with `open-cdm/package/docker/build-docker-yml.sh`.

## 7. Gateway Login (connect gateway)

CloudDM can run behind a connect gateway (go-zoox/connect), the gateway then owns the login and CloudDM no longer needs its own login page:

- After authentication the gateway forwards `X-Connect-Token` to the upstream, a JWT signed with the gateway `secret_key` that carries the user and its `permissions`.
- CloudDM resolves the token to an internal account:
    - an account already bound to this gateway identity: login with it;
    - an internal account with the same email: use that account as it is (including the primary account, whose role stays unchanged);
    - otherwise: create a sub account automatically (random password, `bind_type=Connect`, shown as Connect).
- Permissions decide the role: a permission matching `clouddm.connect.admin-permissions` (default `ADMIN`) gives
  `clouddm.connect.admin-role` (default `Manager`), everything else gives `clouddm.connect.default-role` (default
  `Developers`). A non-empty `clouddm.connect.allow-permissions` acts as a login allow list and rejects other users.
- Roles of gateway accounts follow gateway permissions, but only between `Manager` and the default role; other roles assigned in the console are never overwritten.

### 7.1 CloudDM Configuration

Pick one of the two ways, **do not configure both**: environment variables take precedence over the configuration file, while they are never written back into `conf` when a container is recreated.

Option one, the configuration file `conf/alone.properties` (standalone) or `conf/console.properties` (cluster):

```properties
clouddm.connect.secret-key=<same as the gateway secret_key>
#clouddm.connect.default-role=Developers
#clouddm.connect.admin-role=Manager
#clouddm.connect.admin-permissions=ADMIN
#clouddm.connect.allow-permissions=
```

Option two, inject environment variables into the container (recommended, changing the configuration needs no edit of the `conf` inside the image):

```yaml
services:
  clouddm:
    image: cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/clouddm-alone:latest
    environment:
      # must be identical to the gateway secret_key
      CONNECT_SECRET_KEY: <same as the gateway secret_key>
      # every key below has a default, override it only when needed
      # CONNECT_DEFAULT_ROLE: Developers
      # CONNECT_ADMIN_ROLE: Manager
      # CONNECT_ADMIN_PERMISSIONS: ADMIN
      # CONNECT_ALLOW_PERMISSIONS: ADMIN,DEVELOPER
    ports:
      - "8222:8222"
```

- These five gateway keys use the fixed short environment variable names `CONNECT_*`, a **non blank** value wins over the configuration file, a blank one counts as not set (it never clears the value of the configuration file).
- Every other `conf` key can be overridden with its expanded name: the key `a.b-c` maps to `A_B_C` (dots and hyphens become underscores, the name is uppercased), for example
  `clougence.rdp.login.expire.sec` → `CLOUGENCE_RDP_LOGIN_EXPIRE_SEC`.
- Only `CONNECT_SECRET_KEY` (or `clouddm.connect.secret-key`) is required and acts as the switch, a blank or missing value disables the feature and behavior is identical to a deployment without a gateway.
- The gateway `secret_key` (or `SECRET_KEY` / `SESSION_KEY`) must equal `clouddm.connect.secret-key`, otherwise the token is rejected and the request is handled as not logged in.

### 7.2 Gateway Configuration (connect)

Use the built-in doreamon mode of the gateway, it accepts environment variables for every parameter, so a container only needs environment variables:

```yaml
services:
  clouddm:
    image: cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/clouddm-alone:latest
    environment:
      CONNECT_SECRET_KEY: <shared secret>
    ports:
      - "8222:8222"

  connect:
    image: whatwewant/connect-doreamon:v1
    environment:
      # must be identical to CONNECT_SECRET_KEY / clouddm.connect.secret-key
      SESSION_KEY: <shared secret>
      # upstream points to CloudDM, the gateway injects X-Connect-Token into requests sent there
      UPSTREAM: http://clouddm:8222
      # OAuth2 application credentials of Doreamon (login.zcorky.com)
      CLIENT_ID: <CLIENT_ID>
      CLIENT_SECRET: <CLIENT_SECRET>
      REDIRECT_URI: http://<gateway-host>:8080/login/doreamon/callback
    ports:
      - "8080:8080"
```

Instead of the doreamon entrypoint of the image, a configuration file and `connect server -c config.yml` work as well:

```yaml
port: 8080
secret_key: <same as clouddm.connect.secret-key>
session_max_age: 86400

upstream:
  host: 127.0.0.1
  port: 8222

oauth2:
  - name: doreamon
    client_id: <CLIENT_ID>
    client_secret: <CLIENT_SECRET>
    redirect_uri: http://127.0.0.1:8080/login/doreamon/callback

auth:
  mode: oauth2
  provider: doreamon
```

### 7.3 Notes

- Permissions come from the gateway permissions service, the gateway has to put `permissions` into `X-Connect-Token`:
  the gateway image must be built from a go-zoox/connect carrying that claim (written by `user.Encode`). Without it the
  token has no permissions, every gateway user falls back to `clouddm.connect.default-role`, and a configured
  `allow-permissions` list would reject all of them.
- CloudDM establishes a session from `X-Connect-Token` only when the request has no valid session cookie. After another
  gateway user logs in in the same browser, the previous session stays valid until `clougence.rdp.login.expire.sec`
  (86400 seconds by default) expires; lower it to follow the gateway session strictly.
- The account name of an auto created sub account is generated by CloudDM, the display name comes from the `nickname`
  claim, and both can be managed on the Sub Account page.
- Accounts created for a gateway identity are not deleted automatically when the identity disappears from the gateway,
  disable or delete them on the Sub Account page when needed.
- Permission changes on the gateway side apply when the next session is established, at most after `clougence.rdp.login.expire.sec` (86400 seconds by default).
- The `permissions` values are the role and menu permission codes of the user inside the connected doreamon application
  (for example `global.system.permissions`), not literals like `ADMIN`. Put the permission code of the admin role of your
  open-cdm application into `CONNECT_ADMIN_PERMISSIONS` (comma separated for several), otherwise nobody is recognized as
  an administrator.
