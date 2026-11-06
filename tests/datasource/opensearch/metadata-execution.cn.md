# OpenSearch 元数据与基础命令集成验收

第 5 步长期用例。插件内实现和本地单元验证已接通，以下应用、实库和页面用例全部为 `NOT RUN`，待开发完成后由用户启动应用统一验证。本步骤不启动 CloudDM、不执行 Flyway、不连接共享实库。

## 范围与准备

- 版本沿用[能力矩阵](opensearch-test-matrix.md)，至少在 2.x、3.x 各选一个环境执行全部用例；版本特有字段在相应版本验证。记录服务端真实版本、插件版本、账号权限及执行结果。
- 使用专用索引 `cdm_os_exec_case`、引用索引 `cdm_os_exec_lookup`、空索引 `cdm_os_exec_empty`，不得使用业务索引。创建别名、数据流、Pipeline 和故障条件的准备操作由有权限的测试人员在外部管理工具完成，不计为本期编辑器支持能力。
- 准备普通账号与只读账号。密码、CA、令牌、抓包及原始业务数据不写入仓库。
- 索引树只使用 Resolve Index 返回的精确具体索引，包含账号可访问的隐藏/关闭索引；别名、数据流及其 backing index 不作为本期可执行目标。系统索引可见性由服务端权限控制，不根据名称猜测并隐藏；普通账号的实际边界需验收。
- 支持 GET/POST 单索引 search/count、GET/HEAD 文档或 source、创建/替换/更新/删除文档、同步 update/delete by query，以及创建/删除索引、更新 Mapping/白名单 Settings、读取索引定义/Mapping/Settings/Alias。Alias 只读。Bulk/MSearch、SQL/PPL、集群/Security、向量/Neural/Hybrid、异步任务仍不开放。
- 第 5 步验证单次 REST 响应及行数/字节限制；第 6 步新增的自动 Scroll/PIT 分页另见[分页与导出验收](pagination-export.cn.md)。总命中与本次输出条数分别展示，不能据此声称已获取全部文档。

## 元数据与展示

| 编号 | 操作 | 预期 | 状态 |
| --- | --- | --- | --- |
| META-01 | 刷新索引树；准备普通、隐藏、关闭、空索引，以及 Alias、数据流；用普通账号与受限账号对照 | 返回精确索引并排序；按名称模式过滤；不把 Alias、数据流或其 backing index 当作具体索引；权限失败不可伪装为空列表 | NOT RUN |
| META-02 | 展开索引字段，读取详情；包含 object/nested、multi-field、alias、unsigned_long、flat_object、knn_vector，以及版本支持的 derived 字段 | 字段层级展开为完整路径；保留 Mapping、Settings、Alias 原始属性；类型属于 OpenSearch；未知类型保留原 Mapping；无字段索引不报错 | NOT RUN |
| META-03 | 运行模板中的 `GET /cdm_os_exec_case`、`/_mapping`、`/_settings`、`/_alias`；刷新字段/详情 | 完整 JSON 定义可展示；索引自身名为 error 等普通字段名时不误判为协议错误；关闭/权限不足有明确错误 | NOT RUN |
| META-04 | 在补全或批量字段加载期间修改 Mapping；随后刷新；模拟解析后目标被替换为别名 | 正确重读；预检查返回其他/多个索引时拒绝继续，不能扩大资源范围 | NOT RUN |

## 基础执行与终态

