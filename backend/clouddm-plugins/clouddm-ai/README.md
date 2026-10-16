# CloudDM AI

`clouddm-ai` 只作为目录分组，不注册 Gradle 模块，没有根级 `build.gradle` 或 `src`。

```text
clouddm-ai/
├── ai-common/    通用请求、响应、HTTP 生命周期、工具注册与执行器、取消与 deadline
├── ai-openai/    OpenAI SSE、模型参数兼容与工具参数分片
├── ai-deepseek/  DeepSeek 模型参数适配，复用 ai-openai 协议实现
├── ai-kimi/      继承 OpenAI 客户端，Kimi 请求参数与思考回放
├── ai-minimax/   继承 OpenAI 客户端，MiniMax 请求参数与敏感响应处理
└── ai-dashscope/ DashScope 2.22.11 原生 SDK、原生 SSE 与模型通道选择
```

## 依赖与实现

- `ai-common` 复用 `cg-utils` 的 JSON、异常等通用工具，不依赖具体模型或平台模块。
- `ai-openai` 依赖 `ai-common`，实现 `StreamingChatClient`。
- 对照最新 ReAkh `origin/feat/ai_v5`（`8fb50ac8`）的 `KimiApiServiceImpl`、`MiniMaxApiServiceImpl`、`DashScopeApiServiceImpl` 及其定制器：
  - Kimi、MiniMax、DeepSeek 都继承 OpenAI 客户端，直接覆写客户端的请求、思考回放和敏感响应方法；厂商参数不放在业务服务中。
  - Kimi 固定 `temperature=1`、`top_p=0.95`、`reasoning_effort=max`，拒绝 `max_completion_tokens`。
  - MiniMax 固定 `temperature=1`、`top_p=0.95`，按 `showReasoning` 发送 `thinking=adaptive/disabled`，拒绝 REQUIRED、结构化回答和 web search 等不支持参数；不再额外强制 `reasoning_split`。`input_sensitive` / `output_sensitive` 以及缺失 finish reason 都按内容过滤处理。
  - DeepSeek 按 `showReasoning` 发送 `thinking=enabled/disabled`。这三家都回传 assistant 的 `reasoning_content`，没有思考内容时发送空字符串，与 ReAkh 一致。
  - DashScope 使用同版本原生 SDK 构造参数、消息、工具及 SSE 头；通过原生 text-generation / multimodal-generation 端点调用。文本模型和多模态模型的路由规则与 ReAkh 相同，不再依赖 `ai-openai`。SQL 助手只提供文本输入，不新增图片、音频或搜索功能。
  - Kimi / MiniMax 的 OpenAI 流按 ReAkh 聚合工具 ID、名称和参数分片；忽略空哨兵，缺失 index 时按 fallback 调用归属。允许 usage 独立帧、message 形式的响应和完成后的其他片段；`[DONE]` 仅作为标记，正常连接关闭后结算，MiniMax 缺失 finish reason 仍按内容过滤处理。
  - DashScope 按 index、ID 或当前调用关联片段，名称和参数追加，ID 更新，工具按首次出现顺序返回；正常关闭且缺失 finish reason 时使用 STOP。完整工具存在时按工具调用继续；length 的未完整参数仍丢弃，取消和超时仍终止请求。
- CloudDM 当前没有思考模式配置，使用 ReAkh INSTANT 模式的普通默认 `showReasoning=false`。模型仍返回的独立思考字段及 `<think>` 内容保留在内部回放中，不混入 SQL 回答正文。
- 工具调用保留原始参数字符串，到执行层统一解析。无效参数通过配对的 `Error:` 工具结果返回模型；续接请求将不能回放的参数规范化为 `{}`，保留调用 ID 和错误结果。空参数、代码围栏、引号包裹与尾逗号沿用 ReAkh 的参数处理方式。
- 传输层区分鉴权、404 模型/API 地址不存在、无效请求、限流、超时、服务失败、地址解析和协议错误，并保留异常原因。内容过滤按准确错误码识别；无效 JSON/UTF-8 不归为网络故障。会话层拒绝缺失或未知结束标记，防止误存成功。
- 压缩材料不回放内部思考字段，工具结果只截取前 2,000 字符并标记 `[truncated]`；完整聊天、SQL 和工具结果继续保存在原始历史。压缩最多尝试三次；空摘要或请求失败不提交，取消立即停止重试。摘要与 ReAkh 一样按非空正文验收，不另外检查结束标记或估算摘要长度。
- `cgdm-console` 中的 `AiModelClient` 组装五种预设的客户端，负责解密凭据、平台错误映射和 SQL 工具装配。预设直接决定调用协议：DashScope 使用原生 SDK，其余预设复用 OpenAI 兼容通道；不另外保存或传递协议字段。
- `cgdm-console` 直接依赖六个模型协议/供应商模块，随运行时依赖进入发行包 `libs/`。`boot-alone`、`boot-console` 沿用平台 Console 依赖与组件扫描，不单独引入 AI 业务模块。不作为数据库插件放入 `plugins/`，Sidecar 不携带模型协议/供应商模块。

新增模型时在本目录增加对应 `ai-*` 模块，实现 `StreamingChatClient`，注册 Gradle 并在业务装配处绑定；
兼容 OpenAI wire protocol 的厂商可以复用 `ai-openai`，无需复制流解析和连接清理。

## 平台业务归属与装配

AI 平台业务遵循现有 Console / DAO 分层；本目录仅保留模型协议和供应商实现。

| 职责 | 模块及包路径 |
| --- | --- |
| 引擎、会话与轮次 Controller | `cgdm-console`：`console.web.controller.ai` |
| 引擎、会话、生成、流式恢复、权限元数据与模型客户端装配 | `cgdm-console`：`console.web.service.ai`，工具位于其 `tool` 子包 |
| 请求与响应 | `cgdm-console`：`console.web.model.fo.ai`、`console.web.model.vo.ai`；内部对话数据位于 `console.web.model.ai` |
| 运行限额配置 | `cgdm-console`：现有 `console.web.component.config` 下的 `AiProperties` |
| Mapper、DO 与持久化枚举 | `cgdm-dao`：`platform.dal.mapper.ai`、`platform.dal.model.ai`；XML 位于该模块的 `mybatis/mapper` |
| SSE 就绪回调 | `cgdm-console`：`AiStreamInterceptor` 放在现有 `global.handler`，由 `WebConfigurer` 沿用其他拦截器的 `@Bean` 方法统一注册，通过请求中的 `AiEventSink` 属性识别待处理响应 |
| 国际化 | `DmI18nUtils` 统一加载 `i18n/ai/messages`；接口标签合入现有 `i18n/request-label`，使用实际 Controller / 方法名 |

完整应用沿用启动配置已有的 Console 与 DAL 扫描，Mapper 由 `DmDalConfig` 统一注册，使用现有 `sqlSessionFactory` 与事务管理器。DAO 不依赖 Console 或模型客户端；`AiPreset`、`AiVerificationStatus`、`AiTurnStatus` 随持久化模型归属 DAO。数据库表、密文、JSON 字段和 `/api/entry/ai/turn`、`/api/entry/ai/engine` 接口契约保持不变，不新增 migration，也不需要迁移数据。

