# OpenSearch 版本与能力矩阵

更新时间：2026-11-04。源码起点：`86777b11a1614ca6d3f810b2b0d6d16d83a37cdd`，分支 `feat/opensearch`。覆盖接入计划第 1～2 步；[接入说明](README.md)。

第 1～2 步验证于 2026-11-03 完成。两步的辅助验证脚本及专用依赖配置已于 2026-11-04 清理；以下保留当时的版本组合、验证方法与结果。

**当前结论：3.9.0 独立原型通过，插件注册骨架已落地并完成真实平台加载器验证；版本覆盖为 PARTIAL，产品会话和查询尚未接通。** 状态分别记录本地故障服务、直接驱动/HTTP、插件运行时和发布包证据。

## 1. 固定候选与代表版本

| 项目 | 固定值 / 证据 |
| --- | --- |
| 客户端候选 | `org.opensearch.client:opensearch-java:3.10.0`；已下载、JDK 17 实际加载，并与 3.9.0 建立严格 TLS 连接 |
| HTTP 依赖 | `org.apache.httpcomponents.client5:httpclient5:5.6.4`；`httpcore5`、`httpcore5-h2:5.4.3` |
| 客户端 JVM | Homebrew OpenJDK 17.0.18，macOS arm64 |
| 实库 | 用户提供的 HTTPS REST 实例；根接口回读 `distribution=opensearch`、`number=3.9.0`、`build_type=tar`、`build_hash=4ee42a94e87f66fbf1e62a9871b1b87f91e02472`、Lucene 10.5.1 |
| 实库认证 | 用户名密码；给定 CA，完整证书链和主机名验证；未绕过证书校验 |
| 证书识别 | 测试 CA SHA-256 `63A9FE8ED4254A75BB845E083BEC03073FE55CB48660AF2C998F79CDD1603287`；不提交 CA 或凭据 |
| 临时样本 | 仅本次随机索引、两个主分片、零副本；29 条种子 + 分页途中插入的文档；最终索引删除并确认 HEAD 404 |

精确版本固定为回归代表，不表示整条版本线已认证。

| 组合 | 服务端 × 固定候选 | 选择原因 | `pit` / `shardDoc` 预期 | 本轮直接验证 | 产品 / 安装包 |
| --- | --- | --- | --- | --- | --- |
| V01 | 2.0.1 × 3.10.0 | 2.x 最低系列代表 | false / false | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V02 | 2.3.0 × 3.10.0 | PIT 引入前最后一个次版本代表 | false / false | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V03 | 2.4.1 × 3.10.0 | PIT 引入边界 | true / false | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V04 | 2.19.5 × 3.10.0 | 2.x 后期代表 | true / false | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V05 | 3.0.0 × 3.10.0 | 3.x 大版本边界 | true / false | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V06 | 3.2.0 × 3.10.0 | `_shard_doc` 引入前代表 | true / false | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V07 | 3.3.0 × 3.10.0 | `_shard_doc` 引入边界 | true / true | NOT RUN：未提供环境 | NOT RUN / NOT RUN |
| V08 | 3.9.0 × 3.10.0 | 当前用户提供的实库基线 | true / true | PASS：本节范围内 11 项原型断言 | NOT RUN / NOT RUN |

