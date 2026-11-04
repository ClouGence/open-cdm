# OpenSearch 分页、结果展示与导出验收

第 6 步长期流程。插件代码与本地协议模拟测试不代表真实集群或页面验收；下表全部为 `NOT RUN`。待开发完成后由用户启动 CloudDM，统一验证，不在本步骤执行数据库升级。

## 实现边界

- 有效 size 大于 1000 且符合保守分页条件时，OpenSearch 2.x、3.0～3.2 使用 Scroll，3.3+ 使用 PIT + search_after。有效上限遵循 URL size 优先于 body size，再与平台行数限制取较小值；不擅自把未指定 size 的查询扩展为全量。平台已有的 limit 改写仍先执行。
- 共用 `EsSearchPagination` 的形状判断。聚合、from（含显式 0）、已有 search_after、URL sort、`_doc` 排序、脚本/随机评分、相对时间、named query、inner_hits、外部文档引用等不自动分页，保留原生单次执行及其服务端限制。Scroll 还排除显式关闭或限制 track_total_hits 的查询。
- Scroll 保留原请求排序，不为了性能改成 `_doc`。PIT 保留显式排序；缺少排序时使用 `_score desc`，再追加 `_shard_doc asc`，已有 `_shard_doc` 不重复追加。只有自动添加的排序值从输出移除，不能把同分文档的跨次查询顺序当作稳定契约。
- 分页使用一个结果 ID、固定列元数据、统一缓存和累计字节计数。PAGINATED 后续静默行仍交给既有结果构建器缓存；STREAM/PAGE_FULL 沿用原行为。缓存经既有文件转换服务导出，不新增公共类产品分支。
- 每次分页只使用自己的最新游标 ID，keep_alive 为 1 分钟；不重试失败页、不重建快照、不从失败的 PIT 自动切换 Scroll。不按 `_id` 判重，自定义 routing 下不同分片允许同 ID 文档；Scroll 依赖服务端游标推进，PIT 保留相邻完整排序值重复检查。分片失败、PIT 相邻排序值重复、已知精确总数下提前结束均报告失败，不静默去重或假装完整结束。
- 完成、行数截断、异常、取消和关闭均进入游标清理。清理有独立 5 秒期限，不被重复取消打断；清理失败会导致失败终态，已有主异常时保留清理异常为 suppressed。会话关闭后最多给传输层保留 5 秒清理窗口，防止结果输出阻塞导致连接/线程无限保留。
- 连接中断、响应过大或取消导致从未收到游标 ID 时，不能安全地猜 ID 或清理全局游标；服务端依靠 keep_alive 过期回收。关闭窗口耗尽也可能留下等待过期的服务端上下文，必须记录清理未确认，不能宣称全部即时释放。

## 准备与判定方法

1. 沿用[能力矩阵](opensearch-test-matrix.md)，至少覆盖 2.x、3.2、3.3、3.9；记录真实版本、插件包、账号权限、平台限制和结果。数据集、账号权限使用独立测试环境。
2. 在外部管理工具建立专用索引 `cdm_os_page_case`，至少 2 个主分片；准备 12037 条文档，超过默认 result window。使用可核对的唯一 ID `row-00000` 至 `row-12036`，同时保存基线 ID 清单。准备 `seq`（唯一递增 long）、`rank`（重复整数）、相同全文文本、含空值的排序字段、中文/emoji、长字符串及 `9007199254740993` 等大整数。Bulk 仅用于外部数据准备，不表示本期编辑器开放 Bulk。
3. 准备测试账号的精确索引读取和相应 Scroll clear / PIT create、search、delete 权限；另备缺少创建或清理权限的账号。不得把连接凭据、CA、游标 ID、抓包或原始业务数据提交到仓库。
4. 平台行数限制至少 15000、响应/结果大小上限足够容纳样本，查询总期限足以完成测试；记录真实取值。展示页大小设为小于内部页大小，例如 20。
5. 核对“不重复、不遗漏”须同时满足：输出行数 = 唯一 ID 数 = 基线 ID 数，且双向 ID 集合差为空。不要仅检查 count 或截图。显式排序还需逐行验证主排序值单调，空值位置与原生排序一致；同分文档只要求本次快照不重不漏。
6. 为并发写入用例，在抓到首个 Scroll 响应或 PIT 创建成功后暂停下一次请求，再由外部工具新增 50 条、修改 50 条、删除 50 条并 refresh。恢复读取后应严格对应建立快照时的 ID 和 source 内容，不能把新文档混入、漏掉被删除文档或读取更新后的 source。

