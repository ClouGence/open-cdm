# CloudDM 项目结构

## 1. 启动总览

### 1.1 Console 启动入口

- 启动类：`com.clougence.clouddm.console.web.DmConsoleLauncher`
- 源码位置：`clouddm-platform/cgdm-console/src/main/java/com/clougence/clouddm/console/web/DmConsoleLauncher.java`

行为说明：

- 开发调试时，通常直接以 `main(String[] args)` 作为 IDEA 运行入口。
- 如果 `main` 没有传参，会默认补成 `start`。
- 如果给 `main` 增加 `fix` 参数，会执行数据库更新脚本。
- 正式运行时，不是直接走开发用的 `main(String[] args)`，而是通过 `ClassWorld` 调用 `main(String[] args, ClassWorld world)`。

支持的动作参数：

- `start`：启动 Console
- `stop`：停止 Console
- `fix`：执行数据库修复或更新脚本

### 1.2 Sidecar 启动入口

- 启动类：`com.clougence.clouddm.boot.DmWorkerLauncher`
- 源码位置：`clouddm-platform/cgdm-sidecar/src/main/java/com/clougence/clouddm/worker/DmWorkerLauncher.java`

行为说明：

- 开发调试时，通常直接以 `main(String[] args)` 作为 IDEA 运行入口。
- 如果 `main` 没有传参，会默认补成 `start`。
- 正式运行时，会通过 `ClassWorld` 调用 `main(String[] args, ClassWorld world)`。

支持的动作参数：

- `start`：启动 Sidecar
- `stop`：停止 Sidecar

## 2. 开发时怎么启动

### 2.1 只调 Console 页面和后端逻辑

如果当前开发内容不访问真实数据源，只需要启动 Console：

- 运行 `DmConsoleLauncher`

典型场景：

- 页面开发
- 接口联调
- 权限、菜单、配置页调试
- 不触发真实数据源访问的后端逻辑调试

### 2.2 涉及数据源访问的联调

如果当前功能会访问数据源，则需要同时启动：

- `DmConsoleLauncher`
- `DmWorkerLauncher`

原因：

- Console 负责管理端页面与接口入口
- Sidecar 负责实际的数据源访问与相关执行链路

经验规则：

- 不访问数据源，只启动 Console 即可。
- 访问数据源，Console 和 Sidecar 两个都要启动。

## 3. IDEA 调试建议

### 3.1 Console 调试

推荐直接在 IDEA 中以 `DmConsoleLauncher.main(String[] args)` 作为运行或调试入口。

常用参数：

- 不填参数：等价于 `start`
- `start`：正常启动 Console
- `fix`：执行数据库更新脚本

适用场景：

- 日常开发启动
- 断点调试 Controller、Service、Spring 初始化逻辑
- 本地验证数据库修复脚本

### 3.2 Sidecar 调试

推荐直接在 IDEA 中以 `DmWorkerLauncher.main(String[] args)` 作为运行或调试入口。

常用参数：

- 不填参数：等价于 `start`
- `start`：正常启动 Sidecar

适用场景：

- 断点调试数据源连接、驱动加载、Sidecar 执行链路

### 3.3 正式运行方式说明

开发调试时通常直接运行 `main(String[] args)`。

正式运行时，启动入口会通过 `ClassWorld` 调用：

- `DmConsoleLauncher.main(String[] args, ClassWorld world)`
- `DmWorkerLauncher.main(String[] args, ClassWorld world)`

因此：

- 本地开发主要关心普通 `main`
- 排查正式环境启动链路问题时，需要关注 `ClassWorld` 入口

## 4. 前端和项目构建

根目录提供统一构建脚本：`all_build.sh`

文件位置：`all_build.sh`

### 4.1 只构建前端资源

适用于：

- 仅修改了 `cgdm-web` 前端代码
- 需要快速生成最新静态资源给 Console 使用

命令：

```bash
./all_build.sh web
```

当前实现等价于执行：

```bash
./gradlew :cgdm-web:processResources --parallel --max-workers=8
```

说明：

- 这是前端开发最常用的快速构建方式。
- 构建完成后，通常需要重启 Console 让最新静态资源生效。

### 4.2 全量构建

适用于：

- 需要完整构建 CloudDM
- 需要执行本地发布流程
- 需要验证后端、前端及安装产物联动

命令：

```bash
./all_build.sh all
```

不传参数时，默认也是全量构建：

```bash
./all_build.sh
```

脚本会执行的核心流程包括：

- `./gradlew clean`
- `./gradlew -Pprofile=dev -Ptarget=none buildx local -x test --rerun-tasks --parallel --max-workers=8`
- `./gradlew publishToMavenLocal --parallel --max-workers=8`

## 5. 打包入口

打包相关脚本集中在根目录 `package/` 下。

### 5.1 辅助构建工具入口

- 脚本：`package/buildTools.sh`
- 作用：通过 `buildTools` 子工程执行构建辅助命令

当前脚本内容等价于：

```bash
../gradlew run --args="$*" --settings-file ./buildTools/settings.gradle
```

它主要用于：

- 下载驱动
- 下载第三方运行时资源
- 解压与预处理打包依赖

### 5.2 Desktop/Electron 打包入口

- 脚本：`package/build_electron.sh`

用途：

- 构建 Desktop 相关 Java 产物
- 准备第三方运行时与驱动资源
- 触发 Electron 的平台打包流程

这个脚本会串起以下几类工作：

- 读取版本号与构建标识
- 下载驱动和 CGR 运行时资源
- 执行桌面端相关 Gradle 构建
- 进入 `package/electron` 执行 `npm install` 和各平台打包命令

可见它属于发布/产物打包入口，不属于日常页面开发的常规启动方式。

### 5.3 平台资源目录

`package/package/` 下包含按平台拆分的资源目录：

- `linux/`
- `macos/`
- `win/`
- `tools/`

这些目录主要服务于发布打包，而不是本地日常调试。

## 6. 常用开发账号

测试时可使用以下账号：

- 登录账号：`test@clougence.com`
- 密码：`clougence2021`

如果页面要求输入短信认证码或认证码，统一使用：

- `777777`

## 7. 常见开发操作建议

### 7.1 只改前端页面

推荐流程：

1. 启动 `DmConsoleLauncher`
2. 修改前端代码
3. 执行 `./all_build.sh web`
4. 重启 `DmConsoleLauncher`
5. 使用测试账号登录验证

### 7.2 改了数据源访问链路

推荐流程：

1. 启动 `DmConsoleLauncher`
2. 启动 `DmWorkerLauncher`
3. 在 IDEA 中对目标代码打断点
4. 使用测试账号登录联调

### 7.3 需要执行数据库更新脚本

推荐流程：

1. 在 IDEA 中运行 `DmConsoleLauncher`
2. 给 `main` 增加参数：`fix`
3. 执行完成后，再按正常方式用 `start` 启动 Console

## 8. 入口速查

### 启动入口

- Console：`com.clougence.clouddm.console.web.DmConsoleLauncher`
- Sidecar：`com.clougence.clouddm.boot.DmWorkerLauncher`

### 构建入口

- 前端快速构建：`./all_build.sh web`
- 全量构建：`./all_build.sh all`

### 打包入口

- 构建工具：`package/buildTools.sh`
- Desktop/Electron 打包：`package/build_electron.sh`
