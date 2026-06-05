# AGENTS.md

本文件为 AI 编码代理在 open-cdm 仓库内工作时的规则说明，内容按 open-cdm 的实际工程组织和本地团队规则调整。

## 作用范围

- 本文件适用于当前目录为根的整个 open-cdm 仓库。
- 如果某个子目录下存在更深层的 `AGENTS.md`，则该子目录及其后代目录以内层文件为准。
- 本文件约束源码、文档、配置、脚本和测试变更；默认不要修改生成产物、依赖目录和构建输出，例如 `backend/**/build/`、`frontend/node_modules/`、`frontend/dist/`、`package/build/`。

## 项目定位

- open-cdm 是 CloudDM 的开源工程，面向团队数据库管理。
- 核心能力包括数据源管理、数据查询、权限控制、数据脱敏、SQL 审核、数据库 CI/CD、工单协作和多部署形态。
- 主要技术栈：
  - 后端：Java、JDK 21+、Gradle 9.5.0+、Spring / Spring Boot、MyBatis。
  - 前端：Vue 3、Vue CLI、JavaScript / TypeScript、Node.js 22.22.1。
  - 运行数据库：MySQL 8.0+。

## 项目结构

- `backend/`：后端 Gradle 根工程，包含启动模块、平台模块、插件模块、工具模块和测试模块。
- `backend/settings.gradle`：后端模块注册入口；新增插件或数据源模块时必须同步注册。
- `backend/clouddm-boot/`：启动与初始化模块，包括 `boot-alone`、`boot-console`、`boot-sidecar`、`boot-initialization`。
- `backend/clouddm-platform/`：平台核心模块，包括 API、DAO、Console、Sidecar、插件 SDK 和共享库。
- `backend/clouddm-plugins/`：数据源、认证 Provider、特性插件和内置插件。
- `backend/clouddm-utils/`：通用工具、驱动、RPC、schema、SQL / DSL 解析等基础能力。
- `backend/clouddm-platform/cgdm-dao/src/main/resources/mybatis/mapper/`：MyBatis XML mapper 目录。
- `backend/clouddm-boot/boot-initialization/src/main/java/com/clougence/clouddm/init/component/scripts/`：Flyway Java 初始化和升级脚本目录。
- `frontend/`：CloudDM Web 前端工程，也是后端 Gradle 中的 `cgdm-web` 模块。
- `frontend/src/locales/`：前端国际化文案目录。
- `package/`：统一构建、tgz 打包、Docker 镜像和部署清单生成入口。
- `docs/`：中英文文档与图片资源。
- `backend/gradle.properties`：主版本号 `cg.clouddm.main.version` 所在文件。

## 工作方式

- 以最小且正确的改动解决问题。
- 优先选择直接、清晰、可读的实现，不追求花哨抽象。
- 当多条规则冲突时，优先保证行为正确、状态一致、契约清晰。
- 不乱猜业务语义；拿不到明确数据时，不要擅自补业务对象、状态、内容或其他业务含义。
- 不胡乱兼容旧逻辑；已经明确删除的字段、分支、路径要清干净。
- 先阅读相关模块的现有实现，再决定改法；优先沿用本仓库已有模式、命名、工具类和基础设施。
- 工作区可能存在用户未提交改动；不要回滚、覆盖或重排与当前任务无关的改动。

## 构建与验证命令

### 环境要求

- JDK 21+
- Gradle 9.5.0+
- Node.js 22.22.1
- MySQL 8.0+
- Docker 仅在构建镜像或部署清单时需要

### 全量构建与打包

```bash
# Full build, including frontend assets.
cd package && ./all_build.sh

# Frontend web resources only.
cd package && ./all_build.sh web

# Rebuild one plugin package. Replace the module name as needed.
cd package && ./all_build.sh plugin plus-provider-ldap

# Compile and generate tgz install packages.
cd package && ./package.sh --build

# Compile, package, and build Docker images plus deployment manifests.
cd package && ./package.sh --build --docker

# Build Docker images for one architecture.
cd package && ./package.sh --build --docker arm64
cd package && ./package.sh --build --docker x86_64
```

