# 数据库 Docker 测试环境

`tests/dbs` 提供可直接用于开发和回归测试的数据库、SSH Server 及 HTTP/SOCKS5 代理。

连接规则：

- 从宿主机或其它机器连接：地址填写实际 Docker 宿主机地址，端口使用表中的“外”端口。
- 从同一 Compose 网络内连接：地址填写“容器服务名”，端口使用表中的“内”端口。
- 本文不固定填写宿主机 IP，因为宿主机地址可能变化。

## 启动和停止

在仓库根目录执行：

```bash
# 查看支持的数据源和版本
tests/dbs/init.sh

# 启动全部可用服务
tests/dbs/init.sh -all

# 每类数据库只启动最新版本
tests/dbs/init.sh -simple

# 启动某类数据库的全部版本
tests/dbs/init.sh -ds mysql

# 启动指定版本
tests/dbs/init.sh -ds mysql 8.0

# 查看状态
docker compose -f tests/dbs/docker-compose.yml ps

# 停止服务并保留数据
docker compose -f tests/dbs/docker-compose.yml down

# 删除本工具定义的全部容器、网络和本地数据库数据
tests/dbs/init.sh clean
```

## 数据目录和迁移

数据库数据使用宿主机目录挂载，不依赖 Docker named volume。默认数据根目录是
`tests/dbs/data`，每个数据库版本使用独立子目录，例如 `data/mysql/8.0`、
`data/oracle/23`。停止、删除或重建容器不会删除这些数据。

要把数据放到其它磁盘，启动前设置 `DBS_DATA_DIR`：

```bash
export DBS_DATA_DIR=/mnt/open-cdm-dbs-data
tests/dbs/init.sh -simple
```

相对路径以 `tests/dbs` 为基准。`init.sh` 会在数据根目录写入管理标记，防止 `clean`
误删其它目录。不要让多个正在运行的 Compose 环境共用同一个数据根目录。

迁移到另一台机器时，必须先停止全部数据库，再保存目录及其数字 UID/GID：

```bash
# 源机器：默认数据目录
docker compose -f tests/dbs/docker-compose.yml down
sudo tar --numeric-owner -C tests/dbs -czf /tmp/dbs-data.tar.gz data

# 把仓库和 dbs-data.tar.gz 复制到目标机器后
sudo tar --numeric-owner -C tests/dbs -xzf /tmp/dbs-data.tar.gz
tests/dbs/init.sh -simple
```

使用自定义 `DBS_DATA_DIR` 时，对该目录执行同样的停止、打包和还原操作，并在目标机器设置
新的 `DBS_DATA_DIR` 后运行 `init.sh`。目标机器必须使用相同数据库版本和兼容的 CPU 架构。
镜像不包含在数据目录中，可公开拉取的镜像由 `init.sh` 准备，本地归档镜像需要单独复制和加载。
HANA 1 的许可证与 hardware key 有关，换机器后可能需要重新申请并安装许可证。

TiDB 4.0 的 `mocktikv` 数据通过其落盘目录 `/tmp/tidb` 持久化。

`clean` 会删除全部容器、网络、旧版遗留 Docker 卷以及当前 `DBS_DATA_DIR`。该操作会删除
所有数据库数据，用于彻底重置环境；只想停止服务并保留数据时使用普通的 `down`。

## 普通连接信息

端口统一写为 `容器内端口(内)/容器外端口(外)`；账号密码统一写为 `账号 / 密码`。