AI 数据对象使用普通 Java 类，明确声明字段与构造方法，通过 Lombok 生成 getter；调用方使用 `getXxx()` / `isXxx()`。只读对象保留 `final` 字段和必要的集合副本，JSON 构造参数显式标注字段名。值相等语义仅用于元数据对象 `AiObject` 的集合查找，以及重复请求校验涉及的 `AiSqlSelection`、`AiUserInputAnswerFO`、`AiUserInputAnswerItemFO`；`ChatUsage` 保留日志所需的 `toString()`。工具参数结构从字段上的 `@ChatToolParameter` 和校验注解读取，嵌套集合继续保留元素类型与校验约束。调整这些对象时，应核对 JSON 往返、缺省值、嵌套校验及工具 Schema，避免改变接口和历史数据的字段格式。

## DM 通用能力复用

- 元数据对象与历史引用直接使用 DM 的 `UmiTypes`，只查询 `Table`、`View`；读取结构和引用复核也限定这两种类型，不保留 AI 对象类型枚举或转换层。历史 JSON 的类型名称仍为 `Table`、`View`，不需要数据迁移。其余枚举分别描述 AI 轮次、事件、验证、提问及供应商协议，属于 AI 业务与协议定义。
- 身份直接使用 `RdpUserService` 的 PUID/UID，角色和资源权限使用 `DmAuthServiceForBiz`、`DmResAuthService`；AI 只负责会话所有权、运行期间权限复核和传给模型的元数据裁剪。
- 列表使用 `PageObj`、MyBatis Plus 分页和 `DmPageVO`；错误文案使用 `DmI18nUtils`，普通接口使用 Spring 请求绑定、Bean Validation 和 `ResWebDataUtils`，无参数接口不定义空 FO。
- 引擎和历史会话关键词搜索沿用 DM 列表的 `LIKE CONCAT('%', #{keyword}, '%')`，使用 MyBatis 参数绑定；`%`、`_` 按 LIKE 通配符处理，不额外实现 AI 专用子串匹配或转义规则。
- 七个 Mapper 均继承 `BaseMapper<DO>`，基础 CRUD 复用 MyBatis Plus，插入不重复声明方法或 XML SQL。复杂查询、行锁、游标分页以及版本/终态条件更新保留 MyBatis XML，不改成复杂 Wrapper。自定义方法及 XML 按 QCRUD 排序：列表查询、创建、单条读取、更新、删除；继承的方法不重复声明。DO 的加密字段通过 `@TableField` 使用 DM 的 `StrSecretTypeHandler`，并启用 `autoResultMap`，保证通用 CRUD 也正确加解密。插入时间由现有服务赋值：会话、历史及运行事件取数据库绝对毫秒时间构造 `Date`；引擎配置按 DM 服务方式使用 `new Date()`。修改时间通过明确的 `Date` 参数写入，不混用数据库本地 `NOW()` 与 Java 时间。
- Mapper 方法按查询条件和更新字段命名，例如 `findBySessionId`、`updateCompactionBySessionId`、`deleteBySessionId`；业务流程放在服务层。更新只传实际参与 SET/WHERE 的字段，不传整个 DO，也不新增更新参数包装类；插入仍使用完整 DO。
- Mapper 沿用 DM 的 MyBatis 参数绑定、`foreach` 批量 IN 查询和显式字段映射，SQL 排版以 `DmApprovalProcessMapper.xml` 为准。会话列表只批量读取当前页会话的历史 ID 和运行记录，在 Java 中选择最早的有效历史，再批量读取正文，保留原有权限检查；空页不执行 IN 查询。模型选择列表只读取公开的四个字段，不读取或解密 API Key。事件全量回放使用 `findByRunIdAfterSeq(runId, 0)`，与断线增量回放共用查询。
- 会话和历史保留游标分页，避免新事件、会话更新时间变化导致偏移分页重复或遗漏；数据库时钟用于跨节点截止时间和协议绝对时间。会话、轮次和运行行锁用于重复请求、活动轮次和事件序号一致性，不引入全局锁。历史与运行一对一：Mapper 仅做单表读取、过滤、排序和必要的行锁；`AiTurnStore` 批量组装会话、历史、运行、结果及用户答案，并在 Java 中选择首条历史、活动轮次及超时候选。普通读取使用同一个可重复读事务；写入流程通过当前读获取历史、运行和事件，避免旧快照覆盖已提交终态。
- 七张 AI 表分别由七个 Mapper 读写，AI Mapper 手写 SQL 不含关联、子查询、聚合、JSON 解析或业务字段计算。`AiEngineService` 在同一个可重复读事务内批量读取模型与供应商，在 Java 中合并配置；分页前按存在的供应商编码过滤模型，计数与分页保持原来的有效配置范围。公开模型选择仅读取供应商编码和四个模型展示字段，不读取凭据。连接测试的版本条件更新及结果读取由现有引擎服务在短事务内完成，网络调用不占用该事务。
- 每个引擎创建独立的供应商配置；编辑先按模型版本更新模型，再更新所属供应商，删除先按模型版本删除模型，再直接删除所属供应商。两表修改处于同一事务，后续写入失败整体回滚；不额外锁定供应商或检查共享引用。
- 模型版本递增、验证状态重置、运行截止时间和事件毫秒时间在现有 Java 服务中计算；轮次截止时间和事件毫秒时间使用数据库时钟。`findCurrentTimeMillis` 通过 `UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000` 获取绝对毫秒，Java 计算截止时间并以 `Date` 绑定查询和更新，避免数据库与 JVM 时区不同造成偏移。SQL 保留简单搜索、排序、分页、数据库时钟读取，以及版本和终态条件更新；历史搜索用标题匹配或“标题为空且模型名称匹配”的普通条件保留原有空标题行为。初始化脚本只负责建表、默认值和权限安装，不作为运行时业务查询入口。
- 所有模块的 JSON 使用 `JsonUtils`，不创建 AI Mapper 或 JSON 包装类，也不逐层传递 Mapper。工具参数允许尾逗号使用共享 Mapper 的独立 `ObjectReader`，不修改全局配置。
- 事务注解沿用 DM 的 `rollbackFor = Throwable.class, propagation = Propagation.REQUIRED`，读事务保留 `readOnly = true` 和可重复读，以保证批量组装使用一致快照。数据库事务与 Mapper SQL 使用 DM 默认超时配置，不单独设置 AI 的 5 秒限制；必要的事务边界、行锁、版本及终态条件更新保持不变。
- 前端普通请求走 `services/http/request`，沿用 DM 默认请求超时，仅通过请求级转换保留提示词和 SQL 空白。SSE 使用同一 HTTP 层提供的流式请求，并共用登录失效、操作密码和 HTTP 错误处理；AI 层负责事件协议、受理/闲置超时和断线恢复。
- 线程工厂使用 `ThreadUtils`；异常根因使用 `ExceptionUtils`。AI 仍保留有界线程池、运行限额、取消和引用计数，避免慢连接占用通用工作线程。
- 数据库 `DATETIME` 沿用 DM 的 `java.util.Date` 和 MyBatis 内置处理器，数据库比较、更新时间和分页参数也使用 `Date`，遵循 DM 共用 JDBC 连接的时间映射。接口和运行事件在现有组装处使用 `Date.toInstant()` 保持 ISO 时间协议，不把字段强制解释为 UTC，也不添加 AI 时区配置或 TypeHandler。已有 UTC 字段在本地停写、备份后通过同一 JDBC 映射转换，保留原绝对时间；事件毫秒及事件正文中的 ISO 时间不重复偏移。
- UUID 输入直接使用 Bean Validation 的 `@Pattern`，正则统一放在现有 `AiSqlConstants`；时区选择在提示词组装处使用 JDK `ZoneId` / `ZoneOffset`，偏移文本使用 `DateTimeFormatter`。
- 模型 SSE 解析、HTTP 状态到模型错误的映射、工具参数修复和工具 schema 装配属于供应商及工具协议；它们复用 `JsonUtils`、Bean Validation 和 OkHttp，DM 普通接口/HTTP 工具不提供这些协议行为。token 预算通过缓存的 jtokkit 编码器计算，不逐请求创建编码器。

