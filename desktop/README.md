# CloudDM Desktop

CloudDM macOS 桌面端，使用 Electron 打包为 `.dmg` 安装包。

## 快速开始

```bash
# 在仓库根目录执行
./build-desktop.sh
```

产物：`dist/CloudDM-<version>-macOS-arm64.dmg`

### 增量打包（仅桌面壳 / Electron）

只改了 `desktop/` 或 Electron 相关配置，且后端、前端已经编好时，可跳过 Gradle 与前端构建：

```bash
# 只改桌面壳 / Electron，后端和前端已编好
./build-desktop.sh --skip-build
```

前提：`package/build/cgdm-alone.tar.gz` 与 `frontend/dist/` 已存在且为最新产物。

## 前置条件

- macOS 12+
- JDK 17+
- Node.js 22+
- **可用内存建议 ≥ 16GB**；磁盘空间建议 ≥ 15GB（Gradle 编译 Oracle 语法解析器等大文件较吃内存）


## 安装

1. 双击 `.dmg` 挂载
2. 将 `CloudDM.app` 拖入 `Applications`
3. 首次打开：**右键 CloudDM.app → 打开**（未签名，需绕过 Gatekeeper）

## 默认账号

首次启动会在后台自动完成初始化。完成后使用以下账号登录：

| 用途 | 账号 | 密码 |
|------|------|------|
| CloudDM 登录 | `admin` | `123456` |

内置 MySQL（仅本机 3307，供应用自用，一般无需手动连接）root 密码为 `cgdm`。

删除 `~/.cgdm-desktop` 后重新打开应用会再次自动初始化，账号密码不变。

## 工作原理

启动时 Electron 自动完成：

1. 在 `~/.cgdm-desktop/runtime/` 建立可写运行时目录（libs/plugins/bin 软链到 app 包内资源）
2. 启动内置 MySQL（127.0.0.1:3307）
3. 初始化 root 账号并创建 `cdmgr` 库
4. 启动 Java 后端（127.0.0.1:18222）
5. 首次运行自动完成 schema 初始化
6. 后端就绪后打开应用窗口

关闭窗口时自动停止 Java 和 MySQL 进程。

### 端口规划

| 服务 | 桌面端 | 本地开发 |
|------|--------|----------|
| Web | 18222 | 8222 |
| MySQL | 3307 | 3306 |
| RSocket | 18008 | 8008 |

桌面端与本地开发环境可同时运行，互不冲突。

## 数据目录

数据库、日志、配置文件均存储在 `~/.cgdm-desktop/`：

```
~/.cgdm-desktop/
├── runtime/conf/   # 应用配置（可写，init 会更新 alone.properties）
├── mysql_data/     # MySQL 数据
├── mysql_run/      # MySQL socket / pid
├── logs/           # 运行日志
│   ├── java.log    # Java 后端日志（排查启动问题首选）
│   ├── alone.log   # 后端业务日志
│   └── mysqld.log  # MySQL 错误日志
└── data/           # 应用数据
```

卸载 `.app` 不会删除此目录，重新安装后数据仍在。如需彻底清除（例如升级后 JDBC/端口配置变更），手动删除该目录后重新打开应用。

## 故障排查

启动失败时查看：

```bash
tail -100 ~/.cgdm-desktop/logs/java.log
tail -50 ~/.cgdm-desktop/logs/mysqld.log
```

常见问题：

- **JDK 未安装**：安装 OpenJDK 17，例如 `brew install openjdk@17`
- **端口被占用**：关闭占用 18222/3307/18008 的进程，或删除 `~/.cgdm-desktop` 后重试
- **旧版配置残留**：删除 `~/.cgdm-desktop` 目录，让应用重新初始化

## 目录说明

```
desktop/
├── main.js              # Electron 主进程
├── preload.js           # 预加载脚本
├── loading.html         # 启动页
├── package.json         # 依赖声明
├── electron-builder.yml # 打包配置
├── assets/              # 图标（构建时生成）
└── scripts/
    ├── download-mysql.sh # 下载 MySQL macOS 版
    └── generate-icon.py  # 生成应用图标
```