| 数据源 | 版本 | 容器服务名 | 连接方式/端口 | 数据库/服务 | 账号/密码 |
| --- | --- | --- | --- | --- | --- |
| MySQL | 5.6.51 | `ds-mysql-56` | MySQL：`3306(内)/23356(外)` | `devtester` | `root / 123456` |
| MySQL | 5.7.44 | `ds-mysql-57` | MySQL：`3306(内)/23357(外)` | `devtester` | `root / 123456` |
| MySQL | 8.0.46 | `ds-mysql-80` | MySQL：`3306(内)/2330(外)` | `devtester` | `root / 123456` |
| MySQL | 8.4.10 | `ds-mysql-84` | MySQL：`3306(内)/23384(外)` | `devtester` | `root / 123456` |
| MySQL | 9.7.1 | `ds-mysql-97` | MySQL：`3306(内)/23397(外)` | `devtester` | `root / 123456` |
| TiDB | 4.0.16 | `ds-tidb-40` | MySQL：`4000(内)/24040(外)` | `test` | `root / (空)` |
| TiDB | 5.4.3 | `ds-tidb-54` | MySQL：`4000(内)/24054(外)` | `test` | `root / (空)` |
| TiDB | 6.5.12 | `ds-tidb-65` | MySQL：`4000(内)/24065(外)` | `test` | `root / (空)` |
| TiDB | 7.5.7 | `ds-tidb-75` | MySQL：`4000(内)/24075(外)` | `test` | `root / (空)` |
| TiDB | 8.5.7 | `ds-tidb-85` | MySQL：`4000(内)/24085(外)` | `test` | `root / (空)` |
| Doris | 2.1.0 | `ds-doris-21` | MySQL：`9030(内)/29021(外)` | `(空)` | `root / (空)` |
| Doris | 2.1.9 | `ds-doris-219` | MySQL：`9030(内)/29029(外)` | `(空)` | `root / (空)` |
| Doris | 3.0.8 | `ds-doris-30` | MySQL：`9030(内)/29030(外)` | `(空)` | `root / (空)` |
| 达梦 | 8-20250506 | `ds-dameng-8` | DM：`5236(内)/25236(外)` | 服务 `DMSERVER` | `SYSDBA / Dameng123!` |
| PostgreSQL | 12.22 | `ds-postgres-12` | PostgreSQL：`5432(内)/25412(外)` | `postgres` | `postgres / 123456` |
| PostgreSQL | 13.23 | `ds-postgres-13` | PostgreSQL：`5432(内)/25413(外)` | `postgres` | `postgres / 123456` |
| PostgreSQL | 14.23 | `ds-postgres-14` | PostgreSQL：`5432(内)/25414(外)` | `postgres` | `postgres / 123456` |
| PostgreSQL | 15.18 | `ds-postgres-15` | PostgreSQL：`5432(内)/25415(外)` | `postgres` | `postgres / 123456` |
| PostgreSQL | 16.14 | `ds-postgres-16` | PostgreSQL：`5432(内)/2543(外)` | `postgres` | `postgres / 123456` |
| PostgreSQL | 17.10 | `ds-postgres-17` | PostgreSQL：`5432(内)/25417(外)` | `postgres` | `postgres / 123456` |
| PostgreSQL | 18.4 | `ds-postgres-18` | PostgreSQL：`5432(内)/25418(外)` | `postgres` | `postgres / 123456` |
| HANA | 1.00.122.01 | `ds-hana-1` | SYSTEMDB SQL：`30013(内)/23013(外)`<br>HXE SQL：`30015(内)/23015(外)` | SYSTEMDB<br>HXE | `SYSTEM / Hana@1234` |
| HANA | 2.00.082.00 | `ds-hana-2` | SYSTEMDB SQL：`39013(内)/23913(外)`<br>HXE SQL：`39041(内)/23941(外)` | SYSTEMDB<br>HXE | `SYSTEM / Devtester123!` |
| SQL Server | 2017 | `ds-sqlserver-2017` | TDS：`1433(内)/21417(外)` | `master` | `sa / Share123456!` |
| SQL Server | 2019 | `ds-sqlserver-2019` | TDS：`1433(内)/21419(外)` | `master` | `sa / Share123456!` |
| SQL Server | 2022 | `ds-sqlserver-2022` | TDS：`1433(内)/2143(外)` | `master` | `sa / Share123456!` |
| Oracle | 11.2.0.2 | `ds-oracle-11` | SID：`1521(内)/2511(外)` | SID `XE` | `devtester / 123456` |
| Oracle | 21.3.0 | `ds-oracle-21` | SID：`1521(内)/2522(外)`<br>Service Name：`1521(内)/2522(外)`<br>PDB：`1521(内)/2522(外)` | SID `XE`<br>Service `DEVTEST21`<br>PDB `DEVTEST21` | `SYSTEM / 123456`<br>`devtester / 123456`<br>`devtester / 123456` |
| Oracle | 23.26.2 | `ds-oracle-23` | SID：`1521(内)/2521(外)`<br>Service Name：`1521(内)/2521(外)`<br>PDB：`1521(内)/2521(外)` | SID `FREE`<br>Service `DEVTESTDB`<br>PDB `DEVTESTDB` | `SYSTEM / 123456`<br>`devtester / 123456`<br>`devtester / 123456` |
| StarRocks | 2.5.21 | `ds-starrocks-25` | MySQL：`9030(内)/23325(外)` | `(空)` | `root / (空)` |
| StarRocks | 3.5.20 | `ds-starrocks-35` | MySQL：`9030(内)/23335(外)` | `(空)` | `root / (空)` |
| StarRocks | 4.1.3 | `ds-starrocks-41` | MySQL：`9030(内)/23341(外)` | `(空)` | `root / (空)` |
| Db2 | 10.5.0.4 | `ds-db2-105` | Db2：`50000(内)/2505(外)` | Catalog `TESTDB`<br>Schema `TEST` | `db2inst1 / 123456` |
| Db2 | 11.5.9.0 | `ds-db2-115` | Db2：`50000(内)/2500(外)` | Catalog `TESTDB`<br>Schema `TEST` | `db2inst1 / 123456` |
| MariaDB | 10.11.16 | `ds-mariadb-1011` | MySQL：`3306(内)/23110(外)` | `devtester` | `root / 123456` |
| MariaDB | 11.4.10 | `ds-mariadb-114` | MySQL：`3306(内)/23111(外)` | `devtester` | `root / 123456` |
| MariaDB | 11.8.8 | `ds-mariadb-118` | MySQL：`3306(内)/23118(外)` | `devtester` | `root / 123456` |
| ClickHouse | 20.8.19.4 | `ds-clickhouse-208` | HTTP/JDBC：`8123(内)/28208(外)`<br>Native：`9000(内)/29208(外)` | `default`<br>`default` | `root / password123`<br>`root / password123` |
| ClickHouse | 22.8.21.38 | `ds-clickhouse-228` | HTTP/JDBC：`8123(内)/28228(外)`<br>Native：`9000(内)/29228(外)` | `default`<br>`default` | `root / password123`<br>`root / password123` |
| ClickHouse | 24.8.14.39 | `ds-clickhouse-248` | HTTP/JDBC：`8123(内)/2812(外)`<br>Native：`9000(内)/2900(外)` | `default`<br>`default` | `root / password123`<br>`root / password123` |
| ClickHouse | 26.6.2.81 | `ds-clickhouse-266` | HTTP/JDBC：`8123(内)/28266(外)`<br>Native：`9000(内)/29266(外)` | `default`<br>`default` | `root / password123`<br>`root / password123` |
| Redis | 7.2.3 | `redis-72` | Redis：`6379(内)/2637(外)` | DB `0` | `default / 123456` |
| MongoDB | 6.0.24 | `mongo-60` | MongoDB：`27017(内)/2701(外)` | `admin` | `root / 123456` |