### 保留辅助类清单与复用边界

以下逐项列出协议及运行辅助类。基础能力直接调用 DM/JDK；保留的代码只处理表中列明的协议或业务语义。

| 类 | 作用 | 为什么仍需这段逻辑；已复用什么 |
| --- | --- | --- |
| `ChatHttpTransport` | 绑定流式 HTTP 取消、检查 SSE 类型、释放响应/连接池/执行器。 | 使用 OkHttp、`ExceptionUtils`。DM `CanalHttpClient` 返回 RPC `ResponseData`，`OpenApiHttpClient` 带 DM 签名并缓冲为字符串，都不提供模型流消费、运行取消绑定和中断快照。 |
| `ChatHttpErrorDecoder` | 映射供应商 HTTP 状态及 SSE 错误帧。 | JSON 使用 `JsonUtils`；DM 普通业务错误没有供应商鉴权、限流及内容过滤码语义。传输及解析共同使用此类。 |
| `ChatStreamControl` | 跨连续请求共享 deadline，绑定/解绑当前传输，处理先取消后绑定的竞态。 | 使用 JDK 原子类；DM `Cancellable` 只有 `cancel()`，Future 也不提供跨请求 deadline 与传输重绑定。 |
| `ChatTextAccumulator` | 跨 chunk 识别 `<think>`，拆分正文与思考，保存未闭合标签快照。 | 使用 `StringBuilder`；普通字符串工具没有这套流式标签状态。 |
| `ChatToolArguments` | 处理模型参数的空串、围栏、引号包裹、尾逗号及无效参数回放。 | 使用 `JsonUtils` 与独立 `ObjectReader`；属于模型输出修复，不能改变 DM 全局 JSON 规则。 |
| `OpenAiStreamParser` | 解析 UTF-8/SSE、多行帧、工具分片、usage、结束原因和中断快照。 | 使用 `JsonUtils`；DM `SseResponseUtils` 是向浏览器写 SSE，不能解码供应商输入流。 |
| `ChatResponseState` | 保存单次 OpenAI 流的正文、思考、工具及终态。 | 是请求独享的解析状态，不是通用缓存或 JSON 包装。 |
| `ChatToolCallBuffer` | 按 OpenAI index 累加工具 ID、名称及参数分片。 | 使用 `StringBuilder`；DM 没有模型工具分片的数据模型。 |
| `OpenAiModelCompatibilityHelper` | 归一化 GPT 模型参数及输出 token 参数。 | 是模型参数组合规则，普通 HTTP/JSON 工具不能识别这些限制。 |
| `DashScopeStreamParser` | 解析原生 output/usage/敏感标志/工具分片。 | 使用 `JsonUtils`；原生字段与结束规则不同于 OpenAI，也不属于 DM 出站 SSE。 |
| `DashScopeToolCallBuffer` | 保存原生工具 index、ID、名称和参数。 | ID 更新而非 OpenAI 式追加，不能混用两种协议状态。 |
| `ChatToolRegistry` | 每次运行配对注册工具规格与执行器，拒绝重名，按名称路由。 | 使用 JDK Map；普通 Bean 注册不表达当前运行的工具集及上下文。 |
| `ChatToolSchemas` | 把方法参数、普通类字段、枚举及集合元素校验约束转成工具 schema。 | 使用反射、Jakarta 注解。DM `JsonSchemaElementUtils` 处理 MCP 类字段、`McpField` 和 MCP 国际化，不处理此处的方法参数与 TYPE_USE 约束；协议模块也不能反向依赖 Console。 |
| `ChatToolExecutor` | 参数绑定、校验并反射调用工具，透传业务异常。 | 直接使用 `JsonUtils.toJavaType` 和 DM 注入的 `Validator.forExecutables()`；保留的是工具方法参数配对与调用协议。 |
| `AiExecutors` | 隔离模型、元数据、事件写入、控制任务，统一关闭线程池。 | 线程工厂使用 `ThreadUtils`；AI 的上限、拒绝策略和慢连接隔离不能直接借用公共业务工作池。 |
| `AiProperties` | 集中读取 AI 并发、次数、超时、上下文预算及提示词启动配置，并校验运行限额。 | 沿用 DM 的 `@Value("${配置名:默认值}")` 字段注入，保留正数校验，没有独立配置读取框架。 |
| `AiCapacity` | 原子检查全局/用户/引擎/探测四个维度的运行限额。 | 使用 JDK 同步和 Map；普通线程池或单个 Semaphore 不表达多维原子准入。 |
| `AiPermit` | 引用计数持有名额，父任务及阻塞子任务全部退出才释放，防止重复归还。 | 使用 `AutoCloseable` 和原子类；Future 超时完成不代表底层 RPC 已退出，不能据此提前释放名额。 |
| `AiMetadataExecutor` | 隔离执行元数据 RPC，传递语言环境，限制超时，持有物理执行名额。 | 使用 `CompletableFuture`、定时器和 DM 元数据服务；普通异步提交不管理 AI deadline 及超时后仍阻塞的 RPC 名额。 |
| `AiRunHandle` | 保存首个 AI 失败原因，转换轮次过期并传播取消。 | 底层直接使用 `ChatStreamControl` 和 DM 异常；保留轮次截止与首次业务失败的优先级，不再保留独立供应商异常。 |
| `AiEventSink` | 浏览器连接独立排队写 SSE，生成轮次/请求/序号、心跳，处理慢写与断开。 | 使用 `SseEmitter`、`JsonUtils`、`ClientDisconnectUtils`；普通 SSE 写出没有 AI 队列、序号、终态及独立 writer 的规则。 |
| `AiEventFrame` | 表示一个待发送帧。 | 队列帧数据类，没有另造通用 SSE API。 |
| `AiRunEvents` | 保存草稿/工具活动快照，先持久化再广播，按序回放并支持重订阅。 | 通用存储和事件通知不管理 AI 生成状态及“记账成功才可见”的回放规则。 |
| `AiActiveRun` | 聚合本机运行、名额、句柄、事件和发布/worker 完成标记。 | 是运行数据；一个 Future 的完成标记不能同时表达终态发布与物理 worker 退出。 |
| `AiRunFollower` | 保存跟随其他节点运行的轮次及浏览器订阅。 | 是跟随数据，与本机持有 worker/名额的运行对象职责不同。 |
| `AiPlatformAccess` | 获取库/元数据，裁剪模型可见列与主外键，并在读取前后复核权限。 | 直接调用 `DmAuthServiceForBiz`、`DmResAuthService`、`DmDsConfigService`、`DsSchemaService`；保留的是模型结构投影和复核时机，没有另写身份或权限算法。 |
| `AiMetadataContext` | 保存当前运行的 DM 身份、库层级、objectRef、游标及结构快照。 | 库层级直接使用 DM `DsLevels`；引用只在当前运行有效，不能用全局缓存代替。 |
| `AiCandidateCursor` | 绑定搜索词和上一对象，阻止跨搜索复用游标。 | 是运行内游标数据；DM `PageObj` 不表达搜索绑定与不透明引用协议。 |
| `AiMetadataTools` | 搜索授权对象、返回分页引用、按引用取结构。 | 元数据及权限复用 DM；这里处理工具引用协议与当前轮缓存。 |
| `AiSqlTools` | 把当前运行的库和取消句柄绑定到元数据工具方法。 | 该状态不能放进共享单例，否则运行之间会串库或串取消。 |
| `AiGenerateSqlTool` | 声明 SQL 交付工具，接收 SQL 并返回结构化 artifact。 | 使用 Bean Validation；不执行或重写 DM SQL 能力，注解方法就是工具入口。 |
| `AiAskUserQuestionsTool` | 校验问题/选项，构造交互卡片和问题 ID。 | 使用 Bean Validation、JDK UUID；模型暂停等待回答的契约不是通用表单绑定。 |
| `AiToolService` | 按库上下文装配工具，执行并把可修复错误反馈模型。 | 使用注册器、DM Validator 和统一异常；工具可用性与取消/错误反馈是生成流程规则。 |
| `AiPromptBuilder` | 组装 SQL 职责、数据库上下文、反馈和用户时区。 | JSON/时区直接使用 DM/JDK；提示词内容及顺序属于 SQL 助手业务。 |
| `AiConversation` | 组装历史回放、token 预算、工具配对及压缩材料。 | 使用 `JsonUtils`，直接缓存 jtokkit 编码器；DM 普通历史列表没有模型预算及摘要锚点语义。 |
| `AiModelClient` | 按预设选模型客户端、组装凭据/地址，直接传播供应商异常。 | 使用 DM 异常及通用处理器读取的凭据；接口与唯一实现已合并，是应用与模型协议装配边界。 |

