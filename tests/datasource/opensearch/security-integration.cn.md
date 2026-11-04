# OpenSearch 权限、只读、审计与脱敏验收

本文件是第 7 步的长期用例。应用、数据库账号、页面及下载文件验收均为 **NOT RUN**；开发期间不启动 CloudDM、不运行 Flyway、不连接实库。下述配置由统一验收时在隔离环境准备，不作为自动执行脚本。

## 当前边界

- OpenSearch 独立注册 `SecRulesSupportSpi`，支持索引级查询规则、索引/字段级脱敏范围。公共权限、审计、结果缓存、脱敏和导出服务没有新增 OpenSearch 分支。
- DSL 的行为关系携带主索引及所有关联索引；平台按主操作检查 READ / WRITE / DDL，关联查询索引单独检查 READ。执行端重新解析并比对授权关系及完整索引列表，缺项、改写后范围不一致或行为被篡改均拒绝。
- 查询目标必须是精确普通索引；别名、数据流、通配符、多索引、跨集群、Bulk/MSearch、编码/存储查询及未知插件查询仍拒绝。`terms lookup`、`indexed_shape`（缺省索引为 `shapes`）、MLT 的 `like/unlike` 均计入关联资源；嵌套排序、highlight_query、rescore、聚合过滤条件同样分析。
- 文档写入要求索引已存在；现有索引的默认/最终 Ingest Pipeline、默认 Search Pipeline，以及 semantic / model_id 等隐式资源按执行检查拒绝。创建索引先读取组合模板和传统模板；**只要命中模板就拒绝**，包括仅有普通配置的模板。首期不合并组件模板、优先级、模板别名和 Mapping 的继承语义；由管理员在数据库侧准备这些索引。
- 3.3+ 的搜索类执行还读取集群配置，检查 `cluster.search.enabled_system_generated_factories`，按 transient > persistent > defaults 取有效值。启用任何工厂或配置不可读/格式不符均拒绝；旧版本不读取这一配置。
- 使用平台脱敏时，聚合、fields/docvalue_fields/stored_fields、script_fields、highlight、collapse、explain、profile，以及非 HEAD 的索引定义/Mapping/Settings/Alias 原始输出在发送前拒绝。响应端继续拒绝 inner_hits、未知扩展字段及非对象 `_source`；sort 值不进入结果。文档通过 `_source` 的 `JSON_DOCUMENT` 标记进入现有逐叶字段/整行脱敏链路；写结果消息只显示操作与计数，不回显 get._source 等内容。
- 服务端错误只输出本地错误类型/HTTP 状态，不回显错误正文中的文档、脚本或敏感值。失败消息使用 `notify=true` 驱动既有审计终态；by-query 部分失败保留实际影响计数，仍标失败。取消/超时不是成功，也不意味着服务器写入已回滚。

## 必须保留的限制

1. 按本轮用户决定，**不修改共享脚本语言规则**。现有内联脚本能力保持不变；`knn` 语言和存储脚本仍按原规则拒绝，但未新增自定义语言白名单。第三方脚本引擎的外部资源行为尚不能保证完整分析，不能把它标记为已通过安全验收；发布前需另行决定这类引擎的接入边界。
2. 元数据预检查与执行不是同一服务端事务。索引/别名、Pipeline、模板或插件配置被其他管理员并发修改时，客户端不能提供原子的资源范围保证；数据库账号权限仍是最终边界。验收环境须固定这些配置；文档并发写入可继续按分页用例验证。
3. 当前检查会增加数据库账号的只读元数据权限要求，不以管理员账号代替验证。模板列表和集群配置只用于内部检查，不作为查询结果暴露。缺少预检查权限时明确失败，不静默跳过或重试到其他账号。
4. 平台结果缓存保存文档，展示、单元格读取和文件转换在各自出口执行现有脱敏。单测不能证明所有真实缓存和下载入口均已通过；必须执行下面的出口检查。直接缓存存储的访问控制沿用平台既有机制。

## 命令与权限对照

