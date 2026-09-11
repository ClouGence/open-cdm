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
docker run -d --name cgdm-alone -p 8222:8222 bladepipe/cgdm-alone:4.2.2

# 中国镜像加速
docker run -d --name cgdm-alone -p 8222:8222 \
  cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-alone:4.2.2
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
  bladepipe/cgdm-alone:4.2.2

# 挂载到宿主机目录
mkdir -p /data/cgdm/{conf,logs,data,mysql}

docker run -d --name cgdm-alone \
  -p 8222:8222 \
  -v /data/cgdm/conf:/root/cgdm/alone/conf \
  -v /data/cgdm/logs:/root/cgdm/alone/logs \
  -v /data/cgdm/data:/root/cgdm/alone/data \
  -v /data/cgdm/mysql:/var/lib/mysql \
  bladepipe/cgdm-alone:4.2.2
```

当 `/data/cgdm/conf` 是空目录时，容器启动时会自动写入默认配置文件。

### 3.3 使用 Docker Compose

在构建完毕后 `open-cdm/package/build` 目录下会出现 `docker-alone-xxx.yml` 的部署文件。下面以其中一个：

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
  bladepipe/cgdm-console:4.2.2

# 启动 Sidecar
docker run -d --name dm_sidecar \
  --network cgdm-net \
  -e APP_WEB_PORT=8080 \
  -e DM_CLIENT_AK=<请替换为实际值> \
  -e DM_CLIENT_SK=<请替换为实际值> \
  -e DM_CLIENT_WSN=<请替换为实际值> \
  -e APP_SERVE_NAME=dm_console \
  -e APP_SERVE_PORT=8008 \
  bladepipe/cgdm-sidecar:4.2.2
```

中国区部署时，只需将镜像替换为：

- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-console:4.2.2`
- `cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/cgdm-sidecar:4.2.2`

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

### 6.4 用 GitHub Actions 自动发布镜像到 Docker Hub

仓库自带 `.github/workflows/docker-hub.yml`，适合在自己的 fork 上出镜像，不依赖任何私有 registry：

1. 在仓库 Settings → Secrets and variables → Actions 里配置：
    - 必需 secrets：`DOCKERHUB_USERNAME`（Docker Hub 账号，同时作为默认镜像命名空间）、`DOCKERHUB_TOKEN`（Docker Hub 的 read/write token）。
    - 可选 variables：`DOCKERHUB_NAMESPACE`（默认取 `DOCKERHUB_USERNAME`）、`DOCKERHUB_IMAGE_PREFIX`（默认 `cgdm`）。
    - 可选 secret：`DOCKER_VERSION_FEISHU_BOT_WEBHOOK_URL`，配置后构建成功/失败会发飞书通知，未配置则跳过。
2. 打版本 tag 自动触发：`git tag v4.2.2 && git push origin v4.2.2`，会构建 tgz 并按 `alone` / `console` / `sidecar`
   矩阵发布 `docker.io/<namespace>/cgdm-<service>:4.2.2` 和 `:latest`。
3. 预发布 tag（如 `v4.2.2-rc.1`）只发布该版本号，不会覆盖 `latest`。
4. 也可以在 Actions 页面手动 `Run workflow`，输入 version、services（例如只出 `alone` 省时间）、platforms
   （`linux/amd64`，或 `linux/amd64,linux/arm64`——arm64 走 QEMU 模拟较慢）、latest。
5. 版本一致性：workflow 会把 tag 版本注入 `CG_CLOUDDM_MAIN_VERSION`，并校验 tgz 内 `cgdm/<service>/conf/version`
   与 tag 一致，避免出现「镜像 tag 是 4.2.2、包内版本却是别的」的情况。
6. 资源与耗时：`build-tgz` 是完整 Gradle + 前端构建（约 15–25 分钟），docker 矩阵并行，仅 amd64 时约 10–15 分钟，加 arm64 大致翻倍。

发布完成后，把部署清单里的 `__IMAGE_PREFIX__` / `__IMAGE_TAG__` 换成 `<namespace>/cgdm-<service>` 与版本号即可（也可以用
`package/docker/build-docker-yml.sh` 生成）。

## 七、网关登录（connect 网关）

CloudDM 可以部署在 connect 网关（go-zoox/connect）之后，由网关完成登录，CloudDM 不再需要自己的登录页：

- 网关认证完成后把 `X-Connect-Token` 透传给上游，该 token 是用网关 `secret_key` 签名的 JWT，携带用户信息和 `permissions`。
- CloudDM 识别该 token 后自动匹配内部账户：
    - 已经绑定该网关身份的账户：直接登录；
    - 邮箱与内部账户相同的：直接使用该账户（包括归属主账号，其角色不变）；
    - 两者都没有：自动创建一个子账号（随机密码、`bind_type=Connect`、来源显示 Connect）。
- 权限映射角色：`permissions` 命中 `clouddm.connect.admin-permissions`（默认 `ADMIN`）时为 `clouddm.connect.admin-role`
  （默认 `Manager`），否则为 `clouddm.connect.default-role`（默认 `Developers`）；`clouddm.connect.allow-permissions`
  不为空时作为登录白名单，未命中直接拒绝登录。
- 网关身份的账号角色跟随网关权限，但只在 `Manager` 与默认角色之间调整，控制台手工分配的其它角色不会被覆盖。

### 7.1 CloudDM 侧配置

两种方式二选一，**不要两边同时配**：环境变量的优先级高于配置文件，但容器重建后环境变量不会被写回 `conf`。

方式一，写配置文件 `conf/alone.properties`（单机）或 `conf/console.properties`（集群）：

```properties
clouddm.connect.secret-key=<与网关 secret_key 一致>
#clouddm.connect.default-role=Developers
#clouddm.connect.admin-role=Manager
#clouddm.connect.admin-permissions=ADMIN
#clouddm.connect.allow-permissions=
```

方式二，容器里注入环境变量（推荐，改配置不用动镜像内的 `conf`）：

```yaml
services:
  clouddm:
    image: cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/clouddm-alone:latest
    environment:
      # secret_key 必须与网关一致
      CONNECT_SECRET_KEY: <与网关 secret_key 一致>
      # 以下都有默认值，按需覆盖
      # CONNECT_DEFAULT_ROLE: Developers
      # CONNECT_ADMIN_ROLE: Manager
      # CONNECT_ADMIN_PERMISSIONS: ADMIN
      # CONNECT_ALLOW_PERMISSIONS: ADMIN,DEVELOPER
    ports:
      - "8222:8222"