### 后端

```bash
# Build from the backend Gradle root.
cd backend && ./gradlew -Pprofile=dev -Ptarget=none -PbuildFrontend=true buildx local -x test --parallel --max-workers=16

# Run all tests when the scope requires it.
cd backend && ./gradlew test

# Run one module's tests.
cd backend && ./gradlew :<module>:test

# Build one module.
cd backend && ./gradlew :<module>:build
```

- 本地调试通常打开 `backend/` 作为 Gradle 工程。
- 单机模式入口：`backend/clouddm-boot/boot-alone/src/main/java/com/clougence/clouddm/boot/DmAloneLauncher.java`。
- 启动后访问 `http://localhost:8222`；首次启动会进入初始化流程。

### 前端

```bash
cd frontend && npm install
cd frontend && npm run serve:dm
cd frontend && npm run build:dm
cd frontend && npm run lint
cd frontend && npm run lint-fix
cd frontend && npm run test:unit
cd frontend && npm run check-i18n
```

- 前端使用 `package-lock.json`，默认使用 `npm`，不要擅自切换到其他包管理器。
- 修改用户可见文案时，同步维护 `frontend/src/locales/`。

## 编码规则

- 不过度防御，不为了极低概率场景写复杂兜底逻辑。
- 允许基于模块边界、配置约束和框架契约建立合理信任；不要不看上下文就把所有值都当成任意脏输入。
- 不要为实际不可能出现的 `null`、空值或非法状态写复杂防御分支；防御逻辑只放在真实边界和不可信输入处。
- 避免长条件和层层 `if` 堆叠导致代码难读；如果必须校验，优先让边界、契约和数据结构保持清晰。
- 代码要干净直接，好读优先，不要为了抽象而抽象。
- 尽量不写三元表达式，能用 `if` 表达清楚的逻辑，优先使用 `if`。
- 不写没必要的小 helper，只有复用明显且能降低理解成本时才抽。
- 不为了测试扩大生产代码的 public API。
- 不要增加只为测试存在的生产方法。
- 没用到的字段、方法、分支、返回值要删掉。
- 方法、字段可见性要明确，能 `private` 就不要保留为默认或 `public`。
- 默认不要写内部类；能提成独立类就提成独立类。
- 类不要声明为 `final`，统一使用 `public`。
- 不要使用 `@SneakyThrows`；需要 `try/catch` 异常时，用简短 `msg` 描述错误，并通过 `log.error(msg, e)` 打印异常。
- Java 中能用 Lombok 解决 getter / setter 模板代码的地方，优先使用 Lombok。
- 重复胶水代码要适度收敛，但收敛方式不能比原逻辑更难懂。
- 注释只解释不明显的业务约束、协议边界或复杂流程，不写重复代码字面含义的注释。

## Java 后端规则

- 遵循现有包结构和模块边界，避免跨层直接调用导致平台、插件、启动模块相互缠绕。
- 优先使用项目已有基础设施，例如 `com.clougence.utils`、插件 SDK、DAO、服务层、统一异常和日志机制。
- 数据库实体沿用 `DO` 后缀，表单对象沿用 `FO`，请求 / 响应对象沿用 `VO`。
- 新增数据源能力时，优先参考 `backend/clouddm-plugins/clouddm-ds/` 下相近实现，并在 `backend/settings.gradle` 注册模块。
- 新增认证或第三方集成时，优先参考 `backend/clouddm-plugins/clouddm-provider/` 下相近实现。
- 新增平台 API 时，同时检查 `cgdm-api`、`cgdm-console`、`cgdm-dao`、前端调用和测试是否需要同步调整。
- 日志使用 Lombok `@Slf4j`；错误日志要带上下文和异常栈，不要吞异常。
- 异常信息要简短、明确，避免只抛空泛的 `RuntimeException`。