| 请求 | 主索引平台权限 | 关联索引 | 只读会话 | 审计/计数 |
| --- | --- | --- | --- | --- |
| GET/POST `_search`、`_count`；GET/HEAD 文档或 `_source` | READ | 按实际引用逐个 READ | 允许 | 查询/读取结果；部分失败为失败 |
| GET/HEAD 索引、GET `_mapping` / `_settings` / `_alias` | READ | 无 | 允许 | 元数据；脱敏会话非 HEAD 拒绝 |
| POST `_doc`、PUT/POST `_create/{id}`、op_type=create | WRITE / INSERT | 无 | 拒绝 | created=1 |
| PUT/POST `_doc/{id}` | WRITE / REPLACE | 无 | 拒绝 | created/updated=1 |
| POST `_update/{id}`（含 upsert/script） | WRITE / UPDATE | 无 | 拒绝 | updated/created/deleted=1，noop=0 |
| DELETE `_doc/{id}` | WRITE / DELETE | 无 | 拒绝 | deleted=1，not_found=0 |
| POST `_update_by_query` / `_delete_by_query` | WRITE / UPDATE 或 DELETE | 查询引用逐个 READ | 拒绝 | 保留 updated+deleted 或 deleted；冲突/部分失败标失败 |
| PUT/DELETE 索引；PUT `_mapping` / `_settings` | DDL / CREATE、DROP、ALTER | 无；隐式资源拒绝 | 拒绝 | 成功确认，影响计数为 0 |

WRITE 是平台既有的写权限，包含更新脚本删除文档和 upsert 的写语义；操作分类用于审计/查询规则，不宣称平台存在独立的 INSERT/UPDATE/DELETE 授权项。

## 隔离环境与账号准备

- 版本至少覆盖 2.x、3.2、3.3+；3.9 作为用户环境基线。绑定真实数据库版本，不修改客户端配置冒充版本。
- 准备 `books`、`lookup`、`shapes`、`examples`，普通文档含 `people[].secret`、`people.secret`、空数组/null、长字符串、大整数和精确小数。分页样本沿用[分页与导出验收](pagination-export.cn.md)，至少 2 个主分片。
- 准备平台普通用户：仅 books READ、books WRITE 无 lookup READ、books+lookup READ、DDL、脱敏豁免；避免只使用平台超级管理员。
- 准备数据库账号：仅查询及必需元数据、文档读写及必需元数据、缺少 lookup 读权限、缺少模板读取、缺少 3.3+ 集群配置读取，以及拒绝所有请求的账号。
- 账号权限由管理员在数据库侧按实际接口授予。需验证的内部读取包括根产品探测、`/_resolve/index/{index}`、`/{index}/_mapping`、`/{index}/_settings`；创建额外读取 `/_index_template` 和 `/_template`；3.3+ 搜索额外读取 `/_cluster/settings?include_defaults=true&flat_settings=true`。分页还需要对应 Scroll/PIT 创建、读取、清理权限。
- 开启平台查询审计、字段脱敏及整行脱敏规则，确认规则中的索引/字段范围能选择 OpenSearch。准备有/无脱敏豁免两个用户。

## 统一验收步骤