能力边界依据：[PIT API 从 2.4 引入](https://docs.opensearch.org/2.19/search-plugins/searching-data/point-in-time-api/)、[3.3.0 加入 `_shard_doc`](https://github.com/opensearch-project/OpenSearch/blob/main/release-notes/opensearch.release-notes-3.3.0.md)。[3.10.0 客户端兼容矩阵](https://github.com/opensearch-project/opensearch-java/blob/v3.10.0/COMPATIBILITY.md)声明 3.x 客户端兼容 1.x～3.x 和 JDK 17；这不能替代上表各版本的实际结果。

## 2. 传输原型结果

历史验证使用本地回环故障服务。断开服务先读完请求体并计数，再丢弃确认；不凭异常类型推断重放次数。

第 1 步最终复跑时，本机故障探针退出码 0：22 个必需断言 PASS；5 个原生对照中 1 个 PASS、4 个 FAIL。实库探针退出码 0：11 个必需断言 PASS。原生对照的失败不改记为通过。

| ID / 场景 | 选定异步适配原型结果 | 原生候选对照及处理决定 |
| --- | --- | --- |
| RAW-GET/POST/PUT/DELETE | PASS：原始 JSON、Unicode、大整数文本及编码查询参数保持一致 | 不转换成有损的强类型请求 |
| TLS-CA/HOST/UNTRUSTED | PASS：信任给定 CA；错误主机名、未知 CA 拒绝 | 不能使用 trust-all 或关闭主机名验证 |
| LIMIT、LIMIT-TLS：定长/分块 | PASS：1024 字节上限，HTTP/HTTPS 各自验证；以实际累计字节计数 | SDK 本地 TLS 大响应对照 PASS；直接复用其消费者到原始请求的早期实库路径曾等待超时，最终采用独立消费者显式终结/取消 |
| RAW-CANCEL/LATE-CANCEL | PASS：重复取消释放连接；迟到取消不影响另一条活动交换；通过 HC5 公开执行拦截器保留当前交换的端点终止动作 | SDK-CANCEL **FAIL**：取消返回的 Future 200ms 后仍占用一个连接；直接 HC5 的复用连接路径也不能仅依赖返回 Future |
| RAW-DEADLINE | PASS：250ms 整次请求期限后取消真实 HTTP 交换并释放 | 不能只对衍生 Future 调用 `cancel` 或 `orTimeout` |
| RAW-READ-TIMEOUT | PASS：300ms 配置，实测约 0.3 秒终结并释放 | 必须同时控制 I/O reactor 的检查间隔 |
| RAW-QUEUED-DEADLINE/NOT-SENT | PASS：300ms 显式排队预算后取消，过期请求未到达服务端 | HC5-POOL-TIMEOUT **FAIL**：仅配 300ms pool timeout，仍等到调用方 2 秒期限；不能依赖其自动唤醒 |
| RAW-NO-REPLAY：确认丢失 | PASS：POST 接受次数为 1 | SDK 双节点、关闭自动重试后 **FAIL**：接受次数为 2 |
| RAW-NO-REPLAY：503 | PASS：POST 接受次数为 1，保留原始 HTTP 状态 | SDK 双节点、关闭自动重试后 **FAIL**：接受次数为 2 |
| RAW-NO-REDIRECT | PASS：307 不跟随，目标端点未收到请求 | 防止透明改变写入目标 |
| RAW-CLOSE/REUSE | PASS：立即关闭终结在途交换；取消、超限后可继续发起请求 | 生产实现还需验证空闲关闭、调度器及并发终态 |

选型：固定候选用于验证及对照，**原生 Generic Client 传输不满足直接采用条件**。采用插件内原始 HC5 异步请求、有界消费者和取消关联，不改 SDK 源码，不给公共类增加产品分支。插件及驱动注册已在第 2 步落地；生产传输、取消关联和独立排队阶段调度仍待后续实现。

## 3. V08 实库原型结果

历史验证通过独立 HTTP 原型执行，使用管理账号，覆盖请求取消关联、有界响应及分页。

| ID | 能力 / 验证方法 | 支持边界与实际证据 | 直接验证 | 产品验证 |
| --- | --- | --- | --- | --- |
| SERVER-VERSION | 根 REST 接口识别产品与版本 | 回读 OpenSearch 3.9.0；未伪装成 ES 版本 | PASS | NOT RUN |
| SERVER-SDK-TLS | Generic Client 使用 TransportOptions 中的认证头、给定 CA | 严格 TLS + Basic，响应 200；只能证明该连接样本 | PASS | NOT RUN |
| SERVER-RAW-JSON | 写入并回读文档 | `中文; 😀` 与整数 `9007199254740993` 无损；涉及 PUT、GET、POST 更新及 DELETE 清理 | PASS | NOT RUN |
| SERVER-GET-BODY | 带 JSON body 的 GET 搜索及聚合 | 29 条命中；聚合计数 29 | PASS | NOT RUN |
| SERVER-CONFLICT | 重复使用旧 seq_no / primary_term 条件写入 | 第一次成功，第二次返回 409；冲突未转换成成功或重试 | PASS | NOT RUN |
| SERVER-LIMIT | 实际搜索响应上限 256 字节 | ContentTooLongException 正确终结，之后同一客户端继续完成分页 | PASS | NOT RUN |
| SERVER-SCROLL | 两分片、相同评分、每页 7 条、途中插入 | 5 个非空页，完整且唯一的 29 个种子 ID；不包含新插入文档 | PASS | NOT RUN |
| SERVER-SCROLL-CLOSE | DELETE 带 body 关闭，仅关闭本次 ID | succeeded=true；再次使用已关闭 ID 返回错误 | PASS | NOT RUN |
| SERVER-PIT | OpenSearch PIT + `_score desc, _shard_doc asc` + search_after | 两分片、途中插入、5 个非空页；完整且唯一的 29 个种子 ID | PASS | NOT RUN |
| SERVER-PIT-CLOSE | DELETE /_search/point_in_time，本次 pit_id | pits[0].successful=true；关闭后不可重用 | PASS | NOT RUN |
| SERVER-CLEANUP | 独立连接删除随机索引并 HEAD 回查 | acknowledged=true，HEAD 404；本轮早期失败运行的专用索引也已清理 | PASS | NOT RUN |

原型不证明大规模导出性能、聚合自动翻页、任意查询稳定排序、PIT 权限缺失时降级、分片失效、服务端任务取消、多节点故障转移或数据库账号最小权限。这些项仍为 NOT RUN。

## 4. 首期产品接入门禁

本表与直接原型分开记录；第 2～9 步持续补充实现位置与产品证据。

| 能力 / 入口 | 当前实现位置 | 支持边界 / 验证要求 | 产品状态 |
| --- | --- | --- | --- |
| 新增、保存重开、修改、测试连接 | `ds-opensearch` 配置、序列化与 SessionFactory；产品入口已开放 | 第 3 步已实现多地址、认证、TLS、连接池及超时；新增/保存/修改/连接验证待统一验收 | NOT RUN |
| Session、取消、终态、资源清理 | `OpenSearchClient`、`OpenSearchSession` 及异步交换生命周期 | 双会话隔离，迟到/重复取消、写入结果未知、断网/关闭；HTTP 取消不等于服务器任务取消 | NOT RUN |
| 插件和驱动类加载 | `ds-opensearch`；真实 `PluginLoadHelper`、Maven preparer、带租约的工厂 | 独立包加载、在线准备、离线恢复及 HC5 隔离通过；Console/Sidecar 实际部署待验证；未引入 SDK 或提升 Jackson | PARTIAL：第 2 步加载范围 PASS，部署 NOT RUN |
| 索引/字段、Mapping/Settings | 第 5 步：OpenSearchMetaService / 独立类型映射 | 精确索引、OpenSearch 类型、普通账号可见性、系统索引边界 | NOT RUN |
| REST 编辑、拆句、补全、格式化 | `sql-es` 的 `RestApiProfile` / `RestDslLanguageSpi` 与两插件的固定产品绑定 | 共用语法；校验、分析、改写和执行入口使用产品/版本；长期用例见 [DSL 集成验收](rest-dsl-integration.cn.md) | PARTIAL：单元测试 PASS，集成 NOT RUN |
| 基础查询/聚合、文档读写及索引管理 | 第 5 步：OpenSearchClient / Session / ResultWriter | API/参数白名单、更新计数、部分失败、异步任务边界逐项验证 | NOT RUN |
| Scroll/PIT、结果展示与导出 | 第 6 步：OpenSearchSearchCursor / ResultWriter，复用现有缓存与导出服务 | 按版本、保守查询形状及平台限制选择策略；清理失败保留真实终态；[长期验收用例](pagination-export.cn.md) | PARTIAL：单元测试 PASS，实库/页面/文件导出 NOT RUN |
| 权限、只读、审计与脱敏 | 第 7 步：插件安全规则注册、请求/响应门禁、模板与隐式资源检查，复用现有平台链路 | [安全验收用例](security-integration.cn.md)；自定义脚本语言边界尚待决定 | PARTIAL：单元测试 PASS，应用/实库/导出 NOT RUN |
| 页面、文案、能力开关 | 未接入 | 按仓库规则完成 Web 构建及 @Browser 复测 | NOT RUN |
| 工单/CI/CD、发布包和重启恢复 | 未接入 | 单独完成约定流程；alone、Console/Sidecar 和离线驱动方案 | NOT RUN |

AWS 托管/Serverless、SQL/PPL、Bulk/MSearch、向量/Neural/Hybrid 查询、Data Stream/别名管理、跨集群和集群/Security 管理不纳入本次首期。

## 5. 制品识别

以下为第 1 步本地实际加载 JAR 的 SHA-256；不提交二进制。变更客户端或 HTTP 依赖后须建立新组合并重新验证。

| 制品 | SHA-256 |
| --- | --- |
| opensearch-java-3.10.0.jar | `6732f392c9180033451ed044baf39a8844eab3a014a3f361c0d665f98d687b6c` |
| httpclient5-5.6.4.jar | `bdef5f8841145cd76c4c605ff301bc840eea0d5292332b7b892a93316c0d8ee1` |
| httpcore5-5.4.3.jar | `18bfbbabb478dfb67f31aeaf428c387f3c3df654582e1309f708ee1f3086830a` |
| httpcore5-h2-5.4.3.jar | `c7db7026b8e2dea39132b04a6069f6671e2858309b20a146ec5c7dd6ed73a0b6` |

## 6. 第 2 步插件与驱动注册验证

实现与部署边界见[插件注册说明](plugin-registration.cn.md)。历史验证进程只加载 OpenSearch 及公共 Maven 下载器两个插件，调用真实平台加载和驱动准备链路，45 项断言通过；辅助验证文件已清理。

| 场景 | 结果 / 证据 |
| --- | --- |
| 编译、独立插件包与依赖边界 | PASS：`ds-opensearch:build`、`customFatJar`；制品包含工厂 SPI、驱动 XML 和国际化资源，不含 HC5、Jackson、SDK 或 ES 插件/引擎 JAR |
| 无 HC5、无 ES 的宿主加载 | PASS：未准备驱动仍发现独立产品、隐藏插件、专属驱动家族和配置 SPI；未绑定 SessionFactory/SQL 引擎 |
| 配置多态序列化 | PASS：以公共 `DataSourceConfig` 为目标恢复插件实际配置类型、虚构密码/CA 和驱动版本；使用平台 Jackson 2.19.0；畸形配置异常不含输入凭据 |
| 在线驱动准备与服务发现 | PASS：平台 Maven preparer 实际下载、生成 `files.idx`；真实带租约工厂创建客户端，HC5 为 5.6.4，Core 为 5.4.3 |
| 离线恢复及版本隔离 | PASS：全新 JVM、空可写目录、只读内置驱动目录、不可达 Maven 地址；恢复已准备状态，无 Maven analysis 目录；宿主 HC5 5.4.4 与驱动 5.6.4 类型分离，共享 API/Jackson 仍属于父加载器 |
| 资源关闭 | PASS：启动 ACTIVE、重复关闭 SHUT_DOWN；两个 JVM 各额外快速创建/关闭 3 次，新建的 `httpclient-*` 线程均在 2 秒门限内退出 |
| 既有模块回归 | PASS：`ds-es:build`、`boot-alone:compileJava`、`boot-sidecar:compileJava`；现有 plugin-loader 1 条、cg-drivers 26 条测试最终通过。`ds-opensearch:test`、`cg-schema:test` 为 NO-SOURCE，不计测试通过 |
| 实库、页面、实际安装包及双端部署 | NOT RUN：本步不访问数据库，不代表认证、TLS、Session、查询或发布包验收 |

首次现有 `cg-drivers` 全集运行中，`deleteLocalResources_shouldBeOwnedByDriverVersion` 的时间戳变化断言失败（删除前后为同一毫秒）；该测试单独复跑和 26 条全集复跑均通过。公共 `VerDef` 沿用毫秒时间戳，本步未修改它，也不将此偶发失败隐藏为首次全过。

执行中曾观察到 HC5 立即关闭时后台打印 `ClosedSelectorException`，发生在 selector 已关闭后的 `SingleCoreIOReactor.doTerminate/closeOpenChannels`；客户端状态仍为 SHUT_DOWN。补充快速创建/关闭探针后，两阶段均通过线程释放断言，最终脚本退出码 0，出现 `OPENSEARCH_PLUGIN_VERIFICATION_COMPLETE`。该日志现象仍保留为后续生产传输生命周期验证的关注项，当前样本不能证明任意在途请求或关闭竞争安全。另有现有 `DsObject.close` 的“close ignored”日志，其当前实现会在调用 closer 后打印该信息，不能据此推定未执行关闭。本步未改公共类或屏蔽原生日志。

## 7. 第 4 步自动化验证

本轮 27 项单元测试全部通过，ES 与 OpenSearch 插件编译、打包成功。两个插件包均包含更新后的 sql-es 嵌套依赖，OpenSearch 插件没有将 HC5 测试依赖打入生产包。

运行命令（不启动应用、不访问数据库）：

```bash
cd backend
./gradlew :sql-es:test :ds-opensearch:test :ds-es:compileJava :ds-es:customFatJar :ds-opensearch:customFatJar
```

- `EsDslRegressionTest`：ES 原有版本边界、危险路由/参数拒绝、跨索引引用、文档字面值、Unicode、拆句、格式化和限制改写。
- `RestApiProfileTest`：独立产品识别、非法版本拒绝、缺失版本的保守能力、2.4 PIT 与 3.3 稳定排序边界。
- `OpenSearchDslTest`：原生超时参数、Mapping/查询差异、嵌套向量与脚本入口拒绝、普通数据不误判、资源分析、改写和引擎身份固定。
- `RestDslLanguageTest`：产品/版本补全、产品参数覆盖、诊断，以及两产品共用编辑语法。
- `OpenSearchSessionPolicyTest`：不启动 HTTP 的客户端替身状态，验证执行入口使用已探测版本、固定产品、检查授权范围及拒绝已关闭会话。

上述单元验证不代表页面、插件独立运行、网络故障、实际查询、分页或权限流程已经通过；这些集成结果保持 `NOT RUN`。

## 8. 第 5 步自动化验证

`ds-opensearch` 27 项测试和 `sql-es` 22 项回归测试通过（合计 49 项），ES 编译与 OpenSearch 插件打包成功。本步未修改公共类、ES 插件、前端或数据库结构；没有启动 CloudDM、执行格式化或连接实库。

| 范围 | 自动化证据 | 集成状态 |
| --- | --- | --- |
| 元数据 | 精确索引排序/过滤；排除 Alias、数据流及 backing index；嵌套/multi-field/derived、OpenSearch 特有及未知类型；保留定义属性 | NOT RUN |
| 单次结果 | 空查询、缺失文档、HEAD、聚合空桶/null、count、大整数/小数、行数/字节限制、脱敏拒绝形态 | NOT RUN |
| 写入计数 | created/updated/deleted=1、noop/not_found=0；by-query 更新+脚本删除计数；409/408/500 或 200 部分失败保留计数并失败；索引确认状态 | NOT RUN |
| 执行边界 | HTTP 原始 body/编码参数、非 JSON HTTP 错误；资源范围变化、默认 Pipeline、隐式 Mapping 资源、只读/忙/关闭/真实版本策略 | NOT RUN |
| 资源生命周期 | 真实 HC5 + 进程内 HTTP 模拟：断连写请求不重放、单连接取消后恢复、总期限覆盖预检查、超限恢复、关闭阻止后续写入、失败通知与状态复位 | NOT RUN |

长期应用/实库用例见[元数据与基础执行验收](metadata-execution.cn.md)。回环 HTTP 模拟不证明真实集群兼容、权限、TLS、页面、Sidecar/Console 部署或全量分页；第 1 步实库原型仍只代表当时的原型范围。

## 9. 第 6 步自动化验证

OpenSearch 模块 45 项测试通过（原有 27 项 + 新增分页 18 项），共享 `sql-es` 22 项回归通过，合计 67 项；ES 编译和 OpenSearch 插件打包成功。修复当前分支 `OpenSearchMetaService`、`OpenSearchResultWriter` 缺失的集合导入后完成编译，未执行格式化。没有启动应用、执行数据库升级或连接实库。

| 范围 | 自动化验证 | 集成状态 |
| --- | --- | --- |
| 策略选择 | 2.0/2.4/2.19/3.0/3.2 选择 Scroll，3.3/3.9 选择 PIT；不合格形状保留单次请求；缺权不自动回退 | NOT RUN |
| 排序与快照协议 | 默认 score、显式排序、已有稳定键、精确 64 位 search_after、最新 ID、模拟快照后新增文档不混入；最终行数/ID/顺序逐一比对 | 多分片与并发增删改实库 NOT RUN |
| 结果与缓存契约 | 单结果 ID/固定列、静默后续行、STREAM、末批截断、跨页累计大小、已知总数提前结束及 PIT 相邻完整排序值重复拒绝 | 页面/真实缓存/下载文件 NOT RUN |
| 取消与清理 | 完成响应解码与取消/关闭竞争、在途续页、输出失败、查询超时、重复取消、清理失败 suppressed、部分 PIT、关闭时阻塞输出的资源回收上限 | 实库上下文清理及部署环境 NOT RUN |

测试采用真实 HC5 与进程内协议模拟，不把模拟服务的快照行为当作 OpenSearch 服务端正确性证据。[分页与导出验收](pagination-export.cn.md)规定真实多分片样本、快照后并发增删改、完整 ID 集合对比、排序和导出文件核对；待统一执行后回填结果。

## 10. 第 7 步自动化验证

OpenSearch 58 项、共享 DSL 25 项、脱敏模块 6 项、平台权限分类定向测试 1 项全部通过（共 90 项；本轮新增 21 项）。ES 编译与 OpenSearch 插件打包成功，包内包含安全规则 SPI / 请求门禁，未包含 HTTP 测试替身。当前分支缺失的 Client / MetaService 必要 imports 已补齐，未格式化。

生产改动限于 `ds-opensearch`：安全规则注册、脱敏请求/响应门禁、创建索引模板检查、3.3+ 系统生成 Search Pipeline 检查、model_id 隐式资源拒绝。公共权限、审计、脱敏和导出代码没有新增产品分支；`sql-es` 仅增加测试，共享脚本规则按用户决定保持原样。

[安全集成验收](security-integration.cn.md)保留命令权限矩阵、数据库账号预检查权限、18 项长期用例和限制。应用/实库/下载文件仍是 `NOT RUN`；自定义脚本引擎的资源边界待决定，元数据预检查不提供跨管理员并发配置修改的原子保证。不得据此宣称全部安全范围已经通过验收。
