# DB 测试资源使用说明

`tests/dbs` 用于启动 CloudDM 本地联调和测试所需的数据源、SSH Server、HTTP/SOCKS 代理服务。

## 目录结构

```text
tests/dbs
├── dbs_x86/docker-compose.yml      # x86 环境
├── dbs_arm64/docker-compose.yml    # arm64 环境
├── certs/                          # MySQL、Oracle、PostgreSQL 共用证书目录
├── mysql/                          # MySQL 启动脚本和初始化 SQL
├── oracle/                         # Oracle 初始化 SQL、TCPS 启动脚本和 listener wallet
├── postgres/                       # PostgreSQL 启动脚本和初始化 SQL
├── proxy/3proxy.cfg                # 无认证 3proxy 配置
├── proxy-auth/3proxy.cfg           # 账号密码认证 3proxy 配置
└── ssh/                            # SSH Server 使用的公私钥和初始化脚本
```

`dbs_x86` 和 `dbs_arm64` 的服务名、端口和测试能力保持一致。同一台机器上不要同时启动两套 compose，除非先修改其中一套端口。`dbs_arm64` 中 SQL Server、DB2 和 3proxy 使用 `linux/amd64` 镜像运行，需要 Docker 支持跨架构模拟。

## 启停命令

在仓库根目录执行：

```bash
cd /home/zyc/project/dm/open-cdm
```

x86：

```bash
docker compose -f tests/dbs/dbs_x86/docker-compose.yml up -d
docker compose -f tests/dbs/dbs_x86/docker-compose.yml ps
docker compose -f tests/dbs/dbs_x86/docker-compose.yml down
```

arm64：

```bash
docker compose -f tests/dbs/dbs_arm64/docker-compose.yml up -d
docker compose -f tests/dbs/dbs_arm64/docker-compose.yml ps
docker compose -f tests/dbs/dbs_arm64/docker-compose.yml down
```

如果已经启动过旧版本 SSH Server，修改端口或初始化脚本后需要重建容器：

```bash
docker compose -f tests/dbs/dbs_x86/docker-compose.yml up -d --force-recreate ssh_server
```

## 数据源连接信息

- 宿主机直连时使用 `127.0.0.1` 和对外端口。
- 代理或 SSH 通道，可以使用容器内服务名 + 对内端口。

| 服务 | 容器内服务名 | Database/Service | 常规连接 | SSL（信任） | SSL（CA证书/单向） | SSL（双向） | 备注 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| MySQL | `mysql` | `devtester` | Port: `3306 (内)/2330 (外)`<br>用户: `root` / `123456` | Port: `3306 (内)/2330 (外)`<br>用户: `root` / `123456`<br>证书: 不需要 | Port: `3306 (内)/2330 (外)`<br>用户: `root` / `123456`<br>CA 证书: `certs/ca.p12`<br>CA 证书密码: 留空 | Port: `3306 (内)/2330 (外)`<br>用户: `sslclient` / `123456`<br>CA 证书: `certs/ca.p12`<br>CA 证书密码: 留空<br>客户端 KeyStore: `certs/client.p12`<br>KeyStore 密码: `123456` | 兼容文件: `ca-123456.p12`、`ca.jks`、`client.jks` |
| Oracle | `oracle` | SID: `XE`/`FREE`<br>Service Name: `DEVTESTDB`<br>PDB: `DEVTESTDB` | Port: `1521 (内)/2521 (外)`<br>业务用户: `devtester` / `123456` 使用 `Service Name` 或 `PDB`，值 `DEVTESTDB`<br>实例用户: `SYSTEM` / `123456` 使用 `SID`，x86 值 `XE`，arm64 值 `FREE` | - | Port: `2484 (内)/2484 (外)`<br>连接值和账号同常规连接<br>CA KeyStore: `certs/ca.p12`<br>KeyStore 密码: 留空 | Port: `2485 (内)/2485 (外)`<br>连接值和账号同常规连接<br>CA KeyStore: `certs/ca.p12`<br>CA KeyStore 密码: 留空<br>客户端 KeyStore: `certs/client.p12`<br>KeyStore 密码: `123456` | compose 使用 `oracle/wallet` 启动 TCPS listener |
| PostgreSQL | `postgres` | `postgres` | Port: `5432 (内)/2543 (外)`<br>用户: `postgres` / `123456` | Port: `5432 (内)/2543 (外)`<br>用户: `postgres` / `123456`<br>证书: 不需要 | Port: `5432 (内)/2543 (外)`<br>用户: `postgres` / `123456`<br>CA 证书: `ca.crt` | Port: `5432 (内)/2543 (外)`<br>用户: `sslclient` / 留空<br>CA 证书: `ca.crt`<br>客户端证书: `client.crt`<br>客户端私钥: `client.pk8`<br>私钥短语: 留空 | x86 和 arm64 compose 共享同一套测试证书 |
| Redis | `redis` | - | Port: `6379 (内)/2637 (外)`<br>密码: `123456` | - | - | - | `requirepass` |
| MongoDB | `mongo` | `admin` | Port: `27017 (内)/2701 (外)`<br>用户: `root` / `123456` | - | - | - | admin 用户 |
| SQL Server | `mssql` | - | Port: `1433 (内)/2143 (外)`<br>用户: `sa` / `Share123456!` | - | - | - | arm64 compose 使用 `linux/amd64` 镜像 |
| DB2 | `db2` | `devtesterdb` | Port: `50000 (内)/2500 (外)`<br>用户: `db2inst1` / `123456` | - | - | - | arm64 compose 使用 `linux/amd64` 镜像 |
| ClickHouse HTTP | `clickhouse` | `default` | Port: `8123 (内)/2812 (外)`<br>用户: `root` / `password123` | - | - | - | HTTP 端口 |
| ClickHouse Native | `clickhouse` | `default` | Port: `9000 (内)/2900 (外)`<br>用户: `root` / `password123` | - | - | - | Native 端口 |

