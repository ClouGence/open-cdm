# CloudDM 部署指南

CloudDM 支持 **单机模式（Alone）** 和 **集群模式（Console + Sidecar）**，部署方式支持 **安装包**、**Docker**、**Kubernetes**。本文将会整合了 CloudDM 打包到部署的完整流程来讲解具体用法。
- 单机模式，将 Web 控制台和 Sidecar 以及元信息数据库 合并在一个容器或一个安装包中运行，适合小规模使用。
- 集群模式，最大特点是为了适应多地区数据库的统一授权访问。


## 一、概览

| 维度 | 支持内容 |
|------|----------|
| 运行模式 | Alone、Console + Sidecar |
| 部署方式 | 安装包、Docker、Kubernetes |
| 在线镜像仓库 | 国际区 `docker.io/bladepipe`<br/>中国区 `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence` |
| 本地打包产物目录 | `open-cdm/package/build` |

### 1.1 国际区镜像

国际区镜像托管在 Docker Hub，适合中国大陆以外地区：

| 组件 | 镜像 |
|------|------|
| Alone | `bladepipe/cgdm-alone:<目标版本>` |
| Console | `bladepipe/cgdm-console:<目标版本>` |
| Sidecar | `bladepipe/cgdm-sidecar:<目标版本>` |

### 1.2 中国区镜像

| 组件 | 镜像 |
|------|------|
| Alone | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:<目标版本>` |
| Console | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:<目标版本>` |
| Sidecar | `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:<目标版本>` |

---

## 二、本地打包

从源码仓库本地部署，需要先在 `open-cdm/package` 下执行打包。

### 2.1 仅生成安装包

```bash
cd open-cdm/package
./package.sh --build
```

执行后会生成：

- `cgdm-alone.tar.gz`
- `cgdm-console.tar.gz`
- `cgdm-sidecar.tar.gz`

### 2.2 Docker 镜像和YML

```bash
cd open-cdm/package

# 全部架构
./package.sh --build --docker

# 仅 x86_64
./package.sh --build --docker x86_64

# 仅 arm64
./package.sh --build --docker arm64
```

执行后，`open-cdm/package/build` 中会自动生成：

- 安装包：`cgdm-*.tar.gz`
- 离线镜像：`docker-*.tar`
- Docker Compose：`docker-alone-*.yml`、`docker-cluster-*.yml`
- Kubernetes：`k8s-alone-*.yml`、`k8s-cluster-*.yml`

---

## 三、单机模式部署

### 3.1 使用安装包

```bash
tar -xzf cgdm-alone.tar.gz
cd cgdm-alone
bin/startup.sh
```

首次启动后，通过浏览器访问：

```text
http://localhost:8222
```

系统会自动进入初始化向导，完成数据库初始化和管理员账号创建后即可使用。

### 3.2 使用 Docker

```bash
# 一键启动
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:3.1.1

# 中国镜像加速
docker run -d --name cgdm-alone -p 8222:8222 \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:3.1.1
```

持久化数据卷：

```bash
# 使用 Docker volume
docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v cgdm_alone_conf:/root/cgdm/alone/conf \
  -v cgdm_alone_logs:/root/cgdm/alone/logs \
  -v cgdm_alone_data:/root/cgdm/alone/data \
  -v cgdm_mysql_data:/var/lib/mysql \
  bladepipe/cgdm-alone:3.1.1

# 挂载到宿主机目录
mkdir -p /data/cgdm/{conf,logs,data,mysql}

docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v /data/cgdm/conf:/root/cgdm/alone/conf \
  -v /data/cgdm/logs:/root/cgdm/alone/logs \
  -v /data/cgdm/data:/root/cgdm/alone/data \
  -v /data/cgdm/mysql:/var/lib/mysql \
  bladepipe/cgdm-alone:3.1.1
```

当 `/data/cgdm/conf` 是空目录时，容器启动时会自动写入默认配置文件。

### 3.3 使用 Docker Compose

在构建完毕后 `open-cdm/package/build` 目录下会出现 `docker-alone-xxx.yml` 的部署文件。下面以其中一个：