Oracle 的 `devtester` 用户位于 PDB 中，只能通过对应的 Service Name 或 PDB 连接，不能通过 SID 连接。
Db2 10.5 需要本机已有归档镜像 `local/db2:10.5.0.4`。

## SSL 连接信息

SSL 表只列出本工具已经配置 SSL 的版本。`仅认证`、`单向认证`、`双向认证` 分别表示：

- 仅认证：建立加密连接，使用账号密码认证。
- 单向认证：客户端使用 CA 或 TrustStore 验证服务端证书。
- 双向认证：服务端证书验证之外，客户端还提交客户端证书和私钥。

| 数据源 | 版本 | 容器服务名 | SSL 连接方式/端口 | 数据库/服务 | 账号/密码 | 仅认证 | 单向认证 | 双向认证 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| MySQL | 8.0.46 | `ds-mysql-80` | MySQL TLS：`3306(内)/2330(外)` | `devtester` | `root / 123456`<br>双向：`sslclient / 123456` | 启用 TLS | TrustStore：`certs/ca.p12`<br>密码：`(空)` | TrustStore：`certs/ca.p12`，密码：`(空)`<br>KeyStore：`certs/client.p12`，密码：`123456` |
| TiDB | 8.5.7 | `ds-tidb-85` | MySQL TLS：`4000(内)/24085(外)` | `test` | `root / (空)` | 启用 TLS | CA：`certs/ca.crt` | — |
| PostgreSQL | 16.14 | `ds-postgres-16` | PostgreSQL TLS：`5432(内)/2543(外)` | `postgres` | `postgres / 123456`<br>双向：`sslclient / (空)` | 启用 TLS | CA：`certs/ca.crt` | CA：`certs/ca.crt`<br>客户端证书：`certs/client.crt`<br>客户端私钥：`certs/client.pk8` |
| SQL Server | 2022 | `ds-sqlserver-2022` | TDS TLS：`1433(内)/2143(外)` | `master` | `sa / Share123456!` | 启用加密并信任服务端 | CA：`certs/ca.crt`<br>证书主机名：`localhost` | — |
| Oracle | 23.26.2 | `ds-oracle-23` | TCPS Service Name：`2484(内)/2484(外)`<br>TCPS PDB：`2484(内)/2484(外)`<br>mTLS Service Name：`2485(内)/2485(外)`<br>mTLS PDB：`2485(内)/2485(外)` | Service `DEVTESTDB`<br>PDB `DEVTESTDB`<br>Service `DEVTESTDB`<br>PDB `DEVTESTDB` | `devtester / 123456`<br>`devtester / 123456`<br>`devtester / 123456`<br>`devtester / 123456` | 启用 TCPS | TrustStore：`certs/ca.p12`<br>密码：`(空)` | TrustStore：`certs/ca.p12`，密码：`(空)`<br>KeyStore：`certs/client.p12`，密码：`123456` |
| Db2 | 11.5.9.0 | `ds-db2-115` | Db2 TLS：`50001(内)/2501(外)` | Catalog `TESTDB`<br>Schema `TEST` | `db2inst1 / 123456` | 启用 SSL | CA：`certs/ca.crt` | — |
| MariaDB | 11.4.10 | `ds-mariadb-114` | MySQL TLS：`3306(内)/23111(外)` | `devtester` | `root / 123456`<br>双向：`sslclient / 123456` | 启用 TLS | CA：`certs/ca.crt` | CA：`certs/ca.crt`<br>客户端证书：`certs/client.crt`<br>客户端私钥：`certs/client.key` |
| ClickHouse | 24.8.14.39 | `ds-clickhouse-248` | HTTPS/JDBC：`8443(内)/2843(外)`<br>Native TLS：`9440(内)/2940(外)` | `default`<br>`default` | `root / password123`<br>`root / password123` | 启用 TLS | CA：`certs/ca.crt` | — |
| Redis | 7.2.3 | `redis-72` | Redis TLS：`6380(内)/2638(外)` | DB `0` | `default / 123456` | 启用 TLS | CA：`certs/ca.crt` | CA：`certs/ca.crt`<br>客户端证书：`certs/client.crt`<br>客户端私钥：`certs/client.key` |
| MongoDB | 6.0.24 | `mongo-60` | MongoDB TLS：`27017(内)/2701(外)` | `admin` | `root / 123456` | 启用 TLS | CA：`certs/ca.crt` | CA：`certs/ca.crt`<br>客户端证书：`certs/client.crt`<br>客户端私钥：`certs/client.key` |