| 编号 | 操作 | 预期 | 状态 |
| --- | --- | --- | --- |
| EXEC-01 | GET/POST search 带 JSON body；普通条件、嵌套条件、排序、source 过滤、routing；中文/emoji/空格 ID | 请求方法、body 和参数准确传递，参数不转成 POST 表单；大整数与小数精度保留；source 中的 error、timed_out 等字段仅作用户数据 | NOT RUN |
| EXEC-02 | 查询空索引、count=0、size=0 聚合、空 buckets、null metric；聚合带普通 hits | 空结果仍有列元数据并正常完成；count 显示数值 0；聚合按名称/值输出 JSON，不丢弃空桶和 null；命中数与响应条数分开 | NOT RUN |
| EXEC-03 | POST 自动 ID、PUT 指定 ID 创建与替换、create-only、GET/HEAD、source-only、POST update、DELETE；重复删除与不存在文档 | created/updated/deleted 为 1，noop/not_found 为 0；缺失文档读取为空，HEAD 为 false；index-not-found/403/500 不伪装为缺失文档成功 | NOT RUN |
| EXEC-04 | 重复 create-only；以过期 seq_no/primary_term 更新或删除 | 409 明确报告冲突；执行/审计失败，不自动重试，不报告成功影响行数 | NOT RUN |
| EXEC-05 | update by query 分别执行更新、noop、`ctx.op='delete'`；delete by query 包含零命中 | 更新影响数为 updated + deleted，删除影响数为 deleted；不计入 noops、version_conflicts、total；保留原生执行摘要 | NOT RUN |
| EXEC-06 | by-query 制造版本冲突（abort/proceed）、批次失败、超时；文档写入制造副本分片失败 | 即使 HTTP 为 409/408/500，也保留结构化响应中的已确认影响数，结果记录及审计均为失败；conflicts=proceed 不被标为完整成功；说明已执行写入不会回滚 | NOT RUN |
| EXEC-07 | 创建索引、PUT Mapping、更新 number_of_replicas/refresh_interval 等白名单 Settings、删除索引、HEAD 不存在索引 | acknowledged=true 才完整成功；acknowledged/shards_acknowledged=false 为未完整确认；索引管理影响行数为 0，不当作文档计数 | NOT RUN |
| EXEC-08 | 模拟查询 timed_out、terminated_early、分片失败、错误/畸形 JSON、错误索引响应；代理返回非 JSON 403/500 | 不显示为空成功或完整查询；保留 HTTP 状态；错误通知使审计失败，结果接收端终结，会话恢复空闲 | NOT RUN |
| EXEC-09 | 分别触发行数限制、原始响应大小限制、输出字节限制，覆盖 STREAM/PAGINATED 展示；随后执行短查询 | 行数截断有提示；大小超限明确失败；释放连接，后续请求恢复；不把单次响应当作全量导出 | NOT RUN |
| EXEC-10 | 使用只读会话写入；查询别名/数据流；准备默认 Search Pipeline、默认/最终 Ingest Pipeline、semantic/percolator 等隐式资源配置 | 只读写请求、范围扩大及相应隐式资源操作在目标请求前拒绝；仅查看定义仍可用；不通过共享类特判放行 | NOT RUN |
| EXEC-11 | 在元数据预检查、真实请求、结果输出阶段分别取消/关闭；同会话并发执行；连接池设为 1 | 同会话第二个请求报告忙；取消阻止后续请求；真实连接释放；queryId/执行状态清理；新请求可执行；其他会话不受影响 | NOT RUN |
| EXEC-12 | 慢预检查叠加慢查询；断网、掉响应、多地址切换；写已送达但响应丢失 | 整个命令受同一总期限约束；命令请求不重放；结果未知时报告失败，人工回读确认；恢复后可继续；HTTP 取消不代表服务端写任务停止 | NOT RUN |
| EXEC-13 | 开启字段脱敏后查询 source；查询聚合或无法安全脱敏的扩展输出；结合授权引用索引 | 使用既有 JSON 文档处理链；聚合等不支持的脱敏形态明确拒绝；跨索引资源全部纳入授权检查，不能漏报 | NOT RUN |
| EXEC-14 | 对照 ES 运行同类查询、元数据、写入与分页流程 | ES 现有行为不变；两产品驱动独立加载，OpenSearch 插件不依赖 ds-es | NOT RUN |

## 验收样例

在专用索引执行，ID 使用 `demo-1`。每条命令分别核对结果及审计状态：

```text
PUT /cdm_os_exec_case
{"settings":{"number_of_shards":1,"number_of_replicas":0},"mappings":{"properties":{"amount":{"type":"long"},"status":{"type":"keyword"}}}}

PUT /cdm_os_exec_case/_doc/demo-1?refresh=true
{"amount":9007199254740993,"status":"new","note":"中文 😀"}

GET /cdm_os_exec_case/_search
{"size":20,"query":{"match_all":{}},"aggs":{"by_status":{"terms":{"field":"status"}}}}

POST /cdm_os_exec_case/_update/demo-1?refresh=true
{"doc":{"status":"paid"}}

POST /cdm_os_exec_case/_update_by_query?refresh=true
{"query":{"term":{"status":"paid"}},"script":{"source":"ctx.op = 'delete'"}}

GET /cdm_os_exec_case/_count

DELETE /cdm_os_exec_case/_doc/demo-1

HEAD /cdm_os_exec_case/_doc/demo-1
```

这里第三条返回大整数文档及聚合；按查询脚本删除的影响数应为 1，随后 count 为 0，重复 DELETE 的影响数为 0，最后 HEAD 为 false。冲突/部分失败、受限账号、分页和故障条件需另按表格准备，不能由该成功路径替代。

## 自动化与清理

自动化入口：`cd backend && ./gradlew :ds-opensearch:test :sql-es:test :ds-es:compileJava :ds-opensearch:customFatJar --console=plain`。测试采用 JUnit、进程内回环 HTTP 模拟响应和真实 HC5 适配，覆盖结果输出、计数、资源预检查、总期限、取消、断连、不重放及恢复；不启动 CloudDM 或使用真实 OpenSearch。这不能代替跨版本协议、TLS、普通账号、实际插件类加载和页面验收。

验收后删除专用索引、别名、数据流、Pipeline、临时权限，关闭会话，清理抓包和本地凭据。结果持续回填本文件及能力矩阵，不新建按日期命名的报告。

协议依据：[Resolve Index](https://docs.opensearch.org/latest/api-reference/index-apis/resolve-index/)、[OpenSearch 字段类型](https://docs.opensearch.org/latest/mappings/supported-field-types/index/)、[Update by Query](https://docs.opensearch.org/latest/api-reference/document-apis/update-by-query/)、[默认 Search Pipeline](https://docs.opensearch.org/latest/search-plugins/search-pipelines/using-search-pipeline/)。

非 2xx 部分失败的状态与计数约定见 [BulkIndexByScrollResponseContentListener](https://github.com/opensearch-project/OpenSearch/blob/main/modules/reindex/src/main/java/org/opensearch/index/reindex/BulkIndexByScrollResponseContentListener.java)。