```yml
services:
  dm_alone:
    image: clougence/cgdm-alone:3.1.1
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

将其保存为 `alone-docker-compose.yml` 或者在 `build` 目录下使用命令启动镜像

```bash
docker compose -f alone-docker-compose.yml up -d
```

### 3.4 Kubernetes 部署

Kubernetes 清单由打包流程生成，模板位于 `open-cdm/package/docker/k8s-alone.yml`，生成后位于 `open-cdm/package/build`。清单默认适合单副本验证或小规模使用，包含 Web 服务、配置/日志/数据 PVC，以及内置 MySQL 数据目录 PVC。

```bash
cd open-cdm/package
./package.sh --build --docker x86_64
```

部署前请确认集群具备可用的默认 `StorageClass`，并确认节点可以拉取清单中的镜像。如果使用本地构建镜像，需要先将 `docker-*.tar` 导入到集群节点的容器运行时，或推送到集群可访问的镜像仓库。

```bash
cd open-cdm/package/build
kubectl apply -f k8s-alone-x86_64-<目标版本>.yml
kubectl -n cgdm rollout status deploy/dm-alone
kubectl -n cgdm get pods,svc,pvc
```

自动生成的清单默认会创建：

- `cgdm` 命名空间
- Alone 的 PVC、Service、Deployment
- `cgdm-mysql-data` PVC，用于内置 MySQL 数据持久化

默认 Service 类型为 `ClusterIP`。本地验证可以使用端口转发：

```bash
kubectl -n cgdm port-forward svc/dm-alone 8222:8222
```

然后访问 `http://localhost:8222`。如果需要长期对外访问，请根据集群环境选择 `NodePort`、`LoadBalancer` 或 Ingress；生产环境还应替换默认密码、JWT 密钥，并按数据规模调整 PVC 容量。

---

## 四、集群模式部署

集群模式由 **Console（控制台）** 和 **Sidecar** 两个独立组件组成，适合团队协作、大规模数据源管理和多节点接入。

### 4.1 使用安装包

先安装并启动 Console：

```bash
tar -xzf cgdm-console.tar.gz
cd cgdm-console
bin/startup.sh
```

启动后，通过浏览器访问：

```text
http://localhost:8222
```

完成初始化后，在 Console 中添加 Sidecar 机器，获取 `AK / SK / WSN`，再安装并启动 Sidecar：

```bash
# 解压包
tar -xzf cgdm-sidecar.tar.gz
# 配置 AK / SK / WSN
cd cgdm-sidecar/conf
# 启动 sidecar
bin/startup.sh
```

部署顺序建议如下：

1. 先启动 Console
2. 登录 Console 完成初始化
3. 在 Console 中添加 Sidecar 机器并生成 `AK / SK / WSN`
4. 将生成的参数配置到 Sidecar 后再启动或重启 Sidecar

### 4.2 使用 Docker

```bash
# 创建网络
docker network create cgdm-net

# 启动 MySQL
docker run -d --name dm_mysql \
  --network cgdm-net \
  -p 26000:3306 \
  -e MYSQL_DATABASE=cdmgr \
  -e MYSQL_ROOT_PASSWORD=123456 \
  mysql:8.0 \
  mysqld --character-set-server=utf8mb4 \
         --collation-server=utf8mb4_unicode_ci

# 启动 Console
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
  bladepipe/cgdm-console:3.1.1

# 启动 Sidecar
docker run -d --name dm_sidecar \
  --network cgdm-net \
  -e APP_WEB_PORT=8080 \
  -e DM_CLIENT_AK=<请替换为实际值> \
  -e DM_CLIENT_SK=<请替换为实际值> \
  -e DM_CLIENT_WSN=<请替换为实际值> \
  -e APP_SERVE_NAME=dm_console \
  -e APP_SERVE_PORT=8008 \
  bladepipe/cgdm-sidecar:3.1.1
```

中国区部署时，只需将镜像替换为：

- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:3.1.1`
- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:3.1.1`

### 4.3 使用 Docker Compose

在构建完毕后 `open-cdm/package/build` 目录下会出现 `docker-cluster-xxx.yml` 的部署文件。下面以其中一个：

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
    image: clougence/cgdm-console:x86_64-3.1.1
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
    image: clougence/cgdm-sidecar:x86_64-3.1.1
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
      # 首次安装时默认带了一个默认 Worker
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

将其保存为 `cluster-docker-compose.yml` 或者在 `build` 目录下使用命令启动镜像

```bash
docker compose -f cluster-docker-compose.yml up -d
```

### 4.4 Kubernetes 部署

Kubernetes 清单由打包流程生成，模板位于 `open-cdm/package/docker/k8s-cluster.yml`，生成后位于 `open-cdm/package/build`。清单默认部署一个 MySQL、一个 Console 和一个 Sidecar，适合快速验证集群模式。

```bash
cd open-cdm/package
./package.sh --build --docker x86_64
```

