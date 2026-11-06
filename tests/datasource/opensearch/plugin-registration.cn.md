# OpenSearch 独立插件注册

对应接入计划第 2 步。参考现有 `ds-es` 的插件、配置序列化和驱动 SPI，沿用[数据源接入指南](../../../docs/guides/datasource-integration-guide.cn.md)的模块与交付约定。

## 本步实现边界

| 项目 | 注册值 / 实现 |
| --- | --- |
| Gradle 模块 | `ds-opensearch`，使用现有 `com.clougence.plugin-conventions` |
| 产品标识 | `DataSourceType.OpenSearch` / `DsType.OpenSearch`；名称 `OpenSearch`，短码 `os`，分类 SEARCH |
| 插件 | `OpenSearchDsPlugin`，`display=false`；保持入口隐藏，待会话、执行及治理能力接通后开放 |
| 配置 | `OpenSearchConfig` 继承公共配置字段，绑定独立 `OpenSearch` 序列化 provider；不冒充 ES 配置 |
| 驱动家族 / 版本 | `OpenSearch HTTP Client` / `5.6.4`；这是 HTTP 驱动版本，与服务端 2.x/3.x 版本分别记录 |
| 工厂 / 服务发现 | `OpenSearchDsFactory`；通过 `META-INF/services/com.clougence.drivers.DsFactory` 和 `drivers.xml` 注册 |
| 国际化 | 插件名称、配置解码及客户端初始化错误；默认、英文、中文资源 |
| Schema | 初始化独立 mapping service；类型、元数据、方言和 DSL 能力尚未绑定 |

公共 Java 类仅增加两个产品枚举常量；未修改公共加载器、Session、执行器或 HTTP 工具，也未增加产品判断分支。

本步工厂只负责创建、启动和立即关闭异步 HTTP 客户端，关闭透明重试、重定向和 Cookie 管理。创建客户端不发起网络连接，不能作为“测试连接”成功的证据。认证、TLS、连接参数、期限、有界响应及取消关联在后续连接/会话步骤实现。

配置 SPI 暂不声明 SSL、SSH 或事务能力；认证选项预留无认证和用户名密码，默认端口为 REST 端口 9200。配置中的公共 TLS 字段可以序列化，但本步尚不消费它们。插件没有绑定 SessionFactory 或 SQL 引擎，不复用带 ES 版本约束的引擎；请求 SessionFactory 会明确失败，不返回空会话。

## 依赖及类加载边界

生产插件采用第 1 步选定的原始 HC5 路线，声明以下 `compileOnly` 依赖，并在 `drivers.xml` 中分别固定 Maven 坐标：

- `org.apache.httpcomponents.client5:httpclient5:5.6.4`
- `org.apache.httpcomponents.core5:httpcore5:5.4.3`
- `org.apache.httpcomponents.core5:httpcore5-h2:5.4.3`

`opensearch-java:3.10.0` 曾在第 1 步独立传输原型中作对照，相关临时验证文件现已清理；生产插件未使用其 API，因此不引入 SDK 及其 Jackson 2.22.2。平台 Jackson 保持 2.19.0。

| 层级 | 拥有的内容 |
| --- | --- |
| 平台父加载器 | 插件 SDK、`DsFactory` / `DsObject`、公共配置/枚举、Jackson、日志 API |
| 插件加载器 | OpenSearch 插件、配置、序列化、国际化及现有插件构建约定打入的 `dsc-common` 等依赖 |
| 每个驱动版本的绑定加载器 | 独立加载 `com.clougence.clouddm.ds.opensearch.execute.*` 和 `org.apache.hc.*`；读取已准备的 HC5 JAR |

`@Plugin.includePackages` 使用现有局部优先机制，确保工厂及将来的会话适配使用同一组 HC5 类型。HC5 类型留在插件执行包内，不进入共享 SPI 方法签名。插件包不重复打入 HC5、Jackson、OpenSearch SDK、`ds-es` 或 `sql-es`；日志 API 继续从父加载器获取。

## 在线准备与离线部署

插件 JAR 与驱动文件分开交付。在线环境加载 `ds-opensearch-lib.jar` 及现有 `inner-driver-loader-lib.jar` 后，由平台 Maven preparer 为 `OpenSearch HTTP Client / 5.6.4` 执行依赖分析和下载，写入可写驱动目录。`compileOnly` 和插件加载成功都不表示驱动已准备。

- 可写驱动目录由 `driverDirectory` 指定，默认 `${app.data}/drivers`。
- Maven 仓库由现有 `cg.driver.maven.repository` 配置；验证使用 `https://repo.maven.apache.org/maven2`。连接/请求超时沿用 `cg.driver.maven.connectTimeout`、`cg.driver.maven.requestTimeout`。
- 离线环境需要从已准备环境复制完整 `OpenSearch HTTP Client/5.6.4/` 目录，包含下载的全部文件、原有相对路径和平台生成的 `files.idx`，不能只复制三个主 JAR 或手写索引。
- 可复制到目标可写驱动目录；也可预置到 `${app.home}/built-in-drivers`，或 `builtinDriverDirectory` 指定的只读目录。启动后沿用公共加载器的索引恢复与只读回退机制，无须重新分析 Maven。
- Console、Sidecar 等实际执行节点均需具备相应插件和驱动。准备完成后，可在没有 Maven 网络的环境启动并创建客户端。
- 本步没有修改 `package/pkg/builtin-drivers/built-in-drivers.xml`，因此不代表安装包已经默认内置这些驱动；实际发布包安装、重启及双端部署仍待交付步骤验收。

## 第 2 步验证记录

2026-11-03 已构建真实插件包，通过 `PluginLoadHelper`、`PluginManager`、配置 SPI、平台 Maven preparer 和带租约的驱动工厂完成验证，45 项断言通过。辅助验证脚本及专用 classpath 配置已于 2026-11-04 清理，以下保留当时的验证方法与边界。

验证当时，第一个 JVM 的平台 classpath 不含 HC5，也不含任何 ES 插件；仅扫描 OpenSearch 和公共 Maven 下载器插件包，验证未准备驱动时仍能发现插件及反序列化配置，随后在线下载并创建/关闭客户端。第二个全新 JVM 使用空可写驱动目录、复制并设为只读的内置驱动目录，以及不可达 Maven 地址，验证离线恢复；同时在宿主 classpath 放入 HC5 5.4.4，核对驱动仍独立使用 5.6.4 和 Core 5.4.3。两阶段都检查重复关闭和快速创建/关闭后的 reactor 线程退出。配置错误探针只使用虚构凭据，并检查异常不包含输入值。

两阶段验证最终退出码为 0，临时目录和下载文件已清理。该验证没有访问 OpenSearch 实库，不能替代 TLS、查询、会话、页面或安装包验收。执行结果及关注项见[能力矩阵](opensearch-test-matrix.md)。