五个实际供应商客户端分别保留：`OpenAiStreamingChatClient` 构造标准 wire 请求；`DeepSeekStreamingChatClient` 设置 thinking；`KimiStreamingChatClient` 设置固定参数及思考回放；`MiniMaxStreamingChatClient` 处理参数限制和敏感标志；`DashScopeStreamingChatClient` 构造 SDK 原生请求及通道路由。兼容厂商继承 OpenAI 实现，不复制 HTTP、JSON 或加密能力。

协议数据 `ChatEndpoint`、`ChatRequest`、`ChatResponse`、`ChatToolCall`、`ChatPartialToolCall`、`ChatUsage`、`ChatStreamSnapshot`、`ChatStreamInterruption`、`ChatStreamListener`、`ChatToolSpecification`、`ChatToolOutcome` 表达明确字段；`StreamingChatClient`、`ChatStreamDecoder` 均有不同实际实现；`ChatToolExecutor` 直接完成唯一的反射执行流程，没有单实现接口或外部执行器注册入口。供应商直接抛 DM `ThirdPartyApiException`，不再定义 `ChatError`、`ChatStreamException` 或供应商错误码到业务错误码的映射。注解、枚举、常量及普通 FO/VO/DO 是数据和契约，不是通用能力包装。

供应商的 9 个错误 key 统一定义在现有 `ChatProtocolConstants` 中，既是 DM 国际化 key，也是原有对外错误码；`AiError` 仅保留会话、权限、配置和运行等业务错误，不再重复列出供应商错误。模型 HTTP 状态、DNS、超时及协议错误在实际边界直接构造 DM 异常并保留 cause。`AiModelClient` 原样传播，轮次终态直接保存 `messageKey` 及 `DmI18nUtils` 解析的消息，探测和普通全局异常也复用同一国际化 key。已有历史错误码和消息不变，无需迁移。

ReAkh 的 `OpenAIErrorDecoder` 使用具体上游异常，`ChatUpstreamExceptionSupport` 仅对限流、重复生成及超时转换业务提示；这里沿用其按实际故障分类的思路，直接使用 DM 已有异常机制，没有照搬另一套异常类或一一对应的错误枚举。

会话、引擎、探测、配置、存储及维护 Service/Mapper 是实际业务入口与数据库操作，直接使用 Spring、MyBatis 和 DM 设施；没有 AI 自定义 TypeHandler、JSON、身份、权限策略或加密包装类。

前端四个辅助文件也已核对：`index.js` 在 DM request 上保留空白并转换 AI 业务错误；`aiStream.js` 解码轮次事件、校验序号并管理恢复/超时；`aiMarkdown.js` 处理模型换行和安全 Markdown；`aiShareImage.js` 负责会话图片选取及排版。HTTP、登录失效和流式请求入口已直接复用 DM HTTP 层。

此次删除 `ChatTokenEstimator`、两种 OpenAI Customizer 接口及两个空实现、DeepSeek/Kimi/MiniMax 三个请求 Customizer、MiniMax 响应 Customizer，以及合并后的 `AiModelClientImpl`，共 10 个类型。token 算法及厂商行为不变，jtokkit 依赖只留在实际调用它的 `cgdm-console`。

## 轮次执行与订阅职责

- `AiTurnController` 是会话及轮次的 HTTP 入口，处理参数校验、DM 请求身份、权限注解和 SSE 响应；生成、恢复和取消交给 `AiTurnService`，会话展示交给 `AiSessionService`，删除交给 `AiTurnStore`。字段 `turns`、`sessions`、`store` 分别对应这些职责；接口统一使用 `/api/entry/ai/turn` 前缀，普通 JSON 请求、生成及恢复 SSE 和对应拦截器使用同一路径。
- `AiTurnService` 管理受理、幂等、运行名额、本机 `active`、取消、异常终态和 worker 释放；数据库终态先提交，再发布事件，取消或超时与正常完成竞争时仍由 `AiTurnStore` 的条件更新决定胜者。
- `AiGenerationService` 管理单轮模型请求、续写、工具循环和压缩；`generate` 只编排步骤，请求准备、响应续接、截断续写、工具批次和完成分别处理。每次调用新建 `AiGenerationState`，只保存本轮次数、续写标记和累计用量，单例不保存这些可变状态。未引入 Planner/Action/Executor 接口层。
- `AiConversation` 统一 assistant、工具请求及工具结果消息；`AiToolService` 校验批次并路由本地工具或隔离元数据执行器；`AiMetadataTools` 合并对象/列引用。`AiSessionService` 组合 DM 权限复核，保存引用及组装最终结果，网络调用和 SSE 写出不进入存储事务。
- `AiStreamService` 独立管理浏览器连接及持久化日志跟随，不持有本机运行 Map；恢复使用原轮次，浏览器断开不取消生成。`AiRunEvents` 统一工具事件和终态 payload，实时发布、已结束轮次恢复和跟随收尾使用同一组装逻辑，结果发送前复核权限，内部模型回放不返回前端。
- `AiSessionMaintenance` 是唯一的维护调度入口：连接轻量 tick 在 Timer 上执行，本机状态检查和日志跟随每秒派发到 Control，过期扫描保持两秒间隔。只有一轮维护在执行，Timer 不读取数据库，不关闭或释放仍在物理执行中的模型/元数据任务名额。
- 压缩保持生成服务中的独立流程，选取与消息重建沿用 `AiConversation`，摘要保存沿用 `AiTurnStore`；没有新增压缩 Service、表、配置、接口协议或 migration。