部署前请确认集群具备可用的默认 `StorageClass`，并确认节点可以拉取 `mysql:8.0` 和 CloudDM 镜像。如果使用本地构建镜像，需要先导入到集群节点或推送到可访问的镜像仓库。

```bash
cd open-cdm/package/build
kubectl apply -f k8s-cluster-x86_64-<目标版本>.yml
kubectl -n cgdm rollout status statefulset/dm-mysql
kubectl -n cgdm rollout status deploy/dm-console
kubectl -n cgdm rollout status deploy/dm-sidecar
kubectl -n cgdm get pods,svc,pvc
```

自动生成的清单默认会创建：

- `cgdm` 命名空间
- MySQL Service 与 StatefulSet
- StatefulSet 自动创建的 MySQL 数据 PVC
- Console 的 PVC、Service、Deployment
- Sidecar 的 PVC、Service、Deployment

默认情况下，Console Web 服务以 `ClusterIP` 暴露，端口为 `8222`。本地验证可以使用端口转发：

```bash
kubectl -n cgdm port-forward svc/dm-console 8222:8222
```

然后访问 `http://localhost:8222`。如需集群外长期访问，请结合环境调整 Console Service 为 `NodePort`、`LoadBalancer` 或配置 Ingress。

Sidecar 清单中带有默认 `DM_CLIENT_AK`、`DM_CLIENT_SK`、`DM_CLIENT_WSN`，只适合快速验证。生产环境建议先完成 Console 初始化，在 Console 中创建 Sidecar Worker 后，把实际生成的 `AK / SK / WSN` 写回清单，再重新部署：

```bash
kubectl -n cgdm set env deploy/dm-sidecar \
  DM_CLIENT_AK=<实际 AK> \
  DM_CLIENT_SK=<实际 SK> \
  DM_CLIENT_WSN=<实际 WSN>
```

常用排查命令：

```bash
kubectl -n cgdm describe pod <pod-name>
kubectl -n cgdm logs deploy/dm-console
kubectl -n cgdm logs deploy/dm-sidecar
kubectl -n cgdm logs statefulset/dm-mysql
```

生产环境还应替换默认 MySQL 密码、JWT 密钥，按数据规模调整 PVC 容量，并将敏感配置改为 `Secret` 管理。

---

## 五、访问与初始化

无论是 Alone 还是 Cluster，Web 控制台默认访问地址均为：

```text
http://localhost:8222
```

首次访问会进入初始化向导。完成数据库初始化和管理员账号创建后，系统即可进入完整业务应用。如无特殊配置默认账号为 `admin@cdmgr.com`

---

## 六、镜像发布与渠道化部署文件

除了部署运行本身，仓库当前还提供了一组用于渠道化 yml 生成和镜像发布的脚本。它们依赖 `open-cdm/package/build` 中已经构建好的安装包和离线镜像，不负责源码编译。

### 6.1 脚本位置

| 任务 | 入口脚本 | 说明 |
|------|----------|------|
| 生成 China / Global Compose 与 Kubernetes yml | `open-cdm/package/docker/build-docker-yml.sh` | 读取当前目录模板，输出到 `open-cdm/package/build` |
| 发布中国区镜像 | `open-cdm/package/docker-publish-china.sh` | 从 `open-cdm/package/build` 读取离线镜像 tar |
| 发布全球镜像 | `open-cdm/package/docker-publish-global.sh` | 从 `open-cdm/package/build` 读取离线镜像 tar |

如果你只是要部署本地打包后的产物，直接使用 `open-cdm/package/build` 下自动生成的 `docker-*.yml` 和 `k8s-*.yml` 即可；如果你需要按中国区或全球仓库生成带完整镜像前缀的渠道化清单，再使用这里的脚本。

### 6.2 环境准备与凭据

发布脚本会从 `~/.gradle/gradle.properties` 读取仓库凭据。

```properties
# China Registry (Alibaba Cloud Container Registry)
cgdm.docker.china.username=<your_aliyun_username>
cgdm.docker.china.password=<your_aliyun_fixed_password>

# Global Registry (Docker Hub)
cgdm.docker.global.username=<your_dockerhub_username>
cgdm.docker.global.password=<your_dockerhub_token>
```

### 6.3 发布工作流

顺序分三步：

1. 在 `open-cdm/package` 下执行 `./package.sh --build --docker`，生成安装包、离线镜像和基础清单。
2. 把镜像推送到远端仓库
- `./docker-publish-global.sh` 镜像推送到 DockerHub
- `./docker-publish-china.sh` 镜像推送到中国地区
3. 生成渠道化 yml `open-cdm/package/docker/build-docker-yml.sh`。