## 前端规则

- 前端以 Vue 3 和 Vue CLI 为主，优先沿用 `frontend/src/components/`、`frontend/src/services/`、`frontend/src/store/`、`frontend/src/router/`、`frontend/src/views/` 的既有模式。
- 用户可见文案必须维护在 `frontend/src/locales/`，不要在组件、服务或 store 中直接硬编码展示文案。
- 修改接口字段、枚举、状态或错误码时，要同步检查后端 VO / API、前端 service、页面逻辑和测试。
- 后端已经删除的字段，前端不要继续保留 fallback 行为。
- 不要擅自引入新的 UI 框架、状态管理库或构建工具。
- 复用现有组件和样式规范；只有现有组件无法表达真实交互或可访问性需求时，才新增组件。
- 修改样式时要检查移动端和常见桌面宽度，避免文案溢出、控件遮挡和布局跳动。

## 数据库变更

- 不要修改已经发布或已用于升级路径的历史 Flyway Java migration。
- 新增结构或数据变更时，在 `backend/clouddm-boot/boot-initialization/src/main/java/com/clougence/clouddm/init/component/scripts/` 下新增 migration，并沿用现有命名和执行模式。
- SQL 只负责必要的数据查询、过滤和排序；不要把复杂业务计算写进 SQL。
- 跨日期统计、多口径聚合等复杂计算优先放在 Java 内存里做，数据库侧只拉取必要字段。
- MyBatis XML 中 SQL 比较符统一使用 `<![CDATA[ ... ]]>`，不要写 `&lt;`、`&gt;` 这类 XML 转义符。
- 修改表结构、字段语义或默认数据时，同步检查 DAO、DO、mapper、初始化脚本、升级脚本、前端展示和测试。

## 前后端契约

- 前后端契约必须保持一致。
- 字段删除时，要同时删除后端、前端和测试中的对应逻辑。
- 前端不要为后端已删除字段继续保留 fallback 行为。
- 在增加兼容逻辑前，先确认后端真实协议。
- 接口响应、状态机、权限判断、国际化 key 和错误码要以当前真实实现为准，不要凭命名猜业务含义。

## 并发、连接与流式

- 异步、线程池、队列、锁、连接和流式处理相关代码必须特别谨慎。
- 不要把慢连接或阻塞 I/O 带回生产工作线程。
- 重点关注终态、重试、重复执行、重连、资源释放、不可恢复失败等路径。
- 涉及数据库连接、查询执行、结果导出、WebSocket、任务调度、插件加载和 worker 心跳时，要明确资源释放和失败恢复路径。

## 测试要求

- 默认不要新增无用测试类；除非用户主动要求“添加测试类测试一下”或明确要求补测试，否则不要新增测试类。
- 测试要覆盖真实风险路径，不要只覆盖表面分支。
- 重点覆盖网络断开、重连、终态、不可恢复、重复执行、慢连接、资源释放等场景。
- 后端改动优先运行相关模块测试；影响公共模块、DAO、初始化、权限、SQL 执行或插件协议时扩大验证范围。
- 前端改动优先运行 `npm run lint`、`npm run check-i18n` 和相关单测；用户可见流程变更要做浏览器级检查。

## Review 规则

- Review 只提真实 bug 和明确 concern。
- 不要堆风格噪音，除非它确实影响正确性、可维护性或契约清晰度。
- 优先关注状态一致性、协议破坏、权限绕过、升级兼容、资源泄漏、重复执行和前后端字段不一致。

## PR 与提交

- PR 说明要清楚描述改了什么、为什么改、如何验证。
- 提交信息使用 Conventional Commits，例如 `feat(mysql): support prepared statement` 或 `fix(auth): avoid stale role cache`。
- 提交前确认没有把本地构建产物、依赖目录、日志、临时文件或无关格式化改动带入 diff。
