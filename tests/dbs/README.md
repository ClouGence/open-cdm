# DB 测试资源使用说明

`tests/dbs` 用于启动 CloudDM 本地联调和测试所需的数据源、SSH Server、HTTP/SOCKS 代理服务。

## 目录结构

```text
tests/dbs
├── dbs_x86/docker-compose.yml      # x86 环境
├── dbs_arm64/docker-compose.yml    # arm64 环境
├── proxy/3proxy.cfg                # 无认证 3proxy 配置
├── proxy-auth/3proxy.cfg           # 账号密码认证 3proxy 配置
└── ssh/                            # SSH Server 使用的公私钥和初始化脚本
```

`dbs_x86` 和 `dbs_arm64` 的宿主机暴露端口保持一致，只是镜像平台不同。同一台机器上不要同时启动两套 compose，除非先修改其中一套端口。

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

宿主机直连时使用 `127.0.0.1` 和对外端口。

| 服务 | 容器内服务名 | 宿主机端口 | 容器内端口 | 用户名 | 密码 | 备注 |
| --- | --- | ---: | ---: | --- | --- | --- |
| MySQL | `mysql` | 2330 | 3306 | root | 123456 | database: `devtester` |
| Oracle | `oracle` | 2521 | 1521 | devtester | 123456 | service: `DEVTESTDB` |
| PostgreSQL | `postgres` | 2543 | 5432 | postgres | 123456 | database: `postgres` |
| Redis | `redis` | 2639 | 6379 | - | 123456 | `requirepass` |
| MongoDB | `mongo` | 2701 | 27017 | root | 123456 | admin 用户 |
| SQL Server | `mssql` | 2143 | 1433 | sa | Share123456! | 仅 x86 compose |
| DB2 | `db2` | 2500 | 50000 | db2inst1 | 123456 | 仅 x86 compose，database: `devtesterdb` |
| ClickHouse HTTP | `clickhouse` | 2812 | 8123 | root | password123 | database: `default` |
| ClickHouse Native | `clickhouse` | 2900 | 9000 | root | password123 | database: `default` |

通过 SSH 通道访问这些数据源时，数据源 Host 使用 compose 服务名，例如 `mysql`、`postgres`、`oracle`，端口使用容器内端口。不要把数据源 Host 写成 `127.0.0.1`，因为在 SSH 转发场景下它表示 SSH Server 容器自身。

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
| `proxy` | 无 | HTTP | `127.0.0.1:2080` | `proxy:3128` | - | - |
| `proxy` | 无 | SOCKS4/SOCKS5 | `127.0.0.1:2085` | `proxy:1080` | - | - |
| `proxy_auth` | 账号密码 | HTTP | `127.0.0.1:2081` | `proxy_auth:3128` | proxyuser | 123456 |
| `proxy_auth` | 账号密码 | SOCKS5 | `127.0.0.1:2086` | `proxy_auth:1080` | proxyuser | 123456 |

从宿主机上的 CloudDM/Sidecar 通过代理访问 SSH Server 时，CloudDM/Sidecar 先连接宿主机暴露的代理端口，再由代理容器连接 SSH Server。链路是 `127.0.0.1:2080/2085 -> ssh_server:22 -> 数据源服务名:容器内端口`。为了明确验证是否经过代理，SSH Host 使用容器网络内的 SSH 服务地址：

| 配置项 | 值 |
| --- | --- |
| SSH Host | `ssh_server` |
| SSH Port | `22` |
| 代理类型 | HTTP、SOCKS4 或 SOCKS5 |
| 代理主机 | `127.0.0.1` |
| 代理端口 | 无认证：HTTP 使用 `2080`，SOCKS4/SOCKS5 使用 `2085`；账号密码认证：HTTP 使用 `2081`，SOCKS5 使用 `2086` |
| 代理认证 | 按上表选择无认证或账号密码 |

### 通过代理和 SSH 访问 MySQL

该例子用于验证完整链路：`CloudDM/Sidecar -> proxy:1080 -> ssh_server:22 -> mysql:3306`。

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
| 代理端口 | `2085` |
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
| 代理端口 | `2086` |
| 代理认证 | 账号密码 |
| 代理用户名 | `proxyuser` |
| 代理密码 | `123456` |