通过 SSH 通道访问这些数据源时，数据源 Host 使用 compose 服务名，例如 `mysql`、`postgres`、`oracle`，端口使用容器内端口。不要把数据源 Host 写成 `127.0.0.1`，因为在 SSH 转发场景下它表示 SSH Server 容器自身。

## SSL 数据源配置方式

MySQL、Oracle 和 PostgreSQL 共用 `tests/dbs/certs` 根目录下的 CA、服务端证书和客户端证书。

公共证书文件：

| 文件 | 类型 | 密码 | 用途 |
| --- | --- | --- | --- |
| `ca.crt` | PEM 文本 | - | CA 证书，适合 PostgreSQL JDBC/命令行 |
| `ca.p12` | PKCS#12 TrustStore | 空密码 | CA 证书 TrustStore，适合 MySQL、Oracle JDBC |
| `ca-123456.p12` | PKCS#12 TrustStore | `123456` | CA 证书 TrustStore 兼容版本 |
| `ca.jks` | JKS TrustStore | `123456` | CA 证书 JKS 兼容版本 |
| `client.crt` | PEM 文本 | - | 客户端证书，适合 PostgreSQL JDBC/命令行 |
| `client.key` | PEM 文本 | - | 客户端私钥，适合命令行验证 |
| `client.pk8` | PKCS#8 DER | - | 客户端私钥，适合 PostgreSQL JDBC/CloudDM |
| `client.p12` | PKCS#12 KeyStore | `123456` | 客户端证书和私钥，适合 MySQL、Oracle JDBC |
| `client.jks` | JKS KeyStore | `123456` | 客户端证书和私钥 JKS 兼容版本 |
| `server.crt` | PEM 文本 | - | 服务端证书，容器启动时使用 |
| `server.key` | PEM 文本 | - | 服务端私钥，容器启动时使用 |

SSL 模式含义：

| SSL 模式 | 含义 | 需要的证书字段 |
| --- | --- | --- |
| `DISABLED` | 不启用 SSL，使用普通 TCP 连接 | 不需要上传证书 |
| `TRUST` | 启用 SSL，但不要求用户上传 CA 证书 | 不需要上传证书 |
| `CA` | 单向 SSL，客户端校验服务端证书链 | 上传 CA 证书 |
| `CLIENT_CERT` | 双向 SSL，客户端校验服务端证书链，服务端校验客户端证书 | 上传 CA 证书、客户端证书/私钥或客户端 KeyStore |