## 验收用例

| 编号 | 操作 | 预期 | 状态 |
| --- | --- | --- | --- |
| PAGE-01 | 在 2.x、3.0/3.2 运行 `GET /cdm_os_page_case/_search`，body 为 `{"size":15000,"query":{"match_all":{}}}` | 首次单索引 search 带 scroll；续页 POST /_search/scroll；直到空页，共 12037 条，无重无漏；只 DELETE 最新 scroll_id | NOT RUN |
| PAGE-02 | 在 3.3、3.9 执行同一查询 | 创建该索引 PIT，allow_partial_pit_creation=false；后续无索引路径的 search 使用同一快照及最新 PIT ID；默认 `_score desc, _shard_doc asc`；完整读取后清理本次 PIT | NOT RUN |
| PAGE-03 | 使用大量同分文本、重复 rank；分别执行默认排序、rank 升/降序、多字段混合升降序、带缺失值/nested/geo 的已支持排序 | 保持原生主排序语义，多分片同分文档不重不漏；PIT 不用 `_shard_doc` 替换用户排序；无显式排序时输出不泄漏自动添加的 sort | NOT RUN |
| PAGE-04 | 显式传 `_shard_doc`，以及一个对象内多个 sort 字段；使用大整数/日期 sort 值 | 不重复追加已有稳定键；search_after 的顺序、类型、精度与上页末行一致；用户显式 sort 数组保留，内部追加值隐藏 | NOT RUN |
| PAGE-05 | 分别在 Scroll/PIT 建立后执行准备步骤中的并发新增/更新/删除 | 输出与建快照时的 ID、source 内容一致；完整通过集合差与排序断言；在新查询中才看到并发变化 | NOT RUN |
| PAGE-06 | 带 routing/preference；对照相同条件的原生查询 | PIT 创建携带分片选择条件，后续 search 不重复携带；Scroll 初次携带、续页仅游标；不会扩展为全局查询 | NOT RUN |
| PAGE-07 | body size=15000、URL size=2205、平台行数上限=1205；再测恰好 1000、1001、整页边界及空索引 | 有效上限正确；Scroll 最后一批只输出剩余行；PIT 缩小最后一页；截断即清理；空索引有列元数据、零行成功 | NOT RUN |
| PAGE-08 | 聚合、from、URL sort、`_doc`、已有 search_after、脚本/随机评分、now 时间、同/跨索引 lookup；旧版 track_total_hits=false/整数 | 不自动套游标，不篡改原始语义；超过原生 result window 按原生错误返回，不能标为已完成全量分页 | NOT RUN |
| PAGE-09 | PAGINATED 展示 20 行后等待后台完成，逐页查看中间页/末页；切换 STREAM/PAGE_FULL | 单一结果集持续更新行数；后台页不会成为多个结果集；缓存包含静默写入的全部行，列结构与空值一致 | NOT RUN |
| PAGE-10 | 查询完成后从同一结果缓存导出当前支持的 CSV/JSON/Excel 等格式（按实际可用格式记录），下载并解析 | 文件行数、唯一 ID、source 与结果缓存一致；大整数、中文/emoji、null、长文本及排序值按既有格式契约保留；不只导出页面可见行，不把内部 PIT 排序值写入文件 | NOT RUN |
| PAGE-11 | 启用脱敏后分页查询并导出；返回隐藏的 sort 或不支持的扩展 hit 字段 | source 通过既有 JSON 文档脱敏链；敏感 sort 不暴露；不能安全合并的字段明确失败，不能悄悄丢列或导出原始敏感值 | NOT RUN |
| PAGE-12 | 在第二页以后触发累计输出字节限制、单页原始响应上限、列/元素上限或缓存写失败 | 跨页累计限制生效；结果和审计为失败；已缓存部分行不得表示完整结果；游标清理，连接可复用 | NOT RUN |
| PAGE-13 | 让建 PIT 部分分片失败、查询页分片失败/超时、PIT 页内或跨页相邻完整排序值重复、返回畸形/越界索引结果、在精确 total 前返回空页 | 已返回的游标 ID 仍被清理；错误页不输出；不重试/重建/自动降级；终态为失败并保留已输出数量 | NOT RUN |
| PAGE-14 | 在创建完成、响应解码、在途续页、结果写入、用户限制截断时取消或关闭；清理期间再次取消 | 最新已知 ID 被清理；单连接池不被占死；取消后不再发下一页；queryId/执行状态恢复；其他会话的游标不受影响 | NOT RUN |
| PAGE-15 | 续页故障叠加清理故障；或所有行已读完但清理返回 unsuccessful/超时/断网 | 保留主错误及 suppressed 清理错误；后者单独发生时结果仍为失败；没有“读完即成功”掩盖清理失败 | NOT RUN |
| PAGE-16 | 输出回调阻塞超过 5 秒后关闭会话；或在创建/续页响应 ID 收到前断网；观察服务端上下文数 | 本地连接/调度器有界关闭；无法确认清理的服务端上下文随 keep_alive 过期回收；不发送 `_all` 清理，不影响其他会话 | NOT RUN |
| PAGE-17 | 查询总期限在第二页耗尽；单页连接/响应超时；PIT 创建/清理缺权，游标过期 | 清理不受已耗尽的查询期限阻止；不能自动换快照、静默少行或降级；短查询随后可用；实际账号权限不足有明确错误 | NOT RUN |
| PAGE-18 | 同一页面重复执行、重开会话，并回归 ES 分页、缓存读取及导出 | OpenSearch/ES 不共享游标；无跨查询残留；ES 行为不变；结果与资源清理在实际插件/驱动类加载下正确 | NOT RUN |
| PAGE-19 | 在隔离的双分片索引中，通过 `_search_shards` 确认两组 routing 落到不同分片；用相同 `_id`、不同 routing 写入正文 `seq` 不同的文档。按 `seq` 排序并设置 size 大于 1000，分别安排同 ID 文档位于同页及第 1000/1001 条，覆盖 Scroll/PIT | 同 ID 文档全部保留，不报游标未推进；以基线 `seq` 清单核对展示及导出，无重复遗漏；查询终态成功且游标清理完成 | NOT RUN |