| ID | 操作 | 必须观察的结果 | 状态 |
| --- | --- | --- | --- |
| SEC-01 | 按权限对照逐条执行，包括 POST 查询、脚本 upsert/delete、索引管理 | POST 查询仍 READ；WRITE 与 DDL 不混用；无权限在平台层拒绝，数据库不收到业务请求 | NOT RUN |
| SEC-02 | books 查询引用 lookup；再依次用默认 shapes、MLT like/unlike、嵌套排序、highlight_query、rescore、聚合 filters 引用关联索引 | 任一关联索引缺 READ 都拒绝；补齐权限后执行；安全规则范围也包含关联索引 | NOT RUN |
| SEC-03 | books 有 WRITE、lookup 无 READ，执行带 lookup 的 update/delete by query | 关联 READ 与主索引 WRITE 分别校验；不能用 WRITE 覆盖关联读取 | NOT RUN |
| SEC-04 | 平台允许，数据库账号分别在预检查和业务请求返回 401/403 | 保留拒绝终态，不绕过预检查、不自动重放、不切换账号；后续合法查询可用 | NOT RUN |
| SEC-05 | 配置数据源只读，保存并重开会话后测试全部写命令；检查工作台工具栏 | 不发送写请求，读取继续可用；工作台不提供会话只读切换，数据源只读限制不能被解除；内部会话只读状态切换另由单元测试覆盖 | NOT RUN |
| SEC-06 | 测试别名、数据流、通配符、多索引、远程索引、Bulk/MSearch、wrapper/percolate、存储脚本 | 完整资源无法分析时明确拒绝，不进入业务执行或导出 | NOT RUN |
| SEC-07 | 在主索引分别设置 default_pipeline、final_pipeline、search.default_pipeline，再恢复为 _none | 相关执行拒绝；恢复后成功；关联索引也保持精确解析；错误不得包含 Pipeline 处理的数据 | NOT RUN |
| SEC-08 | 匹配组合/组件模板、传统模板创建新索引；再使用无匹配模板的名字；移除模板读取权限 | 命中模板创建拒绝且索引未创建；无匹配可创建；无权读取拒绝且不执行 PUT | NOT RUN |
| SEC-09 | 3.3+ 启用系统生成 search 工厂，分别置于 transient/persistent/default；测试高优先级空列表；旧版本普通账号查询 | 有效非空工厂列表拒绝；显式高优先级空列表允许；不可读拒绝；旧版本无需额外集群读取 | NOT RUN |
| SEC-10 | 创建带 semantic、knn_vector model_id、动态模板隐式 Mapping 的隔离索引，尝试查询/写入或 PUT Mapping | 按当前隐式资源限制拒绝；普通字段名如 model_id、script、query 不被当成资源关键字 | NOT RUN |
| SEC-11 | 普通、空结果、GET 文档、GET `_source`，分别触发字段/整行脱敏 | 嵌套对象、数组叶值和点号字段正确脱敏；其他类型/精度保持；_id 可按其字段规则脱敏；空结果无泄漏 | NOT RUN |
| SEC-12 | 脱敏用户请求聚合、script_fields、stored/docvalue fields、highlight、collapse、explain、profile、原始索引定义；请求 nested inner_hits | 不可可靠脱敏的输出失败；sort 值不出现；写响应 get._source 与错误正文不回显；没有可下载的泄漏结果 | NOT RUN |
| SEC-13 | 同一结果检查首屏、翻页、长字段/单元格展开、重新读取缓存；再导出 CSV、JSON、Excel（已启用格式） | 所有出口都应用规则；文件中搜索敏感原文不得命中；JSON 类型/数组结构保持；有豁免用户按既有豁免展示 | NOT RUN |
| SEC-14 | size >1000，覆盖旧版 Scroll 和 3.3+ PIT；第二页发生错误/取消；在导出时验证规则 | 与单页同样脱敏；不输出游标/内部排序值；失败标失败，清理本次游标；不能借导出绕过失败或权限 | NOT RUN |
| SEC-15 | 成功写入、409、部分 by-query 失败、超时、断网、取消、游标清理失败，逐项查看平台审计 | 操作类型/索引/实际影响数正确；失败不记成功；一次业务命令不因元数据检查产生多次写审计；状态释放后可继续查询 | NOT RUN |
| SEC-16 | 禁用脱敏插件或让脱敏规则执行失败，再查看/展开/下载受保护结果 | 明确失败，不回退成原始数据；排查实际出口，不以查询成功替代下载验收 | NOT RUN |
| SEC-17 | 自定义脚本语言与外部资源扩展 | 共享脚本规则本轮未改；此项安全范围待决定，不能记录 PASS 或宣称已完整分析 | NOT RUN |
| SEC-18 | 运行现有 ES 查询、规则、脱敏及导出流程 | ES 原有产品策略与行为保持正确 | NOT RUN |

## 自动化证据与复跑

- `OpenSearchSecurityTest`：全部写命令只读拦截、授权资源/行为篡改、关联索引账号拒绝、401/403 不重试与恢复、不可脱敏请求和响应、写消息去敏、两类模板、系统配置优先级/版本边界、隐式模型及安全规则范围。
- `OpenSearchPermissionAnalysisTest`：多版本关联资源解析、默认 shapes、MLT、嵌套查询位置、规则域，以及不透明/多资源请求拒绝。
- `OpenSearchBehaviorPermissionsTest`：平台真实权限分类器把主索引映射为 READ / WRITE / DDL，并将 lookup 保持为独立 READ。
- `OpenSearchDocumentMaskingTest`：调用真实 `SecValueProcessServiceProvider`，验证嵌套数组/点号字段、整行规则、重复处理、非法 JSON 拒绝、豁免及精度。
- 原有执行与分页单测继续覆盖错误通知、实际计数、取消和清理。HTTP 模拟不替代真实数据库授权；脱敏处理器单测不替代真实缓存/导出文件验证。

```bash
cd backend
./gradlew :ds-opensearch:test :sql-es:test :plus-sec-rules:test :ds-es:compileJava :ds-opensearch:customFatJar --console=plain
./gradlew :cgdm-console:test --tests '*OpenSearchBehaviorPermissionsTest' -PbuildFrontend=false --console=plain
```

协议依据：[默认 Search Pipeline](https://docs.opensearch.org/latest/search-plugins/search-pipelines/using-search-pipeline/)、[3.3 系统生成处理器](https://docs.opensearch.org/latest/search-plugins/search-pipelines/system-generated-search-processors/)、[模板继承](https://docs.opensearch.org/latest/opensearch/index-templates/)、[模板模拟仅解析组合模板的实现](https://github.com/opensearch-project/OpenSearch/blob/2.19/server/src/main/java/org/opensearch/action/admin/indices/template/post/TransportSimulateIndexTemplateAction.java)、[模型索引在写入时被读取](https://docs.opensearch.org/latest/vector-search/vector-search-techniques/approximate-knn/)。