```

- 网关登录这 5 项用固定的短变量名 `CONNECT_*`，**非空**的值优先于配置文件；空值视为未设置（不会把配置文件里的值清掉）。
- 其它任何 `conf` 配置项仍然可以用展开形式覆盖：配置项 `a.b-c` 对应环境变量 `A_B_C`（小数点与中划线换成下划线并大写），例如
  `clougence.rdp.login.expire.sec` → `CLOUGENCE_RDP_LOGIN_EXPIRE_SEC`。
- 只有 `CONNECT_SECRET_KEY`（或 `clouddm.connect.secret-key`）是必填的开关，留空或不注入表示关闭该能力，此时行为与未部署网关时一致。
- 网关的 `secret_key`（或 `SECRET_KEY` / `SESSION_KEY`）必须与 `clouddm.connect.secret-key` 相同，否则 token 校验失败，请求按未登录处理。

### 7.2 网关侧配置（connect）

网关直接用自带的 doreamon 模式，参数同样全部支持环境变量，容器里注入即可：

```yaml
services:
  clouddm:
    image: cloudcanal-registry.cn-shanghai.cr.aliyuncs.com/clougence/clouddm-alone:latest
    environment:
      CONNECT_SECRET_KEY: <共享密钥>
    ports:
      - "8222:8222"

  connect:
    image: whatwewant/connect-doreamon:v1
    environment:
      # 必须与 CONNECT_SECRET_KEY / clouddm.connect.secret-key 一致
      SESSION_KEY: <共享密钥>
      # 上游指向 CloudDM，网关会把 X-Connect-Token 注入到发往该地址的请求
      UPSTREAM: http://clouddm:8222
      # 哆啦A梦（login.zcorky.com）OAuth2 应用凭据
      CLIENT_ID: <CLIENT_ID>
      CLIENT_SECRET: <CLIENT_SECRET>
      REDIRECT_URI: http://<网关对外地址>:8080/login/doreamon/callback
    ports:
      - "8080:8080"
```

若不想用镜像自带的 doreamon 入口，也可以写配置文件后用 `connect server -c config.yml` 启动：

```yaml
port: 8080
secret_key: <与 clouddm.connect.secret-key 一致>
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

### 7.3 注意事项

- 用户权限来自网关的 permissions 服务，需要网关把 `permissions` 写进 `X-Connect-Token`：网关镜像必须基于携带该 claim 的
  go-zoox/connect 构建（`user.Encode` 里写入 `permissions`），否则 token 里没有权限，所有网关用户都会落到
  `clouddm.connect.default-role`，并且一旦配置了 `allow-permissions` 白名单会导致全部拒登。
- CloudDM 只在没有有效会话 Cookie 时才会用 `X-Connect-Token` 建立会话；同一浏览器切换网关用户后，旧会话会保留到
  `clougence.rdp.login.expire.sec`（默认 86400 秒）过期。如需严格跟随网关会话，可以把该值调小。
- 首次登录自动创建的子账号账号名由 CloudDM 生成，显示名取 token 的 `nickname`，可在“子账号”页面查看和管理。
- 网关不再返回某个身份时，CloudDM 侧已创建的账号不会被自动删除，需要时可在“子账号”页面禁用或删除。
- 网关侧的权限变更在下一次建立会话时生效，最长延迟为 `clougence.rdp.login.expire.sec`（默认 86400 秒）。
- `permissions` 的值是用户在「对应 doreamon 应用（client_id）」下的角色权限码与菜单码去重列表（例如 `global.system.permissions`），
  不是 `ADMIN` 这类字面量；请把 open-cdm 应用里「管理员角色」的权限码填到 `CONNECT_ADMIN_PERMISSIONS`（多个逗号分隔），否则没人会被识别成管理员。