Oracle x86 compose 的实例 SID 是 `XE`，arm64 compose 的实例 SID 是 `FREE`。两套 compose 都通过 `ORACLE_DATABASE=DEVTESTDB` 创建 PDB，`APP_USER=devtester` 创建在 PDB `DEVTESTDB` 中。

Oracle 连接标识和账号：

| 连接方式 | 连接值 | 可用账号 | 密码 | 用途 |
| --- | --- | --- | --- | --- |
| `SID` | x86: `XE`<br>arm64: `FREE` | `SYSTEM` | `123456` | 连接 CDB/实例 |
| `Service Name` | `DEVTESTDB` | `devtester` | `123456` | 连接业务 PDB |
| `PDB` | `DEVTESTDB` | `devtester` | `123456` | 连接业务 PDB |

`devtester` 不能使用 `SID` 连接。使用 `devtester / 123456` 时，连接方式必须选择 `Service Name` 或 `PDB`，连接值必须填写 `DEVTESTDB`。

Oracle 测试库已设置 `FAILED_LOGIN_ATTEMPTS UNLIMITED`，`devtester` 不会因为连续登录失败被锁定。已初始化过的容器会在每次启动时重置 `devtester` 密码并执行 `ACCOUNT UNLOCK`。

Oracle 端口：

| 场景 | Host | Port | SSL 模式 |
| --- | --- | --- | --- |
| 宿主机普通连接 | `127.0.0.1` | `2521` | 禁用 |
| compose 网络普通连接 | `oracle` | `1521` | 禁用 |
| 宿主机 TCPS 单向 | `127.0.0.1` | `2484` | CA 证书/单向 |
| compose 网络 TCPS 单向 | `oracle` | `2484` | CA 证书/单向 |
| 宿主机 TCPS 双向 | `127.0.0.1` | `2485` | 双向 |
| compose 网络 TCPS 双向 | `oracle` | `2485` | 双向 |

Oracle TCPS 证书：

| SSL 模式 | CA KeyStore | CA KeyStore 密码 | 客户端 KeyStore | 客户端 KeyStore 密码 |
| --- | --- | --- | --- | --- |
| CA 证书/单向 | `certs/ca.p12` | 留空 | - | - |
| 双向 | `certs/ca.p12` | 留空 | `certs/client.p12` | `123456` |

Oracle listener 使用 `tests/dbs/oracle/wallet` 中预置的 auto-login wallet。该 wallet 使用 `tests/dbs/certs` 的服务端证书生成，2484 listener 不要求客户端证书，2485 listener 要求客户端证书。

Oracle listener wallet 文件：

| 文件 | 用途 |
| --- | --- |
| `oracle/wallet/cwallet.sso` | 预生成 listener auto-login wallet，容器启动时挂载 |
| `oracle/wallet/ewallet.p12` | 预生成 listener wallet，容器启动时挂载 |

## SSH Server

SSH Server 用于验证密码、私钥、私钥加密码短语，以及 SSH 端口转发。

| 配置项 | 值 |
| --- | --- |
| 宿主机 Host | `127.0.0.1` |
| 宿主机 Port | `2022` |
| 容器服务名 | `ssh_server` |
| 容器内 Port | `22` |
| 用户名 | `sshuser` |
| 密码 | `123456` |
| 私钥 | `ssh/id_rsa` |
| 带密码短语私钥 | `ssh/id_rsa_passphrase` |
| 密码短语 | `passphrase123` |

本地命令验证：

```bash
ssh -p 2022 sshuser@127.0.0.1
ssh -i tests/dbs/ssh/id_rsa -p 2022 sshuser@127.0.0.1
ssh -i tests/dbs/ssh/id_rsa_passphrase -p 2022 sshuser@127.0.0.1
```

SSH Server 已通过 `tests/dbs/ssh/10-enable-tcp-forwarding.sh` 设置 `Port 22` 和 `AllowTcpForwarding yes`，可以验证端口转发：

```bash
ssh -N -L 13306:mysql:3306 -p 2022 sshuser@127.0.0.1
mysql -h 127.0.0.1 -P 13306 -uroot -p123456
```

这里 `mysql:3306` 是 SSH Server 容器所在 compose 网络内的目标地址。

## CloudDM SSH 通道配置示例

密码方式：

| 配置项 | 值 |
| --- | --- |
| SSH Host | `127.0.0.1` |
| SSH Port | `2022` |
| 用户名 | `sshuser` |
| 认证方式 | 密码 |
| 密码 | `123456` |
| 代理 | 无代理 |