## 服务预设

服务预设只选择厂商调用通道和表单默认地址，模型 ID、API Key 和地址均由用户配置。地址可修改，以支持不同区域或代理端点；切换预设会填入对应默认地址，不覆盖模型 ID。

| 预设 | 默认 API 地址 | 官方文档 |
| --- | --- | --- |
| DeepSeek | `https://api.deepseek.com` | [API 文档](https://api-docs.deepseek.com/) |
| OpenAI | `https://api.openai.com/v1` | [API 文档](https://platform.openai.com/docs/api-reference/chat) |
| Kimi | `https://api.moonshot.cn/v1` | [快速入门](https://platform.kimi.com/blog/posts/kimi-api-quick-start-guide) |
| MiniMax | `https://api.minimax.cn/v1` | [OpenAI SDK](https://platform.minimax.cn/docs/api-reference/text-openai-api) |
| DashScope | `https://dashscope.aliyuncs.com/api/v1` | [各区域 API 地址](https://www.alibabacloud.com/help/en/model-studio/base-url) |

## 流式契约

- `stream` 是阻塞调用，运行在专用有界模型 worker；文本增量回调须快速返回。
- `ChatEndpoint` 由宿主提供最终 URL 和解密后的凭据，没有输出凭据的 `toString()`。
- 不限制域名、IP、网段、端口或 HTTP/HTTPS；使用 OkHttp 默认 DNS、代理选择、协议协商和重定向。
  保留标准 TLS 证书校验、超时和取消；禁用连接失败自动重试，避免重复提交模型请求。
- HTTP connect 30 秒、read 600 秒、write 120 秒分别限制建连、读闲置和写阻塞；模型调用总时限直接使用本轮剩余 deadline，不叠加固定 1800 秒上限。整轮流式期限由 `clouddm.ai.turn-seconds` 配置，默认 3600 秒。连接测试保留 30 秒总期限及 35 秒异步响应期限；元数据读取使用 `clouddm.ai.metadata-seconds`，默认 10 秒。SSE 保留慢写断开、心跳、受理与闲置检测，用于资源释放和重连，不改变后台轮次 deadline。
- 正常连接关闭后结算响应，`[DONE]` 是标记而不是唯一完成条件。OpenAI 缺失/未知结束原因保留为空，MiniMax 缺失/未知结束原因按内容过滤处理，DashScope 缺失结束原因按 STOP 处理，空白标记不覆盖已收到的终态；多候选取第一项，无工具但有正文的 `tool_calls` 终态仍保留正文。多模态响应只拼接字符串 text 项。工具参数分片先聚合再解析，半截工具 JSON 不算完整调用。
- 普通短文本增量立即回调，仅暂存可能跨帧的 `<think>` / `</think>` 前缀；不固定扣留正文末尾字符。assistant 仅思考的历史记录按 ReAkh 保留显式空 `content`，DashScope 文本/多模态通道都支持空正文的工具续接。OpenAI 即使工具列表为空也保留显式 `tool_choice`。
- `ChatStreamListener` 分别接收正文、思考增量及中断通知。启用 `showReasoning` 时发送独立思考增量；普通正文不会混入思考标签。保留仅接收正文的调用入口，当前 SQL 助手仍按 INSTANT 模式调用。
- 流读取失败、取消或超时时，中断通知携带 `ChatStreamSnapshot`：已接收正文、思考、原始工具片段和用量；工具参数可能不完整，不可执行。主动取消的通知使用 `cancelled=true`、`error=null`；HTTP 408 归类为超时。通知后仍抛出终止异常，不将部分结果伪装成成功，也不自动重发模型请求。
- OpenAI 的 HTTP/SSE `ContentFilter` 错误以及 DashScope `DataInspection`/`ContentFilter` 错误统一转成内容过滤终态。OpenAI 保留过滤前的文本快照；DashScope 与 ReAkh 一样返回空正文并保留用量。
- GPT-5 按 ReAkh 规则转换 `max_completion_tokens`、采样参数及 `reasoning_effort`；显式 completion 上限优先。DashScope 转发扩展参数，支持 `output.text` 与 `choices.message`，文本/多模态 token 用量保留缓存输入与推理 token 明细。
- 思考增量独立聚合，不作为回答正文发送；工具调用、普通答案和截断续写的 assistant 消息均保存完整思考内容，参与本轮续接、持久化及历史回放。标准 OpenAI 请求移除该扩展字段，避免切换模型后发送不支持的参数。
- 引擎连接测试不设置 256-token 的输出上限，沿用服务默认值，仍受测试 deadline 和取消约束；思考 token 不会挤占过小的工具输出预算。
- 工具调用使用 AUTO；普通文本 `stop` 直接完成回答，生成的 SQL 通过 `generateSql(sql)` 工具独立交付，保留复制和插入编辑器操作。
- 不设置额外的响应字节、JSON 深度或 SQL 长度上限。工具参数仍遵守类型和必填字段契约，扩展参数不能覆盖协议保留字段。
- 普通模型调用不强制输出 token 上限，交由模型默认值处理；只有摘要设置独立的输出预算；连通性探测沿用服务默认输出上限。
- 对齐 ReAkh 通用工具模式：默认最多 141 次模型调用、140 次工具调用；摘要请求不占用普通模型调用次数。
- 输入使用 ReAkh 的 16384 字符上限（UTF-16）；会话不设数量、创建频率、轮数或闲置自动清理限制，仅清理主动删除的会话。会话必须先通过创建接口生成新的服务端 UUID；生成和恢复接口只查找已有会话，不自动重建已删除会话。
- 元数据不按表数、列数、注释长度或字节数额外拒绝；上下文回放统一按 token 预算压缩，数据库权限校验仍在读取边界执行。
- 无论成功、失败、取消、超时或回调抛错，都关闭响应并清理连接池和句柄绑定。
  `close()` 拒绝新请求并取消在途请求。

## 引擎名称

引擎以 UUID 作为关联标识；名称或模型 ID 可以单独重复，但名称与模型 ID 的组合必须唯一，以便在选择器中区分。模型配置表以 `(label, upstream_model_name)` 保留名称与模型的组合唯一索引。新增和编辑遇到组合冲突时统一返回 `ENGINE_NAME_MODEL_CONFLICT`，提示修改名称或模型 ID；更新同一条配置不会与自身冲突。

## 并发与依赖边界

- 引擎新增由数据库名称＋模型组合唯一索引防重；编辑、删除通过模型版本条件判断竞争结果，两者都按模型、供应商顺序写入。版本竞争失败不修改供应商，后续供应商操作失败回滚模型修改；不同引擎独立提交，不引入显式供应商锁，也不查询或锁定用户表。
- 会话行锁串行化同一会话的受理、重生成、切换模型、摘要与删除；轮次锁使用数据库当前读，防止终态覆盖；运行行锁保护事件序号分配。它们只围绕对应会话和运行记录，不能用进程锁替代跨节点一致性。
- 运行内事件状态与订阅快照需要同步；浏览器写队列只在入队、取帧和关闭时持锁，实际 SSE 写出在锁外。运行名额的多维计数及引用释放保留同步保护；AiPermit 的关闭标记由同一对象锁保护，不再叠加原子变量。
- 权限、数据库元数据、加密与配置通过 DM 对应服务复用；模型协议模块不依赖用户表、Console 或启动模块。引擎保存/删除不借用平台账号或其他业务记录作为锁。

## 当前业务边界

- 每条历史只有一个运行，由 `history_id` 唯一索引约束。重新生成删除目标及后续历史后创建新的历史和运行；不保留 ReAkh 复用同一历史、选择最新运行的分支。数据库超时查询直接按 deadline 排序并分页，服务只组装对应记录。
- 引擎没有隐藏、停用或手动排序入口，因此不保存模型的 `visible`、`enabled`、`sort_order` 或供应商的 `enabled`；列表按名称和模型代码稳定排序，删除通过现有模型版本和事务处理。
- 运行不保存节点归属或数据库心跳，也不周期性续写这些字段。运行异常中断后按原 deadline 收敛为过期；没有 ReAkh 的节点失联提前回收。SSE 的 10 秒注释心跳、当前节点运行检查、取消传播及持久化事件恢复继续保留。
- 受理、取消等事务复用已经锁定的会话和历史，事件追加只读取运行并锁定所属会话及运行行。当前读、终态条件及事件序号保护继续保留。资源权限使用 DM 通用检查，数据源由 `parseLevels` 读取；同一次检查不重复解析，元数据读取后、模型发送前及结果发布前仍复核授权。
- 用户答案使用唯一的 snake_case 字段协议，不接受旧 camelCase 别名。提示词直接由 `AiPromptBuilder` 构造。结构化结果不包含预留的 `warnings` 字段；历史旧警告原文并入对应回答正文后再移除字段，原 SQL、答案和工具回放内容保留。

## 与 ReAkh 对齐的存储

- `dm_ai_provider_config` 保存供应商适配类型、API 地址和加密凭据；`dm_ai_model_config` 保存模型代码、名称、供应商关联、版本及验证信息；不预留尚未使用的思考开关或默认模型字段。内部主键为自增 BIGINT，公开模型代码保持 UUID，现有模型选择和历史关联不变。
- `dm_ai_session` 使用自增主键及公开会话 UUID，保存标题、模型关联、压缩摘要和历史锚点。活动状态查询运行表，上下文用量在已有组装逻辑中计算；开始和结束运行时仅刷新会话修改时间，不重复保存状态或用量。租户、数据库上下文及权限仍由 CloudDM 的既有边界负责。
- `dm_ai_history` 保存用户消息、数据库上下文、模型快照、引用和用量；`dm_ai_event` 保存结构化结果、原生工具回放消息和独立的用户提问答案。答案受理与新历史、新运行记录在同一个事务提交，重新生成时删除后续历史、运行和两类事件，并清除派生摘要。
- `dm_ai_turn_run` 保存独立运行 ID、唯一历史关联、状态、事件序号、错误和 deadline；`dm_ai_turn_event` 保存有序流式事件，运行 ID 与序号组合唯一。事件先持久化再发送，事件写入和序号推进同一事务提交。
- 重连先折叠事件日志生成快照，随后继续订阅当前运行；本节点没有运行对象时，按已读取的日志序号追踪新事件，不重新调用模型。订阅期间重新校验数据库上下文和用户权限，结束、断线及服务停止后释放追踪。
- 已完成会话删除时，在同一个事务中删除会话、历史、运行及关联事件；运行中会话不能删除。没有会话数量、闲置时间或历史轮数清理限制。
- 所有 AI 启动参数由 `AiProperties` 集中注入，提示词启动覆盖沿用原配置名和动态配置优先级。所有模型共用 `AiProperties.contextTokens`：启动参数 `clouddm.ai.context-tokens`，默认 256000，允许 8192～1048576；修改后重启生效。生成和历史回放直接读取该参数，不建上下文配置表，也不提供页面编辑入口或配置保存接口。
- 供应商凭据和会话内容统一使用 DM 通用加密；加解密在 JDBC 映射边界完成，结果与用户答案在解密后合并，不在 SQL 中解析密文。
- 未发布的 AI 初始化与升级脚本统一为 `V202609300001__ai_sql_assistant`，直接创建最终 7 张表和名称＋模型联合唯一索引，并授予内置 Manager 引擎管理权限；不创建旧表再逐步迁移。

## 存储加密

AI 与 DM 其他模块统一使用 `CryptService` 默认加密和 `StrSecretTypeHandler`。服务层传递明文，mapper 在写入时加密、读取时解密；模型调用直接使用读取出的 API Key。会话正文、上下文、引用、摘要、结构化结果、用户答案和流式事件保持加密存储。结构化结果与用户答案分别通过 DM 通用类型处理器读取，在现有会话服务组装页面及流式结果时合并；模型历史回放直接使用独立的答案字段，数据库映射不组装业务结果。

## 与 ReAkh 对齐的工具层

- 通用层通过 `@ChatTool` / `@ChatToolParameter` 自动生成工具规格，与 `ChatToolExecutor` 按名称配对注册。参数类型转换和 Bean Validation 由 `ChatToolExecutor` 直接完成，仅注册实际内置的注解工具。
- 每轮创建独立注册上下文。`generateSql` 和 `ask_user_questions` 始终注册，不依赖选择数据库；有当前数据库时再注册搜索表和读取结构工具，元数据工具绑定本轮数据库和取消句柄，没有共享的可变会话状态。
- 模型工具参数聚合完整后，按顺序解析注册的执行器；普通工具执行结果作为 `role: tool`、原 `tool_call_id` 回传，再继续流式调用模型。
- 未知工具、参数错误和普通工具失败以 ReAkh 的 `Error: ` 结果交回模型，不再整批拒绝或仅允许一次参数纠正；取消、超时和数据库授权失效仍收敛终态。
- `generateSql` 的必填入参 `sql` 是实际 SQL 文本，不是生成要求；执行器仅返回结构化 `{sql}`，不执行 SQL、不读取数据库。前端解析每次成功工具活动中的 `result.sql`，将同一轮成功批次按调用顺序追加到同一个 SQL 卡片。最终聊天正文不再提取 SQL 围栏，也不承担 SQL 交付。
- `ask_user_questions` 成功后结束本轮并等待用户；问题卡不作为工具结果回传。回答使用 ReAkh V2 协议，含 `request_id`、`request_history_id`、`answer_text` 及带问题原文的 `answers`，受理新轮时在同一事务保存到原提问记录。回放用问题的 `request_id` 配对 assistant 工具调用和完整答案的 tool 消息，再接续用户回答轮；未回答的问题不回放未配对工具请求。
- 回答展示使用引用问题与答案的格式。提交答案保留输入框草稿，不携带 SQL 选区；受理失败恢复问题状态。重复提交、旧问题和跨会话回答在受理边界检查，回答轮重新生成会重新建立答案关联。
- 当前注册 `generateSql`、`ask_user_questions` 和两个只读元数据工具。SQL 交付沿用 ReAkh 文件导出的工具结果独立展示方式，未增加文件工具、MCP 服务器配置或沙箱入口。

## 与 ReAkh 对齐的会话链路

对照 `reakh-console` 的 `ChatController`、`ChatServiceImpl`、`ChatRunManager`，以及
`reakh` 前端的 `services/chatService.js` 和聊天组件核对现有 SQL 助手交互。

| 行为 | CloudDM 实现 |
| --- | --- |
| 会话创建 | `createSession` 同步校验模型并写入会话，直接返回 `ResWebData`；没有独立的库表选择接口 |
| 流式接口 | `streamTurn` 和 `resumeTurn` 直接返回 `SseEmitter`，禁止代理缓存和变换；受理前错误通过 SSE error 返回 |
| 受理确认 | 首个 `started` 事件确认请求已受理；HTTP 200 本身不代表生成已开始 |
| 独立生成 | `AiActiveRun` 拥有模型运行句柄；`AiRunEvents` 保存状态和订阅者，关闭助手或切换会话只断开订阅 |
| 断线恢复 | `resumeTurn` 订阅原请求，先回放快照，再接收实时事件；相同 `streamClientId` 替换旧订阅，不重复调用模型 |
| 恢复展示 | 文本增量、阶段、工具活动和上下文占用随快照保存；成功、失败、取消均保留已有过程 |
| 超时和心跳 | 浏览器受理等待 60 秒，流闲置 40 秒；服务端 10 秒心跳。整轮 deadline 仍由模型运行控制 |
| 会话操作 | 生成时可新建或切换历史；关闭后重开恢复当前会话。消息编辑、重生成、模型切换沿用已有操作 |
| 历史和消息 | 历史搜索、滚动加载；消息向上滚动加载更早记录，加载后保留滚动位置 |
| 上下文管理 | 全局预算默认 256k，预留 10%，近期尾部预算 10% 且限制在 2048–12000；显示记录不混入模型回放 |
| 重复提交 | 同一 requestId 恢复现有请求；请求内容冲突仍报错。只有明确的重新生成创建新一轮 |

当前会话通过 Vuex 和路由的 `aiSession` 参数保存；整页刷新后重新打开助手可继续订阅。
跨节点恢复：追踪原运行的持久化事件日志，按序恢复并订阅后续事件，终态以运行记录与结构化历史事件为准。
CloudDM 保留自身登录、数据库授权、SQL 元数据工具和插入编辑器流程；不引入 ReAkh 的文件、沙箱或计费功能。

## 提示词与模型回放

参考 `reakh-console` 的 `origin/feat/ai_v5`（8fb50ac8），提示词统一由 `AiPromptBuilder` 组装：

1. `system`：CloudDM SQL 职责、ReAkh 通用行为/语言/响应规则；只有当前数据库提供元数据工具时，才附加工具使用和编排规则；末尾补充按浏览器时区计算的日期、时区。
2. 历史压缩摘要：使用 ReAkh 的 `<compacted_context>` 包装，声明不是新的用户请求，较新的原始消息优先。
3. 未压缩历史：原始用户文本后追加应用的 `<runtime_context>`，包含消息发生时间、该轮数据库方言与命名空间、选中 SQL；随后按顺序回放原生 `assistant.tool_calls`、`tool` 结果和原始 Markdown 答案。
4. 当前请求：同样保留用户原文，以应用上下文后缀提供当前数据库和 SQL 选区；内部数据源路径不放进提示词。数据库信息不再重复拼接到 system。
5. 当前工具执行：完整调用/结果批次加入对话后继续请求模型；无需验证的同库追问复用历史工具事实。需要新读结构时，仍使用本轮搜索返回的 objectRef，旧数据库的结构不会被当成当前库的结构。

压缩使用 ReAkh 的 Objective、Important Details、Work State、Next Move、Relevant Files 结构；保留用户修正、未完成目标、工具证据和数据库归属，不把生成 SQL 记为已执行。摘要请求固定 4096 tokens 输出预算。历史轮次和当前完整工具批次统一选择压缩前缀，近期尾部按最多 12000 tokens / 窗口的 10% 保留，并对齐五条文本消息与最小尾部预算的选择规则；无法保留尾部时压缩所有候选块。重建只保留一个摘要，当前用户请求原样保留。仅历史压缩可立即提交已有锚点；含当前轮次的摘要在 COMPLETED / NEEDS_INPUT 终态同一事务保存，锚定该轮历史并加入当前用户与压缩后的增量，失败或取消不写入当前锚点。原始原生工具回放始终完整保存，下一轮通过摘要锚点跳过已压缩轮次，避免重新膨胀。界面过程记录、SQL 工具结果、原始用户问题和最终回答独立保留；内部模型回放消息不会返回到前端。

所有完成或等待用户回答的轮次均保存原生回放消息；提问答案从独立的 `USER_INPUT_ANSWERED` 事件读取，再作为对应工具响应续接，不扫描后续消息补答案。已合并的开发数据按当前格式一次性整理，运行时不保留旧历史格式的兼容分支。

## 输出截断续写

处理模型 `finish_reason=length` 时遵循 ReAkh 的续写策略：保留部分文本和用量，以原生 assistant 消息参与下一次请求，并添加仅用于本次请求的 runtime control 指令续写一次。SQL 由工具分批交付完整语句；已成功批次不重复生成。普通正文续写保留已接收文本，不使用末行片段推断或手工拼接。截断批次中的工具调用全部丢弃，不解析、执行不完整参数。

续写仍被截断时，使用既有会话及工具证据作为材料，禁用工具，单独生成完整最终答复；旧部分聊天正文不与该答复拼接，但已经成功返回的 SQL 工具结果不受替换影响。最终答复仍被截断时返回 `MODEL_OUTPUT_TRUNCATED`，不保存为成功结果；失败、取消和超时仍通过 `dm_ai_turn_event` 的持久化事件保留已经成功交付的 SQL 批次。取消、超时、用量累计和断线恢复继续由同一轮次管理，续写不会新建轮次或重复用户请求。

## 验证

### 生成链路日志

`AiTurnService` 记录轮次受理、worker 退出及失败终态；`AiGenerationService` 记录模型请求、工具执行、上下文压缩和结果保存；`AiStreamService` 记录事件追踪失败。通过 `sessionId`、`turnId`、`requestId` 关联。
`AiModelClient` 记录模型/引擎、消息与工具数量、请求输出预算、响应字符数、token 用量和耗时；普通请求的 `maxOutputTokens=null` 表示沿用模型服务默认输出上限。
`OpenAiStreamParser` 记录实际 `finishReason`、累计响应字符数、工具数量、已收到的 usage、未完成帧大小和解析耗时，区分模型停止、长度截断、过滤及流协议错误。
usage 尚未返回时记录 null，不把缺失用量当作 0。日志不打印 API Key、完整提示词、SQL 正文或工具参数/结果正文，不逐 token 打印。

```bash
cd backend
./gradlew :ai-common:build :ai-openai:build :ai-deepseek:build :ai-kimi:build :ai-minimax:build :ai-dashscope:build :cgdm-dao:build :cgdm-console:build
```

模型协议与供应商模块无测试类，`test NO-SOURCE` 不能视为行为验证。流式行为通过隔离 HTTP 服务与临时 JShell
检查，覆盖分片、终态、取消、超时、资源释放、模型参数隔离、HTTP 回环连接、代理和重定向。
隔离 HTTP/SSE 检查覆盖 Kimi 采样参数、MiniMax thinking 与敏感过滤、独立/内联思考分片、工具结果续接、空思考字段回放、原生 DashScope 文本/多模态消息及工具分片、usage 明细、GPT-5 参数归一化、HTTP/SSE 过滤错误、原始工具中断快照、取消、超时和关闭后的拒绝调用，并同时回归 OpenAI / DeepSeek。没有对应厂商的 API Key 时，不能将此检查视为真实厂商服务验收。真实模型测试通过 AI 配置页面执行；不会发送数据库内容。

模块依赖图变化后执行 `cd package && ./all_build.sh` 并通过 IDEA 重启原配置。
发行包验证使用 `./gradlew -Pprofile=dev -Ptarget=all :pkg-alone:tgz :pkg-console:tgz :pkg-sidecar:tgz -x test`，
确认 Alone/Console 的 `libs/` 包含上述六个模型协议/供应商模块，`plugins/` 和 Sidecar 不包含 AI 模块。

SQL 工具结果随活动快照持久化，断线重连和历史恢复按原调用顺序显示；聊天最终正文被替换、停止或失败都不会清空已有 SQL 卡片。同一轮成功批次按调用顺序追加到一个 SQL 代码块，批次之间仅保留一处换行，不自动添加空行；复制和插入操作完整的合并 SQL。不同轮次保持各自的代码块，工具过程仍按实际调用记录展示。最终结果中的 `sql` 汇总成功批次，用于整条回复复制；已有历史的汇总 SQL 继续展示，不重复叠加工具结果和最终汇总。工具参数不完整或校验失败不会生成 SQL 卡片。


## 用户提问工具

`ask_user_questions` 参考 ReAkh 的结构化用户提问协议。入参是可选 `title`、必填 `questions`（1–3 个问题）和可选 `submit_label`。问题支持 `SINGLE_CHOICE`、`MULTI_CHOICE`、`FREE_TEXT`；选择题提供 2–3 个选项，`allow_custom` 和 `required` 默认开启。工具返回 `schema_version=2`、唯一 `request_id` 及规范化问题。

此工具必须独占当前响应的工具批次；混合调用时整批返回工具错误，不执行其中其他工具。成功后本轮以 `NEEDS_INPUT` 保存问题卡及原生工具历史，释放生成资源并结束流，等待用户作答。前端提交答案时携带 `userInputAnswer` 中的 `schema_version`、`request_id`、`request_history_id`、`answer_text`、`answers`，连同答案正文开启下一轮。服务端在会话锁内验证问题仍是最新待回答问题，拒绝过期或重复提交；普通新消息会使旧问题失效。刷新和历史会话保留问题及回答，回答后卡片只读。

流式续接与最新 ReAkh 对齐：内容过滤终态丢弃同批工具，保留可见正文，无正文时返回本地化审核提示；MiniMax 缺失结束标记按内容过滤处理，Kimi/OpenAI 缺失结束原因保留为空，DashScope 原生正常关闭默认 STOP。只返回思考内容时重试一次，仍无正文则禁用工具生成最终答复。达到工具上限时执行额度内的调用，为剩余调用写入配对的 skipped 工具结果，然后使用已有材料生成最终答复；已交付 SQL 保留。模型调用上限内预留最后一次请求生成最终答复。

## 旧存储残留清理

配置在 `AiEngineService` 中按单表查询结果组装，供应商的 `access_token` 由 DM 通用处理器读取；会话的活动轮次在 `AiTurnStore` 中由单表读取的运行和历史记录组装，不依赖会话表中的活动轮次列。SQL、提问答案和流式恢复均使用当前历史/事件表；未发布的旧 migration 与旧表没有运行时读写入口。快照事件用于正文替换和断线恢复，是当前事件协议的一部分。

元信息工具直接使用当前请求的数据库上下文与权限，不保留旧 `AUTO` / `PINNED` 范围或固定表名单；表与列注释随真实元信息返回，不保留始终启用的注释开关。前端提问答案只读取当前问题的独立答案事件，不再从后续消息推断；已完成 SQL 读取结构化结果，生成中或失败时仍保留事件中已交付的批次。


工具续接与上下文压缩对照 ReAkh `origin/feat/ai_v5`（`ea6b1181`）。completion gate 与 ReAkh 一致：结束原因声明工具调用但没有请求时最多重试两次，随后禁用工具生成最终答复；实际工具批次会重置计数。每跨过 30 次调用附加效率提示，工具仍可使用。最终答复和 LENGTH / CONTENT_FILTER 响应中的工具请求均不执行。上下文估算按原始文本、消息及工具参数计算，排除 reasoning_content 和消息 JSON 包装，并包含本次实际续接/最终答复指令；达到 90% 窗口即压缩。

## Prompt 组装

提示词统一位于 `backend/clouddm-platform/cgdm-console/src/main/resources/ai/prompt/`。通用工具及错误恢复规则不依赖数据库选择；仅表结构工具规则按数据库上下文启用。SQL 只通过 `generateSql` 交付完整语句批次，不执行 SQL。

最终答复使用独立的精简系统上下文，只保留安全、语言、回答格式、SQL 交付和运行时信息；历史整理成与 ReAkh 一致的 transcript，移除思考及供应商回放字段，单条工具结果保留前 2000 字符。原始历史和已交付 SQL 产物不受材料裁剪影响。

前端每次新生成请求传入 `timezone` 和 `timezoneOffsetMin`，随当轮上下文保存。时区优先采用有效 IANA 名称，其次使用浏览器偏移，未提供或无效时使用 UTC，与 ReAkh 一致。

以下运行提示依次使用 `SystemDal.fetchSystemConf()` 系统配置、`@Value` 注入的同名启动配置和资源默认值；空白覆盖回退资源默认值：
- `STREAMING_FINAL_ANSWER_DIRECTIVE_PROMPT`
- `STREAMING_THINK_ONLY_RETRY_PROMPT`
- `STREAMING_TOOL_LIMIT_REACHED_PROMPT`
- `STREAMING_TOOL_LIMIT_FAILURE_MESSAGE_PROMPT`
- `STREAMING_TOOL_WARNING_PROMPT`（保留 `{threshold}` 占位符）