## 证书和密码

证书路径均相对于 `tests/dbs`。

| 文件 | 格式 | 密码 | 用途 |
| --- | --- | --- | --- |
| `certs/ca.crt` | PEM | `(空)` | 单向和双向认证的 CA 证书 |
| `certs/ca.p12` | PKCS#12 TrustStore | `(空)` | Oracle 等需要 TrustStore 的客户端 |
| `certs/ca-123456.p12` | PKCS#12 TrustStore | `123456` | 需要非空 TrustStore 密码的客户端 |
| `certs/ca.jks` | JKS TrustStore | `123456` | 使用 JKS 的 Java 客户端 |
| `certs/client.crt` | PEM | `(空)` | 双向认证的客户端证书 |
| `certs/client.key` | PEM | `(空)` | 双向认证的客户端私钥 |
| `certs/client.pk8` | PKCS#8 DER | `(空)` | 要求 PKCS#8 DER 私钥的客户端 |
| `certs/client.p12` | PKCS#12 KeyStore | `123456` | Oracle 等需要 KeyStore 的客户端 |
| `certs/client.jks` | JKS KeyStore | `123456` | 使用 JKS 的 Java 客户端 |

这些证书和密码仅用于本地测试，不得用于生产环境。

## SSH 和代理信息

### SSH Server

| 容器服务名 | 连接方式/端口 | 账号/密码 | 私钥 | 带密码私钥 |
| --- | --- | --- | --- | --- |
| `ssh-server-103` | SSH：`22(内)/2022(外)` | `sshuser / 123456` | `ssh/id_rsa` | `ssh/id_rsa_passphrase`<br>密码短语：`passphrase123` |

通过 SSH 通道连接数据库时，数据库地址填写普通连接表中的容器服务名，端口填写内端口。

### HTTP/SOCKS5 代理

| 容器服务名 | 连接方式/端口 | 账号/密码 |
| --- | --- | --- |
| `proxy-097` | HTTP：`3128(内)/2312(外)`<br>SOCKS5：`1080(内)/2108(外)` | `(空) / (空)` |
| `proxy-auth-097` | HTTP：`3128(内)/2313(外)`<br>SOCKS5：`1080(内)/2109(外)` | `proxyuser / 123456` |

从宿主机或其它机器使用代理时，填写实际 Docker 宿主机地址和外端口；从 Compose 网络内使用时，填写容器服务名和内端口。