私钥方式：

| 配置项 | 值 |
| --- | --- |
| SSH Host | `127.0.0.1` |
| SSH Port | `2022` |
| 用户名 | `sshuser` |
| 认证方式 | 密钥对 |
| 私钥Key | `tests/dbs/ssh/id_rsa` 的私钥内容 |
| 密码短语 | 留空 |

私钥加密码短语方式：

| 配置项 | 值 |
| --- | --- |
| SSH Host | `127.0.0.1` |
| SSH Port | `2022` |
| 用户名 | `sshuser` |
| 认证方式 | 密钥对 |
| 私钥Key | `tests/dbs/ssh/id_rsa_passphrase` 的私钥内容 |
| 密码短语 | `passphrase123` |

使用 SSH 通道访问 MySQL 时，数据源配置为：

| 配置项 | 值 |
| --- | --- |
| 数据源 Host | `mysql` |
| 数据源 Port | `3306` |
| 用户名 | `root` |
| 密码 | `123456` |
| Database | `devtester` |

## 代理服务

代理服务使用 `3proxy/3proxy:latest` 镜像，配置文件挂载到 `/etc/3proxy/3proxy.cfg`。当前提供两组代理服务：`proxy` 无认证，`proxy_auth` 使用账号密码认证。

| 服务 | 认证 | 代理类型 | 宿主机地址 | 容器网络地址 | 用户名 | 密码 |
| --- | --- | --- | --- | --- | --- | --- |
| `proxy` | 无 | HTTP | `127.0.0.1:2312` | `proxy:3128` | - | - |
| `proxy` | 无 | SOCKS4/SOCKS5 | `127.0.0.1:2108` | `proxy:1080` | - | - |
| `proxy_auth` | 账号密码 | HTTP | `127.0.0.1:2313` | `proxy_auth:3128` | proxyuser | 123456 |
| `proxy_auth` | 账号密码 | SOCKS5 | `127.0.0.1:2109` | `proxy_auth:1080` | proxyuser | 123456 |

从宿主机上的 CloudDM/Sidecar 通过代理访问 SSH Server 时，CloudDM/Sidecar 先连接宿主机暴露的代理端口，再由代理容器连接 SSH Server。链路是 `127.0.0.1:2312/2108 -> ssh_server:22 -> 数据源服务名:容器内端口`。为了明确验证是否经过代理，SSH Host 使用容器网络内的 SSH 服务地址：

| 配置项 | 值 |
| --- | --- |
| SSH Host | `ssh_server` |
| SSH Port | `22` |
| 代理类型 | HTTP、SOCKS4 或 SOCKS5 |
| 代理主机 | `127.0.0.1` |
| 代理端口 | 无认证：HTTP 使用 `2312`，SOCKS4/SOCKS5 使用 `2108`；账号密码认证：HTTP 使用 `2313`，SOCKS5 使用 `2109` |
| 代理认证 | 按上表选择无认证或账号密码 |

### 通过代理和 SSH 访问 MySQL

该例子用于验证完整链路：`CloudDM/Sidecar -> 127.0.0.1:2108 -> proxy:1080 -> ssh_server:22 -> mysql:3306`。

SSH 通道配置：

| 配置项 | 值 |
| --- | --- |
| SSH Host | `ssh_server` |
| SSH Port | `22` |
| 用户名 | `sshuser` |
| 认证方式 | 密码 |
| 密码 | `123456` |
| 代理类型 | SOCKS5 |
| 代理主机 | `127.0.0.1` |
| 代理端口 | `2108` |
| 代理认证 | 无 |

数据源配置：

| 配置项 | 值 |
| --- | --- |
| 数据源 Host | `mysql` |
| 数据源 Port | `3306` |
| 用户名 | `root` |
| 密码 | `123456` |
| Database | `devtester` |

如果要验证带账号密码的代理，只需要把代理配置改为：

| 配置项 | 值 |
| --- | --- |
| 代理类型 | SOCKS5 |
| 代理主机 | `127.0.0.1` |
| 代理端口 | `2109` |
| 代理认证 | 账号密码 |
| 代理用户名 | `proxyuser` |
| 代理密码 | `123456` |