## 代码与自动化证据

- `OpenSearchSession` 在授权与元数据预检查后选择分页；`OpenSearchSearchCursor` 管理快照、精确续页及清理；`OpenSearchResultWriter` 按单个结果集持续写出、累计限制并通知进度。
- 现有 `ImplResultSetRowsBuild.receiveRow(boolean, Map)` 即使 silent=true 也向 ResultStorage 提交行；`ResultSetRServiceProvider` 使用已有 FileFormatConvert 从缓存转换文件。本步不修改这些公共类，真实缓存文件与下载结果按 PAGE-09/10 验证。
- 单元测试使用真实 HC5 与进程内协议模拟，检查请求、页边界、输出回调、终态与资源释放；模拟多分片排序值及快照变更不能替代 PAGE-03/05 的实库断言。
- 命令：`cd backend && ./gradlew :ds-opensearch:test :sql-es:test :ds-es:compileJava :ds-opensearch:customFatJar --console=plain`。具体通过数量记录在[能力矩阵](opensearch-test-matrix.md)。

## 清理与依据

验收后停止并发写入，关闭测试会话，确认本次 Scroll/PIT 清理或到期回收，删除专用索引与临时权限，清理导出文件、抓包及本地凭据。保留本流程并回填状态，不生成按日期命名的重复报告。

协议依据：[Scroll API](https://docs.opensearch.org/latest/api-reference/search-apis/scroll/)、[PIT API](https://docs.opensearch.org/latest/api-reference/search-apis/point-in-time-api/)、[分页与快照](https://docs.opensearch.org/latest/search-plugins/searching-data/paginate/)、[排序语义](https://docs.opensearch.org/latest/search-plugins/searching-data/sort/)。
