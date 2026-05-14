# Contributing to CloudDM

感谢您对 **CloudDM** 的关注与支持！我们欢迎任何形式的贡献，包括但不限于提交 Bug 报告、功能建议、代码贡献、文档改进等。

## 目录

- [行为准则](#行为准则)
- [如何贡献](#如何贡献)
  - [报告 Bug](#报告-bug)
  - [提交功能建议](#提交功能建议)
  - [代码贡献](#代码贡献)
- [开发环境搭建](#开发环境搭建)
- [代码规范](#代码规范)
- [提交 PR 的流程](#提交-pr-的流程)
- [Commit 规范](#commit-规范)
- [分支管理](#分支管理)
- [常见问题](#常见问题)

## 行为准则

所有参与本项目的贡献者都应遵守基本的开源合作精神，保持尊重、友善和建设性的沟通。请参考 [Code of Conduct](CODE_OF_CONDUCT.md)（若有）了解更多细节。

## 如何贡献

### 报告 Bug

如果您发现了一个 Bug，请通过 GitHub/Gitee Issues 提交，并尽量包含以下信息：

- 清晰的标题和描述
- 复现步骤（可包含代码片段）
- 期望行为与实际行为
- 运行环境信息（操作系统、JDK 版本、数据库类型等）
- 相关日志或截图

### 提交功能建议

我们欢迎功能建议。请提交 Issue 并在标题前添加 `[Feature]` 前缀，说明：

- 该功能解决什么场景下的问题
- 期望的行为或效果
- 是否有类似实现可以参考

### 代码贡献

1. **Fork** 本仓库到你的 GitHub/Gitee 账号
2. 创建你的特性分支（见[分支管理](#分支管理)）
3. 编写代码并确保通过现有测试
4. 提交 Pull Request

## 开发环境搭建

### 前置要求

- **JDK 21+**
- **Gradle 9.5.0+**
- **Node.js 22.22.1**
- **IntelliJ IDEA** 或 Eclipse（推荐 IntelliJ IDEA）
- **Git**
- **Linux / macOS** 或带 Bash 的 Windows 环境
- **MySQL 8.0+**（用于运行时数据库）

### 本地编译

```bash
# 完整构建（包含前端资源）
./all_build.sh

# 仅更新前端资源
./all_build.sh web

# 编译并生成 tgz 安装包
cd package && ./package.sh --build

# 编译、生成 tgz 安装包，并构建所有架构的 Docker 镜像与部署清单
cd package && ./package.sh --build --docker

# 编译、生成 tgz 安装包，并仅构建指定架构
cd package && ./package.sh --build --docker arm64
cd package && ./package.sh --build --docker x86_64

# 若 package/build 下已有 tgz 安装包，可仅构建 Docker 镜像与部署清单
cd package && ./package.sh --docker
cd package && ./package.sh --docker arm64
cd package && ./package.sh --docker x86_64
```

> **版本号**：定义在 `gradle.properties` 的 `cg.clouddm.main.version` 字段中。
>
> **构建输出目录**：`package/build`。其中会生成 `cgdm-*.tar.gz`、`docker-*.tar`、`docker-*.yml` 和 `k8s-*.yml`。

### 项目结构

#### 顶层模块

| 模块 | 说明 |
|------|------|
| `clouddm-boot/` | Console、Sidecar、Alone 启动入口，以及初始化升级模块 |
| `clouddm-platform/` | Console、Web、Sidecar、插件装载和共享平台能力 |
| `clouddm-plugins/` | 数据源插件、认证 Provider、功能插件和内部扩展 |
| `clouddm-utils/` | 独立的模块、框架、工具 |
| `package/` | tgz 打包、Docker 镜像、compose 模板和交付输出 |

### 导入 IDEA

1. 克隆项目后，使用 IntelliJ IDEA 打开项目根目录
2. IDEA 将自动识别 Gradle 项目并开始导入
3. 等待依赖下载完成后即可开始开发

## 代码规范

### Java 代码格式

- 项目使用 Eclipse 代码格式化配置，文件位于 `codeformat.xml`
- IntelliJ IDEA 用户可安装 **Eclipse Code Formatter** 插件，导入 `codeformat.xml`
- 缩进：4 个空格
- 编码：UTF-8
- 保持统一文件头，添加版权说明。

### 编程风格

- 数据库实体使用 `DO` 后缀，表单对象使用 `FO` 后缀，请求响应数据使用 `VO` 后缀。
- 沿用已有命名模式，避免引入新的命名体系。
- 按职责拆分包结构，优先沿用 `controller`、`service`、`dal`、`model`、`config`、`util`、`global` 等分层。
- 优先使用项目基础设施 `com.clougence.utils` 及现有 SDK / 平台工具类，不重复封装相同功能。
- 统一使用 Lombok 的 `@Slf4j`，错误日志同时输出上下文信息和异常堆栈，避免无意义的异常。
- 收紧可见性，按需暴露 API，默认使用 `private`；谨慎使用 `public` 修饰符。
- 严格遵守单一职责、代码包要保证稳定单向依赖，禁止循环依赖
- 除特别情况，不应该出现巨型类和方法。每个类格式化后控制在 500 行左右。
- 不要过度拆分小方法和零碎类。

### 测试

- 新增功能应包含相应的单元测试或集成测试
- 提交前确保 `./all_build.sh` 构建通过

## 提交 PR 的流程

1. **确保 Fork 的仓库与上游同步**
2. **创建一个新的分支**（见下方分支管理）
3. **编写代码并提交**
4. **运行测试**，确保全部通过
5. **提交 Pull Request** 到目标分支
   - PR 标题清晰描述变更内容
   - PR 描述中关联相关 Issue（如有）
   - 说明变更类型（bugfix / feature / refactor / docs 等）
6. **等待 Review**，并根据反馈进行修改
7. **合并**后分支将被删除

## Commit 规范

建议使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <subject>

<body>
```

常见类型：

| 类型 | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | 修复 Bug |
| `docs` | 文档变更 |
| `refactor` | 重构 |
| `test` | 测试相关 |
| `chore` | 构建/工具链变更 |
| `style` | 代码格式（不影响功能） |

示例：

```
feat(mysql): support prepared statement for MySQL driver
fix(oracle): resolve NPE when connection is null
docs: update README with quick start guide
```

## 分支管理

- `main` / `master`：稳定发布分支
- `dev`：开发主分支
- `feat/<feature-name>`：功能开发分支
- `fix/<bug-name>`：Bug 修复分支

## 常见问题

### 如何添加新的数据库支持？

参考 `clouddm-plugins/clouddm-ds/` 下已有数据源实现（如 `ds-mysql`），创建一个新的数据源插件模块，并在 `settings.gradle` 中注册。

### 如何自定义审核规则？

项目支持通过规则脚本自定义扩展，具体请参考官方文档。

---

再次感谢您的贡献！如有任何问题，欢迎通过 Issue 与我们联系。
